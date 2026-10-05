package com.jonah.stratabasic.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// Water flowing into nether evaporates
@Mixin(FlowingFluid.class)
public class FlowingFluidMixin {

    private static final int NETHER_TOP = -400;

    @Inject(method = "spreadTo", at = @At("HEAD"), cancellable = true)
    private void strata$evaporateInNether(LevelAccessor level, BlockPos pos, BlockState state, Direction direction, FluidState fluidState, CallbackInfo ci) {
        if (pos.getY() < NETHER_TOP && fluidState.is(FluidTags.WATER)) {
            level.levelEvent(1501, pos, 0);      // the fizz and smoke of lava meeting water
            ci.cancel();
        }
    }
}
