package com.jonah.stratabasic.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Level;

// Mantleslate layer creeper, lithoslate creeper but faster with a bigger blast
public class MantleslateCreeper extends LithoslateCreeper {

    public static final float BLAST_RADIUS = 4.5F;
    // Normal creeper has 20
    public static final double BASE_HEALTH = 60.0;
    public static final double SPEED = 0.34;

    public MantleslateCreeper(EntityType<? extends Creeper> type, Level level) {
        super(type, level);
        this.explosionRadius = (int) BLAST_RADIUS;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Creeper.createAttributes().add(Attributes.MOVEMENT_SPEED, SPEED).add(Attributes.MAX_HEALTH, BASE_HEALTH);
    }

    @Override
    public float blastRadius() {
        return BLAST_RADIUS;
    }
}
