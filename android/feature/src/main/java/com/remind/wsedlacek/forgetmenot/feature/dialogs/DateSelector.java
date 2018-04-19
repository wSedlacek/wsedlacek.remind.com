package com.remind.wsedlacek.forgetmenot.feature.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.widget.DatePicker;

import com.remind.wsedlacek.forgetmenot.feature.util.data.MonitoredVariable;

import java.util.Calendar;

import static com.remind.wsedlacek.forgetmenot.feature.util.TimeCorrection.getCorrectDateFormat;

public class DateSelector {
    private String TAG = "DateSelector";

    private String mText;
    private MonitoredVariable<Integer> mYear = new MonitoredVariable<>(0);
    private MonitoredVariable<Integer> mMonth = new MonitoredVariable<>(0);
    private MonitoredVariable<Integer> mDay = new MonitoredVariable<>(0);

    private DateSelector.ChangeListener mListener;

    private DatePickerDialog.OnDateSetListener mDatePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker tView, int tYear, int tMonth, int tDay) {
            updateDate(tYear, tMonth, tDay);
        }
    };

    public DateSelector(DateSelector.ChangeListener tListener) {
        mListener = tListener;
        addMonitoredVariableListeners();
    }

    public interface ChangeListener {
        void onChange();
    }

    private void addMonitoredVariableListeners() {
        mYear.setListener(new MonitoredVariable.ChangeListener() {
            @Override
            public void onChange() {
                updateText();
            }
        });
        mMonth.setListener(new MonitoredVariable.ChangeListener() {
            @Override
            public void onChange() {
                updateText();
            }
        });
        mDay.setListener(new MonitoredVariable.ChangeListener() {
            @Override
            public void onChange() {
                updateText();
            }
        });
    }


    public void updateText() {
        int tYear = mYear.get();
        int tMonth = mMonth.get();
        int tDay = mDay.get();
        mText = getCorrectDateFormat(tYear, tMonth, tDay);
        if (mListener != null) mListener.onChange();
    }

    public Dialog getDialog(final Context tContext) {
        final Calendar tCal = Calendar.getInstance();
        return new DatePickerDialog(tContext, mDatePickerListener, tCal.get(Calendar.YEAR), tCal.get(Calendar.MONTH), tCal.get(Calendar.DAY_OF_MONTH));
    }

    public String getText() {
        return mText;
    }

    private void updateDate(int tYear, int tMonth, int tDay) {
        mYear.set(tYear);
        mMonth.set(tMonth);
        mDay.set(tDay);
    }
}
