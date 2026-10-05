package com.jonah.stratabasic.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

// Overworld biomes with nether biomes below a certain height
// lower_scale shrinks the lower biomes (climate looked up at that many times the coordinates)
// replacements swaps one biome for another
public class LayeredBiomeSource extends BiomeSource {

    public static final MapCodec<LayeredBiomeSource> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BiomeSource.CODEC.fieldOf("delegate").forGetter(source -> source.delegate),
            BiomeSource.CODEC.fieldOf("lower").forGetter(source -> source.lower),
            Codec.INT.fieldOf("below_y").forGetter(source -> source.belowY),
            Codec.intRange(1, 64).optionalFieldOf("lower_scale", 1).forGetter(source -> source.lowerScale),
            Replacement.CODEC.listOf().optionalFieldOf("replacements", List.of()).forGetter(source -> source.replacements)
    ).apply(instance, LayeredBiomeSource::new));

    public record Replacement(Holder<Biome> from, Holder<Biome> to) {
        public static final Codec<Replacement> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Biome.CODEC.fieldOf("from").forGetter(Replacement::from),
                Biome.CODEC.fieldOf("to").forGetter(Replacement::to)
        ).apply(instance, Replacement::new));
    }

    private final BiomeSource delegate;
    private final BiomeSource lower;
    private final int belowY;
    private final int lowerScale;
    private final List<Replacement> replacements;
    private final Map<ResourceKey<Biome>, Holder<Biome>> replaced = new HashMap<>();

    public LayeredBiomeSource(BiomeSource delegate, BiomeSource lower, int belowY, int lowerScale, List<Replacement> replacements) {
        this.delegate = delegate;
        this.lower = lower;
        this.belowY = belowY;
        this.lowerScale = lowerScale;
        this.replacements = replacements;
        for (Replacement replacement : replacements) {
            replacement.from().unwrapKey().ifPresent(key -> replaced.put(key, replacement.to()));
        }
    }

    @Override
    protected MapCodec<? extends BiomeSource> codec() {
        return CODEC;
    }

    @Override
    protected Stream<Holder<Biome>> collectPossibleBiomes() {
        Stream<Holder<Biome>> upper = delegate.possibleBiomes().stream()
                .filter(biome -> biome.unwrapKey().map(key -> !replaced.containsKey(key)).orElse(true));
        return Stream.concat(upper, lower.possibleBiomes().stream());
    }

    @Override
    public Holder<Biome> getNoiseBiome(int x, int y, int z, Climate.Sampler sampler) {
        // y is in quarts (4 blocks)
        if (QuartPos.toBlock(y) < belowY) {
            // Overworld climate is stretched out, so ask lower source further out to shrink its biomes
            return lower.getNoiseBiome(x * lowerScale, y, z * lowerScale, sampler);
        }
        Holder<Biome> biome = delegate.getNoiseBiome(x, y, z, sampler);
        if (replaced.isEmpty()) {
            return biome;
        }
        Optional<ResourceKey<Biome>> key = biome.unwrapKey();
        return key.isPresent() && replaced.containsKey(key.get()) ? replaced.get(key.get()) : biome;
    }
}
