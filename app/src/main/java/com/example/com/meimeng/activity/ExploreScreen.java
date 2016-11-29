package com.example.com.meimeng.activity;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.com.meimeng.MeiMengApplication;
import com.example.com.meimeng.R;
import com.example.com.meimeng.bean.FilterBean;
import com.example.com.meimeng.bean.ProvinceBean;
import com.example.com.meimeng.custom.PickerView;
import com.example.com.meimeng.db.DbOpenHelper;
import com.example.com.meimeng.gson.GsonTools;
import com.example.com.meimeng.gson.gsonbean.ExplorejsonBean;
import com.example.com.meimeng.net.InternetConstant;
import com.example.com.meimeng.net.InternetUtils;
import com.example.com.meimeng.util.DataUtil;
import com.example.com.meimeng.util.GetDbUtils;
import com.example.com.meimeng.util.Utils;

import org.json.JSONException;
import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by Administrator on 2015/7/14.
 */
public class ExploreScreen extends BaseActivity implements View.OnClickListener {


    private float mDensity = 1f;

    //记录当前点击的是哪一行
    private int type = 1;

    //记录当前单个滚动条的内容
    private volatile String singleContent = "";
    private volatile int singleId = 0;
    private FilterBean mFilterBean = new FilterBean();
    private String jsonstr = "";

    private EditText ExploreScreen_edit;

    private LinearLayout explore_screen_region;
    private LinearLayout explore_screen_age;
    private LinearLayout explore_screen_height;

    private TextView screen_region_text;
    private TextView screen_age_text;
    private TextView screen_height_text;

    private TextView explore_screen_btn;

    private RelativeLayout select_rl;

    private RelativeLayout rl;
    private PickerView seleckData_a;
    private PickerView seleckData_b;

    private final int ANIMATIONOVER = 10;
    private final int NOUSER = 110;
    private final int YOUUSER = 112;
    private boolean isRetart = false;
    private int num = 1;
    private Dialog dialog;

    private DataUtil dataUtil;
    private ArrayList<ProvinceBean> provinces = new ArrayList<>();

    private ArrayList<String> province = new ArrayList<>();


    private boolean isTarget = false;

    private TextView name;
    private int ageStart;
    private int ageEnd;

    private int heightStart;
    private int heightEnd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_explore_screen);
        ButterKnife.bind(this);

        MeiMengApplication.currentContext=this;
