package com.remind.wsedlacek.forgetmenot.feature;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.TimePicker;

import java.util.Calendar;

public class TimeSelector {
    private static String TAG = "TimeSelector";

    private static String sTimeText;
    private static MonitoredVariable sHr = new MonitoredVariable(0);
    private static MonitoredVariable sMin = new MonitoredVariable(0);
    private static TimeSelector.ChangeListener sListener;

    public static void init(final Context tContext, TimeSelector.ChangeListener tListener) {
        sListener = tListener;
        addMonitoredVariableListeners();
        updateTime();
    }

    public interface ChangeListener {
        void onChange();
    }

    private static void addMonitoredVariableListeners() {
        sHr.setListener(new MonitoredVariable.ChangeListener() {
            @Override
            public void onChange() {
                updateTimeText();
            }
        });

        sMin.setListener(new MonitoredVariable.ChangeListener() {
            @Override
            public void onChange() {
                updateTimeText();
            }
        });
    }

    public static void updateTimeText() {
        int tHr = (int)sHr.get();
        int tMin = (int)sMin.get();
        sTimeText = TimeManager.getCorrectTimeFormat(tHr, tMin);
        if (sListener != null) sListener.onChange();
    }

    public static Dialog getDialog(final Context tContext) {
        return new TimePickerDialog(tContext, timePickerListener, (int)sHr.get(), (int)sMin.get(), TimeManager.use24Hr());
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
        final Calendar tCal = Calendar.getInstance();
        updateTime(tCal.get(Calendar.HOUR_OF_DAY), tCal.get(Calendar.MINUTE));
    }

    private static void updateTime(int tHr, int tMin) {
        sHr.set(tHr);
        sMin.set(tMin);
    }
}
