package com.example.alex.scheduleandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import com.example.alex.scheduleandroid.service.GcmIntentService;

public class SettingActivity extends BaseActivity {

    Button btnSelectYourGroup;
    TextView textView;

    SharedPreferences sPref;

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setCurrentActivity(R.id.settings);

        String title = getString(R.string.settings);
        initToolBar(title, R.id.toolbarSetting);

        btnSelectYourGroup = (Button) findViewById(R.id.selectYourGroup);


        initNavigationView();
        initUserGroupNameFromPreference();

    }

    // проверка на доступность Google Play Services
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                String notGPCInstall = getString(R.string.googlePlayServicesNotInstall);
                Log.d(Constants.MY_TAG, notGPCInstall);
                Toast.makeText(getApplicationContext(), notGPCInstall, Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        }
        return true;
    }

    // starting the service to register with GCM
    private void registerGCM() {
        Intent intent = new Intent(this, GcmIntentService.class);
        intent.putExtra("group", userGrp);
        startService(intent);
    }

    private void initUserGroupNameFromPreference() {
        String yourGroup= getString(R.string.yourGroup);
        sPref = getSharedPreferences(Constants.GROUP_USER , MODE_PRIVATE);
        String userGrp = sPref.getString(Constants.GROUP_USER, "");
        textView = (TextView) findViewById(R.id.textViewYourGroup);
        textView.setText(yourGroup + userGrp);
    }


    public void onClickSelectYourGroup (View view) {

        Intent intent = new Intent(this,SelectionGroupActivity.class);
        startActivityForResult(intent, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == RESULT_OK) {
            String str = data.getStringExtra("group");
            String grpSaved = this.getResources().getString(R.string.groupSaved);

            TextView tv = getTvUserGroup();
            tv.setText(str);

            SharedPreferences.Editor editor = sPref.edit();
            editor.putString(Constants.GROUP_USER , str);
            editor.apply();
            Toast.makeText(this , grpSaved , Toast.LENGTH_SHORT).show();
            initUserGroupNameFromPreference();
            userGrp = str;

            if (checkPlayServices()) {
                registerGCM();
            }
        }
    }
}
