package com.example.com.meimeng.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.com.meimeng.R;
import com.example.com.meimeng.bean.ChatMessageBean;
import com.example.com.meimeng.net.InternetUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by 010 on 2015/7/13.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.NormalTextViewHolder> implements View.OnClickListener {

    private LayoutInflater layoutInflater;

    private Context context;

    private ArrayList<ChatMessageBean> dataList;

    private OnRecyclerViewItemClickListener onRecyclerListener = null;

    public RecyclerViewAdapter(Context context, ArrayList<ChatMessageBean> dataList) {
        this.context = context;
        this.dataList = dataList;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        onRecyclerListener = listener;
    }

    @Override
    public NormalTextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_of_chat, parent, false);
        NormalTextViewHolder viewHolder = new NormalTextViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final NormalTextViewHolder holder, int position) {

        ChatMessageBean bean = dataList.get(position);


//        holder.headerImageView.setImageResource(R.mipmap.ic_launcher);

        holder.nameText.setText(bean.getName());
        holder.timeText.setText(bean.getTime());
        holder.messageText.setText(bean.getContent());


        try {
            Observable observable = InternetUtils.getBytesObservale(bean.getHeadPic(), true);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1() {
                        @Override
                        public void call(Object o) {
                            byte[] bytes = (byte[]) o;
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            holder.headerImageView.setImageBitmap(bitmap);
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
//                            Toast.makeText(context, "SlideListAdapter: 下载图片出错，" + throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }


        //这里要换成用户头像与用户名字
//        if (bean.getConversation()!=null){
//
//            AVIMConversation conversation = bean.getConversation();
//
//            if (ConversationHelper.typeOfConversation(conversation) == ConversationType.Single) {
//                AVUser user = CacheService.lookupUser(ConversationHelper.otherIdOfConversation(conversation));
//                UserService.displayAvatar(user, holder.headerImageView);
//            } else {
//                holder.headerImageView.setImageBitmap(ConversationManager.getConversationIcon(conversation));
//            }
//
//        }


        holder.itemView.setTag(bean);
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    @Override
    public void onClick(View v) {
        if (onRecyclerListener != null) {
            onRecyclerListener.onItemClick(v, (ChatMessageBean) v.getTag());
        }
    }

    public static class NormalTextViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.item_of_chat_header)
        ImageView headerImageView;

        @Bind(R.id.item_of_chat_name)
        TextView nameText;

        @Bind(R.id.item_of_chat_time)
        TextView timeText;

        @Bind(R.id.item_of_chat_message)
        TextView messageText;


        NormalTextViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


    public static interface OnRecyclerViewItemClickListener {
        /**
         * @param view
         * @param bean 这个参数是Recycler中Item的数据
         */
        void onItemClick(View view, ChatMessageBean bean);
    }
}
