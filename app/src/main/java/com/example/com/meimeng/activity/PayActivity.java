package com.example.com.meimeng.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.com.meimeng.MeiMengApplication;
import com.example.com.meimeng.R;
import com.example.com.meimeng.pay.PayConstants;
import com.example.com.meimeng.pay.PayUtil;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 010 on 2015/8/14.
 */
public class PayActivity extends Activity {

    @Bind(R.id.title_text)
    TextView titleText;

    @Bind(R.id.title_left_arrow_image_view)
    ImageView leftArrowImageView;

    @Bind(R.id.bow_arrow_image_view)
    ImageView bowArrowImageView;

    @Bind(R.id.title_sure_text)
    TextView sureText;

    @Bind(R.id.pay_vip_type_text)
    TextView vipNameText;

    @Bind(R.id.pay_vip_price_text)
    TextView vipPriceText;

    @Bind(R.id.pay_vip_zhifubao_pay_image)
    ImageView zhifubaoPayImage;

    @Bind(R.id.pay_vip_weixin_pay_image)
    ImageView weixinPayImage;

    //记录支付的种类：0---支付宝，1---微信
    private int payType = 0;

    //微信api
    private IWXAPI weixinapi;

    //商品id
    private long goodId = -1l;

    //第三方的uid
    private long targetUid = -1l;

    //支付类型:活动、礼物、会员
    private int payTypeNum = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_vip_layout);
        ButterKnife.bind(this);
        MeiMengApplication.currentContext=this;
        MeiMengApplication.payButton=(Button)findViewById(R.id.pay_vip_pay_button);
        bowArrowImageView.setVisibility(View.GONE);
        leftArrowImageView.setVisibility(View.VISIBLE);

        sureText.setVisibility(View.GONE);
        titleText.setText("支付");

        Intent intent = getIntent();
        String nameStr = intent.getStringExtra("name");
        if (nameStr.length() > 16) {
            StringBuffer sb = new StringBuffer();
            sb.append(nameStr.substring(0, 16)).append("\n").append(nameStr.substring(16));
            vipNameText.setText(sb.toString());
        } else {
            vipNameText.setText(intent.getStringExtra("name"));
        }

        vipPriceText.setText(intent.getStringExtra("price"));
        goodId = intent.getLongExtra("goodId", -1l);
        targetUid = intent.getLongExtra("targetUid", -1l);
        payTypeNum = intent.getIntExtra("pay_type", -1);

        //微信api
        weixinapi = WXAPIFactory.createWXAPI(this, PayConstants.WEIXIN_APP_ID);

    }

    //返回按钮
    @OnClick(R.id.title_left_arrow_layout)
    void leftArrowListener() {
        onBackPressed();
    }

    /**
     * 支付宝支付
     */
    @OnClick(R.id.zhifubao_pay_layout)
    void zhifubaoPayListener() {
        zhifubaoPayImage.setVisibility(View.VISIBLE);
        weixinPayImage.setVisibility(View.GONE);
        payType = 0;
    }

    /**
     * 微信支付
     */
    @OnClick(R.id.weixin_pay_layout)
    void weixinPayListener() {
        zhifubaoPayImage.setVisibility(View.GONE);
        weixinPayImage.setVisibility(View.VISIBLE);
        payType = 1;
    }

    /**
     * 支付按钮
     */
    @OnClick(R.id.pay_vip_pay_button)
    void payListener() {
        //-1l 代表不需要传此参数给服务端
        switch (payType) {
            //支付宝支付
            case 0:
                PayUtil.zhifubaoPay(PayActivity.this, goodId, targetUid, payTypeNum);
                //点击后支付按钮变灰色并且不能点击，避免重复点击
                MeiMengApplication.payButton.setBackgroundColor(getResources().getColor(R.color.text_gray));
                MeiMengApplication.payButton.setClickable(false);

                break;
            //微信支付
            case 1:
                //1--代表开通会员
                PayUtil.weiXinPay(PayActivity.this, goodId, targetUid, payTypeNum);
                MeiMengApplication.payButton.setBackgroundColor(getResources().getColor(R.color.text_gray));
                MeiMengApplication.payButton.setClickable(false);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
