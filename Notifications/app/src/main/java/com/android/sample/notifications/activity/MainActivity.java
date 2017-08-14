package com.android.sample.notifications.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.android.sample.notifications.R;
import com.android.sample.notifications.app.Config;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private TextView mPushMessageTextView;
    private TextView mRegistrationIdTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String token = FirebaseInstanceId.getInstance().getToken();
        Log.i(TAG, "onCreate: token:>> " + token);

        // notification and data payload type test
        String nick = getIntent().getStringExtra("Nick");
        if (nick != null) {
            Log.i(TAG, "onCreate: nick: " + nick);
        }
        String body = getIntent().getStringExtra("body");
        if (body != null) {
            Log.i(TAG, "onCreate: body: " + body);
        }
        String room = getIntent().getStringExtra("Room");
        if (room != null) {
            Log.i(TAG, "onCreate: room: " + room);
        }

        mPushMessageTextView = (TextView) findViewById(R.id.push_message_text_view);
        mRegistrationIdTextView = (TextView) findViewById(R.id.registration_id_text_view);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    String message = intent.getStringExtra("message");
                    mPushMessageTextView.setText(message);
                }
            }
        };
        displayFirebaseRegistrationId();
    }

    private void displayFirebaseRegistrationId() {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String registrationId = preferences.getString("registrationId", null);
        Log.d(TAG, "displayFirebaseRegistrationId: " + registrationId);

        if (!TextUtils.isEmpty(registrationId)) {
            mRegistrationIdTextView.setText("Firebase Registration ID : " + registrationId);
        } else {
            mRegistrationIdTextView.setText("Firebase Registration ID is not received yet!");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }
}
