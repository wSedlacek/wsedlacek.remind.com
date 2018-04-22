package com.remind.wsedlacek.forgetmenot.feature.backend;

import android.os.Handler;
import android.util.Log;

import com.remind.wsedlacek.forgetmenot.feature.util.data.time.Clock;
import com.remind.wsedlacek.forgetmenot.feature.util.data.time.CountDown;
import com.remind.wsedlacek.forgetmenot.feature.util.telemetry.Debug;
import com.remind.wsedlacek.forgetmenot.feature.util.data.MonitoredVariable;

import java.util.Calendar;

import static com.remind.wsedlacek.forgetmenot.feature.util.TimeCorrection.convertStringToDate;
import static com.remind.wsedlacek.forgetmenot.feature.util.TimeCorrection.getCorrectDateFormat;
import static com.remind.wsedlacek.forgetmenot.feature.util.TimeCorrection.getCorrectTimeFormat;

public class TimeManager {
    private static String TAG = "TimeManager";

    private static Clock sClock;

    private static TimeManager.ChangeListener sListener;

    public static CountDown sMyCountDown;
    public static CountDown sOtherCountDown;

    public static void init() {
        Debug.Log(TAG, "Initializing Variables...");
        try {
            sClock = new Clock();
            sMyCountDown = new CountDown(DataManager.sTimeData, DataManager.sDateData, DataManager.sFreqData, BackgroundManager.mTop);
            sOtherCountDown = new CountDown(DataManager.sOtherTimeData, DataManager.sOtherDateData, DataManager.sOtherFreqData, BackgroundManager.mBottom);
            addMonitoredVariableListeners();
            sClock.start();
        } catch (Exception e) {
            Log.e(TAG, "Failed to start time management");
        }
    }

    private static void addMonitoredVariableListeners() {
        Debug.Log(TAG, "Connecting Monitored Variables...");
        sClock.setListener(new MonitoredVariable.ChangeListener() {
            @Override
            public void onChange() {
                sMyCountDown.updateCountDownText(sClock.get());
                sOtherCountDown.updateCountDownText(sClock.get());
                notifyChange();
            }
        });
        sMyCountDown.setListener(new MonitoredVariable.ChangeListener() {
            @Override
            public void onChange() {
                notifyChange();
            }
        });
        sOtherCountDown.setListener(new MonitoredVariable.ChangeListener() {
            @Override
            public void onChange() {
                notifyChange();
            }
        });
    }

    public static void stop() {
        sClock.stop();
    }

    public static void notifyChange() {
        if (sListener != null) sListener.onChange();
    }
    public static void setListener(TimeManager.ChangeListener tListener) {
        sListener = tListener;
    }
    public interface ChangeListener {
        void onChange();
    }
}