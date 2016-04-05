package com.example.alex.scheduleandroid.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.alex.scheduleandroid.Constants;
import com.example.alex.scheduleandroid.R;
import com.example.alex.scheduleandroid.database.DatabaseManager;
import com.example.alex.scheduleandroid.dto.MessageDTO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SentFragment extends Fragment {

    private static final int LAYOUT = R.layout.sent_fragment;
    private ArrayList<Map<String, String>> data;

    private View view;
    private SimpleAdapter simpleAdapter;

    public static SentFragment getInstance() {
        Bundle args = new Bundle();
        SentFragment sentFragment = new SentFragment();
        sentFragment.setArguments(args);

        return sentFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);

        String group = getMyGroup();
        if( !group.equals("")){
            DatabaseManager databaseManager = new DatabaseManager(getContext());

            List<MessageDTO> listMessages = databaseManager.getMyMessages(group);
            data = transformDate(listMessages);

            int[] to = {R.id.textViewMessageTxt, R.id.textViewMessageDate, R.id.imageViewCheckSent};
            String[] from = {Constants.NOTIFICATION_COLUMN_TEXT_MSG, Constants.NOTIFICATION_COLUMN_DATE,
                    Constants.IMAGE_LIST_NOTIFICATION};

            simpleAdapter = new SimpleAdapter(getActivity(), data, R.layout.message_item, from, to);

            ListView listView = (ListView) view.findViewById(R.id.listViewMyMessages);
            listView.setAdapter(simpleAdapter);

            databaseManager.closeDatabase();
        }

        return view;
    }

    private ArrayList<Map<String, String>> transformDate(List<MessageDTO> listMessages) {
        ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
        Map map;
        for (MessageDTO item: listMessages) {
            map = new HashMap();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String date_sent = format.format(item.getDateSent());
            int img;

            if (item.getSent_ok() == 1) {
                img = R.drawable.check_circle_outline;
            } else {
                img = R.drawable.close_circle_outline;
            }

            map.put(Constants.NOTIFICATION_COLUMN_DATE, date_sent );//Время зависит от поставленного на телефоне
            map.put(Constants.NOTIFICATION_COLUMN_TEXT_MSG, item.getTextMsg());
            map.put(Constants.IMAGE_LIST_NOTIFICATION, img);
            list.add(map);
        }

        return list;
    }

    private String getMyGroup () {
        SharedPreferences sPref = getActivity().getSharedPreferences(Constants.GROUP_USER, Context.MODE_PRIVATE);
        return sPref.getString(Constants.GROUP_USER, "");
    }

    public void addNewMessage(String message, int sent_ok) {
        Map map = new HashMap();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String date_sent = format.format(new Date());
        int img;
        if (sent_ok == 1) {
            img = R.drawable.check_circle_outline;
        } else {
            img = R.drawable.close_circle_outline;
        }

        map.put(Constants.NOTIFICATION_COLUMN_DATE, date_sent );//Время зависит от поставленного на телефоне
        map.put(Constants.NOTIFICATION_COLUMN_TEXT_MSG, message);
        map.put(Constants.IMAGE_LIST_NOTIFICATION, img);
        data.add(0, map);
        simpleAdapter.notifyDataSetChanged();

    }
}
