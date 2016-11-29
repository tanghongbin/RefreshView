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
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.com.meimeng.MeiMengApplication;
import com.example.com.meimeng.R;
import com.example.com.meimeng.activity.OthersSelfActivity;
import com.example.com.meimeng.activity.SeniorExploreActivity;
import com.example.com.meimeng.adapter.ExploreListAdapter;
import com.example.com.meimeng.adapter.MyViewPagerAdapter;
import com.example.com.meimeng.bean.FilterBean;
import com.example.com.meimeng.constants.CommonConstants;
import com.example.com.meimeng.fragment.management.AllFragmentManagement;
import com.example.com.meimeng.gson.GsonTools;
import com.example.com.meimeng.gson.gsonbean.ExploreListItem;
import com.example.com.meimeng.gson.gsonbean.ExplorejsonBean;
import com.example.com.meimeng.net.InternetConstant;
import com.example.com.meimeng.net.InternetUtils;
import com.example.com.meimeng.util.DialogUtils;
import com.example.com.meimeng.util.Utils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

import org.json.JSONException;
import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

//import android.support.v4.app.Fragment;

//import android.app.Fragment;

/**
 * 发现页面的Fragment
 */
public class ExploreFragment extends Fragment {
    private List<ExploreListItem> list = new ArrayList<ExploreListItem>();
    private PullToRefreshGridView mgridview;

    private ExplorejsonBean exploreJson;
    private ExploreListAdapter myexploreadapter;
    private volatile int currentPage = 1;

    private static FilterBean filterBean;
    private String place;
    private TextView topage;
    private TextView topheight;
    private TextView topplace;
    private Dialog dialog;
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

    private final int REFRESHOVER = 20;
    private final int TIMEOUT = 30;

    private ArrayList<View> viewlist = new ArrayList<View>();

    private SeniorExploreActivity seniorExploreActivity;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AllFragmentManagement.fragmentList.add(this);
        Log.e("Fragment", "ExploreFragment Create");

    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.exploregridview_f, container, false);
        context = getActivity();
        seniorExploreActivity = (SeniorExploreActivity)getActivity();

        MeiMengApplication.currentContext = getActivity();
        //初始化视图控件
        initView(view);

        //初始化数据
        // init();

        //刷新设置
        PullDownToRefresh();

        //搜索条件设置按钮
