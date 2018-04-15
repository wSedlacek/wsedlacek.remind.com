package com.remind.wsedlacek.forgetmenot.feature;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.FirebaseDatabase;

public class Splash extends AppCompatActivity {

    final Context mContext = this;

    String mID;

    View mBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mID = Settings.Secure.getString(mContext.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        mBackground = (View) findViewById(R.id.background);
        //mBackground.postOnAnimationDelayed(pressRunnable, 750);

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //DatabaseReference myRef = database.getReference(mID);
        //myRef.setValue("Hello, World!");

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(mContext, Setup.class);
                intent.putExtra(Intent.EXTRA_UID, mID);
                startActivity(intent);
            }
        }, 15000);
    }

}
