package com.example.alex.scheduleandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class BaseActivity extends AppCompatActivity {

    protected String userGrp;
    protected DrawerLayout drawerLayout;
    protected Toolbar toolbar;
    protected int currentActivity = -1;

    public int getCurrentActivity() {
        return currentActivity;
    }

    public void setCurrentActivity(int currentActivity) {
        this.currentActivity = currentActivity;
    }

    protected void initToolBar(String title, int idToolvar) {
        toolbar = (Toolbar) findViewById(idToolvar);
        toolbar.setTitle(title);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });
    }


    protected void initNavigationView() {
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

        navigationView.setNavigationItemSelectedListener(selectMenuNavigationView());
    }

    private  NavigationView.OnNavigationItemSelectedListener selectMenuNavigationView(){
        return new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                drawerLayout.closeDrawers();
                switch (item.getItemId()) {
                    case R.id.settings:
                        cleverIntent(SettingActivity.class, R.id.settings);
                        break;
                    case R.id.myLessonsItem:
                        if(!userGrp.equals("") && userGrp != null){
                            cleverIntent(LessonsShowActivity.class, R.id.myLessonsItem);
                        }
                        break;
                    case R.id.notificationItem:
                        cleverIntent(NotificationActivity.class, R.id.notificationItem);
                        break;
                    case R.id.aboutApp:
                        cleverIntent(AboutAppActivity.class, R.id.aboutApp);
                        break;
                    case R.id.listOfGroups:
                        cleverIntent(MainActivity.class, R.id.listOfGroups);
                        break;

                }
                return true;
            }
        };
    }

    private void cleverIntent(Class<?> cls, int selectedCase) {
        if(selectedCase != currentActivity) {
            Intent intent = new Intent(this, cls);
            if (selectedCase == R.id.myLessonsItem) {
                intent.putExtra("group", userGrp);
            }
            startActivity(intent);
        }
    }

}
