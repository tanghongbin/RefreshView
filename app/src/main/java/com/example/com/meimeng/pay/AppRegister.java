package com.example.com.meimeng.pay;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class AppRegister extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		final IWXAPI api = WXAPIFactory.createWXAPI(context, PayConstants.WEIXIN_APP_ID);

		// 将该app注册到微信
		api.registerApp(PayConstants.WEIXIN_APP_ID);
	}
}
