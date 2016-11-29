package com.example.com.meimeng.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.BaseAdapter;

import com.example.com.meimeng.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/12/4.
 */
public class weblistAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> list = new ArrayList<>();

    public weblistAdapter(Context context,ArrayList<String> list){
        this.context = context;
        this.list = list;

    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String url = list.get(position);

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_web_item,null);
        }
        WebView webView = (WebView) convertView.findViewById(R.id.web_item);

        WebSettings mWebSettings=webView.getSettings();
        mWebSettings.setUseWideViewPort(true);
        mWebSettings.setLoadWithOverviewMode(true);
        if(!TextUtils.isEmpty(url)) {
            webView.loadUrl(url);
        }
        return convertView;
    }

    public ArrayList<String> getList() {
        return list;
    }

    public void setList(ArrayList<String> list) {
        this.list = list;
    }
}
