package com.remind.wsedlacek.forgetmenot.feature.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.remind.wsedlacek.forgetmenot.feature.R;
import com.remind.wsedlacek.forgetmenot.feature.backend.BackgroundManager;
import com.remind.wsedlacek.forgetmenot.feature.backend.DataManager;
import com.remind.wsedlacek.forgetmenot.feature.backend.TimeManager;
import com.remind.wsedlacek.forgetmenot.feature.services.firebase.FirebaseMessaging;
import com.remind.wsedlacek.forgetmenot.feature.util.telemetry.Debug;
import com.remind.wsedlacek.forgetmenot.feature.util.data.MonitoredVariable;
import com.remind.wsedlacek.forgetmenot.feature.util.feedback.Vibrate;
import com.remind.wsedlacek.forgetmenot.feature.util.animation.Wobble;

public class Buzz extends AppCompatActivity {
    private final Context mContext = this;
    private final String TAG = "Buzz";

    private TextView mMyEventName;
    private TextView mMyEventTime;
    private TextView mOtherEventName;
    private TextView mOtherEventTime;

    private FloatingActionButton mFAB;
    private MonitoredVariable<Boolean> mFABPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buzz);

        BackgroundManager.setBackground(this);
        BackgroundManager.animateBackground(false);

        Debug.Log(TAG, "Fetching Controls...");
        mMyEventName = findViewById(R.id.my_event_name);
        mMyEventTime = findViewById(R.id.my_event_time);
        mOtherEventName = findViewById(R.id.other_event_name);
        mOtherEventTime = findViewById(R.id.other_event_time);

        mMyEventName.setText((String) DataManager.sNameData.get());
        mOtherEventName.setText((String) DataManager.sOtherNameData.get());

        mFAB = findViewById(R.id.fab);
        mFABPressed = new MonitoredVariable<>(false);

        addButtonClickListeners();
        addMonitoredVariableListeners();

        FirebaseMessaging.setMessageListener(new FirebaseMessaging.MessageListener() {
            @Override
            public void onMessage() {
                buzzMe();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        TimeManager.init();
        TimeManager.setListener(new TimeManager.ChangeListener() {
            @Override
            public void onChange() {
                mMyEventTime.setText(TimeManager.sMyCountDown.getText());
                mMyEventTime.setTextColor(TimeManager.sMyCountDown.past() ? Color.RED : Color.WHITE);

                mOtherEventTime.setText(TimeManager.sOtherCountDown.getText());
                mOtherEventTime.setTextColor(TimeManager.sOtherCountDown.past() ? Color.RED : Color.WHITE);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        TimeManager.stop();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void addButtonClickListeners() {
        Debug.Log(TAG, "Connecting Buttons...");
        mFAB.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View tView, MotionEvent tMotionEvent) {
                if (tMotionEvent.getAction() == MotionEvent.ACTION_DOWN)  mFABPressed.set(true);
                if (tMotionEvent.getAction() == MotionEvent.ACTION_UP)  mFABPressed.set(false);
                return false;
            }
        });

        mFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View tView) {
                buzzOther();
            }
        });
    }

    private void addMonitoredVariableListeners() {
        Debug.Log(TAG, "Connecting Monitored Variables...");
        mFABPressed.setListener(new MonitoredVariable.ChangeListener() {
            @Override
            public void onChange() {
                Vibrate.vibrate(mFABPressed.get());
                Wobble.animateBuzz(mFAB, mContext, mFABPressed.get());

            }
        });
    }

    private void buzzOther() {
        Debug.Log(TAG, "Buzzing Friend...");
        TimeManager.sOtherCountDown.nextFreqency();
    }

    private void buzzMe() {
        Debug.Log(TAG, "You were Buzzed!");
        Vibrate.vibrate(200);
        Wobble.animateBuzz(mFAB, mContext, 200);
        TimeManager.sMyCountDown.nextFreqency();
    }

    //Trigger Disconnect
    @Override
    public void onBackPressed() {
        DataManager.sConnectedData.set(false);
    }
}
