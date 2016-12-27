package com.publishproject.core.views;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * CountDownButton
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/5/26 13:43
 */
public class CountDownButton extends Button {
    private static final int START_DOWN = 0;
    private int countDownMax = 60;
    private int countDown = countDownMax;
    private String normalText = "获取";


    public CountDownButton(Context context) {
        super(context);
    }

    public CountDownButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CountDownButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public String getNormalText() {
        return normalText;
    }

    public void setNormalText(String normalText) {
        this.normalText = normalText;
    }

    public int getCountDownMax() {
        return countDownMax;
    }

    public void setCountDownMax(int countDownMax) {
        this.countDownMax = countDownMax;
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case START_DOWN:
                    countDown = countDown - 1;
                    if (countDown > 0) {
                        setText(countDown + "秒");
                        mHandler.sendEmptyMessageDelayed(START_DOWN, 1000);
                    } else {
                        setText(normalText);
                    }
                    break;

            }
        }
    };

    public void startCountDown() {
        countDown = countDownMax;
        mHandler.removeMessages(START_DOWN);
        setText(countDown + "秒");
        mHandler.sendEmptyMessageDelayed(START_DOWN, 1000);
    }

    public void stopCountDown() {
        mHandler.removeMessages(START_DOWN);
        setText(normalText);
        countDown = countDownMax;
    }

    public void releaseCountDown() {
        countDown = countDownMax;
        mHandler.removeMessages(START_DOWN);
    }
}