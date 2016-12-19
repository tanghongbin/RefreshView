package com.publishproject.core.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Android客户端组-tanghongbin
 * @Title: BindingNetListView
 * @Package com.publishproject.core.views
 * Copyright: Copyright (c) 2016
 * Company: 成都壹柒互动科技有限公司
 * @date 2016/12/19 11:49
 * @Description: 绑定网络状态的view，外部接受接口为List，
 * 可根据list的内容来判断是否可以上拉加载更过，刷新，或者当无内容时显示默认视图,暂时支持pullRefreshListView,
 * PullRreshGridView,PullRefreshExpandListView
 */
public abstract class BindingNetView<VIEW extends PullToRefreshAdapterViewBase> extends FrameLayout{
    private static final String NO_CONTENT_VIEW = "no_content_view";
    private List mList;

    public BindingNetView(Context context) {
        super(context);
        init(context);
    }


    protected VIEW refreshView;
    protected BaseAdapter adapter;

    /**
     * 上下文环境对象
     */
    protected Context mContext;
    /**
     * 没有内容时显示的view
     */
    protected View noContentView;
    /**
     * 指定集合返回的满足的一页的数量
     */
    protected int pageSize = 20;

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public BaseAdapter getAdapter() {
        return adapter;
    }

    public BindingNetView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public View getOriginalView(){
        return refreshView.getRefreshableView();
    }

    //初始化
    protected final void init(Context context) {
        mContext = context;
        refreshView = generateBindView();
        refreshView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
        addView(refreshView);
    }

    /**
     * 生成绑定的view
     * @return
     */
    protected abstract VIEW generateBindView();

    public void setAdapter(BaseAdapter adapter){
        this.adapter = adapter;
        refreshView.setAdapter(adapter);
    }

    public void setNoContentView(View view){
        if(noContentView != null){
            removeView(noContentView);
        }
        this.noContentView = view;
        bindingListChanged();
    }

    /**
     * 通知数据发生改变
     */
    public void notifyObserverDataChanged(){
        if(adapter == null){
            throw new NullPointerException("adater 不能为空");
        }
        adapter.notifyDataSetChanged();
        bindingListChanged();
    }

    /**
     * 启动刷新
     */
    public void enabledRefresh(){
        refreshView.setMode(PullToRefreshBase.Mode.BOTH);
    }

    public void disabledRefresh(){
        refreshView.setMode(PullToRefreshBase.Mode.DISABLED);
    }


    /**
     *     绑定的集合数据发生变化，自动映射到listview
     */
    private void bindingListChanged() {
        //集合为空时，填充默认视图并且隐藏refreshview
        if(mList == null || mList.size() == 0){
            refreshView.setVisibility(GONE);
            if(findViewWithTag(NO_CONTENT_VIEW) == null){
                if(noContentView == null){
                    noContentView = getnerateDefaultView(mContext);
                }
                noContentView.setTag(NO_CONTENT_VIEW);
            }
            if(noContentView.getParent() == null){
                this.addView(noContentView,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            }
            noContentView.setVisibility(View.VISIBLE);
            return;
        }
        //如果有数据，并且布局里面的视图有noContentView，那么隐藏noContentView，重新显示ListView
        if(mList.size() > 0){
            View view = findViewWithTag(NO_CONTENT_VIEW);
            if(view != null){
                noContentView.setVisibility(View.GONE);
                refreshView.setVisibility(View.VISIBLE);
            }
        }

        if(mList.size() < pageSize){
            refreshView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        }

        if(mList.size() > pageSize){
            refreshView.setMode(PullToRefreshBase.Mode.BOTH);
        }

    }

    /**
     * 重置view的显示状态，显示refreshview，隐藏noContentView,并且将刷新模式设置为BOTH
     */
    protected void resetViewStatus(){
        refreshView.setVisibility(VISIBLE);
        refreshView.setMode(PullToRefreshBase.Mode.BOTH);
        if(noContentView != null){
            noContentView.setVisibility(GONE);
        }

    }

    /**
     * 当默认显示内容为空时，填充默认视图
     */
    private View getnerateDefaultView(Context context) {
        TextView view = new TextView(context);
        view.setText("暂无数据显示，点击刷新");
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                resetViewStatus();
            }
        });
        return view;
    }

    public void bindList(List mList) {
        this.mList = mList;
    }

    public List getmList() {
        return mList;
    }

}
