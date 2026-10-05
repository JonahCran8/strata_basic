package com.jonah.stratabasic.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// Water placed in nether evaporates, like in nether dimension
@Mixin(FluidType.class)
public class FluidTypeMixin {

    private static final int NETHER_TOP = -400;

    @Inject(method = "isVaporizedOnPlacement", at = @At("HEAD"), cancellable = true)
    private void strata$vaporizeInNether(Level level, BlockPos pos, FluidStack stack, CallbackInfoReturnable<Boolean> cir) {
        FluidType self = (FluidType) (Object) this;
        if (pos.getY() < NETHER_TOP && (self == NeoForgeMod.WATER_TYPE.value() || self.getStateForPlacement(level, pos, stack).is(FluidTags.WATER))) {
            cir.setReturnValue(true);
        }
    }
}
