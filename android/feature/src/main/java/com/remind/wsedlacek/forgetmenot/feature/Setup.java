package com.remind.wsedlacek.forgetmenot.feature;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Time;

public class Setup extends AppCompatActivity {
    private final Context mContext = this;
    private final String TAG = "SETUP";

    private TransitionDrawable mBackground;
    private FloatingActionButton mFAB;

    private String mEventName;
    private Time mEventTime;
    private int mEventFrequency;

    private EditText mEventNameField;
    private EditText mEventTimeField;
    private RadioGroup mFrequencyField;

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
                DataManager.setData(DataManager.Data.CONNECTED, "1");
            }
        });
    }

    //Skip Splash screen
    @Override
    public void onBackPressed() {
        this.moveTaskToBack(true);
    }
}
