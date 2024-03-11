package com.hammy275.vrhandcontrol.common.network.packet;

import com.hammy275.vrhandcontrol.common.vr.handlers.GestureHandler;
import com.hammy275.vrhandcontrol.common.vr.GestureHandlers;
import dev.architectury.networking.NetworkManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.function.Supplier;

public class GestureActionPacket {

    public final ResourceLocation actionID;

    public GestureActionPacket(ResourceLocation actionID) {
        this.actionID = actionID;
    }

    public GestureActionPacket(FriendlyByteBuf buffer) {
        this(buffer.readResourceLocation());
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeResourceLocation(this.actionID);
    }

    public void handle(Supplier<NetworkManager.PacketContext> ctx) {
        ctx.get().queue(() -> {
            ServerPlayer player = ctx.get().getPlayer() instanceof ServerPlayer ? (ServerPlayer) ctx.get().getPlayer() : null;
            if (player != null) {
                for (GestureHandler handler : GestureHandlers.gestureHandlers) {
                    if (handler.getID().equals(this.actionID)) {
                        handler.performAction(player);
                    }
                }
            }
        });
    }
}
