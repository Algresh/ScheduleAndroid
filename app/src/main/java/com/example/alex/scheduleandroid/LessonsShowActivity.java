package com.example.alex.scheduleandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.alex.scheduleandroid.adapter.LessonListAdapter;
import com.example.alex.scheduleandroid.dto.WorkDayDTO;

import java.util.ArrayList;
import java.util.List;

public class LessonsShowActivity extends AppCompatActivity {

    private static final int LAYOUT = R.layout.activity_lessons_show;

    private ConnectedManager connectedManager;

    private String group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        Intent intent  = getIntent();
        group = intent.getStringExtra("group");

        connectedManager = new ConnectedManager(this);

        RecyclerView recyclerViewLessons = (RecyclerView) findViewById(R.id.recycleViewLessons);
        recyclerViewLessons.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewLessons.setAdapter(new LessonListAdapter(mockLesson()));
    }

    List<WorkDayDTO> mockLesson(){
        List<WorkDayDTO> list = new ArrayList<>();
        list.add(new WorkDayDTO("ddd"));
        list.add(new WorkDayDTO("dddd"));
        list.add(new WorkDayDTO("ddda"));

//        connectedManager.getWorkDTOByGroup(group);

        return list;
    }
}
