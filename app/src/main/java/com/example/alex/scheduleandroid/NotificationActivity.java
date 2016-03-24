package com.example.alex.scheduleandroid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alex.scheduleandroid.adapter.TabsPagerAdapter;
import com.example.alex.scheduleandroid.database.DatabaseManager;
import com.example.alex.scheduleandroid.fragment.SendDialogFragment;

import java.net.HttpURLConnection;

public class NotificationActivity extends AppCompatActivity implements SendDialogFragment.MyDialogListener {

    private ViewPager viewPager;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private String userGrp;

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        initTabs();
        initToolBar();
        initNavigationView();


    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbarNotification);
        toolbar.setTitle(R.string.notificationTitle);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });

    }

    private void initNavigationView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar, R.string.open,  R.string.close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        View headerLayout = navigationView.getHeaderView(0);

        TextView tvUserGroup = (TextView) headerLayout.findViewById(R.id.groupUserNavigationHeader);
        SharedPreferences sPref = getSharedPreferences(Constants.GROUP_USER, MODE_PRIVATE);
        userGrp = sPref.getString(Constants.GROUP_USER, "");
        tvUserGroup.setText(userGrp);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                drawerLayout.closeDrawers();
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.listOfGroups:
                        intent = new Intent(NotificationActivity.this, MainActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.settings:
                        intent = new Intent(NotificationActivity.this, SettingActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.myLessonsItem:
                        intent = new Intent(NotificationActivity.this, LessonsShowActivity.class);
                        intent.putExtra("group", userGrp);
                        startActivity(intent);
                        break;
                }

                return true;
            }
        });

    }

    private void initTabs() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        String[] tabsTitle =  getResources().getStringArray(R.array.tabs_title);
        TabsPagerAdapter adapter = new TabsPagerAdapter(getSupportFragmentManager(), tabsTitle);
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);

    }

    public void onClickShowDialog(View view) {
        FragmentManager manager = getSupportFragmentManager();
        SendDialogFragment sendDialogFragment = new SendDialogFragment();
        sendDialogFragment.show(manager, Constants.DIALOG_SENT_MESSAGE);
    }

    /**
     *
     * @TODO сделать так чтобы при добавлении уведомления оно сразу показывалось в списке!!!
     */

    @Override
    public void onClickSendMessage(String message) {

        new NotificationTask().execute(message);

    }

    public class NotificationTask extends AsyncTask<String, Void, Integer>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            String strMsg = NotificationActivity.this.getString(R.string.sendingMessage);

            pDialog = new ProgressDialog(NotificationActivity.this);
            pDialog.setMessage(strMsg);
            pDialog.show();
        }


        @Override
        protected Integer doInBackground(String... params) {
            String message = params[0];

            ConnectedManager connectedManager = new ConnectedManager(NotificationActivity.this);
            int responseCode = connectedManager.postNotification(message, userGrp);

            boolean flag;
            if (responseCode == HttpURLConnection.HTTP_OK) flag = true;
            else flag = false;

            DatabaseManager manager = new DatabaseManager(NotificationActivity.this);
            manager.addNewMyMessage(message, userGrp, flag);
            manager.closeDatabase();

            return responseCode;
        }

        @Override
        protected void onPostExecute(Integer requestCode) {
            super.onPostExecute(requestCode);
            pDialog.dismiss();
            if (requestCode == HttpURLConnection.HTTP_OK) {
                Toast.makeText(NotificationActivity.this, getString(R.string.notificationSuccess),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(NotificationActivity.this, getString(R.string.notificationError),
                        Toast.LENGTH_SHORT).show();
            }

        }

    }
}
