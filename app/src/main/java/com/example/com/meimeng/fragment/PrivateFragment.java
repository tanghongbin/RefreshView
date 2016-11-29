package com.example.com.meimeng.fragment;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.example.com.meimeng.activity.PrivateEventDetail;
import com.example.com.meimeng.adapter.OfficeOrPrivateAdapter;
import com.example.com.meimeng.constants.CommonConstants;
import com.example.com.meimeng.fragment.management.AllFragmentManagement;
import com.example.com.meimeng.gson.GsonTools;
import com.example.com.meimeng.gson.gsonbean.EventData;
import com.example.com.meimeng.gson.gsonbean.EventListBean;
import com.example.com.meimeng.net.BuildString;
import com.example.com.meimeng.net.InternetUtils;
import com.example.com.meimeng.util.DialogUtils;
import com.example.com.meimeng.util.Utils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by Administrator on 2015/7/23.
 */
public class PrivateFragment extends Fragment {
    private static int currentPage = 1;

    private View view;
    private Dialog dialog;
    private PullToRefreshListView MyListview;
    private OfficeOrPrivateAdapter adapter;
    private List<EventData> data = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AllFragmentManagement.eventFragmentList.add(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab_listview, null);
        MyListview = (PullToRefreshListView) view.findViewById(R.id.custom_listview);

        MeiMengApplication.currentContext=getActivity();
        adapter = new OfficeOrPrivateAdapter(getActivity(), R.layout.office_event_listitem, data);
        MyListview.setAdapter(adapter);

        MyListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (MeiMengApplication.sharedPreferences.getInt(CommonConstants.USER_VERFIY, 0) == 0) {
                    Utils.JudgeUserVerfiy(getActivity(), 0);
                } else if (MeiMengApplication.sharedPreferences.getInt(CommonConstants.USER_VERFIY, 0) == 2) {
                    Utils.JudgeUserVerfiy(getActivity(), 2);
                } else {
                    EventData eventData = (EventData) adapter.getItem(--i);
                    Intent intent = new Intent(getActivity(), PrivateEventDetail.class);
                    intent.putExtra("mActivityId", eventData.getActivityId());
                    startActivity(intent);
                }
            }
        });

        dialog = Utils.createLoadingDialog(getActivity(), "载入中...");
        dialog.show();
        init();

        MyListview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //设置下拉头的时间
                String time_label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("更新于：" + time_label);
                init();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                refreshView.getLoadingLayoutProxy().setPullLabel("加载更多...");
                initFoot();
            }
        });

        return view;
    }

    void init() {

        try {
            String url = BuildString.EventListUrl();
            String jsonStr = BuildString.EventListReqBody(2, 1, 5);//测试用，发布时请传入正确参数
            timeOutCloseDialog();
            //得到Observale并获取返回的数据(主线程中)
            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            EventListBean databean = GsonTools.OfficeEventJson(s);
                            if (null != dialog)
                                dialog.dismiss();
                            if (databean.getSuccess()) {
                                data.clear();
                                data = databean.getlist();
                                if (data.size() == 0) {
                                    Toast.makeText(getActivity(), "活动内容为空", Toast.LENGTH_LONG).show();
                                }
                                adapter.clear();
                                adapter.addAll(data);
                                adapter.notifyDataSetChanged();
                                currentPage = 2;
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
     * 网络超时提示
     */
    private void timeOutCloseDialog() {
        Timer timer = new Timer();
        TimerTask tk = new TimerTask() {
            @Override
            public void run() {
                mhandler.sendEmptyMessage(20);
            }
        };
        timer.schedule(tk, 7000);
    }
    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 20) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                    Toast.makeText(getActivity(), "当前网络不可用，请检查您的网络设置", Toast.LENGTH_SHORT).show();
                }
            } else {
                //不做操作
            }
        }
    };
    void initFoot() {

        try {
            String url = BuildString.EventListUrl();
            String jsonStr = BuildString.EventListReqBody(2, currentPage, 5);//测试用，发布时请传入正确参数

            //得到Observale并获取返回的数据(主线程中)
            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            List<EventData> uplist;
                            EventListBean databean = GsonTools.OfficeEventJson(s);
                            uplist = databean.getlist();
                            if (databean.getSuccess() & (uplist.size() != 0)) {
                                adapter.addAll(uplist);
                                adapter.notifyDataSetChanged();
                                currentPage++;
                            } else if (!databean.getSuccess()) {
                                DialogUtils.setDialog(getActivity(), databean.getError());
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
        AllFragmentManagement.eventFragmentList.remove(this);
    }
}

