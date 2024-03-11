package com.hammy275.vrhandcontrol.client.vr_data;

public record HandTrackingAimState(long aimStatus, Pose aimPose, float pinchStrengthIndex, float pinchStrengthMiddle,
                                   float pinchStrengthRing, float pinchStrengthLittle) {
}
