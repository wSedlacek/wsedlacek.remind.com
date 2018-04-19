package com.remind.wsedlacek.forgetmenot.feature.backend;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

import com.remind.wsedlacek.forgetmenot.feature.activities.Buzz;
import com.remind.wsedlacek.forgetmenot.feature.activities.Setup;
import com.remind.wsedlacek.forgetmenot.feature.services.firebase.FirebaseIDManager;
import com.remind.wsedlacek.forgetmenot.feature.util.telemetry.Debug;
import com.remind.wsedlacek.forgetmenot.feature.util.data.firebase.FirebaseContainer;
import com.remind.wsedlacek.forgetmenot.feature.util.data.firebase.FirebaseContainerVariable;
import com.remind.wsedlacek.forgetmenot.feature.util.data.MonitoredVariable;

import java.util.Map;

public class DataManager {
    private static String TAG = "DataManager";
    private static String sID;

    private static FirebaseContainer sFirebaseContainer;

    public static FirebaseContainerVariable<String> sToken;
    public static FirebaseContainerVariable<String> sNameData;
    public static FirebaseContainerVariable<String> sTimeData;
    public static FirebaseContainerVariable<String> sFreqData;
    public static FirebaseContainerVariable<String> sDateData;
    public static FirebaseContainerVariable<String> sConnectedData;

    public static void init(final Context tContext) {
        Debug.Log(TAG, "Initializing Variables...");
        sID = Settings.Secure.getString(tContext.getContentResolver(), Settings.Secure.ANDROID_ID);

        sFirebaseContainer = new FirebaseContainer(sID, new FirebaseContainer.ChangeListener() {
            @Override
            public void onChange(String tKey) {
                Map<String, Object> tContainer = sFirebaseContainer.get();
                switch (tKey) {
                    case "token": sToken.set(tContainer.get(tKey)); break;
                    case "name": sNameData.set(tContainer.get(tKey)); break;
                    case "time": sTimeData.set(tContainer.get(tKey)); break;
                    case "freq": sFreqData.set(tContainer.get(tKey)); break;
                    case "date": sDateData.set(tContainer.get(tKey)); break;
                    case "connected": sConnectedData.set(tContainer.get(tKey)); break;
                }
            }
        });

        sToken = new FirebaseContainerVariable<>("token", null, sFirebaseContainer);
        sNameData = new FirebaseContainerVariable<>("name", null, sFirebaseContainer);
        sTimeData = new FirebaseContainerVariable<>("time", null, sFirebaseContainer);
        sFreqData = new FirebaseContainerVariable<>("freq", null, sFirebaseContainer);
        sDateData = new FirebaseContainerVariable<>("date", null, sFirebaseContainer);
        sConnectedData = new FirebaseContainerVariable<>("connected", null, sFirebaseContainer, new MonitoredVariable.ChangeListener() {
            @Override
            public void onChange() {
                Intent tIntent;
                switch ((String) sConnectedData.get()) {
                    case "1":
                        tIntent = new Intent(tContext, Buzz.class);
                        break;
                    default:
                        tIntent = new Intent(tContext, Setup.class);
                }

                tContext.startActivity(tIntent);
            }
        });


        FirebaseIDManager.setListener(new Runnable() {
            @Override
            public void run() {
                FirebaseContainer.add("IDs", FirebaseIDManager.getID());
                sToken.set(FirebaseIDManager.getID());
            }
        });
    }
}
