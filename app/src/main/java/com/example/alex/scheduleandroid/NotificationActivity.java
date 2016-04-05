package com.example.alex.scheduleandroid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alex.scheduleandroid.adapter.TabsPagerAdapter;
import com.example.alex.scheduleandroid.database.DatabaseManager;
import com.example.alex.scheduleandroid.fragment.SendDialogFragment;

import java.net.HttpURLConnection;

public class NotificationActivity extends BaseActivity implements SendDialogFragment.MyDialogListener {

    private ViewPager viewPager;
    TabsPagerAdapter adapter;

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        setCurrentActivity(R.id.notificationItem);

        String title = getString(R.string.notificationTitle);
        initTabs();
        initToolBar(title, R.id.toolbarNotification);
        initNavigationView();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getSupportFragmentManager();
                SendDialogFragment sendDialogFragment = new SendDialogFragment();
                sendDialogFragment.show(manager, Constants.DIALOG_SENT_MESSAGE);
            }
        });


    }


    private void initTabs() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        String[] tabsTitle =  getResources().getStringArray(R.array.tabs_title);
        adapter = new TabsPagerAdapter(getSupportFragmentManager(), tabsTitle);
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);

    }


    /**
     *
     * @TODO сделать так чтобы при добавлении уведомления оно сразу показывалось в списке!!!
     */

    @Override
    public void onClickSendMessage(String message) {

        new NotificationTask().execute(message);

//        Fragment fragment = adapter.getItem(Constants.TAB_SENT);
//        ListView listView = (ListView) fragment.getView().findViewById(R.id.listViewMyMessages);
//        ArrayAdapter arrayAdapter = (ArrayAdapter)listView.getAdapter();
//        arrayAdapter.add(message);
    }

    public class NotificationTask extends AsyncTask<String, Void, Integer>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            String strMsg = NotificationActivity.this.getString(R.string.sendingMessage);

            pDialog = new ProgressDialog(NotificationActivity.this);
            pDialog.setMessage(strMsg);
            pDialog.show();
        }


        @Override
        protected Integer doInBackground(String... params) {
            String message = params[0];

            ConnectedManager connectedManager = new ConnectedManager(NotificationActivity.this);
            int responseCode = connectedManager.postNotification(message, userGrp);

            boolean flag;
            if (responseCode == HttpURLConnection.HTTP_OK) flag = true;
            else flag = false;

            DatabaseManager manager = new DatabaseManager(NotificationActivity.this);
            manager.addNewMyMessage(message, userGrp, flag);
            manager.closeDatabase();

            return responseCode;
        }

        @Override
        protected void onPostExecute(Integer requestCode) {
            super.onPostExecute(requestCode);
            pDialog.dismiss();
            if (requestCode == HttpURLConnection.HTTP_OK) {
                Toast.makeText(NotificationActivity.this, getString(R.string.notificationSuccess),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(NotificationActivity.this, getString(R.string.notificationError),
                        Toast.LENGTH_SHORT).show();
            }

        }

    }
}
