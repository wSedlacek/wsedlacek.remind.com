package com.remind.wsedlacek.forgetmenot.feature.backend;

import android.os.Handler;
import android.util.Log;

import com.remind.wsedlacek.forgetmenot.feature.util.MonitoredVariable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.remind.wsedlacek.forgetmenot.feature.util.TimeCorrection.convertStringToDate;
import static com.remind.wsedlacek.forgetmenot.feature.util.TimeCorrection.correctToCurrentDate;
import static com.remind.wsedlacek.forgetmenot.feature.util.TimeCorrection.correctUTC;

public class TimeManager {
    private static String TAG = "TimeManager";


    private static String sMyCountDownText;
    private static String sOtherCountDownText;
    private static MonitoredVariable sMyCountDown = new MonitoredVariable(new Date());
    private static MonitoredVariable sOtherCountDown = new MonitoredVariable(new Date());
    private static MonitoredVariable sCurrentTime = new MonitoredVariable(new Date());
    private static TimeManager.ChangeListener sListener;

    public static void init() {
        addMonitoredVariableListeners();

        updateTime();
        //Update Time Every Minute on the minute.
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                updateTime();
                Date tDate = (Date)sCurrentTime.get();
                handler.postDelayed(this, 1000);
            }
        };
        handler.postDelayed(runnable, 0);
    }

    public static void setListener(TimeManager.ChangeListener tListener) {
        sListener = tListener;
    }

    public static void updateCountDowns() {
        try {
            sMyCountDown.set(convertStringToDate(DataManager.getData(DataManager.Data.TIME)));
        } catch (Exception e) {
            Log.e(TAG, "Failed to convert firebase time to countdown.");
        }
    }

    public static String getMyCountDownText() {
        return sMyCountDownText;
    }

    public interface ChangeListener {
        void onChange();
    }

    private static void addMonitoredVariableListeners() {
        sMyCountDown.setListener(new MonitoredVariable.ChangeListener() {
            @Override
            public void onChange() {
                sMyCountDownText = updateCountDownText((Date)sMyCountDown.get());
                if (sListener != null) sListener.onChange();
            }
        });
        sOtherCountDown.setListener(new MonitoredVariable.ChangeListener() {
            @Override
            public void onChange() {
                sOtherCountDownText = updateCountDownText((Date)sOtherCountDown.get());
                if (sListener != null) sListener.onChange();
            }
        });
        sCurrentTime.setListener(new MonitoredVariable.ChangeListener() {
            @Override
            public void onChange() {
                sMyCountDownText = updateCountDownText((Date)sMyCountDown.get());
                sOtherCountDownText = updateCountDownText((Date)sOtherCountDown.get());
                if (sListener != null) sListener.onChange();
            }
        });
    }

    private static String updateCountDownText(Date tCountDown) {
        tCountDown = calcCountDown(tCountDown);
        return String.valueOf(new SimpleDateFormat("H m s", Locale.US).format(tCountDown));
    }

    private static Date calcCountDown(Date tCountDown) {
        Date tDate = (Date)sCurrentTime.get();
        long diff =  tCountDown.getTime() - tDate.getTime();
        return correctToCurrentDate(correctUTC(new Date(diff)));
    }

    private static void updateTime() {
        final Calendar tCal = Calendar.getInstance();
        sCurrentTime.set(tCal.getTime());
    }
}