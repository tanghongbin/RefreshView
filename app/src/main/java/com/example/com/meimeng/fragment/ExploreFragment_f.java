package com.example.com.meimeng.fragment;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.com.meimeng.MeiMengApplication;
import com.example.com.meimeng.R;
import com.example.com.meimeng.activity.HomeActivity;
import com.example.com.meimeng.activity.OthersSelfActivity;
import com.example.com.meimeng.adapter.FancyCoverFlowSampleAdapter;
import com.example.com.meimeng.adapter.MyViewPagerAdapter;
import com.example.com.meimeng.bean.FilterBean;
import com.example.com.meimeng.constants.CommonConstants;
import com.example.com.meimeng.fancycoverflow.FancyCoverFlow;
import com.example.com.meimeng.fragment.management.AllFragmentManagement;
import com.example.com.meimeng.gson.GsonTools;
import com.example.com.meimeng.gson.gsonbean.ExploreListItem;
import com.example.com.meimeng.gson.gsonbean.ExplorejsonBean;
import com.example.com.meimeng.net.InternetConstant;
import com.example.com.meimeng.net.InternetUtils;
import com.example.com.meimeng.util.DialogUtils;
import com.example.com.meimeng.util.Utils;

import org.json.JSONException;
import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;


/**
 * 发现页面的Fragment
 */
public class ExploreFragment_f extends Fragment {

    private List<ExploreListItem> list = new ArrayList<ExploreListItem>();
    private FancyCoverFlow fancyCoverFlow;
    private View view;
    private ExplorejsonBean exploreJson;
    private FancyCoverFlowSampleAdapter adapter;
    private volatile int currentPage = 1;

    private static FilterBean filterBean;
    private String place;
    private TextView topage;
    private TextView topheight;
    private TextView topplace;
    private Dialog dialog;
    private Dialog mdialog;
//
//    private View selectView;

    private Context context;

    private ViewPager viewPager;

    //viewpager的适配器
    private MyViewPagerAdapter mAdapter;
    //整个布局的父布局
    private RelativeLayout relativeLayout;
    //viewpager的参数设置对象
    private RelativeLayout.LayoutParams viewPagerParams;
    private Button button;
    //屏幕的宽
    private int screenWidth;
    //屏幕的高
    private int screenHeight;

    //记录当前发现数量
    public int currentNum = 10;
    private final int REFRESHOVER = 20;
    private final int TIMEOUT = 30;

    private final int LIMITPAGE=2;

    private ArrayList<View> viewlist = new ArrayList<View>();

