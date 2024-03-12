package com.hammy275.vrhandcontrol.client.proxy;

import com.hammy275.vrhandcontrol.client.vr_data.vivecraft.VRPlugin;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;

public class ClientGestureHandlers {

    public static Boolean goForward = null; // null to do nothing, true to start moving forward, false to stop.


    public static boolean moveHandler(Player playerIn) {
        LocalPlayer player = (LocalPlayer) playerIn;
        if (VRPlugin.API.playerInVR(player) && !VRPlugin.API.isSeated(player)) {
            // If in VR and not seated, need to do things in a mixin later and let Vivecraft handle movement from there
            goForward = Boolean.TRUE;
        } else {
            player.input.forwardImpulse = 1f;
            Minecraft.getInstance().options.keyUp.setDown(true);
            player.setSprinting(true);
        }
        return true;
    }

    public static void stopMoving() {
        LocalPlayer player = Minecraft.getInstance().player;
        if (VRPlugin.API.playerInVR(player) && !VRPlugin.API.isSeated(player)) {
            // If in VR and not seated, need to do things in a mixin later and let Vivecraft handle movement from there
            goForward = Boolean.FALSE;
        } else {
            player.input.forwardImpulse = 0f;
            Minecraft.getInstance().options.keyUp.setDown(false);
            player.setSprinting(false);
        }
    }
}
