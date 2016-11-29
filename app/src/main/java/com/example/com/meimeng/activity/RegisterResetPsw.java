package com.example.com.meimeng.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.com.meimeng.MeiMengApplication;
import com.example.com.meimeng.R;
import com.example.com.meimeng.gson.GsonTools;
import com.example.com.meimeng.gson.gsonbean.BaseBean;
import com.example.com.meimeng.gson.gsonbean.RequestCodeBean;
import com.example.com.meimeng.net.InternetConstant;
import com.example.com.meimeng.net.InternetUtils;
import com.example.com.meimeng.util.TimeCountUtils;

import org.json.JSONStringer;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by Administrator on 2015/8/2.
 */
public class RegisterResetPsw extends BaseActivity {
    @Bind(R.id.title_text)
    TextView titleText;

    @Bind(R.id.title_left_arrow_image_view)
    ImageView leftArrowImageView;

    @Bind(R.id.bow_arrow_image_view)
    ImageView bowArrowImageView;

    @Bind(R.id.title_sure_text)
    TextView sureText;

    @Bind(R.id.register_resetpsw_codeButton)
    TextView codeButton;

    @Bind(R.id.register_resetpsw_tel)
    EditText tel;

    @Bind(R.id.register_resetpsw_codeEdit)
    EditText codeEdit;

    @Bind(R.id.register_resetpsw_psw)
    EditText psw;

//    @Bind(R.id.register_resetpsw_confirmpsw)
//    EditText confirmPsw;

    private String telStr;
    private String code;
    private String pswStr;
//    private String cpsw;
    private TimeCountUtils timeCountUtils;
    private BaseBean basebeanJson;
    private Toast thisToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.register_resetpsw);
        ButterKnife.bind(this);
        MeiMengApplication.currentContext=this;
        MeiMengApplication.loginActivity.add(this);

        bowArrowImageView.setVisibility(View.GONE);
        leftArrowImageView.setVisibility(View.VISIBLE);
        titleText.setText("忘记密码");
        sureText.setVisibility(View.GONE);
        thisToast=new Toast(RegisterResetPsw.this);
    }

    /**
     * 获取验证码前判断手机号是否已经注册
     */
    @OnClick(R.id.register_resetpsw_codeButton)
    void registerfirstcodeListener() {
        if(tel.length()<11){
            thisToast.makeText(RegisterResetPsw.this, "手机号码位数不能低于11位", Toast.LENGTH_SHORT).show();
            //thisToast.setGravity(Gravity.CENTER, 100,100);
        }else{
            timeCountUtils = new TimeCountUtils(this, 60000, 1000, codeButton);
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
                                    Toast.makeText(RegisterResetPsw.this, "该帐号并未注册", Toast.LENGTH_SHORT).show();
                                } else {
                                    requestcode();
                                    timeCountUtils.start();
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
                                Toast.makeText(RegisterResetPsw.this, "已经发送验证码", Toast.LENGTH_SHORT).show();
                            } else
                               // DialogUtils.setDialog(RegisterResetPsw.this, basebeanJson.getError());
                                Toast.makeText(RegisterResetPsw.this,"发送验证码失败",Toast.LENGTH_SHORT).show();
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

    @OnClick(R.id.register_resetpsw_button)
    void registerresetpswbuttonListener() {
        telStr = tel.getText().toString();
        code = codeEdit.getText().toString();
        pswStr = psw.getText().toString();
//        cpsw = confirmPsw.getText().toString();
        if(tel.length()<11){
            Toast.makeText(RegisterResetPsw.this, "手机号码位数不能低于11位", Toast.LENGTH_LONG).show();
        }else{
            if (code.length()<=0){
                Toast.makeText(RegisterResetPsw.this, "请输入验证码", Toast.LENGTH_LONG).show();
            }else{
                if (psw.length() < 8||psw.length()>16) {
                    Toast.makeText(RegisterResetPsw.this, "输入密码位数8到16位", Toast.LENGTH_LONG).show();
                } else {
//            if (cpsw.equals(pswStr)) {
                    try {
                        String url = InternetConstant.SERVER_URL + InternetConstant.AUTH_RESET;
                        JSONStringer jsonStringer = new JSONStringer().object()
                                .key("tel").value(telStr)
                                .key("psw").value(pswStr)
                                .key("code").value(code)
                                .endObject();
                        String jsonStr = jsonStringer.toString();
                        //得到Observable并获取返回的数据(主线程中)
                        Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
                        observable.observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Action1<String>() {
                                    @Override
                                    public void call(String s) {
                                        basebeanJson = GsonTools.getBaseReqBean(s);
                                        if (basebeanJson.isSuccess() != false) {
                                            Toast.makeText(RegisterResetPsw.this, "修改密码成功，请重新登陆", Toast.LENGTH_SHORT).show();
                                            onBackPressed();
                                        } else
                                            Toast.makeText(RegisterResetPsw.this, basebeanJson.getError(), Toast.LENGTH_LONG).show();

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
//            } else {
//                Toast.makeText(RegisterResetPsw.this, "两次输入密码不一致", Toast.LENGTH_LONG).show();
//            }
                }
            }
        }

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
