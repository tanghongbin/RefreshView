package com.example.com.meimeng.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.com.meimeng.MeiMengApplication;
import com.example.com.meimeng.R;
import com.example.com.meimeng.constants.CommonConstants;
import com.example.com.meimeng.gson.GsonTools;
import com.example.com.meimeng.gson.gsonbean.BaseBean;
import com.example.com.meimeng.gson.gsonbean.LoginBean;
import com.example.com.meimeng.gson.gsonbean.LoginItem;
import com.example.com.meimeng.gson.gsonbean.LoginObj;
import com.example.com.meimeng.gson.gsonbean.RequestCodeBean;
import com.example.com.meimeng.net.InternetConstant;
import com.example.com.meimeng.net.InternetUtils;
import com.example.com.meimeng.util.DialogUtils;
import com.example.com.meimeng.util.TimeCountUtils;
import com.example.com.meimeng.util.Utils;

import org.json.JSONStringer;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by Administrator on 2015/7/29.
 */
public class RegisterFirst extends BaseActivity {

    @Bind(R.id.title_text)
    TextView titleText;

    @Bind(R.id.title_left_arrow_image_view)
    ImageView leftArrowImageView;

    @Bind(R.id.bow_arrow_image_view)
    ImageView bowArrowImageView;

    @Bind(R.id.title_sure_text)
    TextView sureText;

    @Bind(R.id.register_first_nextButton)
    Button nextButton;

    @Bind(R.id.register_first_code)
    TextView code;

    @Bind(R.id.register_first_codeEdit)
    EditText codeEdit;

    @Bind(R.id.register_first_psw)
    EditText psw;

    @Bind(R.id.register_first_inviterCode)
    EditText inviterCode;

    @Bind(R.id.register_first_tel)
    EditText tel;

    @Bind(R.id.register_first_treaty_img)
    ImageView treatyImg;
    @Bind(R.id.meimeng_userserver)
    TextView user_service;

