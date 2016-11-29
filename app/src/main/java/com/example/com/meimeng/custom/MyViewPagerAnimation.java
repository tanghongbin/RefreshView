package com.example.com.meimeng.custom;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by shi-02 on 2015/4/27.
 */
public class MyViewPagerAnimation implements ViewPager.PageTransformer {


    private static final float MIN_SCALE = 0.90f;

    @Override
    public void transformPage(View view, float v) {

        //获取当前页面的宽高
        int pageWidth = view.getWidth();
        int pageHeight = view.getHeight();

        //view在屏幕的左边，且看不到
        if (v<-1){

            //完全透明
            view.setAlpha(0);
        }
        else if (v<=1){
            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(v));
            float vertMargin = pageHeight * (1 - scaleFactor) / 2;
            float horzMargin = pageWidth * (1 - scaleFactor) / 2;
            if (v < 0) {
                view.setTranslationX(horzMargin - vertMargin / 2);
                view.setAlpha(1);
            } else {
                view.setTranslationX(-horzMargin + vertMargin / 2);
                view.setAlpha(1);
            }

            float scale = MIN_SCALE
                    + (1 - MIN_SCALE) * (1 - Math.abs(v));
            view.setScaleX(scale);
            view.setScaleY(scale);
        }else {
            view.setAlpha(0);
        }
    }
}
