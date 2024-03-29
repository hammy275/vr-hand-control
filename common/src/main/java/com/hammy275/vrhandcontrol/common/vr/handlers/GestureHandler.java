package com.hammy275.vrhandcontrol.common.vr.handlers;

import com.hammy275.vrhandcontrol.client.vr_data.vd.VRData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.LinkedList;

public interface GestureHandler {
    /**
     * Called once every time data changes to see if this gesture is active. Can take advantage of the
     * fact this is called only when data changes to manage internal state to detect longer gestures. Keep
     * in mind that this is also only called no more than once per game tick. Note that this
     * function returning true does not guarantee the action in performAction() will run! This function
     * runs only on the client, however it should not import client-only imports, even if it runs only on the client.
     * @param data Current tick tracking data.
     * @param oldData List of past up to 10 data points.
     * @return Whether the gesture should activate this tick.
     */
    public boolean handleDetection(VRData data, LinkedList<VRData> oldData);

    /**
     * Perform the action for this gesture. Runs only on the side identified by handleOnClient().
     *
     * @param player Player performing the action.
     * @return Whether the action triggered succeeded.
     */
    public boolean performAction(Player player);

    /**
     * @return A unique ID to identify this GestureHandler.
     */
    public ResourceLocation getID();

    /**
     * @return true to run performAction() client-side. false to run it server-side.
     */
    public boolean handleOnClient();

    /**
     * @return If true, always run performAction() if handleDetection() holds true, even if other gestures have already
     * run! This will also not block other gestures from running.
     */
    public boolean alwaysRun();
}