//       selecctConditons();
        selecct();

        //对人物点击的监听
        itemClick();


        return view;
    }

    /**
     * 搜索
     */
    private void selecct() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bundle bundle = getArguments();
                String s = bundle.getString("jsonstr");
                Log.i("json", "ExploreFragment  :  " + s);
                filterBean = (FilterBean) bundle.getSerializable("mFilterBean");
                if (TextUtils.isEmpty(Utils.getUserId())) {
                    return;
                }
                if (TextUtils.isEmpty(Utils.getUserToken())) {
                    return;
                }
                String url = InternetConstant.SERVER_URL + InternetConstant.EXPLORE_LIST_URL + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();
                Observable observable = InternetUtils.getJsonObservale(url, s);
                observable.observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<String>() {
                            @Override
                            public void call(String s) {
                                ExplorejsonBean explorejsonBean = GsonTools.getExploreJson(s);
                                if (null != dialog){
                                    dialog.dismiss();
                                }
                                list.clear();
                                list = explorejsonBean.getParam().getExploreListItem();
                                myexploreadapter.refresh(list);
                                mgridview.onRefreshComplete();

                                if (list.size() == 0) {
                                    Toast.makeText(getActivity(), "对不起，搜索不到该用户！", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable error) {
                                Log.e("test:", error.getMessage());
                            }
                        });
            }
        }).start();
    }


    /**
     * 初始化视图控件
     *
     * @param view
     */
    private void initView(View view) {
        mgridview = (PullToRefreshGridView) view.findViewById(R.id.grid_view_explore);
        topage = (TextView) view.findViewById(R.id.exploregridview_f_age);
        topheight = (TextView) view.findViewById(R.id.exploregridview_f_height);
        topplace = (TextView) view.findViewById(R.id.exploregridview_f_place);
        //selectView = view.findViewById(R.id.exploregridview_f_onclick);
        //初始化适配器
        myexploreadapter = new ExploreListAdapter(context, list);
        mgridview.setAdapter(myexploreadapter);
        dialog = Utils.createLoadingDialog(getActivity(), "载入中...");
        dialog.show();


    }

    private ArrayList<android.support.v4.app.Fragment> fragmentList = new ArrayList<>();


    /**
     * 下拉刷新、上拉刷新
     */
    private void PullDownToRefresh() {
        mgridview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
                String time_label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("更新于：" + time_label);
                init();
                // mgridview.onRefreshComplete();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
                refreshView.getLoadingLayoutProxy().setPullLabel("加载更多...");
                initFoot();
            }
        });
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
                                myexploreadapter.refresh(list);
                                currentPage = 2;

                                mgridview.onRefreshComplete();
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
                    mgridview.onRefreshComplete();
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

                            //解析json数据，更新UI
                            List<ExploreListItem> uplist;
                            exploreJson = GsonTools.getExploreJson(s);
                            uplist = exploreJson.getParam().getExploreListItem();
                            if (exploreJson.getSuccess()) {
                                if (uplist.size() > 0) {
                                    list.addAll(uplist);
                                    myexploreadapter.refresh(list);
                                    currentPage++;
                                } else {
                                    Toast.makeText(context, "无更多内容", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                DialogUtils.setDialog(context, exploreJson.getError());
                            }
                            mgridview.onRefreshComplete();
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
     * 搜索条件设置按钮
     */
    private void selecctConditons() {
//        selectView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (MeiMengApplication.sharedPreferences.getInt(CommonConstants.USER_VERFIY, 0) == 0) {
//                    Utils.JudgeUserVerfiy(context, 0);
//                } else if (MeiMengApplication.sharedPreferences.getInt(CommonConstants.USER_VERFIY, 0) == 2) {
//                    Utils.JudgeUserVerfiy(context, 2);
//                } else {
//                    Intent intent = new Intent();
//                   intent.setClass(getActivity(), ExploreScreen.class);
//
//                    JSONStringer jsonStringer;
//                    if (null == filterBean) {
//                        try {
//                            jsonStringer = new JSONStringer().object().key("currentPage").value(1).key("pageSize").value(6).endObject();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    } else {
//
//                        intent.putExtra("condition", filterBean);
//
//                    }
//
//
//                    startActivityForResult(intent, 1);
//                }
//            }
//        });
    }

    /**
     * 对人物点击的监听
     */
    private void itemClick() {

        mgridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (MeiMengApplication.sharedPreferences.getBoolean(CommonConstants.MYSTATE, false)==true) {
                    Utils.getUerVerfiy();//重新网络获取审核状态
                    switch (MeiMengApplication.sharedPreferences.getInt(CommonConstants.USER_VERFIY, 0)) {
                        //0，用户待审核中
                        case 0:
                            Utils.setPromptDialog(getActivity(), "您提交的资料正在审核状态。如有疑问，请联系美盟客服：400-8783-520");
                            break;
                        case 1:

                            ExploreListItem exploreListItem = myexploreadapter.getItem(position);
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
                }//资料填写完成

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

    /**
     * 获取搜索请求filterBean类
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case 2:
                String s = data.getStringExtra("json");
                filterBean = (FilterBean) data.getSerializableExtra("condition");
                place = data.getStringExtra("place");

                //设置顶部信息
//                setTop();
                //获取筛选后的信息
                if (TextUtils.isEmpty(Utils.getUserId())) {
                    return;
                }
                if (TextUtils.isEmpty(Utils.getUserToken())) {
                    return;
                }
                String url = InternetConstant.SERVER_URL + InternetConstant.EXPLORE_LIST_URL + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();
                Observable observable = InternetUtils.getJsonObservale(url, s);
                observable.observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<String>() {
                            @Override
                            public void call(String s) {

                                ExplorejsonBean explorejsonBean = GsonTools.getExploreJson(s);
                                list.clear();
                                list = explorejsonBean.getParam().getExploreListItem();
                                myexploreadapter.refresh(list);
                                mgridview.onRefreshComplete();

                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable error) {
                                Log.e("test:", error.getMessage());
                            }
                        });

                break;
            default:
                break;
        }
    }

//    /**
//     *顶部条件字段显示
//     */
//    private void setTop() {
//
//        if (null != filterBean) {
//            if (filterBean.getAgeStart() == 0 && filterBean.getAgeEnd() == 0) {
//                topage.setText("不限");
//            } else if (filterBean.getAgeStart() == 0 && filterBean.getAgeEnd() != 0) {
//                topage.setText("不限~" + filterBean.getAgeEnd() + "岁");
//            } else {
//                topage.setText(String.valueOf(filterBean.getAgeStart()) + "~" + String.valueOf(filterBean.getAgeEnd()) + "岁");
//            }
//
//            if (filterBean.getHeightStart() == 0 && filterBean.getHeightEnd() == 0) {
//                topheight.setText("不限");
//            } else if (filterBean.getHeightStart() == 0 && filterBean.getHeightEnd() != 0) {
//                topheight.setText("不限~" + filterBean.getHeightEnd() + "cm");
//            } else {
//                topheight.setText(String.valueOf(filterBean.getHeightStart()) + "~" + filterBean.getHeightEnd() + "cm");
//            }
//
//        }
//        if (null != place) {
//            topplace.setText(place);
//        }
//
//    }

}
