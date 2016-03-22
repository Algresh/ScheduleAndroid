package com.example.alex.scheduleandroid.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.alex.scheduleandroid.Constants;
import com.example.alex.scheduleandroid.fragment.InboxFragment;
import com.example.alex.scheduleandroid.fragment.SentFragment;


public class TabsPagerAdapter extends FragmentPagerAdapter {

    private String[] tabs;

    public TabsPagerAdapter(FragmentManager fm, String[] tabsTittle) {
        super(fm);
        tabs = tabsTittle;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case Constants.TAB_INBOX:
                return InboxFragment.getInstance();
            case Constants.TAB_SENT:
                return SentFragment.getInstance();
        }


        return null;
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
