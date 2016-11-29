package com.example.com.meimeng.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.com.meimeng.MeiMengApplication;
import com.example.com.meimeng.R;
import com.example.com.meimeng.activity.HomeActivity;
import com.example.com.meimeng.activity.OfficeEventDetail;
import com.example.com.meimeng.adapter.OfficeOrPrivateAdapter;
import com.example.com.meimeng.bean.CityBean;
import com.example.com.meimeng.constants.CommonConstants;
import com.example.com.meimeng.custom.PickerView;
import com.example.com.meimeng.db.DbOpenHelper;
import com.example.com.meimeng.fragment.management.AllFragmentManagement;
import com.example.com.meimeng.gson.GsonTools;
import com.example.com.meimeng.gson.gsonbean.EventData;
import com.example.com.meimeng.gson.gsonbean.EventListBean;
import com.example.com.meimeng.net.BuildString;
import com.example.com.meimeng.net.InternetUtils;
import com.example.com.meimeng.util.DataUtil;
import com.example.com.meimeng.util.DialogUtils;
import com.example.com.meimeng.util.GetDbUtils;
import com.example.com.meimeng.util.Utils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class EventFragment extends Fragment implements View.OnClickListener {



    private volatile String singleContent = null;
    private volatile int singleId = 0;

    private static int currentPage = 1;//拉取数据的分页

    private LinearLayout region;
    private LinearLayout state;
    private TextView month;

    private ImageView regionImg;
    private ImageView stateImg;

    private PullToRefreshListView event_listview;
    private OfficeOrPrivateAdapter adapter;
    private List<EventData> datalist = new ArrayList<>();

    private final int  PULLDOWN = 10; //下拉
    private final int  PULLUP = 20; //上拉
    private final int  REQUEST = 1;
    private final int  ANIMATIONOVER = 1; //退出动画结束

    private View view;
    private RelativeLayout select_rl;
    private DataUtil dataUtil;

    private TextView name;
    private RelativeLayout rl;
    private PickerView seleckData_a;
    private PickerView seleckData_b;

    private  int stateid = -1;
    private int placeid = -1;


    private HomeActivity homeActivity;

    private JSONStringer request ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AllFragmentManagement.fragmentList.add(this);
        Log.e("Fragment", "EventFragment Create");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.from(getActivity()).inflate(R.layout.evemt_custom_layout, null);
        homeActivity = (HomeActivity) getActivity();
        MeiMengApplication.currentContext=getActivity();
        dataUtil = new DataUtil(getActivity());
        dataUtil.getProvinceList();
        //初始化界面
        initView();
        //初始化数据
        initData(PULLDOWN, 1);
        //点击查看活动详情
        onClickDetails();
        //刷新活动数据
        refreshData();


        return view;
    }

    /**
     * 初始化界面控件
     */
    private void initView(){

        //初始化可点击的按钮
        region = (LinearLayout) view.findViewById(R.id.event_region);
        state = (LinearLayout) view.findViewById(R.id.event_state);
//        month = (TextView) view.findViewById(R.id.event_month);
        region.setOnClickListener(this);
        state.setOnClickListener(this);

        regionImg = (ImageView) view.findViewById(R.id.event_region_img);
        stateImg = (ImageView) view.findViewById(R.id.event_state_img);
        name=(TextView)view.findViewById(R.id.select_name_text);

        //初始化ListView
        event_listview = (PullToRefreshListView) view.findViewById(R.id.event_listview);
        adapter = new OfficeOrPrivateAdapter(getActivity(), R.layout.office_event_listitem, datalist);
        event_listview.setAdapter(adapter);

        select_rl = (RelativeLayout) view.findViewById(R.id.select_layout);
        TextView cancel = (TextView) select_rl.findViewById(R.id.select_cancel_text);
        TextView sure = (TextView) select_rl.findViewById(R.id.select_sure_text);
        cancel.setOnClickListener(this);
        sure.setOnClickListener(this);

    }



    /**
     * 获得活动数据
     * currentPage 分页的页数
     */
    private void initData(final int handlerStr,final int currentPage){
        if(handlerStr == PULLDOWN){
            MeiMengApplication.cityBean = new CityBean();
            stateid = -1;
        }
        try{
            String url = BuildString.EventListUrl();
            String jsonStr = "";

            JSONStringer jsonStringer = new JSONStringer().object();
            if(MeiMengApplication.cityBean.getCityId() !=-1) {
                jsonStringer.key("place").value(MeiMengApplication.cityBean.getCityId());
            }
            if(stateid != -1){
                jsonStringer.key("state").value(stateid);
            }
            jsonStringer.key("currentPage").value(currentPage).key("pageSize").value(5).endObject();
            jsonStr = jsonStringer.toString();

            //得到Observale并获取返回的数据(主线程中)
            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            EventListBean databean = GsonTools.OfficeEventJson(s);

                            if (databean.getSuccess()) {
                                datalist.clear();
                                datalist = databean.getlist();
                                if (datalist.size() == 0) {
                                    Toast.makeText(getActivity(), "无更多内容", Toast.LENGTH_LONG).show();
                                }
                                if(currentPage == 1){
                                    adapter.clear();
                                }
                                adapter.addAll(datalist);
                                adapter.notifyDataSetChanged();//刷新适配器
                                mhandler.sendEmptyMessage(handlerStr);
                            }else if (!databean.getSuccess()) {
                                DialogUtils.setDialog(getActivity(), databean.getError());
                            }

                        }
                    },new Action1<Throwable>() {
                        @Override
                        public void call(Throwable error) {

                        }
                    });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 获得数据之后的操作
     */
    private Handler mhandler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
           switch (msg.what){
               case REQUEST:
               case PULLDOWN:
                   currentPage = 2;
                   break;
               case PULLUP:
                   currentPage++;
                   break;

               default:
                   break;
           }
            event_listview.onRefreshComplete();
        }
    };

    /**
     * 点击查看活动详情
     */
    private void onClickDetails() {
        event_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //判断是否审核通过，0是未审核，1是审核通过，2是审核不通过
                switch (MeiMengApplication.sharedPreferences .getInt(CommonConstants.INFO_STATE,0) ){
                    case 0:
                        break;
                    case 1:
                        homeActivity.setAlertDialog_a();
                        break;
                    default:
                        //Utils.getUerVerfiy();//重新网络获取审核状态
                        switch (MeiMengApplication.sharedPreferences.getInt(CommonConstants.USER_VERFIY,0)){
                            case 0:
                                Utils.setPromptDialog(getActivity(), "您提交的资料正在审核状态。如有疑问，请联系美盟客服：400-8783-520");
                                break;
                            case 1:
                                EventData eventData = (EventData) adapter.getItem(--i);
                                Intent intent = new Intent(getActivity(), OfficeEventDetail.class);
                                intent.putExtra("mActivityId", eventData.getActivityId());
                                intent.putExtra("placeDetailShowFlg",eventData.getPlaceDetailShowFlg());
                                startActivity(intent);
                                break;

                            case 2:
                               Utils.setPromptDialog(getActivity(),"对不起，您暂不符合美盟会员要求，未通过审核。您可以关注美盟订阅号:meimeng88888，获取更多信息。");
                                break;
                            default:
                                break;
                        }
                        break;
                }

            }
        });
    }

    /**
     * PullToRefreshListView上下拉刷新活动数据
     */
    private void refreshData() {

        event_listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            //下拉刷新数据
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                String time_label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("更新于：" + time_label);
                currentPage = 1;
                initData(PULLDOWN, currentPage);
            }

            //上拉加载新数据
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                refreshView.getLoadingLayoutProxy().setPullLabel("加载更多...");
                initData(PULLUP, currentPage);
            }
        });
    }


    /**
     * 显示选择器
     */
    private void showSelectDialog(int num,boolean linegone,boolean cmgone,String string) {
        name.setText(string);
        if(!(select_rl.getVisibility() == View.VISIBLE)) {
            select_rl.setVisibility(View.VISIBLE);
            Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_arrow_up);
            select_rl.setAnimation(animation);
        }

        rl = (RelativeLayout) select_rl.findViewById(R.id.layout_picker_one);
        rl.setVisibility(View.VISIBLE);

