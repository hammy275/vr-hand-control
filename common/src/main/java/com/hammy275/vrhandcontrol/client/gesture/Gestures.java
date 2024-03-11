package com.hammy275.vrhandcontrol.client.gesture;

import com.hammy275.vrhandcontrol.client.vr_data.VRData;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Gestures {

    private static final Gestures HANDLER = new Gestures();

    private final List<GestureHandler> gestureHandlers = new ArrayList<>();
    private VRData data = new VRData();
    private final LinkedList<VRData> oldData = new LinkedList<>();
    private final Minecraft mc = Minecraft.getInstance();

    private Gestures() {
        gestureHandlers.add(new SnapHandler());
    }

    public void onClientTick(Minecraft minecraft) {
        data = new VRData();
        data.read();
        if (!oldData.isEmpty() && oldData.getFirst().equals(data)) {
            return;
        }
        oldData.addFirst(data);
        if (oldData.size() > 10) {
            oldData.removeLast();
        }
        if (mc.player != null) {
            // For each gesture handler, handle detection. If it detects the gesture and we haven't performed some
            // other action successfully, perform the action and prevent other actions this tick.
            boolean allowActions = true;
            for (GestureHandler handler : gestureHandlers) {
                if (handler.handleDetection(data, oldData, mc) && allowActions && handler.performAction(data, oldData, mc)) {
                    allowActions = false;
                }
            }
        }

    }

    public static Gestures getInstance() {
        return HANDLER;
    }
}
