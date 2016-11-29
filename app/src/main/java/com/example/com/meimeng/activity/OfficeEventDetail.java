package com.example.com.meimeng.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.LogUtil;
import com.example.com.meimeng.MeiMengApplication;
import com.example.com.meimeng.R;
import com.example.com.meimeng.adapter.weblistAdapter;
import com.example.com.meimeng.bean.OfficePriceBean;
import com.example.com.meimeng.constants.CommonConstants;
import com.example.com.meimeng.custom.MyWebView;
import com.example.com.meimeng.gson.GsonTools;
import com.example.com.meimeng.gson.gsonbean.BaseBean;
import com.example.com.meimeng.gson.gsonbean.EventDetail;
import com.example.com.meimeng.gson.gsonbean.MyBaseInfoBean;
import com.example.com.meimeng.net.BuildString;
import com.example.com.meimeng.net.InternetConstant;
import com.example.com.meimeng.net.InternetUtils;
import com.example.com.meimeng.pay.PayConstants;
import com.example.com.meimeng.util.DialogUtils;
import com.example.com.meimeng.util.ProcessPhoto;
import com.example.com.meimeng.util.Utils;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONStringer;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by Administrator on 2015/7/14.
 */
public class OfficeEventDetail extends BaseActivity implements View.OnClickListener, IWXAPIEventHandler {
    //活动ID
    private long mActivityId;
    //活动类型


    //活动名称
    private String name = "";

    //活动图片id
    private long activityPicId = 0l;

    //参加此活动需要的钱
    private double activityPrice = 0;

    //详情地址显示状态
    private int placeDetailShowFlg = 0;

    private LinearLayout share_menu;

    private ArrayList<String> urls = new ArrayList<>();
    private weblistAdapter adapter;
    private LinearLayout list;

    //活动图片的url
    private String activityPicUrl = "";
    private TextView allNum;
    private String maxNum;

    //活动的链接
    private String activityUrl = "";

    private String str;//短信
    //qq实现类
    private Tencent mTencent;

    //微信分享api
    private IWXAPI weixinShareapi;

