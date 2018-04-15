package com.remind.wsedlacek.forgetmenot.feature;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Buzz extends AppCompatActivity {
    private final Context mContext = this;
    private final String TAG = "BUZZ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buzz);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        String tEvent = DataManager.getData(DataManager.Data.EVENT);
        String tTime = DataManager.getData(DataManager.Data.TIME);
        String tFreq = DataManager.getData(DataManager.Data.FREQ);
    }

    public void onBackPressed() {
        final FirebaseDatabase tDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference tConnected = tDatabase.getReference(DataManager.getDataName(DataManager.Data.CONNECTED));
        tConnected.setValue("0");
    }
}
