package com.hammy275.vrhandcontrol.mixin;

import com.hammy275.vrhandcontrol.client.proxy.ClientGestureHandlers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.vivecraft.client.VivecraftVRMod;
import org.vivecraft.client_vr.ClientDataHolderVR;
import org.vivecraft.client_vr.provider.openvr_lwjgl.VRInputAction;
import org.vivecraft.common.utils.math.Vector2;

@Mixin(VRInputAction.class)
public class VRInputActionMixin {

    @Inject(method = "getAxis2DUseTracked", at = @At("HEAD"), cancellable = true, remap = false)
    public void injectGestureMovement(CallbackInfoReturnable<Vector2> cir) {
        // This messing around makes sure KeyboardInputVRMixin from Vivecraft thinks the control stick is pointed
        // perfectly forward. From there, Vivecraft handles the rest of movement.
        if ((Object) this == ClientDataHolderVR.getInstance().vr.getInputAction(VivecraftVRMod.INSTANCE.keyFreeMoveStrafe)) {
            if (ClientGestureHandlers.goForward == Boolean.TRUE) {
                cir.setReturnValue(new Vector2(0, 1));
            } else if (ClientGestureHandlers.goForward == Boolean.FALSE) {
                cir.setReturnValue(new Vector2(0, 0));
                ClientGestureHandlers.goForward = null;
            }
        }
    }
}
