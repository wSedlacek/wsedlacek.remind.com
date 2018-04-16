package com.remind.wsedlacek.forgetmenot.feature.dialogs;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.TimePicker;

import com.remind.wsedlacek.forgetmenot.feature.util.MonitoredVariable;

import java.util.Calendar;

import static com.remind.wsedlacek.forgetmenot.feature.util.TimeCorrection.getCorrectTimeFormat;
import static com.remind.wsedlacek.forgetmenot.feature.util.TimeCorrection.use24Hr;

public class TimeSelector {
    private String TAG = "TimeSelector";

    private String mText;
    private MonitoredVariable mHr = new MonitoredVariable(0);
    private MonitoredVariable mMin = new MonitoredVariable(0);

    private TimeSelector.ChangeListener mListener;

    private TimePickerDialog.OnTimeSetListener mTimePickerListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker tView, int tHr, int tMin) {
            updateTime(tHr, tMin);
        }
    };

    public TimeSelector(TimeSelector.ChangeListener tListener) {
        mListener = tListener;
        addMonitoredVariableListeners();
    }

    public interface ChangeListener {
        void onChange();
    }

    private void addMonitoredVariableListeners() {
        mHr.setListener(new MonitoredVariable.ChangeListener() {
            @Override
            public void onChange() {
                updateText();
            }
        });
        mMin.setListener(new MonitoredVariable.ChangeListener() {
            @Override
            public void onChange() {
                updateText();
            }
        });
    }

    public void updateText() {
        int tHr = (int)mHr.get();
        int tMin = (int)mMin.get();
        mText = getCorrectTimeFormat(tHr, tMin);
        if (mListener != null) mListener.onChange();
    }

    public Dialog getDialog(final Context tContext) {
        final Calendar tCal = Calendar.getInstance();
        return new TimePickerDialog(tContext, mTimePickerListener, tCal.get(Calendar.HOUR_OF_DAY), tCal.get(Calendar.MINUTE), use24Hr());
    }

    public String getText() {
        return mText;
    }

    private void updateTime(int tHr, int tMin) {
        mHr.set(tHr);
        mMin.set(tMin);
    }
}
