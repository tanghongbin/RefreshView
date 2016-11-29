package com.example.com.meimeng.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.com.meimeng.MeiMengApplication;
import com.example.com.meimeng.R;
import com.example.com.meimeng.constants.CommonConstants;
import com.example.com.meimeng.dialog.UpdateOsDialog;
import com.example.com.meimeng.gson.GsonTools;
import com.example.com.meimeng.gson.gsonbean.CheckOsVersionBean;
import com.example.com.meimeng.gson.gsonbean.LoginBean;
import com.example.com.meimeng.gson.gsonbean.LoginItem;
import com.example.com.meimeng.gson.gsonbean.LoginTokenBean;
import com.example.com.meimeng.net.InternetConstant;
import com.example.com.meimeng.net.InternetUtils;
import com.example.com.meimeng.util.ChannelUtils;
import com.example.com.meimeng.util.DataUtil;
import com.example.com.meimeng.util.Utils;

import org.json.JSONStringer;

import java.util.Timer;
import java.util.TimerTask;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by Administrator on 2015/7/29.
 */
public class StartActivity extends BaseActivity {

    private SharedPreferences.Editor editor;

    private ImageView bgImageView;
    private UpdateOsDialog updateOsDialog;
    private Handler xHandler ;

    private String DEVICE_ID;
    private String venderCode;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);
        MeiMengApplication.currentContext=this;
        editor = MeiMengApplication.sharedPreferences.edit();
        DataUtil dataUtil = new DataUtil(StartActivity.this);
        dataUtil.getProvinceList();
        bgImageView = (ImageView)findViewById(R.id.start_bg_image_view);

        //防止内存溢出处理OOM
        Utils.readBitMap(StartActivity.this,bgImageView, R.raw.logo_meimeng_1);

        //获取手机唯一ID
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        DEVICE_ID = tm.getDeviceId();
        //获取渠道编号
        venderCode= ChannelUtils.getChannelCode(StartActivity.this);
        xHandler = new Handler();
        //检查版本号
        checkOsVersion();
        //检查初始资料
    }

    public  class  Splashhandler implements Runnable {
        //再次登陆后不用输入密码
        public void run() {
            if (MeiMengApplication.sharedPreferences.getBoolean("isFirst",false) == false) {
                setStatistical();//统计接口
                editor.putBoolean("isFirst", true).commit();
                startActivity(new Intent(StartActivity.this, GuideActivity.class));
                StartActivity.this.finish();
            }else{
                ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                // 检查网络连接，如果无网络可用，就不需要进行连网操作等
                NetworkInfo info = manager.getActiveNetworkInfo();
                if (info == null || !manager.getBackgroundDataSetting()) {
                    startActivity(new Intent(StartActivity.this, RegisterActivity.class));
                    StartActivity.this.finish();
                } else {
                    checkLoginByToken();
                   //initview();
                }
            }

        }

    }
    //第一次下载打开接入统计接口
    private void setStatistical() {
        try {
            String url = InternetConstant.SERVER_URL + InternetConstant.VENSER;
            JSONStringer stringer = new JSONStringer().object()
                    .key("venderCode").value(venderCode)
                    .key("deviceTag").value(DEVICE_ID)
                    .endObject();
            String jsonStr = stringer.toString();
            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1() {
                        @Override
                        public void call(Object o) {
                            Log.d("wz",o.toString());
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable error) {

                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Handler myHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what== 555) {
                    finish();
            }
        }
    };
    private void initview() {
        try {
            String url = InternetConstant.SERVER_URL + InternetConstant.LOGIN_URL;
            JSONStringer stringer = new JSONStringer().object()
                    .key("tel").value(MeiMengApplication.sharedPreferences.getString(CommonConstants.USER_ACCOUNT, ""))
                    .key("psw").value(MeiMengApplication.sharedPreferences.getString(CommonConstants.USER_PASSWORD, ""))
                    .key("deviceTag").value(MeiMengApplication.sharedPreferences.getString(CommonConstants.DEVICE_ID, ""))
                    .endObject();
            String jsonStr = stringer.toString();
            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1() {
                        @Override
                        public void call(Object o) {
                            Log.i("qwe","startactivity  :  "+o.toString());
                            LoginBean loginBean = GsonTools.getLoginBean((String) o);
                            if (loginBean.isSuccess()) {
                                LoginItem item = loginBean.getReturnValue();
//                                LoginItem item = loginBean.getResult();
                                MeiMengApplication.sex = item.getSex();
                                MeiMengApplication.residence=item.getResidence();
                                editor.putInt(CommonConstants.USER_VERFIY, item.getUserVerfiy()).commit();
                                editor.putInt(CommonConstants.INFO_STATE,item.getInfoState()).commit();
                                if(MeiMengApplication.residence ==null||MeiMengApplication.sex==null){
                                    Intent intent = new Intent(StartActivity.this, RegisterGender.class);
                                    intent.putExtra("tel",MeiMengApplication.sharedPreferences.getString("tel",null));
                                    intent.putExtra("psw",MeiMengApplication.sharedPreferences.getString("psw", null));
                                    startActivity(intent);
                                }else {
                                    Intent intent = new Intent(StartActivity.this, HomeActivity.class);
                                    intent.putExtra("init_type", 1);
                                    startActivity(intent);
                                }
                                for (Activity activity : MeiMengApplication.loginActivity) {
                                    activity.finish();
                                }

                                //记录用户的id和token,还有DEVICE_ID
                                editor.putLong(CommonConstants.USER_ID, item.getUid());

                                Log.i("qwe", "START ACTIVITYY UUID  :  " + item.getUid());

                                editor.putString(CommonConstants.USER_TOKEN, item.getToken());
                                editor.putInt(CommonConstants.USER_LEVEL, item.getLevel());
                                //editor.putString(CommonConstants.DEVICE_ID, DEVICE_ID);
                                editor.commit();

                            } else {
                                startActivity(new Intent(StartActivity.this, RegisterActivity.class));
                                StartActivity.this.finish();
                            }
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkLoginByToken() {
        try {
            String url = InternetConstant.SERVER_URL + InternetConstant.CHECK_BY_TOKEN + "?uid=" + Utils.getUserId() + "&token=" +Utils.getUserToken()+ "&clientUnique=" + DEVICE_ID;
            ;
            JSONStringer stringer = new JSONStringer().object()
                    .endObject();
            String jsonStr = stringer.toString();
            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1() {
                        @Override
                        public void call(Object o) {
                            LoginTokenBean loginTokenBean= GsonTools.getLoginTokenBean((String) o);
                            if (loginTokenBean.isSuccess()) {
                                Intent intent = new Intent(StartActivity.this, HomeActivity.class);
                                intent.putExtra("init_type", 1);
                                startActivity(intent);

                            } else {
                                initview();
                            }
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    /**
     * 检查版本更新
     */
    private void checkOsVersion() {
        try {

            //当前版本的版本号
            PackageInfo currentInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            final String versionName = currentInfo.versionName;

            String url = InternetConstant.SERVER_URL + InternetConstant.CHECK_OS_VERSION + "?clientUnique=" + DEVICE_ID;
            JSONStringer jsonStringer = new JSONStringer().object().key("androidVerNum").value(versionName)
                    .endObject();
            String jsonStr = jsonStringer.toString();
            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1() {
                        @Override
                        public void call(Object o) {
                            String data = (String) o;
                            CheckOsVersionBean checkOsVersionBean = GsonTools.getCheckOsVersionBean(data);
                            if (!checkOsVersionBean.isSuccess()) {
                                String upToDateVersion = checkOsVersionBean.getParam().getVERSION().getAndroidVerNum();
                                if (!upToDateVersion.equals(versionName)) {//更新版本
                                    updateOsDialog = new UpdateOsDialog(StartActivity.this,myHandler,R.style.loading_dialog);
                                    updateOsDialog.show();
                                }else{//不更新，正常登陆
                                    xHandler.postDelayed(new Splashhandler(), 2000);
                                }
                            }else{
                                xHandler.postDelayed(new Splashhandler(), 2000);
                            }
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
