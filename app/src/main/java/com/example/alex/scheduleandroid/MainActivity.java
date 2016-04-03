package com.example.alex.scheduleandroid;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.example.alex.scheduleandroid.adapter.GroupListAdapter;
import com.example.alex.scheduleandroid.app.Config;
import com.example.alex.scheduleandroid.database.DatabaseManager;
import com.example.alex.scheduleandroid.dto.FacultyDTO;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.ArrayList;
import java.util.List;

import services.GcmIntentService;
//import com.google.android.gms.gcm.GCMRegistrar;
//import com.google.android.gms.gcm.GCM

public class MainActivity extends AppCompatActivity {

    private static final int LAYOUT = R.layout.activity_main;

    private DrawerLayout drawerLayout;

    private ConnectedManager connectedManager;
    private Toolbar toolbar;

    private ProgressDialog pDialog;

    private RecyclerView recyclerView;

    private MyAsyncTask myAsyncTask;

    private String userGrp;

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        initToolBar();
        initNavigationView();


        connectedManager = new ConnectedManager(this);

        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        myAsyncTask =  new MyAsyncTask();
        myAsyncTask.execute();

        if (checkPlayServices()) {
            registerGCM();
        }
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

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });

//        toolbar.inflateMenu(R.menu.menu);

    }



    private void initNavigationView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this , drawerLayout , toolbar , R.string.open , R.string.close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        View headerLayout = navigationView.getHeaderView(0);

        TextView tvUserGroup = (TextView) headerLayout.findViewById(R.id.groupUserNavigationHeader);
        SharedPreferences sPref = getSharedPreferences(Constants.GROUP_USER, MODE_PRIVATE);
        userGrp = sPref.getString(Constants.GROUP_USER , "");
        tvUserGroup.setText(userGrp);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                drawerLayout.closeDrawers();
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.settings:
                        intent = new Intent(MainActivity.this, SettingActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.myLessonsItem:
                        intent = new Intent(MainActivity.this, LessonsShowActivity.class);
                        intent.putExtra("group", userGrp);
                        startActivity(intent);
                        break;
                    case R.id.notificationItem:
                        intent = new Intent(MainActivity.this, NotificationActivity.class);
                        startActivity(intent);
                        break;

                }
                return true;
            }
        });
    }

    public class MyAsyncTask extends AsyncTask<Void , Void , List<FacultyDTO>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            String strMsg = MainActivity.this.getString(R.string.downloadingGroups);

            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage(strMsg);
            pDialog.show();
        }

        @Override
        protected List<FacultyDTO> doInBackground(Void... params) {
            List<FacultyDTO> data = new ArrayList<>();
            String[] facultiesId = Constants.FACULTIES;
            DatabaseManager databaseManager = new DatabaseManager(MainActivity.this);

            if (connectedManager.checkConnection()){
                for(String strItem: facultiesId) {
                    data.add(connectedManager.getGroupDTOByFaculty(strItem));
                }

                databaseManager.updateGroups(data);
            } else {
                for(String strItem: facultiesId) {
                    String faculty = connectedManager.getStringFaculty(strItem);
                    data.add(databaseManager.getFacultyDTO(strItem, faculty));
                }
            }

            databaseManager.closeDatabase();

            return data;
        }

        @Override
        protected void onPostExecute( List<FacultyDTO> list) {
            super.onPostExecute(list);
            pDialog.dismiss();
            recyclerView.setAdapter(new GroupListAdapter(list, MainActivity.this));

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

