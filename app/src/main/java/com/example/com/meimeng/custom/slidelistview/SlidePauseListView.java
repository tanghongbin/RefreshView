package com.example.com.meimeng.custom.slidelistview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Scroller;

import com.example.com.meimeng.R;

public class SlidePauseListView extends ListView implements AbsListView.OnScrollListener {
    View footer;
    int totalItemCount;
    int lastVisibleItem;
    boolean isLoading;
    boolean isStopSlide;
    MyLoadListener myLoadListener;
    /**
     * 当前滑动的ListView　position
     */
    private int slidePosition;
    /**
     * 手指按下X的坐标
     */
    private int downY;
    /**
     * 手指按下Y的坐标
     */
    private int downX;
    /**
     * 屏幕宽度
     */
    private int screenWidth;
    /**
     * ListView的item
     */
    private View itemView;
    /**
     * 滑动类
     */
    private Scroller scroller;
    /**
     * 滑动速度边界值
     */
    private static final int SNAP_VELOCITY = 600;
    /**
     * 速度追踪对象
     */
    private VelocityTracker velocityTracker;
    /**
     * 是否响应滑动，默认为不响应
     */
    private boolean isSlide = false;
    /**
     * 认为是用户滑动的最小距离
     */
    private int mTouchSlop;
    /**
     * 最多可滑动的范围
     */
    private int mTouchLimit;
    /**
     * 移除item后的回调接口
     */
    private RemoveListener mRemoveListener;
    /**
     * 是否已经侧滑
     */
    private boolean hasSlided = false;


    public SlidePauseListView(Context context) {
        this(context, null);
        initView(context);
    }

