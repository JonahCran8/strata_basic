package com.jonah.stratabasic.registry;

import com.jonah.stratabasic.StrataBasic;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

import java.util.List;
import java.util.function.Supplier;

// Puts items next to the vanilla ones they go with
@EventBusSubscriber(modid = StrataBasic.MODID)
public class ModCreativeTabs {

    private static final CreativeModeTab.TabVisibility VISIBILITY = CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS;

    @SubscribeEvent
    public static void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS) {
            after(event, Items.DEEPSLATE, items(ModBlocks.STONE_ITEMS));
            ModBlocks.ORE_ITEMS.forEach((ore, variants) -> {
                Item anchor = net.minecraft.core.registries.BuiltInRegistries.ITEM.get(
                        net.minecraft.resources.ResourceLocation.withDefaultNamespace("deepslate_" + ore + "_ore"));
                after(event, anchor, items(variants));
            });
        }
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            after(event, Items.DEEPSLATE_TILE_WALL, StoneSets.items().values().stream()
                    .flatMap(List::stream).map(item -> (Item) item.get()).toArray(Item[]::new));
        }
        if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
            after(event, Items.CREEPER_SPAWN_EGG, ModItems.DEEPER_CREEPER_SPAWN_EGG.get(), ModItems.LITHOSLATE_CREEPER_SPAWN_EGG.get(),
                    ModItems.MANTLESLATE_CREEPER_SPAWN_EGG.get(), ModItems.VOID_CREEPER_SPAWN_EGG.get());
        }
    }

    private static Item[] items(List<? extends Supplier<? extends Item>> list) {
        return list.stream().map(item -> (Item) item.get()).toArray(Item[]::new);
    }

    // Puts items after vanilla one in order, or at end if it isn't in this tab
    private static void after(BuildCreativeModeTabContentsEvent event, Item anchor, Item... items) {
        if (!event.getParentEntries().contains(new ItemStack(anchor))) {
            for (Item item : items) {
                event.accept(item);
            }
            return;
        }
        Item previous = anchor;
        for (Item item : items) {
            event.insertAfter(new ItemStack(previous), new ItemStack(item), VISIBILITY);
            previous = item;
        }
    }
}
