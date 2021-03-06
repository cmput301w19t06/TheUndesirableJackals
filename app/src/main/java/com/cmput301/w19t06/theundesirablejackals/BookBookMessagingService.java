package com.cmput301.w19t06.theundesirablejackals;

import android.app.Application;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.cmput301.w19t06.theundesirablejackals.activities.ChatActivity;
import com.cmput301.w19t06.theundesirablejackals.activities.MainHomeViewActivity;
import com.cmput301.w19t06.theundesirablejackals.classes.CurrentActivityReceiver;
import com.cmput301.w19t06.theundesirablejackals.database.DatabaseHelper;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class BookBookMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MessagingService";


    public BookBookMessagingService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...
        Intent localMessage = new Intent(CurrentActivityReceiver.CURRENT_ACTIVITY_ACTION);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(localMessage);
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

//            if (/* Check if data needs to be processed by long running job */ true) {
//                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
//                scheduleJob();
//            } else {
//                // Handle message within 10 seconds
//                handleNow();
//            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        DatabaseHelper databaseHelper = new DatabaseHelper();
        databaseHelper.sendRegistrationToServer(token);
    }


//    @Override
//    public void onMessageReceived()
}
