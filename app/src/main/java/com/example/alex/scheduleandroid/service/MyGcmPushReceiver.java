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
import com.example.alex.scheduleandroid.NotificationActivity;
import com.example.alex.scheduleandroid.R;
import com.google.android.gms.gcm.GcmListenerService;



public class MyGcmPushReceiver extends GcmListenerService {

    private static int NOTIFY_ID = 1;

    @Override
    public void onMessageReceived(String from, Bundle bundle) {
        // получение сообщения
        String message = bundle.getString("message");
        Log.d(Constants.MY_TAG, "From: " + from);
        Log.d(Constants.MY_TAG, "Message: " + message);
        sendNotification(message);
    }

    // отправка уведомления о новом сообщении
    private void sendNotification(String message) {
        Context context = getApplicationContext();

        Intent notificationIntent = new Intent(context, NotificationActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        Resources res = context.getResources();
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.message_text_white)
                .setTicker(message)
                .setAutoCancel(true)
                .setContentTitle(res.getString(R.string.notifyMessage))
                .setContentText(message);
        Notification notification = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            notification = builder.build();
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFY_ID, notification);

        if(NOTIFY_ID > 10000) {
            NOTIFY_ID = 1;
        } else {
            NOTIFY_ID++;
        }


    }



}
