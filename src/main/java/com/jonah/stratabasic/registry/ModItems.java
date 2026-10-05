package com.jonah.stratabasic.registry;

import com.jonah.stratabasic.StrataBasic;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(StrataBasic.MODID);

    // Block items are registered with their blocks (see ModBlocks and StoneSets)
    public static final Supplier<Item> DEEPER_CREEPER_SPAWN_EGG =
            ITEMS.register("deeper_creeper_spawn_egg", () ->
                    new DeferredSpawnEggItem(ModEntities.DEEPER_CREEPER, 0x17171A, 0x66666E, new Item.Properties()));
    public static final Supplier<Item> LITHOSLATE_CREEPER_SPAWN_EGG =
            ITEMS.register("lithoslate_creeper_spawn_egg", () ->
                    new DeferredSpawnEggItem(ModEntities.LITHOSLATE_CREEPER, 0x2A1416, 0xB0382E, new Item.Properties()));
    public static final Supplier<Item> MANTLESLATE_CREEPER_SPAWN_EGG =
            ITEMS.register("mantleslate_creeper_spawn_egg", () ->
                    new DeferredSpawnEggItem(ModEntities.MANTLESLATE_CREEPER, 0x2E1210, 0xD2502A, new Item.Properties()));
    public static final Supplier<Item> VOID_CREEPER_SPAWN_EGG =
            ITEMS.register("void_creeper_spawn_egg", () ->
                    new DeferredSpawnEggItem(ModEntities.VOID_CREEPER, 0x0C0A14, 0x9040E0, new Item.Properties()));
}
