package com.android.sample.notifications.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.android.sample.notifications.R;
import com.android.sample.notifications.activity.MainActivity;
import com.android.sample.notifications.app.Config;
import com.android.sample.notifications.util.NotificationUtils;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yoon on 2017. 8. 8..
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    private NotificationUtils mNotificationUtils;

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                scheduleJob();
            } else {
                // Handle message within 10 seconds
                handleNow();
            }

            try {
                JSONObject jsonObject = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(jsonObject);
            } catch (Exception e) {
                Log.e(TAG, "onMessageReceived: Exception: " + e.getMessage());
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
//            sendNotification(remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]

    /**
     * Schedule a job using FirebaseJobDispatcher.
     */
    private void scheduleJob() {
        // [START dispatch_job]
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        Job myJob = dispatcher.newJobBuilder()
                .setService(MyJobService.class)
                .setTag("my-job-tag")
                .build();
        dispatcher.schedule(myJob);
        // [END dispatch_job]
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_music_note_black_24dp)
                .setColor(getResources().getColor(R.color.colorNotificationBackground))
                .setContentTitle("FCM Message")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private void handleNotification(String messageBody) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", messageBody);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        }
    }

    private void handleDataMessage(JSONObject jsonObject) {
        Log.d(TAG, "handleDataMessage: jsonObject" + jsonObject.toString());
        mNotificationUtils = new NotificationUtils(getApplicationContext());

        try {
            JSONObject dataJSONObject = jsonObject.getJSONObject("data");
            String title = dataJSONObject.getString("title");
            String message = dataJSONObject.getString("message");
            boolean isBackground = dataJSONObject.getBoolean("is_background");
            String imageUrl = dataJSONObject.getString("image_url");
            String timeStamp = dataJSONObject.getString("time_stamp");
            JSONObject payloadJSONObject = dataJSONObject.getJSONObject("payload");

            Log.d(TAG, "handleDataMessage: title: " + title);
            Log.d(TAG, "handleDataMessage: message: " + message);
            Log.d(TAG, "handleDataMessage: isBackground: " + isBackground);
            Log.d(TAG, "handleDataMessage: imageUrl: " + imageUrl);
            Log.d(TAG, "handleDataMessage: timeStamp: " + timeStamp);
            Log.d(TAG, "handleDataMessage: payloadJSONObject: " + payloadJSONObject.toString());

            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                pushNotification.putExtra("message", message);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                mNotificationUtils.playNotificationSound();
            } else {
                Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);

                resultIntent.putExtra("message", message);
                resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                if (TextUtils.isEmpty(imageUrl)) {

                    mNotificationUtils.showNotificationMessage(title, message, timeStamp, resultIntent);
                } else {
                    mNotificationUtils.showNotificationMessage(title, message, timeStamp, resultIntent, imageUrl);
                }
            }

        } catch (JSONException e) {
            Log.e(TAG, "handleDataMessage: JSONException" + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "handleDataMessage: Exception" + e.getMessage());
        }
    }
}
