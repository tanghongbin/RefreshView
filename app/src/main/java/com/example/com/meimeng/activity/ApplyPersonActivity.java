package com.example.com.meimeng.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.com.meimeng.MeiMengApplication;
import com.example.com.meimeng.R;
import com.example.com.meimeng.adapter.ApplyListAdapter;
import com.example.com.meimeng.gson.GsonTools;
import com.example.com.meimeng.gson.gsonbean.ApplyUserItem;
import com.example.com.meimeng.gson.gsonbean.ApplyUserListBean;
import com.example.com.meimeng.net.InternetConstant;
import com.example.com.meimeng.net.InternetUtils;
import com.example.com.meimeng.util.DialogUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONStringer;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by wjw on 2015/7/17.
 * 获取我发起的活动的报名用户列表Activity
 */
public class ApplyPersonActivity extends BaseActivity {

    @Bind(R.id.bow_arrow_image_view)
    ImageView imageView;

    @Bind(R.id.title_left_arrow_image_view)
    ImageView leftArrowImageView;

    @Bind(R.id.title_text)
    TextView titleText;

    @Bind(R.id.custom_listview)
    PullToRefreshListView lv;

    private volatile int currentPage = 1;

    Context context = this;
    ArrayList<ApplyUserItem> mlist = new ArrayList<>();
    private ApplyListAdapter madapter;
    private ApplyUserListBean applyUserListBean;

    private long activityId;
    private int state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.event_applypersonlistview);
        ButterKnife.bind(this);
        MeiMengApplication.currentContext=this;
        Intent intent = getIntent();
        activityId = intent.getExtras().getLong("mActivityId");
        state = intent.getExtras().getInt("mState");

        init();
        getJson(activityId);

        lv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //设置下拉头的时间
                String time_label = DateUtils.formatDateTime(ApplyPersonActivity.this, System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后更新：" + time_label);
                getJson(activityId);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                refreshView.getLoadingLayoutProxy().setPullLabel("加载更多...");
                getFootJson(activityId);
            }
        });

    }

    void init() {

        leftArrowImageView.setVisibility(View.VISIBLE);
        titleText.setText("我的邀约");
        imageView.setVisibility(View.GONE);
        madapter = new ApplyListAdapter(context, R.layout.event_applypersonlistitem, mlist, activityId,state);
        lv.setAdapter(madapter);

    }

    /**
     * 获取我发起的活动的报名用户列表 获取Json数据并解析展示在对应控件中
     * 下拉
     *
     * @param activityId 活动id
     */
    private void getJson(final long activityId) {
        try {
            //构造url和json格式的请求参数
            String url = InternetConstant.SERVER_URL + InternetConstant.USERACTIVITY_APPLYLIST + InternetConstant.USETOKEN;
            JSONStringer jsonStringer = new JSONStringer().object().key("activityId").value(activityId).key("currentPage").value(1).key("pageSize").value(5).endObject();
            String jsonStr = jsonStringer.toString();
            //得到Observable并获取返回的数据(主线程中)
            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            //解析json数据，更新UI
                            applyUserListBean = GsonTools.getApplyUserListBean(s);
                             if (applyUserListBean.isSuccess()) {
                                mlist.clear();
                                mlist = applyUserListBean.getReturnValue();
                                madapter.clear();
                                madapter.addAll(mlist);

                                madapter.notifyDataSetChanged();
                                currentPage = 2;
                            } else {
                                 DialogUtils.setDialog(ApplyPersonActivity.this,applyUserListBean.getError());
                            }
                            lv.onRefreshComplete();
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable error) {
                            Log.e("test:", error.getMessage());
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取我发起的活动的报名用户列表 获取Json数据并解析展示在对应控件中
     * 上拉
     *
     * @param activityId 活动id
     */
    private void getFootJson(final long activityId) {
        try {
            //构造url和json格式的请求参数
            String url = InternetConstant.SERVER_URL + InternetConstant.USERACTIVITY_APPLYLIST + InternetConstant.USETOKEN;
            JSONStringer jsonStringer = new JSONStringer().object().key("activityId").value(activityId).key("currentPage").value(currentPage).key("pageSize").value(5).endObject();
            String jsonStr = jsonStringer.toString();
            //得到Observable并获取返回的数据(主线程中)
            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            //解析json数据，更新UI
                            applyUserListBean = GsonTools.getApplyUserListBean(s);
                            ArrayList<ApplyUserItem> uplist = applyUserListBean.getReturnValue();
                            if (applyUserListBean.isSuccess() & (uplist.size() != 0)) {
                                madapter.addAll(uplist);
                                madapter.notifyDataSetChanged();
                                currentPage++;
                            } else if (!applyUserListBean.isSuccess()) {
                                DialogUtils.setDialog(ApplyPersonActivity.this,applyUserListBean.getError());
                            } else {
                                Toast.makeText(ApplyPersonActivity.this, "无更多内容", Toast.LENGTH_SHORT).show();
                            }
                            lv.onRefreshComplete();
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable error) {
                            Log.e("test:", error.getMessage());
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @OnClick(R.id.title_left_arrow_image_view)
    void BackListener() {
        finish();
    }
}
