package com.metaphore;

import com.metaphore.war3keybinder.utils.Utils;

import java.io.File;

public class NativeInput {
    static {
        File lib = Utils.copyToTempFile("NativeInput.dll");
        System.load(lib.getAbsolutePath());
    }

    public native void performClick(int x, int y);
}
