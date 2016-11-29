package com.example.com.meimeng.activity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.com.meimeng.MeiMengApplication;
import com.example.com.meimeng.R;
import com.example.com.meimeng.util.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2015/8/1.
 */
public class AboutMeimeng extends BaseActivity {
    @Bind(R.id.title_text)
    TextView titleText;

    @Bind(R.id.title_left_arrow_image_view)
    ImageView leftArrowImageView;

    @Bind(R.id.bow_arrow_image_view)
    ImageView bowArrowImageView;
    @Bind(R.id.aboutmeimeng_pic)
    ImageView aboutmeimeng_pic;
    @Bind(R.id.aboutmeimeng_pic1)
    ImageView aboutmeimeng_pic1;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.about_meimeng);
        ButterKnife.bind(this);
        MeiMengApplication.currentContext=this;
        titleText.setText("关于我们");
        leftArrowImageView.setVisibility(View.VISIBLE);
        bowArrowImageView.setVisibility(View.GONE);


        Utils.readBitMap(AboutMeimeng.this, aboutmeimeng_pic, R.raw.pic_about);
        Utils.readBitMap(AboutMeimeng.this, aboutmeimeng_pic1, R.raw.aboutmm);

    }

    @OnClick(R.id.title_left_arrow_image_view)
    void leftArrowListener() {
        super.onBackPressed();
    }
}
