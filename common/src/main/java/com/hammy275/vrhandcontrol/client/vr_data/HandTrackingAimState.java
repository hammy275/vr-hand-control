package com.hammy275.vrhandcontrol.client.vr_data;

import com.mojang.datafixers.util.Pair;

public record HandTrackingAimState(long aimStatus, Pose aimPose, float pinchStrengthIndex, float pinchStrengthMiddle,
                                   float pinchStrengthRing, float pinchStrengthLittle) {
    public boolean isPinching(Finger finger) {
        if (finger == Finger.THUMB) {
            throw new IllegalArgumentException("Can't pinch thumb with thumb!");
        }
        return (finger.pinchingBit & aimStatus) != 0;
    }

    public Pair<Finger, Float> getMaxFingerPinch() {
        float indexMiddle = Math.max(pinchStrengthIndex, pinchStrengthMiddle);
        Finger fingerA = indexMiddle == pinchStrengthIndex ? Finger.INDEX : Finger.MIDDLE;
        float ringLittle = Math.max(pinchStrengthRing, pinchStrengthLittle);
        Finger fingerB = ringLittle == pinchStrengthRing ? Finger.RING : Finger.LITTLE;
        float max = Math.max(indexMiddle, ringLittle);
        return new Pair<>(max == indexMiddle ? fingerA : fingerB, max);
    }
}
