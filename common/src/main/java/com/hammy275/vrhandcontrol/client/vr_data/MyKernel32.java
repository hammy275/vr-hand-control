package com.hammy275.vrhandcontrol.client.vr_data;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.win32.W32APIOptions;

public interface MyKernel32 extends Kernel32 {
    MyKernel32 INSTANCE = Native.loadLibrary(MyKernel32.class, W32APIOptions.DEFAULT_OPTIONS);
    WinNT.HANDLE OpenFileMapping(int dwDesiredAccess, boolean bInheritHandle, String lpName);
    Pointer MapViewOfFile(HANDLE handle, int var2, int var3, int var4, int var5);
}
