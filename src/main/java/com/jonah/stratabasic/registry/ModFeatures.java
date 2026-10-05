package com.jonah.stratabasic.registry;

import com.jonah.stratabasic.StrataBasic;
import com.jonah.stratabasic.worldgen.LavaReducerFeature;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

// Custom world generation features (placed in data)
public class ModFeatures {

    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, StrataBasic.MODID);

    // Finishes nether terrain (see LavaReducerFeature)
    public static final DeferredHolder<Feature<?>, LavaReducerFeature> LAVA_REDUCER =
            FEATURES.register("lava_reducer", () -> new LavaReducerFeature(NoneFeatureConfiguration.CODEC));
}
