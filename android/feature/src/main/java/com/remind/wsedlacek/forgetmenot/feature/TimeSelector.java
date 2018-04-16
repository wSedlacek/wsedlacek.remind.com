package com.remind.wsedlacek.forgetmenot.feature;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.TimePicker;

import java.util.Calendar;

public class TimeSelector {
    private String TAG = "TimeSelector";

    private String mTimeText;
    private MonitoredVariable mHr = new MonitoredVariable(0);
    private MonitoredVariable mMin = new MonitoredVariable(0);
    private TimeSelector.ChangeListener mListener;

    public TimeSelector(TimeSelector.ChangeListener tListener) {
        mListener = tListener;
        addMonitoredVariableListeners();
        updateTime();
    }

    public interface ChangeListener {
        void onChange();
    }

    private void addMonitoredVariableListeners() {
        mHr.setListener(new MonitoredVariable.ChangeListener() {
            @Override
            public void onChange() {
                updateTimeText();
            }
        });
        mMin.setListener(new MonitoredVariable.ChangeListener() {
            @Override
            public void onChange() {
                updateTimeText();
            }
        });
    }

    public void updateTimeText() {
        int tHr = (int)mHr.get();
        int tMin = (int)mMin.get();
        mTimeText = TimeManager.getCorrectTimeFormat(tHr, tMin);
        if (mListener != null) mListener.onChange();
    }

    public Dialog getDialog(final Context tContext) {
        return new TimePickerDialog(tContext, timePickerListener, (int)mHr.get(), (int)mMin.get(), TimeManager.use24Hr());
    }

    public String getTimeText() {
        return mTimeText;
    }

    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker tView, int tHr, int tMin) {
        updateTime(tHr, tMin);
        }
    };

    private void updateTime() {
        final Calendar tCal = Calendar.getInstance();
        updateTime(tCal.get(Calendar.HOUR_OF_DAY), tCal.get(Calendar.MINUTE));
    }

    private void updateTime(int tHr, int tMin) {
        mHr.set(tHr);
        mMin.set(tMin);
    }
}
