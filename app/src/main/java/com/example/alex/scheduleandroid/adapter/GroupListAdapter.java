package com.example.alex.scheduleandroid.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;


import com.example.alex.scheduleandroid.LessonsShowActivity;
import com.example.alex.scheduleandroid.R;
import com.example.alex.scheduleandroid.dto.GroupDTO;

import java.util.List;

public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.GroupViewHolder> {

    private List<GroupDTO> data;// расписание с сервера
    private Context context;


    public GroupListAdapter(List<GroupDTO> dataDepartment, Context context) {
        this.data = dataDepartment;
        this.context = context;
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

        GroupExpandableListAdapter groupExpandableListAdapter = new GroupExpandableListAdapter(context);

        holder.lvGroup.setAdapter(groupExpandableListAdapter.getAdapter(item));

        holder.lvGroup.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                String groupName = ((TextView) v).getText().toString();
                Intent intent = new Intent(context, LessonsShowActivity.class);
                intent.putExtra("group" , groupName);
                context.startActivity(intent);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
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
