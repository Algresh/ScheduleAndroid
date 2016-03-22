package com.example.alex.scheduleandroid.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alex.scheduleandroid.R;


public class SentFragment extends Fragment {

    private static final int LAYOUT = R.layout.sent_fragment;

    private View view;

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
        return view;
    }
}
