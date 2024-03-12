package com.hammy275.vrhandcontrol.client.vr_data.vd;

public class VRData extends VRDataGrabber {

    public VRData() {
        super();
    }

    public FingerJointState getFingerJoint(Joint joint, Hand hand) {
        FingerJointState[] states = hand == Hand.LEFT ? this.leftHandJointStates : this.rightHandJointStates;
        return states[joint.ordinal()];
    }

}
