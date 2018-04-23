package com.remind.wsedlacek.forgetmenot.feature.util;

import android.os.CountDownTimer;
import android.util.Log;

import com.remind.wsedlacek.forgetmenot.feature.util.data.MonitoredVariable;

import java.util.ArrayList;

public class Shift {
    private static String TAG = "Shift";
    private static ArrayList<CountDownTimer> mShiftOverTimers = new ArrayList<>();

    public static void changeOverTime(final MonitoredVariable<Integer> tVar, final int tTo, final long tTime, final long tUpdateFreq) {
        if (tTime < tUpdateFreq) { Log.e(TAG, "Time must be greater then update freq."); }
        if (tVar == null) { Log.e(TAG, "Container cannot be null."); }
        else {
            float tSteps = tTime / tUpdateFreq;
            final float tInterval = (tTo - tVar.get()) / tSteps;
            final float[] tVal = {tVar.get()};

            CountDownTimer tTimer = new CountDownTimer(tTime, tUpdateFreq) {
                @Override
                public void onTick(long l) {
                    tVal[0] += tInterval;
                    tVar.set(Math.round(tVal[0]));
                }

                @Override
                public void onFinish() {
                    tVar.set(tTo);
                    mShiftOverTimers.remove(this);
                }
            };
            tTimer.start();
            mShiftOverTimers.add(tTimer);
        }
    }

    public static void cancelShift() {
        for (CountDownTimer tTimer: mShiftOverTimers) {
            tTimer.cancel();
        }
    }

    public static int calcPercDiff (float tPercent, int tZero, int tOne) {
        if (tPercent < 0f || tPercent > 1.0f) {
            Log.e(TAG, "PERCENT OUT SIDE OF RANGE!!");
            return tZero;
        }
        return Math.round(tZero + ((tOne-tZero)*tPercent));
    }
}


