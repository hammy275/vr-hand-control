package com.hammy275.vrhandcontrol.common.vr.handlers;

import com.hammy275.vrhandcontrol.VRHandControl;
import com.hammy275.vrhandcontrol.client.proxy.ClientGestureHandlers;
import com.hammy275.vrhandcontrol.client.vr_data.vd.Hand;
import com.hammy275.vrhandcontrol.client.vr_data.vd.Joint;
import com.hammy275.vrhandcontrol.client.vr_data.vd.VRData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.joml.Vector3f;

import java.util.LinkedList;

public class MoveHandler implements GestureHandler {

    private static final Joint[] palmDistanceChecks = new Joint[]{Joint.INDEX_TIP, Joint.MIDDLE_TIP, Joint.RING_TIP, Joint.LITTLE_TIP};
    private static final float palmDistMaxSqr = 0.1f*0.1f;

    private boolean hadFistClosed = false;

    @Override
    public boolean handleDetection(VRData data, LinkedList<VRData> oldData) {
        Vector3f palmPos = data.getFingerJoint(Joint.PALM, Hand.LEFT).pose().position();
        int numZeros = 0;
        for (Joint joint : palmDistanceChecks) {
            float dist = data.getFingerJoint(joint, Hand.LEFT).pose().position().distanceSquared(palmPos);
            if (dist > palmDistMaxSqr) {
                if (hadFistClosed) {
                    ClientGestureHandlers.stopMoving();
                    hadFistClosed = false;
                }
                return false;
            } else if (dist == 0f) {
                numZeros++;
            }
        }
        hadFistClosed = true;
        return numZeros < 4; // If all fingers are 0, we're getting bad data, and should just ignore it
    }

    @Override
    public boolean performAction(Player player) {
        return ClientGestureHandlers.moveHandler(player);
    }

    @Override
    public ResourceLocation getID() {
        return new ResourceLocation(VRHandControl.MOD_ID, "fist");
    }

    @Override
    public boolean handleOnClient() {
        return true;
    }

    @Override
    public boolean alwaysRun() {
        return true;
    }
}
