package com.remind.wsedlacek.forgetmenot.feature.activities;

import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.remind.wsedlacek.forgetmenot.feature.R;
import com.remind.wsedlacek.forgetmenot.feature.backend.DataManager;
import com.remind.wsedlacek.forgetmenot.feature.util.telemetry.Debug;
import com.remind.wsedlacek.forgetmenot.feature.util.TimeCorrection;
import com.remind.wsedlacek.forgetmenot.feature.util.feedback.Vibrate;

public class Splash extends AppCompatActivity {
    private final Context mContext = this;
    private final String TAG = "SPLASH";

    private View mBackground;
    private Handler mConnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mBackground = findViewById(R.id.background);

        Debug.Log(TAG, "Starting backend...");
        DataManager.init(mContext);
        TimeCorrection.init(mContext);
        Vibrate.init(mContext);

        //If previous data is not on the server then set connected to false.
        mConnect = new Handler();
        mConnect.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (DataManager.sConnectedData.get() == null)
                    DataManager.sConnectedData.set(false);
            }
        }, 3000);
    }
}
