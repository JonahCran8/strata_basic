package com.jonah.stratabasic.client;

import com.jonah.stratabasic.StrataBasic;
import com.mojang.blaze3d.shaders.FogShape;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.world.level.material.FogType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ViewportEvent;

// Adds nether fog and ambient light below the nether roof (overworld dimension has neither)
@EventBusSubscriber(modid = StrataBasic.MODID, value = Dist.CLIENT)
public class NetherAtmosphere {

    // Nether roof
    public static final double TOP = -400.0;
    // Nether dimension ambient light
    public static final float AMBIENT_LIGHT = 0.1F;
    // Nether fog
    private static final float FOG_START = 0.2F;
    private static final float FOG_END = 0.9F;
    private static final float FOG_MAX_DISTANCE = 224.0F;

    public static boolean inNether() {
        Minecraft minecraft = Minecraft.getInstance();
        return minecraft.player != null && minecraft.player.getY() < TOP;
    }

    @SubscribeEvent
    public static void fog(ViewportEvent.RenderFog event) {
        if (event.getType() != FogType.NONE || event.getCamera().getPosition().y >= TOP) {
            return;
        }
        float renderDistance = event.getFarPlaneDistance();
        if (event.getMode() == FogRenderer.FogMode.FOG_TERRAIN) {
            event.setNearPlaneDistance(renderDistance * FOG_START);
            event.setFarPlaneDistance(Math.min(renderDistance, FOG_MAX_DISTANCE) * FOG_END);
        } else {
            event.setNearPlaneDistance(0.0F);
            event.setFarPlaneDistance(renderDistance);
        }
        event.setFogShape(FogShape.SPHERE);
        event.setCanceled(true);
    }
}
