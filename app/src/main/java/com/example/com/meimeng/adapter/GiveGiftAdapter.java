package com.example.com.meimeng.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.com.meimeng.R;
import com.example.com.meimeng.bean.GiftInfoBean;

import java.util.ArrayList;

/**
 * Created by 010 on 2015/7/16.
 * 赠送礼物adpter
 */
public class GiveGiftAdapter extends BaseAdapter {

    private Context context;

    private ArrayList<GiftInfoBean> giftList=new ArrayList<>();

    public GiveGiftAdapter(Context context, ArrayList<GiftInfoBean> giftList) {
        this.context = context;
        this.giftList = giftList;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return giftList.get(position);
    }

    @Override
    public int getCount() {
        return giftList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_of_give_gift, null);
            viewHolder = new ViewHolder();
            viewHolder.pictureImageView = (ImageView) convertView.findViewById(R.id.item_of_give_gift_picture);
            viewHolder.giftNameText = (TextView) convertView.findViewById(R.id.item_of_give_gift_name_text);
            viewHolder.giftPriceText = (TextView) convertView.findViewById(R.id.item_of_give_gift_price_text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            resetViewHolder(viewHolder);
        }
        GiftInfoBean giftInfoBean = giftList.get(position);

        viewHolder.pictureImageView.setImageResource(giftInfoBean.getGoodsPicId());

        viewHolder.giftNameText.setText(giftInfoBean.getGoodsName());

        viewHolder.giftPriceText.setText(giftInfoBean.getGoodsPrices()+"M");
        return convertView;
    }

    private void resetViewHolder(ViewHolder viewHolder) {
        viewHolder.pictureImageView.setImageBitmap(null);

        viewHolder.giftNameText.setText(null);

        viewHolder.giftPriceText.setText(null);
    }

    private class ViewHolder {
        private ImageView pictureImageView;

        private TextView giftNameText;

        private TextView giftPriceText;

    }
}
