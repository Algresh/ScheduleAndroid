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
import com.example.alex.scheduleandroid.R;
import com.example.alex.scheduleandroid.dto.WorkDayDTO;

import java.util.List;

public class WorkDayListAdapter extends RecyclerView.Adapter<WorkDayListAdapter.LessonViewHolder> {

    private List<WorkDayDTO> data;// расписание с сервера

    private Context context;

    public WorkDayListAdapter(List<WorkDayDTO> data, Context context) {
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
        WorkDayDTO item = data.get(position);
        holder.textView.setText(item.getDateOfWorkDay(position));

//        Log.d(ConnectedManager.MY_TAG, position + "");
        LessonListAdapter lessonListAdapter = new LessonListAdapter(context);
        SimpleAdapter adapter = lessonListAdapter.getAdapter(item , position);

        holder.listView.setAdapter(adapter);
        setListViewHeightBasedOnChildren(holder.listView);

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
        return data.size();
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
