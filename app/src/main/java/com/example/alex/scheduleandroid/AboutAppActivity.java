package com.example.alex.scheduleandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class AboutAppActivity extends AppCompatActivity {

    private String userGrp;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarAboutApp);
//        setSupportActionBar(toolbar);
//        toolbar.setTitle(R.string.about_app);

        initToolBar();
        initNavigationView();
    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbarAboutApp);
        toolbar.setTitle(R.string.about_app);
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
                        intent = new Intent(AboutAppActivity.this, SettingActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.myLessonsItem:
                        intent = new Intent(AboutAppActivity.this, LessonsShowActivity.class);
                        intent.putExtra("group", userGrp);
                        startActivity(intent);
                        break;
                    case R.id.notificationItem:
                        intent = new Intent(AboutAppActivity.this, NotificationActivity.class);
                        startActivity(intent);
                        break;

                }
                return true;
            }
        });
    }

}
