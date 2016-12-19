package com.publishproject.core.views;

import android.content.Context;
import android.util.AttributeSet;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * @author Android客户端组-tanghongbin
 * @Title: BindingListView
 * @Package com.publishproject.core.views
 * Copyright: Copyright (c) 2016
 * Company: 成都壹柒互动科技有限公司
 * @date 2016/12/19 16:14
 * @Description: TODO
 */
public class BindingListView<T> extends BindingNetView<PullToRefreshListView> {
    public BindingListView(Context context) {
        super(context);
    }

    public BindingListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected PullToRefreshListView generateBindView() {
        return new PullToRefreshListView(mContext);
    }
}
