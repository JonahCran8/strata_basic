package com.jonah.stratabasic.registry;

import com.jonah.stratabasic.StrataBasic;
import com.jonah.stratabasic.worldgen.NetherFortressStructure;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModStructures {

    public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPES = DeferredRegister.create(Registries.STRUCTURE_TYPE, StrataBasic.MODID);

    // Vanilla nether fortress moved down (see NetherFortressStructure)
    public static final DeferredHolder<StructureType<?>, StructureType<NetherFortressStructure>> NETHER_FORTRESS =
            STRUCTURE_TYPES.register("nether_fortress", () -> () -> NetherFortressStructure.CODEC);
}
