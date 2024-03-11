package com.hammy275.vrhandcontrol.common.network;

import com.hammy275.vrhandcontrol.VRHandControl;
import com.hammy275.vrhandcontrol.common.network.packet.GestureActionPacket;
import dev.architectury.networking.NetworkChannel;
import net.minecraft.resources.ResourceLocation;

public class Network {

    public static final NetworkChannel INSTANCE = NetworkChannel.create(new ResourceLocation(VRHandControl.MOD_ID, "network"));

    public static void registerPackets() {
        INSTANCE.register(GestureActionPacket.class, GestureActionPacket::encode, GestureActionPacket::new, GestureActionPacket::handle);
    }
}
