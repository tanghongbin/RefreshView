package com.publishproject.core.common.loadingdialog.loadingdialogtypes;

/**
 * @author Android客户端组-tanghongbin
 * @Title: CustomProgressDialog
 * @Package com.publishproject.core.commonconfig.dialogconfigs.dialogtypes
 * @Description: TODO
 * Copyright: Copyright (c) 2016
 * Company: 成都壹柒互动科技有限公司
 * @date 2016/12/9 11:48
 */

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.publishproject.R;
import com.publishproject.core.common.loadingdialog.LoadingDialogInterface;


public class CustomProgressDialog extends Dialog implements LoadingDialogInterface<CustomProgressDialog> {

    private TextView tv;
    private LinearLayout linear;

    public CustomProgressDialog(Context context) {
        super(context);
    }

    public CustomProgressDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    //默认返回可以取消
    private boolean cancelable = true;
    private OnCancelListener cancelListener;
    public CustomProgressDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context);
        this.cancelable = cancelable;
        this.cancelListener = cancelListener;
    }

    public void setCancelListener(OnCancelListener cancelListener) {
        this.cancelListener = cancelListener;
    }

    public void show(String text){
        show();
        setMessage(text);
    }

    /**
     * 当窗口焦点改变时调用
     */
    public void onWindowFocusChanged(boolean hasFocus) {
        //隐藏progressBar
        findViewById(R.id.progress_bar).setVisibility(View.GONE);

        //如果自定义了图片动画，在这里开始执行动画
        ImageView imageView = (ImageView) findViewById(R.id.image);
        AnimationDrawable ad = (AnimationDrawable) imageView.getBackground();
        ad.start();//开始动画
    }

    /**
     * 给Dialog设置提示信息
     *
     * @param message
     */
    public void setMessage(CharSequence message) {
        if (message != null && message.length() > 0) {
            findViewById(R.id.message).setVisibility(View.VISIBLE);
            TextView txt = (TextView) findViewById(R.id.message);
            txt.setText(message);
            txt.invalidate();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_custom_progress_dialog);
        linear = (LinearLayout) findViewById(R.id.custom_dialog_linear);

        // 按返回键是否取消
        setCancelable(cancelable);
        // 监听返回键处理
        if (cancelListener != null) {
            setOnCancelListener(cancelListener);
        }

        // 设置居中
        getWindow().getAttributes().gravity = Gravity.CENTER;
        WindowManager.LayoutParams lp = getWindow().getAttributes();

        getWindow().setBackgroundDrawableResource(R.drawable.dialog_back);
//        meaSureWidth();
//        getWindow().set
        // 设置背景层透明度
//        lp.dimAmount = 0.0f;
//        lp.alpha = 0.0f;
        getWindow().setAttributes(lp);
        tv = (TextView) findViewById(R.id.message);

    }

    private int [] meaSureWidth() {
        linear.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        return new int [] {linear.getMeasuredWidth(),linear.getMeasuredHeight()};
    }

    @Override
    public void showDialog() {
        show();
    }

    @Override
    public void showDialog(String content) {
        if(!TextUtils.isEmpty(content)) {
            tv.setText(content);
            tv.setVisibility(View.VISIBLE);
        }else {
            tv.setVisibility(View.GONE);
        }
        show();
    }

    @Override
    public void dismissDialog() {
        dismiss();
    }

    @Override
    public CustomProgressDialog getDialog() {
        return this;
    }


}

