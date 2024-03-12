package com.hammy275.vrhandcontrol.fabric;

import com.hammy275.vrhandcontrol.VRHandControl;
import com.hammy275.vrhandcontrol.client.vr_data.vivecraft.VRPlugin;
import net.blf02.vrapi.api.IVRAPI;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;

import java.util.List;

public class VRHandControlFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        VRHandControl.init();
        List<EntrypointContainer<IVRAPI>> apis = FabricLoader.getInstance().getEntrypointContainers("vrapi", IVRAPI.class);
        if (!apis.isEmpty()) {
            VRPlugin.API = apis.get(0).getEntrypoint();
        }
    }
}