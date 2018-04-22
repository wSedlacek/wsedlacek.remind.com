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

    public static FirebaseContainerVariable<String> sOtherNameData;
    public static FirebaseContainerVariable<String> sOtherTimeData;
    public static FirebaseContainerVariable<String> sOtherFreqData;
    public static FirebaseContainerVariable<String> sOtherDateData;

    public static FirebaseContainerVariable<Boolean> sConnectedData;

    public static void init(final Context tContext) {
        Debug.Log(TAG, "Initializing Variables...");
        sID = Settings.Secure.getString(tContext.getContentResolver(), Settings.Secure.ANDROID_ID);

        sFirebaseContainer = new FirebaseContainer(sID);
        sToken = new FirebaseContainerVariable<>("token", sFirebaseContainer);

        sNameData = new FirebaseContainerVariable<>("name", sFirebaseContainer);
        sTimeData = new FirebaseContainerVariable<>("time", sFirebaseContainer);
        sFreqData = new FirebaseContainerVariable<>("freq", sFirebaseContainer);
        sDateData = new FirebaseContainerVariable<>("date", sFirebaseContainer);

        sOtherNameData = new FirebaseContainerVariable<>("other-name", sFirebaseContainer);
        sOtherTimeData = new FirebaseContainerVariable<>("other-time", sFirebaseContainer);
        sOtherFreqData = new FirebaseContainerVariable<>("other-freq", sFirebaseContainer);
        sOtherDateData = new FirebaseContainerVariable<>("other-date", sFirebaseContainer);

        sConnectedData = new FirebaseContainerVariable<>("connected", sFirebaseContainer, new MonitoredVariable.ChangeListener() {
            @Override
            public void onChange() {
                if (sConnectedData.get() != null)
                    tContext.startActivity(new Intent(tContext, (Boolean) sConnectedData.get() ? Buzz.class : Setup.class));
            }
        });

        FirebaseIDManager.setListener(new FirebaseIDManager.ChangeListener() {
            @Override
            public void onChange() {
                FirebaseContainer.add("IDs", FirebaseIDManager.getID());
                sToken.set(FirebaseIDManager.getID());
            }
        });
    }
}
