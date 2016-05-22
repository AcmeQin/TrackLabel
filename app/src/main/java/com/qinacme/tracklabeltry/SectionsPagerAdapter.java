package com.qinacme.tracklabeltry;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by qinacme on 2016/5/22.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {
    CurrentFragment mCurrentFragment;
    HistoryFragment mHistoryFragment;
    Context mApp;
    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setPages(CurrentFragment currentFragment,
                         HistoryFragment historyFragment){
        mCurrentFragment =currentFragment;
        mHistoryFragment =historyFragment;
    }

    public void setContext(Context app){
        mApp=app;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        switch (position){
            case 0:
                return mCurrentFragment;
            case 1:
                return mHistoryFragment;
        }
        return mCurrentFragment;
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return mApp.getString(R.string.currentFragementName);
            case 1:
                return mApp.getString(R.string.historyFragementName);
        }
        return null;
    }
}
