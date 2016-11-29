package com.example.com.meimeng.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.com.meimeng.MeiMengApplication;
import com.example.com.meimeng.R;
import com.example.com.meimeng.activity.GitManagerActivity;
import com.example.com.meimeng.activity.OfficeEventDetail;
import com.example.com.meimeng.activity.OthersSelfActivity;
import com.example.com.meimeng.activity.PrivateEventDetail;
import com.example.com.meimeng.activity.SixPersonDateDetail;
import com.example.com.meimeng.activity.VipActivity;
import com.example.com.meimeng.gson.GsonTools;
import com.example.com.meimeng.gson.gsonbean.OrderStateWeixinBean;
import com.example.com.meimeng.net.InternetConstant;
import com.example.com.meimeng.net.InternetUtils;
import com.example.com.meimeng.pay.PayConstants;
import com.example.com.meimeng.util.DialogUtils;
import com.example.com.meimeng.util.MessageUtils;
import com.example.com.meimeng.util.Utils;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONStringer;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

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
	
    private IWXAPI api;

	//记录支付结果
	private boolean payState = false;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vip_pay_result);
		ButterKnife.bind(this);
		MeiMengApplication.payButton.setBackgroundColor(getResources().getColor(R.color.gold_textcolor));
		MeiMengApplication.payButton.setClickable(true);

    	api = WXAPIFactory.createWXAPI(this, PayConstants.WEIXIN_APP_ID);
        api.handleIntent(getIntent(), this);

		bowArrowImageView.setVisibility(View.GONE);
		leftArrowImageView.setVisibility(View.VISIBLE);
		sureText.setVisibility(View.GONE);
		titleText.setText("支付状态");
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			 switch (resp.errCode){
				 case 0:
					 judeWeixinPayResult();
					 break;
				 case -1:
					 Toast.makeText(WXPayEntryActivity.this,"微信支付失败",Toast.LENGTH_LONG).show();
					 vipPayStateText.setVisibility(View.VISIBLE);
					 vipPayStateImage.setVisibility(View.VISIBLE);
					 vipPayStateButton.setVisibility(View.VISIBLE);
					 break;
				 case -2:
					 Toast.makeText(WXPayEntryActivity.this,"用户取消了支付",Toast.LENGTH_LONG).show();
					 vipPayStateText.setVisibility(View.VISIBLE);
					 vipPayStateImage.setVisibility(View.VISIBLE);
					 vipPayStateButton.setVisibility(View.VISIBLE);
					 break;
				 default:
					 break;

			 }
		}
	}

	//返回按钮
	@OnClick(R.id.title_left_arrow_layout)
	void leftArrowListener() {
		onBackPressed();
	}

	/**
	 * 判断微信支付的结果
	 */
	private void judeWeixinPayResult(){

		if (MeiMengApplication.weixinOutTradeNo==-1l){
			Toast.makeText(WXPayEntryActivity.this,"微信支付的订单号还没有下来",Toast.LENGTH_SHORT).show();
			vipPayStateText.setVisibility(View.VISIBLE);
			vipPayStateImage.setVisibility(View.VISIBLE);
			vipPayStateButton.setVisibility(View.VISIBLE);

			return;
		}
		try{
			if (TextUtils.isEmpty(Utils.getUserId())) {
				return;
			}
			if (TextUtils.isEmpty( Utils.getUserToken() )) {
				return;
			}
			String url = InternetConstant.SERVER_URL + InternetConstant.CONFIRM_WEIXIN_PAY_RESULT+"?uid="+ Utils.getUserId()+"&token="+Utils.getUserToken();
			JSONStringer stringer = new JSONStringer().object().key("out_trade_no").value(MeiMengApplication.weixinOutTradeNo).endObject();
		    String jsonStr = stringer.toString();
			Observable observable = InternetUtils.getJsonObservale(url,jsonStr);
			observable.observeOn(AndroidSchedulers.mainThread())
					.subscribe(new Action1() {
						@Override
						public void call(Object o) {
							OrderStateWeixinBean orderStateWeixinBean = GsonTools.getOrderStateWeixinBean((String)o);
							if (orderStateWeixinBean.isSuccess()){
								vipPayStateImage.setImageResource(R.drawable.pay_success_ico);
								vipPayStateImage.setVisibility(View.VISIBLE);
								vipPayStateText.setText("支付成功");
								vipPayStateText.setVisibility(View.VISIBLE);
								vipPayStateText.setTextColor(getResources().getColor(R.color.gold_textcolor));
								vipPayStateButton.setText("确定");
								vipPayStateButton.setVisibility(View.VISIBLE);
								payState = true;
								if (MeiMengApplication.weixinPayCallBack == 2 || MeiMengApplication.weixinPayCallBack == 4) {
									MeiMengApplication.attend.setText("已参加");
									MeiMengApplication.attend.setClickable(false);
								}
							}else {
								vipPayStateText.setVisibility(View.VISIBLE);
								vipPayStateImage.setVisibility(View.VISIBLE);
								vipPayStateButton.setVisibility(View.VISIBLE);
								DialogUtils.setDialog(WXPayEntryActivity.this,orderStateWeixinBean.getError());
							}
						}
					});

		}catch (Exception e){
			e.printStackTrace();
		}
	}

	//0---他人页面中送礼物(OthersSelfActivity)；1---我的礼物页面中送他人礼物(GitManagerActivity)；2---报名参加活动页面(OfficeEventDetail)；
	// 3---报名参加活动页面(PrivateEventDetail);4---报名参加活动页面(SixPersonDateDetail);5---会员等级界面
	@OnClick(R.id.vip_pay_result_button)
	void payResultButtonListener(){
		if (payState){
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
		}else {
			onBackPressed();
		}
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
		MessageUtils.setLeanCloudOtherUid(WXPayEntryActivity.this, Long.valueOf(otherId));

	}
	/**
	 * 启动回调
	 * @param cl
	 */
	private void callBackListener(Class cl){
		Intent intent = new Intent(WXPayEntryActivity.this,cl);
		startActivity(intent);
	}
}