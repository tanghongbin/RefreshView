package com.example.com.meimeng.activity;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.NotificationCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avoscloud.leanchatlib.controller.ChatManager;
import com.avoscloud.leanchatlib.controller.MessageHelper;
import com.example.com.meimeng.MeiMengApplication;
import com.example.com.meimeng.R;
import com.example.com.meimeng.bean.ChatMessageBean;
import com.example.com.meimeng.bean.ProgressBean;
import com.example.com.meimeng.bean.RegisterFirstInfoBean;
import com.example.com.meimeng.constants.CommonConstants;
import com.example.com.meimeng.custom.ThreeWheelView;
import com.example.com.meimeng.custom.WheelView;
import com.example.com.meimeng.dialog.SelectPictureDialog;
import com.example.com.meimeng.fragment.ChatMessageFragment;
import com.example.com.meimeng.fragment.EventFragment;
import com.example.com.meimeng.fragment.ExploreFragment_f;
import com.example.com.meimeng.fragment.HomeFragment;
import com.example.com.meimeng.fragment.MeFragment;
import com.example.com.meimeng.fragment.SearchFragment;
import com.example.com.meimeng.fragment.SelectPictureFragment;
import com.example.com.meimeng.fragment.management.AllFragmentManagement;
import com.example.com.meimeng.fragment.management.FriendsFragment;
import com.example.com.meimeng.gson.GsonTools;
import com.example.com.meimeng.gson.gsonbean.ActivityOriginateBean;
import com.example.com.meimeng.gson.gsonbean.BaseBean;
import com.example.com.meimeng.gson.gsonbean.FirstInfoEditBean;
import com.example.com.meimeng.gson.gsonbean.MatchMakerBean;
import com.example.com.meimeng.gson.gsonbean.UnReadServiceMsgNumBean;
import com.example.com.meimeng.gson.gsonbean.UploadPictureBean;
import com.example.com.meimeng.gson.gsonbean.UserPartInfoBean;
import com.example.com.meimeng.gson.gsonbean.UserPartInfoItem;
import com.example.com.meimeng.net.InternetConstant;
import com.example.com.meimeng.net.InternetUtils;
import com.example.com.meimeng.util.ActivityCollector;
import com.example.com.meimeng.util.BadgeUtil;
import com.example.com.meimeng.util.ChatSQliteDataUtil;
import com.example.com.meimeng.util.DialogUtils;
import com.example.com.meimeng.util.MessageUtils;
import com.example.com.meimeng.util.NewMessageState;
import com.example.com.meimeng.util.Utils;
import com.flurry.android.FlurryAgent;

import org.json.JSONStringer;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * 登陆后的第一个Activity
 */

