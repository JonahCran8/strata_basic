package com.jonah.stratabasic.client;

import com.jonah.stratabasic.StrataBasic;
import net.minecraft.client.renderer.entity.CreeperRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Creeper;

// Draws creeper with own texture
public class DeepCreeperRenderer extends CreeperRenderer {

    private final ResourceLocation texture;

    public DeepCreeperRenderer(EntityRendererProvider.Context context, String name) {
        super(context);
        this.texture = ResourceLocation.fromNamespaceAndPath(StrataBasic.MODID, "textures/entity/" + name + ".png");
    }

    @Override
    public ResourceLocation getTextureLocation(Creeper entity) {
        return texture;
    }
}
