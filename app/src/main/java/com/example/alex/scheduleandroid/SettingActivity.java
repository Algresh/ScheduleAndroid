package com.example.alex.scheduleandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    final static String GROUP_USER = "group_user";
    private DrawerLayout drawerLayout;

    private String userGrp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        sPref = getSharedPreferences(GROUP_USER , MODE_PRIVATE);
        String userGrp = sPref.getString(GROUP_USER , "");


        btnSelectYourGroup = (Button) findViewById(R.id.selectYourGroup);
        edtYourGroup = (EditText) findViewById(R.id.editTextYourGroup);
        edtYourGroup.setText(userGrp);

        initNavigationView();

    }

    private void initNavigationView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_setting);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        View headerLayout = navigationView.getHeaderView(0);

        TextView tvUserGroup = (TextView) headerLayout.findViewById(R.id.groupUserNavigationHeader);
        sPref = getSharedPreferences(GROUP_USER, MODE_PRIVATE);
        userGrp = sPref.getString(GROUP_USER , "");
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
        editor.putString(GROUP_USER , str);
        editor.apply();
        Toast.makeText(this , grpSaved , Toast.LENGTH_SHORT).show();
    }

}