//        if(rl.getVisibility() == View.VISIBLE) {
        seleckData_a = (PickerView) rl.findViewById(R.id.picker_one_a);
        seleckData_b = (PickerView) rl.findViewById(R.id.picker_one_b);

        seleckData_a.setIsWheel(false);
        seleckData_a.setOnSelectListener(new PickerView.onSelectListener() {

            public void onSelect(String text) {
                MeiMengApplication.cityBean.setCityName(text);
                Log.d("whh", "text="+text);
            }
        });

        ImageView line = (ImageView) rl.findViewById(R.id.line);
        TextView cm = (TextView)rl.findViewById(R.id.CM);

        if(linegone){
            line.setVisibility(View.GONE);
        }else {
            line.setVisibility(View.VISIBLE);
        }

        if(cmgone){
            cm.setVisibility(View.GONE);
        }else {
            cm.setVisibility(View.VISIBLE);
        }

        if(num == 1){
            seleckData_b.setVisibility(View.GONE);
        }else {
            seleckData_b.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 取消选择器
     */
    private void hideSelectDialog() {
        if(select_rl.getVisibility() == View.VISIBLE) {
            initImg();
            select_rl.clearAnimation();
            select_rl.setVisibility(View.GONE);
        }
    }

    /**
     * 初始化图标
     */
    private void initImg(){
        regionImg.setImageResource(R.drawable.event_btn_default);
        stateImg.setImageResource(R.drawable.event_btn_default);
    }


    private HashMap<String, Integer> nationHashMap = new HashMap<>();
    private ArrayList<String> getNationList(int id) {
        nationHashMap.clear();

        DbOpenHelper dbOpenHelper = new DbOpenHelper(getActivity());
        SQLiteDatabase database = dbOpenHelper.openDatabase();
        Cursor curso = database.query("sys_master", null, "type=?", new String[]{String.valueOf(id)}, null, null, null);
        if (curso.moveToFirst()) {
            do {
                String nation = curso.getString(curso.getColumnIndex("value"));
                int key = curso.getInt(curso.getColumnIndex("key"));
                nationHashMap.put(nation, key);

            } while (curso.moveToNext());
        }
        curso.close();
        ArrayList<String> nationList = new ArrayList<>();
        nationList = GetDbUtils.getArrayListSort(nationHashMap);
        return nationList;
    }


    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {


        if (MeiMengApplication.sharedPreferences.getBoolean(CommonConstants.MYSTATE, false)==false) {
            homeActivity.setAlertDialog_a();//资料未填写完成，提示完善资料
        }else {//资料填写完成
            switch (MeiMengApplication.sharedPreferences.getInt(CommonConstants.USER_VERFIY,0)){
                case 0:
                    Utils.setPromptDialog(getActivity(), "您提交的资料正在审核状态。如有疑问，请联系美盟客服：400-8783-520");
                    break;
                case 1:
                    onViewClick(v);
                    break;
                case 2:
                    Utils.setPromptDialog(getActivity(),"对不起，您暂不符合美盟会员要求，未通过审核。您可以关注美盟订阅号:meimeng88888，获取更多信息。");
                    break;
            }
        }

    }

    private int btnState;
    private void onViewClick(View v){
        //界面点击事件
        switch (v.getId()){
            case R.id.event_region: //地区选择按钮
                hideSelectDialog();
                showSelectDialog(1, true, true, "地区");
                setRegionData();

                btnState=0;
                break;
            case R.id.event_state:  //状态选择按钮
                hideSelectDialog();
                showSelectDialog(1, true, true, "状态");
                setStateData();
                btnState=1;
                break;
            case R.id.select_cancel_text: //选择器取消按钮
                hideSelectDialog();

                break;
            case R.id.select_sure_text: //选择器确定按钮
                selectSure(btnState);
                hideSelectDialog();

                break;

            default:
                break;
        }
    }




    /**
     * 地区选择数据设置
     */
    private void setRegionData() {
        regionImg.setImageResource(R.drawable.event_btn_click);
//        dataUtil.setRegionData(seleckData_a,seleckData_b);
        ArrayList<String> statelist = new ArrayList<>();
        statelist.add("全国");
        statelist.add("北京");
        statelist.add("上海");
        statelist.add("广州");
        statelist.add("深圳");
        statelist.add("成都");
        statelist.add("武汉");
        seleckData_a.setData(statelist);
    }
    /**
     * 状态选择数据设置
     */
    private void setStateData() {

        stateImg.setImageResource(R.drawable.event_btn_click);
        final HashMap<String,Integer> statemap = new HashMap<>();
        statemap.put("进行中",1);
        statemap.put("已结束", 4);
        final ArrayList<String> statelist = new ArrayList<>();
        statelist.add("进行中");
        statelist.add("已结束");
        seleckData_a.setData(statelist);
        seleckData_a.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                stateid = statemap.get(text);
            }
        });
    }

    /**
     * 选择确定操作
     */
    private void selectSure(int state) {
        currentPage = 1;
        if (state==0){
            if (seleckData_a.isWheel()==false) {
                MeiMengApplication.cityBean.setCityId(-1);
                MeiMengApplication.cityBean.setCityName("全国");
            }
            if(MeiMengApplication.cityBean.getCityName()==null||MeiMengApplication.cityBean.getCityName().equals("全国")){
                if(MeiMengApplication.cityBean.getCityName()==null){
                    MeiMengApplication.cityBean.setCityName("全国");
                }
                initData(PULLDOWN, 1);
                return;
            }else if(MeiMengApplication.cityBean.getCityName().equals("成都")){
                MeiMengApplication.cityBean.setCityId(2499);
            }else if(MeiMengApplication.cityBean.getCityName().equals("武汉")){
                MeiMengApplication.cityBean.setCityId(1846);
            }else if(MeiMengApplication.cityBean.getCityName().equals("北京")){
                MeiMengApplication.cityBean.setCityId(2);
            }else if(MeiMengApplication.cityBean.getCityName().equals("上海")){
                MeiMengApplication.cityBean.setCityId(857);
            }else if(MeiMengApplication.cityBean.getCityName().equals("广州")){
                MeiMengApplication.cityBean.setCityId(2126);
            }else if(MeiMengApplication.cityBean.getCityName().equals("深圳")){
                MeiMengApplication.cityBean.setCityId(2151);
            }
        }

        if (state == 1) {
            if (seleckData_a.isWheel()==false) {
                stateid=1;
            }
        }
        initData(REQUEST, currentPage);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AllFragmentManagement.fragmentList.remove(this);
        Log.e("Fragment", "EventFragment Destroy");
    }

}
