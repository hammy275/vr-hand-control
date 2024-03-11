package com.hammy275.vrhandcontrol.client.vr_data;

import org.joml.Vector3f;

public record FingerJointState(Pose pose, float radius, Vector3f angularVelocity, Vector3f linearVelocity) {
}
