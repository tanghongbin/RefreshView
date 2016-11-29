package com.example.com.meimeng.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.com.meimeng.MeiMengApplication;
import com.example.com.meimeng.R;
import com.example.com.meimeng.activity.identify.IdentifyEducationActivity;
import com.example.com.meimeng.activity.identify.IdentifyMarriageActivity;
import com.example.com.meimeng.activity.identify.IdentifyMoneyActivity;
import com.example.com.meimeng.activity.identify.IdentifyUserActivity;
import com.example.com.meimeng.activity.identify.WorkIdentifyActivity;
import com.example.com.meimeng.gson.GsonTools;
import com.example.com.meimeng.gson.gsonbean.CertificateStatus;
import com.example.com.meimeng.gson.gsonbean.CertificateStatusBean;
import com.example.com.meimeng.net.InternetConstant;
import com.example.com.meimeng.net.InternetUtils;
import com.example.com.meimeng.util.DialogUtils;
import com.example.com.meimeng.util.Utils;

import org.json.JSONStringer;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by Administrator on 2015/11/29.
 */
public class CertificationActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout Identify_User_rl;
    private RelativeLayout Identify_Marriage_rl;
    private RelativeLayout Identify_Money_rl;
    private RelativeLayout Identify_Work_rl;
    private RelativeLayout Identify_Education_rl;

    private TextView Identify_User_text;
    private TextView Identify_Marriage_text;
    private TextView Identify_Money_text;
    private TextView Identify_Work_text;
    private TextView Identify_Education_text;

    private final int GETDATAOVER = 10;

    private CertificateStatus certificateStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_certification);

        MeiMengApplication.currentContext=this;
        //初始化视图
        initView();

    }



    /**
     * 初始化视图
     */
    private void initView() {
        initTitleBar("认证中心",R.drawable.icon_nav_back,-1,this);

        Identify_User_rl = (RelativeLayout) findViewById(R.id.Identify_User_rl);
        Identify_Marriage_rl = (RelativeLayout) findViewById(R.id.Identify_Marriage_rl);
        Identify_Money_rl = (RelativeLayout) findViewById(R.id.Identify_Money_rl);
        Identify_Work_rl = (RelativeLayout) findViewById(R.id.Identify_Work_rl);
        Identify_Education_rl = (RelativeLayout) findViewById(R.id.Identify_Education_rl);
        Identify_User_rl.setOnClickListener(this);
        Identify_Marriage_rl.setOnClickListener(this);
        Identify_Money_rl.setOnClickListener(this);
        Identify_Work_rl.setOnClickListener(this);
        Identify_Education_rl.setOnClickListener(this);

        Identify_User_text = (TextView) findViewById(R.id.Identify_User_text);
        Identify_Marriage_text = (TextView)findViewById(R.id.Identify_Marriage_text);
        Identify_Money_text = (TextView) findViewById(R.id.Identify_Money_text);
        Identify_Work_text = (TextView) findViewById(R.id.Identify_Work_text);
        Identify_Education_text = (TextView) findViewById(R.id.Identify_Education_text);

    }

    @Override
    protected void onStart() {
        super.onStart();
        //接口部分
        certificateStatus=null;
        setData();
    }

    private void setData() {

        //如果有网
        if (InternetUtils.isNetworkConnected(CertificationActivity.this)) {

            try {
                //获取用户的uid,和token
                if (TextUtils.isEmpty(Utils.getUserId())) {
                    return;
                }
                if (TextUtils.isEmpty( Utils.getUserToken() )) {
                    return;
                }
                String url = InternetConstant.SERVER_URL + InternetConstant.CERTIFICATE_GETSTATUS + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();
                //{"targetUid":1}
                JSONStringer stringer = new JSONStringer().object()
                        .key("targetUid").value(Utils.getUserId())
                        .endObject();
                String jsonStr = stringer.toString();

                Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
                observable.observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1() {
                            @Override
                            public void call(Object o) {
                                CertificateStatusBean certificateStatusBean = GsonTools.getCertificateStatusBean((String) o);
                                if (certificateStatusBean.isSuccess()) {
                                    certificateStatus = certificateStatusBean.getParam().getStatus();
                                    mhandler.sendEmptyMessage(GETDATAOVER);
                                } else {
                                    DialogUtils.setDialog(CertificationActivity.this, certificateStatusBean.getError());

                                }
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {

                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private Handler mhandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case GETDATAOVER:
                    if (certificateStatus.getIdeStatus()==null){
                        setText(-1,Identify_User_text);
                    }else {
                        setText(certificateStatus.getIdeStatus(),Identify_User_text);
                    }

                    if (certificateStatus.getMarStatus()==null){
                        setText(-1,Identify_Marriage_text);
                    }else {
                        setText(certificateStatus.getMarStatus(),Identify_Marriage_text);
                    }

                    if (certificateStatus.getPropertyStatus()==null){
                        setText(-1,Identify_Money_text);
                    }else {
                        setText(certificateStatus.getPropertyStatus(),Identify_Money_text);
                    }

                    if (certificateStatus.getJobStatus()==null){
                        setText(-1,Identify_Work_text);
                    }else {
                        setText(certificateStatus.getJobStatus(),Identify_Work_text);
                    }

                    if (certificateStatus.getEduStatus() == null){
                        setText(-1,Identify_Education_text);
                    }else {
                        setText(certificateStatus.getEduStatus(),Identify_Education_text);
                    }
                    break;
            }
        }
    };

    private void setText(int state,TextView view) {
        switch (state){
            case -1:
                view.setText("/未提交");
                break;
            case 0://待认证
                view.setText("/已提交");
                break;
            case 1://已认证
                view.setText("/已认证");
                break;
            case 2://认证失败
                view.setText("/未通过");
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.more_search_layout://返回
                super.onBackPressed();
                break;
            case R.id.Identify_User_rl://身份认证
                Intent intent_1 = new Intent(this,IdentifyUserActivity.class);
                startActivity(intent_1);
                break;
            case R.id.Identify_Marriage_rl://单身承诺
                Intent intent_2 = new Intent(this,IdentifyMarriageActivity.class);
                startActivity(intent_2);
                break;
            case R.id.Identify_Money_rl://资产认证
                Intent intent_3 = new Intent(this,IdentifyMoneyActivity.class);
                startActivity(intent_3);
                break;
            case R.id.Identify_Work_rl://职位认证
                Intent intent_4 = new Intent(this,WorkIdentifyActivity.class);
                startActivity(intent_4);
                break;
            case R.id.Identify_Education_rl://学历认证
                Intent intent_5 = new Intent(this,IdentifyEducationActivity.class);
                startActivity(intent_5);
                break;

        }
    }
}