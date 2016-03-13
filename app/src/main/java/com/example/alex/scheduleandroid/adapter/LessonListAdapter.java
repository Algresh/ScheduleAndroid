package com.example.alex.scheduleandroid.adapter;

import android.content.Context;
import android.widget.SimpleAdapter;

import com.example.alex.scheduleandroid.Constants;
import com.example.alex.scheduleandroid.dto.Lesson;
import com.example.alex.scheduleandroid.R;
import com.example.alex.scheduleandroid.dto.WorkDayDTO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class LessonListAdapter {

    Context context;

    String[] from = {Constants.CLASS_ROOM , Constants.NUMBER_LESSON , Constants.NAME_SUBJECT ,
            Constants.TEACHER , Constants.SUB_GROUP , Constants.ADDRESS};

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

        long dateLong1 =  10000 * calendar.get(Calendar.YEAR)  + calendar.get(Calendar.MONTH) * 100 + calendar.get(Calendar.DAY_OF_MONTH);
        long dateLong2 = 0;

        ArrayList<HashMap<String , String>> list = new ArrayList<HashMap<String , String>>();

        String title;

        HashMap<String , String> hm;

        boolean dayWithoutLessons = true; // флаг показывающий есть ли занятие в этот день

        for(Lesson item: itemDTO.getLessons()) {

            for(String strDate : item.getDateOfLesson()) {
                try {
                    Date date = format.parse(strDate);
                    calendar2.setTime(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                dateLong2 = 10000 * calendar2.get(Calendar.YEAR)  + calendar2.get(Calendar.MONTH) * 100 + calendar2.get(Calendar.DAY_OF_MONTH);

                if(dateLong1 == dateLong2) {
                    dayWithoutLessons = false;

                    hm = new HashMap<String , String>();

                    title = item.getTypeLesson() + ": " + item.getTitleOfSubject();

                    hm.put(Constants.CLASS_ROOM , classRoom + item.getClassRoom());
                    hm.put(Constants.NUMBER_LESSON , numLesson +  item.getNumberOfLesson());
                    hm.put(Constants.NAME_SUBJECT , title );
                    hm.put(Constants.TEACHER , item.getTeacher());
                    if(item.getSunGroup() != 0 ){
                        hm.put(Constants.SUB_GROUP , subGrp + item.getSunGroup());
                    } else {
                        hm.put(Constants.SUB_GROUP ,"");
                    }
                    hm.put(Constants.ADDRESS, item.getAdress());

                    list.add(hm);
                }
            }

        }

        if (dayWithoutLessons) {
            hm = new HashMap<String , String>();
            title = context.getResources().getString(R.string.noLessons);
            hm.put(Constants.NAME_SUBJECT , title );
            list.add(hm);
        }




        SimpleAdapter simpleAdapter = new SimpleAdapter(context , list , R.layout.lesson_item ,
                from , to);

        return simpleAdapter;

    }
}
