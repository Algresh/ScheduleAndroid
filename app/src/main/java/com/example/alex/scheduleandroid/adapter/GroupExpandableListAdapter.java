package com.example.alex.scheduleandroid.adapter;

import android.content.Context;
import android.widget.SimpleExpandableListAdapter;

import com.example.alex.scheduleandroid.R;
import com.example.alex.scheduleandroid.dto.GroupDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class GroupExpandableListAdapter {

    final static int NUMBER_OF_CURSES = 6;

    private ArrayList<Map<String , String>> coursesData; //колекция названий курсов

    private ArrayList<ArrayList<Map<String, String>>> childData;// общая коллекция для коллекций элементов

    // атрибуты для сопоставления id и и элемента в колекции Map
    private String courseFrom[];
    private int courseTo[];
    private String childFrom[];
    private int childTo[];

    private Context context;

    private String[] courses;

    public GroupExpandableListAdapter(Context context) {
        this.context = context;

        this.context = context;

        this.courseFrom = new String[] {"course"};
        this.courseTo = new int[] {android.R.id.text1};

        this.childFrom = new String[] {"group"};
        this.childTo = new int[] {android.R.id.text1};

        this.courses = context.getResources().getStringArray(R.array.name_array_curses);
    }

    public SimpleExpandableListAdapter getAdapter(GroupDTO item) {

        String[] titleGroups; //Получаем группы

        this.filingCourses();

        childData = new ArrayList<ArrayList<Map<String, String>>>();

        for(int i = 0; i < NUMBER_OF_CURSES; i++) {
            titleGroups = item.getNameOfGroups(i);
            childData.add(this.filingGroups(titleGroups));// заполняем коллекцию курсов группами
        }


        SimpleExpandableListAdapter adapterExp = new SimpleExpandableListAdapter(
                context,
                coursesData,
                android.R.layout.simple_expandable_list_item_1,
                courseFrom,
                courseTo,
                childData,
                android.R.layout.simple_list_item_1,
                childFrom,
                childTo);

        return adapterExp;

    }

    private void filingCourses () { // заполнение курсов названиями
        coursesData = new ArrayList<Map<String, String>>();
        Map<String, String> m;

        for (String course : courses) {
            m = new HashMap<String, String>();
            m.put("course", course);
            coursesData.add(m);
        }
    }

    // заполнение групп названиями
    private ArrayList<Map<String, String>> filingGroups (String[] groups) {
        ArrayList<Map<String, String>> childDataItem = new ArrayList<Map<String, String>>();
        Map<String, String> m;

        for (String group : groups) {
            m = new HashMap<String, String>();
            m.put("group", group);
            childDataItem.add(m);
        }

        return childDataItem;
    }
}
