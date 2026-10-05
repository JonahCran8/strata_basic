package com.jonah.stratabasic.mixin;

import com.jonah.stratabasic.client.NetherAtmosphere;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.level.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// Gives nether the nether dimension's ambient light
@Mixin(LightTexture.class)
public class LightTextureMixin {

    @Inject(method = "getBrightness", at = @At("RETURN"), cancellable = true)
    private static void strata$netherAmbientLight(DimensionType dimensionType, int lightLevel, CallbackInfoReturnable<Float> cir) {
        if (NetherAtmosphere.inNether()) {
            cir.setReturnValue(Mth.lerp(NetherAtmosphere.AMBIENT_LIGHT, cir.getReturnValueF(), 1.0F));
        }
    }
}
