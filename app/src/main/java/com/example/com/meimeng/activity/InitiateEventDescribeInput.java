package com.example.com.meimeng.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.com.meimeng.MeiMengApplication;
import com.example.com.meimeng.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2015/7/23.
 */
public class InitiateEventDescribeInput extends BaseActivity {


    private EditText describe = null;

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
        setContentView(R.layout.event_initiate_describeinput);
        ButterKnife.bind(this);

        MeiMengApplication.currentContext=this;
        describe = (EditText) findViewById(R.id.event_initiate_describeinput_text);

        bowArrowImageView.setVisibility(View.GONE);
        leftArrowImageView.setVisibility(View.VISIBLE);
        Intent intent = getIntent();
        String titleName = intent.getStringExtra("type");
        titleText.setText(titleName);

        int hintType = intent.getIntExtra("hint_type",-1);
        if (hintType==1){
            describe.setText(intent.getStringExtra("content"));
        }else if (hintType == 0){
            describe.setHint(intent.getStringExtra("content"));
        }
        sureText.setVisibility(View.VISIBLE);
        sureText.setText("确认");
        sureText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                String getDescribe = describe.getText().toString();
                intent.putExtra("des",getDescribe);
                setResult(1,intent);
                finish();
            }
        });

    }//返回按钮
    @OnClick(R.id.title_left_arrow_layout)

    void back(){
        this.finish();
    }

}
