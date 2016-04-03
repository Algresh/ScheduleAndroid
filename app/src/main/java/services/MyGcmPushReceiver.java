package services;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.example.alex.scheduleandroid.Constants;
import com.google.android.gms.gcm.GcmListenerService;



public class MyGcmPushReceiver extends GcmListenerService {

    private static final String TAG = MyGcmPushReceiver.class.getSimpleName();

    @Override
    public void onMessageReceived(String from, Bundle bundle) {
        String title = bundle.getString("message");
        Log.d(Constants.MY_TAG, "From: " + from);
        Log.d(Constants.MY_TAG, "Title: " + title);
    }



}
