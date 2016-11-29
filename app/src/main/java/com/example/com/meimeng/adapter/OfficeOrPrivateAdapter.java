package com.example.com.meimeng.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.com.meimeng.R;
import com.example.com.meimeng.activity.HomeActivity;
import com.example.com.meimeng.gson.gsonbean.EventData;
import com.example.com.meimeng.net.InternetUtils;
import com.example.com.meimeng.util.Utils;

import java.util.List;

/**
 * Created by Zhengjie on 2015/7/12.
 */
public class OfficeOrPrivateAdapter extends ArrayAdapter {
    private int mresource;
    private Context mContext;
    private List<EventData> list;
    private HomeActivity activity = new HomeActivity();

    public OfficeOrPrivateAdapter(Context context, int resource, List<EventData> objects) {
        super(context, resource, objects);
        this.mresource = resource;
        this.mContext = context;
        this.list = objects;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;
        ViewHold viewhold;
        EventData eventData = (EventData) getItem(position);
        /*HashMap map=new HashMap();
        map.put("targetUid",eventData.getActivityId());
        map.put("price",eventData.getPrice());
        MeiMengApplication.goodsPrice.add(map);*/
        if (convertView == null) {
            view = LayoutInflater.from(mContext).inflate(mresource, null);
            viewhold = new ViewHold();
            viewhold.date = (TextView) view.findViewById(R.id.event_item_text_date);
            viewhold.location = (TextView) view.findViewById(R.id.event_item_text_location);
 //           viewhold.personNum = (TextView) view.findViewById(R.id.event_item_text_number);
            viewhold.event_content_rl = (RelativeLayout) view.findViewById(R.id.event_content_rl);
            viewhold.title = (TextView) view.findViewById(R.id.office_event_item_titl);
            viewhold.state = (TextView) view.findViewById(R.id.office_event_item_state);
            viewhold.post = (ImageView) view.findViewById(R.id.office_event_item_image);
            view.setTag(viewhold);
        } else {
            view = convertView;
            viewhold = (ViewHold) view.getTag();
            viewhold.post.setImageResource(R.drawable.meimeng_ico_index_missing);
        }
        viewhold.post.setTag(position);
        //标题截断
        if (eventData != null && eventData.getTitle() != null) {
          // viewhold.title.setText(Utils.setString(eventData.getTitle(), 12));
           viewhold.title.setText(eventData.getTitle()+"");


//            viewhold.personNum.setText(String.valueOf(eventData.getApplyAllCount()));
            switch (eventData.getState()) {     //活动状态 1:报名中，2:即将开始，3:进行中，4:已过期
                //报名中
                case 1:
                    viewhold.state.setText("报名中");

                    break;
                //即将开始
                case 2:
                    viewhold.state.setText("即将开始");

                    break;
                //进行中
                case 3:
                    viewhold.state.setText("进行中");


                    break;
                //过期
                case 4:
                    viewhold.state.setText("已结束");

                    break;
                default:
                    viewhold.state.setText("审核中");

                    break;
            }
            StringBuffer location =new StringBuffer();


            if(TextUtils.isEmpty(eventData.getPlace())){
               location.append("未知");
            }else {
                location.append(eventData.getPlace());

            }

            viewhold.location.setText(Utils.setString(location.toString(), 12));

            viewhold.date.setText(Utils.getDisplayDate(eventData.getStartTime(),"yyyy-MM-dd"));
            InternetUtils.getPicIntoView(375, 220, viewhold.post, eventData.getPic(), position, viewhold.event_content_rl, mContext);

            Utils.applyBlur(mContext, viewhold.post, viewhold.event_content_rl,1);


        }

        return view;

    }

    class ViewHold {
        private ImageView post;
        private TextView title;
        private TextView date;
        private TextView location;
        private RelativeLayout event_content_rl;
//        private TextView personNum;
        private TextView state;
        private boolean isFirst = true;
    }



}
