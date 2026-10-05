package com.jonah.stratabasic.mixin;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.Aquifer;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// Fixes cave fluids: lava only under nether lava sea (Y -496), none up to Y -54, vanilla water above
@Mixin(NoiseBasedChunkGenerator.class)
public class NoiseFluidMixin {

    private static final int NETHER_SEA = -496;
    private static final int VANILLA_LAVA_TOP = -54;

    @Inject(method = "createFluidPicker", at = @At("RETURN"), cancellable = true)
    private static void strata$fluids(NoiseGeneratorSettings settings, CallbackInfoReturnable<Aquifer.FluidPicker> cir) {
        Aquifer.FluidStatus lava = new Aquifer.FluidStatus(NETHER_SEA, Blocks.LAVA.defaultBlockState());
        Aquifer.FluidStatus none = new Aquifer.FluidStatus(DimensionType.MIN_Y * 2, Blocks.AIR.defaultBlockState());
        Aquifer.FluidStatus water = new Aquifer.FluidStatus(settings.seaLevel(), settings.defaultFluid());
        cir.setReturnValue((x, y, z) -> y < NETHER_SEA ? lava : y < VANILLA_LAVA_TOP ? none : water);
    }
}
