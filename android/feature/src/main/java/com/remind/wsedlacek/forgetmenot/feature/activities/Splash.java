package com.remind.wsedlacek.forgetmenot.feature.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.remind.wsedlacek.forgetmenot.feature.R;
import com.remind.wsedlacek.forgetmenot.feature.backend.DataManager;
import com.remind.wsedlacek.forgetmenot.feature.backend.TimeManager;
import com.remind.wsedlacek.forgetmenot.feature.util.Debug;
import com.remind.wsedlacek.forgetmenot.feature.util.TimeCorrection;
import com.remind.wsedlacek.forgetmenot.feature.util.Vibrate;

public class Splash extends AppCompatActivity {
    private final Context mContext = this;
    private final String TAG = "SPLASH";

    private View mBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mBackground = (View) findViewById(R.id.background);

        Debug.Log(TAG, "Starting backend...");
        DataManager.init(mContext);
        TimeCorrection.init(mContext);
        Vibrate.init(mContext);
    }
}
