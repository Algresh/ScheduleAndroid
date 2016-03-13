package com.example.alex.scheduleandroid.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.alex.scheduleandroid.Constants;
import com.example.alex.scheduleandroid.dto.FacultyDTO;
import com.example.alex.scheduleandroid.dto.Group;

import java.util.List;


public class DatabaseManager {
    private DatabaseHelper mDatabaseHelper;
    private SQLiteDatabase sqLiteDatabase;

    private Context context;

    public DatabaseManager(Context context) {
        this.context = context;
        mDatabaseHelper = new DatabaseHelper(context);
        sqLiteDatabase = mDatabaseHelper.getReadableDatabase();
    }

    public void updateGroups(List<FacultyDTO> list) {

        ContentValues cv = new ContentValues();

        for(FacultyDTO item : list) {
            for (Group itemGrp: item.getGroups()) {
                if(!checkDatabaseOnGroup(itemGrp , item.getTitle())) {
                    cv.clear();
                    cv.put(Constants.GROUP_COLUMN_NAME , itemGrp.getTitleGrp());
                    cv.put(Constants.GROUP_COLUMN_VERSION , itemGrp.getVersionGrp());
                    cv.put(Constants.GROUP_COLUMN_NUMBER_MESSAGE , itemGrp.getNumberMessages());
                    cv.put(Constants.GROUP_COLUMN_FACULTY , item.getTitle());
                    cv.put(Constants.GROUP_COLUMN_COURSE, itemGrp.getCourse());
                    long rowID = sqLiteDatabase.insert(Constants.DATABASE_TABLE_GROUP , null , cv);
                    Log.d(Constants.MY_TAG, "row inserted, ID = " + rowID);
                }
            }
        }
    }

    private boolean checkDatabaseOnGroup (Group grp , String faculty) {

        String[] argsQuery = {grp.getTitleGrp() , String.valueOf(faculty) , String.valueOf(grp.getCourse())};

        Cursor cursor = sqLiteDatabase.query(Constants.DATABASE_TABLE_GROUP , null,
            Constants.SELECTION_CHECK_GROUP , argsQuery , null, null, null);

        return cursor.getCount() != 0;

    }
}