    int str = 2;
    private String DEVICE_ID;
    private Dialog dialog;
    private String inviterCodestr;
    private TimeCountUtils timeCountUtils;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //获取手机唯一ID
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        DEVICE_ID = tm.getDeviceId();

        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.register_first);
        ButterKnife.bind(this);
        MeiMengApplication.currentContext = this;
        MeiMengApplication.loginActivity.add(this);
        /*DataUtil dataUtil = new DataUtil(RegisterFirst.this);
        dataUtil.getProvinceList();*/

        editor = MeiMengApplication.sharedPreferences.edit();
        bowArrowImageView.setVisibility(View.GONE);
        leftArrowImageView.setVisibility(View.VISIBLE);
        titleText.setText("注册");
        sureText.setVisibility(View.GONE);

        //点击我同意服务协议时下方下一步的按钮变颜色
        treatyImg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                str = str + 1;
                if (str % 2 == 0) {
                    nextButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.buttonstyle_pressed));
                    nextButton.setTextColor(getResources().getColor(R.color.text));
                    treatyImg.setBackgroundDrawable(getResources().getDrawable(R.drawable.login_ico_slected));

                } else {
                    nextButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.buttonstyle_normal));
                    nextButton.setTextColor(getResources().getColor(R.color.text_gray));
                    treatyImg.setBackgroundDrawable(getResources().getDrawable(R.drawable.login_ico_slect));
                }
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (inviterCode.getText().toString().equals("") || inviterCode.getText().toString() == null) {
                    inviterCodestr = null;
                } else {
                    inviterCodestr = inviterCode.getText().toString();
                }

                if (tel.getText().toString().equals("") || tel.getText().toString() == null) {
                    Toast.makeText(RegisterFirst.this, "请输入账号", Toast.LENGTH_LONG).show();
                } else {
                    if (tel.length() != 11) {
                        Toast.makeText(RegisterFirst.this, "账号为11位手机号码", Toast.LENGTH_LONG).show();
                    } else {
                        if (psw.getText().toString().equals("") || psw.getText().toString() == null) {
                            Toast.makeText(RegisterFirst.this, "请输入密码", Toast.LENGTH_LONG).show();
                        } else {
                            if (psw.length() < 8) {
                                Toast.makeText(RegisterFirst.this, "密码不能低于8位", Toast.LENGTH_LONG).show();
                            } else {
                                if (codeEdit.getText().toString().equals("") || codeEdit.getText().toString() == null) {
                                    Toast.makeText(RegisterFirst.this, "请输入验证码", Toast.LENGTH_LONG).show();
                                } else {
//                                    if (str % 2 == 0) {
                                    dialog = Utils.createLoadingDialog(RegisterFirst.this, "正在注册中...");
                                    dialog.show();
                                    iniview();
//                                    } else {
//                                        Toast.makeText(getApplicationContext(), "请接受协议", Toast.LENGTH_LONG).show();
//                                    }
                                }
                            }


                        }
                    }

                }


            }
        });


        user_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterFirst.this, ServiceTermActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 提交注册
     */
    private void iniview() {
        try {
            String url = InternetConstant.SERVER_URL + InternetConstant.REGIST;
            JSONStringer stringer = new JSONStringer().object()
                    .key("tel").value(tel.getText().toString())
//                    .key("sex").value(String.valueOf(MeiMengApplication.sex))
                    .key("code").value(codeEdit.getText().toString())
                    .key("inviterCode").value(inviterCodestr)
                    .key("psw").value(psw.getText().toString())
                    .key("deviceTag").value(DEVICE_ID)
                    .endObject();
            String jsonStr = stringer.toString();
            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1() {
                        @Override
                        public void call(Object o) {
                            Log.i("msg", o.toString());
                            if (null != dialog)
                                dialog.dismiss();
                            LoginBean loginBean = GsonTools.getLoginBean((String) o);

                            if (loginBean.isSuccess()) {

                                LoginObj param = loginBean.getParam();
                                LoginItem item = param.getResult();

                                editor.putInt(CommonConstants.USER_VERFIY, item.getUserVerfiy()).commit();

                                Intent intent = new Intent(RegisterFirst.this, RegisterGender.class);
                                intent.putExtra("tel", tel.getText().toString());
                                MeiMengApplication.sharedPreferences.edit().putString("tel", tel.getText().toString()).commit();
                                intent.putExtra("psw", psw.getText().toString());
                                MeiMengApplication.sharedPreferences.edit().putString("psw", psw.getText().toString()).commit();
                                startActivity(intent);
                                Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_LONG).show();

                                //记录用户的id和token,还有DEVICE_ID
                                editor.putLong(CommonConstants.USER_ID, item.getUid());
                                Log.i("qwe", "RegisterFirst UUID  :  " + item.getUid());
                                editor.putString(CommonConstants.USER_TOKEN, item.getToken());
                                editor.putString(CommonConstants.DEVICE_ID, DEVICE_ID);
                                editor.putInt(CommonConstants.USER_LEVEL, item.getLevel());
                                editor.commit();

                            } else {
                                Toast.makeText(getApplicationContext(), loginBean.getError(), Toast.LENGTH_LONG).show();
                            }
                        }

                    },new Action1<Throwable>() {
                        @Override
                        public void call(Throwable error) {
                            Toast.makeText(getApplicationContext(), "注册失败", Toast.LENGTH_LONG).show();
                            //Log.e("test:", error.getMessage());
                        }

                    });

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * 获取验证码前参看号码是否已经注册
     */
    @OnClick(R.id.register_first_code)
    void registerfirstcodeListener() {

        timeCountUtils = new TimeCountUtils(this, 60000, 1000, code);
        //code.setBackgroundColor(0xbf9f62);
        if (tel.length() != 11) {
            Toast.makeText(RegisterFirst.this, "账号为11位手机号码", Toast.LENGTH_LONG).show();
        } else {
            try {
                String url = InternetConstant.SERVER_URL + InternetConstant.USER_HAS;
                JSONStringer jsonStringer = new JSONStringer().object()
                        .key("tel").value(tel.getText().toString())
                        .endObject();
                String jsonStr = jsonStringer.toString();
                //得到Observable并获取返回的数据(主线程中)
                Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
                observable.observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<String>() {
                            @Override
                            public void call(String s) {
                                RequestCodeBean requestCodeBeanJson = GsonTools.getRequestCodeBean(s);
                                if (requestCodeBeanJson.isReturnValue() == false) {
                                    timeCountUtils.start();
                                    requestcode();
                                } else {
                                    Intent intent = new Intent(RegisterFirst.this, RegisterActivity.class);
                                    startActivity(intent);
                                    Toast.makeText(RegisterFirst.this, "该账号已经注册", Toast.LENGTH_LONG).show();
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

    /**
     * 获取验证码
     */
    public void requestcode() {
        try {
            String url = InternetConstant.SERVER_URL + InternetConstant.REQUESTCODE;
            JSONStringer jsonStringer = new JSONStringer().object()
                    .key("tel").value(tel.getText().toString())
                    .endObject();
            String jsonStr = jsonStringer.toString();
            //得到Observable并获取返回的数据(主线程中)
            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            BaseBean basebeanJson = GsonTools.getBaseReqBean(s);
                            if (basebeanJson.isSuccess() != false) {
                                Toast.makeText(RegisterFirst.this, "已经发送验证码", Toast.LENGTH_SHORT).show();
                            } else
                                DialogUtils.setDialog(RegisterFirst.this, basebeanJson.getError());

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
     * 美盟服务条款
     */
    @OnClick(R.id.register_first_treaty_text)
    void registerfirsttreatytext() {
        Intent intent = new Intent(RegisterFirst.this, ServiceClause.class);
        startActivity(intent);
    }

    //返回按钮
    @OnClick(R.id.title_left_arrow_layout)
    void leftArrowListener() {
        onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MeiMengApplication.loginActivity.remove(this);
    }
}
