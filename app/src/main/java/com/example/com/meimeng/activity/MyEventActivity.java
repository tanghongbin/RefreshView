package com.example.com.meimeng.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.com.meimeng.MeiMengApplication;
import com.example.com.meimeng.R;
import com.example.com.meimeng.adapter.MyEventAdater;
import com.example.com.meimeng.gson.GsonTools;
import com.example.com.meimeng.gson.gsonbean.MyEVentItem;
import com.example.com.meimeng.gson.gsonbean.MyEventList;
import com.example.com.meimeng.gson.gsonbean.MyEventObj;
import com.example.com.meimeng.net.InternetConstant;
import com.example.com.meimeng.net.InternetUtils;
import com.example.com.meimeng.util.Utils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by Administrator on 2015/11/25.
 */
public class MyEventActivity extends BaseActivity implements View.OnClickListener {


    private final int  PULLDOWN = 10; //下拉
    private final int  PULLUP = 20; //上拉

    private static int currentPage = 1;//拉取数据的分页
    private List<MyEventList> list = new ArrayList<>();//数据集合


    private PullToRefreshListView myevent;
    private MyEventAdater adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_myevent);
        MeiMengApplication.currentContext=this;
        //初始化视图
        initView();
        //初始化数据
        initData(PULLDOWN, 1);
        //刷新活动数据
        refreshData();
        //点击查看活动详情
        onClickDetails();
    }

    /**
     * 初始化视图
     */
    private void initView() {

        initTitleBar("我的活动", R.drawable.icon_nav_back, -1, this);

        myevent = (PullToRefreshListView) findViewById(R.id.myevent_listview);
        adapter = new MyEventAdater(this);
        myevent.setAdapter(adapter);
    }


    /**
     * 初始化数据
     */
    private void initData(final int handlerStr,final int currentPage) {
        try{
            if(TextUtils.isEmpty(Utils.getUserId())){
                return;
            }
            if(TextUtils.isEmpty(Utils.getUserToken())){
                return;
            }
            String url = InternetConstant.SERVER_URL + InternetConstant.USERACTIVITY_LIST + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();
            JSONStringer jsonstring = new JSONStringer().object().key("type").value(1).key("currentPage").value(currentPage).key("pageSize").value(5).endObject();
            String jsonStr = jsonstring.toString();

            //得到Observable并获取返回的数据(主线程中)
            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            if(handlerStr == PULLDOWN){
                                list.clear();
                            }
                            MyEventObj param = GsonTools.getMyEventObjJson(s);
                            MyEVentItem item = param.getParam();
                            list.addAll(item.getMyActivityList());
                            mhandler.sendEmptyMessage(handlerStr);

                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {

                        }
                    });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * PullToRefreshListView 上下拉刷新
     */
    private void refreshData() {
        myevent.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {


                String time_label = DateUtils.formatDateTime(MyEventActivity.this, System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("更新于：" + time_label);
                currentPage = 1;
                initData(PULLDOWN, currentPage);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                refreshView.getLoadingLayoutProxy().setPullLabel("加载更多...");
                initData(PULLUP, currentPage);
            }
        });

    }

    /**
     * 点击查看活动详情
     */
    private void onClickDetails() {


        myevent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        MyEventList eventData = (MyEventList) adapter.getItem(--position);
                        Intent intent = new Intent(MyEventActivity.this, OfficeEventDetail.class);
                        intent.putExtra("mActivityId", eventData.getActivityId());

                        startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.more_search_layout: //返回键
                super.onBackPressed();
                break;
        }

    }

    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case PULLDOWN:
                    adapter.setList(list);
                    adapter.notifyDataSetChanged();
                    currentPage = 2;
                    break;
                case PULLUP:
                    adapter.setList(list);
                    adapter.notifyDataSetChanged();
                    currentPage++;
                    break;
            }
            myevent.onRefreshComplete();
        }
    };
}
