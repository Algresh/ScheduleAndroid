package com.example.alex.scheduleandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class LessonsShowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessons_show);

        TextView tvView = (TextView) findViewById(R.id.textViewTest);

        Intent intent  = getIntent();

        String group = intent.getStringExtra("group");

        tvView.setText(group);
    }
}
