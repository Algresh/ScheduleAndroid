package com.example.alex.scheduleandroid;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;


import com.example.alex.scheduleandroid.adapter.GroupListAdapter;
import com.example.alex.scheduleandroid.dto.GroupDTO;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final int LAYOUT = R.layout.activity_main;

    private DrawerLayout drawerLayout;

    private ConnectedManager connectedManager;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        initNavigationView();
        initToolBar();

        connectedManager = new ConnectedManager(this);




        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new GroupListAdapter(createMockDepartmentListData() , this));

    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });

//        toolbar.inflateMenu(R.menu.menu);

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

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this , drawerLayout , toolbar , R.string.open , R.string.close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                drawerLayout.closeDrawers();
                switch (item.getItemId()) {
                    case R.id.listOfGroups:
                        /**
                         * @TODO Проверку в какой активити ты находишься
                         */

                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });
    }


}

