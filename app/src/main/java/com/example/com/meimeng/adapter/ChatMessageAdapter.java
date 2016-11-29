package com.example.com.meimeng.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.com.meimeng.MeiMengApplication;
import com.example.com.meimeng.R;
import com.example.com.meimeng.activity.HomeActivity;
import com.example.com.meimeng.bean.ChatMessageBean;
import com.example.com.meimeng.constants.CommonConstants;
import com.example.com.meimeng.gson.GsonTools;
import com.example.com.meimeng.gson.gsonbean.DeleteInfoBean;
import com.example.com.meimeng.net.InternetConstant;
import com.example.com.meimeng.net.InternetUtils;
import com.example.com.meimeng.util.BadgeUtil;
import com.example.com.meimeng.util.ChatSQliteDataUtil;
import com.example.com.meimeng.util.MessageUtils;
import com.example.com.meimeng.util.NewMessageState;
import com.example.com.meimeng.util.Utils;

import org.json.JSONStringer;

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

    private NewMessageState nms;

    private Handler handler;
    private ChatSQliteDataUtil sq;
    public ChatMessageAdapter(Context context,Handler handler, List<ChatMessageBean> dataList) {

        this.context = context;
        this.dataList = dataList;
        this.handler = handler;
        sq = new ChatSQliteDataUtil(context);
        nms = NewMessageState.getInstance(context);
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

        int size = dataList.size();
        if (size == 0) {
            handler.sendEmptyMessage(1000);
        }
        for (int i = 0; i < size; i++) {
            ChatMessageBean bean = dataList.get(i);
            if (bean.getContent() == null || bean.getContent().equals("")) {
                Log.e(TAG, "2--------------------聊天消息是空的----------------" + i);
                dataList.remove(i);
            }

            if (i == size-1) {
                handler.sendEmptyMessage(1000);
            }
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ChatMessageBean bean = dataList.get(position);
        View view = LayoutInflater.from(context).inflate(R.layout.item_of_chat, null);


        final ImageView headerImageView = (ImageView) view.findViewById(R.id.item_of_chat_header);

        final TextView message_red_listview = (TextView) view.findViewById(R.id.message_red_listview);

        TextView nameText = (TextView) view.findViewById(R.id.item_of_chat_name);

        LinearLayout thirdLayout = (LinearLayout) view.findViewById(R.id.chat_slide_third_layout);
        TextView timeText = (TextView) view.findViewById(R.id.item_of_chat_time);

        TextView messageText = (TextView) view.findViewById(R.id.item_of_chat_message);
        ImageView messagekaitong = (ImageView) view.findViewById(R.id.weikaitong);


        if ((Boolean)(bean.getAttributes().get("isOpen"))==false) {
            messagekaitong.setVisibility(View.VISIBLE);
        }else{
            messagekaitong.setVisibility(View.GONE);
        }
        timeText.setText(bean.getTime());
        messageText.setText(bean.getContent());
        int state = nms.getState(bean.getUserId() + "");
        if (messagekaitong.getVisibility()==View.VISIBLE&&state==2) {
            state=1;
        }

        if (state != 0 && state != -1) {
            message_red_listview.setVisibility(View.VISIBLE);
            message_red_listview.setText(state + "");
        } else if (message_red_listview.getVisibility() == View.VISIBLE) {
            message_red_listview.setVisibility(View.GONE);
        }
        nameText.setText(bean.getName());
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
//                            Toast.makeText(context, "SlideListAdapter: 下载图片出错，" + throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MeiMengApplication.sharedPreferences.getInt(CommonConstants.USER_VERFIY, 0) == 0) {
                    Utils.JudgeUserVerfiy(context, 0);
                } else if (MeiMengApplication.sharedPreferences.getInt(CommonConstants.USER_VERFIY, 0) == 2) {
                    Utils.JudgeUserVerfiy(context, 2);
                } else {
                    MessageUtils.setLeanCloudSelfUid();
                    MessageUtils.setLeanCloudOtherUid(context, bean.getUserId());
                    goneMessageRed_tabbar();
                    message_red_listview.setVisibility(View.GONE);
                    nms.setState(bean.getUserId() + "", 0);
                }
            }
        });

        thirdLayout.setOnClickListener(new View.OnClickListener() {//删除本地数据库聊天数据
            @Override
            public void onClick(View v) {
                deleteFromService(bean,String.valueOf(bean.getUserId()), bean.getConversationId());
            }
        });
        return view;
    }

    private void deleteFromService(final ChatMessageBean bean , final String s,String conversationId) {

        try {
            //获取用户的uid,和token
            if (TextUtils.isEmpty(Utils.getUserId())) {
                return;
            }
            if (TextUtils.isEmpty(Utils.getUserToken())) {
                return;
            }

            String url = InternetConstant.SERVER_URL + InternetConstant.DELETERFROMSERVER + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();
            JSONStringer jsonStringer = new JSONStringer().object()
                    .key("uid").value(Long.parseLong(Utils.getUserId()))
                    .key("conversation").value(conversationId)
                    .endObject();
            String jsonStr = jsonStringer.toString();
            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            DeleteInfoBean deleteInfoBean= GsonTools.getDeleteBean(s);
                            if (deleteInfoBean.isSuccess()==true) {
                                //Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                            }else{
                                if (deleteInfoBean.getError().equals("NO-PARAMTER")) {
                                    //Toast.makeText(context, deleteInfoBean.getError(), Toast.LENGTH_SHORT).show();
                                }
                            }
                            sq.deleteUserById(String.valueOf(bean.getUserId()));//通过Id删除本地数据库
                            upDatebs();
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable error) {
                           //Log.e("test:", error.getMessage());
                            sq.deleteUserById(String.valueOf(bean.getUserId()));
                            upDatebs();
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //刷新
    private void upDatebs() {
        HomeActivity.dataList.clear();
        HomeActivity.dataList.addAll(sq.getDataAll());
        dataList=HomeActivity.dataList;
        notifyDataSetChanged();
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

    public void goneMessageRed_tabbar() {
        int flag = 0;
        for (ChatMessageBean bean : dataList) {
            if (nms.getState(bean.getUserId() + "") != 0&&nms.getState(bean.getUserId() + "") !=-1) {
                flag++;
            }
        }

        if (flag == 1 || flag == 0) {
            nms.setState("new message", 0);
            MeiMengApplication.messageRed.setVisibility(View.GONE);
            BadgeUtil.resetBadgeCount(context);
        }

    }


}
