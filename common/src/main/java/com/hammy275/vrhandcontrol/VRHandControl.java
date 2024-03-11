package com.hammy275.vrhandcontrol;

import com.hammy275.vrhandcontrol.client.vr_data.VRDataGrabber;
import dev.architectury.event.events.client.ClientTickEvent;
import net.minecraft.network.chat.Component;

public class VRHandControl
{
	public static final String MOD_ID = "vr_hand_control";

	public static void init() {
		ClientTickEvent.CLIENT_POST.register((minecraft) -> {
			VRDataGrabber grabber = new VRDataGrabber(true);
			grabber.read();
		});
	}
}
