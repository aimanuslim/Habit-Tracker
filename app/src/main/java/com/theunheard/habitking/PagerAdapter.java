package com.theunheard.habitking;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by ian21 on 3/15/2017.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;


    private DBHandler db;
    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.db = db;
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                InsertDataFragment insertDataFragment = new InsertDataFragment();
                return insertDataFragment;
            case 1:
                DataListFragment dataListFragment = new DataListFragment();
                return dataListFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

}
