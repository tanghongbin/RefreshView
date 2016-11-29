package com.xqd.chatmessage.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.xqd.chatmessage.ChatAplication;
import com.xqd.chatmessage.R;
import com.xqd.chatmessage.gson.ChatMessageBean;
import com.xqd.chatmessage.net.InternetUtils;
import com.xqd.chatmessage.util.BadgeUtil;
import com.xqd.chatmessage.util.MessageUtils;
import com.xqd.chatmessage.util.NewMessageState;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by lx on 2015/8/10.
 */
public class ChatMessageAdapter extends BaseAdapter {

    private static final String TAG = "ChatMessageAdapter";

    private Context context;

    private List<ChatMessageBean> dataList;

    private NewMessageState nms ;

    public ChatMessageAdapter(Context context, List<ChatMessageBean> dataList) {

        this.context = context;
        this.dataList = dataList;
        this.nms =NewMessageState.getInstance(context);
        for (int i = 0; i < dataList.size(); i++) {
            ChatMessageBean bean = dataList.get(i);
            if (bean.getContent() == null || bean.getContent().equals("")) {
                Log.e(TAG, "1--------------------聊天消息是空的----------------" + i);
            }
        }

    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();

        for (int i = 0; i < dataList.size(); i++) {
            ChatMessageBean bean = dataList.get(i);
            if (bean.getContent() == null || bean.getContent().equals("")) {
                Log.e(TAG, "2--------------------聊天消息是空的----------------" + i);
                dataList.remove(i);
            }

        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ChatMessageBean bean = dataList.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_of_chat, null);
        }
        final TextView message_red_listview =(TextView) convertView.findViewById(R.id.message_red_listview);

        final ImageView headerImageView = (ImageView) convertView.findViewById(R.id.item_of_chat_header);

        TextView nameText = (TextView) convertView.findViewById(R.id.item_of_chat_name);

        TextView timeText = (TextView) convertView.findViewById(R.id.item_of_chat_time);

        TextView messageText = (TextView) convertView.findViewById(R.id.item_of_chat_message);


        nameText.setText(bean.getName());
        timeText.setText(bean.getTime());
        messageText.setText(bean.getContent());

        int state = nms.getState(bean.getUserId()+"");

        if(state != 0 && state != -1){
            message_red_listview.setVisibility(View.VISIBLE);
            message_red_listview.setText(state+"");
        }else if(message_red_listview.getVisibility() == View.VISIBLE ){
            message_red_listview.setVisibility(View.GONE);
        }

        try {
            Observable observable = InternetUtils.getBytesObservale(bean.getHeadPic(), true);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1() {
                        @Override
                        public void call(Object o) {
                            byte[] bytes = (byte[]) o;
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            headerImageView.setImageBitmap(bitmap);
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            Toast.makeText(context, "SlideListAdapter: 下载图片出错，" + throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MessageUtils.setLeanCloudSelfUid();
                MessageUtils.setLeanCloudOtherUid(context, bean.getUserId());

                goneMessageRed_tabbar();
                message_red_listview.setVisibility(View.GONE);
                nms.setState(bean.getUserId()+"",0);

            }
        });

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    public List<ChatMessageBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<ChatMessageBean> dataList) {
        this.dataList = dataList;
    }


    public void goneMessageRed_tabbar(){
        int flag = 0;
        for (ChatMessageBean bean : dataList){
            if(nms.getState(bean.getUserId()+"") != 0){
                flag ++;
            }
        }

        if(flag ==  1|| flag ==0){
            nms.setState("new message", 0);
            ChatAplication.messageRed.setVisibility(View.GONE);
            BadgeUtil.resetBadgeCount(context);
        }

    }
}
