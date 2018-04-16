package com.remind.wsedlacek.forgetmenot.feature.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.remind.wsedlacek.forgetmenot.feature.R;
import com.remind.wsedlacek.forgetmenot.feature.backend.DataManager;
import com.remind.wsedlacek.forgetmenot.feature.backend.TimeManager;
import com.remind.wsedlacek.forgetmenot.feature.util.MonitoredVariable;
import com.remind.wsedlacek.forgetmenot.feature.util.Vibrate;

import java.util.Date;


public class Buzz extends AppCompatActivity {
    private final Context mContext = this;
    private final String TAG = "Buzz";

    private TextView mMyEventName;
    private TextView mMyEventTime;
    private TextView mOtherEventName;
    private TextView mOtherEventTime;

    private FloatingActionButton mFAB;
    private MonitoredVariable mFABPressed = new MonitoredVariable(false);

    private Date mMyEventTimeCountDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buzz);

        mMyEventName = (TextView) findViewById(R.id.my_event_name);
        mMyEventTime = (TextView) findViewById(R.id.my_event_time);
        mOtherEventName = (TextView) findViewById(R.id.other_event_name);
        mOtherEventTime = (TextView) findViewById(R.id.other_event_time);

        mMyEventName.setText(DataManager.getData(DataManager.Data.NAME));
        //mMyEventTime.setText(DataManager.getData(DataManager.Data.TIME));

        mFAB = (FloatingActionButton) findViewById(R.id.fab);


        TimeManager.setListener(new TimeManager.ChangeListener() {
            @Override
            public void onChange() {
                mMyEventTime.setText(TimeManager.getMyCountDownText());
            }
        });
        TimeManager.updateCountDowns();

        addButtonClickListeners();

        mFABPressed.setListener(new MonitoredVariable.ChangeListener() {
            @Override
            public void onChange() {
                Vibrate.vibrate((boolean)mFABPressed.get());
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    public void addButtonClickListeners() {
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

    private void buzzOther() {

    }

    private void buzzMe() {

    }

    @Override
    public void onBackPressed() {
        DataManager.setData(DataManager.Data.CONNECTED, "0");
    }
}
