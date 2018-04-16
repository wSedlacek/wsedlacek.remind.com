package com.remind.wsedlacek.forgetmenot.feature.activities;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Surface;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.remind.wsedlacek.forgetmenot.feature.R;
import com.remind.wsedlacek.forgetmenot.feature.backend.DataManager;
import com.remind.wsedlacek.forgetmenot.feature.dialogs.TimeSelector;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;
import static android.content.res.Configuration.ORIENTATION_PORTRAIT;

public class Setup extends AppCompatActivity {
    private final Context mContext = this;
    private final String TAG = "SETUP";

    private TransitionDrawable mBackground;
    private FloatingActionButton mFAB;

    private EditText mEventName;
    private EditText mEventTime;
    private RadioGroup mFrequency;
    private TimeSelector mTimeSelector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        View tBackground = (View) findViewById(R.id.background);
        mBackground = (TransitionDrawable) tBackground.getBackground();

        mEventName = (EditText) findViewById(R.id.event_name);
        mEventTime = (EditText) findViewById(R.id.event_time);
        mFrequency = (RadioGroup) findViewById(R.id.event_freqency);
        mFAB = (FloatingActionButton) findViewById(R.id.fab);

        mEventName.setText(DataManager.getData(DataManager.Data.NAME));
        mEventTime.setText(DataManager.getData(DataManager.Data.TIME));
        setFrequency(DataManager.getData(DataManager.Data.FREQ));

        addButtonClickListeners();
        mTimeSelector = new TimeSelector(new TimeSelector.ChangeListener() {
            @Override
            public void onChange() {
                if(mTimeSelector != null) mEventTime.setText(mTimeSelector.getTimeText());
            }
        });

    }

    public void addButtonClickListeners() {
        mEventTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimeSelector.getDialog(mContext).show();
            }
        });

        mFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataManager.setData(DataManager.Data.NAME, mEventName.getText().toString());
                DataManager.setData(DataManager.Data.TIME, mEventTime.getText().toString());
                DataManager.setData(DataManager.Data.FREQ, getFrequency());
                DataManager.setData(DataManager.Data.CONNECTED, "1");
            }
        });
    }

    private String getFrequency() {
        RadioButton tRadio = (RadioButton) findViewById(mFrequency.getCheckedRadioButtonId());
        return (String) tRadio.getText();
    }

    private void setFrequency(String tFreq) {
        switch (tFreq) {
            case "Hourly":  break;
            case "Daily": break;
            case "Weekly": break;
            default:
        }
    }

    //Skip Splash screen
    @Override
    public void onBackPressed() {
        this.moveTaskToBack(true);
    }
}
