package com.hammy275.vrhandcontrol.client.vr_data.vd;

public enum Finger {
    THUMB(0),
    INDEX(0x4),
    MIDDLE(0x8),
    RING(0x10),
    LITTLE(0x20);

    public final long pinchingBit;

    Finger(long pinchingBit) {
        this.pinchingBit = pinchingBit;
    }
}
