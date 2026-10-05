package com.jonah.stratabasic.client;

import com.jonah.stratabasic.StrataBasic;
import com.jonah.stratabasic.registry.ModEntities;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = StrataBasic.MODID, value = Dist.CLIENT)
public class EntityRenderSetup {

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.DEEPER_CREEPER.get(), context -> new DeepCreeperRenderer(context, "deeper_creeper"));
        event.registerEntityRenderer(ModEntities.LITHOSLATE_CREEPER.get(), context -> new DeepCreeperRenderer(context, "lithoslate_creeper"));
        event.registerEntityRenderer(ModEntities.MANTLESLATE_CREEPER.get(), context -> new DeepCreeperRenderer(context, "mantleslate_creeper"));
        event.registerEntityRenderer(ModEntities.VOID_CREEPER.get(), context -> new DeepCreeperRenderer(context, "void_creeper"));
    }
}
