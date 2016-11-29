package com.example.com.meimeng.fragment;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.com.meimeng.MeiMengApplication;
import com.example.com.meimeng.R;
import com.example.com.meimeng.activity.PayActivity;
import com.example.com.meimeng.adapter.GiftListAdapter;
import com.example.com.meimeng.fragment.management.AllFragmentManagement;
import com.example.com.meimeng.gson.GsonTools;
import com.example.com.meimeng.gson.gsonbean.ReceivedGiftItem;
import com.example.com.meimeng.gson.gsonbean.ReceivedGiftListBean;
import com.example.com.meimeng.net.InternetConstant;
import com.example.com.meimeng.net.InternetUtils;
import com.example.com.meimeng.util.DialogUtils;
import com.example.com.meimeng.util.Utils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * 我收到的礼物
 * Created by Administrator on 2015/12/9.
 */
public class IsTomeGiftFragment extends Fragment {

    private PullToRefreshListView gitListView;

    private View view;
    private int gifyType = 1;


    private GiftListAdapter giftListAdapter;

    private ArrayList<ReceivedGiftItem> datalist = new ArrayList<>();

    private volatile int currentPage = 1;
    private Dialog dialog;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AllFragmentManagement.fragmentList.add(this);
        Log.e("Fragment", "IsMyGiftFragment Create");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.gift_manager_item, container, false);

        initview();

        getjson();

        refresh();

        return view;
    }

    /**
     * 初始化数据
     */
    private void initview() {
        gitListView = (PullToRefreshListView) view.findViewById(R.id.git_manager_list);
        datalist.clear();
        giftListAdapter = new GiftListAdapter(getActivity(), datalist,gifyType);
        gitListView.setAdapter(giftListAdapter);
        dialog = Utils.createLoadingDialog(getActivity(), "加载中...");
        dialog.show();

    }


    private void refresh() {
        gitListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

                String time_label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("" + time_label);
                if (InternetUtils.isNetworkConnected(getActivity())) {
                    getjson();
                } else {
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                refreshView.getLoadingLayoutProxy().setPullLabel("...");
                if (InternetUtils.isNetworkConnected(getActivity())) {
                    getfootjson();
                } else {
                }

            }
        });
    }

    /**
     * 我收到的礼物
     */
    private void getjson() {

        try {
            if (TextUtils.isEmpty(Utils.getUserId())) {
                return;
            }
            if (TextUtils.isEmpty(Utils.getUserToken())) {
                return;
            }

            String url = InternetConstant.SERVER_URL + InternetConstant.GIFT_RECEIVED_LIST + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();
            JSONStringer jsonStringer = new JSONStringer().object()
                    .key("giftType").value(gifyType)
                    .key("currentPage").value(1).key("pageSize").value(7)
                    .endObject();
            String jsonStr = jsonStringer.toString();
            timeOutCloseDialog();

            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            ReceivedGiftListBean receivedGiftListBean = GsonTools.getReceivedGiftListJson(s);
                            if (null != dialog)
                                dialog.dismiss();
                            if (receivedGiftListBean.isSuccess()) {
                                if (receivedGiftListBean.getParam().getGifts() == null) {

                                } else {
                                    datalist.clear();
                                    datalist.addAll(receivedGiftListBean.getParam().getGifts());
                                    giftListAdapter.notifyDataSetChanged();
                                    currentPage = 2;
                                }
                                gitListView.onRefreshComplete();
                            } else {
                                DialogUtils.setDialog(getActivity(), receivedGiftListBean.getError());
                            }

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
                }
            } else {

            }
        }
    };


    private void getfootjson() {
        try {
            if (TextUtils.isEmpty(Utils.getUserId())) {
                return;
            }
            if (TextUtils.isEmpty(Utils.getUserToken())) {
                return;
            }

            String url = InternetConstant.SERVER_URL + InternetConstant.GIFT_RECEIVED_LIST + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();
            JSONStringer jsonStringer = new JSONStringer().object()
                    .key("giftType").value(gifyType)
                    .key("currentPage").value(currentPage).key("pageSize").value(7)
                    .endObject();
            String jsonStr = jsonStringer.toString();
            timeOutCloseDialog();

            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            ReceivedGiftListBean receivedGiftListBean = GsonTools.getReceivedGiftListJson(s);
                            if (receivedGiftListBean.isSuccess()) {
                                if (receivedGiftListBean.getParam().getGifts().isEmpty()) {
                                    Toast.makeText(getActivity(), "加载完成", Toast.LENGTH_SHORT).show();
                                } else {
                                    datalist.addAll(receivedGiftListBean.getParam().getGifts());
                                    giftListAdapter.notifyDataSetChanged();
                                    currentPage++;
                                }
                                gitListView.onRefreshComplete();
                            } else {
                                DialogUtils.setDialog(getActivity(), receivedGiftListBean.getError());
                            }
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
