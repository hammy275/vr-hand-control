package com.hammy275.vrhandcontrol.client.vr_data;

import org.joml.Quaternionf;
import org.joml.Vector3f;

public record Pose(Quaternionf rotation, Vector3f position) {
}
