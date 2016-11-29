package com.example.com.meimeng.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.com.meimeng.MeiMengApplication;
import com.example.com.meimeng.R;
import com.example.com.meimeng.activity.ApplyPersonActivity;
import com.example.com.meimeng.adapter.UserActivityListAdapter;
import com.example.com.meimeng.gson.GsonTools;
import com.example.com.meimeng.gson.gsonbean.UserActivityListBean;
import com.example.com.meimeng.gson.gsonbean.UserActivityListItem;
import com.example.com.meimeng.net.InternetConstant;
import com.example.com.meimeng.net.InternetUtils;
import com.example.com.meimeng.util.DialogUtils;
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
 * Created by wjw
 */

public class MyPrivateActivityFragment extends Fragment {

    private volatile int currentPage = 0;
    private View view;
    private PullToRefreshListView MyListview;
    private UserActivityListAdapter mPrivateAdapter;
    private List<UserActivityListItem> mPrivatelist = new ArrayList<>();
    private UserActivityListBean userActivityListBean;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.tab_listview, container, false);
        MeiMengApplication.currentContext=getActivity();
        MyListview = (PullToRefreshListView) view.findViewById(R.id.custom_listview);
        mPrivateAdapter = new UserActivityListAdapter(getActivity(), mPrivatelist);
        MyListview.setAdapter(mPrivateAdapter);
        MyListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                UserActivityListItem eventData = (UserActivityListItem) mPrivateAdapter.getItem(--i);
                if (eventData.getState() == 0) {
                    Toast.makeText(getActivity(), "当前活动审核中，无人报名", Toast.LENGTH_SHORT).show();
                } else if (eventData.getState() == -1) {
                    Toast.makeText(getActivity(), "您发布的活动未通过审核，请与您的私人红娘进行联系", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getActivity(), ApplyPersonActivity.class);//跳转到报名我发起活动的人的列表页面
                    intent.putExtra("mActivityId", eventData.getActivityId());
                    intent.putExtra("mState", eventData.getState());
                    startActivity(intent);
                }

            }
        });

        init(1);//1表示为我发起的活动

        MyListview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //设置下拉头的时间
                String time_label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后更新：" + time_label);
                init(1);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                refreshView.getLoadingLayoutProxy().setPullLabel("加载更多...");
                initFoot(1);
            }
        });
        return view;
    }

    /**
     * 下拉刷新
     * @param type
     */
    void init(final int type) {

        try {
            if (TextUtils.isEmpty(Utils.getUserId())) {
                return;
            }
            if (TextUtils.isEmpty(Utils.getUserToken())) {
                return;
            }
            String url = InternetConstant.SERVER_URL + InternetConstant.USERACTIVITY_LIST + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();
            JSONStringer jsonStringer = new JSONStringer().object().key("type").value(type).key("currentPage").value(1).key("pageSize").value(5).endObject();
            String jsonStr = jsonStringer.toString();
            //得到Observable并获取返回的数据(主线程中)
            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            //解析json数据，更新UI
                            userActivityListBean = GsonTools.getUserActivityListJson(s);
                            if (userActivityListBean.isSuccess()) {
                                mPrivatelist.clear();
                                List<UserActivityListItem> list = userActivityListBean.getParam().getActivityList();
                                mPrivatelist.addAll(list);
                                mPrivateAdapter.notifyDataSetChanged();
                                currentPage = 2;
                            } else {
                                DialogUtils.setDialog(getActivity(), userActivityListBean.getError());
                            }
                            MyListview.onRefreshComplete();
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
     * 上拉加载
     * @param type
     */
    void initFoot(final int type) {

        try {
            if (TextUtils.isEmpty(Utils.getUserId())) {
                return;
            }
            if (TextUtils.isEmpty(Utils.getUserToken())) {
                return;
            }
            String url = InternetConstant.SERVER_URL + InternetConstant.USERACTIVITY_LIST + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();
            JSONStringer jsonStringer = new JSONStringer().object().key("type").value(type).key("currentPage").value(currentPage).key("pageSize").value(5).endObject();
            String jsonStr = jsonStringer.toString();
            //得到Observable并获取返回的数据(主线程中)
            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            //解析json数据，更新UI
                            List<UserActivityListItem> uplist;
                            userActivityListBean = GsonTools.getUserActivityListJson(s);
                            uplist = userActivityListBean.getParam().getActivityList();
                            if (userActivityListBean.isSuccess() & (uplist.size() != 0)) {
                                mPrivatelist.addAll(uplist);
                                mPrivateAdapter.notifyDataSetChanged();
                                currentPage++;
                            } else if (!userActivityListBean.isSuccess()) {
                                DialogUtils.setDialog(getActivity(), userActivityListBean.getError());
                            } else {
                                Toast.makeText(getActivity(), "无更多内容", Toast.LENGTH_SHORT).show();
                            }
                            MyListview.onRefreshComplete();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}


