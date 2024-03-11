package com.hammy275.vrhandcontrol.fabric;

import com.hammy275.vrhandcontrol.VRHandControl;
import net.fabricmc.api.ModInitializer;

public class VRHandControlFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        VRHandControl.init();
    }
}