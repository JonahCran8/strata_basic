package com.jonah.stratabasic.content;

import com.jonah.stratabasic.StrataBasic;
import com.jonah.stratabasic.entity.DeepCreeper;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.ExplosionEvent;

// Runs layer creeper blasts with their own radius (can be fractional) and lights a few fires if they spread fire
@EventBusSubscriber(modid = StrataBasic.MODID)
public class CreeperBlasts {

    // Chance for each destroyed block to catch fire
    private static final float FIRE_CHANCE = 0.04f;

    @SubscribeEvent
    public static void onExplosionStart(ExplosionEvent.Start event) {
        Explosion explosion = event.getExplosion();
        if (!(explosion.getDirectSourceEntity() instanceof Creeper creeper) || !(creeper instanceof DeepCreeper deep) || deep.blastStarted()) {
            return;
        }

        deep.startBlast();
        event.setCanceled(true);
        Level level = event.getLevel();
        float radius = deep.blastRadius() * (creeper.isPowered() ? 2.0F : 1.0F);
        Explosion blast = level.explode(creeper, creeper.getX(), creeper.getY(), creeper.getZ(), radius, false, Level.ExplosionInteraction.MOB);

        if (level.isClientSide() || !deep.spreadsFire()) {
            return;
        }
        for (BlockPos pos : blast.getToBlow()) {
            if (level.random.nextFloat() < FIRE_CHANCE && level.isEmptyBlock(pos) && BaseFireBlock.canBePlacedAt(level, pos, Direction.DOWN)) {
                level.setBlockAndUpdate(pos, BaseFireBlock.getState(level, pos));
            }
        }
    }
}
