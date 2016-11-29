package com.example.com.meimeng.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.com.meimeng.R;
import com.example.com.meimeng.gson.gsonbean.UserActivityListItem;
import com.example.com.meimeng.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的活动Adapter
 */
public class UserActivityListAdapter extends BaseAdapter {

    private Context mContext;
    List<UserActivityListItem> datalist = new ArrayList<>();

    public UserActivityListAdapter(Context context, List<UserActivityListItem> datalist) {
        this.mContext = context;
        this.datalist = datalist;
    }

    @Override
    public int getCount() {
        return datalist.size();
    }

    @Override
    public Object getItem(int position) {
        return datalist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UserActivityListItem data = datalist.get(position);
        View view;

        view = LayoutInflater.from(mContext).inflate(R.layout.useractivity_listviewitem, null);
        TextView personNum = (TextView) view.findViewById(R.id.event_item_people_num);
        TextView date = (TextView) view.findViewById(R.id.event_item_text_date);
        TextView location = (TextView) view.findViewById(R.id.event_item_text_location);
        TextView title = (TextView) view.findViewById(R.id.office_event_item_title);
        TextView state = (TextView) view.findViewById(R.id.myeventtime_imageview);

        //设置时间,是否过期
        if (data.getTitle().length() > 14) {
            title.setText(data.getTitle().substring(0, 14) + "...");
        } else {
            title.setText(data.getTitle());
        }

        location.setText(data.getPlace());
        date.setText(Utils.getDisplayDate(data.getStartTime()));
        personNum.setText(String.valueOf(data.getApplyAllCount()));
        switch (data.getState()) {
            //报名中
            case 1:
                state.setText("报名中");
                state.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.text_4_shape));
                break;
            //即将开始
            case 2:
                state.setText("即将开始");
                state.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.text_4_shape));
                break;
            //进行中
            case 3:
                state.setText("进行中");
                state.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.text_on_shape));
                break;
            //过期
            case 4:
                state.setText("活动结束");
                state.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.text_end_shape));
                break;
            case -1:
                state.setText("审核不通过");
                state.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.text_end_shape));
                break;
            default:
                state.setText("审核中");
                state.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.text_4_shape));
                break;
        }

        return view;

    }

}
