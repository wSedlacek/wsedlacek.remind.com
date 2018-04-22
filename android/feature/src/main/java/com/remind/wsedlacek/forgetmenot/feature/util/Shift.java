package com.remind.wsedlacek.forgetmenot.feature.util;

import android.util.Log;

import com.remind.wsedlacek.forgetmenot.feature.util.data.MonitoredVariable;
import com.remind.wsedlacek.forgetmenot.feature.util.telemetry.Debug;

import static java.lang.Thread.sleep;

public class Shift {
    private static String TAG = "Shift";

    //175 -> 67,      2000,    100
    public static void changeOverTime(final int tFrom, final int tTo, final long tTime, final long tUpdateFreq, final MonitoredVariable<Integer> tVar, final String tTAG) {
        if (tTime < tUpdateFreq) { Log.e(TAG, "Time must be less then update freq."); }
        if (tVar == null) { Log.e(TAG, "Container cannot be null."); }
        else {
            Debug.Log("TESTING", "SHIFTING [" + tTAG + "]: " + tFrom + " -> " + tTo);
            final Thread tBackgroundThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    float tSteps = tTime / tUpdateFreq; // 2000/100 = 20
                    float tInterval = (tTo - tFrom) / tSteps; // 67-175 = -108/20 = -5.4
                    float tVal = tFrom;
                    while (Math.round(tVal) != tTo) {
                        tVal += tInterval;
                        tryToSleep(tUpdateFreq);
                        Debug.Log("TESTING", "Setting value [" + tTAG + "] to " + Math.round(tVal));
                        tVar.set(Math.round(tVal));
                    }
                }
            });
            tVar.set(tFrom);
            tBackgroundThread.start();
        }
    }

    private static void tryToSleep(long tTime) {
        try { sleep(tTime); }
        catch (InterruptedException e) { e.printStackTrace(); }
    }


    // 0, 13, 27
    public static int calcPercDiff (float tPercent, int tZero, int tOne) {
        if (tPercent < 0f || tPercent > 1.0f) {
            Log.e(TAG, "PERCENT OUT SIDE OF RANGE!!");
            return tZero;
        }
        return Math.round(tZero + ((tOne-tZero)*tPercent));
    }
}


