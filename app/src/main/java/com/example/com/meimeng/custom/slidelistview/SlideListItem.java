package com.example.com.meimeng.custom.slidelistview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

public class SlideListItem extends LinearLayout {

    public SlideListItem(Context context) {
        this(context, null);
    }

    public SlideListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.d("test", "event : " + event);
        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int margeLeft = 0;
        int size = getChildCount();

        for (int i = 0; i < size; i++) {
            View view = getChildAt(i);
            if (view.getVisibility() != View.GONE) {
                int childWidth = view.getMeasuredWidth();
                int height = view.getMeasuredHeight();
                LayoutParams p = (LayoutParams) view.getLayoutParams();
                int bottom = p.bottomMargin;
                int top = p.topMargin;
                // 将内部子孩子横排排列
                view.layout(margeLeft, 0, margeLeft + childWidth,
                        height);
                margeLeft += childWidth;
            }
        }
    }
}
