package com.hammy275.vrhandcontrol.client.gesture;

import com.hammy275.vrhandcontrol.client.vr_data.vd.VRData;
import com.hammy275.vrhandcontrol.client.vr_data.vivecraft.VRPlugin;
import com.hammy275.vrhandcontrol.common.network.Network;
import com.hammy275.vrhandcontrol.common.network.packet.GestureActionPacket;
import com.hammy275.vrhandcontrol.common.vr.handlers.GestureHandler;
import com.hammy275.vrhandcontrol.common.vr.GestureHandlers;
import dev.architectury.platform.Platform;
import net.minecraft.client.Minecraft;

import java.util.LinkedList;

public class GestureDetection {

    private static final GestureDetection HANDLER = new GestureDetection();

    private VRData data = new VRData();
    private final LinkedList<VRData> oldData = new LinkedList<>();
    private final Minecraft mc = Minecraft.getInstance();

    private GestureDetection() {
    }

    public void onClientTick(Minecraft minecraft) {
        if (minecraft.player != null && !VRPlugin.API.playerInVR(mc.player) && !Platform.isDevelopmentEnvironment()) {
            return;
        }
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
            // For each gesture handler, handle detection. If it detects the gesture, and we haven't performed some
            // other action successfully, perform the action and prevent other actions this tick.
            boolean allowActions = true;
            for (GestureHandler handler : GestureHandlers.gestureHandlers) {
                if (handler.handleDetection(data, oldData) && (allowActions || handler.alwaysRun())) {
                    allowActions = allowActions && !handler.alwaysRun();
                    if (handler.handleOnClient()) {
                        handler.performAction(mc.player);
                    } else {
                        Network.INSTANCE.sendToServer(new GestureActionPacket(handler.getID()));
                    }
                }
            }
        }

    }

    public static GestureDetection getInstance() {
        return HANDLER;
    }
}
