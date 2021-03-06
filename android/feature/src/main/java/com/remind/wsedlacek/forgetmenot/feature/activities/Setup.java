package com.remind.wsedlacek.forgetmenot.feature.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.remind.wsedlacek.forgetmenot.feature.R;
import com.remind.wsedlacek.forgetmenot.feature.backend.BackgroundManager;
import com.remind.wsedlacek.forgetmenot.feature.backend.DataManager;
import com.remind.wsedlacek.forgetmenot.feature.dialogs.DateSelector;
import com.remind.wsedlacek.forgetmenot.feature.dialogs.TimeSelector;
import com.remind.wsedlacek.forgetmenot.feature.util.telemetry.Debug;

public class Setup extends AppCompatActivity {
    private final Context mContext = this;
    private final String TAG = "SETUP";

    private FloatingActionButton mFAB;

    private RadioGroup mFrequency;
    private RadioButton mHourly;
    private RadioButton mDaily;
    private RadioButton mWeekly;
    private RadioButton[] mRadioButtons;

    private EditText mEventName;
    private EditText mEventTime;
    private EditText mEventDate;
    private TimeSelector mTimeSelector;
    private DateSelector mDateSelector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        BackgroundManager.setBackground(this);

        Debug.Log(TAG, "Fetching Controls...");
        mFrequency = findViewById(R.id.event_freqency);
        mHourly = findViewById(R.id.hourly);
        mDaily = findViewById(R.id.weekly);
        mWeekly = findViewById(R.id.daily);
        mRadioButtons = new RadioButton[]{mHourly, mDaily, mWeekly};

        mEventName = findViewById(R.id.event_name);
        mEventTime = findViewById(R.id.event_time);
        mEventDate = findViewById(R.id.event_date);
        mFAB = findViewById(R.id.fab);

        Debug.Log(TAG, "Filling in fields with current values...");
        mEventName.setText((String) DataManager.sNameData.get());
        mEventTime.setText((String) DataManager.sTimeData.get());
        mEventDate.setText((String) DataManager.sDateData.get());
        setFrequency((String) DataManager.sFreqData.get());

        addButtonClickListeners();
        addSelectorChangeListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        BackgroundManager.animateBackground(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        BackgroundManager.animateBackground(false);
    }

    public void addButtonClickListeners() {
        Debug.Log(TAG, "Connecting Buttons...");
        mFrequency.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                setFrequency(getFrequency());
            }
        });

        mEventName.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View tView, int tKey, KeyEvent tEvent) {
                // If the event is a key-down event on the "enter" button
                if ((tEvent.getAction() == KeyEvent.ACTION_DOWN) &&
                        (tKey == KeyEvent.KEYCODE_ENTER)) {
                    mFAB.performClick();
                    return true;
                }
                return false;
            }
        });

        mEventTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimeSelector.getDialog(mContext).show();
            }
        });

        mEventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDateSelector.getDialog(mContext).show();
            }
        });

        mFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tError = checkData();
                if (tError.equals("")) {
                    DataManager.sNameData.set(mEventName.getText().toString());
                    DataManager.sTimeData.set(mEventTime.getText().toString());
                    DataManager.sDateData.set(mEventDate.getText().toString());
                    DataManager.sFreqData.set(getFrequency());
                    DataManager.sConnectedData.set(true);
                } else {
                    Snackbar.make(view, "Error! Please check " + tError + " & try again.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null)
                            .show();
                }
            }
        });
    }

    public void addSelectorChangeListeners() {
        Debug.Log(TAG, "Connecting Selectors...");
        mTimeSelector = new TimeSelector(new TimeSelector.ChangeListener() {
            @Override
            public void onChange() {
                if(mTimeSelector != null) mEventTime.setText(mTimeSelector.getText());
            }
        });

        mDateSelector = new DateSelector(new DateSelector.ChangeListener() {
            @Override
            public void onChange() {
                if(mDateSelector != null) mEventDate.setText(mDateSelector.getText());
            }
        });
    }

    private String getFrequency() {
        RadioButton tCheckedRadioButton = findViewById(mFrequency.getCheckedRadioButtonId());
        return (String) tCheckedRadioButton.getText();
    }

    private void setFrequency(String tFreq) {
        if (tFreq != null) {
            for (RadioButton tRadioButton : mRadioButtons) {
                tRadioButton.setChecked(tRadioButton.getText().equals(tFreq));
            }

            switch (tFreq) {
                case "Hourly":
                    mEventDate.setVisibility(View.GONE);
                    break;
                case "Daily":
                    mEventDate.setVisibility(View.GONE);
                    break;
                case "Weekly":
                    mEventDate.setVisibility(View.VISIBLE);
                    break;
                default:
            }
        }
    }

    private String checkData() {
        if (mEventName.getText().toString().equals("")) return "Name";
        if (mEventTime.getText().toString().equals(""))return "Time";
        if (mEventDate.getText().toString().equals("")) return "Date";
        if (getFrequency().equals("")) return "Freqency";

        return "";
    }

    //Skip Splash screen
    @Override
    public void onBackPressed() {
        this.moveTaskToBack(true);
    }
}
