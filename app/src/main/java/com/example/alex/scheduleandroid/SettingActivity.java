package com.example.alex.scheduleandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SettingActivity extends AppCompatActivity {

    Button btnSelectYourGroup;
    EditText edtYourGroup;

    SharedPreferences sPref;

    private DrawerLayout drawerLayout;

    private String userGrp;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initToolBar();

        sPref = getSharedPreferences(Constants.GROUP_USER , MODE_PRIVATE);
        String userGrp = sPref.getString(Constants.GROUP_USER , "");


        btnSelectYourGroup = (Button) findViewById(R.id.selectYourGroup);
        edtYourGroup = (EditText) findViewById(R.id.editTextYourGroup);
        edtYourGroup.setText(userGrp);

        initNavigationView();

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
        userGrp = sPref.getString(Constants.GROUP_USER , "");
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
                }

                return true;
            }
        });
    }

    public void onClickSelectYourGroup (View view) {

        String str = edtYourGroup.getText().toString();
        String grpSaved = this.getResources().getString(R.string.groupSaved);

        SharedPreferences.Editor editor = sPref.edit();
        editor.putString(Constants.GROUP_USER , str);
        editor.apply();
        Toast.makeText(this , grpSaved , Toast.LENGTH_SHORT).show();
    }

}
