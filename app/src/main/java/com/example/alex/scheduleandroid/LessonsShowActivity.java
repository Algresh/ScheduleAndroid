package com.example.alex.scheduleandroid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.alex.scheduleandroid.adapter.WorkDayListAdapter;
import com.example.alex.scheduleandroid.database.DatabaseManager;
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
    private LinearLayoutManager manager;
    private String userGrp;
    private Toolbar toolbar;
    private int numberLoadingWeek = 0;
    private WorkDayListAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        Intent intent  = getIntent();
        group = intent.getStringExtra("group");

        connectedManager = new ConnectedManager(this);

        this.dayOfWeek = this.getResources().getStringArray(R.array.name_day_of_week);
        this.month = this.getResources().getStringArray(R.array.name_month);
        initToolBar();
        initNavigationView();


        recyclerViewLessons = (RecyclerView) findViewById(R.id.recycleViewLessons);
        manager = new LinearLayoutManager(this);
        recyclerViewLessons.setLayoutManager(manager);
        downloadPageTask = new DownloadPageTask();
        downloadPageTask.execute();

    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbarLessons);
        toolbar.setTitle(group);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });
    }

    private void initNavigationView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_lesson);
        // синхронизируем toolbar и drawerLayout
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar
                , R.string.open, R.string.close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        // получения хидера navigationView
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        View headerLayout = navigationView.getHeaderView(0);

        //устанавливаем название группы юзера в TextView в хидере
        TextView tvUserGroup = (TextView) headerLayout.findViewById(R.id.groupUserNavigationHeader);
        SharedPreferences sPref = getSharedPreferences(Constants.GROUP_USER, MODE_PRIVATE);
        userGrp = sPref.getString(Constants.GROUP_USER, "");
        tvUserGroup.setText(userGrp);

        // обработка нажатий на меню в navigationView
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

    // мето который генерирует dateOfWorkDay для разных недель
    // если numberWeek равен нулю это значит текущая неделя
    // остальные значения numberWeek говорят сколько раз по 7 дней надо прибавить к текущей дате
    private List<String> getSevenDays(java.util.Calendar calendar, int numberWeek){
        List<String>sevenDays = new ArrayList<>();
        int firstElementIndex = 0;

        String month;
        String dayOdWeek;
        String dayOdMonth;

        if(numberWeek == 0) {// для текущех 7 дней
            sevenDays.add(this.getResources().getString(R.string.today));
            sevenDays.add(getResources().getString(R.string.tomorrow));
            calendar.add(Calendar.DAY_OF_MONTH, 2);
            firstElementIndex = 2;
        } else {
            calendar.add(Calendar.DAY_OF_MONTH, numberWeek * 7);
        }


        for (int i = firstElementIndex; i < Constants.DAYS_FOR_SHOWING; i++) {

            month = this.month[calendar.get(Calendar.MONTH)];
            dayOdWeek = this.dayOfWeek[calendar.get(Calendar.DAY_OF_WEEK) - 1];
            dayOdMonth = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));

            sevenDays.add(dayOdMonth + " " + month + ", " + dayOdWeek);

            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        return  sevenDays;
    }

    public class DownloadPageTask extends AsyncTask<Void , Void , WorkDayDTO>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            String strMsg = LessonsShowActivity.this.getString(R.string.downloadingLessons);

            pDialog = new ProgressDialog(LessonsShowActivity.this);
            pDialog.setMessage(strMsg);
            pDialog.show();
        }



        @Override
        protected WorkDayDTO doInBackground(Void... params) {
            Calendar calendar = Calendar.getInstance();
            DatabaseManager databaseManager = new DatabaseManager(LessonsShowActivity.this);


            List dates = getSevenDays(calendar, numberLoadingWeek);
            WorkDayDTO workDayDTO;

            if (connectedManager.checkConnection()) {// проверка подключения
                // узнаем верси расписания группы
                int versionGrp = connectedManager.getVersionGroup(group);

                // если версия не соответствует той которая есть на телефоне или вообще нет занятий
                // то загружаем их с сервера и заносим в БД
                if (!databaseManager.compareVersions(versionGrp , group) || databaseManager.isLessonEmpty()) {
                    workDayDTO = connectedManager.getWorkDTOByGroup(group, dates);
                    if (workDayDTO != null) {
                        databaseManager.updateLessons(workDayDTO , group , versionGrp);
                    }
                } else {
                    // если совпали версии просто достаем их из БД
                    workDayDTO = databaseManager.getWorkDayDTO(group , dates);
                }
            } else {
                // если нет подключения берем расписание из БД
                workDayDTO = databaseManager.getWorkDayDTO(group , dates);
            }

            databaseManager.closeDatabase();


            return workDayDTO;
        }

        @Override
        protected void onPostExecute(WorkDayDTO workDayDTO) {
            super.onPostExecute(workDayDTO);
            pDialog.dismiss();
            if (workDayDTO != null) {
                adapter = new WorkDayListAdapter(workDayDTO, LessonsShowActivity.this);

                recyclerViewLessons.setAdapter(adapter);

                // необходимо чтобы подгружать новые занятия при скролле
                recyclerViewLessons.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        addNewItemsByScroll(dy);
                    }
                });

            } else {
                TextView textView = (TextView) findViewById(R.id.noLessonsInGroup);
                textView.setText(R.string.noDateAboutThisGroup);
            }
        }

        // метод добавляющий новые элементы когда скрол достиг последнего элемента
        private void addNewItemsByScroll(int dy) {
            if (dy > 0) { //проверка что скроллится вниз

                Calendar calendar = Calendar.getInstance();
                int visibleItemCount = manager.getChildCount();
                int totalItemCount = manager.getItemCount();
                int pastVisiblesItems = manager.findFirstVisibleItemPosition();

                if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                    numberLoadingWeek++;
                    adapter.addNewItems(getSevenDays(calendar, numberLoadingWeek));
                }
            }
        }

    }


}
