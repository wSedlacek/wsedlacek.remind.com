package com.remind.wsedlacek.forgetmenot.feature.util.data.time;

import android.os.Handler;

import com.remind.wsedlacek.forgetmenot.feature.util.data.MonitoredVariable;
import com.remind.wsedlacek.forgetmenot.feature.util.telemetry.Debug;

import java.util.Calendar;

public class Clock extends MonitoredVariable<Calendar>{
    private static String TAG = "Clock";
    private static Handler mClock;

    public Clock() {
        super(Calendar.getInstance(), null);
    }

    public Clock(ChangeListener tListener) {
        super(Calendar.getInstance(), tListener);
    }

    public void start() {
        Debug.Log(TAG, "Starting Clock...");
        mClock = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (mData != null) {
                    tic();
                    mClock.postDelayed(this, (60 - mData.get(Calendar.SECOND)) * 1000);
                } else {
                    Debug.Log(TAG, "Error with time... Stopping Clock!");
                }
            }
        };
        mClock.postDelayed(runnable, 0);
    }

    public void stop() {
        Debug.Log(TAG, "Stoping Clock...");
        mData = null;
        mClock.removeCallbacksAndMessages(null);
    }

    private void tic() {
        mData = Calendar.getInstance();
        notifyChange();
        Debug.Log(TAG, "Tic - New time is " + Calendar.getInstance().getTime());
    }
}
