package com.example.alex.scheduleandroid.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.alex.scheduleandroid.ConnectedManager;
import com.example.alex.scheduleandroid.Constants;
import com.example.alex.scheduleandroid.R;
import com.example.alex.scheduleandroid.dto.MessageDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InboxFragment extends Fragment {

    private static final int LAYOUT = R.layout.inbox_fragment;

    private View view;
    int[] to = {R.id.textViewMessageTxt, R.id.textViewMessageDate};
    String[] from = {Constants.NOTIFICATION_COLUMN_TEXT_MSG, Constants.NOTIFICATION_COLUMN_DATE};

    ListView listView;

    private ProgressDialog pDialog;

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
            listView = (ListView) view.findViewById(R.id.listViewOtherMessages);
            new  InboxMessagesDownloader().execute();
        }

        return view;
    }

    private String getMyGroup () {
        SharedPreferences sPref = getActivity().getSharedPreferences(Constants.GROUP_USER, Context.MODE_PRIVATE);
        return sPref.getString(Constants.GROUP_USER, "");
    }

    private ArrayList<Map<String, String>> transformDate(List<MessageDTO> listMessages) {
        ArrayList<Map<String, String>> list = new ArrayList<>();
        Map map;
        for (MessageDTO item: listMessages) {
            map = new HashMap();
            String date_sent = item.getDateSentString();


            map.put(Constants.NOTIFICATION_COLUMN_DATE, date_sent );
            map.put(Constants.NOTIFICATION_COLUMN_TEXT_MSG, item.getTextMsg());
            list.add(map);
        }

        return list;
    }

    public class InboxMessagesDownloader extends AsyncTask<Void, Void,  List<MessageDTO>>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            String strMsg = getContext().getString(R.string.downloadingGroups);

            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage(strMsg);
            pDialog.show();
        }

        @Override
        protected List<MessageDTO> doInBackground(Void... params) {
            ConnectedManager connectedManager = new ConnectedManager(getContext());

            if(connectedManager.checkConnection()) {
                return  connectedManager.getMessages();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<MessageDTO> messageDTOs) {
            super.onPostExecute(messageDTOs);
            pDialog.dismiss();

            if (messageDTOs != null) {
                ArrayList<Map<String, String>> date = transformDate(messageDTOs);

                SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), date, R.layout.message_inbox_item, from, to);
                listView.setAdapter(simpleAdapter);
            }
        }
    }
}
