package com.hammy275.vrhandcontrol;

import com.hammy275.vrhandcontrol.client.gesture.Gestures;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;

public class VRHandControl
{
	public static final String MOD_ID = "vr_hand_control";

	public static void init() {
		if (Platform.getEnvironment() == Env.CLIENT) {
			ClientTickEvent.CLIENT_POST.register(Gestures.getInstance()::onClientTick);
		}
	}
}
