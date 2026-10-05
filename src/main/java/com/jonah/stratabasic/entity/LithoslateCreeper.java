package com.jonah.stratabasic.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Level;

// Lithoslate layer creeper, faster with a bigger blast that spreads fire
public class LithoslateCreeper extends Creeper implements DeepCreeper {

    public static final float BLAST_RADIUS = 4.0F;
    // Normal creeper has 20
    public static final double BASE_HEALTH = 20.0;
    public static final double SPEED = 0.30;

    private boolean blastStarted;

    public LithoslateCreeper(EntityType<? extends Creeper> type, Level level) {
        super(type, level);
        this.explosionRadius = (int) blastRadius();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Creeper.createAttributes().add(Attributes.MOVEMENT_SPEED, SPEED).add(Attributes.MAX_HEALTH, BASE_HEALTH);
    }

    @Override
    public float blastRadius() {
        return BLAST_RADIUS;
    }

    @Override
    public boolean spreadsFire() {
        return true;
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
