package com.example.alex.scheduleandroid.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alex.scheduleandroid.R;
import com.example.alex.scheduleandroid.dto.WorkDayDTO;

import java.util.List;

public class LessonListAdapter extends RecyclerView.Adapter<LessonListAdapter.LessonViewHolder> {

    private List<WorkDayDTO> data;// расписание с сервера

    public LessonListAdapter(List<WorkDayDTO> data) {
        this.data = data;
    }

    @Override
    public LessonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.work_day_item, parent , false);

        return new LessonListAdapter.LessonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LessonViewHolder holder, int position) {
        WorkDayDTO item = data.get(position);
        holder.textView.setText(item.getDateOfWorkDay());
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public class LessonViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView textView;

        public LessonViewHolder(View itemView) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.cardViewWorkDay);
            textView = (TextView) itemView.findViewById(R.id.dateOfLessons);
        }
    }
}
