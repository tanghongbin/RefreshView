package com.example.com.meimeng.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.com.meimeng.R;
import com.example.com.meimeng.gson.gsonbean.ReceivedGiftItem;
import com.example.com.meimeng.util.ImageUtil;

import java.util.ArrayList;

/**
 * Created by 010 on 2015/7/16.
 * 收到的用户礼物list
 */
public class GiftAdapter extends BaseAdapter {

    private Context context;

    private ArrayList<ReceivedGiftItem> giftList;

    public GiftAdapter(Context context, ArrayList<ReceivedGiftItem> giftList) {
        this.context = context;
        this.giftList = giftList;
    }

    @Override
    public int getCount() {
        return giftList.size();
    }

    @Override
    public Object getItem(int position) {
        return giftList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    int i;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_of_gift_grid, null);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) convertView.findViewById(R.id.gift_name);
            viewHolder.pictureImageView = (ImageView) convertView.findViewById(R.id.gift_picture);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            //resetViewHolder(viewHolder);
        }
        //viewHolder.pictureImageView.setTag(position);
       ReceivedGiftItem item = giftList.get(position);
       /* String name = String.valueOf(item.getGiftPic());
        Bitmap bitmap = ImageUtil.getGiftImage(context, name);
        if(bitmap != null){
            viewHolder.pictureImageView.setImageBitmap(bitmap);
        }
        if(!TextUtils.isEmpty(item.getGiftName())){
            viewHolder.textView.setText(item.getGiftName());
        }*/


      /*  long giftId=receivedGiftItem.getGiftId();
        switch ((int)giftId){
            case 1:
                resetViewHolder(viewHolder.textView,viewHolder.pictureImageView ,R.drawable.g_4441,"巧克力");
                break;
            case 2:
                resetViewHolder(viewHolder.textView,viewHolder.pictureImageView ,R.drawable.g_4442,"玫瑰花");
                break;
            case 3:
                resetViewHolder(viewHolder.textView,viewHolder.pictureImageView ,R.drawable.g_4443,"手表");
                break;
            case 4:
                resetViewHolder(viewHolder.textView,viewHolder.pictureImageView ,R.drawable.g_4444,"香水");
                break;
            case 5:
                resetViewHolder(viewHolder.textView,viewHolder.pictureImageView ,R.drawable.g_4445,"燕尾服");
                break;
            case 6:
                resetViewHolder(viewHolder.textView,viewHolder.pictureImageView ,R.drawable.g_4446,"晚礼服");
                break;
            case 7:
                resetViewHolder(viewHolder.textView,viewHolder.pictureImageView ,R.drawable.g_4447,"跑车");
                break;
            case 8:
                resetViewHolder(viewHolder.textView,viewHolder.pictureImageView ,R.drawable.g_4448,"项链");
                break;
            default:
                break;
        }*/
       viewHolder.textView.setText(item.getName());
//        InternetUtils2.getPicIntoView(200, 200, viewHolder.pictureImageView, receivedGiftItem.getGiftPic(), position, true);
        //
        String name = String.valueOf(item.getGiftPic());
        Bitmap bitmap = ImageUtil.getGiftImage(context, name);
        viewHolder.pictureImageView.setImageBitmap(bitmap);
        //
        return convertView;
    }

   /* private void resetViewHolder(TextView tv,ImageView iv,int resId,String name) {
            tv.setText(name);
            iv.setImageResource(resId);
    }*/
    private class ViewHolder {
        private ImageView pictureImageView;
        private TextView textView;
    }
}
