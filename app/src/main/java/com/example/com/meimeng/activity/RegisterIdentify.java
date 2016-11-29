package com.example.com.meimeng.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.com.meimeng.MeiMengApplication;
import com.example.com.meimeng.R;
import com.example.com.meimeng.activity.identify.IdentifyEducationActivity;
import com.example.com.meimeng.activity.identify.IdentifyMarriageActivity;
import com.example.com.meimeng.activity.identify.IdentifyMoneyActivity;
import com.example.com.meimeng.activity.identify.IdentifyUserActivity;
import com.example.com.meimeng.activity.identify.WorkIdentifyActivity;
import com.example.com.meimeng.util.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2015/7/29.
 */
public class RegisterIdentify extends BaseActivity {

    @Bind(R.id.title_text)
    TextView titleText;

    @Bind(R.id.title_left_arrow_image_view)
    ImageView leftArrowImageView;

    @Bind(R.id.bow_arrow_image_view)
    ImageView bowArrowImageView;

    @Bind(R.id.title_sure_text)
    TextView sureText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.register_identify);
        ButterKnife.bind(this);
        MeiMengApplication.currentContext=this;
        MeiMengApplication.loginActivity.add(this);
        bowArrowImageView.setVisibility(View.GONE);
        leftArrowImageView.setVisibility(View.VISIBLE);
        titleText.setText("认证");
        sureText.setVisibility(View.GONE);
    }

    @OnClick(R.id.register_identify_user)
    void userIdentifyRow() {
        Intent intent = new Intent(RegisterIdentify.this, IdentifyUserActivity.class);
        startActivity(intent);

    }

    @OnClick(R.id.register_identify_money)
    void moneyIdentifyRow() {
        Intent intent = new Intent(RegisterIdentify.this, IdentifyMoneyActivity.class);
        startActivity(intent);

    }

    @OnClick(R.id.register_identify_mar)
    void marriageIdentifyRow() {
        Intent intent = new Intent(RegisterIdentify.this, IdentifyMarriageActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.register_identify_job)
    void jobIdentifyRow() {
        Intent intent = new Intent(RegisterIdentify.this, WorkIdentifyActivity.class);
        startActivity(intent);

    }

    @OnClick(R.id.register_identify_education)
    void educationIdentifyRow() {
        Intent intent = new Intent(RegisterIdentify.this, IdentifyEducationActivity.class);
        startActivity(intent);
    }


    //返回按钮
    @OnClick(R.id.title_left_arrow_layout)
    void leftArrowListener() {
        onBackPressed();
    }

    //完成
    @OnClick(R.id.register_identify_button)
    void registeridentifybuttonListener() {

        Utils.EndRegister(RegisterIdentify.this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MeiMengApplication.loginActivity.remove(this);
    }

}