//        Intent intent = getIntent();
//        mFilterBean = (FilterBean) intent.getSerializableExtra("condition");

        dataUtil = new DataUtil(this);
        dataUtil.getProvinceList();


        //初始化界面
        initView();


        // 自定义选择View监听
        ArrayList<String> list = getNationList(1);


    }

    /**
     * 初始化界面
     */
    private void initView() {
        initTitleBar("搜索", R.drawable.icon_nav_back, -1, this);

        ExploreScreen_edit = (EditText) findViewById(R.id.explore_screen_edit);

        explore_screen_region = (LinearLayout) findViewById(R.id.explore_screen_region);
        explore_screen_age = (LinearLayout) findViewById(R.id.explore_screen_age);
        explore_screen_height = (LinearLayout) findViewById(R.id.explore_screen_height);
        explore_screen_region.setOnClickListener(this);
        explore_screen_age.setOnClickListener(this);
        explore_screen_height.setOnClickListener(this);

        screen_region_text = (TextView) findViewById(R.id.screen_region_text);
        screen_age_text = (TextView) findViewById(R.id.screen_age_text);
        screen_height_text = (TextView) findViewById(R.id.screen_height_text);

        explore_screen_btn = (TextView) findViewById(R.id.explore_screen_btn);
        explore_screen_btn.setOnClickListener(this);

        select_rl = (RelativeLayout) findViewById(R.id.select_layout);
        TextView cancel = (TextView) select_rl.findViewById(R.id.select_cancel_text);
        name = (TextView) select_rl.findViewById(R.id.select_name_text);
        TextView sure = (TextView) select_rl.findViewById(R.id.select_sure_text);
        cancel.setOnClickListener(this);
        sure.setOnClickListener(this);

    }

    private HashMap<String, Integer> nationHashMap = new HashMap<>();

    private ArrayList<String> getNationList(int id) {
        nationHashMap.clear();

        DbOpenHelper dbOpenHelper = new DbOpenHelper(this);
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


    //返回按钮


    /**
     * 获取搜索请求JSON字段，并返回给上一个Activity
     */

    void sureButtonListener() {


        try {

            JSONStringer stringer = new JSONStringer().object();
            if (mFilterBean.getAgeStart() != 0) {
                stringer.key("ageStart").value(mFilterBean.getAgeStart());
                Log.i("json", "age1   " + mFilterBean.getAgeStart());
            }
            if (mFilterBean.getAgeEnd() != 0) {
                stringer.key("ageEnd").value(mFilterBean.getAgeEnd());
            }
            if (mFilterBean.getHeightStart() != 0) {
                stringer.key("heightStart").value(mFilterBean.getHeightStart());
            }
            if (mFilterBean.getHeightEnd() != 0) {
                stringer.key("heightEnd").value(mFilterBean.getHeightEnd());
            }
            Integer residence = mFilterBean.getResidence();
            if (residence != -1) {
                if (residence == 1) {
                    residence = null;
                }
                stringer.key("residence").value(residence);
            }
//            if (mFilterBean.getEducation() != 0) {
//                stringer.key("education").value(mFilterBean.getEducation());
//            }
//            if (mFilterBean.getIndustry() != 0) {
//                stringer.key("industry").value(mFilterBean.getIndustry());
//            }
//            if (mFilterBean.getHouseState() != 0) {
//                stringer.key("houseState").value(mFilterBean.getHouseState());
//            }
//            if (mFilterBean.getCarState() != 0) {
//                stringer.key("carState").value(mFilterBean.getCarState());
//            }
//            if (mFilterBean.getYearIncome() != 0) {
//                stringer.key("yearIncome").value(mFilterBean.getYearIncome());
//            }
//            if (mFilterBean.getMarstate() != 0) {
//                stringer.key("maritalState").value(mFilterBean.getMarstate());
//            }
            stringer.key("currentPage").value(0).key("pageSize").value(5)
                    .endObject();

            jsonstr = stringer.toString();

            //TODO 需要回传
            //传递给上一个界面
//            Intent intent = new Intent();
//            intent.putExtra("json", jsonStr);
//            intent.putExtra("place", mFilterBean.getPlace());
//            intent.putExtra("condition", mFilterBean);
//            setResult(2, intent);
//            onBackPressed();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 得到年龄的数据
     *
     * @return
     */
    private ArrayList<String> getAgeList(int agestart) {
        ArrayList<String> ageList = new ArrayList<>();

        for (int i = agestart; i < 60; i++) {
            ageList.add(String.valueOf(i));
        }
        return ageList;
    }

    /**
     * 得到身高的数据
     *
     * @return
     */
    private ArrayList<String> getHeightList(int heightstart) {
        ArrayList<String> heightList = new ArrayList<>();

        for (int i = heightstart; i < 220; i++) {
            heightList.add(String.valueOf(i));
        }
        return heightList;
    }

    /**
     * 得到学历的数据
     *
     * @return
     */
    private ArrayList<String> getEducationList() {
        ArrayList<String> educationList = new ArrayList<>();
        educationList.add("不限");
        educationList.add("大专及以上");
        educationList.add("本科及以上");
        educationList.add("硕士及以上");
        educationList.add("博士及以上");
        educationList.add("博士后及以上");
        return educationList;
    }

    /**
     * 得到月收入的数据
     *
     * @return
     */
    private ArrayList<String> getMoneyList() {
        ArrayList<String> moneyList = new ArrayList<>();
        moneyList.add("不限");
        moneyList.add("30万以下");
        moneyList.add("30-50万");
        moneyList.add("50-100万");
        moneyList.add("100-300万");
        moneyList.add("300-500万");
        moneyList.add("500-1000万");
        moneyList.add("1000万以上");
        return moneyList;
    }

    /**
     * 得到住房情况的数据
     *
     * @return
     */
    private ArrayList<String> getHouseList() {
        ArrayList<String> houseList = new ArrayList<>();
        houseList.add("有");
        houseList.add("无");
       /* houseList.add("不限");
        houseList.add("已购房");
        houseList.add("租房");
        houseList.add("单位宿舍");
        houseList.add("和家人同住");*/

        return houseList;
    }

    /**
     * 得到行业的数据
     *
     * @return
     */
    private ArrayList<String> getTradeList() {
        ArrayList<String> tradeList = new ArrayList<>();
        tradeList.add("不限");
        tradeList.add("计算机/互联网/通信");
        tradeList.add("公务员/事业单位");
        tradeList.add("教师");
        tradeList.add("医生");
        tradeList.add("护士");
        tradeList.add("空乘人员");
        tradeList.add("生产/工艺/制造");
        tradeList.add("商业/服务业/个体经营");
        tradeList.add("金融/银行/投资/保险");
        tradeList.add("律师/法务");
        tradeList.add("教育/培训/管理咨询");
        tradeList.add("建筑/房地产/物业");
        tradeList.add("消费零售/贸易/交通物流");
        tradeList.add("酒店旅游");
        tradeList.add("现代农业");
        tradeList.add("在校学生");

        return tradeList;
    }

    /**
     * 得到购车情况的数据
     *
     * @return
     */
    private ArrayList<String> getCarList() {
        ArrayList<String> carList = new ArrayList<>();
        carList.add("有");
        carList.add("无");
       /* carList.add("不限");
        carList.add("已购车");
        carList.add("未购车");*/

        return carList;

    }

    private ArrayList<String> getMarList() {
        ArrayList<String> marList = new ArrayList<>();
        marList.add("不限");
        marList.add("未婚");
        marList.add("离异");

        return marList;

    }

    //explore_screen_height
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.more_search_layout: //返回键
                super.onBackPressed();
                break;

            case R.id.explore_screen_region:// 地区
                hideSelectDialog();
                isRetart = true;
                num = 2;
                showSelectDialog(num, true, true, "地区");
                dataUtil.setRegionData(seleckData_a, seleckData_b);
                break;
            case R.id.explore_screen_age:// 年龄
                hideSelectDialog();
                isRetart = true;
                num = 2;
                showSelectDialog(num, false, true, "年龄");
                setAgeData();
                break;
            case R.id.explore_screen_height:// 身高
                hideSelectDialog();
                isRetart = true;
                num = 2;
                showSelectDialog(num, false, false, "身高");
                setheightData();

                break;

            case R.id.explore_screen_btn:
                onScreen();
                break;

            case R.id.select_cancel_text: //选择器取消按钮
                hideSelectDialog();
                isRetart = false;
                break;
            case R.id.select_sure_text: //选择器确定按钮
                String flag = name.getText() + "";
                selectSure(flag);
                hideSelectDialog();
                isRetart = false;
                break;

        }
    }

    /**
     * 设置身高
     */
    private void setheightData() {
        ArrayList<String> heightstartlist = new ArrayList<>();
        final ArrayList<String> heightendlist = new ArrayList<>();
        heightstartlist.addAll(getHeightList(150));
        heightendlist.addAll(getHeightList(150));
        seleckData_a.setData(heightstartlist);
        seleckData_b.setData(heightendlist);
        heightStart = 150;
        heightEnd = 150;
        seleckData_a.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                heightStart = Integer.parseInt(text);
                heightEnd = Integer.parseInt(text);
                heightendlist.clear();
                heightendlist.addAll(getHeightList(heightStart));
                seleckData_b.setData(heightendlist);
            }
        });

        seleckData_b.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                heightEnd = Integer.parseInt(text);
            }
        });
    }

    /**
     * 设置年龄
     */
    private void setAgeData() {

        ArrayList<String> agestartlist = new ArrayList<>();
        final ArrayList<String> ageendlist = new ArrayList<>();
        agestartlist.addAll(getAgeList(18));
        ageendlist.addAll(getAgeList(18));
        seleckData_a.setData(agestartlist);
        seleckData_b.setData(ageendlist);
        ageStart = 18;
        ageEnd = 18;
        seleckData_a.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                ageStart = Integer.parseInt(text);
                ageEnd = Integer.parseInt(text);
                ageendlist.clear();
                ageendlist.addAll(getAgeList(ageStart));
                seleckData_b.setData(ageendlist);
            }
        });

        seleckData_b.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                ageEnd = Integer.parseInt(text);
            }
        });

    }


    /**
     * 搜索
     */
    private void onScreen() {
        String id = ExploreScreen_edit.getText().toString();
        dialog = Utils.createLoadingDialog(ExploreScreen.this, "载入中...");
        dialog.show();
        if (!TextUtils.isEmpty(id)) {


            if (id.matches("^(([0-9]+)*)$")) {
                try {
                    JSONStringer stringer = new JSONStringer().object();
                    stringer.key("targetUid").value(Long.valueOf(id)).endObject();
                    jsonstr = stringer.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, "请输入会员的ID", Toast.LENGTH_SHORT).show();
            }


        } else {
            sureButtonListener();
        }

        if (TextUtils.isEmpty(Utils.getUserId())) {
            return;
        }
        if (TextUtils.isEmpty(Utils.getUserToken())) {
            return;
        }
        String url = InternetConstant.SERVER_URL + InternetConstant.EXPLORE_LIST_URL + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();
        Observable observable = InternetUtils.getJsonObservale(url, jsonstr);
        observable.observeOn(AndroidSchedulers.mainThread());
        observable.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                if (null != dialog)
                    dialog.dismiss();
                ExplorejsonBean explorejsonBean = GsonTools.getExploreJson(s);
                if (explorejsonBean.getParam().getExploreListItem().size() == 0) {
                    Looper.prepare();
                    Toast.makeText(ExploreScreen.this, "未找到符合条件的用户！", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                } else {
                    //onSearchCallBack1.onSearch(mFilterBean, jsonstr);
                    Intent intent=new Intent(ExploreScreen.this,SeniorExploreActivity.class);
                    intent.putExtra("mFilterBean",mFilterBean);
                    intent.putExtra("jsonstr",jsonstr);
                    startActivity(intent);
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable error) {
                Log.e("test:", error.getMessage());
            }
        });


        //onSearchCallBack1.onSearch(mFilterBean, jsonstr);
        //super.onBackPressed();
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case NOUSER:
                   // setBackActivity();
                    Intent intent=new Intent(ExploreScreen.this,SeniorExploreActivity.class);
                    startActivity(intent);
                    break;
                case YOUUSER:
                    Toast.makeText(ExploreScreen.this, "对不起，没有符合条件的用户！", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
    private void setBackActivity(){
        super.onBackPressed();
    }


    /**
     * 选择器确定按钮
     */
    private void selectSure(String flag) {

        if (flag.equals("地区")) {
            //地区
            try {
                if(MeiMengApplication.cityBean.getCityId()==3) {
                    MeiMengApplication.cityBean.setCityId(857);
                }
                mFilterBean.setResidence(MeiMengApplication.cityBean.getCityId());
                mFilterBean.setPlace(MeiMengApplication.cityBean.getCityName());
                MeiMengApplication.cityBean = null;//城市数据复位
            } catch (NullPointerException e) {
                e.printStackTrace();
            } finally {
                if (mFilterBean.getPlace() == null || mFilterBean.getPlace().equals("")) {
                    //TODO 设置mFilterBean参数
                    screen_region_text.setText("不限");
                } else {
                    screen_region_text.setText(mFilterBean.getPlace());
                    mFilterBean.setPlace(null);
                }
            }
        } else if (flag.equals("年龄")) {
            //年龄
            mFilterBean.setAgeStart(ageStart);
            mFilterBean.setAgeEnd(ageEnd);
            if (!(ageStart == 0)) {
                screen_age_text.setText(ageStart + "-" + ageEnd);
            }
        } else if (flag.equals("身高")) {
            //身高
            mFilterBean.setHeightStart(heightStart);
            mFilterBean.setHeightEnd(heightEnd);
            if (!(heightStart == 0)) {
                screen_height_text.setText(heightStart + "-" + heightEnd + "CM");
            }
        }
    }

    /**
     * 显示选择器
     */
    private void showSelectDialog(int num, boolean linegone, boolean cmgone, String string) {
        if (!(select_rl.getVisibility() == View.VISIBLE)) {
            select_rl.setVisibility(View.VISIBLE);
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_arrow_up);
            select_rl.setAnimation(animation);
        }
        name.setText(string);
        rl = (RelativeLayout) select_rl.findViewById(R.id.layout_picker_one);
        rl.setVisibility(View.VISIBLE);

//        if(rl.getVisibility() == View.VISIBLE) {
        seleckData_a = (PickerView) rl.findViewById(R.id.picker_one_a);
        seleckData_b = (PickerView) rl.findViewById(R.id.picker_one_b);

        ImageView line = (ImageView) rl.findViewById(R.id.line);
        TextView cm = (TextView) rl.findViewById(R.id.CM);

        if (linegone) {
            line.setVisibility(View.GONE);
        } else {
            line.setVisibility(View.VISIBLE);
        }

        if (cmgone) {
            cm.setVisibility(View.GONE);
        } else {
            cm.setVisibility(View.VISIBLE);
        }

        if (num == 1) {
            seleckData_b.setVisibility(View.GONE);
        } else {
            seleckData_b.setVisibility(View.VISIBLE);

        }

    }

    /**
     * 取消选择器
     */
    private void hideSelectDialog() {
        if (select_rl.getVisibility() == View.VISIBLE) {

            select_rl.clearAnimation();
            select_rl.setVisibility(View.GONE);


        }
    }


    /**
     * 初始化筛选条件
     */

    public static interface onSearchCallBack {
        void onSearch(FilterBean mFilterBean, String jsonStr);
    }

    public static onSearchCallBack onSearchCallBack1;

    public static void getOnSearch(onSearchCallBack onSearchCallBack) {
        onSearchCallBack1 = onSearchCallBack;
    }


}
