package com.example.com.meimeng.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shi-02 on 2015/4/21.
 */
public class ViewsPagerAdapter extends PagerAdapter {

    private ArrayList<View> viewList;

    public ViewsPagerAdapter(ArrayList<View> viewList){
        this.viewList = viewList;
    }

    @Override
    public int getCount() {
        return viewList.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    //销毁Item
    @Override
    public void destroyItem(View view, int position, Object object)
    {
        ((ViewPager) view).removeView(viewList.get(position));
    }


    //更新数据
    public void refresh(List<View> objects) {
        this.viewList = (ArrayList<View>) objects;
        notifyDataSetChanged();
    }
    //实例化Item
    @Override
    public Object instantiateItem(View view, int position)
    {
        ((ViewPager) view).addView(viewList.get(position), 0);
        return viewList.get(position);
    }
}
