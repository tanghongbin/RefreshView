package com.xqd.chatmessage.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
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
import com.xqd.chatmessage.ChatAplication;
import com.xqd.chatmessage.R;
import com.xqd.chatmessage.gson.GsonTools;
import com.xqd.chatmessage.gson.LoginBean;
import com.xqd.chatmessage.gson.LoginItem;
import com.xqd.chatmessage.net.InternetConstant;
import com.xqd.chatmessage.net.InternetUtils;
import com.xqd.chatmessage.util.CommonConstants;

import org.json.JSONStringer;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by Administrator on 2015/9/9.
 */
public class LoginActivity extends BaseActivity {
    @Bind(R.id.login_name)
    EditText loginname;

    @Bind(R.id.login_password)
    EditText loginpsw;

    @Bind(R.id.login_button)
    Button loginbtn;

    private String DEVICE_ID;
    private String accoutNum;
    private String paw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        ChatAplication.loginActivity.add(this);
        loginname.setText(ChatAplication.sharedPreferences.getString(CommonConstants.USER_ACCOUNT, ""));
        loginpsw.setText(ChatAplication.sharedPreferences.getString(CommonConstants.USER_PASSWORD, ""));
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accoutNum = loginname.getText().toString();
                paw = loginpsw.getText().toString();
                ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                // 检查网络连接，如果无网络可用，就不需要进行连网操作等
                NetworkInfo info = manager.getActiveNetworkInfo();
                if (info == null || !manager.getBackgroundDataSetting()) {
                    Toast.makeText(LoginActivity.this, "网络没有连接，请检查您的网络", Toast.LENGTH_LONG).show();
                } else {
                    if (accoutNum.equals("") || accoutNum == null) {
                        Toast.makeText(LoginActivity.this, "请输入账号", Toast.LENGTH_LONG).show();
                    } else {
                        if (paw.equals("") || paw == null) {
                            Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_LONG).show();
                        } else {
                            //记录账号和密码
                            final SharedPreferences.Editor editor = ChatAplication.sharedPreferences.edit();
                            editor.putString(CommonConstants.USER_ACCOUNT, accoutNum);
                            editor.putString(CommonConstants.USER_PASSWORD, paw);
                            editor.commit();
                            login();
                        }
                    }
                }

            }
        });

    }

    private void login() {
        try {
            String url = InternetConstant.SERVER_URL + InternetConstant.LOGIN_URL;
            JSONStringer stringer = new JSONStringer().object()
                    .key("tel").value(accoutNum)
                    .key("psw").value(paw)
                    .key("deviceTag").value(DEVICE_ID)
                    .endObject();
            String jsonStr = stringer.toString();
            Log.e("登录参数：", jsonStr);

            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1() {
                        @Override
                        public void call(Object o) {
                            LoginBean loginBean = GsonTools.getLoginBean((String) o);
                            if (loginBean.isSuccess()) {
                                ChatAplication.tel = loginBean.getReturnValue().getTel();
                                ChatAplication.name = loginBean.getReturnValue().getName();
                                LoginItem item = loginBean.getReturnValue();
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(intent);
                                for (Activity activity : ChatAplication.loginActivity) {
                                    activity.finish();

                                }
                                //记录用户的id和token,还有DEVICE_ID
                                SharedPreferences.Editor editor1 = ChatAplication.sharedPreferences.edit();
                                editor1.putLong(CommonConstants.USER_ID, item.getManagerId());
                                editor1.putString(CommonConstants.USER_TOKEN, item.getToken());
                                editor1.putString(CommonConstants.DEVICE_ID, DEVICE_ID);
                                editor1.commit();

                            } else {
                                Toast.makeText(LoginActivity.this, loginBean.getError(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    //获取当前设备的installationId并设置默认的页面
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
                Log.e("id:", DEVICE_ID);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ChatAplication.loginActivity.remove(this);
    }
}
