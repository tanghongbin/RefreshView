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
import com.example.com.meimeng.activity.OfficeEventDetail;
import com.example.com.meimeng.activity.PrivateEventDetail;
import com.example.com.meimeng.activity.SixPersonDateDetail;
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
public class MyJoinActivityFragment extends Fragment {
    /**
     * 官方活动
     */
    private static final int OFFICEACTIVITY = 1;
    /**
     * 私人活动
     */
    private static final int PRIVATEACTIVITY = 2;
    /**
     * 六人活动
     */
    private static final int SIXACTIVITY = 3;
    private View view;
    private PullToRefreshListView MyListview;
    private UserActivityListAdapter mJoinAdapter;
    private List<UserActivityListItem> mjionlist = new ArrayList<>();
    private UserActivityListBean userActivityListBean;
    private volatile int currentPage = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.tab_listview, container, false);
        MeiMengApplication.currentContext=getActivity();
        MyListview = (PullToRefreshListView) view.findViewById(R.id.custom_listview);
        mJoinAdapter = new UserActivityListAdapter(getActivity(), mjionlist);
        MyListview.setAdapter(mJoinAdapter);
        MyListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                UserActivityListItem eventData = (UserActivityListItem) mJoinAdapter.getItem(--i);
                //判断活动类型
                switch (eventData.getType()) {
                    case OFFICEACTIVITY:
                        Intent officeintent = new Intent(getActivity(), OfficeEventDetail.class);
                        officeintent.putExtra("mActivityId", eventData.getActivityId());
                        startActivity(officeintent);
                        break;
                    case PRIVATEACTIVITY:
                        Intent privateintent = new Intent(getActivity(), PrivateEventDetail.class);
                        privateintent.putExtra("mActivityId", eventData.getActivityId());
                        startActivity(privateintent);
                        break;
                    case SIXACTIVITY:
                        Intent sixintent = new Intent(getActivity(), SixPersonDateDetail.class);
                        sixintent.putExtra("mActivityId", eventData.getActivityId());
                        startActivity(sixintent);
                        break;
                    default:
                        break;

                }
            }
        });

        init(2);//2代表我参加的活动

        MyListview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //设置下拉头的时间
                String time_label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后更新：" + time_label);
                init(2);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                refreshView.getLoadingLayoutProxy().setPullLabel("加载更多...");
                initFoot(2);
            }
        });

        return view;
    }

    /**
     * 下拉刷新接口
     *
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
                                mjionlist.clear();
                                List<UserActivityListItem> list = userActivityListBean.getParam().getActivityList();
                                mjionlist.addAll(list);
                                mJoinAdapter.notifyDataSetChanged();
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
     * 上拉加载接口
     *
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
                            List<UserActivityListItem> uplist = new ArrayList<>();
                            userActivityListBean = GsonTools.getUserActivityListJson(s);
                            uplist = userActivityListBean.getParam().getActivityList();
                            if (userActivityListBean.isSuccess() & (uplist.size() != 0)) {
                                mjionlist.addAll(uplist);
                                mJoinAdapter.notifyDataSetChanged();
                                currentPage++;//页数增加
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


}


