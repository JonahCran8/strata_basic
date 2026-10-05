package com.jonah.stratabasic.content;

import com.jonah.stratabasic.StrataBasic;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

// Makes piglins and hoglins immune to zombification below the nether roof
@EventBusSubscriber(modid = StrataBasic.MODID)
public class NetherCreatures {

    private static final int NETHER_TOP = -400;

    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Pre event) {
        Entity entity = event.getEntity();
        if (entity.level().isClientSide() || entity.tickCount % 20 != 0 || entity.getY() >= NETHER_TOP) {
            return;
        }
        if (entity instanceof AbstractPiglin piglin) {
            piglin.setImmuneToZombification(true);
        } else if (entity instanceof Hoglin hoglin) {
            hoglin.setImmuneToZombification(true);
        }
    }
}
