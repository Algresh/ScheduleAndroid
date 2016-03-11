package com.example.alex.scheduleandroid.adapter;

import android.content.Context;
import android.util.Log;
import android.widget.SimpleAdapter;

import com.example.alex.scheduleandroid.ConnectedManager;
import com.example.alex.scheduleandroid.Lesson;
import com.example.alex.scheduleandroid.R;
import com.example.alex.scheduleandroid.dto.WorkDayDTO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class LessonListAdapter {

    private static final String CLASS_ROOM = "classRoom";
    private static final String NUMBER_LESSON = "numberLesson";
    private static final String NAME_SUBJECT = "nameSubject";
    private static final String TEACHER = "teacher";
    private static final String SUB_GROUP = "subGroup";
    private static final String ADDRESS = "address";

    public static final String MY_TAG = "myTag";

    Context context;

    String[] from = {CLASS_ROOM , NUMBER_LESSON , NAME_SUBJECT , TEACHER , SUB_GROUP , ADDRESS};

    int[] to = {R.id.classRoom , R.id.numLesson , R.id.nameSubj
            , R.id.teacher , R.id.subGroup , R.id.address};

    private String classRoom;
    private String numLesson;
    private String subGrp;

    public LessonListAdapter(Context context) {
        this.context = context;

        classRoom = context.getResources().getString(R.string.classRoom);
        numLesson = context.getResources().getString(R.string.numLesson);
        subGrp = context.getResources().getString(R.string.subGroup);
    }

    public SimpleAdapter getAdapter(WorkDayDTO itemDTO , int dayOfWeek) {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, dayOfWeek);

        Calendar calendar2 = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

//        Log.d(ConnectedManager.MY_TAG, calendar.toString());
//        Log.d(ConnectedManager.MY_TAG, calendar2.toString());
        long dateLong1 =  10000 * calendar.get(Calendar.YEAR)  + calendar.get(Calendar.MONTH) * 100 + calendar.get(Calendar.DAY_OF_MONTH);
        long dateLong2 = 0;
//        Log.d(ConnectedManager.MY_TAG, dateLong1 + " | " + dateLong2);

//        calendar.compareTo()

//        Log.d(ConnectedManager.MY_TAG , calendar.toString());

        ArrayList<HashMap<String , String>> list = new ArrayList<HashMap<String , String>>();

        String title;

        HashMap<String , String> hm;
//        Log.d(ConnectedManager.MY_TAG, "1");

        for(Lesson item: itemDTO.getLessons()) {
//            Log.d(ConnectedManager.MY_TAG, "dds");

            for(String strDate : item.getDateOfLesson()){
//                Log.d(ConnectedManager.MY_TAG, "2");
                try {
                    Date date = format.parse(strDate);
                    calendar2.setTime(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

//                Log.d(ConnectedManager.MY_TAG, "3");
                dateLong2 = 10000 * calendar2.get(Calendar.YEAR)  + calendar2.get(Calendar.MONTH) * 100 + calendar2.get(Calendar.DAY_OF_MONTH);

                if(dateLong1 == dateLong2) {
//                    Log.d(ConnectedManager.MY_TAG, "4");
                    hm = new HashMap<String , String>();

                    title = item.getTypeLesson() + ": " + item.getTitleOfSubject();

                    hm.put(CLASS_ROOM , classRoom + item.getClassRoom());
                    hm.put(NUMBER_LESSON , numLesson +  item.getNumberOfLesson());
                    hm.put(NAME_SUBJECT , title );
                    hm.put(TEACHER , item.getTeacher());
                    if(item.getSunGroup() != 0 ){
                        hm.put(SUB_GROUP , subGrp + item.getSunGroup());
                    } else {
                        hm.put(SUB_GROUP ,"");
                    }
                    hm.put(ADDRESS, item.getAdress());

                    list.add(hm);
                }
            }

        }


        SimpleAdapter simpleAdapter = new SimpleAdapter(context , list , R.layout.lesson_item ,
                from , to);

        return simpleAdapter;

    }
}
