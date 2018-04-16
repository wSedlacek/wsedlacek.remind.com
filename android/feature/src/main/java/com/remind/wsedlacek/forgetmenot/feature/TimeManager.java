package com.remind.wsedlacek.forgetmenot.feature;

import android.content.Context;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class TimeManager {
    private static String TAG = "TimeManager";

    private static boolean s24Hr;
    private static String sMyCountDownText;
    private static String sOtherCountDownText;
    private static Date sMyCountDown = new Date();
    private static Date sOtherCountDown = new Date();
    private static MonitoredVariable sCurrentTime = new MonitoredVariable(new Date());
    private static TimeManager.ChangeListener sListener;

    public static void init(final Context tContext) {
        s24Hr = Settings.System.getString(tContext.getContentResolver(), Settings.System.TIME_12_24).equals("24");
        addMonitoredVariableListeners();

        updateTime();
        //Update Time Every Minute on the minute.
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                updateTime();
                Date tDate = (Date)sCurrentTime.get();
                handler.postDelayed(this, (60 - tDate.getSeconds())*1000);
            }
        };
        handler.postDelayed(runnable, 0);
    }

    public static boolean use24Hr() {
        return s24Hr;
    }

    public static void setListener(TimeManager.ChangeListener tListener) {
        sListener = tListener;
    }

    public static void updateCountDowns() {
        try {
            sMyCountDown = convertStringToDate(DataManager.getData(DataManager.Data.TIME));
            updateCountDownText();
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
        sCurrentTime.setListener(new MonitoredVariable.ChangeListener() {
            @Override
            public void onChange() {
                updateCountDownText();
            }
        });
    }

    public static void updateCountDownText() {
        Date tCountDown = calcCountDown(sMyCountDown);
        sMyCountDownText = String.valueOf(new SimpleDateFormat("H:mm", Locale.US).format(tCountDown));
        sOtherCountDownText = String.valueOf(sOtherCountDown);
        if (sListener != null) sListener.onChange();
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

    public static String getCorrectTimeFormat(int tHr, int tMin) {
        return s24Hr ? convertTo24HrString(tHr, tMin) : convertTo12HrString(tHr, tMin);
    }

    public static Date convertStringToDate(String tTimeStr) throws Exception {
        return correctToCurrentDate(new SimpleDateFormat(getTimeFormat(), Locale.US).parse(tTimeStr) );
    }

    public static Date correctToCurrentDate(Date tTime) {
        Date tDate = new Date();
        tDate.setHours(tTime.getHours());
        tDate.setMinutes(tTime.getMinutes());
        tDate.setSeconds(tTime.getSeconds());
        return tDate;
    }

    private static Date correctUTC(Date tTime) {
        long tTimeMs = tTime.getTime();
        long tOffset = tTime.getTimezoneOffset() * 60 * 1000;
        return new Date(tTimeMs + tOffset);
    }

    private static String getTimeFormat() {
        return s24Hr ? "H:mm" : "h:mm a";
    }

    private String convertDateToString(Date tDate) {
        return new SimpleDateFormat(getTimeFormat(), Locale.US).format(tDate);
    }

    private static String convertTo12HrString(int tHr, int tMin) {
        String tSet = "";

        switch (tHr) {
            case 12: tSet = "PM"; break;
            case 0:  tSet = "AM"; tHr = 12; break;
            default:
                tSet = tHr > 12 ? "PM" : "AM";
                tHr = tHr > 12 ? tHr - 12 : tHr;
        }

        String tHrStr = String.valueOf(tHr);
        String tMinStr = tMin < 10 ? "0" + tMin : String.valueOf(tMin);
        return tHrStr + ":" + tMinStr + " " + tSet;
    }

    private static String convertTo24HrString(int tHr, int tMin) {
        String tHrStr = String.valueOf(tHr);
        String tMinStr = tMin < 10 ? "0" + tMin : String.valueOf(tMin);
        return tHrStr + ":" + tMinStr;
    }
}
