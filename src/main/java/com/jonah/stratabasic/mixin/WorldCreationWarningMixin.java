package com.jonah.stratabasic.mixin;

import com.mojang.serialization.Lifecycle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.client.gui.screens.worldselection.WorldOpenFlows;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// Skips experimental settings warning on world creation
@Mixin(WorldOpenFlows.class)
public class WorldCreationWarningMixin {

    @Inject(method = "confirmWorldCreation", at = @At("HEAD"), cancellable = true)
    private static void strata$noExperimentalWarning(Minecraft minecraft, CreateWorldScreen screen, Lifecycle lifecycle, Runnable loadWorld,
                                                     boolean skipWarnings, CallbackInfo ci) {
        loadWorld.run();
        ci.cancel();
    }
}
