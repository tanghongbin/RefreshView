package com.example.com.meimeng.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableString;
import android.widget.TextView;

import com.example.com.meimeng.R;

/**
 * Created by Administrator on 2015/7/30.
 */
public class TimeCountUtils extends CountDownTimer {
    private Activity mActivity;
    private TextView btn;//按钮

    // 在这个构造方法里需要传入三个参数，一个是Activity，一个是总的时间millisInFuture，一个是countDownInterval，然后就是你在哪个按钮上做这个是，就把这个按钮传过来就可以了
    public TimeCountUtils(Activity mActivity, long millisInFuture, long countDownInterval, TextView btn) {
        super(millisInFuture, countDownInterval);
        this.mActivity = mActivity;
        this.btn = btn;
    }


    @SuppressLint("NewApi")
    @Override
    public void onTick(long millisUntilFinished) {
        btn.setClickable(false);//设置不能点击

        btn.setText(millisUntilFinished / 1000 + "s后重新获取");//设置倒计时时间
        btn.setTextColor(0xbf9f62);
      //设置按钮为灰色，这时是不能点击的
//        btn.setBackground(mActivity.getResources().getDrawable(R.drawable.verify_btn_pressed));
        btn.setTextColor(mActivity.getResources().getColor(R.color.text_gray));
        Spannable span = new SpannableString(btn.getText().toString());//获取按钮的文字
//        span.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 3, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);//讲倒计时时间显示为红色
        btn.setText(span);

    }

    @SuppressLint("NewApi")
    @Override
    public void onFinish() {
        btn.setText("重新获取");
        btn.setClickable(true);//重新获得点击
//        btn.setBackground(mActivity.getResources().getDrawable(R.drawable.verify_btn_normal));//还原背景色
        btn.setTextColor(mActivity.getResources().getColor(R.color.text));

    }


}