package com.example.alex.scheduleandroid;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


import com.example.alex.scheduleandroid.adapter.GroupListAdapter;
import com.example.alex.scheduleandroid.database.DatabaseManager;
import com.example.alex.scheduleandroid.dto.FacultyDTO;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private static final int LAYOUT = R.layout.activity_main;

    private ConnectedManager connectedManager;

    private ProgressDialog pDialog;

    private RecyclerView recyclerView;

    private MyAsyncTask myAsyncTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        setCurrentActivity(R.id.listOfGroups);
        String title = getString(R.string.app_name);
        initToolBar(title, R.id.toolbar);
        initNavigationView();


        connectedManager = new ConnectedManager(this);

        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        myAsyncTask =  new MyAsyncTask();
        myAsyncTask.execute();

    }


    public class MyAsyncTask extends AsyncTask<Void , Void , List<FacultyDTO>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            String strMsg = MainActivity.this.getString(R.string.downloadingGroups);

            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage(strMsg);
            pDialog.show();
        }

        @Override
        protected List<FacultyDTO> doInBackground(Void... params) {
            List<FacultyDTO> data = new ArrayList<>();
            String[] facultiesId = Constants.FACULTIES;
            DatabaseManager databaseManager = new DatabaseManager(MainActivity.this);

            if (connectedManager.checkConnection()){
                for(String strItem: facultiesId) {
                    data.add(connectedManager.getGroupDTOByFaculty(strItem));
                }

                databaseManager.updateGroups(data);
            } else {
                for(String strItem: facultiesId) {
                    String faculty = connectedManager.getStringFaculty(strItem);
                    data.add(databaseManager.getFacultyDTO(strItem, faculty));
                }
            }

            databaseManager.closeDatabase();

            return data;
        }

        @Override
        protected void onPostExecute( List<FacultyDTO> list) {
            super.onPostExecute(list);
            pDialog.dismiss();
            recyclerView.setAdapter(new GroupListAdapter(list, MainActivity.this));

        }

    }



}

