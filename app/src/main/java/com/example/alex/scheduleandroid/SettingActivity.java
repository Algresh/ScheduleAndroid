package com.example.alex.scheduleandroid;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SettingActivity extends AppCompatActivity {

    Button btnSelectYourGroup;
    EditText edtYourGroup;

    SharedPreferences sPref;

    final static String GROUP_USER = "group_user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        sPref = getPreferences(MODE_PRIVATE);
        String userGrp = sPref.getString(GROUP_USER , "");


        btnSelectYourGroup = (Button) findViewById(R.id.selectYourGroup);
        edtYourGroup = (EditText) findViewById(R.id.editTextYourGroup);
        edtYourGroup.setText(userGrp);

    }

    public void onClickSelectYourGroup (View view) {

        String str = edtYourGroup.getText().toString();
        String grpSaved = this.getResources().getString(R.string.groupSaved);

        SharedPreferences.Editor editor = sPref.edit();
        editor.putString(GROUP_USER , str);
        editor.commit();
        Toast.makeText(this , grpSaved , Toast.LENGTH_SHORT).show();
    }

}
