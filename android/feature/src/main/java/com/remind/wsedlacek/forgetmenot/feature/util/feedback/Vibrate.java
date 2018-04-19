package com.remind.wsedlacek.forgetmenot.feature.util.feedback;

import android.content.Context;
import android.os.Vibrator;

public class Vibrate {
    private static Vibrator sVibrator;

    public static void init(Context tContext) {
        sVibrator = (Vibrator) tContext.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public static void vibrate(long tLength) {
        sVibrator.vibrate(tLength);
    }

    public static void vibrate(boolean tVibrate) {
        if (tVibrate) startVibrate();
        else stopVibrate();
    }

    public static void startVibrate() {
        sVibrator.vibrate(60000);
    }

    public static void stopVibrate() {
        sVibrator.cancel();
    }
}
