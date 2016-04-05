package com.example.alex.scheduleandroid;
import android.os.Bundle;

public class AboutAppActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);
        setCurrentActivity(R.id.aboutApp);
        String title = getString(R.string.about_app);

        initToolBar(title, R.id.toolbarAboutApp);
        initNavigationView();
    }

}
