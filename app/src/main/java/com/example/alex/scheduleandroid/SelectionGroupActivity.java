package com.example.alex.scheduleandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.alex.scheduleandroid.database.DatabaseManager;

public class SelectionGroupActivity extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_group);

        listView = (ListView) findViewById(R.id.listViewGroups);

        DatabaseManager databaseManager = new DatabaseManager(this);
        String[] groups = databaseManager.getAllGroups();
        databaseManager.closeDatabase();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                groups);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedGroup = ((TextView) view).getText().toString();
//                Log.d(Constants.MY_TAG, ((TextView)view).getText().toString());
                Intent intent = new Intent();
                intent.putExtra("group", selectedGroup);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }
}
