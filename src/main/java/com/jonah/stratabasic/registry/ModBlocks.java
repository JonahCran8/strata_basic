package com.jonah.stratabasic.registry;

import com.jonah.stratabasic.StrataBasic;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.RedStoneOreBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

// Layer stones and their ores, what's made from them is in StoneSets
public class ModBlocks {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(StrataBasic.MODID);

    private record Layer(String name, MapColor color) {}

    private static final List<Layer> LAYERS = List.of(
            new Layer("lithoslate", MapColor.TERRACOTTA_RED),
            new Layer("mantleslate", MapColor.COLOR_BLACK),
            new Layer("ferrite", MapColor.CRIMSON_NYLIUM));
    public static final List<String> ORES = List.of("coal", "iron", "copper", "gold", "redstone", "lapis", "diamond", "emerald");

    // For creative menu: stones and each ore's variants
    public static final List<DeferredItem<BlockItem>> STONE_ITEMS = new ArrayList<>();
    public static final Map<String, List<DeferredItem<BlockItem>>> ORE_ITEMS = new LinkedHashMap<>();

    public static void registerAll() {
        for (Layer layer : LAYERS) {
            DeferredBlock<Block> stone = BLOCKS.registerBlock(layer.name(), RotatedPillarBlock::new,
                    BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE).mapColor(layer.color()));
            STONE_ITEMS.add(ModItems.ITEMS.registerSimpleBlockItem(layer.name(), stone));

            for (String ore : ORES) {
                Block vanilla = BuiltInRegistries.BLOCK.get(ResourceLocation.withDefaultNamespace("deepslate_" + ore + "_ore"));
                Function<BlockBehaviour.Properties, Block> factory = switch (ore) {
                    case "coal" -> props -> new DropExperienceBlock(UniformInt.of(0, 2), props);
                    case "lapis" -> props -> new DropExperienceBlock(UniformInt.of(2, 5), props);
                    case "diamond", "emerald" -> props -> new DropExperienceBlock(UniformInt.of(3, 7), props);
                    case "redstone" -> RedStoneOreBlock::new;
                    default -> Block::new;
                };
                String name = layer.name() + "_" + ore + "_ore";
                DeferredBlock<Block> block = BLOCKS.registerBlock(name, factory, BlockBehaviour.Properties.ofFullCopy(vanilla).mapColor(layer.color()));
                ORE_ITEMS.computeIfAbsent(ore, key -> new ArrayList<>()).add(ModItems.ITEMS.registerSimpleBlockItem(name, block));
            }
        }
        StoneSets.register();
    }
}
