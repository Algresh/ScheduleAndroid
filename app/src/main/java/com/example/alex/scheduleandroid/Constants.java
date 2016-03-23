package com.example.alex.scheduleandroid;

public class Constants {
    public final static String GROUP_USER = "group_user";

    public static final String GROUP_URL = "http://10.0.2.2/schedule/APIController/get_all_groups.php?faculty=";
    public static final String LESSON_URL = "http://10.0.2.2/schedule/APIController/get_all_lessons_by_grp.php?grp=";
    public static final String VERSION_GROUP_URL = "http://10.0.2.2/schedule/APIController/get_version_group.php?grp=";

    public static final String FACULTY_DKE = "1";
    public static final String FACULTY_DEE = "2";
    public static final String FACULTY_DPM = "3";
    public static final String[] FACULTIES = {FACULTY_DKE , FACULTY_DEE , FACULTY_DPM};

    public static final int TAB_INBOX = 0;
    public static final int TAB_SENT = 1;

    public static final String DIALOG_SENT_MESSAGE = "dialog_message";

    public static final String MY_TAG = "myTag";
    public static final String DATE_FORMAT = "yyyy-MM-dd";

    public static final int NUMBER_OF_CURSES = 6;
    public static final int DAYS_FOR_SHOWING = 7;

    public static final int MY_MESSAGES = 0;
    public static final int OTHER_MESSAGES = 1;

    public static final String CLASS_ROOM = "classRoom";
    public static final String NUMBER_LESSON = "numberLesson";
    public static final String NAME_SUBJECT = "nameSubject";
    public static final String TEACHER = "teacher";
    public static final String SUB_GROUP = "subGroup";
    public static final String ADDRESS = "address";

    public static final String DATABASE_NAME = "mydatabase.db";
    public static final int RIGHT_DATABASE_VERSION = 1;

    //------------------------TABLE GROUP----------------------
    public static final String DATABASE_TABLE_GROUP = "grp";
    public static final String GROUP_COLUMN_ID = "id";
    public static final String GROUP_COLUMN_NAME = "nam_grp";
    public static final String GROUP_COLUMN_VERSION = "version_grp";
    public static final String GROUP_COLUMN_NUMBER_MESSAGE = "num_message";
    public static final String GROUP_COLUMN_FACULTY = "faculty";
    public static final String GROUP_COLUMN_COURSE = "course";

    //------------------------TABLE LESSON----------------------
    public static final String DATABASE_TABLE_LESSON = "lesson";
    public static final String LESSON_COLUMN_ID = "id";
    public static final String LESSON_COLUMN_NAME = "nam_subj";
    public static final String LESSON_COLUMN_TYPE = "type_lesson";
    public static final String LESSON_COLUMN_NUMBER = "number_lesson";
    public static final String LESSON_COLUMN_CLASSROOM = "class_room";
    public static final String LESSON_COLUMN_TEACHER = "teacher";
    public static final String LESSON_COLUMN_SUB_GRP = "sub_grp";
    public static final String LESSON_COLUMN_PLACE = "place_lesson";
    public static final String LESSON_COLUMN_GRP_ID = "grp_id";

    //------------------------TABLE DATELESSON----------------------
    public static final String DATABASE_TABLE_DATELESSON = "date_lesson";
    public static final String DATELESSON_COLUMN_ID = "id";
    public static final String DATELESSON_COLUMN_DATE = "lesson_date";
    public static final String DATELESSON_COLUMN_LESSON_ID = "lesson_id";
    public static final String DATELESSON_COLUMN_FIRST_LESSON = "first_lesson";

    //------------------------TABLE DATELESSON----------------------
    public static final String DATABASE_TABLE_NOTIFICATION = "notification";
    public static final String NOTIFICATION_COLUMN_ID = "id";
    public static final String NOTIFICATION_COLUMN_DATE = "date_sent";
    public static final String NOTIFICATION_COLUMN_GROUP_ID = "id_grp";
    public static final String NOTIFICATION_COLUMN_TEXT_MSG = "text_msg";


