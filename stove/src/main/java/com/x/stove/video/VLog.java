package com.x.stove.video;

import android.support.annotation.NonNull;
import android.util.Log;

/**
 * @author Houxingxiu
 * Date : 2018/11/1
 */
public class VLog {

    private static String V_LOG_TAG        = "--StoveVideoLog--";
    private static String V_LOG_FORMAT_MSG = "-> %s <------>>>:%s";

    public static void D(@NonNull Object o, Object msg) {
        Log.d(V_LOG_TAG, String.format(V_LOG_FORMAT_MSG, o.getClass().getCanonicalName(), msg));
    }

    public static void D(@NonNull Object o, String flag, Object msg) {
        D(o, flag + msg);
    }

}
