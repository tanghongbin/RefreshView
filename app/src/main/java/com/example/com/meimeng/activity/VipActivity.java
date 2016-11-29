package com.example.com.meimeng.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.com.meimeng.MeiMengApplication;
import com.example.com.meimeng.R;
import com.example.com.meimeng.constants.CommonConstants;
import com.example.com.meimeng.custom.RoundCornerImageView;
import com.example.com.meimeng.gson.GsonTools;
import com.example.com.meimeng.gson.gsonbean.UserBaseInfoBean;
import com.example.com.meimeng.gson.gsonbean.UserBaseInfoItem;
import com.example.com.meimeng.gson.gsonbean.VipPriceListBean;
import com.example.com.meimeng.gson.gsonbean.VipPriceListItem;
import com.example.com.meimeng.net.InternetConstant;
import com.example.com.meimeng.net.InternetUtils;
import com.example.com.meimeng.util.DialogUtils;
import com.example.com.meimeng.util.Utils;

import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by 010 on 2015/7/20.
 */
public class VipActivity extends BaseActivity {
    @Bind(R.id.title_text)
    TextView titleText;

    @Bind(R.id.title_left_arrow_image_view)
    ImageView leftArrowImageView;

    @Bind(R.id.bow_arrow_image_view)
    ImageView bowArrowImageView;

    @Bind(R.id.title_sure_text)
    TextView sureText;

    @Bind(R.id.vid_center_content)
    LinearLayout vid_center_content;
    @Bind(R.id.text_mormal)
    TextView text_mormal;

  /*  @Bind(R.id.vid_center_content2)
    LinearLayout vid_center_content2;*/

    @Bind(R.id.vid_center_content3)
    LinearLayout vid_center_content3;

   /* @Bind(R.id.vid_center_content4)
    LinearLayout vid_center_content4;*/

    @Bind(R.id.vip_center_btn1)
    ImageView vip_center_btn1;

  /*  @Bind(R.id.vip_center2_btn1)
    ImageView vip_center2_btn1;*/

    @Bind(R.id.vip_center3_btn1)
    ImageView vip_center3_btn1;

   /* @Bind(R.id.vip_center4_btn1)
    ImageView vip_center4_btn1;*/

    @Bind(R.id.vip_center_re)
    RelativeLayout vip_center_re;

   /* @Bind(R.id.vip_center_re2)
    RelativeLayout vip_center_re2;*/

    @Bind(R.id.vip_center_re3)
    RelativeLayout vip_center_re3;

   /* @Bind(R.id.vip_center_re2)
    RelativeLayout vip_center_re4;*/

    @Bind(R.id.vip_center_header)
    RoundCornerImageView vip_center_header;

    @Bind(R.id.vip_center_icon)
    ImageView vip_center_icon;

    @Bind(R.id.vip_center_user_name)
    TextView vip_center_user_name;

    @Bind(R.id.vip_center_level)
    TextView vip_center_level;

  /*  @Bind(R.id.vip_center2_buy_button)
    Button vipcenter2buybutton;*/

    @Bind(R.id.vip_center3_buy_button)
    Button vipcenter3buybutton;

  /*  @Bind(R.id.vip_center4_buy_button)
    Button vipcenter4buybutton;*/

