package com.hammy275.vrhandcontrol.common.vr.handlers;

import com.hammy275.vrhandcontrol.VRHandControl;
import com.hammy275.vrhandcontrol.client.vr_data.vd.Finger;
import com.hammy275.vrhandcontrol.client.vr_data.vd.Hand;
import com.hammy275.vrhandcontrol.client.vr_data.vd.Joint;
import com.hammy275.vrhandcontrol.client.vr_data.vd.VRData;
import com.mojang.datafixers.util.Pair;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.joml.Vector3f;

import java.util.LinkedList;

public class SnapHandler implements GestureHandler {

    private boolean middlePinched = false;

    @Override
    public boolean handleDetection(VRData data, LinkedList<VRData> oldData) {
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
    public boolean performAction(Player player) {
        player.sendSystemMessage(Component.literal("Snap detected!"));
        return true;
    }

    @Override
    public ResourceLocation getID() {
        return new ResourceLocation(VRHandControl.MOD_ID, "snap");
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
