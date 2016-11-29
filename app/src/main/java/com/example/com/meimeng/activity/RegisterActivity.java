package com.example.com.meimeng.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.PushService;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.callback.AVIMMessagesQueryCallback;
import com.avoscloud.leanchatlib.controller.ChatManager;
import com.avoscloud.leanchatlib.controller.MessageHelper;
import com.example.com.meimeng.MeiMengApplication;
import com.example.com.meimeng.R;
import com.example.com.meimeng.bean.ChatMessageBean;
import com.example.com.meimeng.constants.CommonConstants;
import com.example.com.meimeng.gson.GsonTools;
import com.example.com.meimeng.gson.gsonbean.ConversationInfoBean;
import com.example.com.meimeng.gson.gsonbean.LoginBean;
import com.example.com.meimeng.gson.gsonbean.LoginItem;
import com.example.com.meimeng.gson.gsonbean.UserPartInfoBean;
import com.example.com.meimeng.gson.gsonbean.UserPartInfoItem;
import com.example.com.meimeng.net.InternetConstant;
import com.example.com.meimeng.net.InternetUtils;
import com.example.com.meimeng.util.ChatSQliteDataUtil;
import com.example.com.meimeng.util.MessageUtils;
import com.example.com.meimeng.util.Utils;

import org.json.JSONStringer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by Administrator on 2015/7/29.
 */
public class RegisterActivity extends Activity {
    @Bind(R.id.login_name)
    EditText loginName;

    @Bind(R.id.login_password)
    EditText loginPassWord;

    @Bind(R.id.login_button)
    Button loginButton;  //登录按钮

    private String DEVICE_ID;
    private Dialog dialog;
    private String accoutNum;
    private String psw;
    private SharedPreferences.Editor editor;