    private int AClick = 0;
    private int BClick = 0;
    private int CClick = 0;
    private int DClick = 0;
    //存放会员等级的价格
    private SharedPreferences.Editor editor;
    private HashMap<Integer, Long> vipPriceHashMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.vip_center);
        ButterKnife.bind(this);

        MeiMengApplication.currentContext = this;
        editor = MeiMengApplication.sharedPreferences.edit();
        //获取会员等级id
        getVipPriceId();

        bowArrowImageView.setVisibility(View.GONE);
        leftArrowImageView.setVisibility(View.VISIBLE);

        sureText.setVisibility(View.GONE);
        titleText.setText("会员服务");
        String input = "1，\t免费使用美盟会员专属APP\n—您通过审核，即有权限进入美盟会员专属APP，享受基本会员权益；\n—您可以通过完善基本资料并上传6张真实照片获得3次聊天机会；\n—您每邀请一位好友即刻获得2次聊天机会；\n—您可以接受其他优质会员向您发起的聊天申请，免费相识；\n2，免费获得私人推荐服务\n美盟私人红娘会根据您自身条件和择偶需求进行免费推荐，并为您建立详细婚恋需求档案，免费进行专业分析和婚恋指导。\n3，免费参加美盟高端活动\n成为美盟会员，您将有机会受邀参加美盟各种高端派对、品质沙龙、品牌活动等会员回馈活动。";
        String cc = ToDBC(input);
        text_mormal.setText(cc);

        initView();//初始化视图
        //根据等级设置购买按钮的颜色和是否能点击
        switch (MeiMengApplication.sharedPreferences.getInt(CommonConstants.USER_LEVEL, 0)) {
            case 1:
             /*   vipcenter2buybutton.setBackgroundDrawable(getResources().getDrawable(R.drawable.buttonstyle_normal));
                vipcenter2buybutton.setTextColor(getResources().getColor(R.color.text_gray));
                vipcenter2buybutton.setClickable(false);*/
                break;
            case 2:
              /*  vipcenter2buybutton.setBackgroundDrawable(getResources().getDrawable(R.drawable.buttonstyle_normal));
                vipcenter2buybutton.setTextColor(getResources().getColor(R.color.text_gray));
                vipcenter2buybutton.setClickable(false);*/
                vipcenter3buybutton.setBackgroundDrawable(getResources().getDrawable(R.drawable.buttonstyle_normal));
                vipcenter3buybutton.setTextColor(getResources().getColor(R.color.text_gray));
                vipcenter3buybutton.setClickable(false);
                break;
            case 3:
              /*  vipcenter2buybutton.setBackgroundDrawable(getResources().getDrawable(R.drawable.buttonstyle_normal));
                vipcenter2buybutton.setTextColor(getResources().getColor(R.color.text_gray));
                vipcenter2buybutton.setClickable(false);*/
                vipcenter3buybutton.setBackgroundDrawable(getResources().getDrawable(R.drawable.buttonstyle_normal));
                vipcenter3buybutton.setTextColor(getResources().getColor(R.color.text_gray));
                vipcenter3buybutton.setClickable(false);
               /* vipcenter4buybutton.setBackgroundDrawable(getResources().getDrawable(R.drawable.buttonstyle_normal));
                vipcenter4buybutton.setTextColor(getResources().getColor(R.color.text_gray));
                vipcenter4buybutton.setClickable(false);*/
                break;
            default:
                break;
        }
    }


    public static String ToDBC(String str) {
        char[] c = str.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    //返回按钮
    @OnClick(R.id.title_left_arrow_layout)
    void leftArrowListener() {
        onBackPressed();
    }

    /**
     * 点击出现或者消失具体内容
     */
    @OnClick(R.id.vip_center_re)
    void ARexpandBtnListener() {

        if (AClick == 0) {

            AClick = 1;
            vid_center_content.setVisibility(View.VISIBLE);
            vip_center_btn1.setImageResource(R.drawable.btn_uparrow_2);

        } else {

            AClick = 0;
            vid_center_content.setVisibility(View.GONE);
            vip_center_btn1.setImageResource(R.drawable.btn_downarrow_2);
        }

    }

   /* @OnClick(R.id.vip_center_re2)
    void BRexpandBtnListener() {

        if (BClick == 0) {

            BClick = 1;
          *//*  vid_center_content2.setVisibility(View.VISIBLE);
            vip_center2_btn1.setImageResource(R.drawable.btn_uparrow_2);*//*

        } else {

            BClick = 0;
           *//* vid_center_content2.setVisibility(View.GONE);
            vip_center2_btn1.setImageResource(R.drawable.btn_downarrow_2);*//*
        }

    }*/

    @OnClick(R.id.vip_center_re3)
    void CRexpandBtnListener() {

        if (CClick == 0) {

            CClick = 1;
            vid_center_content3.setVisibility(View.VISIBLE);
            vip_center3_btn1.setImageResource(R.drawable.btn_uparrow_2);

        } else {

            CClick = 0;
            vid_center_content3.setVisibility(View.GONE);
            vip_center3_btn1.setImageResource(R.drawable.btn_downarrow_2);
        }

    }
/*
    @OnClick(R.id.vip_center_re4)
    void DRexpandBtnListener() {

        if (DClick == 0) {

            DClick = 1;
           *//* vid_center_content4.setVisibility(View.VISIBLE);
            vip_center4_btn1.setImageResource(R.drawable.btn_uparrow_2);*//*

        } else {

            DClick = 0;
           *//* vid_center_content4.setVisibility(View.GONE);
            vip_center4_btn1.setImageResource(R.drawable.btn_downarrow_2);*//*
        }



    }*/

    /**
     * 初始化视图
     */
    private void initView() {

        initContentLayout();

        try {
            //获取用户的uid,和token
            if (TextUtils.isEmpty(Utils.getUserId())) {
                return;
            }
            if (TextUtils.isEmpty(Utils.getUserToken())) {
                return;
            }
            String url = InternetConstant.SERVER_URL + InternetConstant.GET_USER_BASE_INFO_URL + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();
            JSONStringer stringer = new JSONStringer().object().key("targetUid").value(Utils.getUserId()).endObject();
            String jsonStr = stringer.toString();
            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1() {
                        @Override
                        public void call(Object o) {
                            UserBaseInfoBean baseInfoBean = GsonTools.getUserBaseInfoBean((String) o);
                            if (baseInfoBean.isSuccess()) {

                                UserBaseInfoItem item = baseInfoBean.getParam().getUserSimpleInfo();
                                StringBuffer sb = new StringBuffer();

                                if (!TextUtils.isEmpty(item.getFirstName())) {
                                    sb.append(item.getFirstName());
                                }
                               /* if(!TextUtils.isEmpty(item.getLastName())){
                                    sb.append(item.getLastName());
                                }*/
                                vip_center_user_name.setText(sb.toString() + (item.getSex() == 0 ? "先生" : "女士"));
                                editor.putInt(CommonConstants.USER_LEVEL, baseInfoBean.getParam().getUserSimpleInfo().getLevel()).commit();

                                switch (MeiMengApplication.sharedPreferences.getInt(CommonConstants.USER_LEVEL, 0)) {
                                    case 0:
                                        vip_center_level.setText("普通会籍");
                                        vip_center_icon.setImageResource(R.drawable.vip_0);
                                        break;
                                    /*case 1:
                                        vip_center_level.setText("银牌会籍");
                                        vip_center_icon.setImageResource(R.drawable.vip_1);
                                        break;*/
                                    case 2:
                                        vip_center_level.setText("金牌会籍");
                                        vip_center_icon.setImageResource(R.drawable.vip_2);
                                        break;
                                    /*case 3:
                                        vip_center_level.setText("黑牌会籍");
                                        vip_center_icon.setImageResource(R.drawable.vip_3);
                                        break;*/
                                    default:
                                        break;

                                }
                                InternetUtils.getPicIntoView(200, 200, vip_center_header, baseInfoBean.getParam().getUserSimpleInfo().getHeadPic(), true);
                                vip_center_header.setAngie(20f, 20f);

                            } else {
                                DialogUtils.setDialog(VipActivity.this, baseInfoBean.getError());
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            Log.e("获取用户基本信息失败了", throwable.getMessage());
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   /* *//**
     * 购买金牌
     *//*
    @OnClick(R.id.vip_center2_buy_button)
    void buyCenter2Vip() {
        buyCenter2VipListener();
    }*/

    /**
     * 购买金牌
     */
    @OnClick(R.id.vip_center3_buy_button)
    void buyCenter3Vip() {
        buyCenter3VipListener();
    }

   /* *//**
     * 购买黑牌
     *//*
    @OnClick(R.id.vip_center4_buy_button)
    void buyCenter4Vip() {
        buyCenter4VipListener();
    }*/

    /**
     * 购买银牌会员
     */
    private void buyCenter2VipListener() {
        MeiMengApplication.weixinPayCallBack = 5;
        Intent intent = new Intent(VipActivity.this, PayActivity.class);
        buyIntent(intent, "美盟银牌会籍6个月", "1998", vipPriceHashMap.get(0));
    }

    /**
     * 购买金牌会员
     */
    private void buyCenter3VipListener() {
        MeiMengApplication.weixinPayCallBack = 5;
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:400-8783-520"));
        startActivity(intent);
        finish();
       // DialogUtils.vipDialog(VipActivity.this);
       /* Intent intent = new Intent(VipActivity.this, PayActivity.class);
        buyIntent(intent, "美盟金牌会籍一年", "20000", vipPriceHashMap.get(1));*/
    }

    /**
     * 购买黑牌会员
     */
    private void buyCenter4VipListener() {
        MeiMengApplication.weixinPayCallBack = 5;
        Intent intent = new Intent(VipActivity.this, PayActivity.class);
        buyIntent(intent, "美盟黑牌会籍一年", "10W", vipPriceHashMap.get(2));
    }

    /**
     * 参数传递
     */
    private void buyIntent(Intent intent, String string, String value, Long value2) {
        MeiMengApplication.payTitle = string;
        MeiMengApplication.payGoodsId = value2;
        if (value.equals("10W")) {
            value = "100000";
        }
        MeiMengApplication.payPrice = Double.parseDouble(value);
        intent.putExtra("name", string);
        intent.putExtra("price", value);
        intent.putExtra("goodId", value2);
        intent.putExtra("pay_type", 1);
        startActivity(intent);
        finish();
    }

    /**
     * 获取会员等级id
     */
    private void getVipPriceId() {
        try {
            //获取用户的uid,和token
            if (TextUtils.isEmpty(Utils.getUserId())) {
                return;
            }
            if (TextUtils.isEmpty(Utils.getUserToken())) {
                return;
            }
            String url = InternetConstant.SERVER_URL + InternetConstant.VIP_PRICE_LIST + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();
            Observable observable = InternetUtils.getJsonObservale(url, "");
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1() {
                        @Override
                        public void call(Object o) {
                            VipPriceListBean vipPriceListBean = GsonTools.getVipPriceListBean((String) o);
                            if (vipPriceListBean.isSuccess()) {
                                ArrayList<VipPriceListItem> vipList = vipPriceListBean.getParam().getVip();
                                for (int i = 0; i < vipList.size(); i++) {
                                    VipPriceListItem vipItem = vipList.get(i);
                                    vipPriceHashMap.put(i, vipItem.getGoodId());
                                }
                            } else {
                                DialogUtils.setDialog(VipActivity.this, vipPriceListBean.getError());
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化内容layout
     */
    private void initContentLayout() {
        vid_center_content.setVisibility(View.GONE);
        //vid_center_content2.setVisibility(View.GONE);
        vid_center_content3.setVisibility(View.GONE);
        // vid_center_content4.setVisibility(View.GONE);

    }
}
