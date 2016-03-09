package com.example.alex.scheduleandroid.adapter;

import android.content.Context;
import android.util.Log;
import android.widget.SimpleAdapter;

import com.example.alex.scheduleandroid.Lesson;
import com.example.alex.scheduleandroid.R;
import com.example.alex.scheduleandroid.dto.WorkDayDTO;

import java.util.ArrayList;
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

    public SimpleAdapter getAdapter(WorkDayDTO itemDTO) {

        ArrayList<HashMap<String , String>> list = new ArrayList<HashMap<String , String>>();

        String title;

        HashMap<String , String> hm;

        for(Lesson item: itemDTO.getLessons()) {

            hm = new HashMap<String , String>();

            title = item.getTypeLesson() + ": " + item.getTitleOfSubject();

            hm.put(CLASS_ROOM , classRoom + item.getClassRoom());
            hm.put(NUMBER_LESSON , numLesson +  item.getNumberOfLesson());
            hm.put(NAME_SUBJECT , title );
            hm.put(TEACHER , item.getTeacher());
            hm.put(SUB_GROUP , subGrp + item.getSunGroup());
            hm.put(ADDRESS , item.getAdress());

            list.add(hm);
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(context , list , R.layout.lesson_item ,
                from , to);

        return simpleAdapter;

    }
}
