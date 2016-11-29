package com.example.com.meimeng.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.example.com.meimeng.net.InternetConstant;
import com.example.com.meimeng.net.InternetUtils;
import com.example.com.meimeng.util.DialogUtils;
import com.example.com.meimeng.util.Utils;

import org.json.JSONStringer;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by Administrator on 2015/7/30.
 */
public class ChangePsw extends BaseActivity {
    @Bind(R.id.title_text)
    TextView titleText;

    @Bind(R.id.title_left_arrow_image_view)
    ImageView leftArrowImageView;

    @Bind(R.id.bow_arrow_image_view)
    ImageView bowArrowImageView;

    @Bind(R.id.change_password_oldpsw)
    EditText oldpsw;

    @Bind(R.id.change_password_newpsw)
    EditText newpsw;

    @Bind(R.id.change_password_newpswagain)
    EditText newpswagain;

    @Bind(R.id.change_password_button)
    Button passwordbutton;

    private BaseBean basebeanJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.change_password);
        ButterKnife.bind(this);

        MeiMengApplication.currentContext=this;
        titleText.setText("修改密码");
        leftArrowImageView.setVisibility(View.VISIBLE);
        bowArrowImageView.setVisibility(View.GONE);

        passwordbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newpsw.getText().toString().length() < 8) {
                    Toast.makeText(ChangePsw.this, "新密码长度不能小于8位", Toast.LENGTH_SHORT).show();
                } else {
                    if (newpsw.getText().toString().equals(oldpsw.getText().toString())) {
                        Toast.makeText(ChangePsw.this, "新密码不能与旧密码相同", Toast.LENGTH_SHORT).show();
                    } else {
                        if (newpsw.getText().toString().equals(newpswagain.getText().toString())) {
                            try {
                                //新的密码
                                final String newPassword = newpsw.getText().toString();
                                //获取用户的uid,和token
                                if (TextUtils.isEmpty(Utils.getUserId())) {
                                    return;
                                }
                                if (TextUtils.isEmpty(Utils.getUserToken())) {
                                    return;
                                }

                                String url = InternetConstant.SERVER_URL + InternetConstant.UPDATEPASSWD + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();

                                JSONStringer stringer = new JSONStringer().object()
                                        .key("psw").value(newpsw.getText().toString())
                                        .key("oldPsw").value(oldpsw.getText().toString())
                                        .endObject();
                                String jsonStr = stringer.toString();
                                Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
                                observable.observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Action1<String>() {
                                            @Override
                                            public void call(String s) {
                                                basebeanJson = GsonTools.getBaseReqBean(s);
                                                if (basebeanJson.isSuccess() != false) {
                                                    Toast.makeText(ChangePsw.this, "密码修改成功", Toast.LENGTH_SHORT).show();

                                                    //清空本地记录的密码
                                                    SharedPreferences.Editor editor = MeiMengApplication.sharedPreferences.edit();
                                                    editor.putString(CommonConstants.USER_PASSWORD, newPassword);
                                                    editor.commit();

                                                    finish();
                                                } else {
                                                    DialogUtils.setDialog(ChangePsw.this, basebeanJson.getError());
                                                    if (basebeanJson.getError().equals("旧密码错误")) {
                                                        Toast.makeText(ChangePsw.this, basebeanJson.getError(), Toast.LENGTH_SHORT).show();
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
                        } else {
                            Toast.makeText(ChangePsw.this, "两次输入密码不一致", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

            }
        });

    }

    @OnClick(R.id.title_left_arrow_image_view)
    void leftArrowListener() {
        super.onBackPressed();
    }
}