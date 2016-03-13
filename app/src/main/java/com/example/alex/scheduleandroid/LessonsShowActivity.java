package com.example.alex.scheduleandroid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.alex.scheduleandroid.adapter.WorkDayListAdapter;
import com.example.alex.scheduleandroid.dto.WorkDayDTO;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class LessonsShowActivity extends AppCompatActivity {

    private static final int LAYOUT = R.layout.activity_lessons_show;

    private ConnectedManager connectedManager;

    private String group;
    private String[] dayOfWeek;
    private String[] month;

    private ProgressDialog pDialog;

    private RecyclerView recyclerViewLessons;

    private DownloadPageTask downloadPageTask;

    private DrawerLayout drawerLayout;
    private String userGrp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        Intent intent  = getIntent();
        group = intent.getStringExtra("group");

        connectedManager = new ConnectedManager(this);

        this.dayOfWeek = this.getResources().getStringArray(R.array.name_day_of_week);
        this.month = this.getResources().getStringArray(R.array.name_month);

        initNavigationView();


        recyclerViewLessons = (RecyclerView) findViewById(R.id.recycleViewLessons);
        recyclerViewLessons.setLayoutManager(new LinearLayoutManager(this));
        downloadPageTask = new DownloadPageTask();
        downloadPageTask.execute();
    }

    private void initNavigationView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_lesson);

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
                    case R.id.listOfGroups:
                        intent = new Intent(LessonsShowActivity.this , MainActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.settings:
                        intent = new Intent(LessonsShowActivity.this, SettingActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.myLessonsItem:
                        intent = new Intent(LessonsShowActivity.this , LessonsShowActivity.class);
                        intent.putExtra("group" , userGrp);
                        startActivity(intent);
                        break;
                }

                return true;
            }
        });
    }


    private String[] getSevenDays(java.util.Calendar calendar){
        String[] sevenDays = new String[Constants.DAYS_FOR_SHOWING];

        String month;
        String dayOdWeek;
        String dayOdMonth;

        sevenDays[0] = this.getResources().getString(R.string.today);
        sevenDays[1] = this.getResources().getString(R.string.tomorrow);

        calendar.add(Calendar.DAY_OF_MONTH, 2);

        for (int i = 2; i < Constants.DAYS_FOR_SHOWING; i++) {


            month = this.month[calendar.get(Calendar.MONTH)];
            dayOdWeek = this.dayOfWeek[calendar.get(Calendar.DAY_OF_WEEK) - 1];
            dayOdMonth = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));

            sevenDays[i] = dayOdMonth + " " + month + ", " + dayOdWeek;

            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        return  sevenDays;
    }

    public class DownloadPageTask extends AsyncTask<Void , Void , List<WorkDayDTO>>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            String strMsg = LessonsShowActivity.this.getString(R.string.downloadingGroups);

            pDialog = new ProgressDialog(LessonsShowActivity.this);
            pDialog.setMessage(strMsg);
            pDialog.show();
        }



        @Override
        protected List<WorkDayDTO> doInBackground(Void... params) {
            java.util.Calendar calendar = java.util.Calendar.getInstance(java.util.TimeZone.getDefault(), java.util.Locale.getDefault());
            calendar.setTime(new java.util.Date());

            List<WorkDayDTO> list = new ArrayList<>();

            String[] dates = getSevenDays(calendar);

            WorkDayDTO workDayDTO = connectedManager.getWorkDTOByGroup(group, dates);

            if (workDayDTO != null) {
                for (int i = 0; i < Constants.DAYS_FOR_SHOWING; i++) {
                    list.add(workDayDTO);
                }
            } else {
                return null;
            }

            return list;
        }

        @Override
        protected void onPostExecute(List<WorkDayDTO> list) {
            super.onPostExecute(list);
            pDialog.dismiss();
            if (list != null) {
                recyclerViewLessons.setAdapter(new WorkDayListAdapter(list, LessonsShowActivity.this));
            } else {
                TextView textView = (TextView) findViewById(R.id.noLessonsInGroup);
                textView.setText(R.string.noDateAboutThisGroup);
            }
        }

    }


}
