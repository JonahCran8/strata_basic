package com.jonah.stratabasic.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.SwellGoal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.EnumSet;

// Ferrite layer creeper, teleports behind player when hurt, biggest blast and no fire
public class VoidCreeper extends Creeper implements DeepCreeper {

    public static final float BLAST_RADIUS = 5.0F;
    // Normal creeper has 20
    public static final double BASE_HEALTH = 80.0;
    public static final double SPEED = 0.30;
    // Fuse in ticks, twice a normal creeper's
    public static final int FUSE = 60;
    // Once lit, fuse only stops when player is this far away
    private static final double FUSE_RANGE = 12.0;
    // Teleports this far behind player (min to max) and a few blocks up or down
    private static final double TELEPORT_MIN = 6.0;
    private static final double TELEPORT_MAX = 8.0;
    private static final int TELEPORT_VERTICAL = 2;
    // How far to either side of directly behind (radians)
    private static final double BEHIND_SPREAD = Math.PI / 3.0;
    private static final int TELEPORT_TRIES = 16;
    // Ticks between teleports so fire or lava doesn't make it flicker
    private static final int TELEPORT_COOLDOWN = 10;

    private boolean blastStarted;
    private int lastTeleport = -TELEPORT_COOLDOWN;

    public VoidCreeper(EntityType<? extends Creeper> type, Level level) {
        super(type, level);
        this.explosionRadius = (int) BLAST_RADIUS;
        this.maxSwell = FUSE;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Creeper.createAttributes().add(Attributes.MOVEMENT_SPEED, SPEED).add(Attributes.MAX_HEALTH, BASE_HEALTH);
    }

    // Normal creeper gives up fuse at 7 blocks or out of sight, this one only at FUSE_RANGE
    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.removeAllGoals(goal -> goal instanceof SwellGoal);
        this.goalSelector.addGoal(1, new RelentlessSwellGoal(this));
    }

    private static class RelentlessSwellGoal extends Goal {
        private final VoidCreeper creeper;
        private LivingEntity target;

        RelentlessSwellGoal(VoidCreeper creeper) {
            this.creeper = creeper;
            setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            LivingEntity target = creeper.getTarget();
            return creeper.getSwellDir() > 0 || (target != null && creeper.distanceToSqr(target) < 9.0);
        }

        @Override
        public void start() {
            creeper.getNavigation().stop();
            target = creeper.getTarget();
        }

        @Override
        public void stop() {
            target = null;
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            if (target == null || !target.isAlive() || creeper.distanceToSqr(target) > FUSE_RANGE * FUSE_RANGE) {
                creeper.setSwellDir(-1);
            } else {
                creeper.setSwellDir(1);
            }
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean hurt = super.hurt(source, amount);
        // Only teleports when a player hurts it (hit or arrow)
        if (hurt && !level().isClientSide() && isAlive() && source.getEntity() instanceof Player player
                && tickCount - lastTeleport >= TELEPORT_COOLDOWN && teleportBehind(player)) {
            lastTeleport = tickCount;
        }
        return hurt;
    }

    private boolean teleportBehind(Player player) {
        ServerLevel level = (ServerLevel) level();
        double fromX = getX();
        double fromY = getY();
        double fromZ = getZ();
        for (int i = 0; i < TELEPORT_TRIES; i++) {
            // Behind player is the opposite of where they face
            double angle = Math.toRadians(player.getYRot()) + Math.PI + (random.nextDouble() - 0.5) * 2.0 * BEHIND_SPREAD;
            double distance = TELEPORT_MIN + random.nextDouble() * (TELEPORT_MAX - TELEPORT_MIN);
            double x = player.getX() - Math.sin(angle) * distance;
            double y = player.getY() + random.nextInt(TELEPORT_VERTICAL * 2 + 1) - TELEPORT_VERTICAL;
            double z = player.getZ() + Math.cos(angle) * distance;
            if (randomTeleport(x, y, z, true)) {
                level.sendParticles(ParticleTypes.PORTAL, fromX, fromY + 1.0, fromZ, 24, 0.3, 0.6, 0.3, 0.3);
                level.playSound(null, fromX, fromY, fromZ, SoundEvents.ENDERMAN_TELEPORT, SoundSource.HOSTILE, 1.0F, 1.0F);
                level.playSound(null, getX(), getY(), getZ(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.HOSTILE, 1.0F, 1.0F);
                return true;
            }
        }
        return false;
    }

    @Override
    public float blastRadius() {
        return BLAST_RADIUS;
    }

    @Override
    public boolean spreadsFire() {
        return false;
    }

    @Override
    public boolean blastStarted() {
        return blastStarted;
    }

    @Override
    public void startBlast() {
        blastStarted = true;
    }
}
