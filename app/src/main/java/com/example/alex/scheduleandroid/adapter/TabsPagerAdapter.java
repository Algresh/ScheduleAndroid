package com.example.alex.scheduleandroid.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.alex.scheduleandroid.Constants;
import com.example.alex.scheduleandroid.fragment.InboxFragment;
import com.example.alex.scheduleandroid.fragment.SentFragment;


public class TabsPagerAdapter extends FragmentPagerAdapter {

    private String[] tabs;
    private InboxFragment inboxFragment;
    private SentFragment sentFragment;

    public TabsPagerAdapter(FragmentManager fm, String[] tabsTittle) {
        super(fm);
        tabs = tabsTittle;
        inboxFragment = InboxFragment.getInstance();
        sentFragment = SentFragment.getInstance();
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case Constants.TAB_INBOX:
                return inboxFragment;
            case Constants.TAB_SENT:
                return sentFragment;
        }


        return null;
    }

    public SentFragment getSentFragment() {
        return sentFragment;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return tabs[position];
    }

    @Override
    public int getCount() {
        return tabs.length;
    }
}
