package com.example.com.meimeng.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.com.meimeng.MeiMengApplication;
import com.example.com.meimeng.R;
import com.example.com.meimeng.util.MessageUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 010 on 2015/8/14.
 */
public class ZhifubaoPayResultActivity extends BaseActivity {

    @Bind(R.id.title_text)
    TextView titleText;

    @Bind(R.id.title_left_arrow_image_view)
    ImageView leftArrowImageView;

    @Bind(R.id.bow_arrow_image_view)
    ImageView bowArrowImageView;

    @Bind(R.id.title_sure_text)
    TextView sureText;

    @Bind(R.id.vip_pay_result_state_image)
    ImageView vipPayStateImage;

    @Bind(R.id.vip_pay_result_state_text)
    TextView vipPayStateText;

    @Bind(R.id.vip_pay_result_button)
    Button vipPayStateButton;


    //记录支付宝支付结果
    private boolean payState = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vip_pay_result);
        ButterKnife.bind(this);

        MeiMengApplication.currentContext=this;
        bowArrowImageView.setVisibility(View.GONE);
        leftArrowImageView.setVisibility(View.VISIBLE);

        sureText.setVisibility(View.GONE);
        titleText.setText("支付状态");

        initView();
    }

    //返回按钮
    @OnClick(R.id.title_left_arrow_layout)
    void leftArrowListener() {
        onBackPressed();
    }

    /**
     * 根据支付结果初始化界面
     */
    private void initView() {
        Intent intent = getIntent();
        payState = intent.getBooleanExtra("isSuccess", false);
        MeiMengApplication.payButton.setBackgroundColor(getResources().getColor(R.color.gold_textcolor));
        MeiMengApplication.payButton.setClickable(true);
        if (payState) {
            vipPayStateImage.setImageResource(R.drawable.pay_success_ico);
            vipPayStateImage.setVisibility(View.VISIBLE);
            vipPayStateText.setText("支付成功");
            vipPayStateText.setVisibility(View.VISIBLE);
            vipPayStateText.setTextColor(getResources().getColor(R.color.gold_textcolor));
            vipPayStateButton.setText("确定");
            vipPayStateButton.setVisibility(View.VISIBLE);
            if (MeiMengApplication.weixinPayCallBack == 2 || MeiMengApplication.weixinPayCallBack == 4) {
                MeiMengApplication.attend.setText("已参加");
                MeiMengApplication.attend.setClickable(false);
            }
        } else {
            vipPayStateImage.setImageResource(R.drawable.pay_failure_ico);
            vipPayStateText.setText("支付失败");
            vipPayStateText.setTextColor(getResources().getColor(R.color.text_gray));
            vipPayStateButton.setText("重新支付");
            vipPayStateImage.setVisibility(View.VISIBLE);
            vipPayStateText.setVisibility(View.VISIBLE);
            vipPayStateButton.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.vip_pay_result_button)
    void payResultButtonListener() {
        if (payState) {
            switch (MeiMengApplication.weixinPayCallBack) {
                case 0:
                    callBackListener(OthersSelfActivity.class);
                    break;
                case 1:
                    callBackListener(GitManagerActivity.class);
                    break;
                case 2:
                    callBackListener(OfficeEventDetail.class);
                    break;
                case 3:
                    callBackListener(PrivateEventDetail.class);
                    break;
                case 4:
                    callBackListener(SixPersonDateDetail.class);
                    break;
                case 5:
                    callBackListener(VipActivity.class);
                    break;
                case 6:
                    chatGiveGiftResult();
                    finish();
                    break;
                default:
                    break;
            }
        } else {
            Intent intent = new Intent(ZhifubaoPayResultActivity.this, PayActivity.class);
            switch (MeiMengApplication.weixinPayCallBack) {
                case 0://他人、聊天界面送礼支付
                case 1:
                case 6:
                    intent.putExtra("name", MeiMengApplication.payTitle);
                    intent.putExtra("goodId",MeiMengApplication.payGoodsId);
                    intent.putExtra("targetUid", MeiMengApplication.payGoodsPicId);
                    intent.putExtra("price", "" + MeiMengApplication.payPrice);
                    break;
                case 2://个人活动重新支付
                case 3://活动详情支付
                case 4:
                    intent.putExtra("name", MeiMengApplication.payTitle);
                    intent.putExtra("goodId", -2l);
                    intent.putExtra("targetUid",MeiMengApplication.payActivityId);
                    intent.putExtra("price", "" + MeiMengApplication.payPrice);
                    intent.putExtra("pay_type", 3);
                    break;
                case 5:
                    intent.putExtra("name",  MeiMengApplication.payTitle);
                    intent.putExtra("price", MeiMengApplication.payPrice+"");
                    intent.putExtra("goodId", MeiMengApplication.payGoodsId);
                    intent.putExtra("pay_type", 1);
                    break;

                default:
                    break;
            }
            startActivity(intent);
            finish();
            //onBackPressed();
        }
    }

    /**
     * 启动回调
     *
     * @param cl
     */
    private void callBackListener(Class cl) {
        Intent intent = new Intent(ZhifubaoPayResultActivity.this, cl);
        startActivity(intent);
        finish();
    }

    /**
     * 礼物赠送结果
     */
    private void chatGiveGiftResult(){

        SharedPreferences.Editor editor = MeiMengApplication.sharedPreferences.edit();
        //赠送成功
        if (payState){
            editor.putInt("chat_flag",1);
        }
        //赠送失败
        else {
            editor.putInt("chat_flag",-1);
        }
        editor.commit();

        String otherId = MeiMengApplication.sharedPreferences.getString("chat_other_id","");

        MessageUtils.setLeanCloudSelfUid();
        MessageUtils.setLeanCloudOtherUid(ZhifubaoPayResultActivity.this, Long.valueOf(otherId));

    }
}
