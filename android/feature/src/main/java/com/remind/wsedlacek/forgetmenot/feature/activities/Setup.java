package com.remind.wsedlacek.forgetmenot.feature.activities;

import android.content.Context;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.remind.wsedlacek.forgetmenot.feature.R;
import com.remind.wsedlacek.forgetmenot.feature.backend.DataManager;
import com.remind.wsedlacek.forgetmenot.feature.dialogs.DateSelector;
import com.remind.wsedlacek.forgetmenot.feature.dialogs.TimeSelector;
import com.remind.wsedlacek.forgetmenot.feature.util.telemetry.Debug;

public class Setup extends AppCompatActivity {
    private final Context mContext = this;
    private final String TAG = "SETUP";

    private TransitionDrawable mBackground;
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

        View tBackground = (View) findViewById(R.id.background);
        mBackground = (TransitionDrawable) tBackground.getBackground();

        Debug.Log(TAG, "Fetching Controls...");
        mFrequency = (RadioGroup) findViewById(R.id.event_freqency);
        mHourly = (RadioButton) findViewById(R.id.hourly);
        mDaily = (RadioButton) findViewById(R.id.weekly);
        mWeekly = (RadioButton) findViewById(R.id.daily);
        mRadioButtons = new RadioButton[]{mHourly, mDaily, mWeekly};

        mEventName = (EditText) findViewById(R.id.event_name);
        mEventTime = (EditText) findViewById(R.id.event_time);
        mEventDate = (EditText) findViewById(R.id.event_date);
        mFAB = (FloatingActionButton) findViewById(R.id.fab);

        Debug.Log(TAG, "Filling in fields with current values...");
        mEventName.setText((String) DataManager.sNameData.get());
        mEventTime.setText((String) DataManager.sTimeData.get());
        mEventDate.setText((String) DataManager.sDateData.get());
        setFrequency((String) DataManager.sFreqData.get());

        addButtonClickListeners();
        addSelectorChangeListeners();
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
                DataManager.sNameData.set(mEventName.getText().toString());
                DataManager.sTimeData.set(mEventTime.getText().toString());
                DataManager.sDateData.set(mEventDate.getText().toString());
                DataManager.sFreqData.set(getFrequency());
                DataManager.sConnectedData.set("1");
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
        RadioButton tCheckedRadioButton = (RadioButton) findViewById(mFrequency.getCheckedRadioButtonId());
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

    //Skip Splash screen
    @Override
    public void onBackPressed() {
        this.moveTaskToBack(true);
    }
}
