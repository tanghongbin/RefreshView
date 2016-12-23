package com.publishproject.core.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.publishproject.R;


/**
 * @author : 汤洪斌
 * @time : 2016/9/9 12:00
 * @des     : 共用标题头
 */
public class HeadView extends LinearLayout {
    /**
     * 左上角返回imageView
     * */
    public ImageView common_back;

    // 标题左边文字
    public TextView head_common_left_tv;

    //标题右边图片
    public ImageView head_common_right_iv;

    //标题中间文本
    public TextView head_common_title;
    //标题右边文字
    public TextView head_common_right_tv;

    public ImageView head_common_right_iv2;
    public TextView head_common_right_tv2;
    private View view;
    private RelativeLayout rl_back;

    public HeadView(Context context) {
        super(context);
    }

    public HeadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);

    }

    public void setCustomBackGroud(int color){
        rl_back.setBackgroundColor(color);

    }

    public void setCenterTitleColor(int color){
        head_common_title.setTextColor(color);
    }

    private void initView(Context context) {
        view = View.inflate(context, R.layout.common_head_wrap_layout_copy,this);
        rl_back = (RelativeLayout)view.findViewById(R.id.head_view_backgroud);
        common_back = (ImageView) view.findViewById(R.id.common_back);
        head_common_left_tv = (TextView)view.findViewById(R.id.head_common_left_tv);
        head_common_right_iv = (ImageView) view.findViewById(R.id.head_common_right_iv);
        head_common_title = (TextView)view.findViewById(R.id.head_common_title);
        head_common_right_tv = (TextView)view.findViewById(R.id.head_common_right_tv);
        head_common_right_iv2 = (ImageView) view.findViewById(R.id.head_common_right_iv2);
        head_common_right_tv2 = (TextView)view.findViewById(R.id.head_common_right_tv2);

       // common_back.setImageResource(R.drawable.back_button_white);
    }

    //设置标题左边图片监听器
    public void setLeftListener(OnClickListener listener){
        common_back.setOnClickListener(listener);
    }

    //设置标题中间文字
    public void setCenterTitle(String ms){
        head_common_title.setText(ms);
    }

    //设置标题左边文字
    public void setLeftText(String msg,OnClickListener listener){
        head_common_left_tv.setText(msg);
        common_back.setVisibility(View.GONE);
        if(msg != null)head_common_left_tv.setOnClickListener(listener);
    }

    //设置右边文字和监听器
    public void setRightText(String msg,OnClickListener listener){
        head_common_right_tv.setText(msg);
        head_common_right_tv.setVisibility(View.VISIBLE);
        if(msg != null)head_common_right_tv.setOnClickListener(listener);
    }

    public void setRightTextColor(int color){
        head_common_right_tv.setTextColor(getResources().getColor(color));
    }
    //设置右边文字
    public void setRightText(String tv){
        head_common_right_tv.setText(tv);
    }
    //设置右边监听器
    public void setRightListener(OnClickListener onClickListener){
        head_common_right_tv.setOnClickListener(onClickListener);
    }
    public void setRightListener2(OnClickListener onClickListener){
        head_common_right_tv2.setOnClickListener(onClickListener);
    }

    //设置左边图片和监听器
    public void setLeftImage(int image,OnClickListener listener){
        if(image != 0){
            common_back.setImageResource(image);
            common_back.setOnClickListener(listener);
        }
    }

    //设置左边图片和监听器,如果为空则不显示
    public void setLeftImage(int image){
        if(image != 0){
            common_back.setImageResource(image);
        }else {
            common_back.setVisibility(View.GONE);
        }
    }
    public void setRightImage(int image,OnClickListener listener){
        if(image != 0){
            head_common_right_iv.setVisibility(View.VISIBLE);
            head_common_right_iv.setImageResource(image);
            head_common_right_iv.setOnClickListener(listener);
        }
    }

    public View getRightImageView(){
        return head_common_right_iv;
    }
    public  void setRightImage2(int image,OnClickListener listener){
        if(image != 0){
            head_common_right_iv2.setVisibility(View.VISIBLE);
            head_common_right_iv2.setImageResource(image);
            head_common_right_iv2.setOnClickListener(listener);
        }
    }

    /**
     * 返回头部标题
     * */
    public String getTitle(){
       return head_common_title.getText().toString();
    }



}
