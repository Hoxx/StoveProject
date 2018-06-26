package com.x.firerouter;

import android.util.Log;

public class FireConstant {

    static boolean DEBUG;

    static String FIRE_RULER_INSTANCE_CLASS_NAME = "$$FireRuler";
    static String FIRE_RULER_INSTANCE_PACKAGE_NAME = "com.x.firerouter.generate";

    public static void Log(String msg) {
        if (DEBUG)
            Log.e("FireRouter-Log", msg);
    }

}
