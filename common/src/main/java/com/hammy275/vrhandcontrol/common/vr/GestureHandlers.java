package com.hammy275.vrhandcontrol.common.vr;

import com.hammy275.vrhandcontrol.common.vr.handlers.GestureHandler;
import com.hammy275.vrhandcontrol.common.vr.handlers.SnapHandler;

import java.util.ArrayList;
import java.util.List;

public class GestureHandlers {
    public static final List<GestureHandler> gestureHandlers = new ArrayList<>();

    static {
        gestureHandlers.add(new SnapHandler());
    }
}
