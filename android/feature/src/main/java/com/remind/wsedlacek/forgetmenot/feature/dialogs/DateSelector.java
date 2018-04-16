package com.remind.wsedlacek.forgetmenot.feature.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.remind.wsedlacek.forgetmenot.feature.backend.TimeManager;
import com.remind.wsedlacek.forgetmenot.feature.util.MonitoredVariable;

import java.util.Calendar;

import static com.remind.wsedlacek.forgetmenot.feature.util.TimeCorrection.getCorrectTimeFormat;
import static com.remind.wsedlacek.forgetmenot.feature.util.TimeCorrection.use24Hr;

public class TimeSelector {
    private String TAG = "TimeSelector";

    private String mTimeText;
    private MonitoredVariable mHr = new MonitoredVariable(0);
    private MonitoredVariable mMin = new MonitoredVariable(0);

    private String mDateText;
    private MonitoredVariable mYear = new MonitoredVariable(0);
    private MonitoredVariable mMonth = new MonitoredVariable(0);
    private MonitoredVariable mDay = new MonitoredVariable(0);

    private TimeSelector.ChangeListener mListener;

    private TimePickerDialog.OnTimeSetListener mTimePickerListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker tView, int tHr, int tMin) {
            updateTime(tHr, tMin);
        }
    };

    private DatePickerDialog.OnDateSetListener mDatePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker tView, int tYear, int tMonth, int tDay) {
            updateDate(tYear, tMonth, tDay);
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
                updateTimeText();
            }
        });
        mMin.setListener(new MonitoredVariable.ChangeListener() {
            @Override
            public void onChange() {
                updateTimeText();
            }
        });
        mYear.setListener(new MonitoredVariable.ChangeListener() {
            @Override
            public void onChange() {
                updateDateText();
            }
        });
        mMonth.setListener(new MonitoredVariable.ChangeListener() {
            @Override
            public void onChange() {
                updateDateText();
            }
        });
        mDay.setListener(new MonitoredVariable.ChangeListener() {
            @Override
            public void onChange() {
                updateDateText();
            }
        });
    }

    public void updateTimeText() {
        int tHr = (int)mHr.get();
        int tMin = (int)mMin.get();
        mTimeText = getCorrectTimeFormat(tHr, tMin);
        if (mListener != null) mListener.onChange();
    }

    public void updateDateText() {
        int tYear = (int)mYear.get();
        int tMonth = (int)mMonth.get();
        int tDay = (int)mDay.get();
        mDateText = getCorrectDateFormat(tYear, tMonth, tDay);
        if (mListener != null) mListener.onChange();
    }

    public Dialog getTimeDialog(final Context tContext) {
        final Calendar tCal = Calendar.getInstance();
        return new TimePickerDialog(tContext, mTimePickerListener, tCal.get(Calendar.HOUR_OF_DAY), tCal.get(Calendar.MINUTE), use24Hr());
    }

    public Dialog getDateDialog(final Context tContext) {
        final Calendar tCal = Calendar.getInstance();
        return new DatePickerDialog(tContext, mDatePickerListener);
    }

    public String getTimeText() {
        return mTimeText;
    }

    private void updateTime(int tHr, int tMin) {
        mHr.set(tHr);
        mMin.set(tMin);
    }

    private void updateDate(int tYear, int tMonth, int tDay) {
        mYear.set(tYear);
        mMonth.set(tMonth);
        mDay.set(tDay);
    }
}
