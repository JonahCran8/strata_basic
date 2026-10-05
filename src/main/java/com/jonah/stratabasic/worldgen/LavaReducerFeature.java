package com.jonah.stratabasic.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

// Finishes nether terrain: reruns surface rules and turns leftover stone into netherrack
// Placed first in every chunk, before nether ores and fortress
public class LavaReducerFeature extends Feature<NoneFeatureConfiguration> {

    // Nether roof
    private static final int NETHER_TOP = -400;

    public LavaReducerFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        // Chunk's corner at bottom of world
        BlockPos origin = context.origin();
        ChunkAccess chunk = level.getChunk(origin);
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        boolean changed = false;

        // Floors are under air now so surface rules work, run them again (only touches plain stone)
        if (level instanceof WorldGenRegion region) {
            ServerLevel serverLevel = region.getLevel();
            serverLevel.getChunkSource().getGenerator().buildSurface(region, serverLevel.structureManager(),
                    serverLevel.getChunkSource().randomState(), chunk);
        }

        // Rest of the stone in nether becomes netherrack
        BlockState netherrack = Blocks.NETHERRACK.defaultBlockState();
        for (int index = 0; index < chunk.getSectionsCount(); index++) {
            int baseY = chunk.getSectionYFromSectionIndex(index) * 16;
            if (baseY >= NETHER_TOP || !chunk.getSection(index).maybeHas(state -> state.is(Blocks.STONE))) {
                continue;
            }
            for (int dy = 0; dy < 16 && baseY + dy < NETHER_TOP; dy++) {
                for (int dx = 0; dx < 16; dx++) {
                    for (int dz = 0; dz < 16; dz++) {
                        pos.set(origin.getX() + dx, baseY + dy, origin.getZ() + dz);
                        if (level.getBlockState(pos).is(Blocks.STONE)) {
                            level.setBlock(pos, netherrack, 2);
                            changed = true;
                        }
                    }
                }
            }
        }
        return changed;
    }
}
