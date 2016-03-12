package com.example.alex.scheduleandroid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
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
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {

    private static final int LAYOUT = R.layout.activity_main;

    private DrawerLayout drawerLayout;

    private ConnectedManager connectedManager;
    private Toolbar toolbar;

    private ProgressDialog pDialog;

    private RecyclerView recyclerView;

    private MyAsyncTask myAsyncTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        initNavigationView();
        initToolBar();

        connectedManager = new ConnectedManager(this);

        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myAsyncTask =  new MyAsyncTask();
        myAsyncTask.execute();
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

    public class MyAsyncTask extends AsyncTask<Void , Void , List<GroupDTO>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            String strMsg = MainActivity.this.getString(R.string.downloadingGroups);

            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage(strMsg);
            pDialog.show();
        }

        @Override
        protected List<GroupDTO> doInBackground(Void... params) {
            List<GroupDTO> data = new ArrayList<>();
            data.add(connectedManager.getGroupDTOByFaculty(ConnectedManager.FACULTY_DKE));
            data.add(connectedManager.getGroupDTOByFaculty(ConnectedManager.FACULTY_DEE));
            data.add(connectedManager.getGroupDTOByFaculty(ConnectedManager.FACULTY_DPM));

            return data;
        }

        @Override
        protected void onPostExecute( List<GroupDTO> list) {
            super.onPostExecute(list);
            pDialog.dismiss();
            recyclerView.setAdapter(new GroupListAdapter(list, MainActivity.this));

        }

    }


}

