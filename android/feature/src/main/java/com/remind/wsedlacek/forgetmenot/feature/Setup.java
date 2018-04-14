package com.remind.wsedlacek.forgetmenot.feature;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Arrays;

public class Setup extends AppCompatActivity {

    final Context mContext = this;

    TransitionDrawable mBackground;

    ArrayList<View> mViews = new ArrayList<View>();
    View mCurrentView;
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

        View tBackground = (View) findViewById(R.id.background);
        mBackground = (TransitionDrawable) tBackground.getBackground();
        
        mEventOptions = (View) findViewById(R.id.event_options);
        mNewEvent = (View) findViewById(R.id.new_event);
        mExistingEvent = (View) findViewById(R.id.existing_event);
        mNewEventBtn = (Button) findViewById(R.id.new_event_btn);
        mExistingEventBtn = (Button) findViewById(R.id.existing_event_btn);
        mFAB = (FloatingActionButton) findViewById(R.id.fab);
        mViews.addAll(Arrays.asList(mEventOptions, mNewEvent, mExistingEvent));

        updateView(mEventOptions, false, false);


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
        if (mEventOptions != mCurrentView) {
            this.updateView(mEventOptions, false, true, true);
        } else {
            super.onBackPressed();
        }
    }

    public void updateView(View tViewToUse) {
        updateView(tViewToUse, false);
    }

    public void updateView(View tViewToUse, Boolean tUseFAB) {
        updateView(tViewToUse, tUseFAB, true);
    }

    public void updateView(View tViewToUse, Boolean tUseFAB, Boolean tAnimate) {
        updateView(tViewToUse, tUseFAB, tAnimate, false);
    }

    public void updateView(View tViewToUse, Boolean tUseFAB, Boolean tAnimate, Boolean tInverseAnimation) {
        for (View tView: mViews) {
            tView.setVisibility(View.GONE);
        }

        mCurrentView = tViewToUse;
        mCurrentView.setVisibility(View.VISIBLE);
        mFAB.setVisibility(tUseFAB ? View.VISIBLE : View.GONE);
        if (tAnimate) updateBackground(tInverseAnimation);
    }


    public void updateBackground () {
        updateBackground(false);
    }

    public void updateBackground (Boolean tInverseAnimation) {
        if (tInverseAnimation) {
            mBackground.reverseTransition(1000);
        } else {
            mBackground.startTransition(1000);
        }
    }

}
