package com.example.com.meimeng.fragment;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.com.meimeng.R;
import com.example.com.meimeng.adapter.HomeListAdapter;
import com.example.com.meimeng.fragment.management.AllFragmentManagement;
import com.example.com.meimeng.gson.GsonTools;
import com.example.com.meimeng.gson.gsonbean.HomeJsonBean;
import com.example.com.meimeng.gson.gsonbean.HomeListItem;
import com.example.com.meimeng.net.InternetConstant;
import com.example.com.meimeng.net.InternetUtils;
import com.example.com.meimeng.util.DialogUtils;
import com.example.com.meimeng.util.Utils;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * 首页Fragment
 */
public class HomeFragment extends Fragment implements View.OnClickListener{

    private List<HomeListItem> mlist = new ArrayList<HomeListItem>();
    private PullToRefreshListView mlistview;
    private Context context;
    private HomeJsonBean homeJson;
    private HomeListAdapter madapter;
    private volatile int currentPage = 1;
    private Dialog dialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AllFragmentManagement.fragmentList.add(this);
        Log.e("Fragment", "HomeFragment Create");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.homelistview_f, container, false);
        context = getActivity();

//        mlistview = (PullToRefreshListView) view.findViewById(R.id.list_view_home);
//        madapter = new HomeListAdapter(context, mlist);
//        mlistview.setAdapter(madapter);
//        dialog = Utils.createLoadingDialog(getActivity(), "载入中...");
//        dialog.show();
//        getjson();
//        mlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (MeiMengApplication.sharedPreferences.getInt(CommonConstants.USER_VERFIY, 0) == 0) {
//                    Utils.JudgeUserVerfiy(context, 0);
//                } else if (MeiMengApplication.sharedPreferences.getInt(CommonConstants.USER_VERFIY, 0) == 2) {
//                    Utils.JudgeUserVerfiy(context, 2);
//                } else {
//                    Intent intent = new Intent(getActivity(), OtherStoryActivity.class);
//                    long targetUid = mlist.get(--position).getUid();
//                    intent.putExtra("targetUid", targetUid);
//                    startActivity(intent);
//                }
//            }
//        });
//        // 设置下拉刷新和上拉加载
//        mlistview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
//            @Override
//            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
//                //设置下拉头的时间
//                String time_label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
//                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
//                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("更新于：" + time_label);
//                getjson();
//            }
//
//            @Override
//            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
//                refreshView.getLoadingLayoutProxy().setPullLabel("加载更多...");
//                getfootjson();
//
//            }
//        });



        return view;
    }

    /**
     * 获得下拉刷新获得Json数据并得到解析
     */
    private void getjson() {
        try {
            if (TextUtils.isEmpty(Utils.getUserId())) {
                return;
            }
            if (TextUtils.isEmpty(Utils.getUserToken())) {
                return;
            }
            //构造url和json格式的请求参数
            String url = InternetConstant.SERVER_URL + InternetConstant.HOME_LIST_URL + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();
            JSONStringer jsonStringer = new JSONStringer().object().key("currentPage").value(1).key("pageSize").value(5).endObject();
            String jsonStr = jsonStringer.toString();
            timeOutCloseDialog();
            //得到Observable并获取返回的数据(主线程中)
            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            //解析json数据，更新UI
                            homeJson = GsonTools.getHomeJson(s);
                            if (null != dialog)
                                dialog.dismiss();
                            if (homeJson.isSuccess()) {
                                mlist.clear();
                                mlist = homeJson.getParam().getHomeRecommendation();
                                if(mlist.size()==0){
                                    Toast.makeText(getActivity(),"推荐用户为空",Toast.LENGTH_LONG).show();
                                }
                                madapter.refresh(mlist);
                                currentPage = 2;
                            } else {
                                DialogUtils.setDialog(context, homeJson.getError());
                            }
                            mlistview.onRefreshComplete();
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
    /**
     * 获得上拉加载Json数据并得到解析
     */
    private void getfootjson() {

        try {
            //构造url和json格式的请求参数
            if (TextUtils.isEmpty(Utils.getUserId())) {
                return;
            }
            if (TextUtils.isEmpty(Utils.getUserToken())) {
                return;
            }
            //构造url和json格式的请求参数
            String url = InternetConstant.SERVER_URL + InternetConstant.HOME_LIST_URL + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();
            JSONStringer jsonStringer = new JSONStringer().object().key("currentPage").value(currentPage).key("pageSize").value(5).endObject();
            String jsonStr = jsonStringer.toString();
            timeOutCloseDialog();
            //得到Observable并获取返回的数据(主线程中)
            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            //解析json数据，更新UI
                            List<HomeListItem> uplist;
                            homeJson = GsonTools.getHomeJson(s);
                            uplist = homeJson.getParam().getHomeRecommendation();
                            if ((uplist.size() != 0) & homeJson.isSuccess()) {
                                mlist.addAll(uplist);
                                madapter.refresh(mlist);
                                currentPage++;
                            } else if (!homeJson.isSuccess()) {
                                DialogUtils.setDialog(context, homeJson.getError());
                            } else {
                                Toast.makeText(context, "无更多用户", Toast.LENGTH_SHORT).show();
                            }

                            mlistview.onRefreshComplete();
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
        AllFragmentManagement.fragmentList.remove(this);
        Log.e("Fragment", "HomeFragment Destroy");
    }

    @Override
    public void onClick(View v) {

    }
}
