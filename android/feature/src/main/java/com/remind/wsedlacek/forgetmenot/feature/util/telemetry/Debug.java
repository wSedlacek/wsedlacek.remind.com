package com.remind.wsedlacek.forgetmenot.feature.util.telemetry;

import android.util.Log;

public class Debug {
    private static boolean DEBUG = true;

    public static void Log(String TAG, String msg) {
        if (DEBUG) Log.d(TAG, msg);
    }
}
