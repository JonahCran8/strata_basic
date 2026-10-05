package com.jonah.stratabasic;

import com.jonah.stratabasic.registry.ModBiomeSources;
import com.jonah.stratabasic.registry.ModBlocks;
import com.jonah.stratabasic.registry.ModEntities;
import com.jonah.stratabasic.registry.ModFeatures;
import com.jonah.stratabasic.registry.ModItems;
import com.jonah.stratabasic.registry.ModStructures;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(StrataBasic.MODID)
public class StrataBasic {

    public static final String MODID = "strata_basic";

    public StrataBasic(IEventBus modBus) {
        ModItems.ITEMS.register(modBus);
        ModBlocks.BLOCKS.register(modBus);
        ModBlocks.registerAll();
        ModEntities.ENTITY_TYPES.register(modBus);
        ModFeatures.FEATURES.register(modBus);
        ModBiomeSources.BIOME_SOURCES.register(modBus);
        ModStructures.STRUCTURE_TYPES.register(modBus);
    }
}
