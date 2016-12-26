package com.publishproject.core.views;

import android.content.Context;
import android.util.AttributeSet;

import com.handmark.pulltorefresh.library.PullToRefreshGridView;

/**
 * @author Android客户端组-tanghongbin
 * @Title: BindingGridView
 * @Package com.publishproject.core.views
 * Copyright: Copyright (c) 2016
 * Company: 成都壹柒互动科技有限公司
 * @date 2016/12/19 17:01
 * @Description: TODO
 */
public class BindingGridView extends BindingNetView<PullToRefreshGridView> {
    public BindingGridView(Context context) {
        super(context);
    }

    public BindingGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected PullToRefreshGridView generateBindView() {
        return new PullToRefreshGridView(mContext);
    }
}
