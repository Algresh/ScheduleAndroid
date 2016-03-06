package com.example.alex.scheduleandroid;

import android.net.ConnectivityManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


import com.example.alex.scheduleandroid.adapter.GroupListAdapter;
import com.example.alex.scheduleandroid.dto.GroupDTO;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final int LAYOUT = R.layout.activity_main;

    private DrawerLayout drawerLayout;

    private ConnectedManager connectedManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        initNavigationView();

        connectedManager = new ConnectedManager(this);




        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new GroupListAdapter(createMockDepartmentListData() , this));

    }

    private List<GroupDTO> createMockDepartmentListData() {
        List<GroupDTO> data = new ArrayList<>();
        data.add(connectedManager.getGroupDTOByFaculty(ConnectedManager.FACULTY_DKE));// Эта правильная ее нужно раскоментить!!!
        data.add(connectedManager.getGroupDTOByFaculty(ConnectedManager.FACULTY_DEE));// Эта правильная ее нужно раскоментить!!!
        data.add(connectedManager.getGroupDTOByFaculty(ConnectedManager.FACULTY_DPM));// Эта правильная ее нужно раскоментить!!!
//        data.add(new GroupDTO("Департамент электронной инженерии"));
//        data.add(new GroupDTO("Департамент электронной инженерии"));
//        data.add(new GroupDTO("Департамент прикладной математики"));

        return data;
    }


    private void initNavigationView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    }


}

