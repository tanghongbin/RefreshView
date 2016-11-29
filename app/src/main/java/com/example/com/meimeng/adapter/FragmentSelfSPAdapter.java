package com.example.com.meimeng.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;


/**
 * Created by shi-02 on 2015/4/14.
 */
public class FragmentSelfSPAdapter extends FragmentStatePagerAdapter {

    private ArrayList<Fragment> fragmentList;

    public FragmentSelfSPAdapter(FragmentManager fm, ArrayList<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;

    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }


}