    private HomeActivity homeActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AllFragmentManagement.fragmentList.add(this);
        Log.e("Fragment", "ExploreFragment Create");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.explore_fragment_f, container, false);
        context = getActivity();
        MeiMengApplication.currentContext = getActivity();
        homeActivity = (HomeActivity) getActivity();
        //初始化视图控件
        initView(view);

        //初始化数据
        init();
        //对人物点击的监听
        itemClick();
        return view;
    }

    /**
     * 拉取下拉列表数据
     */
    private void init() {
        try {
            if (TextUtils.isEmpty(Utils.getUserId())) {
                return;
            }
            if (TextUtils.isEmpty(Utils.getUserToken())) {
                return;

            }
            String url = InternetConstant.SERVER_URL + InternetConstant.EXPLORE_LIST_URL + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();
            Log.d("whh","UId="+Utils.getUserId() + "--TOKEN="+Utils.getUserToken());
            JSONStringer jsonStringer = null;
            String jsonStr = "";

            if (null == filterBean) {
                jsonStringer = new JSONStringer().object().key("currentPage").value(1).key("pageSize").value(10).key("picVerfiy").value(1).endObject();
            } else {
                jsonStringer = getJSONString(1, 10);
            }
            jsonStr = jsonStringer.toString();
            timeOutCloseDialog();
            //得到Observable并获取返回的数据(主线程中)
            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            if (mdialog != null) {
                                mdialog.dismiss();
                                mdialog = null;
                                currentNum = 10;
                            }
                            //解析json数据，更新UI
                            exploreJson = GsonTools.getExploreJson(s);
                            if (null != dialog)
                                dialog.dismiss();
                            if (exploreJson.getSuccess()) {
                                list.clear();
                                list = exploreJson.getParam().getExploreListItem();
                                if (list.size() == 0) {
                                    Toast.makeText(getActivity(), "发现内容为空", Toast.LENGTH_LONG).show();
                                }
                                Log.i("msg", "qk1");
                                mhandler.sendEmptyMessage(REFRESHOVER);
                                adapter.refresh(list);
                                currentPage = 2;
                                //mgridview.onRefreshComplete();
                            } else {
                                DialogUtils.setDialog(context, exploreJson.getError());
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


    /**
     * 初始化视图控件
     *
     * @param view
     */
    private void initView(View view) {
        fancyCoverFlow = (FancyCoverFlow) view.findViewById(R.id.fancyCoverFlow);
        LinearLayout explore_fragment = (LinearLayout) view.findViewById(R.id.explore_fragment);
        adapter = new FancyCoverFlowSampleAdapter(context, list);
        fancyCoverFlow.setAdapter(adapter);
        fancyCoverFlow.setUnselectedAlpha(1.0f);
        fancyCoverFlow.setUnselectedSaturation(0.0f);
        fancyCoverFlow.setUnselectedScale(0.8f);
        fancyCoverFlow.setSpacing(15);
        fancyCoverFlow.setMaxRotation(0);
        fancyCoverFlow.setScaleDownGravity(0.5f);
        fancyCoverFlow.setGravity(Gravity.CENTER);
        fancyCoverFlow.setActionDistance(FancyCoverFlow.ACTION_DISTANCE_AUTO);
        fancyCoverFlow.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fancyCoverFlow.setOnRefreshOrLoadMoreListenner(currentNum, position, new FancyCoverFlow.Callback() {
                    @Override
                    public void onRight() {
                        //加载更多
                        if (mdialog == null) {
                            mdialog = Utils.createLoadingDialog(getActivity(), "加载更多...");
                            mdialog.show();
                            setLoadMore();
                        } else {
                            //网络太差
                            noInternetDialog();
                        }
                    }
                    @Override
                    public void onLeft() {
                        if (mdialog == null) {
                            //刷新
                            mdialog = Utils.createLoadingDialog(getActivity(), "刷新中...");
                            mdialog.show();
                            init();
                        } else {
                            //网络太差
                            noInternetDialog();
                        }
                    }
                });
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                Log.d("wz", "parent=" + parent);
            }
        });


        Utils.readBitMap(getActivity(),explore_fragment,R.raw.explore_back);
        dialog = Utils.createLoadingDialog(getActivity(), "载入中...");
        dialog.show();
    }

    /**
     * 设置未通过审核只能看20名
     * */
    private void setLoadMore() {
        if (currentPage >LIMITPAGE) {
            if (MeiMengApplication.sharedPreferences.getBoolean(CommonConstants.MYSTATE, false) == false) {
                homeActivity.setAlertDialog_a();//资料未填写完成，提示完善资料
            } else {//资料填写完成
                Utils.getUerVerfiy();//重新网络获取审核状态
                switch (MeiMengApplication.sharedPreferences.getInt(CommonConstants.USER_VERFIY, 0)) {
                    //0，用户待审核中
                    case 0:
                        //Utils.setPromptDialog(getActivity(), "您提交的资料正在审核状态。如有疑问，请联系美盟客服：400-8783-520");
                        Utils.setPromptDialog(getActivity(), "未通过审核，无法查看更多");
                        break;
                    case 1:
                        initFoot();
                        break;
                    //2，未通过审核
                    case 2:
                        //Utils.setPromptDialog(getActivity(), "对不起，您暂不符合美盟会员要求，未通过审核。您可以关注美盟订阅号:meimeng88888，获取更多信息。");
                        Utils.setPromptDialog(getActivity(), "未通过审核，无法查看更多");
                        break;
                }
            }
            if (mdialog != null) {
                mdialog.dismiss();
                mdialog = null;
            }
        }else {
            initFoot();
        }
    }

    /**
     * 上拉刷新数据，加载更多
     */
    private void initFoot() {
        try {
            if (TextUtils.isEmpty(Utils.getUserId())) {
                return;
            }
            if (TextUtils.isEmpty(Utils.getUserToken())) {
                return;
            }
            String url = InternetConstant.SERVER_URL + InternetConstant.EXPLORE_LIST_URL + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();
            JSONStringer jsonStringer;

            if (null == filterBean) {
                jsonStringer = new JSONStringer().object().key("currentPage").value(currentPage).key("pageSize").value(10).key("picVerfiy").value(1).endObject();
            } else {

                jsonStringer = getJSONString(currentPage, 10);

            }
            String jsonStr = jsonStringer.toString();
            timeOutCloseDialog();
            //得到Observable并获取返回的数据(主线程中)
            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {

                            if (mdialog != null) {
                                mdialog.dismiss();
                                mdialog = null;
                            }
                            //解析json数据，更新UI
                            List<ExploreListItem> uplist;
                            exploreJson = GsonTools.getExploreJson(s);
                            uplist = exploreJson.getParam().getExploreListItem();
                            if (exploreJson.getSuccess()) {
                                if (uplist.size() > 0) {
                                    list.addAll(uplist);
                                    adapter.refresh(list);
                                    currentNum = list.size();
                                    currentPage++;
                                } else {
                                    Toast.makeText(context, "无更多内容", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                DialogUtils.setDialog(context, exploreJson.getError());
                            }
                            //mgridview.onRefreshComplete();
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
                mhandler.sendEmptyMessage(TIMEOUT);
            }
        };
        timer.schedule(tk, 7000);
    }

    /**
     * 网络太差网络提示
     */
    private void noInternetDialog() {
        Timer timer = new Timer();
        TimerTask tk = new TimerTask() {
            @Override
            public void run() {
                mdialog = null;
            }
        };
        timer.schedule(tk, 4000);
    }

    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {


            switch (msg.what) {
                case REFRESHOVER:
                    Log.i("msg", "111");
//
//                    for (ExploreListItem item : list) {
//
////                        View view = LayoutInflater.from(context).inflate(R.layout.layout_explore_item,null);
////                        ImageView view = new ImageView(context);
////                        view.setImageResource(R.color.bar_pressed);
////
////                        viewlist.add(view);
//                        fragmentList.add(new NewStrategyFragment());
//
//                    }
//                    Log.i("msg","size   :   "+viewlist.size());
// //                   mAdapter.notifyDataSetChanged();
//                    viewPager.setCurrentItem(fragmentList.size() - 1);
//                    viewPager.setOffscreenPageLimit(fragmentList.size());
                    break;
                case TIMEOUT:
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                        Toast.makeText(getActivity(), "当前网络不可用，请检查您的网络设置", Toast.LENGTH_SHORT).show();
                    } else {

                    }

                    break;

            }
        }

    };

    /**
     * 对人物点击的监听
     */
    private void itemClick() {

        fancyCoverFlow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (MeiMengApplication.sharedPreferences.getBoolean(CommonConstants.MYSTATE, false) == false) {
                    homeActivity.setAlertDialog_a();//资料未填写完成，提示完善资料
                } else {//资料填写完成
                    Utils.getUerVerfiy();//重新网络获取审核状态
                    switch (MeiMengApplication.sharedPreferences.getInt(CommonConstants.USER_VERFIY, 0)) {
                        //0，用户待审核中
                        case 0:
                            Utils.setPromptDialog(getActivity(), "您提交的资料正在审核状态。如有疑问，请联系美盟客服：400-8783-520");
                            break;
                        case 1:
                            ExploreListItem exploreListItem = adapter.getItem(position);
                            Intent intent;
                            intent = new Intent(getActivity(), OthersSelfActivity.class);
                            intent.putExtra("targetUid", exploreListItem.getUid());
                            startActivity(intent);
                            break;
                        //2，未通过审核
                        case 2:
                            Utils.setPromptDialog(getActivity(), "对不起，您暂不符合美盟会员要求，未通过审核。您可以关注美盟订阅号:meimeng88888，获取更多信息。");

                            break;
                    }
                }


            }
        });

    }


    /**
     * 获取搜索请求JSON字段
     *
     * @param currentPage
     * @param pageSize
     * @return
     */
    private JSONStringer getJSONString(int currentPage, int pageSize) {
        try {
            JSONStringer jsonStringer = new JSONStringer().object();
            if (filterBean.getAgeStart() != 0) {
                jsonStringer.key("ageStart").value(filterBean.getAgeStart());
            }
            if (filterBean.getAgeEnd() != 0) {
                jsonStringer.key("ageEnd").value(filterBean.getAgeEnd());
            }
            if (filterBean.getHeightStart() != 0) {
                jsonStringer.key("heightStart").value(filterBean.getHeightStart());
            }
            if (filterBean.getHeightEnd() != 0) {
                jsonStringer.key("heightEnd").value(filterBean.getHeightEnd());
            }
            Integer residence = filterBean.getResidence();
            if (residence != -1) {
                if (residence == 1) {
                    residence = null;
                }
                jsonStringer.key("residence").value(residence);
            }
//

            jsonStringer.key("currentPage").value(currentPage).key("pageSize").value(pageSize)
                    .endObject();
            return jsonStringer;

        } catch (JSONException e) {
            e.printStackTrace();

        }
        return null;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        AllFragmentManagement.fragmentList.remove(this);
        Log.e("Fragment", "ExploreFragment Destroy");
    }

}