    @Bind(R.id.office_event_detail_sc)
    ScrollView scroll;
    //qq分享的回调接口
    IUiListener qqShareListener = new IUiListener() {
        @Override
        public void onCancel() {

        }

        @Override
        public void onComplete(Object response) {
            // TODO Auto-generated method stub
            Toast.makeText(OfficeEventDetail.this, "分享成功", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onError(UiError e) {
            // TODO Auto-generated method stub
            Toast.makeText(OfficeEventDetail.this, e.errorMessage, Toast.LENGTH_LONG).show();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.office_event_detail_final);
        ButterKnife.bind(this);
        MeiMengApplication.currentContext = this;

        initTitleBar("活动详情", R.drawable.icon_nav_back, R.drawable.invite_share, this);
        Intent intent = getIntent();
        mActivityId = intent.getExtras().getLong("mActivityId");

        // Tencent类是SDK的主要实现类，开发者可通过Tencent类访问腾讯开放的OpenAPI。
        // 其中APP_ID是分配给第三方应用的appid，类型为String。
        mTencent = Tencent.createInstance(CommonConstants.QQ_APP_ID, this.getApplicationContext());

        //微信分享api
        weixinShareapi = WXAPIFactory.createWXAPI(this, PayConstants.WEIXIN_APP_ID);
        weixinShareapi.handleIntent(getIntent(), this);
        //加载数据
        init();

    }


    private void setPriceDb(EventDetail eventDetail) {
        DbUtils dbUtils = DbUtils.create(OfficeEventDetail.this, "Price.db");

        OfficePriceBean officePriceBean = new OfficePriceBean();
        officePriceBean.setPrice(eventDetail.getPrice());
        officePriceBean.setTargetUid(eventDetail.getActivityId());
        try {
            dbUtils.save(officePriceBean);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载数据
     */
    void init() {

        //活动链接
        activityUrl = InternetConstant.SERVER_URL + InternetConstant.SHARE_ACTIVITY + "?activityId=" + mActivityId;

        final ImageView post = (ImageView) findViewById(R.id.office_event_detail_image);
        scroll.smoothScrollTo(0, 0);
        final TextView title = (TextView) findViewById(R.id.office_event_detail_title);
//        final TextView state = (TextView) findViewById(R.id.office_event_detail_state);
        final TextView place = (TextView) findViewById(R.id.office_event_detail_text_location);
        final TextView placedetail = (TextView) findViewById(R.id.office_event_detail_text_PlaceDetail);

        final TextView time = (TextView) findViewById(R.id.office_detail_date);
        allNum = (TextView) findViewById(R.id.office_detail_num);
//        final TextView describe = (TextView) findViewById(R.id.event_detail_describe);
//        final TextView requirment = (TextView) findViewById(R.id.event_detail_requirment);
        final TextView cost = (TextView) findViewById(R.id.office_detail_cost_text);
//        ImageView backBtn = (ImageView) findViewById(R.id.back_icon);
        list = (LinearLayout) findViewById(R.id.event_describe);
        adapter = new weblistAdapter(OfficeEventDetail.this, urls);
//        listview.setAdapter(adapter);

        share_menu = (LinearLayout) findViewById(R.id.share_menu_rl);

        //实例化参加的按钮
        MeiMengApplication.attend = (Button) findViewById(R.id.office_detail_attend_button);
        MeiMengApplication.attend.setOnClickListener(this);
//        backBtn.setOnClickListener(this);

        try {
            if (TextUtils.isEmpty(Utils.getUserId())) {
                return;
            }
            if (TextUtils.isEmpty(Utils.getUserToken())) {
                return;
            }
            //构造url和json格式的请求参数
            String url = BuildString.EventDetailUrl(Utils.getUserId(), Utils.getUserToken());
//            String jsonStr = BuildString.EventDetailReqBody(mActivityId);//测试用，正式版时请传入正确参数

            String jsonStr = BuildString.EventDetailReqBody(mActivityId);
            //得到Observale并获取返回的数据(主线程中)
            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {

                            EventDetail eventDetail = GsonTools.getEventDetailJson(s);
                            // setPriceDb(eventDetail);
                            //活动的名称
                            name = eventDetail.getTitle();
                            title.setText(name);
                            place.setText(eventDetail.getPlace());

                            //活动图片
                            activityPicId = eventDetail.getPic();
                            activityPicUrl = InternetUtils.setPictureUrl(activityPicId);

                            Log.i("data", eventDetail.getDescribe());
                            if (!TextUtils.isEmpty(eventDetail.getDescribe())) {
                                urls = Utils.getUrlStr(eventDetail.getDescribe());
                                for (int n = 0; n < urls.size(); n++) {
                                    for (int m = 0; m < urls.size(); m++) {
                                        if (urls.get(n).equals(urls.get(m))) {
                                            urls.remove(m);
                                        }
                                    }
                                }
                                for (String str : urls) {
                                    MyWebView web = new MyWebView(OfficeEventDetail.this);
                                    web.loadUrl(str);
                                    WebSettings mWebSettings = web.getSettings();
                                    mWebSettings.setUseWideViewPort(true);
                                    mWebSettings.setLoadWithOverviewMode(true);
                                    web.requestFocus();
                                    web.requestFocusFromTouch();
                                    web.setClickable(false);
                                    list.addView(web);
                                }

//                                adapter.setList(urls);
//                                adapter.notifyDataSetChanged();

                            }

                            placeDetailShowFlg = eventDetail.getPlaceDetailShowFlg();
                            if (!TextUtils.isEmpty(eventDetail.getPlaceDetail())) {
                                switch (placeDetailShowFlg) {
                                    case 0://显示
                                        placedetail.setVisibility(View.VISIBLE);
                                        placedetail.setText(eventDetail.getPlaceDetail());
                                        break;
                                    case 1://不显示
                                        placedetail.setVisibility(View.GONE);
                                        break;
                                    default:
                                        break;
                                }

                            }

                            time.setText(Utils.getDisplayDate(eventDetail.getStartTime(), "yyyy.MM.dd"));
                            String num = String.valueOf(eventDetail.getApplyAllCount());
                            maxNum = String.valueOf(eventDetail.getPeopleSize());
                            StringBuffer sb = new StringBuffer();
                            sb.append("已报名： (").append(num).append("/" + maxNum + ")");
                            allNum.setText(sb.toString());

                            //参加此活动的价格
                            activityPrice = eventDetail.getPrice();
                            if (String.valueOf(activityPrice).equals("0.0")) {
                                cost.setText("免费");
                            } else {
                                cost.setText("¥  " + (int) activityPrice + "/人");
                            }

                            String describetext = eventDetail.getDescribe();
                            String requirementtext = eventDetail.getRequirement();
                            if (describetext == null) {
                                describetext = "  ";
                            }
                            if (requirementtext == null) {
                                requirementtext = "  ";
                            }
//                            describe.setText(describetext);
//                            requirment.setText(requirementtext);
                            switch (eventDetail.getState()) {
                                //报名中
                                case 1:
//                                    state.setText("报名中");
//                                    state.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_4_shape));
                                    if (eventDetail.isHasApply()) {
                                        MeiMengApplication.attend.setText("已参加");
                                        MeiMengApplication.attend.setClickable(false);
                                        MeiMengApplication.attend.setBackgroundResource(R.drawable.explore_screen_btn_background_1);
                                    }
                                    break;
                                //即将开始
                                case 2:
//                                    state.setText("即将开始");
//                                    state.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_4_shape));
                                    MeiMengApplication.attend.setText("报名截止");
                                    MeiMengApplication.attend.setClickable(false);
                                    MeiMengApplication.attend.setBackgroundResource(R.drawable.explore_screen_btn_background_1);
                                    break;
                                //进行中
                                case 3:
//                                    state.setText("进行中");
//                                    state.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_on_shape));
                                    MeiMengApplication.attend.setText("报名截止");
                                    MeiMengApplication.attend.setClickable(false);
                                    MeiMengApplication.attend.setBackgroundResource(R.drawable.explore_screen_btn_background_1);
                                    break;
                                //过期
                                case 4:
//                                    state.setText("活动结束");
//                                    state.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_end_shape));
                                    MeiMengApplication.attend.setText("报名截止");
                                    MeiMengApplication.attend.setClickable(false);
                                    MeiMengApplication.attend.setBackgroundResource(R.drawable.explore_screen_btn_background_1);
                                    break;
                                default:
                                    break;
                            }

                            InternetUtils.getPicIntoView(375, 220, post, activityPicId);
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable error) {
                            Log.e("test:", error.getMessage());
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 10:
                    break;

            }
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.more_search_layout://返回键
                finish();
                break;
            case R.id.more_menu_layout://
                menu_layout();
                break;
            case R.id.office_detail_attend_button:
                //点击参加活动，如果免费，直接调接口参加，不免费就调支付页面
                if (activityPrice == 0.0) {
                    getApply();
                } else {
                    getApply();
                    //支付页面
                    MeiMengApplication.payTitle = name;
                    MeiMengApplication.payActivityId = mActivityId;
                    MeiMengApplication.payPrice = activityPrice;
                    DialogUtils.setActDialog(OfficeEventDetail.this, mActivityId, activityPrice, name, 2);
                   /* PayDialogFragment dialog = new PayDialogFragment(mActivityId, activityPrice, name, 2);
                    dialog.show(getFragmentManager(), null);*/
                }
                break;

        }
        shareMenu(view);
    }

    private void getApply() {
        if (TextUtils.isEmpty(Utils.getUserId())) {
            return;
        }
        if (TextUtils.isEmpty(Utils.getUserToken())) {
            return;
        }
        String url = InternetConstant.SERVER_URL + InternetConstant.ACTIVITY_DOAPPLY_URL + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();
        //构造url和json格式的请求参数
        try {
            JSONStringer jsonStringer = new JSONStringer().object().key("activityId").value(mActivityId).endObject();
            String jsonStr = jsonStringer.toString();
            //得到Observable并获取返回的数据(主线程中)
            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            BaseBean baseBean = GsonTools.getBaseReqBean(s);
                            if (baseBean.isSuccess()) {
                                MeiMengApplication.attend.setText("已参加");
                                MeiMengApplication.attend.setBackgroundResource(R.color.hasjoin);
                                MeiMengApplication.attend.setClickable(false);
                                Toast.makeText(getApplicationContext(), "参加活动成功", Toast.LENGTH_SHORT).show();
                                String initSb = allNum.getText() + "";
                                char initsv = initSb.charAt(6);
                                int num = Integer.parseInt(initsv + "");
                                StringBuffer sb = new StringBuffer();
                                sb.append("已报名： (").append((num + 1)).append("/" + maxNum + ")");
                                allNum.setText(sb.toString());
                            } else {
                                DialogUtils.setDialog(OfficeEventDetail.this, baseBean.getError());
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable error) {
                            Log.e("test:", error.getMessage());
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 分享菜单
     */
    private void menu_layout() {
//        LinearLayout share_menu = (LinearLayout) findViewById(R.id.share_menu_rl);
        if (share_menu.getVisibility() != View.VISIBLE) {
            share_menu.setVisibility(View.VISIBLE);
            LinearLayout invite_weixin = (LinearLayout) share_menu.findViewById(R.id.invite_weixin);
            LinearLayout invite_pyq = (LinearLayout) share_menu.findViewById(R.id.invite_pyq);
            LinearLayout invite_qq = (LinearLayout) share_menu.findViewById(R.id.invite_qq);
            LinearLayout invite_mail = (LinearLayout) share_menu.findViewById(R.id.invite_mail);
            LinearLayout invite_message = (LinearLayout) share_menu.findViewById(R.id.invite_message);
            LinearLayout invite_copylink = (LinearLayout) share_menu.findViewById(R.id.invite_copylink);

            invite_weixin.setOnClickListener(this);
            invite_pyq.setOnClickListener(this);
            invite_qq.setOnClickListener(this);
            invite_mail.setOnClickListener(this);
            invite_message.setOnClickListener(this);
            invite_copylink.setOnClickListener(this);
        } else {
            share_menu.setVisibility(View.GONE);
        }


    }

    /**
     * 分享菜单点击事件
     *
     * @param view
     */
    private void shareMenu(View view) {
        switch (view.getId()) {
            case R.id.invite_weixin://微信好友
                weixinShareListener(SendMessageToWX.Req.WXSceneSession);
                break;
            case R.id.invite_pyq://微信朋友圈
                weixinShareListener(SendMessageToWX.Req.WXSceneTimeline);
                break;
            case R.id.invite_qq://QQ好友
                qqShareListener();
                break;
            case R.id.invite_mail://邮件
                emailShareListener();
                break;
            case R.id.invite_message://短信
                setLianJie();

                break;
            case R.id.invite_copylink://复制链接
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("simple text", activityUrl);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(this, "活动链接复制成功", Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
    }

    //发送短信
    private void setLianJie() {
        //如果有网
        if (InternetUtils.isNetworkConnected(OfficeEventDetail.this)) {
            try {
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
                                MyBaseInfoBean baseInfoBean = GsonTools.getMyBaseInfoBean((String) o);
                                if (baseInfoBean.isSuccess()) {
                                    str = baseInfoBean.getParam().getUserSimpleInfo().getFirstName();
                                    if (baseInfoBean.getParam().getUserSimpleInfo().getSex() == 0) {
                                        str += "先生";
                                    } else {
                                        str += "女士";
                                    }
                                    Intent it = new Intent(Intent.ACTION_VIEW);
                                    String strxin = str + "邀请您参加" + name + "活动。" + activityUrl + "\n美盟--婚恋定制专家。";
                                    it.putExtra("sms_body", strxin);
                                    it.setType("vnd.android-dir/mms-sms");
                                    startActivity(it);
                                } else {
                                    if (baseInfoBean.getError().equals("未登陆")) {
                                        DialogUtils.setDialog(OfficeEventDetail.this, baseInfoBean.getError());
                                    } else {
//                                        Toast.makeText(getActivity(), baseInfoBean.getError(), Toast.LENGTH_LONG).show();
                                    }

                                }
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                // Log.e(TAG, throwable.getMessage());
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(OfficeEventDetail.this, "网络没有连接，请检查您的网络", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                DialogUtils.setDialog(OfficeEventDetail.this, "发送成功");
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                DialogUtils.setDialog(OfficeEventDetail.this, "发送取消");
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                DialogUtils.setDialog(OfficeEventDetail.this, "发送被拒绝");
                break;
            default:
                DialogUtils.setDialog(OfficeEventDetail.this, "发送返回");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (null != mTencent)
            mTencent.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * qq分享活动
     */
    private void qqShareListener() {


        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, "活动：");
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, name);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, activityUrl);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, activityPicUrl);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "美盟");
        mTencent.shareToQQ(OfficeEventDetail.this, params, qqShareListener);
    }

    /**
     * 分享活动给朋友或者朋友圈
     *
     * @param type
     */
    private void weixinShareListener(final int type) {


        Bitmap bitmap = InternetUtils.getBitmapFromMemoryCache(activityPicId);

        //缓存中有图片
        if (bitmap != null) {
            weixinShare(bitmap, type);
        }
        //缓存中没有图片，从网络下载图片
        else {
            Observable observable = InternetUtils.getBytesObservale(activityPicId);
            observable.observeOn(rx.android.concurrency.AndroidSchedulers.mainThread()).subscribe(new Action1<byte[]>() {

                @Override
                public void call(byte[] bytes) {
                    // Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    Bitmap bitmap = ProcessPhoto.decodeFromByte(bytes, 0, bytes.length, 140, 140);//此按照活动页的大小设置，应该在getPicIntoView里添加期望的长宽参数
                    if (bitmap != null) {
                        InternetUtils.addBitmapToMemoryCache(activityPicId, bitmap);
                        weixinShare(bitmap, type);
                    }

                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    LogUtil.log.e("OfficeEventDetail", throwable.getMessage());
                }
            });
        }
    }

    /**
     * 微信的分享事件
     *
     * @param bitmap
     * @param type
     */
    private void weixinShare(Bitmap bitmap, int type) {
        WXWebpageObject appExtendObject = new WXWebpageObject();

        //活动链接
        appExtendObject.webpageUrl = activityUrl;

        // 用WXTextObject对象初始化一个WXMediaMessage对象
        WXMediaMessage msg = new WXMediaMessage(appExtendObject);
        msg.title = "活动";
        msg.description = name;
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, 140, 140, true);
//        bitmap.recycle();
        msg.setThumbImage(thumbBmp);
        msg.mediaObject = appExtendObject;
        // 构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("text1"); // transaction字段用于唯一标识一个请求
        req.message = msg;
        req.scene = type;

        // 调用api接口发送数据到微信
        weixinShareapi.sendReq(req);
    }

    /**
     * 生成唯一标识
     *
     * @param type
     * @return
     */
    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    /**
     * 通过邮件分享
     */
    private void emailShareListener() {
        //发邮件
        Intent email = new Intent(Intent.ACTION_SEND);
        email.setType("plain/text");
        String emailSubject = "活动";

        //设置邮件默认标题
        email.putExtra(Intent.EXTRA_SUBJECT, emailSubject);
        //设置要默认发送的内容
        email.putExtra(Intent.EXTRA_TEXT, name + "详情链接：" + activityPicUrl);
        //调用系统的邮件系统
        startActivityForResult(Intent.createChooser(email, "请选择邮件发送软件"), 1001);

    }
}

