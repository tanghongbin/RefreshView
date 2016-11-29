package com.example.com.meimeng.activity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.com.meimeng.MeiMengApplication;
import com.example.com.meimeng.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2015/8/6.
 */
public class ServiceClause extends BaseActivity {
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
        setContentView(R.layout.service_clause);

        ButterKnife.bind(this);

        MeiMengApplication.currentContext=this;
        bowArrowImageView.setVisibility(View.GONE);
        leftArrowImageView.setVisibility(View.VISIBLE);

        sureText.setVisibility(View.GONE);
        titleText.setText("服务条款");

    }
    //返回按钮
    @OnClick(R.id.title_left_arrow_layout)
    void leftArrowListener() {
        onBackPressed();
    }
}
