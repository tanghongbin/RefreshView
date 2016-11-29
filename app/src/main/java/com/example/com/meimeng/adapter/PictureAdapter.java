package com.example.com.meimeng.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.com.meimeng.R;
import com.example.com.meimeng.activity.PhotoDisplay.PhotoDisplayEditActivity;
import com.example.com.meimeng.bean.PhotoIdBean;
import com.example.com.meimeng.net.InternetUtils;

import java.util.ArrayList;


/**
 * Created by 010 on 2015/7/16.
 */
public class PictureAdapter extends BaseAdapter {

    private Context context;

    private ArrayList<PhotoIdBean> pictureList;

    public PictureAdapter(Context context, ArrayList<PhotoIdBean> pictureList) {
        this.context = context;
        this.pictureList = pictureList;
    }

    @Override
    public int getCount() {
        return pictureList.size();
    }

    @Override
    public Object getItem(int position) {
        return pictureList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_of_self_gridview, null);
            viewHolder = new ViewHolder();
            viewHolder.pictureImageView = (ImageView) convertView.findViewById(R.id.picture);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            resetViewHolder(viewHolder);
        }


        final int currentPosition = position;

        viewHolder.pictureImageView.setTag(position);
        final PhotoIdBean bean = pictureList.get(position);
        viewHolder.pictureImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PhotoDisplayEditActivity.class);
                intent.putExtra("ImageId", bean.getPhotoId());
                intent.putExtra("position",currentPosition);
                intent.putExtra("allImageId",getPictureIdList(pictureList));

                intent.putExtra("type",1);
                Log.e("bean.getPhotoId()",String.valueOf(bean.getPhotoId()));
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.pic_in, R.anim.pic_out);
            }
        });

        InternetUtils.getPicIntoView(200,200,viewHolder.pictureImageView, bean.getPhotoId(),position,true);

        return convertView;

    }

    private void resetViewHolder(ViewHolder viewHolder) {
        viewHolder.pictureImageView.setImageBitmap(null);
    }

    private class ViewHolder {
        private ImageView pictureImageView;

        private TextView textView;
    }

    /**
     * �
     * @param list
     * @return
     */
    private ArrayList<Long> getPictureIdList(ArrayList<PhotoIdBean> list){
        ArrayList<Long> pictureIdList = new ArrayList<>();

        for (int i = 0;i<list.size();i++){
            long id = list.get(i).getPhotoId();
            pictureIdList.add(id);
        }

        return pictureIdList;
    }
}
