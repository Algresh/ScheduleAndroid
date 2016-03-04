package com.example.alex.scheduleandroid.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;


import com.example.alex.scheduleandroid.R;
import com.example.alex.scheduleandroid.dto.GroupDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by algresh on 14.02.16.
 */
public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.GroupViewHolder> {

    private String[] courses;

    final static int NUMBER_OF_CURSES = 6;

    private ArrayList<Map<String , String>> coursesData; //колекция названий курсов

    private ArrayList<ArrayList<Map<String, String>>> childData;// общая коллекция для коллекций элементов


    private List<GroupDTO> data;// расписание с сервера
    private Context context;

    // атрибуты для сопоставления id и и элемента в колекции Map
    private String courseFrom[];
    private int courseTo[];
    private String childFrom[];
    private int childTo[];

    public GroupListAdapter(List<GroupDTO> dataDepartment, Context context) {
        this.data = dataDepartment;
        this.context = context;

        this.courseFrom = new String[] {"course"};
        this.courseTo = new int[] {android.R.id.text1};

        this.childFrom = new String[] {"group"};
        this.childTo = new int[] {android.R.id.text1};

        this.courses = context.getResources().getStringArray(R.array.name_array_curses);


    }

    @Override
    public GroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.department_item, parent , false);

        return new GroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GroupViewHolder holder, int position) {
        GroupDTO item = data.get(position);
        holder.textView.setText(item.getTitle());

        String[] titleGroups = item.getNameOfGroups(); //Получаем группы

        this.filingCourses();

        childData = new ArrayList<ArrayList<Map<String, String>>>();

        for(int i = 0; i < NUMBER_OF_CURSES; i++) {
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

        holder.lvGroup.setAdapter(adapterExp);

    }

    @Override
    public int getItemCount() {
        return data.size();
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

    public static class GroupViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView textView;
        ExpandableListView lvGroup;

        public GroupViewHolder(View itemView) {
            super(itemView);

            lvGroup = (ExpandableListView) itemView.findViewById(R.id.lvGroup);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            textView = (TextView) itemView.findViewById(R.id.titleDepartment);

        }
    }


}
