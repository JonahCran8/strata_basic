package com.jonah.stratabasic.mixin;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Aquifer;
import net.minecraft.world.level.levelgen.DensityFunction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// Flat lava sea in nether instead of random aquifer lakes (see NoiseFluidMixin)
@Mixin(Aquifer.NoiseBasedAquifer.class)
public class NoiseAquiferMixin {

    private static final int NETHER_TOP = -400;

    @Shadow
    @Final
    private Aquifer.FluidPicker globalFluidPicker;

    @Inject(method = "computeSubstance", at = @At("HEAD"), cancellable = true)
    private void strata$flatNetherSea(DensityFunction.FunctionContext context, double substance, CallbackInfoReturnable<BlockState> cir) {
        if (context.blockY() < NETHER_TOP) {
            cir.setReturnValue(substance > 0.0 ? null
                    : this.globalFluidPicker.computeFluid(context.blockX(), context.blockY(), context.blockZ()).at(context.blockY()));
        }
    }
}
