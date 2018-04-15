package com.remind.wsedlacek.forgetmenot.feature;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Splash extends AppCompatActivity {
    private final Context mContext = this;
    private final String TAG = "SPLASH";

    private View mBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mBackground = (View) findViewById(R.id.background);

        DataManager.init(mContext);
    }
}
