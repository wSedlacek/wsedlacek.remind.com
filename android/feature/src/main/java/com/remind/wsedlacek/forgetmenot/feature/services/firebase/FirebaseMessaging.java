package com.remind.wsedlacek.forgetmenot.feature.services.firebase;

import android.app.Notification;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.remind.wsedlacek.forgetmenot.feature.util.telemetry.Debug;

import java.util.ArrayList;
import java.util.Map;

public class FirebaseMessaging extends FirebaseMessagingService {
    private static String TAG = "FirebaseMessaging";

    private static Runnable sAction;
    private static ArrayList<String> sMessages = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        startForeground(1,new Notification());
    }

    public static void setChangeListener(Runnable tAction) {
        Debug.Log(TAG, "Setting Listener...");
        sAction = tAction;
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Debug.Log(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Debug.Log(TAG, "Message data payload: " + remoteMessage.getData());
            for(Map.Entry m:remoteMessage.getData().entrySet()){
                sMessages.add((String) m.getValue());
            }

            if (sAction != null) sAction.run();
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Debug.Log(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    public static String getLastMsg() {
        return sMessages.get(sMessages.size() -1);
    }
}
