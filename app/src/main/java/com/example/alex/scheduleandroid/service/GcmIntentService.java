package com.example.alex.scheduleandroid.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import com.example.alex.scheduleandroid.ConnectedManager;
import com.example.alex.scheduleandroid.Constants;
import com.example.alex.scheduleandroid.R;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;


public class GcmIntentService extends IntentService {

    private static final String TAG = GcmIntentService.class.getSimpleName();

    public GcmIntentService() {
        super(TAG);
    }

    private String userGroup;

    @Override
    protected void onHandleIntent(Intent intent) {
        userGroup = intent.getStringExtra("group");

        String token = null;

        try {
            // получение токена устройства
            InstanceID instanceID = InstanceID.getInstance(this);
            token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

            Log.d(TAG, "GCM Registration Token: " + token);

            // sending the registration id to our server
            sendRegistrationToServer(token);
        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
        }
    }

    // отправка токена на сервер
    private void sendRegistrationToServer(final String token) {
        Log.d(Constants.MY_TAG, "sendRegistrationToServer: " + token);
        ConnectedManager connectedManager = new ConnectedManager(GcmIntentService.this);
        connectedManager.postRegistration(token, userGroup);
    }
//    private void registerGCM() {
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        String token = null;
//
//        try {
//            InstanceID instanceID = InstanceID.getInstance(this);
//            token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
//                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
//
//            Log.e(TAG, "GCM Registration Token: " + token);
//
//            // sending the registration id to our server
//            sendRegistrationToServer(token);
//
//            sharedPreferences.edit().putBoolean(Config.SENT_TOKEN_TO_SERVER, true).apply();
//        } catch (Exception e) {
//            Log.e(TAG, "Failed to complete token refresh", e);
//
//            sharedPreferences.edit().putBoolean(Config.SENT_TOKEN_TO_SERVER, false).apply();
//        }
//    }



//    /**
//     * Subscribe to a topic
//     */
//    public void subscribeToTopic(String topic) {
//        GcmPubSub pubSub = GcmPubSub.getInstance(getApplicationContext());
//        InstanceID instanceID = InstanceID.getInstance(getApplicationContext());
//        String token = null;
//        try {
//            token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
//                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
//            if (token != null) {
//                pubSub.subscribe(token, "/topics/" + topic, null);
//                Log.d(Constants.MY_TAG, "Subscribed to topic: " + topic);
//            } else {
//                Log.d(Constants.MY_TAG, "error: gcm registration id is null");
//            }
//        } catch (IOException e) {
//            Log.d(Constants.MY_TAG, "Topic subscribe error. Topic: " + topic + ", error: " + e.getMessage());
//            Toast.makeText(getApplicationContext(), "Topic subscribe error. Topic: " + topic + ", error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    public void unsubscribeFromTopic(String topic) {
//        GcmPubSub pubSub = GcmPubSub.getInstance(getApplicationContext());
//        InstanceID instanceID = InstanceID.getInstance(getApplicationContext());
//        String token = null;
//        try {
//            token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
//                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
//            if (token != null) {
//                pubSub.unsubscribe(token, "");
//                Log.e(Constants.MY_TAG, "Unsubscribed from topic: " + topic);
//            } else {
//                Log.e(Constants.MY_TAG, "error: gcm registration id is null");
//            }
//        } catch (IOException e) {
//            Log.e(Constants.MY_TAG, "Topic unsubscribe error. Topic: " + topic + ", error: " + e.getMessage());
//            Toast.makeText(getApplicationContext(), "Topic subscribe error. Topic: " + topic + ", error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//    }


}