    public SlidePauseListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
//        initView(context);
    }

    public SlidePauseListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
        DisplayMetrics dm = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        scroller = new Scroller(context);
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        mTouchLimit = screenWidth / 4;
    }

    //添加底部加载布局
    private void initView(Context context) {
        isStopSlide=true;
        LayoutInflater inflater = LayoutInflater.from(context);
        footer = inflater.inflate(R.layout.footer_layout, null);
        footer.findViewById(R.id.load_layout).setVisibility(View.GONE);
        this.addFooterView(footer);
        this.setOnScrollListener(this);
    }

    /**
     * 设置滑动删除的回调接口
     *
     * @param removeListener
     */
    public void setRemoveListener(RemoveListener removeListener) {
        this.mRemoveListener = removeListener;
    }


    /**
     * 设置可滑动的最大值
     *
     * @param limit
     */
    public void setTouchLimit(int limit) {
        this.mTouchLimit = limit;
    }

    /**
     * 获取可滑动的最大值
     *
     * @return
     */
    public int getTouchLimit() {
        return this.mTouchLimit;
    }

    /**
     * 退回原位
     */
    public void slideBack() {
        if (hasSlided && itemView != null) {
            scrollXBy(itemView, -mTouchLimit);
            hasSlided = false;
        }
    }

    /**
     * 分发事件，主要做的是判断点击的是那个item, 以及通过postDelayed来设置响应左右滑动事件
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (isStopSlide==true) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    addVelocityTracker(event);

                    // 假如scroller滚动还没有结束，我们直接返回
                    if (!scroller.isFinished()) {
                        return super.dispatchTouchEvent(event);
                    }
                    // 如果已经侧滑了，我们直接返回
                    if (hasSlided) {
                        return super.dispatchTouchEvent(event);
                    }
                    downX = (int) event.getX();
                    downY = (int) event.getY();

                    slidePosition = pointToPosition(downX, downY);

                    // 无效的position, 不做任何处理
                    if (slidePosition == AdapterView.INVALID_POSITION) {
                        return super.dispatchTouchEvent(event);
                    }

                    // 获取我们点击的item view
                    itemView = getChildAt(slidePosition - getFirstVisiblePosition());
                    if (itemView instanceof SlideListItem) {
                        SlideListItem item = (SlideListItem) itemView;
                        int count = item.getChildCount();
                        mTouchLimit = 0;
                        for (int i = 0; i < count; i++) {
                            if (i > 0) {
                                mTouchLimit += item.getChildAt(i).getMeasuredWidth();
                            }
                        }
                    }
                    break;
                }
                case MotionEvent.ACTION_MOVE: {
//			Log.d("test", "downX : " + downX + ", event.getX() : " + event.getX() + ", mTouchSlop : " + mTouchSlop);
                    if (Math.abs(getScrollVelocity()) > SNAP_VELOCITY
                            || (downX - event.getX() > mTouchSlop && Math
                            .abs(event.getY() - downY) < mTouchSlop)) {
                        isSlide = true;
                    }
                    break;
                }
                case MotionEvent.ACTION_UP:
                    recycleVelocityTracker();
                    break;
            }
        }
        return super.dispatchTouchEvent(event);
    }


    public  void setIsSlide(boolean isdr){
        isStopSlide=isdr;
    }
    /**
     * 处理我们拖动ListView item的逻辑
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        Log.d("test", "hasSlided : " + hasSlided + ", isSlide : " + isSlide);
        if (isStopSlide == true) {
            // 如果已经侧滑了，回归原位
            if (hasSlided) {
                scrollXBy(itemView, -mTouchLimit);
                hasSlided = false;
                // 拦截事件，不继续处理
                return false;
            }

            if (isSlide && slidePosition != AdapterView.INVALID_POSITION) {
                requestDisallowInterceptTouchEvent(true);
                addVelocityTracker(ev);
                final int action = ev.getAction();
                int x = (int) ev.getX();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_MOVE:

                        MotionEvent cancelEvent = MotionEvent.obtain(ev);
                        cancelEvent.setAction(MotionEvent.ACTION_CANCEL |
                                (ev.getActionIndex() << MotionEvent.ACTION_POINTER_INDEX_SHIFT));
                        onTouchEvent(cancelEvent);

                        int deltaX = downX - x;
                        downX = x;

                        // 手指拖动itemView滚动, deltaX大于0向左滚动，小于0向右滚
                        scrollXBy(itemView, deltaX);

                        return true;  //拖动的时候ListView不滚动
                    case MotionEvent.ACTION_UP:
                        int velocityX = getScrollVelocity();
                        if (velocityX < -SNAP_VELOCITY) {
                            scrollLeft();
                        } else {
                            scrollByDistanceX();
                        }
                        recycleVelocityTracker();
                        // 手指离开的时候就不响应左右滚动
                        isSlide = false;
                        break;
                }
            }
        }

//		boolean result = super.onTouchEvent(ev);
//		Log.d("test", "result : " + result);
        //否则直接交给ListView来处理onTouchEvent事件

        return super.onTouchEvent(ev);
    }



    /**
     * 让指定的view滚动x位置，设置左右边界，view最多滚动到边界位置
     *
     * @param view
     * @param x
     */
    private void scrollXBy(View view, int x) {
        // 如果已经滑动了最大值，并希望继续向左滑，忽略
        if (view.getScrollX() >= mTouchLimit && x >= 0) return;
        // 如果已经回到原位，并希望继续想右划，忽略
        if (view.getScrollX() <= 0 && x <= 0) return;

        if (view.getScrollX() + x > mTouchLimit) {
            x = mTouchLimit - view.getScrollX();
        } else if (view.getScrollX() + x < 0) {
            x = -view.getScrollX();
        }
        view.scrollBy(x, 0);
    }

    @Override
    public void computeScroll() {
        // 调用startScroll的时候scroller.computeScrollOffset()返回true，
        if (scroller.computeScrollOffset()) {
            // 让ListView item根据当前的滚动偏移量进行滚动
            itemView.scrollTo(scroller.getCurrX(), scroller.getCurrY());

            postInvalidate();

            // 滚动动画结束的时候调用回调接口
            if (scroller.isFinished()) {
                if (mRemoveListener != null) {
                    mRemoveListener.removeItem(slidePosition);
                }
            }
        }
    }

    /**
     * 向左滑动，根据上面我们知道向左滑动为正值
     */
    private void scrollLeft() {
        final int delta = (mTouchLimit - itemView.getScrollX());
        // 调用startScroll方法来设置一些滚动的参数，我们在computeScroll()方法中调用scrollTo来滚动item
        scroller.startScroll(itemView.getScrollX(), 0, delta, 0,
                Math.abs(delta));
        postInvalidate(); // 刷新itemView
        hasSlided = true;
    }

    /**
     * 根据手指滚动itemView的距离来判断是滚动到开始位置还是向左或者向右滚动
     */
    private void scrollByDistanceX() {
        // 如果向左滚动的距离大于屏幕的二分之一，就让其删除
        if (itemView.getScrollX() >= mTouchLimit / 2) {
            scrollLeft();
        } else {
            // 滚回到原始位置,为了偷下懒这里是直接调用scrollTo滚动
            itemView.scrollTo(0, 0);
        }
    }

    /**
     * 添加用户的速度跟踪器
     *
     * @param event
     */
    private void addVelocityTracker(MotionEvent event) {
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        }

        velocityTracker.addMovement(event);
    }

    /**
     * 移除用户速度跟踪器
     */
    private void recycleVelocityTracker() {
        if (velocityTracker != null) {
            velocityTracker.recycle();
            velocityTracker = null;
        }
    }

    /**
     * 获取X方向的滑动速度,大于0向右滑动，反之向左
     *
     * @return
     */
    private int getScrollVelocity() {
        velocityTracker.computeCurrentVelocity(1000);
        int velocity = (int) velocityTracker.getXVelocity();
        return velocity;
    }
    /**
     *scrollState有三种状态，分别是SCROLL_STATE_IDLE、SCROLL_STATE_TOUCH_SCROLL、SCROLL_STATE_FLING
     *SCROLL_STATE_IDLE是当屏幕停止滚动时
     *SCROLL_STATE_TOUCH_SCROLL是当用户在以触屏方式滚动屏幕并且手指仍然还在屏幕上时（The user is scrolling using touch, and their finger is still on the screen）
     *SCROLL_STATE_FLING是当用户由于之前划动屏幕并抬起手指，屏幕产生惯性滑动时（The user had previously been scrolling using touch and had performed a fling）
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (lastVisibleItem == totalItemCount && scrollState == SCROLL_STATE_IDLE) {
            //加载更多数据
            if (!isLoading) {
                footer.findViewById(R.id.load_layout).setVisibility(View.VISIBLE);
                isLoading = true;
                if (myLoadListener!=null){
                    myLoadListener.onLoad();
                }else{
                    footer.findViewById(R.id.load_layout).setVisibility(View.GONE);
                }
            }

        }
    }
    /**
     * firstVisibleItem 表示在当前屏幕显示的第一个listItem在整个listView里面的位置（下标从0开始）
     * visibleItemCount表示在现时屏幕可以见到的ListItem(部分显示的ListItem也算)总数
     * totalItemCount表示ListView的ListItem总数
     * listView.getLastVisiblePosition()表示在现时屏幕最后一个ListItem
     * (最后ListItem要完全显示出来才算)在整个ListView的位置（下标从0开始）
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.lastVisibleItem = firstVisibleItem + visibleItemCount;
        this.totalItemCount = totalItemCount;
    }

    /**
     * 当ListView item滑出屏幕，回调这个接口
     * 我们需要在回调方法removeItem()中移除该Item,然后刷新ListView
     *
     * @author xiaanming
     */
    public interface RemoveListener {
        public void removeItem(int position);
    }

    //加载更多数据的回调接口
    public interface MyLoadListener {
        public void onLoad();

    }

    public void setInterface(MyLoadListener myLoadListener) {
        this.myLoadListener = myLoadListener;
    }

    public void LoadComplet() {
        isLoading = false;
        footer.findViewById(R.id.load_layout).setVisibility(View.GONE);
    }
}