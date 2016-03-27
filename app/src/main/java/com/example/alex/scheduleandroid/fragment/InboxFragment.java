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


public class InboxFragment extends Fragment {

    private static final int LAYOUT = R.layout.inbox_fragment;

    private View view;
    int[] to = {R.id.textViewMessageTxt, R.id.textViewMessageDate, R.id.imageViewCheckSent};
    String[] from = {Constants.NOTIFICATION_COLUMN_TEXT_MSG, Constants.NOTIFICATION_COLUMN_DATE,
            Constants.IMAGE_LIST_NOTIFICATION};

    public static InboxFragment getInstance() {
        Bundle args = new Bundle();
        InboxFragment fragment = new InboxFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);

        String group = getMyGroup();
        if(!group.equals("")) {

//            SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), date, R.layout.message_item, from, to);

            ListView listView = (ListView) view.findViewById(R.id.listViewMyMessages);
        }

        return view;
    }

    private String getMyGroup () {
        SharedPreferences sPref = getActivity().getSharedPreferences(Constants.GROUP_USER, Context.MODE_PRIVATE);
        return sPref.getString(Constants.GROUP_USER, "");
    }
}
