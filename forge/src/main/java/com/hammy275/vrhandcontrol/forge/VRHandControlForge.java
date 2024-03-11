package com.hammy275.vrhandcontrol.forge;

import dev.architectury.platform.forge.EventBuses;
import com.hammy275.vrhandcontrol.VRHandControl;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(VRHandControl.MOD_ID)
public class VRHandControlForge {
    public VRHandControlForge() {
		// Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(VRHandControl.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        VRHandControl.init();
    }
}