    private ChatSQliteDataUtil sq = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.register);
        ButterKnife.bind(this);
        MeiMengApplication.currentContext = this;
        MeiMengApplication.loginActivity.add(this);
        //注销号码后，号码仍然能显示
        loginName.setText(MeiMengApplication.sharedPreferences.getString(CommonConstants.USER_ACCOUNT, ""));

        //获得InstallationId
        setInstallationId();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                // 检查网络连接，如果无网络可用，就不需要进行连网操作等
                NetworkInfo info = manager.getActiveNetworkInfo();
                if (info == null || !manager.getBackgroundDataSetting()) {
                    Toast.makeText(RegisterActivity.this, "网络没有连接，请检查您的网络", Toast.LENGTH_LONG).show();
                } else {
                    dialog = Utils.createLoadingDialog(RegisterActivity.this, "登录中...");
                    dialog.show();
                    accoutNum = loginName.getText().toString();
                    psw = loginPassWord.getText().toString();

                    if (accoutNum.equals("") || accoutNum == null) {
                        Toast.makeText(RegisterActivity.this, "请输入账号", Toast.LENGTH_LONG).show();
                        if (null != dialog)
                            dialog.dismiss();
                    } else {
                        if (psw.equals("") || psw == null) {
                            Toast.makeText(RegisterActivity.this, "请输入密码", Toast.LENGTH_LONG).show();
                            if (null != dialog)
                                dialog.dismiss();
                        } else {
                            //记录账号和密码
                            editor = MeiMengApplication.sharedPreferences.edit();
                            editor.putString(CommonConstants.USER_ACCOUNT, accoutNum);
                            editor.putString(CommonConstants.USER_PASSWORD, psw);
                            editor.commit();
                            MeiMengApplication.sharedPreferences.edit().putString("tel", accoutNum).commit();
                            MeiMengApplication.sharedPreferences.edit().putString("psw", psw).commit();
                            //网络请求登录
                            initview();

                        }
                    }
                }


            }
        });

    }

    /**
     * 网络请求登陆
     */
    private void initview() {
        try {
            String url = InternetConstant.SERVER_URL + InternetConstant.LOGIN_URL + "?clientUnique=" + DEVICE_ID;
            ;
            JSONStringer stringer = new JSONStringer().object()
                    .key("tel").value(accoutNum)
                    .key("psw").value(psw)
                    .key("deviceTag").value(DEVICE_ID)
                    .endObject();
            String jsonStr = stringer.toString();
            timeOutCloseDialog();
            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1() {
                        @Override
                        public void call(Object o) {
                            Log.i("msg", o.toString());
                            LoginBean loginBean = GsonTools.getLoginBean((String) o);
                            if (loginBean.isSuccess()) {
                                editor.putBoolean(CommonConstants.LOGGIN_STATE, true).commit();
                                LoginItem item = loginBean.getReturnValue();
                                MeiMengApplication.sex = item.getSex();
                                MeiMengApplication.residence = item.getResidence();
                                Log.i("msg", item.getInfoState() + "");
                                editor.putInt(CommonConstants.INFO_STATE, item.getInfoState()).commit();
                                editor.putInt(CommonConstants.USER_VERFIY, item.getUserVerfiy()).commit();
                                //记录用户的id和token,还有DEVICE_ID
                                editor.putLong(CommonConstants.USER_ID, item.getUid());
                                editor.putString(CommonConstants.USER_TOKEN, item.getToken());
                                editor.putInt(CommonConstants.USER_LEVEL, item.getLevel());
                                editor.putString(CommonConstants.DEVICE_ID, DEVICE_ID);
                                editor.commit();
                                if (MeiMengApplication.residence == null || MeiMengApplication.sex == null) {
                                    Intent intent = new Intent(RegisterActivity.this, RegisterGender.class);
                                    intent.putExtra("tel", MeiMengApplication.sharedPreferences.getString("tel", null));
                                    intent.putExtra("psw", MeiMengApplication.sharedPreferences.getString("psw", null));
                                    startActivity(intent);
                                    for (Activity activity : MeiMengApplication.loginActivity) {
                                        activity.finish();
                                    }
                                } else {
                                    getListFromNet(item);
                                }

                            } else {
                                if (dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                                Toast.makeText(RegisterActivity.this, loginBean.getError(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void jumpHomeActivity(LoginItem item) {

        if (null != dialog&&dialog.isShowing()){
            dialog.dismiss();
        }
        Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
        intent.putExtra("init_type", 1);
        startActivity(intent);
        for (Activity activity : MeiMengApplication.loginActivity) {
            activity.finish();
        }
    }

    /**
     * 客户端获取该用户会话列表
     */
    private void getListFromNet(final LoginItem item) {


        final String uid=String.valueOf(item.getUid());
        final String token=item.getToken();
        final SharedPreferences.Editor editor = MeiMengApplication.sharedPreferences.edit();

        Log.i("qwe", "getListFromNet UUID  :  " + item.getUid());
        editor.putLong(CommonConstants.USER_ID, Long.parseLong(uid));
        editor.commit();
        MessageUtils.setLeanCloudSelfUid();
       /* sq = new ChatSQliteDataUtil(RegisterActivity.this);
        sq.deleteTable("chatMessage_hn"+MeiMengApplication.sharedPreferences.getLong(CommonConstants.USER_ID,-1)+"");*/
        sq = new ChatSQliteDataUtil(RegisterActivity.this);
        try {
            //获取用户的uid,和token
            if (TextUtils.isEmpty(uid)) {
                return;
            }
            if (TextUtils.isEmpty(token)) {
                return;
            }
            String url = InternetConstant.SERVER_URL + InternetConstant.GETRFROMSERVER + "?uid=" + uid + "&token=" + token;
            JSONStringer jsonStringer = new JSONStringer().object()
                    .key("uid").value(Long.parseLong(uid))
                    .endObject();
            String jsonStr = jsonStringer.toString();
            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            ConversationInfoBean conversationInfoBean = GsonTools.getListConversationBean(s);

                            List conversations = conversationInfoBean.getParam().getResultDO().getConversations();
                            final ChatManager chatManager = ChatManager.getInstance();

                            chatManager.setOnConversation(new ChatManager.OnConversation() {
                                @Override
                                public void onResult(List<AVIMConversation> list) {

                                    for (int i = 0; i < list.size(); i++) {
                                        final AVIMConversation conversation = list.get(i);
                                        conversation.queryMessages(new AVIMMessagesQueryCallback() {
                                            @Override
                                            public void done(List<AVIMMessage> list, AVIMException e) {
                                                if (e == null) {
                                                    if (list.size() > 0) {
                                                        AVIMMessage avimMessage = list.get((list.size() - 1));
                                                        Long time = avimMessage.getTimestamp(); //接收到消息时的时间（毫秒值）
                                                        Date date = new Date(time);
                                                        SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
                                                        String myUid = null;
                                                        if (!uid.equals(avimMessage.getFrom())){
                                                             myUid =avimMessage.getFrom();
                                                        }else if (!uid.equals(conversation.getMembers().get(0))){
                                                            myUid =conversation.getMembers().get(0);
                                                        }else{
                                                            myUid =conversation.getMembers().get(1);
                                                        }
                                                        String timestr = format.format(date);
                                                        String conversationId = conversation.getConversationId();
                                                        CharSequence content = MessageHelper.outlineOfMsg((AVIMTypedMessage) avimMessage);//接收消息的内容
                                                        boolean isOpen = true;
                                                        if (conversation.getAttribute("isOpen") != null) {
                                                            isOpen = (boolean) conversation.getAttribute("isOpen");
                                                        }
                                                        if (!myUid.startsWith("hn")) {
                                                            if (sq.isExist(Long.parseLong(myUid))) {//本地数据库中如果存在就更新本地数据的内容，如果不存在就添加到本地数据库
                                                                upData(myUid, timestr, content, isOpen, conversationId);
                                                            } else {
                                                                getNewUserMessage(myUid, token, content, timestr, isOpen, conversationId);
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        });

                                    }
                                }
                            });
                            chatManager.fetchConversationById(conversations,uid);
                            jumpHomeActivity(item);//跳转到home页面
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
     * 获取新用户的信息
     *
     * @param targetUid
     * @return
     */
    private ChatMessageBean getNewUserMessage(final String targetUid, final String token, final CharSequence content, final String time, final boolean isOpen, final String conversationId) {


        final ChatMessageBean chatMessageBean = new ChatMessageBean();
        try {

            if (TextUtils.isEmpty(Utils.getUserId())) {
                return null;
            }
            if (TextUtils.isEmpty(token)) {
                return null;
            }

            String url = InternetConstant.SERVER_URL + InternetConstant.USER_GETPARTINFO + "?uid=" + Utils.getUserId() + "&token=" + token;
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

                                chatMessageBean.setContent(getContent(content).toString());

                                chatMessageBean.setTime(time);
                                chatMessageBean.setConversationId(conversationId);
                                Map attrs = new HashMap();
                                attrs.put("isOpen", isOpen);
                                chatMessageBean.setAttributes(attrs);
                                addData(chatMessageBean);

                            } else {
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


    private void addData(ChatMessageBean bean) {
        sq.addData(bean);
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

    public CharSequence getContent(CharSequence content) {

        if (content.toString().matches("^((:[A-Za-z0-9_]+:)*)$")) {
            content = "[表情]";
        }
        return content;
    }


    /**
     * 网络超时提示
     */
    private void timeOutCloseDialog() {
        Timer timer = new Timer();
        TimerTask tk = new TimerTask() {
            @Override
            public void run() {
                mhandler.sendEmptyMessage(20);
            }
        };
        timer.schedule(tk, 15000);
    }

    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 20) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                    Toast.makeText(RegisterActivity.this, "当前网络不可用，请检查您的网络设置", Toast.LENGTH_SHORT).show();
                }
            } else {
                //不做操作
            }
        }
    };

    /**
     * 关于美盟按钮
     */
    @OnClick(R.id.register_aboutmeimeng)
    void registerboutmeimengLstener() {
        Intent intent = new Intent(RegisterActivity.this, AboutMeimeng.class);
        startActivity(intent);
    }

    /**
     * 忘记密码
     */
    @OnClick(R.id.register_forgetpsw)
    void registerforgetpswListener() {
        Intent intent = new Intent(RegisterActivity.this, RegisterResetPsw.class);
        startActivity(intent);
    }

    /**
     * 注册账号跳转
     */
    @OnClick(R.id.register_number)
    void registernumberlistener() {
        Intent intent = new Intent(RegisterActivity.this, RegisterFirst.class);
        startActivity(intent);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("RegisterActivity", "onStart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MeiMengApplication.loginActivity.remove(this);
    }

    /**
     * 获取当前设备的installationId并设置默认的页面
     */
    private void setInstallationId() {

        // 设置默认打开的 Activity
        PushService.setDefaultPushCallback(this, HomeActivity.class);
        // 订阅频道，当该频道消息到来的时候，打开对应的 Activity
        PushService.subscribe(this, "public", HomeActivity.class);

        // 保存 installation 到服务器
        AVInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                AVInstallation.getCurrentInstallation().saveInBackground();
                //获取当前设备的installationId
                DEVICE_ID = AVInstallation.getCurrentInstallation().getObjectId();
            }
        });
    }
}
