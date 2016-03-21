package com.example.alex.scheduleandroid.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.alex.scheduleandroid.ConnectedManager;
import com.example.alex.scheduleandroid.Constants;
import com.example.alex.scheduleandroid.R;
import com.example.alex.scheduleandroid.dto.Lesson;
import com.example.alex.scheduleandroid.dto.WorkDayDTO;

import java.util.List;

public class WorkDayListAdapter extends RecyclerView.Adapter<WorkDayListAdapter.LessonViewHolder> {

    private WorkDayDTO data;// расписание с сервера

    private Context context;

    public WorkDayListAdapter(WorkDayDTO data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public LessonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.work_day_item, parent , false);

        return new WorkDayListAdapter.LessonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LessonViewHolder holder, int position) {
//        WorkDayDTO item = data.get(1);
//        if (position > 6) position = 6;

        holder.textView.setText(data.getDateOfWorkDay(position));

        LessonListAdapter lessonListAdapter = new LessonListAdapter(context);
        SimpleAdapter adapter = lessonListAdapter.getAdapter(data , position);

        holder.listView.setAdapter(adapter);
        setListViewHeightBasedOnChildren(holder.listView);

    }

    public void addNewItems(List<String> dateOfWorkDay) {
        data.addNewDateOfWorkDay(dateOfWorkDay);
//        data.add(data.size(), workDayDTO);
        notifyItemInserted(data.getNumberOfDateOfWorkDay() - 1);
//        notifyItemInserted(data.size() + Constants.DAYS_FOR_SHOWING - 1);
//        notifyDataSetChanged();
//        notifyItemRangeInserted(data.size() + 1, 7);
    }




    public static void setListViewHeightBasedOnChildren(ListView listView) {
        /**
         * @TODO understand how this method worked
         */
        ListAdapter listAdapter = listView.getAdapter();// получаем адаптер списка
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }


    @Override
    public int getItemCount() {
        return data.getNumberOfDateOfWorkDay();
    }

    public class LessonViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView textView;
        ListView listView;
        public LessonViewHolder(View itemView) {
            super(itemView);

            listView = (ListView) itemView.findViewById(R.id.listViewLessons);
            cardView = (CardView) itemView.findViewById(R.id.cardViewWorkDay);
            textView = (TextView) itemView.findViewById(R.id.dateOfLessons);
        }
    }
}
