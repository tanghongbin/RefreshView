package com.example.com.meimeng.activity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.com.meimeng.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ServiceTermActivity extends BaseActivity {


    @Bind(R.id.title_text)
    TextView titleText;

    @Bind(R.id.title_left_arrow_image_view)
    ImageView leftArrowImageView;

    @Bind(R.id.bow_arrow_image_view)
    ImageView bowArrowImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_service_term);
        ButterKnife.bind(this);
        titleText.setText("服务条款");
        leftArrowImageView.setVisibility(View.VISIBLE);
        bowArrowImageView.setVisibility(View.GONE);
        leftArrowImageView.setOnClickListener(nClickListener);
    }

    private  View.OnClickListener nClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onBackPressed();
        }
    };



}
