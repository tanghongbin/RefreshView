package com.example.com.meimeng.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/11/19.
 */
public class MyViewPagerAdapter extends PagerAdapter {

    private Context context;
//    private ArrayList<ExploreListItem> list = new ArrayList<ExploreListItem>();

    private ArrayList<View> list ;
    public MyViewPagerAdapter(Context context,ArrayList<View> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(list.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {


        container.addView(list.get(position));
        return list.get(position);
    }



    public ArrayList<View> getList() {
        return list;
    }

    public void setList(ArrayList<View> list) {
        this.list = list;
    }
}
