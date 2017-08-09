package com.android.sample.notifications.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.sample.notifications.R;
import com.google.firebase.iid.FirebaseInstanceId;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

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
    }
}
