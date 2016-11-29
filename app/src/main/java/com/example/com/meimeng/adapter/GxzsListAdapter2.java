package com.example.com.meimeng.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.com.meimeng.custom.SingleSelectView;

import java.util.List;

/**
 * Created by 003 on 2015/8/1.
 * 个性展示里面用户单选的页面
 */
public class GxzsListAdapter2 extends BaseAdapter {

    private Context context;
    private List<String> datalist;


    public GxzsListAdapter2(Context context, List<String> objects) {
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
        singleSelectView.setFirstText((char) (position + 97) + "");
        String content = getItem(position);
        singleSelectView.setContentText(content);
        return singleSelectView;

    }


}
