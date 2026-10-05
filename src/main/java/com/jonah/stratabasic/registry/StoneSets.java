package com.jonah.stratabasic.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// Everything deepslate makes for each layer stone, names are deepslate's with deepslate replaced
// (cobbled_deepslate_slab -> cobbled_lithoslate_slab)
public class StoneSets {

    private enum Type { BLOCK, SLAB, STAIRS, WALL }

    // Vanilla name, type and base block for slabs, stairs and walls (base comes first)
    private record Kind(String vanilla, Type type, String base) {
        static Kind block(String vanilla) {
            return new Kind(vanilla, Type.BLOCK, null);
        }
    }

    private static final List<Kind> KINDS = List.of(
            Kind.block("cobbled_deepslate"),
            new Kind("cobbled_deepslate_slab", Type.SLAB, "cobbled_deepslate"),
            new Kind("cobbled_deepslate_stairs", Type.STAIRS, "cobbled_deepslate"),
            new Kind("cobbled_deepslate_wall", Type.WALL, "cobbled_deepslate"),
            Kind.block("polished_deepslate"),
            new Kind("polished_deepslate_slab", Type.SLAB, "polished_deepslate"),
            new Kind("polished_deepslate_stairs", Type.STAIRS, "polished_deepslate"),
            new Kind("polished_deepslate_wall", Type.WALL, "polished_deepslate"),
            Kind.block("deepslate_bricks"),
            new Kind("deepslate_brick_slab", Type.SLAB, "deepslate_bricks"),
            new Kind("deepslate_brick_stairs", Type.STAIRS, "deepslate_bricks"),
            new Kind("deepslate_brick_wall", Type.WALL, "deepslate_bricks"),
            Kind.block("cracked_deepslate_bricks"),
            Kind.block("deepslate_tiles"),
            new Kind("deepslate_tile_slab", Type.SLAB, "deepslate_tiles"),
            new Kind("deepslate_tile_stairs", Type.STAIRS, "deepslate_tiles"),
            new Kind("deepslate_tile_wall", Type.WALL, "deepslate_tiles"),
            Kind.block("cracked_deepslate_tiles"),
            Kind.block("chiseled_deepslate")
    );

    private static final Map<String, DeferredBlock<? extends Block>> BLOCKS = new LinkedHashMap<>();
    // Each stone's items in creative menu order
    private static final Map<String, List<DeferredItem<BlockItem>>> ITEMS = new LinkedHashMap<>();

    // Called from mod constructor after ModBlocks and ModItems have their registers
    public static void register() {
        family("lithoslate", MapColor.TERRACOTTA_RED);
        family("mantleslate", MapColor.COLOR_BLACK);
        family("ferrite", MapColor.CRIMSON_NYLIUM);
    }

    private static void family(String stone, MapColor color) {
        List<DeferredItem<BlockItem>> items = new ArrayList<>();
        for (Kind kind : KINDS) {
            String name = kind.vanilla().replace("deepslate", stone);
            Block vanilla = BuiltInRegistries.BLOCK.get(ResourceLocation.withDefaultNamespace(kind.vanilla()));
            BlockBehaviour.Properties properties = BlockBehaviour.Properties.ofFullCopy(vanilla).mapColor(color);
            DeferredBlock<? extends Block> base = kind.base() == null ? null : BLOCKS.get(kind.base().replace("deepslate", stone));

            DeferredBlock<? extends Block> block = switch (kind.type()) {
                case BLOCK -> ModBlocks.BLOCKS.registerBlock(name, Block::new, properties);
                case SLAB -> ModBlocks.BLOCKS.registerBlock(name, SlabBlock::new, properties);
                case STAIRS -> ModBlocks.BLOCKS.registerBlock(name, props -> new StairBlock(base.get().defaultBlockState(), props), properties);
                case WALL -> ModBlocks.BLOCKS.registerBlock(name, WallBlock::new, properties);
            };
            BLOCKS.put(name, block);
            items.add(ModItems.ITEMS.registerSimpleBlockItem(name, block));
        }
        ITEMS.put(stone, items);
    }

    public static Block block(String name) {
        return BLOCKS.get(name).get();
    }

    public static Map<String, List<DeferredItem<BlockItem>>> items() {
        return ITEMS;
    }
}
