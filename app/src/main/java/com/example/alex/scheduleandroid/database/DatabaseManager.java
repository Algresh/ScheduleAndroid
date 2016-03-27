package com.example.alex.scheduleandroid.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.alex.scheduleandroid.Constants;
import com.example.alex.scheduleandroid.R;
import com.example.alex.scheduleandroid.dto.FacultyDTO;
import com.example.alex.scheduleandroid.dto.Group;
import com.example.alex.scheduleandroid.dto.Lesson;
import com.example.alex.scheduleandroid.dto.MessageDTO;
import com.example.alex.scheduleandroid.dto.WorkDayDTO;

import java.util.ArrayList;
import java.util.List;


public class DatabaseManager {
    private DatabaseHelper mDatabaseHelper;
    private SQLiteDatabase sqLiteDatabase;

    private String[] facultiesStr;

    public DatabaseManager(Context context) {
        mDatabaseHelper = new DatabaseHelper(context);
        sqLiteDatabase = mDatabaseHelper.getReadableDatabase();

        facultiesStr = context.getResources().getStringArray(R.array.name_array_faculties);
    }

    // закрывает соединение с базой
    public void closeDatabase() {
        sqLiteDatabase.close();
    }

    //этот метод проверяет все группы
    // если есть те которых нет в базе записывает их
    public void updateGroups(List<FacultyDTO> list) {


        ContentValues cv = new ContentValues();

        for(FacultyDTO item : list) {
            int facultyId = getFacultyIdByString(item.getTitle());

            if (facultyId > 0) {
                for (Group itemGrp: item.getGroups()) {
                    if(!checkDatabaseOnGroup(itemGrp , String.valueOf(facultyId))) {
                        cv.clear();
                        cv.put(Constants.GROUP_COLUMN_NAME , itemGrp.getTitleGrp());
                        cv.put(Constants.GROUP_COLUMN_VERSION , itemGrp.getVersionGrp());
                        cv.put(Constants.GROUP_COLUMN_NUMBER_MESSAGE , itemGrp.getNumberMessages());
                        cv.put(Constants.GROUP_COLUMN_FACULTY , facultyId);
                        cv.put(Constants.GROUP_COLUMN_COURSE, itemGrp.getCourse());
                        long rowID = sqLiteDatabase.insert(Constants.DATABASE_TABLE_GROUP , null , cv);
                        Log.d(Constants.MY_TAG, "row inserted, ID = " + rowID);
                    }
                }
            }

        }
    }

    //проверка на наличие занятий в базе
    public boolean isLessonEmpty () {

        Cursor cursor = sqLiteDatabase.query(Constants.DATABASE_TABLE_LESSON , null, null, null
                , null, null, null);

        return cursor.getCount() == 0;
    }


    //этот метод вытаскивает данные из базы и кладет их в WorkDTO
    public WorkDayDTO getWorkDayDTO (String group , List dateOfWorkDay) {


        WorkDayDTO workDayDTO = null;

        Cursor cursor;
        int idGroup = getGroupIdByName(group);

        if (idGroup > 0) {
            workDayDTO = new WorkDayDTO(dateOfWorkDay);
            String[] argsQuery;

            argsQuery = new String[]{String.valueOf(idGroup)};
            cursor = sqLiteDatabase.query(Constants.DATABASE_TABLE_LESSON , null , Constants.SELECTION_LESSONS_BY_GROUP_ID ,
                    argsQuery, null, null, null );

            if (cursor.moveToFirst()) {

                do {

                    String[] date = null;

                    String teacher = cursor.getString(cursor.getColumnIndex(Constants.LESSON_COLUMN_TEACHER));
                    int subGrp = cursor.getInt(cursor.getColumnIndex(Constants.LESSON_COLUMN_SUB_GRP));
                    String classRoom = cursor.getString(cursor.getColumnIndex(Constants.LESSON_COLUMN_CLASSROOM));
                    String place = cursor.getString(cursor.getColumnIndex(Constants.LESSON_COLUMN_PLACE));
                    int numberLesson = cursor.getInt(cursor.getColumnIndex(Constants.LESSON_COLUMN_NUMBER));
                    String typeLesson = cursor.getString(cursor.getColumnIndex(Constants.LESSON_COLUMN_TYPE));
                    String nameSubject = cursor.getString(cursor.getColumnIndex(Constants.LESSON_COLUMN_NAME));

                    int idLesson =  cursor.getInt(cursor.getColumnIndex(Constants.LESSON_COLUMN_ID));

                    argsQuery = new String[]{String.valueOf(idLesson)};

                    Cursor cursorDate = sqLiteDatabase.query(Constants.DATABASE_TABLE_DATELESSON , null, Constants.SELECTION_DATELESSON_BY_LESSON_ID,
                            argsQuery, null, null, null);

                    if(cursorDate.moveToFirst()) {
                        int i = 0;
                        date = new String[cursorDate.getCount()];
                        do {
                            date[i] = cursorDate.getString(cursorDate.getColumnIndex(Constants.DATELESSON_COLUMN_DATE));
                            i++;
                        } while (cursorDate.moveToNext());
                    }
                    workDayDTO.setLesson(new Lesson(nameSubject , numberLesson , classRoom , typeLesson , teacher ,subGrp ,place ,date));

                } while (cursor.moveToNext());
            }

        }


        return workDayDTO;
    }

