package com.example.com.meimeng.activity;

import android.app.Notification;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.com.meimeng.MeiMengApplication;
import com.example.com.meimeng.R;
import com.example.com.meimeng.constants.CommonConstants;
import com.example.com.meimeng.gson.GsonTools;
import com.example.com.meimeng.gson.gsonbean.BaseBean;
import com.example.com.meimeng.net.InternetConstant;
import com.example.com.meimeng.net.InternetUtils;
import com.example.com.meimeng.util.ActivityCollector;
import com.example.com.meimeng.util.DataCleanManager;
import com.example.com.meimeng.util.DialogUtils;
import com.example.com.meimeng.util.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by 010 on 2015/7/20.
 */
public class SettingActivity extends BaseActivity {

    @Bind(R.id.title_text)
    TextView titleText;

    @Bind(R.id.title_left_arrow_image_view)
    ImageView leftArrowImageView;

    @Bind(R.id.bow_arrow_image_view)
    ImageView bowArrowImageView;

    @Bind(R.id.setting_exit)
    TextView exitView;

    @Bind(R.id.issound_image)
    ImageView issound_image;

    @Bind(R.id.isshake_image)
    ImageView isshake_image;

    private  SharedPreferences.Editor editor = MeiMengApplication.sharedPreferences.edit();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        ButterKnife.bind(this);
        MeiMengApplication.currentContext=this;
        isSound_show();
        titleText.setText("设置");
        leftArrowImageView.setVisibility(View.VISIBLE);
        bowArrowImageView.setVisibility(View.GONE);
    }

    /**
     * 声音开关
     */
    @OnClick(R.id.issound_rl)
    void setIssound_rl(){
        isSound(CommonConstants.SOUNDMODE,issound_image);
        setSound();
    }

    /**
     * 振动开关
     */
    @OnClick(R.id.isshake_rl)
    void setIsshake_rl(){
        isSound(CommonConstants.VIBRATEMODE,isshake_image);
        setSound();
    }

    /**
     * 修改密码
     */
    @OnClick(R.id.setting_changepsw)
    void settingListener() {
        Intent intent = new Intent(SettingActivity.this, ChangePsw.class);
        startActivity(intent);
    }

    @OnClick(R.id.title_left_arrow_image_view)
    void leftArrowListener() {
        super.onBackPressed();
    }

    @OnClick(R.id.setting_exit)
    void exitListerner() {//退出当前账号

        try {
            //获取用户的uid,和token
            if (TextUtils.isEmpty(Utils.getUserId())) {
                return;
            }
            if (TextUtils.isEmpty( Utils.getUserToken() )) {
                return;
            }
            final SharedPreferences.Editor editor = MeiMengApplication.sharedPreferences.edit();
            String url = InternetConstant.SERVER_URL + InternetConstant.EXIT + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken() + "&deviceTag=" + "";
            Observable observable = InternetUtils.getJsonObservale(url, "");
            observable.observeOn(AndroidSchedulers.mainThread());
            observable.subscribe(new Action1() {
                @Override
                public void call(Object o) {
                    BaseBean baseBean = GsonTools.getBaseReqBean((String) o);
                    //如果成功，跳转到登录界面
                    if (baseBean.isSuccess()) {

                        editor.putLong(CommonConstants.USER_ID, -1).commit();

                        editor.putString(CommonConstants.USER_PASSWORD, "").commit();

                        Intent intent = new Intent(SettingActivity.this, RegisterActivity.class);
                        startActivity(intent);
                        ActivityCollector.finishAll();

                    } else {
                       DialogUtils.setDialog(SettingActivity.this, baseBean.getError());
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
//            finish();
        }
    }

    /**
     * 清除缓存的事件
     */
    @OnClick(R.id.setting_clean_cache_text)
    void cleanCacheListener(){
        DataCleanManager.cleanApplicationData(SettingActivity.this, CommonConstants.MEIMENG_FILE_PATH);
        Toast.makeText(SettingActivity.this,"清除缓存成功",Toast.LENGTH_LONG).show();
        MeiMengApplication.sharedPreferences.edit().putBoolean("isFirst",true).commit();
    }

    public void isSound(String MODE,ImageView view){
        Boolean flag = MeiMengApplication.sharedPreferences.getBoolean(MODE,false);
        editor.putBoolean(MODE,!flag).commit();
        view.setSelected(!flag);
    }

    public void setSound(){
        Boolean issound = MeiMengApplication.sharedPreferences.getBoolean(CommonConstants.SOUNDMODE,false);
        Boolean isshake =  MeiMengApplication.sharedPreferences.getBoolean(CommonConstants.VIBRATEMODE,false);

       int mode = Notification.DEFAULT_ALL; //提示模式统一默认为全部开启

       if(issound && isshake){
           mode = Notification.COLOR_DEFAULT;
       }
        if(issound && !isshake){
           mode = Notification.DEFAULT_VIBRATE;
        }
        if(!issound && isshake){
           mode = Notification.DEFAULT_SOUND;
        }

        editor.putInt(CommonConstants.MSGPROMPT, mode).commit();
    }

    public void isSound_show(){
        Boolean issound = MeiMengApplication.sharedPreferences.getBoolean(CommonConstants.SOUNDMODE,false);
        Boolean isshake =  MeiMengApplication.sharedPreferences.getBoolean(CommonConstants.VIBRATEMODE,false);
        issound_image.setSelected(issound);
        isshake_image.setSelected(isshake);
    }

}
