package com.hammy275.vrhandcontrol.forge;

import com.hammy275.vrhandcontrol.client.vr_data.vivecraft.VRPlugin;
import net.blf02.forge.VRAPIPlugin;
import net.blf02.forge.VRAPIPluginProvider;
import net.blf02.vrapi.api.IVRAPI;

@VRAPIPlugin
public class VRPluginForge implements VRAPIPluginProvider {
    @Override
    public void getVRAPI(IVRAPI ivrapi) {
        VRPlugin.API = ivrapi;
    }
}
