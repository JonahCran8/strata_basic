package com.jonah.stratabasic.registry;

import com.jonah.stratabasic.StrataBasic;
import com.jonah.stratabasic.entity.DeeperCreeper;
import com.jonah.stratabasic.entity.LithoslateCreeper;
import com.jonah.stratabasic.entity.MantleslateCreeper;
import com.jonah.stratabasic.entity.VoidCreeper;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, StrataBasic.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<DeeperCreeper>> DEEPER_CREEPER =
            ENTITY_TYPES.register("deeper_creeper", () -> EntityType.Builder.of(DeeperCreeper::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.7F).clientTrackingRange(8).build("deeper_creeper"));

    public static final DeferredHolder<EntityType<?>, EntityType<LithoslateCreeper>> LITHOSLATE_CREEPER =
            ENTITY_TYPES.register("lithoslate_creeper", () -> EntityType.Builder.of(LithoslateCreeper::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.7F).clientTrackingRange(8).build("lithoslate_creeper"));

    public static final DeferredHolder<EntityType<?>, EntityType<MantleslateCreeper>> MANTLESLATE_CREEPER =
            ENTITY_TYPES.register("mantleslate_creeper", () -> EntityType.Builder.of(MantleslateCreeper::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.7F).clientTrackingRange(8).build("mantleslate_creeper"));

    public static final DeferredHolder<EntityType<?>, EntityType<VoidCreeper>> VOID_CREEPER =
            ENTITY_TYPES.register("void_creeper", () -> EntityType.Builder.of(VoidCreeper::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.7F).clientTrackingRange(8).build("void_creeper"));
}