    //этот метод вытаскивает данные из базы и кладет их в FacultyDTO
    public FacultyDTO getFacultyDTO(String facultyId , String faculty) {
        FacultyDTO facultyDTO = new FacultyDTO(faculty);

        Cursor cursor = sqLiteDatabase.query(Constants.DATABASE_TABLE_GROUP, null,
                Constants.SELECTION_GROUPS_BY_FACULTY, new String[]{facultyId}, null, null, null);

        if (cursor.moveToFirst()) {

            do {
                int idGrp = cursor.getInt(cursor.getColumnIndex(Constants.GROUP_COLUMN_ID));
                int versionGrp = cursor.getInt(cursor.getColumnIndex(Constants.GROUP_COLUMN_VERSION));
                int numMessages = cursor.getInt(cursor.getColumnIndex(Constants.GROUP_COLUMN_NUMBER_MESSAGE));
                int course = cursor.getInt(cursor.getColumnIndex(Constants.GROUP_COLUMN_COURSE));
                String namGrp = cursor.getString(cursor.getColumnIndex(Constants.GROUP_COLUMN_NAME));

                facultyDTO.setGroup(new Group(namGrp , versionGrp , idGrp , numMessages , course));

            } while (cursor.moveToNext());

        }

        return facultyDTO;
    }

    //сравнивает версию группы которая в базе и которая пришла от сервера
    public boolean compareVersions (int version , String group) {
        String[] argsQuery = {group , String.valueOf(version)};

        Cursor cursor = sqLiteDatabase.query(Constants.DATABASE_TABLE_GROUP , null,
                Constants.SELECTION_COMPARE_VERSIONS , argsQuery , null , null , null);

        return cursor.getCount() != 0;

    }

    //обновляет занятия для задонной группы
    //сначала удаляет все занятия а потом записывает новые
    public void updateLessons(WorkDayDTO workDayDTO , String group , int versionGrp) {

        sqLiteDatabase.beginTransaction();

        int idGroup = getGroupIdByName(group);

        if(idGroup > 0) {
            deleteAllLessonsByGroup(idGroup);//удаление

            addLessonsByGroup(workDayDTO, idGroup);//добавление

            ContentValues contentValues = new ContentValues();

            contentValues.put(Constants.GROUP_COLUMN_VERSION, versionGrp);
            int updCount = sqLiteDatabase.update(Constants.DATABASE_TABLE_GROUP, contentValues,
                    Constants.SELECTION_VERSION_UPDATE_BY_ID, new String[]{String.valueOf(idGroup)});
            Log.d(Constants.MY_TAG, "updated rows count = " + updCount);

        }

        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();

    }

