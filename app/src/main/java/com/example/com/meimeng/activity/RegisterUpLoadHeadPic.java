package com.example.com.meimeng.activity;

import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.com.meimeng.MeiMengApplication;
import com.example.com.meimeng.R;
import com.example.com.meimeng.bean.ProgressBean;
import com.example.com.meimeng.custom.CircleImageView;
import com.example.com.meimeng.fragment.SelectPictureFragment;
import com.example.com.meimeng.gson.GsonTools;
import com.example.com.meimeng.gson.gsonbean.BaseBean;
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
 * Created by Administrator on 2015/8/2.
 */
public class RegisterUpLoadHeadPic extends BaseActivity implements SelectPictureFragment.OnSelectPictureDialogListener {
    @Bind(R.id.title_text)
    TextView titleText;

    @Bind(R.id.title_left_arrow_image_view)
    ImageView leftArrowImageView;

    @Bind(R.id.title_sure_text)
    TextView sureText;

    @Bind(R.id.bow_arrow_image_view)
    ImageView bowArrowImageView;

    @Bind(R.id.main_dialog_layout)
    LinearLayout mainDialogLayout;

    @Bind(R.id.register_uploadheadpic_nextButton)
    Button nextButton;

    @Bind(R.id.register_uploadheadpic_picview)
    CircleImageView picview;




    //进度条的文本
    private TextView progressTextView;

    private SelectPictureFragment selectPictureFragment;
    private long picId;
    private Dialog dialog;
    private boolean isUpLoadHeadPic = false;

    protected void onCreate(Bundle savedInstanceState) {
        //获得性别信息

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.register_uploadheadpic);
        ButterKnife.bind(this);
        MeiMengApplication.currentContext=this;
        MeiMengApplication.loginActivity.add(this);

        if (MeiMengApplication.sex == 0) {
            picview.setImageResource(R.drawable.meimeng_ico_user_missing);
        } else {
            picview.setImageResource(R.drawable.meimeng_ico_user_missing_wo);
        }
        titleText.setText("上传头像");
        leftArrowImageView.setVisibility(View.VISIBLE);
        bowArrowImageView.setVisibility(View.GONE);
        sureText.setVisibility(View.VISIBLE);
        sureText.setText("跳过");
        sureText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterUpLoadHeadPic.this, RegisterIdentify.class);
                startActivity(intent);
            }
        });

        //下一步
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isUpLoadHeadPic == false) {
                    Toast.makeText(RegisterUpLoadHeadPic.this, "您未上传头像", Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(RegisterUpLoadHeadPic.this, RegisterIdentify.class);
                startActivity(intent);
            }
        });
    }

    //返回按钮
    @OnClick(R.id.title_left_arrow_layout)
    void leftArrowListener() {
        onBackPressed();
    }

    @OnClick(R.id.register_uploadheadpic_layout)
    void registeruploadheadpiclayoutListener() {
        addSelectPictureFragment();
    }

    @OnClick(R.id.register_uploadheadpic_picview)
    void registeruploadheadpicpicviewListener() {
        addSelectPictureFragment();
    }

    @Override
    public void cancelDialog() {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.remove(selectPictureFragment);
        transaction.commit();
        mainDialogLayout.setVisibility(View.GONE);
    }

    @Override

    public void sendResultJson(String type, long pid) {
        this.picId = pid;
    }

    @Override
    public void cancelDialog2() {
        if (null != dialog)
            dialog.dismiss();

    }

    @Override
    public void requestifok() {

        try {
            //获取用户的uid,和token
            if (TextUtils.isEmpty(Utils.getUserId())) {
                return;
            }
            if (TextUtils.isEmpty(Utils.getUserToken())) {
                return;
            }
            String url = InternetConstant.SERVER_URL + InternetConstant.USER_EDIT + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();
            JSONStringer jsonStringer = new JSONStringer().object()
                    .key("headPic").value(picId)
                    .endObject();
            String jsonStr = jsonStringer.toString();
            //得到Observable并获取返回的数据(主线程中)
            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            if (null != dialog)
                                dialog.dismiss();
                            BaseBean basebeanJson = GsonTools.getBaseReqBean(s);
                            if (basebeanJson.isSuccess()) {
                                Toast.makeText(RegisterUpLoadHeadPic.this, "头像上传成功", Toast.LENGTH_SHORT).show();
                                InternetUtils.getPicIntoView(240, 240, picview, picId);
                                isUpLoadHeadPic = true;
                            } else
                                DialogUtils.setDialog(RegisterUpLoadHeadPic.this, basebeanJson.getError());

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

    @Override
    public void getPicturePath(String path) {
//        dialog = Utils.createLoadingDialog(this, "正在上传头像...");
//        dialog.show();
        ProgressBean bean  = DialogUtils.createLoadingDialog2(this,"正在上传...");
        progressTextView = bean.getTextView();
        dialog = bean.getDialog();
        dialog.show();


    }

    @Override
    public void setUploadProgress(String pro) {

        if (progressTextView!=null){
            progressTextView.setText(pro);
        }else {
            Log.e("RegisterUploadHeadPic","上传文件进度条出错");
        }
    }

    //确定按钮
    @OnClick(R.id.title_sure_text)
    void sureListener() {
        onBackPressed();
    }

    @OnClick(R.id.dialog_button)
    void dialogButtonLitener() {
        cancelDialog();
    }

    private void addSelectPictureFragment() {
        if (selectPictureFragment == null) {
            selectPictureFragment = new SelectPictureFragment();
        }
        mainDialogLayout.setVisibility(View.VISIBLE);
        Bundle bundle = new Bundle();
        bundle.putString("type", "headPic");
        selectPictureFragment.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.bottom_in, 0);
        transaction.add(R.id.dialog_layout, selectPictureFragment);
        transaction.commit();
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MeiMengApplication.loginActivity.remove(this);
    }
}

