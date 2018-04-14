package com.remind.wsedlacek.forgetmenot.feature;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class Setup extends AppCompatActivity {

    final Context mContext = this;

    TransitionDrawable mBackground;

    FloatingActionButton mFAB;

    String mID;
    String mEventName;
    Time mEventTime;
    int mEventFrequency;

    EditText mEventNameField;
    EditText mEventTimeField;
    RadioGroup mFrequencyField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        View tBackground = (View) findViewById(R.id.background);
        mBackground = (TransitionDrawable) tBackground.getBackground();

        mFAB = (FloatingActionButton) findViewById(R.id.fab);

        //Correct for nav bar height
        Resources resources = this.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            mFAB.setTranslationY(-1 * resources.getDimensionPixelSize(resourceId));
        }

        mFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent intent = new Intent(mContext, Buzz.class);

            ArrayList tExtra = new ArrayList();
            tExtra.addAll(Arrays.asList(mID, mEventName, mEventTime));

            intent.putExtra(EXTRA_MESSAGE, tExtra);
            startActivity(intent);
            }
        });
    }

    //Skip Splash screen
    @Override
    public void onBackPressed() {
        this.moveTaskToBack(true);
    }
}
