package com.x.stoverouter;

import android.util.Log;

public class StoveConstant {

    static boolean DEBUG;

    static String FIRE_RULER_INSTANCE_CLASS_NAME = "$$StoveRouterRule";
    static String FIRE_RULER_INSTANCE_PACKAGE_NAME = "com.x.stoverouter.generate";

    public static void Log(String msg) {
        if (DEBUG)
            Log.e("StoveRouter-Log", msg);
    }

}
