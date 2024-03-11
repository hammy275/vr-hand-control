package com.hammy275.vrhandcontrol.client.gesture;

import com.hammy275.vrhandcontrol.client.vr_data.VRData;
import net.minecraft.client.Minecraft;

import java.util.LinkedList;

public interface GestureHandler {
    /**
     * Called once every time data changes to see if this gesture is active. Can take advantage of the
     * fact this is called only when data changes to manage internal state to detect longer gestures. Keep
     * in mind that this is also only called no more than once per game tick. Note that this
     * function returning true does not guarantee the action in performAction() will run!
     * @param data Current tick tracking data.
     * @param oldData List of past up to 10 data points.
     * @param mc Minecraft instance.
     * @return Whether the gesture should activate this tick.
     */
    public boolean handleDetection(VRData data, LinkedList<VRData> oldData, Minecraft mc);

    /**
     * Perform the action for this gesture.
     * @param data Current tick tracking data.
     * @param oldData List of past up to 10 data points.
     * @param mc Minecraft instance.
     * @return Whether the action triggered succeeded.
     */
    public boolean performAction(VRData data, LinkedList<VRData> oldData, Minecraft mc);
}
