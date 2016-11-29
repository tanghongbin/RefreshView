package com.xqd.chatmessage.activity;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avoscloud.leanchatlib.controller.ChatManager;
import com.avoscloud.leanchatlib.controller.MessageHelper;


import com.xqd.chatmessage.db.DbOpenHelper;
import com.xqd.chatmessage.gson.ChatMessageBean;
import com.xqd.chatmessage.util.BadgeUtil;
import com.xqd.chatmessage.ChatAplication;
import com.xqd.chatmessage.R;
import com.xqd.chatmessage.fragment.AllFragmentManagement;
import com.xqd.chatmessage.fragment.Friendsfragment;
import com.xqd.chatmessage.fragment.MeFragment;
import com.xqd.chatmessage.fragment.MessageFragment;
import com.xqd.chatmessage.util.ActivityCollector;
import com.xqd.chatmessage.util.ChatSQliteDataUtil;
import com.xqd.chatmessage.util.CommonConstants;
import com.xqd.chatmessage.util.MessageUtils;
import com.xqd.chatmessage.util.NewMessageState;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2015/9/9.
 */
public class HomeActivity extends BaseActivity implements View.OnClickListener{

    //tabbar中的控件

    private RelativeLayout FriendsLinearLayout;
    private RelativeLayout MessageLinearLayout;
    private RelativeLayout MeLinearLayout;

    private TextView friendstext;
    private TextView messagetext;
    private TextView metext;

    private ImageView friendsimage;
    private ImageView messageimage;
    private ImageView meimage;

    //五大页面的Fragment
    private MessageFragment messageFragment;
    private MeFragment meFragment;
    private Friendsfragment friendsFragment;
    //判断是否需要add  Fragment
    private boolean addFriendsOk = false;
    private boolean addMessageOK = false;
    private boolean addMeOK = false;

    //记录返回键是否按过一次
    private boolean isPressed = false;

    public static final int NEWMESSAGE = 10;

    public static ArrayList<ChatMessageBean> dataList = new ArrayList<>();

    private ChatSQliteDataUtil sq = null;

    @Bind(R.id.title_bar_text)
    TextView titleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        new DbOpenHelper(this).setDatabase();
        sq = new ChatSQliteDataUtil();
        FriendsLinearLayout = (RelativeLayout) findViewById(R.id.friends);
        MessageLinearLayout = (RelativeLayout) findViewById(R.id.message);
        MeLinearLayout = (RelativeLayout) findViewById(R.id.me);

        friendstext = (TextView) findViewById(R.id.friends_text);
        messagetext = (TextView) findViewById(R.id.message_text);
        metext = (TextView) findViewById(R.id.me_text);

        friendsimage = (ImageView) findViewById(R.id.friends_ico);
        messageimage = (ImageView) findViewById(R.id.message_ico);
        meimage = (ImageView) findViewById(R.id.me_ico);
        ChatAplication.messageRed = (ImageView) findViewById(R.id.message_ico_red);

        //添加点击事件
        FriendsLinearLayout.setOnClickListener(this);
        MessageLinearLayout.setOnClickListener(this);
        MeLinearLayout.setOnClickListener(this);

