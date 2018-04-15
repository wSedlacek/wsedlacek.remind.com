package com.remind.wsedlacek.forgetmenot.feature;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.provider.Settings;
import android.widget.TimePicker;

import java.util.Calendar;

public class TimeSelector {
    private static Context sContext;

    private static boolean s24Hr;
    private static String sTimeText;
    private static MonitoredInteger sHr = new MonitoredInteger();
    private static MonitoredInteger sMin = new MonitoredInteger();
    private static TimeSelector.ChangeListener sListener;

    public static void init(Context tContext, TimeSelector.ChangeListener tListener) {
        sContext = tContext;
        sListener = tListener;
        s24Hr = Settings.System.getString(sContext.getContentResolver(), Settings.System.TIME_12_24).equals("24");
        addMonitoredVariableListeners();
        updateTime();
    }

    public interface ChangeListener {
        void onChange();
    }

    private static void addMonitoredVariableListeners() {
        sHr.setListener(new MonitoredInteger.ChangeListener() {
            @Override
            public void onChange() {
                updateTimeText();
            }
        });

        sMin.setListener(new MonitoredInteger.ChangeListener() {
            @Override
            public void onChange() {
                updateTimeText();
            }
        });
    }

    public static void updateTimeText() {
        sTimeText = s24Hr ? convertTo24HrString(sHr.get(), sMin.get()) : convertTo12HrString(sHr.get(), sMin.get());
        if (sListener != null) sListener.onChange();
    }

    public static Dialog getDialog() {
        return new TimePickerDialog(sContext, timePickerListener, sHr.get(), sMin.get(), s24Hr);
    }

    public static String getTimeText() {
        return sTimeText;
    }

    private static TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker tView, int tHr, int tMin) {
            updateTime(tHr, tMin);
        }
    };

    private static void updateTime() {
        final Calendar c = Calendar.getInstance();
        updateTime(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));
    }

    private static void updateTime(int tHr, int tMin) {
        sHr.set(tHr);
        sMin.set(tMin);
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
