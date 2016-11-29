package com.xqd.chatmessage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xqd.chatmessage.R;
import com.xqd.chatmessage.gson.UserPeopleBean;
import com.xqd.chatmessage.net.InternetUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/9/9.
 */
public class friendsAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<UserPeopleBean> userPeopleBeans = new ArrayList<>();


    public friendsAdapter(Context context, ArrayList<UserPeopleBean> userPeopleBeans) {
        this.context = context;
        this.userPeopleBeans = userPeopleBeans;
    }

    @Override
    public int getCount() {
        return userPeopleBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return userPeopleBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.fragment_friends_listitem, null);
            viewHolder = new ViewHolder();

            viewHolder.headerImageView = (ImageView) convertView.findViewById(R.id.slide_header_image_view);
            viewHolder.nameText = (TextView) convertView.findViewById(R.id.slide_name_text);
            viewHolder.telText = (TextView) convertView.findViewById(R.id.slide_tel_text);
//            viewHolder.placeText = (TextView) convertView.findViewById(R.id.slide_place_text);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            resetViewHolder(viewHolder);
        }
        viewHolder.headerImageView.setTag(position);
        UserPeopleBean bean = userPeopleBeans.get(position);

        InternetUtils.getPicIntoView(200, 200, viewHolder.headerImageView, bean.getHeadId(), position, true);
        viewHolder.nameText.setText(bean.getName());
        viewHolder.telText.setText(bean.getTel());
//        viewHolder.placeText.setText(bean.getCity());
        return convertView;
    }

    private void resetViewHolder(ViewHolder viewHolder) {
        viewHolder.headerImageView.setImageBitmap(null);
        viewHolder.nameText.setText(null);
        viewHolder.telText.setText(null);
//        viewHolder.placeText.setText(null);
    }

    private class ViewHolder {
        private ImageView headerImageView;
        private TextView nameText;
        private TextView telText;
//        private TextView placeText;

    }



}
