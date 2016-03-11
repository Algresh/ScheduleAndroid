package com.example.alex.scheduleandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.alex.scheduleandroid.adapter.WorkDayListAdapter;
import com.example.alex.scheduleandroid.dto.WorkDayDTO;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class LessonsShowActivity extends AppCompatActivity {

    private static final int LAYOUT = R.layout.activity_lessons_show;
    private static final int DAYS_FOR_SHOWING = 7;

    private ConnectedManager connectedManager;

    private String group;
    private String[] dayOfWeek;
    private String[] month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        Intent intent  = getIntent();
        group = intent.getStringExtra("group");

        connectedManager = new ConnectedManager(this);

        this.dayOfWeek = this.getResources().getStringArray(R.array.name_day_of_week);
        this.month = this.getResources().getStringArray(R.array.name_month);

        RecyclerView recyclerViewLessons = (RecyclerView) findViewById(R.id.recycleViewLessons);
        recyclerViewLessons.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewLessons.setAdapter(new WorkDayListAdapter(mockLesson() , this));
    }

    private List<WorkDayDTO> mockLesson(){
        java.util.Calendar calendar = java.util.Calendar.getInstance(java.util.TimeZone.getDefault(), java.util.Locale.getDefault());
        calendar.setTime(new java.util.Date());

        List<WorkDayDTO> list = new ArrayList<>();

        String[] dates = getSevenDays(calendar);

        WorkDayDTO workDayDTO = connectedManager.getWorkDTOByGroup(group, dates);

        for (int i = 0; i < DAYS_FOR_SHOWING; i++) {
            list.add(workDayDTO);
        }

        return list;
    }

    private String[] getSevenDays(java.util.Calendar calendar){
        String[] sevenDays = new String[DAYS_FOR_SHOWING];

        String month;
        String dayOdWeek;
        String dayOdMonth;

        sevenDays[0] = this.getResources().getString(R.string.today);
        sevenDays[1] = this.getResources().getString(R.string.tomorrow);

        calendar.add(Calendar.DAY_OF_MONTH, 2);

        for (int i = 2; i < DAYS_FOR_SHOWING; i++) {


            month = this.month[calendar.get(Calendar.MONTH)];
            dayOdWeek = this.dayOfWeek[calendar.get(Calendar.DAY_OF_WEEK) - 1];
            dayOdMonth = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));

            sevenDays[i] = dayOdMonth + " " + month + ", " + dayOdWeek;

            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        return  sevenDays;
    }


}
