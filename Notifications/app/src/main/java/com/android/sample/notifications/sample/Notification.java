package com.android.sample.notifications.sample;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.android.sample.notifications.activity.MainActivity;
import com.android.sample.notifications.R;

public class Notification extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button_1).setOnClickListener(this);
        findViewById(R.id.button_2).setOnClickListener(this);
        findViewById(R.id.button_3).setOnClickListener(this);
    }


    private void sendNotification() {

        final Intent launchIntent = new Intent(this, MainActivity.class);
        final PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 1, launchIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_music_note_black_24dp)
                .setContentTitle("ContentTitle1")
                .setContentText("ContentText1")
                .setContentIntent(pendingIntent);
        NotificationManagerCompat.from(this).notify(1, builder.build());
    }


    private void sendActionNotification() {

        final Intent launchIntent = new Intent(this, MainActivity.class);
        final PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 2, launchIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_music_note_black_24dp)
                .setContentTitle("ContentTitle2")
                .setContentText("ContentText2")
//                .setContentIntent(pendingIntent)
                .addAction(new NotificationCompat.Action(
                        R.drawable.ic_music_note_black_24dp, "ActionText2", pendingIntent));
        NotificationManagerCompat.from(this).notify(2, builder.build());
    }


    private void sendHeadUpNotification() {

        final Intent launchIntent = new Intent(this, MainActivity.class);
        final PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 3, launchIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_music_note_black_24dp)
                .setContentTitle("ContentTitle3")
                .setContentText("ContentText3")
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setVibrate(new long[] {0, 1000, 250, 1000})
//                .setContentIntent(pendingIntent)
                .addAction(new NotificationCompat.Action(
                        R.drawable.ic_music_note_black_24dp, "ActionText3-1", pendingIntent))
                .addAction(new NotificationCompat.Action(
                        R.drawable.ic_music_note_black_24dp, "ActionText3-2", pendingIntent))
                .addAction(new NotificationCompat.Action(
                        R.drawable.ic_music_note_black_24dp, "ActionText3-3", pendingIntent));
        NotificationManagerCompat.from(this).notify(3, builder.build());
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_1:
                sendNotification();
                break;
            case R.id.button_2:
                sendActionNotification();
                break;
            case R.id.button_3:
                sendHeadUpNotification();
                break;
        }
    }
}
