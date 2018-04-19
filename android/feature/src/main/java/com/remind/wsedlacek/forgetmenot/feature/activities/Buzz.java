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
import com.remind.wsedlacek.forgetmenot.feature.backend.DataManager;
import com.remind.wsedlacek.forgetmenot.feature.backend.TimeManager;
import com.remind.wsedlacek.forgetmenot.feature.services.FirebaseMessaging;
import com.remind.wsedlacek.forgetmenot.feature.util.Debug;
import com.remind.wsedlacek.forgetmenot.feature.util.MonitoredVariable;
import com.remind.wsedlacek.forgetmenot.feature.util.Vibrate;
import com.remind.wsedlacek.forgetmenot.feature.util.Wobble;

import java.util.Date;

public class Buzz extends AppCompatActivity {
    private final Context mContext = this;
    private final String TAG = "Buzz";

    private TextView mMyEventName;
    private TextView mMyEventTime;
    private TextView mOtherEventName;
    private TextView mOtherEventTime;

    private FloatingActionButton mFAB;
    private MonitoredVariable mFABPressed;

    private Date mMyEventTimeCountDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buzz);

        Debug.Log(TAG, "Fetching Controls...");
        mMyEventName = (TextView) findViewById(R.id.my_event_name);
        mMyEventTime = (TextView) findViewById(R.id.my_event_time);
        mOtherEventName = (TextView) findViewById(R.id.other_event_name);
        mOtherEventTime = (TextView) findViewById(R.id.other_event_time);

        mMyEventName.setText((String) DataManager.sNameData.get());
        //mMyEventTime.setText(DataManager.getData(DataManager.sTimeData));

        mFAB = (FloatingActionButton) findViewById(R.id.fab);
        mFABPressed = new MonitoredVariable(false);

        addButtonClickListeners();
        addMonitoredVariableListeners();

        FirebaseMessaging.setChangeListener(new Runnable() {
            @Override
            public void run() {
                buzzMe();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        TimeManager.init();
        TimeManager.setListener(new TimeManager.ChangeListener() {
            @Override
            public void onChange() {
                mMyEventTime.setText(TimeManager.getMyCountDownText());
                mMyEventTime.setTextColor(TimeManager.sMyPastTimmer ? Color.RED : Color.WHITE);
            }
        });
        TimeManager.updateCountDowns();
    }

    @Override
    public void onStop() {
        super.onStop();
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
                Vibrate.vibrate((boolean)mFABPressed.get());
                Wobble.animateBuzz(mFAB, mContext, (boolean)mFABPressed.get());

            }
        });
    }

    private void buzzOther() {
        Debug.Log(TAG, "Buzzing Friend...");
    }

    private void buzzMe() {
        Debug.Log(TAG, "You were Buzzed!");
        Vibrate.vibrate(200);
        Wobble.animateBuzz(mFAB, mContext, 200);
    }

    //Trigger Disconnect
    @Override
    public void onBackPressed() {
        DataManager.sConnectedData.set("0");
    }
}
