package com.example.com.meimeng.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.com.meimeng.R;
import java.util.List;

/**
 * Created by 003 on 2015/8/1.
 * 个性展示里面用于多选的页面
 */
public class GxzsListAdapter1 extends BaseAdapter {

    private Context context;
    private List<String> datalist;


    public GxzsListAdapter1(Context context, List<String> objects) {
        this.context = context;
        this.datalist = objects;
    }


    @Override
    public int getCount() {
        return datalist.size();
    }

    @Override
    public String  getItem(int position) {
        return datalist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.gxzsitemlayout1, null);
        ImageView state_image = (ImageView) convertView.findViewById(R.id.state_image);
        TextView content_text = (TextView) convertView.findViewById(R.id.content_text);
        String content = getItem(position);
        state_image.setVisibility(View.INVISIBLE);
        content_text.setText(content);
        return convertView;

    }
}
