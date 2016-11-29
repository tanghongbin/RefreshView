package com.example.com.meimeng.custom;

import android.widget.ListView;

/**
 * Created by 010 on 2015/7/17.
 */
public class ScrollViewWithListView extends ListView {

    public ScrollViewWithListView(android.content.Context context,
                                  android.util.AttributeSet attrs) {
        super(context, attrs);
    }

    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);

    }
}
