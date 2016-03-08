package com.example.alex.scheduleandroid.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

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
        holder.textView.setText(item.getDateOfWorkDay());

        LessonListAdapter lessonListAdapter = new LessonListAdapter(context);
        SimpleAdapter adapter = lessonListAdapter.getAdapter(item);

        holder.listView.setAdapter(adapter);

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
