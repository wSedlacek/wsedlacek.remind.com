package com.remind.wsedlacek.forgetmenot.feature.backend;

import android.os.Handler;
import android.util.Log;

import com.remind.wsedlacek.forgetmenot.feature.util.Debug;
import com.remind.wsedlacek.forgetmenot.feature.util.MonitoredVariable;

import java.util.Calendar;

import static com.remind.wsedlacek.forgetmenot.feature.util.TimeCorrection.convertStringToDate;
import static com.remind.wsedlacek.forgetmenot.feature.util.TimeCorrection.correctToCurrentDate;
import static com.remind.wsedlacek.forgetmenot.feature.util.TimeCorrection.correctUTC;

public class TimeManager {
    private static String TAG = "TimeManager";

    private static MonitoredVariable sCurrentTime;

    private static String sMyCountDownText;
    private static MonitoredVariable sMyCountDown;
    public static boolean sMyPastTimmer;

    private static String sOtherCountDownText;
    private static MonitoredVariable sOtherCountDown;
    public static boolean sOtherPastTimmer;

    private static TimeManager.ChangeListener sListener;
    private static Handler mClock;

    public static void init() {
        Debug.Log(TAG, "Initializing Variables...");
        sCurrentTime= new MonitoredVariable(Calendar.getInstance());

        sMyCountDown = new MonitoredVariable(Calendar.getInstance());
        sMyPastTimmer = false;

        sOtherCountDown = new MonitoredVariable(Calendar.getInstance());
        sOtherPastTimmer = false;

        addMonitoredVariableListeners();

        Debug.Log(TAG, "Starting Clock...");
        mClock = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (sCurrentTime != null) {
                    updateTime();
                    Calendar tDate = (Calendar) sCurrentTime.get();
                    mClock.postDelayed(this, 1000);
                } else {
                    Debug.Log(TAG, "Stopping Clock...");
                }
            }
        };
        mClock.postDelayed(runnable, 0);
    }

    public static void stop() {
        Debug.Log(TAG, "Clearing Time Manager...");
        sCurrentTime = null;
        sMyCountDown = null;
        sOtherCountDown = null;
    }

    public static void setListener(TimeManager.ChangeListener tListener) {
        sListener = tListener;
    }

    public static void updateCountDowns() {
        try {
            Debug.Log(TAG, "Updating Countdown...");
            sMyCountDown.set(convertStringToDate((String) DataManager.sTimeData.get()));
        } catch (Exception e) {
            Log.e(TAG, "Failed to convert firebase time to countdown.");
        }
    }

    public static String getMyCountDownText() {
        return sMyCountDownText;
    }
    public static String getOtherCountDownText() {
        return sOtherCountDownText;
    }


    public interface ChangeListener {
        void onChange();
    }

    private static void addMonitoredVariableListeners() {
        sMyCountDown.setListener(new MonitoredVariable.ChangeListener() {
            @Override
            public void onChange() {
                sMyCountDownText = updateCountDownText((Calendar)sMyCountDown.get(), sMyPastTimmer);
                if (sListener != null) sListener.onChange();
            }
        });
        sOtherCountDown.setListener(new MonitoredVariable.ChangeListener() {
            @Override
            public void onChange() {
                sOtherCountDownText = updateCountDownText((Calendar)sOtherCountDown.get(), sOtherPastTimmer);
                if (sListener != null) sListener.onChange();
            }
        });
        sCurrentTime.setListener(new MonitoredVariable.ChangeListener() {
            @Override
            public void onChange() {
                sMyCountDownText = updateCountDownText((Calendar)sMyCountDown.get(), sMyPastTimmer);
                //sOtherCountDownText = updateCountDownText((Calendar)sOtherCountDown.get(), sOtherPastTimmer);
                if (sListener != null) sListener.onChange();
            }
        });
    }

    private static String updateCountDownText(Calendar tCountDown, boolean tLate) {
        Debug.Log(TAG, "Updating Countdown Text...");
        tCountDown = calcCountDown(tCountDown);

        int tHour = tCountDown.get(Calendar.HOUR_OF_DAY);
        int tMin = tCountDown.get(Calendar.MINUTE);
        int tSec = tCountDown.get(Calendar.SECOND);

        String tReturn = tLate ? "LATE: " : "in ";
        tReturn += tHour != 0 ? tHour + "h " : "";
        tReturn += tHour != 0 || tMin != 0  ? tMin + "m " : "";
        tReturn += tSec + "s ";
        return tReturn;
    }

    private static Calendar calcCountDown(Calendar tCountDown) {
        Debug.Log(TAG, "Calculating Countdown...");
        Calendar tDate = (Calendar) sCurrentTime.get();

        Debug.Log(TAG, "Comparing Current Time and Countdown Timer");
        long diff =  tCountDown.getTimeInMillis() - tDate.getTimeInMillis();
        Debug.Log(TAG, "Time Difference: " + diff + "ms");
        if (Math.abs(diff) != diff) {
            diff *= -1;
            sMyPastTimmer = true;
        }
        Debug.Log(TAG, "Time in past: " + sMyPastTimmer);

        Calendar tReturn = Calendar.getInstance();
        tReturn.setTimeInMillis(diff);

        Debug.Log(TAG, "Correcing Timezone and setting to current date...");
        correctUTC(tReturn);
        correctToCurrentDate(tReturn);
        return tReturn;
    }

    private static void updateTime() {
        sCurrentTime.set(Calendar.getInstance());
        Debug.Log(TAG, "Tic - New time is " + Calendar.getInstance().getTime());
    }
}