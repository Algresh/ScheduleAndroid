package com.example.alex.scheduleandroid;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alex.scheduleandroid.app.Config;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import com.example.alex.scheduleandroid.service.GcmIntentService;

public class SettingActivity extends AppCompatActivity {

    Button btnSelectYourGroup;
    TextView textView;

    SharedPreferences sPref;

    private DrawerLayout drawerLayout;

    private String userGrp;
    private Toolbar toolbar;

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initToolBar();

        btnSelectYourGroup = (Button) findViewById(R.id.selectYourGroup);

/**
 * @TODO сделать так чтобы при изменение Preference срабатовало событие
 */

        initNavigationView();
        initUserGroupNameFromPreference();

    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.d(Constants.MY_TAG, "This device is not supported. Google Play Services not installed!");
                Toast.makeText(getApplicationContext(), "This device is not supported. Google Play Services not installed!", Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        }
        return true;
    }

    // starting the service to register with GCM
    private void registerGCM() {
        Intent intent = new Intent(this, GcmIntentService.class);
        intent.putExtra("key", "register");
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

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbarSetting);
        toolbar.setTitle(R.string.settings);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });
    }

    private void initNavigationView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_setting);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this , drawerLayout ,toolbar,
                R.string.open , R.string.close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        View headerLayout = navigationView.getHeaderView(0);

        TextView tvUserGroup = (TextView) headerLayout.findViewById(R.id.groupUserNavigationHeader);
        sPref = getSharedPreferences(Constants.GROUP_USER, MODE_PRIVATE);
        userGrp = sPref.getString(Constants.GROUP_USER, "");
        tvUserGroup.setText(userGrp);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                drawerLayout.closeDrawers();
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.listOfGroups:
                        intent = new Intent(SettingActivity.this, MainActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.myLessonsItem:
                        intent = new Intent(SettingActivity.this, LessonsShowActivity.class);
                        intent.putExtra("group", userGrp);
                        startActivity(intent);
                        break;
                    case R.id.notificationItem:
                        intent = new Intent(SettingActivity.this, NotificationActivity.class);
                        startActivity(intent);
                        break;
                }

                return true;
            }
        });
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

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }
}
