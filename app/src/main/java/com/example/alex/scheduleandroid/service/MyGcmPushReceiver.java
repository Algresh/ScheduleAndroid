package com.example.alex.scheduleandroid.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;

import com.example.alex.scheduleandroid.Constants;
import com.example.alex.scheduleandroid.MainActivity;
import com.example.alex.scheduleandroid.R;
import com.google.android.gms.gcm.GcmListenerService;



public class MyGcmPushReceiver extends GcmListenerService {

    private static final String TAG = MyGcmPushReceiver.class.getSimpleName();

    @Override
    public void onMessageReceived(String from, Bundle bundle) {
        String title = bundle.getString("message");
        Log.d(Constants.MY_TAG, "From: " + from);
        Log.d(Constants.MY_TAG, "Title: " + title);
        sendNotification(title);
    }

    private void sendNotification(String message) {
        Context context = getApplicationContext();

        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        Resources res = context.getResources();
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.message_text)
                .setTicker(message)
                .setAutoCancel(true)
                .setContentTitle("Notification!")
                .setContentText(message);
        Notification notification = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            notification = builder.build();
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(101, notification);
    }



}
