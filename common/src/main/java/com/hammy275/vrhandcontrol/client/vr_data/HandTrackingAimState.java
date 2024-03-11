package com.hammy275.vrhandcontrol.client.vr_data;

public record HandTrackingAimState(long aimStatus, Pose aimPose, float pinchStrengthIndex, float pinchStrengthMiddle,
                                   float pinchStrengthRing, float pinchStrengthLittle) {
    public boolean isPinching(Finger finger) {
        if (finger == Finger.THUMB) {
            throw new IllegalArgumentException("Can't pinch thumb with thumb!");
        }
        return (finger.pinchingBit & aimStatus) != 0;
    }
}