    //добавляет занятия в базу
    // возвращает количество добавлений
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
        return sumRowsAdded;
    }

    //возвращает id группы
    private int getGroupIdByName (String name ) {
        int idGroup = -1;
        String[] argsQuery = {name};
        Cursor cursor;
        cursor = sqLiteDatabase.query(Constants.DATABASE_TABLE_GROUP, null, Constants.SELECTION_ID_BY_GROUP_NAME,
                argsQuery, null, null, null);

        if (cursor.moveToFirst()) {
            int idGroupIndex = cursor.getColumnIndex(Constants.GROUP_COLUMN_ID);
            idGroup = cursor.getInt(idGroupIndex);
        }
        cursor.close();
        return idGroup;

    }

    public String[] getAllGroups() {

        Cursor cursor = sqLiteDatabase.query(Constants.DATABASE_TABLE_GROUP, null, null, null, null
                ,null, Constants.GROUP_COLUMN_FACULTY + ", " + Constants.GROUP_COLUMN_COURSE);

        String[] strGroups = new String[cursor.getCount()];
        int i = 0;
        if(cursor.moveToFirst()) {
            do {
                strGroups[i] = cursor.getString(cursor.getColumnIndex(Constants.GROUP_COLUMN_NAME));
                i++;
            } while (cursor.moveToNext());

        }

        return strGroups;
    }

    //удаляет все занятия группы из базы
    // возвращает количество удалений
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
                        Constants.SELECTION_DATELESSON_BY_LESSON_ID , new String[] {String.valueOf(idLesson)});

            } while (cursor.moveToNext());
        } else {
            cursor.close();
        }

        sumRowsDeleted = sumRowsDeleted + sqLiteDatabase.delete(Constants.DATABASE_TABLE_LESSON ,
                Constants.SELECTION_LESSONS_BY_GROUP_ID , argsQuery);

        return sumRowsDeleted;
    }

    //получить номер первой пары группы
    public int getNumberLesson (String dateStr, String group) {

        String idGroup = String.valueOf(getGroupIdByName(group));
//        Log.d(Constants.MY_TAG, dateStr);
//        Log.d(Constants.MY_TAG, idGroup);

        Cursor cursor = sqLiteDatabase.rawQuery(Constants.QUERY_NUMBER_LESSON, new String[]{idGroup, dateStr});

        if (cursor.moveToFirst()) {
            int firstLesson = cursor.getInt(cursor.getColumnIndex(Constants.DATELESSON_COLUMN_FIRST_LESSON));

            return firstLesson;
        }

        return 0;
    }


    //проверяет наличие группы в базе
    //true - если есть группа, false - усли нет
    private boolean checkDatabaseOnGroup (Group grp , String faculty) {

        String[] argsQuery = {grp.getTitleGrp() , String.valueOf(faculty) , String.valueOf(grp.getCourse())};

        Cursor cursor = sqLiteDatabase.query(Constants.DATABASE_TABLE_GROUP, null,
                Constants.SELECTION_CHECK_GROUP, argsQuery, null, null, null);

        return cursor.getCount() != 0;
    }

    // возвращает номер факультета
    public int getFacultyIdByString(String facultyStr) {
        int facultyId = 0;

        for (int i = 0;i < this.facultiesStr.length ; i++) {
            if (facultyStr.equals(this.facultiesStr[i])) {
                facultyId = i + 1;
            }
        }
        return facultyId;
    }

    public void addNewMyMessage(String textMsg, String group, boolean flagOk) {
        int sentOk = flagOk ? 1: 0;

        int idMyGroup = getGroupIdByName(group);

        ContentValues contentValues = new ContentValues();

        if(idMyGroup > 0 && !textMsg.equals("")) {
            contentValues.put(Constants.NOTIFICATION_COLUMN_TEXT_MSG, textMsg);
            contentValues.put(Constants.NOTIFICATION_COLUMN_GROUP_ID, idMyGroup);
            contentValues.put(Constants.NOTIFICATION_COLUMN_DATE, System.currentTimeMillis());
            contentValues.put(Constants.NOTIFICATION_COLUMN_SENT_OK, sentOk);

            sqLiteDatabase.insert(Constants.DATABASE_TABLE_NOTIFICATION, null, contentValues);
        }
    }

    public List<MessageDTO> getMyMessages (String group) {
        return getMessages(group, Constants.MY_MESSAGES);
    }

    public List<MessageDTO> getOtherMessages (String group) {
        return getMessages(group, Constants.OTHER_MESSAGES);
    }

    private List<MessageDTO> getMessages (String group , int type) {
        List<MessageDTO> list = new ArrayList<>();

        int idGrp = getGroupIdByName(group);

        Cursor cursor;
        String selection = "";
        if (type == Constants.MY_MESSAGES) {
            selection = Constants.SELECTION_MY_MESSAGES;
        } else if (type == Constants.OTHER_MESSAGES) {
            selection = Constants.SELECTION_OTHER_MESSAGES;
        }

        cursor = sqLiteDatabase.query(Constants.DATABASE_TABLE_NOTIFICATION, null, selection,
                new String[]{String.valueOf(idGrp)}, null, null, Constants.NOTIFICATION_COLUMN_DATE + " DESC");

        if(cursor.moveToFirst()) {

            do {
                int id = cursor.getInt(cursor.getColumnIndex(Constants.NOTIFICATION_COLUMN_ID));
                int groupId = cursor.getInt(cursor.getColumnIndex(Constants.NOTIFICATION_COLUMN_GROUP_ID));
                long date_sent = cursor.getLong(cursor.getColumnIndex(Constants.NOTIFICATION_COLUMN_DATE));
                String text_msg = cursor.getString(cursor.getColumnIndex(Constants.NOTIFICATION_COLUMN_TEXT_MSG));
                int sentOk = cursor.getInt(cursor.getColumnIndex(Constants.NOTIFICATION_COLUMN_SENT_OK));

                list.add(new MessageDTO(id, date_sent, groupId, text_msg, sentOk));
            } while (cursor.moveToNext());
        }

        return list;
    }

}
