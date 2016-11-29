package com.example.com.meimeng.pay;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.example.com.meimeng.MeiMengApplication;
import com.example.com.meimeng.activity.ZhifubaoPayResultActivity;
import com.example.com.meimeng.gson.GsonTools;
import com.example.com.meimeng.gson.gsonbean.WeiXinPayBean;
import com.example.com.meimeng.gson.gsonbean.WeiXinPayItem;
import com.example.com.meimeng.gson.gsonbean.ZhifubaoResultBean;
import com.example.com.meimeng.net.InternetConstant;
import com.example.com.meimeng.net.InternetUtils;
import com.example.com.meimeng.util.Utils;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONStringer;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by 010 on 2015/8/13.
 */
public class PayUtil {

    /**
     * 支付宝的支付功能
     *
     * @param context   上下文
     * @param goodsId   商品id
     * @param targetUid 目标用户
     * @param payType   订单类型：活动、礼物、会员
     * @return
     */
    public static void zhifubaoPay(final Context context, long goodsId, long targetUid, int payType) {
        //调一个接口，返回一字符串，即完整的符合支付宝参数规范的订单信息
        try {
            if (TextUtils.isEmpty(Utils.getUserId())) {
                return;
            }
            if (TextUtils.isEmpty(Utils.getUserToken())) {
                return;
            }
            String url = InternetConstant.SERVER_URL + InternetConstant.PAY_GETPREORDER + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();
            //0代表支付宝
            String jsonStr = setRequestData(goodsId, targetUid, payType, 0);
            Observable observable = InternetUtils.getZhifubaoObserable(context, url, jsonStr);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1() {
                        @Override
                        public void call(Object o) {
                            ZhifubaoResultBean zhifubaoResultBean = GsonTools.getZhifubaoResultBean((String) o);
                            Intent intent = new Intent(context, ZhifubaoPayResultActivity.class);
                            intent.putExtra("isSuccess", zhifubaoResultBean.isSuccess());
                            context.startActivity(intent);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 微信支付功能
     *
     * @param context
     * @param goodsId   商品id
     * @param targetUid 目标id: 送礼物的时候，是目标用户id，报名活动的时候，是活动id
     * @param type      类型：1:开通会员 2：送礼物 3：报名活动
     * @return
     */
    public static void weiXinPay(final Context context, long goodsId, long targetUid, int type) {
        try {
            if (TextUtils.isEmpty(Utils.getUserId())) {
                return;
            }
            if (TextUtils.isEmpty(Utils.getUserToken())) {
                return;
            }
            String url = InternetConstant.SERVER_URL + InternetConstant.PAY_GETPREORDER + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();

            //1-微信
            String jsonStr = setRequestData(goodsId, targetUid, type, 1);

            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1() {
                        @Override
                        public void call(Object o) {
                            WeiXinPayBean weiXinPayBean = GsonTools.getWeiXinPayBean((String) o);
                            String s = (String) o;
                            Log.e("json：", s);
                            final IWXAPI msgApi = WXAPIFactory.createWXAPI(context, null);
                            msgApi.registerApp(PayConstants.WEIXIN_APP_ID);

                            PayReq req = new PayReq();

                            //构造参数
                            WeiXinPayItem weiXinPayItem = weiXinPayBean.getParam().getPreOrder();
                            req.appId = PayConstants.WEIXIN_APP_ID;
                            req.partnerId = weiXinPayItem.getPartnerid();
                            req.prepayId = weiXinPayItem.getPrepayid();
                            req.packageValue = weiXinPayItem.getPackAge();
                            req.nonceStr = weiXinPayItem.getNoncestr();
                            req.timeStamp = weiXinPayItem.getTimestamp();
                            req.sign = weiXinPayItem.getSign();

                            MeiMengApplication.weixinOutTradeNo = weiXinPayBean.getParam().getOutTradeNo();
                            Log.e("订单号：", MeiMengApplication.weixinOutTradeNo + "");

                            //发起微信支付
                            msgApi.sendReq(req);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 得到订单参数
     *
     * @param goodsId    商品id
     * @param targetUid  目标id
     * @param type       商品类型：1:开通会员 2：送礼物 3：报名活动
     * @param payChannel 支付渠道：0：支付宝 1：微信
     * @return
     */
    private static String setRequestData(long goodsId, long targetUid, int type, int payChannel) {

        try {
            JSONStringer stringer;

            //购买会员时
            if (targetUid == -1l) {
                stringer = new JSONStringer().object()
                        .key("type").value(type)
                        .key("goodId").value(goodsId)
                        .key("payChannel").value(payChannel)
                        .endObject();

            } else {
                // 报名活动
                if (goodsId == -2l) {
                    stringer = new JSONStringer().object()
                            .key("thirdId").value(targetUid)
                            .key("type").value(type)
                            .key("payChannel").value(payChannel)
                            .endObject();
                }
                // 送礼物
                else {
                    stringer = new JSONStringer().object()
                            .key("thirdId").value(targetUid)
                            .key("goodId").value(goodsId)
                            .key("type").value(type)
                            .key("payChannel").value(payChannel)
                            .endObject();
                }
            }
            String jsonStr = stringer.toString();

            return jsonStr;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