        settabbar(messageimage, R.drawable.bottom_ico_message_pressed, messagetext);
        messageFragment = new MessageFragment();
        if (messageFragment != null) {
            addMessageOK = true;
        }
        AllFragmentManagement.ChangeFragment(this, R.id.fragment, messageFragment, addMessageOK);
        MessageUtils.setLeanCloudSelfUid();
        getMessage();
        onMessageReceipt();
    }

    /**
    * Message回调
    *
    */

    private String uid;

    public void getMessage(){
        final NewMessageState nms = NewMessageState.getInstance(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                ChatManager.getMessage(new ChatManager.onMessageCallBack() {
                    @Override
                    public void onMessage(AVIMTypedMessage message, AVIMConversation conversation, AVIMClient client) {
                       if (client.getClientId().equals("hn"+ChatAplication.sharedPreferences.getLong(CommonConstants.USER_ID, -1) )) {
                            uid = message.getFrom();

                            dataList = getgetMessage(message,uid);
                            mhandler.sendEmptyMessage(NEWMESSAGE);

                            int mNotificationId = 10086;
                            if (ChatAplication.isSound == 1) {
                                ChatAplication.messageIntentType = 1;
                                ChatAplication.messageCont++;

                                BadgeUtil.setBadgeCount(getApplicationContext(), ChatAplication.messageCont);
                                NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(HomeActivity.this);
                                mBuilder.setContentTitle("您有新的私信，请注意查收")//设置通知栏标题
                                        .setContentText(getContent(message))
                                        .setContentIntent(getDefalutIntent(Notification.FLAG_AUTO_CANCEL)) //设置通知栏点击意图
//                                  .setNumber(2) //设置通知集合的数量
                                        .setTicker(getContent(message)) //通知首次出现在通知栏，带上升动画效果的
                                        .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                                        .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
                                        .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                                        .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                                        .setDefaults(Notification.DEFAULT_SOUND)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                                        .setSmallIcon(R.drawable.ico_launcher);//设置通知小ICON
                                mNotificationManager.notify(mNotificationId, mBuilder.build());
                            }
                        }
                    }
                });
            }
        }).start();
    }

    private String Uid;

    public void onMessageReceipt(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                ChatManager.getMessageReceipt(new ChatManager.onMessageReceiptCallBack() {
                    @Override
                    public void onMessageReceipt(AVIMTypedMessage message, AVIMConversation conv,AVIMClient client) {

                        Uid = ChatAplication.Uid.getUid();

                        dataList = getgetMessage(message,Uid);
                        if(MessageFragment.chatMessageAdapter != null){
                            MessageFragment.chatMessageAdapter.setDataList(dataList);
                            MessageFragment.chatMessageAdapter.notifyDataSetChanged();
                        }

                    }
                });
            }
        }).start();
    }


    public CharSequence getContent(AVIMTypedMessage message) {
        CharSequence content = MessageHelper.outlineOfMsg(message);
        if (content.toString().matches("^((:[A-Za-z0-9_]+:)*)$")) {
            content = "[表情]";
        }
        return content;
    }

    public PendingIntent getDefalutIntent(int flags) {
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, new Intent(getApplication(), HomeActivity.class), flags);
        return pendingIntent;
    }

    public ArrayList<ChatMessageBean> getgetMessage(AVIMTypedMessage message,String uid) {

        Long time = message.getTimestamp();
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");


        String content_str = message.getContent();
        String timestr = format.format(date);
        CharSequence content = MessageHelper.outlineOfMsg(message);

        sq.upData("userid",uid,"time",timestr);
        sq.upData("userid",uid,"content",  content_str);

        ArrayList<ChatMessageBean> list = new ArrayList<ChatMessageBean>();
        for (int i = 0; i < sq.getDataAll().size(); i++) {
            ChatMessageBean bean = sq.getDataAll().get(i);
            if (uid.equals(bean.getUserId() + "")) {
                bean.setContent(content);
                bean.setTime(timestr);
                list.add(bean);
            }

        }
        for (int i = 0; i < sq.getDataAll().size(); i++) {
            ChatMessageBean bean = sq.getDataAll().get(i);
            if ((!uid.equals(bean.getUserId() + "")) && bean.getContent() != null && !bean.getContent().equals("")) {
                list.add(bean);
            }

        }
        return list;

    }


    private Handler mhandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            NewMessageState nms = NewMessageState.getInstance(HomeActivity.this);
            if(msg.what == NEWMESSAGE){

                if(ChatAplication.isSound == 1){
                    setMessageState(uid);
                    setMessageState_tabbar();

                }
                if(MessageFragment.chatMessageAdapter != null){
                    MessageFragment.chatMessageAdapter.setDataList(dataList);
                    MessageFragment.chatMessageAdapter.notifyDataSetChanged();
                }
            }
        }
    };


        public void setMessageState(String uid) {
            NewMessageState nms = NewMessageState.getInstance(HomeActivity.this);
            int content = nms.getState(uid);
            if(content == -1){
                content = 0;
            }
            content++;
            nms.setState(uid, content);
            nms.setState("new message", 1);
        }

        public void setMessageState_tabbar() {
            NewMessageState nms = NewMessageState.getInstance(HomeActivity.this);
//            str.append("+").append(nms.getState("new message"));

            if (nms.getState("new message") == 1) {
                ChatAplication.messageRed.setVisibility(View.VISIBLE);
            }
        }




    /**
     * 监听点击事件
     *
     * @param v
     */
    public void onClick(View v) {
        ChangeFragementImage(v);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void ChangeFragementImage(View v) {
        switch (v.getId()) {

            case R.id.friends:

                titleText.setText("好友");
                settabbar(friendsimage, R.drawable.bottom_ico_friends_pressed, friendstext);
                if (friendsFragment == null) {
                    friendsFragment = new Friendsfragment();
                    addFriendsOk = true;
                } else {
                    addFriendsOk = false;
                }
                AllFragmentManagement.ChangeFragment(this, R.id.fragment, friendsFragment, addFriendsOk);
                break;
            case R.id.message:

                titleText.setText("会话");
                settabbar(messageimage, R.drawable.bottom_ico_message_pressed, messagetext);
                if (messageFragment == null) {
                    messageFragment = new MessageFragment();
                    addMessageOK = true;
                } else {
                    addMessageOK = false;
                }
                AllFragmentManagement.ChangeFragment(this, R.id.fragment, messageFragment, addMessageOK);
                break;
            case R.id.me:

                titleText.setText("我");
                settabbar(meimage, R.drawable.bottom_ico_me_pressed, metext);
                if (meFragment == null) {
                    meFragment = new MeFragment();
                    addMeOK = true;
                } else {
                    addMeOK = false;
                }
                AllFragmentManagement.ChangeFragment(this, R.id.fragment, meFragment, addMeOK);

            default:
                break;
        }

    }

    private void settabbar(ImageView imageView, int drawableid, TextView textview) {
        inittabbar();
        imageView.setImageResource(drawableid);
        textview.setTextColor(this.getResources().getColor(R.color.bar_pressed));
    }

    /**
     * 初始化底部导航栏
     */
    private void inittabbar() {

        //字的颜色恢复到默认状态
        friendstext.setTextColor(this.getResources().getColor(R.color.bar_normal));
        messagetext.setTextColor(this.getResources().getColor(R.color.bar_normal));
        metext.setTextColor(this.getResources().getColor(R.color.bar_normal));

        //图标恢复到默认的样式
        friendsimage.setImageResource(R.drawable.bottom_ico_friends_normal);
        messageimage.setImageResource(R.drawable.bottom_ico_message_normal);
        meimage.setImageResource(R.drawable.bottom_ico_me_normal);

    }

    /**
     * 搜索id的事件
     */
    @OnClick(R.id.search_image_view)
    void searchListener() {
        Intent intent=new Intent(HomeActivity.this,SearchActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (isPressed) {
            ActivityCollector.finishAll();
            super.onBackPressed();
        } else {
            Toast.makeText(HomeActivity.this, "再按一次，退出", Toast.LENGTH_LONG).show();
            isPressed = true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setMessageState_tabbar();
        ChatAplication.isSound = 1;
    }
}
