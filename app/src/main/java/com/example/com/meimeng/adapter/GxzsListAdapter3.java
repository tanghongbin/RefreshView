package com.example.com.meimeng.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.com.meimeng.custom.SingleSelectView;

import java.util.List;

/**
 * Created by Administrator on 2015/8/12.
 * 编辑资料页面民族，发型等选择页面的适配器
 */
public class GxzsListAdapter3 extends BaseAdapter {
    private Context context;
    private List<String> datalist;

    public GxzsListAdapter3(Context context, List<String> objects) {
        this.context = context;
        this.datalist = objects;
    }


    @Override
    public int getCount() {
        return datalist.size();
    }

    @Override
    public String getItem(int position) {
        return datalist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final SingleSelectView singleSelectView = new SingleSelectView(context);
        String content = getItem(position);
        singleSelectView.setContentText(content);
        return singleSelectView;

    }


}

