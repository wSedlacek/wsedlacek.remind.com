package com.remind.wsedlacek.forgetmenot.feature;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;

import java.util.Calendar;

public class Setup extends AppCompatActivity {
    private final Context mContext = this;
    private final String TAG = "SETUP";

    private TransitionDrawable mBackground;
    private FloatingActionButton mFAB;

    private EditText mEventName;
    private EditText mEventTime;
    private RadioGroup mFrequency;

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
        setmFrequency(DataManager.getData(DataManager.Data.FREQ));

        //Correct for nav bar height
        Resources resources = this.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            mFAB.setTranslationY(-1 * resources.getDimensionPixelSize(resourceId));
        }

        addButtonClickListeners();
        TimeSelector.init(mContext, new TimeSelector.ChangeListener() {
            @Override
            public void onChange() {
                mEventTime.setText(TimeSelector.getTimeText());
            }
        });
    }

    public void addButtonClickListeners() {
        mEventTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeSelector.getDialog().show();
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
        switch (tRadio.getText().toString()) {
            case "Hourly": return "3600";
            case "Daily": return "86400";
            case "Weekly": return "604800";
            default: return "";
        }
    }

    private int setmFrequency(String tFreq) {
        switch (tFreq) {
            case "3600":  return 0;
            case "86400": return 0;
            case "604800": return 0;
            default: return -1;
        }
    }

    //Skip Splash screen
    @Override
    public void onBackPressed() {
        this.moveTaskToBack(true);
    }
}
