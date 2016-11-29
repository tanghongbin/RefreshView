package com.example.com.meimeng.activity.identify;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.com.meimeng.MeiMengApplication;
import com.example.com.meimeng.R;
import com.example.com.meimeng.activity.BaseActivity;
import com.example.com.meimeng.gson.GsonTools;
import com.example.com.meimeng.gson.gsonbean.BaseBean;
import com.example.com.meimeng.gson.gsonbean.CertificateStatusBean;
import com.example.com.meimeng.net.InternetConstant;
import com.example.com.meimeng.net.InternetUtils;
import com.example.com.meimeng.util.DialogUtils;
import com.example.com.meimeng.util.Utils;

import org.json.JSONStringer;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by 010 on 2015/7/17.
 * 单身认证界面
 */
public class IdentifyMarriageActivity extends BaseActivity {

    /**
     * 已上传、待认证
     */
    private static final Integer HASUPLOAD = 0;
    /**
     * 认证通过
     */
    private static final Integer HASCERTIFICATE = 1;
    /**
     * 认证不通过
     */
    private static final Integer UNCERTIFICATE = 2;

    private static final String TAG = "IdentifyMarriage";
    @Bind(R.id.upload_identify_layout)
    RelativeLayout mUploadIdentifyLayout;
    @Bind(R.id.ll_identify_layout)
    LinearLayout mLlIdentifyLayout;
    @Bind(R.id.is_show)
    RelativeLayout mIsShow;
    private BaseBean basebeanJson;
    @Bind(R.id.title_text)
    TextView titleText;
    @Bind(R.id.title_left_arrow_image_view)
    ImageView leftArrowImageView;
    @Bind(R.id.title_sure_text)
    TextView sureText;
    @Bind(R.id.bow_arrow_image_view)
    ImageView bowArrowImageView;

    @Bind(R.id.describe_image_view)
    ImageView describeImageView;

    @Bind(R.id.describe_2_2_text)
    TextView describe2_2_Text;
    @Bind(R.id.describe_2_text)
    TextView describe2Text;


    /**
     * 用来判断是否为承诺状态,0为未承诺，1为已经承诺。
     */
    private volatile int haspromise = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.identify_user);
        ButterKnife.bind(this);

        MeiMengApplication.currentContext = this;
        describeImageView.setImageResource(R.drawable.icon_unmarried);
        describe2_2_Text.setVisibility(View.VISIBLE);
        describe2Text.setVisibility(View.GONE);
        mIsShow.setVisibility(View.GONE);
        describe2_2_Text.setText("\n\n本人承诺：\n目前处于单身状态（非已婚人士，无正常交往的男女朋友或处于离异状态），" +
                "本人结束单身将在第一时间修改美盟状态或关闭账户：如因隐瞒非单身情况而导致的一切相关后果，将由本人承担。\n如无异议请单击确认。");
        titleText.setText("单身承诺");
        leftArrowImageView.setVisibility(View.VISIBLE);
        bowArrowImageView.setVisibility(View.GONE);
        mUploadIdentifyLayout.setVisibility(View.GONE);
        mLlIdentifyLayout.setVisibility(View.GONE);
        initImageView();

    }

    /**
     * 初始化
     */
    private void initImageView() {
        //如果有网
        if (InternetUtils.isNetworkConnected(IdentifyMarriageActivity.this)) {

            try {
                //获取用户的uid,和token
                if (TextUtils.isEmpty(Utils.getUserId())) {
                    return;
                }
                if (TextUtils.isEmpty(Utils.getUserToken())) {
                    return;
                }
                String url = InternetConstant.SERVER_URL + InternetConstant.CERTIFICATE_GETSTATUS + "?uid=" + Utils
                        .getUserId() + "&token=" + Utils.getUserToken();
                JSONStringer stringer = new JSONStringer().object()
                        .key("targetUid").value(Utils.getUserId())
                        .endObject();
                String jsonStr = stringer.toString();
                Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
                observable.observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1() {
                            @Override
                            public void call(Object o) {
                                CertificateStatusBean certificateStatusBean = GsonTools.getCertificateStatusBean(
                                        (String) o);
                                if (certificateStatusBean.isSuccess()) {
                                    initWedget(certificateStatusBean);
                                } else {
                                    DialogUtils.setDialog(IdentifyMarriageActivity.this, certificateStatusBean
                                            .getError());
                                }
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                Log.e(TAG, throwable.getMessage());
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 调用接口后的初始化界面
     *
     * @param certificateStatusBean
     */
    private void initWedget(CertificateStatusBean certificateStatusBean) {

        if (certificateStatusBean.getParam().getStatus().getMarStatus() == HASCERTIFICATE) {
            sureText.setVisibility(View.GONE);
            haspromise = 1;
        } else if (certificateStatusBean.getParam().getStatus().getMarStatus() == UNCERTIFICATE) {
            sureText.setVisibility(View.VISIBLE);
            haspromise = 2;
        } else if (certificateStatusBean.getParam().getStatus().getMarStatus() == HASUPLOAD) {
            sureText.setVisibility(View.GONE);
            haspromise = 2;
        } else {
            sureText.setVisibility(View.VISIBLE);
            haspromise = 2;
        }
    }

    //确定按钮
    @OnClick(R.id.title_sure_text)
    void sureListener() {
        setimport2();
    }

    private void setimport2() {

        if (haspromise == 2) {
            try {
                //获取用户的uid,和token
                if (TextUtils.isEmpty(Utils.getUserId())) {
                    return;
                }
                if (TextUtils.isEmpty(Utils.getUserToken())) {
                    return;
                }
                String url = InternetConstant.SERVER_URL + InternetConstant.CERTIFICATE_EDIT + "?uid=" + Utils
                        .getUserId() + "&token=" + Utils.getUserToken();
                JSONStringer jsonStringer = new JSONStringer().object().key("uid").value(Utils.getUserId()).key
                        ("marName").value("1")
                        .endObject();
                String jsonStr = jsonStringer.toString();
                //得到Observable并获取返回的数据(主线程中)
                Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
                observable.observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<String>() {
                            @Override
                            public void call(String s) {
                                basebeanJson = GsonTools.getBaseReqBean(s);
                                if (basebeanJson.isSuccess()) {
                                    haspromise = 0;
                                    onBackPressed();
                                } else {
                                    DialogUtils.setDialog(IdentifyMarriageActivity.this, basebeanJson.getError());
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
    }

    //返回按钮
    @OnClick(R.id.title_left_arrow_layout)
    void leftArrowListener() {
        onBackPressed();
    }


}