public class HomeActivity extends BaseActivity implements View.OnClickListener, MeFragment.OnMeListener,
        SelectPictureFragment.OnSelectPictureDialogListener, SearchFragment.OnSearchFragmentListener {
    private final static String TAG = "HomeActivity";
    private static final int SERVERNUM = 111;

    private PopupWindow pwMyPopWindow;// popupwindow
    private int NUM_OF_VISIBLE_LIST_ROWS = 3;// 指定popupwindow中条数
    private LinearLayout matchmakerlayout;
    private LinearLayout eventlayout;
    private LinearLayout twoLovelayout;
    private long picId;
    private String type;

    private ImageView MoreMenuImageView;//右上角的图标

    //tabbar中的控件
    private RelativeLayout HomeLinearLayout;

    private RelativeLayout MessageLinearLayout;

    private RelativeLayout MeLinearLayout;

    private RelativeLayout FriendsLinearLayout;

    private TextView hometext;
    private TextView exploretext;
    private TextView eventtext;
    private TextView messagetext;
    private TextView metext;
    private TextView friendstext;

    private ImageView homeimage;

    private ImageView messageimage;
    private ImageView meimage;
    private ImageView friendsimage;

    //五大页面的Fragment
    private HomeFragment homeFragment;
    //private ExploreFragment exploreFragment;
    //替用
    private ExploreFragment_f exploreFragment;
    private EventFragment eventFragment;
    private ChatMessageFragment messageFragment;
    private MeFragment meFragment;
    private FriendsFragment friendsFragment;

    //Fragment页面标签
    private final int isExplor = 1;
    private final int isEvent = 2;
    private final int isMessage = 3;
    private final int isFriends = 4;
    private final int isMe = 5;

    //判断是否需要add  Fragment
    private boolean addHomeOk = false;
    private boolean addExploreOk = false;
    private boolean addEventOk = false;
    private boolean addMessageOK = false;
    private boolean reMessageRE = true;
    private boolean addMeOK = false;
    private boolean addFriendsOK = false;

    public static String strs = "1234";
    public static final int NEWMESSAGE = 10;
    public static final int HNMESSAGE = 20;

//    @Bind(R.id.title_bar_text)
//    TextView titleText;

    @Bind(R.id.main_dialog_layout)
    LinearLayout mainDialogLayout;

    @Bind(R.id.more_menu_layout)
    RelativeLayout MoreMenuLayout;

    @Bind(R.id.more_search_layout)
    LinearLayout moreSearchLayout;

    @Bind(R.id.title_layout)
    RelativeLayout titleLayout;

    private SelectPictureFragment selectPictureFragment;
    private Dialog dialog;

    //0----代表是第一次編輯資料時的圖片，1-----代表是個人中心的圖片選擇
    private int firstOrSelfType = 0;

    //相册中图片的序列
    private int sort;

    //记录返回键是否按过一次
    private boolean isPressed = false;

    //搜索fragment
    private SearchFragment searchFragment;

    public static ArrayList<ChatMessageBean> dataList = new ArrayList<>();
    private NewMessageState nms;
    private String uid;

    //进度条的文本
    private TextView progressTextView;


    private int fgstr;
    private SharedPreferences sp;
    private boolean isFirstMessage;
    private boolean isFirstMyself;
    private int flag;
    private int mFlag;

    private DisplayMetrics dm;
    private Dialog aldialog_a;
    private Dialog aldialog_b;
    private Dialog dialog_c;

    private RelativeLayout select_rl;


    private final int ANIMATIONOVER = 11;

    //相机剪切图片的请求码
    private final int CAMEAR_CROP_PICTURE = 31;

    //选中的图片
    private ImageView pictureImageView;

    //更改图片的imageview
    private ImageView changePictureImageView;

    //认证图片
    private ImageView identifyImageView;

    //添加认证图片的imageview
    private ImageView addIdentifyImageView;

    //图片的类型（头像/认证图片）
    private int pictureType = 0;

    //用户的生日
    private TextView user_birthday;

    //用户的身高
    private TextView userHeight;

    //dialog的标题
    private TextView titleText;
    private ImageView guideImage;


    //上传图片时，当前的imageview
    private ImageView flagImageView;


    @Override
    public void setUploadProgress(String pro) {
        if (progressTextView != null) {
            progressTextView.setText(pro);
            Log.e(TAG, "上传文件进度条正确");
        } else {
            Log.e(TAG, "上传文件进度条出错");
        }

    }

    //用户第一次编辑资料的信息
    private RegisterFirstInfoBean registerFirstInfoBean = new RegisterFirstInfoBean();

    private ChatSQliteDataUtil sq = null;

    //用于将上传图片的进度传给主线程
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.e("++++++++++", "------------" + msg.obj);

            //显示上传进度
            setUploadProgress(msg.obj + "");
        }
    };

    //选择图片的dialog
    private SelectPictureDialog selectPictureDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.mainlayout);
        ButterKnife.bind(this);
        MeiMengApplication.currentContext = this;
        nms = NewMessageState.getInstance(this);
        BadgeUtil.resetBadgeCount(getApplicationContext());
        sq = new ChatSQliteDataUtil(this);
        dm = Utils.getScreenMetrics(HomeActivity.this);
        //初始化视图界面
        initView();
        setGuide();//设置新手引导
        //初始化右上角更多菜单
        initPopupWindow();
        //初始化tabbar
        settabbar(homeimage, R.drawable.ico_index_pressed, hometext);
        exploretext.setTextColor(this.getResources().getColor(R.color.bar_pressed));

        //服务消息跳转处理
        serviceMessageProcessing();
        //聊天消息处理
        getMessage();
        //系统消息数
        getServiceUnreadMsgNum();
        onMessageReceipt();

        MessageUtils.setLeanCloudSelfUid();

        Utils.getUerVerfiy();//获取认证信息
    }

    private void setGuide() {

        sp = MeiMengApplication.sharedPreferences;
        isFirstMessage = sp.getBoolean("geuide_message", false);
        isFirstMyself = sp.getBoolean("geuide_myself", false);
    }

    /**
     * 初始化视图界面
     */
    private void initView() {
        MoreMenuImageView = (ImageView) findViewById(R.id.image_more);
        HomeLinearLayout = (RelativeLayout) findViewById(R.id.home);

        FriendsLinearLayout = (RelativeLayout) findViewById(R.id.friends);

        guideImage = (ImageView) findViewById(R.id.guide_xinshou);
        MessageLinearLayout = (RelativeLayout) findViewById(R.id.message);

        MeLinearLayout = (RelativeLayout) findViewById(R.id.me);
        hometext = (TextView) findViewById(R.id.home_text);

        exploretext = (TextView) findViewById(R.id.explore_text);
        eventtext = (TextView) findViewById(R.id.event_text);

        messagetext = (TextView) findViewById(R.id.message_text);
        metext = (TextView) findViewById(R.id.me_text);

        friendstext = (TextView) findViewById(R.id.friends_text);

        homeimage = (ImageView) findViewById(R.id.home_ico);

        messageimage = (ImageView) findViewById(R.id.message_ico);

        meimage = (ImageView) findViewById(R.id.me_ico);

        friendsimage = (ImageView) findViewById(R.id.friends_ico);
        MeiMengApplication.messageRed = (ImageView) findViewById(R.id.message_ico_red);
        //添加点击事件
        HomeLinearLayout.setOnClickListener(this);
//        ExploreLinearLayout.setOnClickListener(this);
//        EventLinearLayout.setOnClickListener(this);
        MessageLinearLayout.setOnClickListener(this);
        MeLinearLayout.setOnClickListener(this);
        MoreMenuLayout.setOnClickListener(this);
        FriendsLinearLayout.setOnClickListener(this);
        initTitleBar("isHome", -1, R.drawable.ico_search_b, this);

    }

    private static Handler eHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == SERVERNUM) {
                MeiMengApplication.messageRed.setVisibility(View.VISIBLE);
            }
        }
    };

    /**
     * 服务消息跳转处理
     */
    private void serviceMessageProcessing() {
        Intent intent = getIntent();
        int initType = intent.getIntExtra("init_type", -1);

        if (initType == 1) {
//            homeFragment = new HomeFragment();
//            if (homeFragment != null) {
//                addHomeOk = true;
//                AllFragmentManagement.ChangeFragment(1, this, R.id.fragment, homeFragment, addHomeOk);
//            }
            fgstr = isExplor;
            if (exploreFragment == null) {
                //exploreFragment = new ExploreFragment();
                exploreFragment = new ExploreFragment_f();
                addExploreOk = true;
            }
            AllFragmentManagement.ChangeFragment(1, this, R.id.fragment, exploreFragment, addExploreOk);

        } else {

            initTitleBar("消息", -1, -1, this);
            if (messageFragment == null) {
                messageFragment = new ChatMessageFragment();
                addMessageOK = true;
            } else {
                addMessageOK = false;
            }
            settabbar(messageimage, R.drawable.ico_message_pressed, messagetext);
            AllFragmentManagement.ChangeFragment(1, this, R.id.fragment, messageFragment, addMessageOK);
//            MeiMengApplication.messageRed.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 发送消息的回调
     */

    public void onMessageReceipt() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ChatManager.getMessageReceipt(new ChatManager.onMessageReceiptCallBack() {
                    @Override
                    public void onMessageReceipt(AVIMTypedMessage message, AVIMConversation conv, AVIMClient client) {


                    }
                });
            }
        }).start();
    }


    /**
     * 获取系统未读的消息
     *
     * @return
     */
    public static void getServiceUnreadMsgNum() {
        try {

            //获取用户的uid,和token
            if (TextUtils.isEmpty(Utils.getUserId())) {
                return;
            }
            if (TextUtils.isEmpty(Utils.getUserToken())) {
                return;
            }

            String url = InternetConstant.SERVER_URL + InternetConstant.UNREAD_SERVER_MESSAGE_NUM + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();
            String jsonStr = "{}";
            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            UnReadServiceMsgNumBean unReadServiceMsgNumBean = GsonTools.getUnReadServiceMsgNumBean(s);
                            if (unReadServiceMsgNumBean.isSuccess()) {
                                if (unReadServiceMsgNumBean.getParam().getUnread() != null) {
                                    //系统未读消息数目
                                    Integer num = unReadServiceMsgNumBean.getParam().getUnread();
                                    if (num != null) {
                                        eHandler.sendEmptyMessage(SERVERNUM);
                                    }
                                }
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable error) {
                            Log.e("test:", error.getMessage());
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 接收消息的回调
     */

    public void getMessage() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                ChatManager.getMessage(new ChatManager.onMessageCallBack() {
                    @Override
                    public void onMessage(AVIMTypedMessage message, AVIMConversation conversation, AVIMClient client) {

                        if (client.getClientId().equals(MeiMengApplication.sharedPreferences.getLong(CommonConstants.USER_ID, -1) + "")) { //防错乱，接收的对象ID与当时的用户的ID做匹配

                            uid = message.getFrom();//发送者的ID
                            if (uid.substring(0, 2).equals("hn")) { //发送者为红娘
                                mhandler.sendEmptyMessage(HNMESSAGE);
                            } else {//发送者为会员
                                getgetMessage(message, conversation);
                                dataList.clear();
                                dataList.addAll(sq.getDataAll());
                                mhandler.sendEmptyMessage(NEWMESSAGE);
                            }

                            int mNotificationId = 10086;

                            if (MeiMengApplication.isSound == 1) {
                                MeiMengApplication.messageIntentType = 1;
                                MeiMengApplication.messageCont++;
                                BadgeUtil.setBadgeCount(getApplicationContext(), MeiMengApplication.messageCont);
                                NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(HomeActivity.this);
                                CharSequence content = getContent(MessageHelper.outlineOfMsg(message));
                                mBuilder.setContentTitle("您有新的私信，请注意查收")//设置通知栏标题
                                        .setContentText(getContent(content))
                                        .setContentIntent(getDefalutIntent(Notification.FLAG_AUTO_CANCEL)) //设置通知栏点击意图
//                                  .setNumber(2) //设置通知集合的数量
                                        .setTicker(getContent(content)) //通知首次出现在通知栏，带上升动画效果的
                                        .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                                        .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
                                        .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                                        .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                                        .setDefaults(MeiMengApplication.sharedPreferences.getInt(CommonConstants.MSGPROMPT, Notification.DEFAULT_ALL))//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                                        .setSmallIcon(R.drawable.ico_launcher);//设置通知小ICON
                                mNotificationManager.notify(mNotificationId, mBuilder.build());
                            }

                        }
                    }

                });

            }
        }).start();

    }

    public void getgetMessage(AVIMTypedMessage message, AVIMConversation conversation) {

        Long time = message.getTimestamp(); //接收到消息时的时间（毫秒值）
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");

        String timestr = format.format(date);
        String conversationId = conversation.getConversationId();
        CharSequence content = MessageHelper.outlineOfMsg(message);//接收消息的内容
        boolean isOpen = true;
        if (conversation.getAttribute("isOpen") != null) {
            isOpen = (boolean) conversation.getAttribute("isOpen");
        }

        if (sq.isExist(Long.parseLong(uid))) {//本地数据库中如果存在就更新本地数据的内容，如果不存在就添加到本地数据库
            upData(uid, timestr, content, isOpen, conversationId);
        } else {
            getNewUserMessage(uid, content, timestr, isOpen, conversationId);
        }

    }

    private void upData(String uid, String time, CharSequence content, boolean isOpen, String conversationId) {
        sq.upData("userid", uid, "time", time);
        sq.upData("userid", uid, "conversationId", conversationId);
        if (isOpen == true) {
            sq.upData("userid", uid, "isOpen", 1);
        } else {
            sq.upData("userid", uid, "isOpen", 0);
        }
        sq.upData("userid", uid, "content", getContent(content).toString());
        Log.d("wz", "isOpen=" + isOpen);
        Log.d("wz", "content=" + getContent(content).toString());
    }

    private void addData(ChatMessageBean bean) {
        sq.addData(bean);
    }

    /**
     * 获取新用户的信息
     *
     * @param targetUid
     * @return
     */
    private ChatMessageBean getNewUserMessage(final String targetUid, final CharSequence content, final String time, final boolean isOpen, final String conversationId) {


        final ChatMessageBean chatMessageBean = new ChatMessageBean();
        try {

            if (TextUtils.isEmpty(Utils.getUserId())) {
                return null;
            }
            if (TextUtils.isEmpty(Utils.getUserToken())) {
                return null;
            }

            Log.e("whh","Utils.getUserId()" + Utils.getUserId());
            String url = InternetConstant.SERVER_URL + InternetConstant.USER_GETPARTINFO + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();
            JSONStringer stringer = new JSONStringer().object().key("targetUid").value(targetUid).endObject();
            String jsonStr = stringer.toString();
            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1() {

                        @Override
                        public void call(Object o) {
                            Log.i("json", o.toString());
                            UserPartInfoBean mUserPartInfoBean = GsonTools.getUserPartInfoParam((String) o);
                            if (mUserPartInfoBean.isSuccess()) {

                                UserPartInfoItem ubi = mUserPartInfoBean.getParam().getUserPartInfoItem();

                                chatMessageBean.setHeadPic(mUserPartInfoBean.getParam().getUserPartInfoItem().getHeadPic());

                                StringBuffer sb = new StringBuffer();
                                if (!TextUtils.isEmpty(ubi.getFirstName())) {
                                    sb.append(ubi.getFirstName());
                                }

                                String name = "";
                                switch (ubi.getSex()) {
                                    case 0://男
                                        name = ubi.getFirstName() + "先生";
                                        break;
                                    case 1://女
                                        name = ubi.getFirstName() + "女士";
                                        break;
                                    default:
                                        break;

                                }
                                chatMessageBean.setName(name);

                                chatMessageBean.setUserId(Long.valueOf(targetUid));
                                Log.e("whh","targetUid" + targetUid);

                                chatMessageBean.setContent(getContent(content).toString());

                                chatMessageBean.setTime(time);
                                chatMessageBean.setConversationId(conversationId);
                                Map attrs = new HashMap();
                                attrs.put("isOpen", isOpen);
                                chatMessageBean.setAttributes(attrs);
                                addData(chatMessageBean);

                                dataList.clear();
                                dataList.addAll(sq.getDataAll());

                                mhandler.sendEmptyMessage(NEWMESSAGE);

                            } else

                            {
                                Log.e("TAG", "初始化出错了");

                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            Log.e("获取用户基本信息失败了", throwable.getMessage());
                        }
                    });
        } catch (
                Exception e
                )

        {
            e.printStackTrace();
        }

        return chatMessageBean;
    }

    public CharSequence getContent(CharSequence content) {

        if (content.toString().matches("^((:[A-Za-z0-9_]+:)*)$")) {
            content = "[表情]";
        }
        return content;
    }

    /**
     * 聊天消息Handler
     */
    public Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            if (msg.what == NEWMESSAGE) {
                if (MeiMengApplication.isSound == 1) {
                    setMessageState(uid);
                    setMessageState_tabbar();

                    if (ChatMessageFragment.chatMessageAdapter != null) {
                        ChatMessageFragment.chatMessageAdapter.setDataList(sq.getDataAll());
                        ChatMessageFragment.chatMessageAdapter.notifyDataSetChanged();

                    }
                }

            }
            if (msg.what == HNMESSAGE) {
                if (MeiMengApplication.isSound == 1) {
                    int state = nms.getState(uid);
                    if (state == -1) {
                        state = 0;
                    }
                    state++;
                    if (MeiMengApplication.messageRedMatcher != null) {
                        MeiMengApplication.messageRedMatcher.setVisibility(View.VISIBLE);
                        MeiMengApplication.messageRedMatcher.setText(state + "");
                    }
                    MeiMengApplication.messageRed.setVisibility(View.VISIBLE);
                    nms.setState(uid, state);

                    nms.setState(CommonConstants.NEWMESSAGE, 1);
                    setMessageState_tabbar();
                }
            }
        }
    };

    /**
     * 记录新消息
     *
     * @param uid
     */
    public void setMessageState(String uid) {
        int content = nms.getState(uid);
        nms.setState(uid, content + 1);
        nms.setState(CommonConstants.NEWMESSAGE, 1);
    }

    /**
     * 新消息来时，亮起小红点
     */
    public void setMessageState_tabbar() {
        if (nms.getState(CommonConstants.NEWMESSAGE) == 1) {
            MeiMengApplication.messageRed.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置通知栏点击意图
     *
     * @param flags
     * @return
     */
    public PendingIntent getDefalutIntent(int flags) {
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, new Intent(getApplication(), HomeActivity.class), flags);
        return pendingIntent;
    }

    /**
     * 初始化右上角点击弹窗
     */
    private void initPopupWindow() {
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.popmenuview, null);
        matchmakerlayout = (LinearLayout) layout.findViewById(R.id.linearlayout_matchmaker);
        eventlayout = (LinearLayout) layout.findViewById(R.id.linearlayout_event);
        twoLovelayout = (LinearLayout) layout.findViewById(R.id.linearlayout_twoLove);
        pwMyPopWindow = new PopupWindow(layout);
        pwMyPopWindow.setFocusable(true);// 加上这个popupwindow中才可以接收点击事件

        //添加点击事件
        matchmakerlayout.setOnClickListener(this);
        eventlayout.setOnClickListener(this);
        twoLovelayout.setOnClickListener(this);
        // 控制popupwindow的宽度和高度自适应
        matchmakerlayout.measure(View.MeasureSpec.UNSPECIFIED,
                View.MeasureSpec.UNSPECIFIED);
        pwMyPopWindow.setWidth(matchmakerlayout.getMeasuredWidth());
        pwMyPopWindow.setHeight((matchmakerlayout.getMeasuredHeight())
                * NUM_OF_VISIBLE_LIST_ROWS);
        // 控制popupwindow点击屏幕其他地方消失
        pwMyPopWindow.setBackgroundDrawable(this.getResources().getDrawable(
                R.drawable.top_bg_popup));// 设置背景图片，不能在布局中设置，要通过代码来设置
        pwMyPopWindow.setOutsideTouchable(true);// 触摸popupwindow外部，popupwindow消失。这个要求你的popupwindow要有背景图片才可以成功，如上

        //测量右上角图标的大小
        MoreMenuImageView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
    }

    /**
     * 监听点击事件
     * 邓成博修改部分
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
//        switch (MeiMengApplication.sharedPreferences .getInt(CommonConstants.INFO_STATE, 0) ){
//            case 0:
////                if(!aldialog_a.isShowing()) {
////                    setAlertDialog_a();
//
//                UnlimitedOnClick(v);//无审核限制按钮
////                }
//                break;
//            default:
//                switch (MeiMengApplication.sharedPreferences.getInt(CommonConstants.USER_VERFIY,0)){
//                    case 0:
//                        Utils.setPromptDialog(this, "用户资料待审核中...");
//
//                        break;
//                    case 1:
//                        ChangeFragementImage(v);
//                        break;
//                    case 2:
//                        Utils.setPromptDialog(this,"用户资料未通过审核，请修改资料！");
//                        MeFragmentOnclick(v);//我的页面入口
//                        break;
//                }
//                break;
//        }


        UnlimitedOnClick(v);//无审核限制按钮

//        ChangeFragementImage(v);
    }

    /**
     * 邓成博修改部分
     *
     * @param v
     */
    private void MeFragmentOnclick(View v) {
        switch (v.getId()) {
            case R.id.home://首页
                initTitleBar("isHome", -1, R.drawable.ico_search_b, this);
                settabbar(homeimage, R.drawable.ico_index_pressed, hometext);
                setRightRes(R.drawable.ico_search_b);

                exploretext.setTextColor(this.getResources().getColor(R.color.bar_pressed));
                fgstr = isExplor;

                if (exploreFragment == null) {
                    //exploreFragment = new ExploreFragment();
                    exploreFragment = new ExploreFragment_f();
                    addExploreOk = true;
                } else {
                    addExploreOk = false;
                }
                AllFragmentManagement.ChangeFragment(1, this, R.id.fragment, exploreFragment, addExploreOk);
                break;
            case R.id.me://个人中心
//                cancelSearchFragment();
                //设置标题
                initTitleBar("个人中心", -1, -1, this);
                settabbar(meimage, R.drawable.ico_my_pressed, metext);

                fgstr = isMe;
                if (meFragment == null) {
                    meFragment = new MeFragment();
                    addMeOK = true;
                } else {
                    addMeOK = false;
                }
                AllFragmentManagement.ChangeFragment(1, this, R.id.fragment, meFragment, addMeOK);
                break;
            default:
                Utils.setPromptDialog(this, "用户资料未通过审核，请修改资料！");
                break;
        }

    }

    /**
     * 邓成博修改部分
     *
     * @param v
     */
    private void UnlimitedOnClick(View v) {
        switch (v.getId()) {

            case R.id.home://首页
                initTitleBar("isHome", -1, R.drawable.ico_search_b, this);
                settabbar(homeimage, R.drawable.ico_index_pressed, hometext);

            case R.id.explore://发现
//                cancelSearch();
                //设置标题
//                titleText.setText("发现");
//                moreSearchLayout.setVisibility(View.VISIBLE);
                setRightRes(R.drawable.ico_search_b);
                settabbar(homeimage, R.drawable.ico_index_pressed, hometext);
                exploretext.setTextColor(this.getResources().getColor(R.color.bar_pressed));
                fgstr = isExplor;

                if (exploreFragment == null) {
                    //exploreFragment = new ExploreFragment();
                    exploreFragment = new ExploreFragment_f();
                    addExploreOk = true;
                } else {
                    addExploreOk = false;
                }
                AllFragmentManagement.ChangeFragment(1, this, R.id.fragment, exploreFragment, addExploreOk);
                break;
            case R.id.event://活动
//                cancelSearchFragment();
                //设置标题
//                titleText.setText("活动");
                setRightRes(R.drawable.icon_my_activities);
                settabbar(homeimage, R.drawable.ico_index_pressed, hometext);
                eventtext.setTextColor(this.getResources().getColor(R.color.bar_pressed));
                fgstr = isEvent;

                if (eventFragment == null) {
                    eventFragment = new EventFragment();
                    addEventOk = true;
                } else {
                    addEventOk = false;
                }
                AllFragmentManagement.ChangeFragment(1, this, R.id.fragment, eventFragment, addEventOk);
                break;
            case R.id.message://消息
                //               cancelSearchFragment();
                //设置标题
                setMessageGuide();
//                titleText.setText("消息");
                initTitleBar("消息", -1, -1, this);
                settabbar(messageimage, R.drawable.ico_message_pressed, messagetext);

                fgstr = isMessage;
                if (messageFragment == null) {
                    messageFragment = new ChatMessageFragment();
                    addMessageOK = true;
                } else {
                    addMessageOK = false;
                }
                AllFragmentManagement.ChangeFragment(1, this, R.id.fragment, messageFragment, addMessageOK);
                break;
            case R.id.friends://好友
                Log.i("msg", "friends");
                initTitleBar("我的好友", -1, -1, this);
                settabbar(friendsimage, R.drawable.ico_friends_pressed, friendstext);
                fgstr = isFriends;

                if (friendsFragment == null) {
                    friendsFragment = new FriendsFragment();
                    addFriendsOK = true;
                } else {
                    addFriendsOK = false;
                }
                AllFragmentManagement.ChangeFragment(1, this, R.id.fragment, friendsFragment, addFriendsOK);
                break;
            case R.id.me://个人中心
//                cancelSearchFragment();
                //设置标题
                setMyselfGuide();
                initTitleBar("个人中心", -1, -1, this);
                settabbar(meimage, R.drawable.ico_my_pressed, metext);

                fgstr = isMe;
                if (meFragment == null) {
                    meFragment = new MeFragment();
                    addMeOK = true;
                } else {
                    addMeOK = false;
                }
                AllFragmentManagement.ChangeFragment(1, this, R.id.fragment, meFragment, addMeOK);
                break;
            /*case R.id.last_step_btn://上一步
                aldialog_b.dismiss();
                setAlertDialog_a();
                break;*/

            //右上角按钮的功能
            case R.id.more_menu_layout:
                if (MeiMengApplication.sharedPreferences.getBoolean(CommonConstants.MYSTATE, false) == false) {
                    setAlertDialog_a();//资料未填写完成，提示完善资料
                } else {//资料填写完成
                    switch (MeiMengApplication.sharedPreferences.getInt(CommonConstants.USER_VERFIY, 0)) {
                        case 0:
                            Utils.setPromptDialog(this, "您提交的资料正在审核状态。如有疑问，请联系美盟客服：400-8783-520");
                            break;
                        case 1:
                            ChangeFragementImage(v);
                            break;
                        case 2:

//                                MeFragmentOnclick(v);//我的页面入口
                            break;
                    }
                }
                break;
            default:
                break;
        }
        Utils.getUerVerfiy();
    }

    private void setMessageGuide() {

        if (isFirstMessage == false) {
            mFlag = 0;
            guideImage.setVisibility(View.VISIBLE);
            Utils.readBitMap(HomeActivity.this, guideImage, R.raw.guide_chathn);
            guideImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (mFlag) {
                        case 0:
                            Utils.readBitMap(HomeActivity.this, guideImage, R.raw.guide_server);
                            mFlag = 1;
                            break;
                        case 1:
                            Utils.readBitMap(HomeActivity.this, guideImage, R.raw.guide_friend);
                            mFlag = 2;
                            break;
                        case 2:
                            guideImage.setVisibility(View.GONE);
                            isFirstMessage = true;
                            sp.edit().putBoolean("geuide_message", true).commit();
                            break;
                        default:
                            break;
                    }
                }
            });
            guideImage.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (mFlag) {
                        case 0:
                        case 1:
                            if (!(event.getX() > (13 * dm.widthPixels / 36)
                                    && event.getX() < (23 * dm.widthPixels / 36)
                                    && event.getY() > (96 * dm.heightPixels / 128)
                                    && event.getY() < (105 * dm.heightPixels / 128))) {
                                return true;
                            }
                            break;
                        case 2:
                            if (!(event.getX() > (13 * dm.widthPixels / 36)
                                    && event.getX() < (23 * dm.widthPixels / 36)
                                    && event.getY() > (108 * dm.heightPixels / 128)
                                    && event.getY() < (117 * dm.heightPixels / 128))) {
                                return true;
                            }
                            break;
                        default:
                            break;
                    }

                    return false;
                }
            });
        }


    }

    private void setMyselfGuide() {
        if (isFirstMyself == false) {
            flag = 0;
            guideImage.setVisibility(View.VISIBLE);
            Utils.readBitMap(HomeActivity.this, guideImage, R.raw.guide_headpic);
            guideImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (flag) {
                        case 0:
                            Utils.readBitMap(HomeActivity.this, guideImage, R.raw.guide_myself);
                            flag = 1;
                            break;
                        case 1:
                            Utils.readBitMap(HomeActivity.this,guideImage, R.raw.guide_vip);
                            flag = 2;
                            break;
                        case 2:
                            guideImage.setVisibility(View.GONE);
                            isFirstMyself = true;
                            sp.edit().putBoolean("geuide_myself", true).commit();
                            break;
                        default:
                            break;
                    }

                }
            });
            guideImage.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // TODO Auto-generated method stub

                    switch (flag) {
                        case 0:
                            if (!(event.getX() > (13 * dm.widthPixels / 36)
                                    && event.getX() < (23 * dm.widthPixels / 36)
                                    && event.getY() > (92 * dm.heightPixels / 128)
                                    && event.getY() < (102 * dm.heightPixels / 128))) {
                                return true;
                            }
                            break;
                        case 1:
                            if (!(event.getX() > (13 * dm.widthPixels / 36)
                                    && event.getX() < (23 * dm.widthPixels / 36)
                                    && event.getY() > (87 * dm.heightPixels / 128)
                                    && event.getY() < (94 * dm.heightPixels / 128))) {
                                return true;
                            }
                            break;
                        case 2:
                            if (!(event.getX() > (13 * dm.widthPixels / 36)
                                    && event.getX() < (23 * dm.widthPixels / 36)
                                    && event.getY() > (102 * dm.heightPixels / 128)
                                    && event.getY() < (109 * dm.heightPixels / 128))) {
                                return true;
                            }
                            break;
                        default:
                            break;
                    }
                    return false;
                }
            });
        }

    }

    /**
     * 邓成博修改部分
     *
     * @param v
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void ChangeFragementImage(View v) {
        switch (v.getId()) {
            case R.id.home://首页
//                cancelSearchFragment();
                //设置标题
//                titleText.setText("首页");
//                settabbar(homeimage, R.drawable.ico_index_pressed, hometext);
//                if (homeFragment == null) {
//                    homeFragment = new HomeFragment();
//                    addHomeOk = true;
//                } else {
//                    addHomeOk = false;
//                }
//
//                AllFragmentManagement.ChangeFragment(1, this, R.id.fragment, homeFragment, addHomeOk);
//                cancelSearch();

                initTitleBar("isHome", -1, R.drawable.ico_search_b, this);
                settabbar(homeimage, R.drawable.ico_index_pressed, hometext);
            case R.id.explore://发现
//                cancelSearch();
                //设置标题
//                titleText.setText("发现");
//                moreSearchLayout.setVisibility(View.VISIBLE);
                setRightRes(R.drawable.ico_search_b);
                settabbar(homeimage, R.drawable.ico_index_pressed, hometext);
                exploretext.setTextColor(this.getResources().getColor(R.color.bar_pressed));
                fgstr = isExplor;

                if (exploreFragment == null) {
                    //exploreFragment = new ExploreFragment();
                    exploreFragment = new ExploreFragment_f();
                    addExploreOk = true;
                } else {
                    addExploreOk = false;
                }
                AllFragmentManagement.ChangeFragment(1, this, R.id.fragment, exploreFragment, addExploreOk);
                break;
            case R.id.event://活动
//                cancelSearchFragment();
                //设置标题
//                titleText.setText("活动");
                setRightRes(R.drawable.icon_my_activities);
                settabbar(homeimage, R.drawable.ico_index_pressed, hometext);
                eventtext.setTextColor(this.getResources().getColor(R.color.bar_pressed));
                fgstr = isEvent;

                if (eventFragment == null) {
                    eventFragment = new EventFragment();
                    addEventOk = true;
                } else {
                    addEventOk = false;
                }
                AllFragmentManagement.ChangeFragment(1, this, R.id.fragment, eventFragment, addEventOk);
                break;
            case R.id.message://消息
                //               cancelSearchFragment();
                //设置标题
//                titleText.setText("消息");
                initTitleBar("消息", -1, -1, this);
                settabbar(messageimage, R.drawable.ico_message_pressed, messagetext);

                fgstr = isMessage;
                if (messageFragment == null) {
                    messageFragment = new ChatMessageFragment();
                    addMessageOK = true;
                } else {
                   /* if (MeiMengApplication.sharedPreferences.getInt(CommonConstants.USER_VERFIY,2)==1) {
                        messageFragment = new MessageFragment();
                        AllFragmentManagement.ChangeFragment(1, this, R.id.fragment, messageFragment, addMessageOK,reMessageRE);
                        return;
                    }*/
                    addMessageOK = false;
                }
                AllFragmentManagement.ChangeFragment(1, this, R.id.fragment, messageFragment, addMessageOK);
                break;
            case R.id.friends://好友
                Log.i("msg", "friends");
                initTitleBar("我的好友", -1, -1, this);
                settabbar(friendsimage, R.drawable.ico_friends_pressed, friendstext);
                fgstr = isFriends;

                if (friendsFragment == null) {
                    friendsFragment = new FriendsFragment();
                    addFriendsOK = true;
                } else {
                    addFriendsOK = false;
                }
                AllFragmentManagement.ChangeFragment(1, this, R.id.fragment, friendsFragment, addFriendsOK);
                break;
            case R.id.me://个人中心
