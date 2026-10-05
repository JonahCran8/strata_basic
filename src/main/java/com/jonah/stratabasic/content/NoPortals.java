package com.jonah.stratabasic.content;

import com.jonah.stratabasic.StrataBasic;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityTravelToDimensionEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

/**
 Removes nether dimension
 */
@EventBusSubscriber(modid = StrataBasic.MODID)
public class NoPortals {

    @SubscribeEvent
    public static void onPortalSpawn(BlockEvent.PortalSpawnEvent event) {
        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onTravel(EntityTravelToDimensionEvent event) {
        if (event.getDimension() == Level.NETHER) {
            event.setCanceled(true);
        }
    }
}
