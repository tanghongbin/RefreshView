package com.example.com.meimeng.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.com.meimeng.MeiMengApplication;
import com.example.com.meimeng.R;
import com.example.com.meimeng.constants.CommonConstants;
import com.example.com.meimeng.fragment.ShareDialogFragment;
import com.example.com.meimeng.gson.GsonTools;
import com.example.com.meimeng.gson.gsonbean.InviteBean;
import com.example.com.meimeng.net.InternetConstant;
import com.example.com.meimeng.net.InternetUtils;
import com.example.com.meimeng.pay.PayConstants;
import com.example.com.meimeng.util.DialogUtils;
import com.example.com.meimeng.util.Utils;
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

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by 010 on 2015/7/18.
 */
public class InviteActivity extends BaseActivity implements ShareDialogFragment.OnDialogListener, IWXAPIEventHandler {

    @Bind(R.id.title_text)
    TextView titleText;

    @Bind(R.id.bow_arrow_image_view)
    ImageView bowImageView;

    @Bind(R.id.title_left_arrow_image_view)
    ImageView leftArrayImageView;

    @Bind(R.id.main_dialog_layout)
    LinearLayout mainDialogLayout;

    @Bind(R.id.maker_num_times_text)
    TextView times;

    @Bind(R.id.invite_code_text)
    TextView codetext;

    private int timesget = 0;
    private String code = null;

    private ShareDialogFragment shareDialogFragment;

    //qq实现类
    private Tencent mTencent;

    //微信分享api
    private IWXAPI weixinShareapi;

    //qq分享的回调接口
    IUiListener qqShareListener = new IUiListener() {
        @Override
        public void onCancel() {
            onCancelListener();
        }

        @Override
        public void onComplete(Object response) {
            // TODO Auto-generated method stub
            Toast.makeText(InviteActivity.this, "分享成功", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onError(UiError e) {
            // TODO Auto-generated method stub
            Toast.makeText(InviteActivity.this, e.errorMessage, Toast.LENGTH_LONG).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.invite);
        ButterKnife.bind(this);

        MeiMengApplication.currentContext=this;
        // Tencent类是SDK的主要实现类，开发者可通过Tencent类访问腾讯开放的OpenAPI。
        // 其中APP_ID是分配给第三方应用的appid，类型为String。
        mTencent = Tencent.createInstance(CommonConstants.QQ_APP_ID, this.getApplicationContext());

        //微信分享api
        weixinShareapi = WXAPIFactory.createWXAPI(this, PayConstants.WEIXIN_APP_ID);
        weixinShareapi.handleIntent(getIntent(), this);
        // 初始化视图
        initView();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        titleText.setText("邀请");
        bowImageView.setVisibility(View.GONE);
        try {
            if (TextUtils.isEmpty(Utils.getUserId())) {
                return;
            }
            if (TextUtils.isEmpty(Utils.getUserToken())) {
                return;
            }
            String url = InternetConstant.SERVER_URL + InternetConstant.SHARCODE_GET + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();
            JSONStringer stringer = new JSONStringer().object().endObject();
            String jsonStr = stringer.toString();
            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            InviteBean invitebean = GsonTools.getInviteBean(s);
                            timesget = invitebean.getParam().getChatChance();
                            code = invitebean.getParam().getShareCode();
                            times.setText(String.valueOf(timesget));
                            codetext.setText(code);
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
        leftArrayImageView.setVisibility(View.VISIBLE);

    }

    /**
     * 弹出分享的页面
     */
    @OnClick(R.id.invite_code)
    void inviteCodeListener() {
        addShareDialogLayout();
    }

    private void addShareDialogLayout() {
        if (shareDialogFragment == null) {
            shareDialogFragment = new ShareDialogFragment();
            shareDialogFragment.setOnDialogListener(this);
        }
        mainDialogLayout.setVisibility(View.VISIBLE);
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.anim.bottom_in, 0);
        transaction.replace(R.id.dialog_layout, shareDialogFragment);
        transaction.commit();
    }

    @Override
    @OnClick(R.id.dialog_button)
    public void onCancelListener() {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.remove(shareDialogFragment);
        transaction.commit();
        mainDialogLayout.setVisibility(View.GONE);
    }

    @Override
    public void shareListener(int type) {
        switch (type) {
            case 1:
                //分享给朋友
                weixinShareListener(SendMessageToWX.Req.WXSceneSession);
                break;
            case 2:
                //分享到朋友圈
                weixinShareListener(SendMessageToWX.Req.WXSceneTimeline);
                break;
            case 3:
                //通过qq分享给朋友
                qqShareListener();
                break;
            case 4:
                //通过邮件分享
                emailShareListener();
                break;
            case 5:
                Intent it = new Intent(Intent.ACTION_VIEW);
                it.putExtra("sms_body", "邀请码:" + code + ",美盟-婚恋定制专家" + ",美盟为您相约你的Ta,获取免费红娘邀约机会！下载链接：http://a.app.qq.com/o/simple.jsp?pkgname=com.example.com.meimeng&g_f=991653");
                it.setType("vnd.android-dir/mms-sms");
                startActivity(it);
                break;
            case 6:
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("simple text", code);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(this, "邀请码复制成功", Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
    }

    @OnClick(R.id.title_left_arrow_layout)
    void leftArrowListener() {
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (null != mTencent)
            mTencent.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * qq分享邀请码
     */
    private void qqShareListener() {

        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, "邀请码:" + code + ",美盟-婚恋定制专家");
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, "美盟为您相约你的Ta,获取免费红娘邀约机会！");
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, "http://a.app.qq.com/o/simple.jsp?pkgname=com.example.com.meimeng&g_f=991653");
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, "http://img1.gao7.com/files/appleimage/44C/44CA7AC2-E36E-4DB9-B253-570548281E66.jpg");
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "美盟");
        mTencent.shareToQQ(InviteActivity.this, params, qqShareListener);
    }

    /**
     * 分享给朋友或者朋友圈
     *
     * @param type
     */
    private void weixinShareListener(int type) {

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ico_launcher_2);
        WXWebpageObject appExtendObject = new WXWebpageObject();
        appExtendObject.webpageUrl = "http://a.app.qq.com/o/simple.jsp?pkgname=com.example.com.meimeng&g_f=991653";
        // 用WXTextObject对象初始化一个WXMediaMessage对象
        WXMediaMessage msg = new WXMediaMessage(appExtendObject);
        msg.title = "邀请码:" + code + ",美盟-婚恋定制专家";
        msg.description = "美盟为您相约你的Ta,获取免费红娘邀约机会！";
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, 140, 140, true);
        bitmap.recycle();
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
        String emailSubject = "邀请码:" + code + ",美盟-婚恋定制专家";

        //设置邮件默认地址
        // email.putExtra(android.content.Intent.EXTRA_EMAIL, emailReciver);
        //设置邮件默认标题
        email.putExtra(Intent.EXTRA_SUBJECT, emailSubject);
        //设置要默认发送的内容
        email.putExtra(Intent.EXTRA_TEXT, "美盟为您相约你的Ta,获取免费红娘邀约机会！");
        //调用系统的邮件系统
        startActivityForResult(Intent.createChooser(email, "请选择邮件发送软件"), 1001);

    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {

        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                DialogUtils.setDialog(InviteActivity.this, "发送成功");
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                DialogUtils.setDialog(InviteActivity.this, "发送取消");
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                DialogUtils.setDialog(InviteActivity.this, "发送被拒绝");
                break;
            default:
                DialogUtils.setDialog(InviteActivity.this, "发送返回");
                break;
        }
    }
}
