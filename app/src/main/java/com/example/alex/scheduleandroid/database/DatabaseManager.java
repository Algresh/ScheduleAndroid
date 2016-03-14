package com.example.alex.scheduleandroid.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.alex.scheduleandroid.Constants;
import com.example.alex.scheduleandroid.dto.FacultyDTO;
import com.example.alex.scheduleandroid.dto.Group;
import com.example.alex.scheduleandroid.dto.Lesson;
import com.example.alex.scheduleandroid.dto.WorkDayDTO;

import java.util.List;


public class DatabaseManager {
    private DatabaseHelper mDatabaseHelper;
    private SQLiteDatabase sqLiteDatabase;

    public DatabaseManager(Context context) {
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

    public boolean compareVersions (int version , String group) {
        String[] argsQuery = {group , String.valueOf(version)};

        Cursor cursor = sqLiteDatabase.query(Constants.DATABASE_TABLE_GROUP , null,
                Constants.SELECTION_COMPARE_VERSIONS , argsQuery , null , null , null);

        return cursor.getCount() != 0;

    }

    public void updateLessons(WorkDayDTO workDayDTO , String group , int versionGrp) {

        sqLiteDatabase.beginTransaction();

        String[] argsQuery = {group};
        Cursor cursor;
        cursor = sqLiteDatabase.query(Constants.DATABASE_TABLE_GROUP, null, Constants.SELECTION_ID_BY_GROUP_NAME,
                argsQuery, null, null, null);

        if (cursor.moveToFirst()) {
            int idGroupIndex = cursor.getColumnIndex(Constants.GROUP_COLUMN_ID);
            int idGroup = cursor.getInt(idGroupIndex);
            cursor.close();

            deleteAllLessonsByGroup(idGroup);

            addLessonsByGroup(workDayDTO, idGroup);

            ContentValues contentValues = new ContentValues();

            contentValues.put(Constants.GROUP_COLUMN_VERSION, versionGrp);
            int updCount = sqLiteDatabase.update(Constants.DATABASE_TABLE_GROUP , contentValues,
                    Constants.SELECTION_VERSION_UPDATE_BY_ID , new String[]{String.valueOf(idGroup)} );
            Log.d(Constants.MY_TAG, "updated rows count = " + updCount);


        } else {
            cursor.close();
        }
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();

    }

    private int addLessonsByGroup (WorkDayDTO workDayDTO , int group) {
        int sumRowsAdded = 0;
        ContentValues contentValues = new ContentValues();

        for (Lesson lessonItem : workDayDTO.getLessons()) {

            contentValues.put(Constants.LESSON_COLUMN_NAME , lessonItem.getTitleOfSubject());
            contentValues.put(Constants.LESSON_COLUMN_CLASSROOM , lessonItem.getClassRoom());
            contentValues.put(Constants.LESSON_COLUMN_GRP_ID , group);
            contentValues.put(Constants.LESSON_COLUMN_NUMBER , lessonItem.getNumberOfLesson());
            contentValues.put(Constants.LESSON_COLUMN_PLACE , lessonItem.getAdress());
            contentValues.put(Constants.LESSON_COLUMN_SUB_GRP , lessonItem.getSunGroup());
            contentValues.put(Constants.LESSON_COLUMN_TEACHER , lessonItem.getTeacher());
            contentValues.put(Constants.LESSON_COLUMN_TYPE, lessonItem.getTypeLesson());

            long idLesson = sqLiteDatabase.insert(Constants.DATABASE_TABLE_LESSON , null , contentValues);
            sumRowsAdded++;

            contentValues.clear();
            for (String dateItem : lessonItem.getDateOfLesson()) {
                contentValues.put(Constants.DATELESSON_COLUMN_DATE , dateItem);
                contentValues.put(Constants.DATELESSON_COLUMN_LESSON_ID , idLesson);
                sqLiteDatabase.insert(Constants.DATABASE_TABLE_DATELESSON, null, contentValues);
                sumRowsAdded++;
            }
            contentValues.clear();

        }
        Log.d(Constants.MY_TAG , "ADD " + sumRowsAdded);
        return sumRowsAdded;
    }

    private int deleteAllLessonsByGroup (int group) {
        int sumRowsDeleted = 0;
        Cursor cursor;
        String[] argsQuery;

        argsQuery = new String[]{String.valueOf(group)};
        cursor = sqLiteDatabase.query(Constants.DATABASE_TABLE_LESSON , null , Constants.SELECTION_LESSONS_BY_GROUP_ID ,
                argsQuery, null, null, null );

        if (cursor.moveToFirst()) {
            int idLessonIndex = cursor.getColumnIndex(Constants.LESSON_COLUMN_ID);
            int idLesson;

            do {
                idLesson = cursor.getInt(idLessonIndex);
                sumRowsDeleted = sumRowsDeleted + sqLiteDatabase.delete(Constants.DATABASE_TABLE_DATELESSON ,
                        Constants.SELECTION_DATELESSON_DELETE , new String[] {String.valueOf(idLesson)});

            } while (cursor.moveToNext());
        } else {
            cursor.close();
        }

        sumRowsDeleted = sumRowsDeleted + sqLiteDatabase.delete(Constants.DATABASE_TABLE_LESSON ,
                Constants.SELECTION_LESSONS_BY_GROUP_ID , argsQuery);

        Log.d(Constants.MY_TAG , "DELETE " + sumRowsDeleted);
        return sumRowsDeleted;
    }

    private boolean checkDatabaseOnGroup (Group grp , String faculty) {

        String[] argsQuery = {grp.getTitleGrp() , String.valueOf(faculty) , String.valueOf(grp.getCourse())};

        Cursor cursor = sqLiteDatabase.query(Constants.DATABASE_TABLE_GROUP , null,
                Constants.SELECTION_CHECK_GROUP , argsQuery , null, null, null);

        return cursor.getCount() != 0;

    }
}
