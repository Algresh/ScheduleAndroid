package com.example.alex.scheduleandroid.database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.alex.scheduleandroid.Constants;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, Constants.DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Constants.DATABASE_CREATE_TABLE_GROUP);

        db.execSQL(Constants.DATABASE_CREATE_TABLE_LESSON);

        db.execSQL(Constants.DATABASE_CREATE_TABLE_DATELESSON);

        db.execSQL(Constants.DATABASE_CREATE_TABLE_NOTIFICATION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Запишем в журнал
        Log.w(Constants.MY_TAG, "Обновляемся с версии " + oldVersion + " на версию " + newVersion);

        // Удаляем старую таблицу и создаём новую
        db.execSQL("DROP TABLE IF IT EXISTS " + Constants.DATABASE_TABLE_GROUP);
        db.execSQL("DROP TABLE IF IT EXISTS " + Constants.DATABASE_TABLE_LESSON);
        db.execSQL("DROP TABLE IF IT EXISTS " + Constants.DATABASE_TABLE_DATELESSON);
        db.execSQL("DROP TABLE IF IT EXISTS " + Constants.DATABASE_TABLE_NOTIFICATION);
        // Создаём новую таблицу
        onCreate(db);
    }

}
