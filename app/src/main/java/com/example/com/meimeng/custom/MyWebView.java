package com.example.com.meimeng.custom;

import android.content.Context;
import android.view.MotionEvent;
import android.webkit.WebView;

/**
 * Created by Administrator on 2015/12/4.
 */
public class MyWebView extends WebView{
    public MyWebView(Context context) {
        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        return super.onTouchEvent(event);
        return false;
    }
}