    //------------------------CREATE TABLE GROUP----------------------
    public static final String DATABASE_CREATE_TABLE_GROUP = "create table " + DATABASE_TABLE_GROUP + "(" +
            GROUP_COLUMN_ID  + " integer primary key autoincrement," +
            GROUP_COLUMN_NAME  + " text," +
            GROUP_COLUMN_VERSION  + " integer," +
            GROUP_COLUMN_NUMBER_MESSAGE  + " integer," +
            GROUP_COLUMN_FACULTY  + " integer," +
            GROUP_COLUMN_COURSE  + " integer" +
            ");";


    //------------------------CREATE TABLE LESSON----------------------
    public static final String DATABASE_CREATE_TABLE_LESSON = "create table " + DATABASE_TABLE_LESSON + "(" +
            LESSON_COLUMN_ID  + " integer primary key autoincrement," +
            LESSON_COLUMN_NAME + " text," +
            LESSON_COLUMN_TYPE + " text," +
            LESSON_COLUMN_NUMBER + " integer," +
            LESSON_COLUMN_CLASSROOM + " text," +
            LESSON_COLUMN_TEACHER + " text," +
            LESSON_COLUMN_SUB_GRP + " integer," +
            LESSON_COLUMN_PLACE + " text," +
            LESSON_COLUMN_GRP_ID + " integer);";


    //------------------------CREATE TABLE DATELESSON----------------------
    public static final String DATABASE_CREATE_TABLE_DATELESSON = "create table " + DATABASE_TABLE_DATELESSON + "(" +
            DATELESSON_COLUMN_ID  + " integer primary key autoincrement," +
            DATELESSON_COLUMN_DATE  + " text," +
            DATELESSON_COLUMN_LESSON_ID  + " integer );";

    //------------------------CREATE TABLE NOTIFICATION----------------------
    public static final String DATABASE_CREATE_TABLE_NOTIFICATION = "create table " + DATABASE_TABLE_NOTIFICATION + "(" +
            NOTIFICATION_COLUMN_ID  + " integer primary key autoincrement," +
            NOTIFICATION_COLUMN_DATE  + " integer," +
            NOTIFICATION_COLUMN_TEXT_MSG  + " text," +
            NOTIFICATION_COLUMN_GROUP_ID  + " integer );";


    //------------------------SELECTIONS----------------------
    public static final String SELECTION_CHECK_GROUP = GROUP_COLUMN_NAME + "=? AND "
            + GROUP_COLUMN_FACULTY + "=? AND " + GROUP_COLUMN_COURSE + "=?";

    public static final String SELECTION_COMPARE_VERSIONS = GROUP_COLUMN_NAME + "=? AND "
            + GROUP_COLUMN_VERSION + "=?";

    public static final String SELECTION_ID_BY_GROUP_NAME = GROUP_COLUMN_NAME + "=?";

    public static final String SELECTION_LESSONS_BY_GROUP_ID = LESSON_COLUMN_GRP_ID + "=?";

    public static final String SELECTION_DATELESSON_BY_LESSON_ID = DATELESSON_COLUMN_LESSON_ID + "=?";
    public static final String SELECTION_VERSION_UPDATE_BY_ID = GROUP_COLUMN_ID + "=?";
    public static final String SELECTION_GROUPS_BY_FACULTY = GROUP_COLUMN_FACULTY + "=?";
    public static final String SELECTION_MY_MESSAGES = NOTIFICATION_COLUMN_GROUP_ID + "=?";
    public static final String SELECTION_OTHER_MESSAGES = NOTIFICATION_COLUMN_GROUP_ID + "<>?";

    //------------------------QUERIES----------------------
    public static final String QUERY_NUMBER_LESSON = "SELECT MIN(lesson.number_lesson) AS first_lesson FROM lesson " +
            "INNER JOIN date_lesson " +
            "ON lesson.id = date_lesson.lesson_id " +
            "WHERE lesson.grp_id=? AND date_lesson.lesson_date =?";


}
