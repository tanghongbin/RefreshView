package com.example.com.meimeng.activity.dataedit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.com.meimeng.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by songhuaiyu on 2015/12/1.
 */
public class MultiSelectableAdapter extends BaseAdapter {
    // 填充数据的list
    private List<String> list;
    // 用来控制CheckBox的选中状况
    private HashMap<Integer, Boolean> isSelected;
    // 上下文
    private Context context;
    // 用来导入布局
    private LayoutInflater inflater = null;

    // 构造器
    public MultiSelectableAdapter(List<String> list, Context context) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
        isSelected = new HashMap<Integer, Boolean>();
        // 初始化数据
        initDate();
    }

    // 初始化isSelected的数据
    private void initDate() {
        for (int i = 0; i < list.size(); i++) {
            getIsSelected().put(i, false);
        }
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
        ViewHolder holder = null;
        /*CustomViewHolder customHolder = null;
        if(position == 0){
            if (customHolder == null) {
                customHolder = new CustomViewHolder();
                convertView = inflater.inflate(R.layout.add_custom_item, null);
                customHolder.addImageView = (ImageView) convertView.findViewById(R.id.add_custom_image_view);
                customHolder.tipsTextView = (TextView) convertView.findViewById(R.id.item_add_tips_text_view);
                convertView.setTag(customHolder);
            } else {
                customHolder = (CustomViewHolder) convertView.getTag();
            }

            if(isSelected.size() >= 9){
                customHolder.tipsTextView.setVisibility(View.VISIBLE);
            }else{
                customHolder.tipsTextView.setVisibility(View.GONE);
            }
            return convertView;
        }else */{
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.gxzsitemlayout2, null);
                holder.tv = (TextView) convertView.findViewById(R.id.content_text);
                holder.cb = (CheckBox) convertView.findViewById(R.id.check_box);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv.setText(list.get(position));
            holder.cb.setChecked(getIsSelected().get(position));
            return convertView;
        }
    }

    public HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(HashMap<Integer, Boolean> isSelected) {
        this.isSelected = isSelected;
    }

    public static class CustomViewHolder {
        ImageView addImageView;
        TextView tipsTextView;
    }

    public static class ViewHolder {
        TextView tv;
        CheckBox cb;
    }
}
