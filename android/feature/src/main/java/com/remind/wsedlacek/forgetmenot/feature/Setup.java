package com.remind.wsedlacek.forgetmenot.feature;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Arrays;

public class Setup extends AppCompatActivity {

    final Context mContext = this;

    ArrayList<View> mViews=new ArrayList<View>();
    View mEventOptions;
    View mNewEvent;
    View mExistingEvent;
    Button mNewEventBtn;
    Button mExistingEventBtn;
    FloatingActionButton mFAB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        mEventOptions = (View) findViewById(R.id.event_options);
        mNewEvent = (View) findViewById(R.id.new_event);
        mExistingEvent = (View) findViewById(R.id.existing_event);
        mNewEventBtn = (Button) findViewById(R.id.new_event_btn);
        mExistingEventBtn = (Button) findViewById(R.id.existing_event_btn);
        mFAB = (FloatingActionButton) findViewById(R.id.fab);
        mViews.addAll(Arrays.asList(mEventOptions, mNewEvent, mExistingEvent));


        mNewEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateView(mNewEvent, true);
            }
        });

        mExistingEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateView(mExistingEvent, true);
            }
        });

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
                //intent.putExtra(EXTRA_MESSAGE, message);
                startActivity(intent);
            }
        });
    }

    //Intercept Back Button
    @Override
    public void onBackPressed() {
        if (mEventOptions.getVisibility() == View.GONE) {
            updateView(mEventOptions, false);
        } else {
            super.onBackPressed();
        }
    }

    public void updateView(View tViewToUse, Boolean tUseFAB) {
        for (View tView: mViews) {
            tView.setVisibility(View.GONE);
        }

        tViewToUse.setVisibility(View.VISIBLE);
        mFAB.setVisibility(tUseFAB ? View.VISIBLE : View.GONE);
    }

}
