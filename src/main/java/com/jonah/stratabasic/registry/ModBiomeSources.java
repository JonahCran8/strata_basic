package com.jonah.stratabasic.registry;

import com.jonah.stratabasic.StrataBasic;
import com.jonah.stratabasic.worldgen.LayeredBiomeSource;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.biome.BiomeSource;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBiomeSources {

    public static final DeferredRegister<MapCodec<? extends BiomeSource>> BIOME_SOURCES =
            DeferredRegister.create(Registries.BIOME_SOURCE, StrataBasic.MODID);

    // Overworld biomes with nether biomes below a certain height (see worldgen/LayeredBiomeSource)
    public static final DeferredHolder<MapCodec<? extends BiomeSource>, MapCodec<LayeredBiomeSource>> LAYERED =
            BIOME_SOURCES.register("layered", () -> LayeredBiomeSource.CODEC);
}
