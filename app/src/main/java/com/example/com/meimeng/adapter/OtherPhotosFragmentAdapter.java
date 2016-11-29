package com.example.com.meimeng.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.com.meimeng.R;
import com.example.com.meimeng.fragment.OtherPhotosFragment;
import com.example.com.meimeng.gson.gsonbean.PhotoItem;
import com.viewpagerindicator.IconPagerAdapter;

import java.util.List;

public class OtherPhotosFragmentAdapter extends FragmentPagerAdapter{
    private List<PhotoItem> photoIds;

    public OtherPhotosFragmentAdapter(FragmentManager fm,List<PhotoItem> photoIds) {
        super(fm);
        this.photoIds = photoIds;
    }

    @Override
    public Fragment getItem(int position) {
        return OtherPhotosFragment.newInstance(photoIds.get(position).getPicId());
    }

    @Override
    public int getCount() {
        return photoIds.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
      return "";
    }
}