//                cancelSearchFragment();
                //设置标题

                initTitleBar("个人中心", -1, -1, this);
                settabbar(meimage, R.drawable.ico_my_pressed, metext);

                fgstr = isMe;
                if (meFragment == null) {
                    meFragment = new MeFragment();
                    addMeOK = true;
                } else {
                    addMeOK = false;
                }
                AllFragmentManagement.ChangeFragment(1, this, R.id.fragment, meFragment, addMeOK);
                break;

            case R.id.more_menu_layout://右上角快捷入口
                setRightTitle();

                break;
            case R.id.more_search_layout:
                setAlertDialog_a();
                break;
            case R.id.linearlayout_matchmaker://私人红娘
                cancelSearchFragment();
                if (MeiMengApplication.sharedPreferences.getInt(CommonConstants.USER_VERFIY, 0) == 0) {
                    Utils.JudgeUserVerfiy(HomeActivity.this, 0);
                } else if (MeiMengApplication.sharedPreferences.getInt(CommonConstants.USER_VERFIY, 0) == 2) {
                    Utils.JudgeUserVerfiy(HomeActivity.this, 2);
                } else {
                    if (MeiMengApplication.sharedPreferences.getInt(CommonConstants.USER_LEVEL, 0) < 2) {
                        Utils.setHomeDialog(HomeActivity.this);
                    } else {
                        try {
                            if (TextUtils.isEmpty(Utils.getUserId())) {
                                return;
                            }
                            if (TextUtils.isEmpty(Utils.getUserToken())) {
                                return;
                            }
                            String url = InternetConstant.SERVER_URL + InternetConstant.MATCHMAKER_CONNECT + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();
                            JSONStringer jsonStringer = new JSONStringer().object().endObject();
                            String jsonStr = jsonStringer.toString();
                            //得到Observable并获取返回的数据(主线程中)
                            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
                            observable.observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Action1<String>() {
                                        @Override
                                        public void call(String s) {
                                            MatchMakerBean matchmakerJson = GsonTools.getMatchMakerJson(s);
                                            if (matchmakerJson.isSuccess()) {
                                                if (matchmakerJson.getParam().getMatchmaker() == null) {
                                                    Utils.connectMatchMakerDialog2(HomeActivity.this);
                                                } else {
                                                    Utils.connectMatchMakerDialog(HomeActivity.this, matchmakerJson.getParam().getMatchmaker());
                                                }

                                            } else {
                                                DialogUtils.setDialog(HomeActivity.this, matchmakerJson.getError());
                                            }
                                        }
                                    }, new Action1<Throwable>() {
                                        @Override
                                        public void call(Throwable error) {
                                            Log.e("test:", error.getMessage());
                                        }
                                    });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
                break;
            case R.id.linearlayout_event://私人邀约
                if (MeiMengApplication.sharedPreferences.getInt(CommonConstants.USER_VERFIY, 0) == 0) {
                    Utils.JudgeUserVerfiy(HomeActivity.this, 0);
                } else if (MeiMengApplication.sharedPreferences.getInt(CommonConstants.USER_VERFIY, 0) == 2) {
                    Utils.JudgeUserVerfiy(HomeActivity.this, 2);
                } else {
                    if (MeiMengApplication.sharedPreferences.getInt(CommonConstants.USER_LEVEL, 0) > 1) {
                        try {
                            if (TextUtils.isEmpty(Utils.getUserId())) {
                                return;
                            }
                            if (TextUtils.isEmpty(Utils.getUserToken())) {
                                return;
                            }
                            String url = InternetConstant.SERVER_URL + InternetConstant.ACTIVITY_ORIGINATE + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();
                            JSONStringer jsonStringer = new JSONStringer().object().endObject();
                            String jsonStr = jsonStringer.toString();
                            //得到Observable并获取返回的数据(主线程中)
                            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
                            observable.observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Action1<String>() {
                                        @Override
                                        public void call(String s) {
                                            ActivityOriginateBean activityOriginateJson = GsonTools.getactivityoriginateJson(s);
                                            if (activityOriginateJson.isSuccess()) {
                                                Utils.getInitiateEventChance(HomeActivity.this, activityOriginateJson.getParam().getActivityChance());
                                            } else {
                                                DialogUtils.setDialog(HomeActivity.this, activityOriginateJson.getError());
                                            }
                                        }
                                    }, new Action1<Throwable>() {
                                        @Override
                                        public void call(Throwable error) {
                                            Log.e("test:", error.getMessage());
                                        }
                                    });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        pwMyPopWindow.dismiss();
                    } else {
                        Utils.setInitiateEventDialog(HomeActivity.this);
                    }
                }
                break;
            case R.id.linearlayout_twoLove://两情相悦
                Intent intent = new Intent();
                intent.putExtra("type", 1);
                intent.setClass(HomeActivity.this, FriendsManageActivity.class);
                startActivity(intent);
                pwMyPopWindow.dismiss();
                break;

            default:
                break;
        }

    }

   /* *//**
     * 设置Dialog_a
     * 邓成博修改部分
     *//*
    public void setAlertDialog_a() {

        //首次編輯資料
        firstOrSelfType = 0;

        View view = LayoutInflater.from(this).inflate(R.layout.layout_information_write, null);

        aldialog_a = new Dialog(this, R.style.loading_dialog);
        setDatatoView_a(view);
        aldialog_a.setContentView(view);
        aldialog_a.show();
        aldialog_a.setCancelable(false);

        //相片的点击事件
        RelativeLayout pictureLayout = (RelativeLayout) view.findViewById(R.id.layout_information_write_picture_layout);
        pictureLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //头像
                setPictureDialog(SelectPictureDialog.SQUARE);

                pictureType = 0;

            }
        });

    }*/

    /**
     * 设置Dialog_a
     * 美盟3认证
     */
    public void setAlertDialog_a() {

        //首次編輯資料
        firstOrSelfType = 0;

        View view = LayoutInflater.from(this).inflate(R.layout.identify_dialog_layout, null);
        TextView cancle = (TextView) view.findViewById(R.id.simple_dialog_cancel_text);
        TextView identify = (TextView) view.findViewById(R.id.simple_dialog_sure_text);
        aldialog_a = new Dialog(this, R.style.loading_dialog);
        aldialog_a.setCancelable(false);
        aldialog_a.setContentView(view);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aldialog_a.dismiss();
            }
        });
        identify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(HomeActivity.this, AuthIdentifyActivity.class);
                startActivity(intent);
                aldialog_a.dismiss();
            }
        });
        aldialog_a.show();
    }

    /**
     * 选择图片的dialog
     */
    private void setPictureDialog() {
        if (selectPictureDialog == null) {
            selectPictureDialog = new SelectPictureDialog(HomeActivity.this, R.style.loading_dialog);
        }
        selectPictureDialog.show();
        selectPictureDialog.setCancelable(false);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = selectPictureDialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()); //设置宽度
        selectPictureDialog.getWindow().setAttributes(lp);
    }

    /**
     * 设置Dialog_b
     * 邓成博修改部分
     */
    private void setAlertDialog_b() {

        View view = LayoutInflater.from(this).inflate(R.layout.layout_information_write_b, null);
        aldialog_b = new Dialog(this, R.style.loading_dialog);
        setDatatoView_b(view);
        aldialog_b.setContentView(view);
        aldialog_b.show();
        aldialog_b.setCancelable(false);

    }

    /**
     * 设置Dialog_c
     * 邓成博修改部分
     */
    private void setDialog_c(int type) {

        View view = LayoutInflater.from(this).inflate(R.layout.birthday_register_layout, null);
        dialog_c = new Dialog(this, R.style.loading_dialog);
        setDatatoView_c(view, type);
        dialog_c.setContentView(view);
        dialog_c.show();
        dialog_c.setCancelable(false);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog_c.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()); //设置宽度
        dialog_c.getWindow().setAttributes(lp);
    }


    /**
     * 设置Dialog_a的view
     *
     * @param view
     */
    private void setDatatoView_a(View view) {

        /*changePictureImageView = (ImageView) view.findViewById(R.id.picture_change_image_view);
        pictureImageView = (ImageView) view.findViewById(R.id.picture_image_view);*/
        user_birthday = (TextView) view.findViewById(R.id.user_birthday);
        userHeight = (TextView) view.findViewById(R.id.user_height);
        RelativeLayout personHeight = (RelativeLayout) view.findViewById(R.id.person_height_layout);
        EditText firstEdit = (EditText) view.findViewById(R.id.first_name_edit);
        EditText lastEdit = (EditText) view.findViewById(R.id.last_name_edit);

        //初始化
        if (registerFirstInfoBean != null) {
            if (registerFirstInfoBean.getHeadPic() != null) {
                Long headerPic = registerFirstInfoBean.getHeadPic();
                InternetUtils.getPicIntoView(100, 100, pictureImageView, headerPic);
                changePictureImageView.setVisibility(View.GONE);
                pictureImageView.setVisibility(View.VISIBLE);
            }

            if (!TextUtils.isEmpty(registerFirstInfoBean.getFirstName())) {
                firstEdit.setText(registerFirstInfoBean.getFirstName());
            }

            if (!TextUtils.isEmpty(registerFirstInfoBean.getLastName())) {
                lastEdit.setText(registerFirstInfoBean.getLastName());
            }

            if (!TextUtils.isEmpty(registerFirstInfoBean.getBirthday())) {
                user_birthday.setText(registerFirstInfoBean.getBirthday());
            }

            if (registerFirstInfoBean.getHeight() != null) {
                userHeight.setText(registerFirstInfoBean.getHeight() + "CM");
            }

        }


        //选择生日
        user_birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDialog_c(0);
            }
        });

        //选择身高
        personHeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDialog_c(1);
            }
        });


        firstEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String firstName = String.valueOf(s);
                registerFirstInfoBean.setFirstName(firstName);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        lastEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String lastName = String.valueOf(s);
                registerFirstInfoBean.setLastName(lastName);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    /**
     * 设置Dialog_b的view
     *
     * @param view
     */
    private void setDatatoView_b(View view) {
       /* RelativeLayout undo_btn = (RelativeLayout) view.findViewById(R.id.undo_2_btn);
        RelativeLayout last_step_btn = (RelativeLayout) view.findViewById(R.id.last_step_btn);
        undo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aldialog_b.dismiss();
            }
        });
        last_step_btn.setOnClickListener(this);*/

        identifyImageView = (ImageView) view.findViewById(R.id.identify_upload_picture_image_view);
        addIdentifyImageView = (ImageView) view.findViewById(R.id.identify_upload_image_view);

        //初始化
        if (registerFirstInfoBean != null) {
            if (registerFirstInfoBean.getIdentifyPicIdList() != null) {
                if (registerFirstInfoBean.getIdentifyPicIdList().get(0) != null) {
                    Long headerPic = registerFirstInfoBean.getIdentifyPicIdList().get(0);
                    InternetUtils.getPicIntoView(100, 100, identifyImageView, headerPic);
                    identifyImageView.setVisibility(View.VISIBLE);
                    addIdentifyImageView.setVisibility(View.GONE);
                }
            }
        }
        RelativeLayout identifyLayout = (RelativeLayout) view.findViewById(R.id.linearlayout_first);
        identifyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPictureDialog();

                pictureType = 1;
            }
        });

        TextView submitText = (TextView) view.findViewById(R.id.submit_first_info_text);
        submitText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    boolean b = checkFirstInfo(registerFirstInfoBean);
                    if (!b) {
                        return;
                    }
                    Long uidKey = MeiMengApplication.sharedPreferences.getLong(CommonConstants.USER_ID, 1l);
                    String token = MeiMengApplication.sharedPreferences.getString(CommonConstants.USER_TOKEN, "");
                    String url = InternetConstant.SERVER_URL + InternetConstant.FIRST_INFO_EDIT + "?uid=" + uidKey + "&token=" + token;
                    JSONStringer jsonStringer = new JSONStringer().object().key("headPic").value(registerFirstInfoBean.getHeadPic() + "")
                            .key("firstName").value(registerFirstInfoBean.getFirstName())
                            .key("lastName").value(registerFirstInfoBean.getLastName())
                            .key("birthday").value(registerFirstInfoBean.getBirthday() + " 00:00:00")
                            .key("height").value(registerFirstInfoBean.getHeight() + "")
                            .key("marStatus").value(1)
                            .key("identityPicId").array().value(registerFirstInfoBean.getIdentifyPicIdList().get(0) + "").endArray()
                            .endObject();

                    Observable observable = InternetUtils.getJsonObservale(url, jsonStringer.toString());
                    observable.observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Action1<String>() {
                                @Override
                                public void call(String s) {

                                    FirstInfoEditBean firstInfoEditBean = GsonTools.getFirstInfoEditBean(s);
                                    if (firstInfoEditBean.isSuccess()) {
                                        Toast.makeText(HomeActivity.this, "提交成功", Toast.LENGTH_LONG).show();
                                        aldialog_b.dismiss();

                                        SharedPreferences.Editor editor = MeiMengApplication.sharedPreferences.edit();

                                        //2:用户资料审核中
                                        editor.putInt(CommonConstants.INFO_STATE, 2);
                                        editor.commit();
                                    }
                                }
                            }, new Action1<Throwable>() {
                                @Override
                                public void call(Throwable error) {
                                    Log.e("test:", error.getMessage());
                                }
                            });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 检查信息是否输入完整
     *
     * @param registerFirstInfoBean
     * @return
     */
    private boolean checkFirstInfo(RegisterFirstInfoBean registerFirstInfoBean) {
        String errorMessage = "error";
        if (registerFirstInfoBean.getHeadPic() == null) {
            errorMessage = "请上传头像";
        } else if (TextUtils.isEmpty(registerFirstInfoBean.getFirstName())) {
            errorMessage = "请输入姓";
        } else if (TextUtils.isEmpty(registerFirstInfoBean.getLastName())) {
            errorMessage = "请输入名";
        } else if (registerFirstInfoBean.getHeight() == null) {
            errorMessage = "请输入身高";
        } else if (TextUtils.isEmpty(registerFirstInfoBean.getBirthday())) {
            errorMessage = "请输入生日";
        } else if (registerFirstInfoBean.getIdentifyPicIdList() == null) {
            errorMessage = "请上传认证图片";
        } else {
            return true;
        }
        Toast.makeText(HomeActivity.this, errorMessage, Toast.LENGTH_LONG).show();

        return false;
    }

    /**
     * 设置身高数据
     *
     * @param personHeightWheelView
     */
    private void setHeightData(WheelView personHeightWheelView) {
        ArrayList<String> heightList = new ArrayList<>();
        for (int i = 150; i < 221; i++) {
            heightList.add(String.valueOf(i) + "CM");
        }
        personHeightWheelView.setData(heightList);
        personHeightWheelView.setDefault(0);
    }

    private void setDatatoView_c(View view, final int type) {
        titleText = (TextView) view.findViewById(R.id.birthday_register_title_text);
        TextView cancel = (TextView) view.findViewById(R.id.birthday_register_cancel_text);
        TextView sure = (TextView) view.findViewById(R.id.birthday_register_sure_text);
        final ThreeWheelView birthdayView = (ThreeWheelView) view.findViewById(R.id.birthday_register_wheel_picker);
        final WheelView personHeightWheelView = (WheelView) view.findViewById(R.id.person_height_wheel_view);

        //初始化身高数据
        setHeightData(personHeightWheelView);

        //生日
        if (type == 0) {
            personHeightWheelView.setVisibility(View.GONE);
            birthdayView.setVisibility(View.VISIBLE);
            titleText.setText("生日");
        }
        //身高
        else {
            personHeightWheelView.setDefault(20);
            personHeightWheelView.setVisibility(View.VISIBLE);
            birthdayView.setVisibility(View.GONE);
            titleText.setText("身高");
        }

        select_rl = (RelativeLayout) view.findViewById(R.id.select_layout);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_c.dismiss();
            }
        });
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //生日
                if (type == 0) {
                    String birthday = birthdayView.getData();
                    user_birthday.setText(birthday);
                    registerFirstInfoBean.setBirthday(birthday);

                }
                //身高
                else {
                    String height = personHeightWheelView.getSelectedText();
                    userHeight.setText(height);
                    int length = height.length();
                    String heS = height.substring(0, length - 2);
                    registerFirstInfoBean.setHeight(Integer.valueOf(heS));

                }
                dialog_c.dismiss();
            }
        });

        //显示年月日
        showSelectDialog(3);


    }

    /**
     * 显示选择器
     * 邓成博修改部分
     */
    private void showSelectDialog(int num) {

        if (!(select_rl.getVisibility() == View.VISIBLE)) {
            select_rl.setVisibility(View.VISIBLE);
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_arrow_up);
            select_rl.setAnimation(animation);
        }

    }

    /**
     * 提示Dialog
     * 邓成博修改部分
     */


    /**
     * 邓成博修改部分
     */
    private void setRightTitle() {
        switch (fgstr) {
            case 1:
                Intent intent_1 = new Intent(HomeActivity.this, ExploreScreen.class);
                startActivity(intent_1);
                break;
            case 2:
                Intent intent_2 = new Intent(HomeActivity.this, MyEventActivity.class);
                startActivity(intent_2);
                break;
            default:
                break;
        }
    }


    /**
     * 初始化tabbar
     */
    private void settabbar(ImageView imageView, int drawableid, TextView textview) {
        inittabbar();
        if (imageView != null) {
            imageView.setImageResource(drawableid);
        }
        textview.setTextColor(this.getResources().getColor(R.color.bar_pressed));
    }

    /**
     * 初始化底部导航栏
     */
    private void inittabbar() {
        //字的颜色恢复到默认状态
        hometext.setTextColor(this.getResources().getColor(R.color.text_dark));
        metext.setTextColor(this.getResources().getColor(R.color.text_dark));
        messagetext.setTextColor(this.getResources().getColor(R.color.text_dark));
        friendstext.setTextColor(this.getResources().getColor(R.color.text_dark));

        exploretext.setTextColor(this.getResources().getColor(R.color.text_dark));
        eventtext.setTextColor(this.getResources().getColor(R.color.text_dark));


        //图标恢复到默认的样式
        homeimage.setImageResource(R.drawable.ico_index_normal);
//        exploreimage.setImageResource(R.drawable.bottom_ico_explore_normal);
//        eventimage.setImageResource(R.drawable.bottom_ico_event_normal);
        messageimage.setImageResource(R.drawable.ico_message_normal);
        meimage.setImageResource(R.drawable.ico_my_normal);
        friendsimage.setImageResource(R.drawable.ico_friends_normal);

    }

    /**
     * 取消搜索id功能
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void cancelSearch() {
        titleLayout.setVisibility(View.VISIBLE);

        if (searchFragment == null) {
            return;
        }
        if (exploreFragment == null) {
            // exploreFragment = new ExploreFragment();
            exploreFragment = new ExploreFragment_f();
        }
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.remove(searchFragment);
        transaction.commit();
    }

    /**
     * 退出搜索
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void cancelSearchFragment() {
        titleLayout.setVisibility(View.VISIBLE);
        moreSearchLayout.setVisibility(View.GONE);

        if (searchFragment == null) {
            return;
        }

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.remove(searchFragment);
        transaction.commit();
    }


    /**
     * 上传照片
     */

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void uploadPicture(ImageView imageView, int sort) {

        //個人中心
        firstOrSelfType = 1;

        this.sort = sort;

        flagImageView = imageView;

        setPictureDialog();
    }

    /**
     * 用来上传头像用的
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void uploadHeadpicture(ImageView imageView) {

        //当前要设置的ImageView

        if (selectPictureFragment == null) {
            selectPictureFragment = new SelectPictureFragment();
        }

        mainDialogLayout.setVisibility(View.VISIBLE);
        Bundle bundle = new Bundle();
        bundle.putString("type", "headPic");

        selectPictureFragment.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.bottom_in, 0);
        transaction.add(R.id.dialog_layout, selectPictureFragment);
        transaction.commit();
    }

    /**
     * 取消dialog
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void cancelDialog() {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.remove(selectPictureFragment);
        transaction.commit();

        mainDialogLayout.setVisibility(View.GONE);
    }


    /**
     * 开始上传图片的dialog
     *
     * @param path
     */
    @Override
    public void getPicturePath(String path) {
        //展示进度dialog

//        Bitmap bitmap = getBitmapFromPath(path);
//        flagImageView.setImageBitmap(bitmap);

        ProgressBean bean = DialogUtils.createLoadingDialog2(this, "正在上传...");
        progressTextView = bean.getTextView();
        dialog = bean.getDialog();
        dialog.show();

    }

    @Override
    public void sendResultJson(String type, long pid) {
        this.type = type;
        this.picId = pid;

    }

    /**
     * 取消dialog2
     */
    @Override
    public void cancelDialog2() {
        if (null != dialog)
            dialog.dismiss();
    }

    /**
     * 上传图片
     */
    @Override
    public void requestifok() {
        if (type.equals("userPic")) {
            try {
                if (TextUtils.isEmpty(Utils.getUserId())) {
                    return;
                }
                if (TextUtils.isEmpty(Utils.getUserToken())) {
                    return;
                }
                String url = InternetConstant.SERVER_URL + InternetConstant.USERPHOTO_ADD + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();
                JSONStringer jsonStringer = new JSONStringer().object()
                        .key("picId").value(picId)
                        .endObject();
                String jsonStr = jsonStringer.toString();
                //得到Observable并获取返回的数据(主线程中)
                Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
                observable.observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<String>() {
                            @Override
                            public void call(String s) {
                                if (null != dialog)
                                    dialog.dismiss();
                                BaseBean basebeanJson = GsonTools.getBaseReqBean(s);
                                if (basebeanJson.isSuccess()) {

                                    Toast.makeText(HomeActivity.this, "照片上传成功", Toast.LENGTH_SHORT).show();
                                } else {
                                    DialogUtils.setDialog(HomeActivity.this, basebeanJson.getError());
                                }
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable error) {
                                Log.e("test:", error.getMessage());
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //上传头像
        if (type.equals("headPic")) {
            try {
                if (TextUtils.isEmpty(Utils.getUserId())) {
                    return;
                }
                if (TextUtils.isEmpty(Utils.getUserToken())) {
                    return;
                }
                String url = InternetConstant.SERVER_URL + InternetConstant.USER_EDIT + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();
                JSONStringer jsonStringer = new JSONStringer().object()
                        .key("headPic").value(picId)
                        .endObject();
                String jsonStr = jsonStringer.toString();
                //得到Observable并获取返回的数据(主线程中)
                Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
                observable.observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<String>() {
                            @Override
                            public void call(String s) {
                                if (null != dialog)
                                    dialog.dismiss();
                                BaseBean basebeanJson = GsonTools.getBaseReqBean(s);
                                if (basebeanJson.isSuccess()) {
                                    Toast.makeText(HomeActivity.this, "上传头像成功", Toast.LENGTH_SHORT).show();
                                    Intent mIntent = new Intent();
                                    mIntent.setAction("action:MeimengRefreshMe");
                                    sendBroadcast(mIntent);
                                } else
                                    DialogUtils.setDialog(HomeActivity.this, basebeanJson.getError());
                                Intent mIntent = new Intent();
                                mIntent.setAction("action:MeimengRefreshMe");
                                sendBroadcast(mIntent);
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable error) {
                                Log.e("test:", error.getMessage());
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    @OnClick(R.id.dialog_button)
    void dialogButtonListener() {
        cancelDialog();
    }

    StringBuilder str = new StringBuilder();

    @Override
    protected void onResume() {
        Log.e(TAG, "onResume");
        super.onResume();
        setMessageState_tabbar();
        MeiMengApplication.isSound = 1;
    }

    @Override
    protected void onStart() {
        Log.e(TAG, "onStart");
        super.onStart();
        //激活Flurry
        FlurryAgent.setUserId(String.valueOf(MeiMengApplication.sharedPreferences.getLong(CommonConstants.USER_ID, 0)));
        FlurryAgent.onStartSession(this, "3Z353FG5ZGGXGJGVR5NH");
        Intent intent = getIntent();
        String condition = intent.getStringExtra("str_condition");
        if (condition != null && condition.equals("是否提示完善认证信息")) {
            // MeiMengApplication.registerFirstInfoBean=null;
            Intent intent2 = new Intent(HomeActivity.this, AuthIdentifyActivity.class);
            startActivity(intent2);
            intent.putExtra("str_condition", "");
        }
    }

    @Override
    protected void onRestart() {
        Log.e(TAG, "onRestart");
        super.onRestart();
    }

    @Override
    protected void onPause() {
        Log.e(TAG, "onPause");
        homeFragment = null;
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.e(TAG, "onStop");
        super.onStop();
        FlurryAgent.onEndSession(this);

    }

    @Override
    protected void onDestroy() {
        Log.e(TAG, "onDestroy");
        dataList.clear();
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
    }

    @Override
    public void onBackPressed() {
        if (isPressed) {
            ActivityCollector.finishAll();
            super.onBackPressed();
        } else {
            Toast.makeText(HomeActivity.this, "再按一次，退出美盟", Toast.LENGTH_LONG).show();
            isPressed = true;
        }
    }

    /**
     * 搜索id的事件
     */
    @OnClick(R.id.more_search_layout)
    void searchListener() {
        if (searchFragment == null) {
            searchFragment = new SearchFragment();
        }
        titleLayout.setVisibility(View.GONE);
        AllFragmentManagement.ChangeFragment(1, this, R.id.fragment, searchFragment, true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                //相册
                case SelectPictureDialog.CHOOSE_BIG_PICTURE:
                    String path = getPath(HomeActivity.this, SelectPictureDialog.imageUri);
                    Bitmap bitmap = getBitmapFromPath(path);
                    if (firstOrSelfType == 1) {
                        flagImageView.setImageBitmap(bitmap);
                        LinearLayout auditView = getAduitView(flagImageView);
                        auditView.setVisibility(View.VISIBLE);
                    } else {
                        if (pictureType == 0) {
                            pictureImageView.setImageBitmap(bitmap);
                            pictureImageView.setVisibility(View.VISIBLE);
                            changePictureImageView.setVisibility(View.GONE);
                        } else {
                            identifyImageView.setImageBitmap(bitmap);
                            identifyImageView.setVisibility(View.VISIBLE);
                            addIdentifyImageView.setVisibility(View.GONE);
                        }
                    }


                    uploadPictureListener(path);
                    break;
                //相机
                case SelectPictureDialog.SELECT_CAMERA_CODE:
                    cropImageUri(SelectPictureDialog.cameraUri, 1300, 1300);
                    break;
                //剪切相机的图片
                case CAMEAR_CROP_PICTURE:
                    String path2 = getPath(HomeActivity.this, SelectPictureDialog.cameraUri);
                    Bitmap bitmap2 = getBitmapFromPath(path2);
                    if (firstOrSelfType == 1) {
                        flagImageView.setImageBitmap(bitmap2);
                        LinearLayout auditView = getAduitView(flagImageView);
                        auditView.setVisibility(View.VISIBLE);
                    } else {
                        if (pictureType == 0) {
                            pictureImageView.setImageBitmap(bitmap2);
                            pictureImageView.setVisibility(View.VISIBLE);
                            changePictureImageView.setVisibility(View.GONE);
                        } else {
                            identifyImageView.setImageBitmap(bitmap2);
                            identifyImageView.setVisibility(View.VISIBLE);
                            addIdentifyImageView.setVisibility(View.GONE);
                        }
                    }
                    uploadPictureListener(path2);
                    break;
            }

        }
        selectPictureDialog.dismiss();
    }

    /**
     * 从路径中获取图片
     *
     * @param picturePath
     * @return
     */
    private Bitmap getBitmapFromPath(String picturePath) {
        //2、根据路径构造流的方法
        try {
            File file = new File(picturePath);
            InputStream inputStream = new FileInputStream(file);
            return BitmapFactory.decodeStream(inputStream, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;


    }

    /**
     * 根据URI得到路径
     *
     * @param context
     * @param uri
     * @return
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }


    private static Handler meHandler;

    public static void setMeHandler(Handler handler) {
        meHandler = handler;
    }

    /**
     * 上传图片
     *
     * @param path
     */
    private void uploadPictureListener(String path) {
        if (TextUtils.isEmpty(Utils.getUserId())) {
            return;
        }

        SharedPreferences.Editor editor = MeiMengApplication.sharedPreferences.edit();
        editor.putString(CommonConstants.UPLOAD_PIC_PATH, path);
        editor.commit();

        Log.e("SelectPicture", "-------上传图片");

        //显示进度条（刚开始上传）
        ProgressBean proBean = DialogUtils.createLoadingDialog2(this, "正在上传...");
        progressTextView = proBean.getTextView();
        dialog = proBean.getDialog();
        dialog.show();

//        ((OnSelectPictureDialogListener) context).getPicturePath(path);
        Observable observable = InternetUtils.getUploadPictureObservale(Utils.getUserId(), path, handler);

        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1() {
                    @Override
                    public void call(Object o) {
                        UploadPictureBean bean = GsonTools.getUploadPhototBean((String) o);
                        if (bean.isSuccess()) {
                            meHandler.sendEmptyMessage(999);
                            long pid = bean.getParam().getPid();

                            //个人中心图片上传
                            if (firstOrSelfType == 1) {

                                //添加图片，相册
                                addUserPhoto(pid, sort);
                            } else {
                                //头像的picid
                                if (pictureType == 0) {
                                    registerFirstInfoBean.setHeadPic(pid);
                                }
                                //认证图片的id
                                else {
                                    ArrayList<Long> picList = new ArrayList<Long>();
                                    picList.add(pid);
                                    registerFirstInfoBean.setIdentifyPicIdList(picList);
                                }
                            }

                            Log.e(TAG, "上传图片返回的id" + pid);

                            //取消进度条
                            if (null != dialog)
                                dialog.dismiss();

                        } else {
                            DialogUtils.setDialog(HomeActivity.this, bean.getError());
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e("Select", throwable.getMessage());
//                        ((OnSelectPictureDialogListener) context).cancelDialog2();
                    }
                });
//        ((OnSelectPictureDialogListener) context).cancelDialog();
    }

    /**
     * 剪切图片
     *
     * @param uri
     * @param outputX
     * @param outputY
     */
    private void cropImageUri(Uri uri, int outputX, int outputY) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");

        //通知剪切图片
        intent.putExtra("crop", true);

        //X方向的比例
        intent.putExtra("aspectX", 1);

        //Y方向的比例
        intent.putExtra("aspectY", 1);

        //裁剪区的宽度
        intent.putExtra("outputX", outputX);

        //裁剪区的高度
        intent.putExtra("outputY", outputY);

        //按照比例裁剪
        intent.putExtra("scale", true);

        //裁剪后的图片存放在uri中
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

        //裁剪后的图片不以Bitmap的形式返回
        intent.putExtra("return-data", false);

        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

        intent.putExtra("noFaceDetection", true);

        startActivityForResult(intent, CAMEAR_CROP_PICTURE);
    }

    /**
     * 上传用户的图片
     *
     * @param picId 图片id
     * @param sort  图片序列
     */
    private void addUserPhoto(final Long picId, int sort) {
        try {
            if (TextUtils.isEmpty(Utils.getUserId())) {
                return;
            }
            if (TextUtils.isEmpty(Utils.getUserToken())) {
                return;
            }
            String url = InternetConstant.SERVER_URL + InternetConstant.USERPHOTO_ADD + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();
            JSONStringer jsonStringer = new JSONStringer().object()
                    .key("picId").value(picId)
                    .key("sort").value(sort)
                    .key("verifyState").value("0")
                    .endObject();
            String jsonStr = jsonStringer.toString();
            //得到Observable并获取返回的数据(主线程中)
            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            if (null != dialog)
                                dialog.dismiss();
                            BaseBean basebeanJson = GsonTools.getBaseReqBean(s);
                            if (basebeanJson.isSuccess()) {

                                flagImageView.setTag(picId);
                            } else {
                                DialogUtils.setDialog(HomeActivity.this, basebeanJson.getError());
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable error) {
                            Log.e("test:", error.getMessage());
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private LinearLayout getAduitView(ImageView imgView) {
        RelativeLayout parentView = (RelativeLayout) imgView.getParent();
        LinearLayout auditingView = (LinearLayout) parentView.findViewById(R.id.me_auditing_layout);
        return auditingView;
    }

}
