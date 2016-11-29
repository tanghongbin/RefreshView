package com.example.com.meimeng.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.com.meimeng.MeiMengApplication;
import com.example.com.meimeng.R;
import com.example.com.meimeng.activity.PayActivity;
import com.example.com.meimeng.gson.gsonbean.MyEventList;
import com.example.com.meimeng.net.InternetUtils;
import com.example.com.meimeng.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/25.
 */
public class MyEventAdater extends BaseAdapter {

    private Context context;
    private static double price;
//    private ArrayList<String> list = new ArrayList<>();

    private List<MyEventList> list = new ArrayList<>();

    public MyEventAdater(Context context) {
        this.context = context;

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
        MyEventList myEvent = list.get(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_myevent_item, null);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.myevent_img);
            viewHolder.title = (TextView) convertView.findViewById(R.id.myevent_title);
            viewHolder.place = (TextView) convertView.findViewById(R.id.myevent_place);
            viewHolder.date = (TextView) convertView.findViewById(R.id.myevent_date);
            viewHolder.order = (TextView) convertView.findViewById(R.id.myevent_order);
            viewHolder.pay_btn = (RelativeLayout) convertView.findViewById(R.id.myevent_btn);
            viewHolder.pay_text = (TextView) convertView.findViewById(R.id.pay_text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();

        }

        viewHolder.imageView.setTag(position);


        if (!TextUtils.isEmpty(myEvent.getTitle())) {
            viewHolder.title.setText(myEvent.getTitle());
        }
        StringBuffer sb = new StringBuffer();
        if (!TextUtils.isEmpty(myEvent.getPlace())) {
            sb.append(myEvent.getPlace());
        }
        if (!TextUtils.isEmpty(myEvent.getPlaceDetail())) {
            sb.append("-").append(myEvent.getPlaceDetail());
        }
        viewHolder.place.setText(Utils.setString(sb.toString(), 14));

        Long time = myEvent.getStartTime();
        if (time != null) {
            viewHolder.date.setText(Utils.getDisplayDate(time, "yyyy.MM.dd"));
        }

        int paystate = myEvent.getPayState();
        int state = myEvent.getState();
        if (state == 1) {
            switch (paystate) {
                case 0://未支付
                    viewHolder.pay_text.setText("未支付");
                    viewHolder.pay_text.setBackgroundResource(R.color.myevent_btn_detail_color);
                    setonClick(viewHolder.pay_text, myEvent);
                    break;
                case 1://已支付
                    viewHolder.pay_text.setText("已支付");
                    viewHolder.pay_text.setBackgroundResource(R.color.myevent_btn_pressed_color);
                    break;
                case 2://支付失败
                    viewHolder.pay_text.setText("支付失败");
                    viewHolder.pay_text.setBackgroundResource(R.color.myevent_btn_detail_color);
                    setonClick(viewHolder.pay_text, myEvent);
                    break;
            }
        } else {
            viewHolder.pay_text.setText("已结束");
            viewHolder.pay_text.setBackgroundResource(R.color.myevent_btn_pressed_color);
        }
        if (myEvent.getPic() != null) {
            InternetUtils.getPicIntoView(160, 160, viewHolder.imageView, myEvent.getPic(), position, 2);
        }
        return convertView;
    }

    private void setonClick(TextView view, final MyEventList myEvent) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "pay_btn", Toast.LENGTH_SHORT).show();
                MeiMengApplication.payTitle = myEvent.getTitle();
                MeiMengApplication.payActivityId = myEvent.getActivityId();
                MeiMengApplication.payPrice = myEvent.getPrice();
                MeiMengApplication.weixinPayCallBack = 3;
                Intent intent = new Intent(context, PayActivity.class);
                intent.putExtra("name", myEvent.getTitle());
                intent.putExtra("goodId", -2l);
                intent.putExtra("targetUid", myEvent.getActivityId());
                intent.putExtra("price", "" + myEvent.getPrice());
                intent.putExtra("pay_type", 3);
                context.startActivity(intent);


            }
        });
    }

    private class ViewHolder {
        ImageView imageView;
        TextView title;
        TextView place;
        TextView date;
        TextView order;
        RelativeLayout pay_btn;
        TextView pay_text;

    }

    public List<MyEventList> getList() {
        return list;
    }

    public void setList(List<MyEventList> list) {
        this.list = list;
    }
}
