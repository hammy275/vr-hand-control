package com.hammy275.vrhandcontrol.client.vr_data;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinNT;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.nio.ByteBuffer;

/**
 * This file is based on https://github.com/DenTechs/VirtualDesktop_ReadMMF/blob/main/read_virtualdesktop_mmf.py, which,
 * as of writing, does NOT contain a license. As such, this file does not fall under the MIT license, and should be considered
 * unusable at this time.
 */
public class VRDataGrabber {

    private ByteBuffer buffer;
    private Pointer pointer;

    public boolean faceIsValid = false;
    public boolean isEyeFollowingBlendShapesValid = false;
    public float[] expressionWeights = new float[70];
    public float[] expressionConfidences = new float[2];
    public boolean isLeftEyeValid = false;
    public boolean isRightEyeValid = false;
    public Pose leftEyePose = null;
    public Pose rightEyePose = null;
    public float leftEyeConfidence = 0f;
    public float rightEyeConfidence = 0f;
    public boolean leftHandActive = false;
    public boolean rightHandActive = false;
    public FingerJointState[] leftHandJointStates = new FingerJointState[26];
    public FingerJointState[] rightHandJointStates = new FingerJointState[26];
    public HandTrackingAimState leftAimState = null;
    public HandTrackingAimState rightAimState = null;
    public boolean isBodyTrackingCalibrated = false;
    public boolean isBodyTrackingHighFidelity = false;
    public float bodyTrackingConfidence = 0f;
    public BodyJointLocation[] bodyJoints = new BodyJointLocation[84];
    public SkeletonJoint[] skeletonJoints = new SkeletonJoint[84];
    public int skeletonChangedCount = 0;

    public VRDataGrabber() {
        WinNT.HANDLE handle = MyKernel32.INSTANCE.OpenFileMapping(WinNT.FILE_MAP_READ, false, "VirtualDesktop.BodyState");
        pointer = MyKernel32.INSTANCE.MapViewOfFile(handle, WinNT.FILE_MAP_READ, 0, 0, 12288);
    }

    public boolean read() {
        this.buffer = pointer.getByteBuffer(0, 12288);

        this.faceIsValid = getBoolean();

        this.isEyeFollowingBlendShapesValid = getBoolean();

        for (int i = 0; i < this.expressionWeights.length; i++) {
            this.expressionWeights[i] = getFloat();
        }

        for (int i = 0; i < this.expressionConfidences.length; i++) {
            this.expressionConfidences[i] = getFloat();
        }

        this.isLeftEyeValid = getBoolean();
        this.isRightEyeValid = getBoolean();

        this.leftEyePose = getPose();
        this.rightEyePose = getPose();

        this.leftEyeConfidence = getFloat();
        this.rightEyeConfidence = getFloat();

        this.leftHandActive = getBoolean();
        this.rightHandActive = getBoolean();

        for (int i = 0; i < this.leftHandJointStates.length; i++) {
            this.leftHandJointStates[i] = getFingerJointState();
        }
        for (int i = 0; i < this.rightHandJointStates.length; i++) {
            this.rightHandJointStates[i] = getFingerJointState();
        }

        this.leftAimState = getHandTrackingAimState();
        this.rightAimState = getHandTrackingAimState();

        this.isBodyTrackingCalibrated = getBoolean();
        this.isBodyTrackingHighFidelity = getBoolean();
        this.bodyTrackingConfidence = getFloat();

        for (int i = 0; i < this.bodyJoints.length; i++) {
            this.bodyJoints[i] = getBodyJointLocation();
        }

        for (int i = 0; i < this.skeletonJoints.length; i++) {
            this.skeletonJoints[i] = getSkeletonJoint();
        }

        this.skeletonChangedCount = getInt();

        return true;
    }

    private Quaternionf getQuaternion() {
        Quaternionf quat = new Quaternionf(getFloat(), getFloat(), getFloat(), getFloat());
        align(4);
        return quat;
    }

    private Vector3f getVector3f() {
        Vector3f vec = new Vector3f(getFloat(), getFloat(), getFloat());
        align(4);
        return vec;
    }

    private Pose getPose() {
        Pose pose = new Pose(getQuaternion(), getVector3f());
        align(4);
        return pose;
    }

    private FingerJointState getFingerJointState() {
        FingerJointState fjs = new FingerJointState(getPose(), getFloat(), getVector3f(), getVector3f());
        align(4);
        return fjs;
    }

    private HandTrackingAimState getHandTrackingAimState() {
        HandTrackingAimState htas = new HandTrackingAimState(getLong(), getPose(), getFloat(), getFloat(), getFloat(), getFloat());
        align(8);
        return htas;
    }

    private BodyJointLocation getBodyJointLocation() {
        BodyJointLocation bjl = new BodyJointLocation(getLong(), getPose());
        align(8);
        return bjl;
    }

    private SkeletonJoint getSkeletonJoint() {
        SkeletonJoint sj = new SkeletonJoint(buffer.getInt(), buffer.getInt(), getPose());
        align(4);
        return sj;
    }

    private boolean getBoolean() {
        return buffer.get() != 0;
    }

    private float getFloat() {
        align(4);
        return buffer.getFloat();
    }

    private int getInt() {
        align(4);
        return buffer.getInt();
    }
    
    private long getLong() {
        align(8);
        return buffer.getLong();
    }

    private void align(int alignAmount) {
        while (buffer.position() % alignAmount != 0) {
            buffer.get();
        }
    }


}
