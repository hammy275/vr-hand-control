package com.hammy275.vrhandcontrol.client.gesture;

import com.hammy275.vrhandcontrol.client.vr_data.Finger;
import com.hammy275.vrhandcontrol.client.vr_data.VRData;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import java.util.LinkedList;

public class SnapHandler implements GestureHandler {
    @Override
    public boolean handleDetection(VRData data, LinkedList<VRData> oldData, Minecraft mc) {
        return data.rightAimState.isPinching(Finger.MIDDLE);
    }

    @Override
    public boolean performAction(VRData data, LinkedList<VRData> oldData, Minecraft mc) {
        mc.player.sendSystemMessage(Component.literal("Snap detected!"));
        return true;
    }
}
