package com.hammy275.vrhandcontrol.client.gesture;

import com.hammy275.vrhandcontrol.client.vr_data.Finger;
import com.hammy275.vrhandcontrol.client.vr_data.Hand;
import com.hammy275.vrhandcontrol.client.vr_data.Joint;
import com.hammy275.vrhandcontrol.client.vr_data.VRData;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.joml.Vector3f;

import java.util.LinkedList;

public class SnapHandler implements GestureHandler {

    private boolean middlePinched = false;

    @Override
    public boolean handleDetection(VRData data, LinkedList<VRData> oldData, Minecraft mc) {
        // TODO: Potentitally detect if the middle finger tip is extremely close to the palm after the snap motion
        if (middlePinched && !isMiddlePinched(data)) {
            Pair<Finger, Float> maxPinch = data.rightAimState.getMaxFingerPinch();
            // If we're now at least barely pinching our index finger, and we aren't pinching our
            // middle finger much at all anymore, then we just did a snap.
            if (maxPinch.getFirst() == Finger.INDEX && maxPinch.getSecond() > 0.01f
                && closerToIndexThanMiddle(data)) {
                middlePinched = false;
                return true;
            }
        }
        middlePinched = isMiddlePinched(data);
        return false;
    }

    @Override
    public boolean performAction(VRData data, LinkedList<VRData> oldData, Minecraft mc) {
        mc.player.sendSystemMessage(Component.literal("Snap detected!"));
        return true;
    }

    private boolean isMiddlePinched(VRData data) {
        Pair<Finger, Float> maxPinch = data.rightAimState.getMaxFingerPinch();
        return data.rightAimState.isPinching(Finger.MIDDLE) || (maxPinch.getFirst() == Finger.MIDDLE && maxPinch.getSecond() > 0.5);
    }

    private boolean closerToIndexThanMiddle(VRData data) {
        Vector3f thumb = data.getFingerJoint(Joint.THUMB_TIP, Hand.RIGHT).pose().position();
        Vector3f indexA = data.getFingerJoint(Joint.INDEX_TIP, Hand.RIGHT).pose().position();
        Vector3f indexB = data.getFingerJoint(Joint.INDEX_DISTAL, Hand.RIGHT).pose().position();
        Vector3f middle = data.getFingerJoint(Joint.MIDDLE_TIP, Hand.RIGHT).pose().position();
        return thumb.distanceSquared(indexA) < thumb.distanceSquared(middle) &&
                thumb.distanceSquared(indexB) < thumb.distanceSquared(middle);
    }
}
