package com.publishproject.core.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import java.util.List;

/**
 * @author Android客户端组-tanghongbin
 * @Title: BindingNetListView
 * @Package com.publishproject.core.views
 * Copyright: Copyright (c) 2016
 * Company: 成都壹柒互动科技有限公司
 * @date 2016/12/19 11:49
 * @Description: 绑定网络状态的listview，外部接受接口为List，
 * 可根据list的内容来判断是否可以上拉加载更过，刷新，或者当无内容时显示默认视图
 */
public class BindingNetListView extends FrameLayout{
    public BindingNetListView(Context context) {
        super(context);
        init(context);
    }

    private List<Object> mList;

    public BindingNetListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    //初始化
    private void init(Context context) {

    }

    public void bindList(List<Object> mList) {
        this.mList = mList;
    }

    public List<Object> getmList() {
        return mList;
    }

    enum DATA{
        //集合为空，没有数据
        NONE_DATA,
        //有数据，但是少于一页
        LESS_THAN_A_PAGE_DATA,
        //有很多数据，大于一页
        MORE_THEN_A_PAGE_DATA
    }
}
