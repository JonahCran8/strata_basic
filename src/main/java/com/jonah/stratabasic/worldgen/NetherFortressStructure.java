package com.jonah.stratabasic.worldgen;

import com.jonah.stratabasic.registry.ModStructures;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.structures.NetherFortressPieces;
import net.neoforged.fml.ModList;

import java.util.List;
import java.util.Optional;

// Moves vanilla nether fortresses down to bottom of overworld
public class NetherFortressStructure extends Structure {

    public static final MapCodec<NetherFortressStructure> CODEC = simpleCodec(NetherFortressStructure::new);

    private static final int SHIFT = 528;

    public NetherFortressStructure(Structure.StructureSettings settings) {
        super(settings);
    }

    @Override
    public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
        // YUNG's Better Nether Fortresses replaces these (see data/betterfortresses override)
        if (ModList.get().isLoaded("betterfortresses")) {
            return Optional.empty();
        }

        ChunkPos chunkPos = context.chunkPos();
        BlockPos pos = new BlockPos(chunkPos.getMinBlockX(), 64 - SHIFT, chunkPos.getMinBlockZ());
        return Optional.of(new Structure.GenerationStub(pos, builder -> generatePieces(builder, context)));
    }

    private static void generatePieces(StructurePiecesBuilder builder, Structure.GenerationContext context) {
        NetherFortressPieces.StartPiece start = new NetherFortressPieces.StartPiece(
                context.random(), context.chunkPos().getBlockX(2), context.chunkPos().getBlockZ(2));
        builder.addPiece(start);
        start.addChildren(start, builder, context.random());
        List<StructurePiece> pending = start.pendingChildren;

        while (!pending.isEmpty()) {
            int index = context.random().nextInt(pending.size());
            StructurePiece piece = pending.remove(index);
            piece.addChildren(start, builder, context.random());
        }

        builder.moveInsideHeights(context.random(), 48 - SHIFT, 70 - SHIFT);
    }

    @Override
    public StructureType<?> type() {
        return ModStructures.NETHER_FORTRESS.get();
    }
}
