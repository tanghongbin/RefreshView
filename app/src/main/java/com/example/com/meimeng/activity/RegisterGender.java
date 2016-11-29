package com.example.com.meimeng.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.com.meimeng.MeiMengApplication;
import com.example.com.meimeng.R;
import com.example.com.meimeng.constants.CommonConstants;
import com.example.com.meimeng.custom.PickerView;
import com.example.com.meimeng.custom.RoundCornerImageView;
import com.example.com.meimeng.gson.GsonTools;
import com.example.com.meimeng.gson.gsonbean.BaseBean;
import com.example.com.meimeng.gson.gsonbean.LoginBean;
import com.example.com.meimeng.gson.gsonbean.LoginItem;
import com.example.com.meimeng.net.InternetConstant;
import com.example.com.meimeng.net.InternetUtils;
import com.example.com.meimeng.util.DataUtil;
import com.example.com.meimeng.util.Utils;

import org.json.JSONStringer;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by Administrator on 2015/7/30.
 */
public class RegisterGender extends BaseActivity {


    @Bind(R.id.register_gender_nextButton)
    TextView nextButton;


    @Bind(R.id.register_gender_man_view)
    ImageView manView;
    @Bind(R.id.img_llback)
    LinearLayout ll_back;


    @Bind(R.id.register_gender_woman_view)
    ImageView womanView;


    private DataUtil dataUtil;
    private PickerView pickerView_a;
    private PickerView pickerView_b;

    private Dialog dialog;



    //注册账号
    private String telNum = "";

    //密码
    private String pswNum = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.register_gender);
        ButterKnife.bind(this);
        MeiMengApplication.currentContext=this;
        MeiMengApplication.sex = 0;

        //得到账号和密码
        Intent intent = getIntent();
        telNum = intent.getStringExtra("tel");
        pswNum = intent.getStringExtra("psw");

        RoundCornerImageView img_bg = (RoundCornerImageView) findViewById(R.id.img_bg);
        img_bg.setAngie(20f, 20f);
        MeiMengApplication.loginActivity.add(this);
        dataUtil = new DataUtil(RegisterGender.this);


        pickerView_a = (PickerView) findViewById(R.id.picker_one_a);
        pickerView_b = (PickerView) findViewById(R.id.picker_one_b);



        Utils.readBitMap(RegisterGender.this,ll_back, R.raw.register_gender_bg);

        //点击选择性别男
        manView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manView.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_manchoose_pitch));
//                manText.setTextColor(getResources().getColor(R.color.text));
                womanView.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_womanchoose_normal));
//                womanText.setTextColor(getResources().getColor(R.color.text_gray));
                MeiMengApplication.sex = 0;
            }
        });

        //点击选择性别女
        womanView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manView.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_manchoose_normal));
//                manText.setTextColor(getResources().getColor(R.color.text_gray));
                womanView.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_womanchoose_pitch));
//                womanText.setTextColor(getResources().getColor(R.color.text));
                MeiMengApplication.sex = 1;
            }
        });

        Utils.getUerVerfiy();
        //下一步
        //邓成博修改部分
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(RegisterGender.this, RegisterFirst.class);
//                startActivity(intent);
                dataCommit();//提交
            }
        });


    }

    @Override
    protected void onStart() {
        super.onResume();
        setData();
    }

    private void setData() {
        dataUtil.setRegionData(pickerView_a, pickerView_b,true);
    }




    /**
     * 数据提交
     * 邓成博修改
     */
    private void dataCommit() {

        try {

            if (TextUtils.isEmpty(Utils.getUserId())) {
                return;
            }
            if (TextUtils.isEmpty(Utils.getUserToken())) {
                return;
            }
            String url = InternetConstant.SERVER_URL + InternetConstant.USER_EDIT + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();
            JSONStringer jsonStringer = new JSONStringer().object();
            int cityId = MeiMengApplication.cityBean.getCityId();

            //默认北京市
            if (cityId==-1){
                cityId = 2;
            }else if(cityId==3){
                cityId=857;
            }
            jsonStringer.key("sex").value(MeiMengApplication.sex)
                    .key("residence").value(cityId).endObject();
            String jsonStr = jsonStringer.toString();
            timeOutCloseDialog();   //网络获取超时设置
            //得到Observable并获取返回的数据(主线程中)
            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            if (null != dialog)
                                dialog.dismiss();
                            BaseBean baseBeanJson = GsonTools.getBaseReqBean(s);
                            if (baseBeanJson.isSuccess() != false) {
                                //登陆
                                loginListener();
                            } else {
                                Utils.setPromptDialog(RegisterGender.this, "服务器正在搬运数据请重新刷新");
                            }

                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable error) {
                            //Log.e("test:", error.getMessage());
                        }
                    });


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 网络超时提示
     */
    private void timeOutCloseDialog() {
        dialog = Utils.createLoadingDialog(RegisterGender.this, "请稍后...");
        dialog.show();
        dialog.setCancelable(false);
        Timer timer = new Timer();
        TimerTask tk = new TimerTask() {
            @Override
            public void run() {
                mhandler.sendEmptyMessage(20);
            }
        };
        timer.schedule(tk, 7000);
    }

    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 20) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                    Toast.makeText(RegisterGender.this, "当前网络不可用，请检查您的网络设置", Toast.LENGTH_SHORT).show();
                }
            } else {
                //不做操作
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MeiMengApplication.loginActivity.remove(this);
    }

    /**
     * 登陆功能
     */
    private void loginListener() {
        try {
            String deviceTag = MeiMengApplication.sharedPreferences.getString(CommonConstants.DEVICE_ID, "");
            String url = InternetConstant.SERVER_URL + InternetConstant.LOGIN_URL;
            JSONStringer stringer = new JSONStringer().object()
                    .key("tel").value(telNum)
                    .key("psw").value(pswNum)
                    .key("deviceTag").value(deviceTag)
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
                            if (null != dialog)
                                dialog.dismiss();
                            if (loginBean.isSuccess()) {
                                SharedPreferences.Editor editor = MeiMengApplication.sharedPreferences.edit();

                                editor.putBoolean(CommonConstants.LOGGIN_STATE, true).commit();
                                LoginItem item = loginBean.getReturnValue();
//                                LoginItem item = loginBean.getResult();
                                MeiMengApplication.sex = item.getSex();
                                Log.i("msg", item.getInfoState() + "");
                                editor.putInt(CommonConstants.INFO_STATE, item.getInfoState()).commit();
                                editor.putInt(CommonConstants.USER_VERFIY, item.getUserVerfiy()).commit();

                                Intent intent = new Intent(RegisterGender.this, HomeActivity.class);
                                intent.putExtra("init_type", 1);
                                intent.putExtra("str_condition","是否提示完善认证信息");
                                startActivity(intent);

                                for (Activity activity : MeiMengApplication.loginActivity) {
                                    activity.finish();
                                }

                                //记录用户的id和token,还有DEVICE_ID
                                editor.putLong(CommonConstants.USER_ID, item.getUid());
                                Log.i("qwe", "RegistergENDER UUID  :  " + item.getUid());
                                editor.putString(CommonConstants.USER_TOKEN, item.getToken());
                                editor.putInt(CommonConstants.USER_LEVEL, item.getLevel());
                                editor.commit();

                            } else {
                                Toast.makeText(RegisterGender.this, loginBean.getError(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

