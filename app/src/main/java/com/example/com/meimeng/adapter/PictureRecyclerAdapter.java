package com.example.com.meimeng.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.com.meimeng.R;
import com.example.com.meimeng.bean.PictureBean;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 010 on 2015/7/16.
 */
public class PictureRecyclerAdapter extends RecyclerView.Adapter<PictureRecyclerAdapter.PictureViewHolder> {

    private Context context;

    private ArrayList<PictureBean> pictureList;

    public PictureRecyclerAdapter(Context context, ArrayList<PictureBean> pictureList) {
        this.context = context;
        this.pictureList = pictureList;
    }

    @Override
    public PictureViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_of_self_gridview, parent, false);
        PictureViewHolder viewHolder = new PictureViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PictureViewHolder holder, int position) {
        PictureBean bean = pictureList.get(position);

        holder.pictureImage.setImageResource(R.drawable.meimeng_ico_user_missing_small);
    }

    @Override
    public int getItemCount() {
        return pictureList == null ? 0 : pictureList.size();
    }

    public class PictureViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.picture)
        ImageView pictureImage;


        PictureViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
