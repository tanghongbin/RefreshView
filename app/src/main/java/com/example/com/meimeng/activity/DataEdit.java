package com.example.com.meimeng.activity;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.com.meimeng.MeiMengApplication;
import com.example.com.meimeng.R;
import com.example.com.meimeng.activity.dataedit.DataEditBook;
import com.example.com.meimeng.activity.dataedit.DataEditCompanyIndustry;
import com.example.com.meimeng.activity.dataedit.DataEditEducation;
import com.example.com.meimeng.activity.dataedit.DataEditFood;
import com.example.com.meimeng.activity.dataedit.DataEditJob;
import com.example.com.meimeng.activity.dataedit.DataEditMovie;
import com.example.com.meimeng.activity.dataedit.DataEditMusic;
import com.example.com.meimeng.activity.dataedit.DataEditNation;
import com.example.com.meimeng.activity.dataedit.DataEditSports;
import com.example.com.meimeng.activity.dataedit.DataEditTravel;
import com.example.com.meimeng.custom.CityPicker;
import com.example.com.meimeng.custom.CityPicker2;
import com.example.com.meimeng.custom.TimePicker;
import com.example.com.meimeng.custom.TiwnPicker;
import com.example.com.meimeng.custom.WheelView;
import com.example.com.meimeng.db.DbOpenHelper;
import com.example.com.meimeng.gson.GsonTools;
import com.example.com.meimeng.gson.gsonbean.BaseBean;
import com.example.com.meimeng.gson.gsonbean.MyDetailInfoBean;
import com.example.com.meimeng.gson.gsonbean.MyDetailInfoItem;
import com.example.com.meimeng.net.InternetConstant;
import com.example.com.meimeng.net.InternetUtils;
import com.example.com.meimeng.util.DialogUtils;
import com.example.com.meimeng.util.GetDbUtils;
import com.example.com.meimeng.util.Utils;

import org.apmem.tools.layouts.FlowLayout;
import org.json.JSONArray;
import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by Administrator on 2015/7/16.
 * 编辑资料页面
 */
public class DataEdit extends BaseActivity{

    //进度条的文本
    private TextView progressTextView;

    private BaseBean baseBeanJson;
    private String type;
    @Bind(R.id.main_dialog_layout)
    LinearLayout mainDialogLayout;

    //region others bind
    //title按钮
    @Bind(R.id.title_text)
    TextView titleText;

    @Bind(R.id.title_left_arrow_image_view)
    ImageView leftArrowImageView;

    @Bind(R.id.bow_arrow_image_view)
    ImageView bowArrowImageView;

    @Bind(R.id.title_sure_text)
    TextView sureText;

    @Bind(R.id.wheel_layout)
    RelativeLayout wheelLayout;

    @Bind(R.id.wheel_name_text)
    TextView wheelName;

    @Bind(R.id.wheel_cover_button)
    Button wheelCoverButton;

    @Bind(R.id.wheel_select_layout)
    RelativeLayout wheelSelectLayout;

    @Bind(R.id.wheel_city_picker)
    CityPicker cityPicker;

    @Bind(R.id.wheel_city_picker2)
    CityPicker2 cityPicker2;

    @Bind(R.id.wheel_single_view)
    WheelView singleWheelView;

    @Bind(R.id.wheel_time_picker)
    TimePicker timePacker;

    @Bind(R.id.wheel_twin_picker)
    TiwnPicker wheelTiwnPicker;
    //endregion

    //shy

    //region bind base info1
    @Bind(R.id.me_first_name_textview)//姓
            TextView meFirstNameTextView;
    @Bind(R.id.me_last_name_textview)//名
            TextView meLastNameTextView;
    @Bind(R.id.me_sex_textview)//性别
            TextView meSexTextView;
    @Bind(R.id.me_birthday_textview)//出生日期
            TextView meBirthdayTextView;
    @Bind(R.id.me_chinese_zodiac_textview)//生肖
            TextView meChineseZodiacTextView;
    @Bind(R.id.me_zodiac_textview)//星座
            TextView meZodiacTextView;
    //endregion
    //region bind base info2
    @Bind(R.id.me_height_textview)//身高
            TextView meHeightTextView;
    @Bind(R.id.me_weight_textview)//体重
            TextView meWeightTextView;
    @Bind(R.id.me_location_textview)//居住地
            TextView meLocationTextView;
    @Bind(R.id.me_nation_textview)//民族
            TextView meNationTextView;
    //endregion
    //region bind work info
    @Bind(R.id.me_education_textview)//学历
            TextView meEducationTextView;
    @Bind(R.id.me_school_edit_text)//毕业学校
            EditText meSchoolEditText;
    @Bind(R.id.me_trades_textview)//行业
            TextView meTradesTextView;
    @Bind(R.id.me_job_textview)//职位
            TextView meJobTextView;
    @Bind(R.id.me_overseas_textview)//海外经历
            TextView meOverseasTextView;
    //endregion
    //region bind income info
    @Bind(R.id.me_yearly_salary_textview)//年薪
            TextView meYearlySalaryTextView;
    @Bind(R.id.me_assets_textview)//资产
            TextView meAssetsTextView;
    @Bind(R.id.me_family_background_textview)//家庭背景
            TextView meFamilyBackgroundTextView;
    @Bind(R.id.me_house_textview)//住房情况
            TextView meHouseTextView;
    @Bind(R.id.me_car_textview)//购车情况
            TextView meCarTextView;
    @Bind(R.id.me_marry_textview)//婚姻情况
            TextView meMarryTextView;
    //endregion
    //region bind interest info
    @Bind(R.id.me_interest_sport_items_layout)
    FlowLayout sportLayout;
    @Bind(R.id.me_interest_music_items_layout)
    FlowLayout musicLayout;
    @Bind(R.id.me_interest_food_items_layout)
    FlowLayout foodLayout;
    @Bind(R.id.me_interest_movie_items_layout)
    FlowLayout movieLayout;
    @Bind(R.id.me_interest_book_items_layout)
    FlowLayout bookLayout;
    @Bind(R.id.me_interest_travel_items_layout)
    FlowLayout travelLayout;
    //endregion
    @Bind(R.id.me_story_edit_text)
    EditText meStoryEditText;
    //end shy

    //region var defines
    //正在编辑的类型
    private int typeWheel = 1;
    private float mDensity = 1f;

    //记录当前单个滚动条的内容
    private volatile String singleContent = null;
    private volatile int singleId = 0;
    private HashMap<String, Integer> nationHashMap = new HashMap<>();
    private Dialog dialog;
    private boolean isSave = false;
    //endregion

    public DataEdit() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.data_edit);
        ButterKnife.bind(this);

        MeiMengApplication.currentContext=this;
        dialog = Utils.createLoadingDialog(DataEdit.this, "载入中...");
        dialog.show();
        initView();

        mDensity = Utils.getScreenMetrics(DataEdit.this).density;
        bowArrowImageView.setVisibility(View.GONE);
        leftArrowImageView.setVisibility(View.VISIBLE);
        titleText.setText("编辑资料");

        sureText.setVisibility(View.VISIBLE);
        sureText.setText("保存");
        sureText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = Utils.createLoadingDialog(DataEdit.this, "保存中...");
                dialog.show();
                save();
            }
        });

        //获取从滑动选择器选择的选项的key值和value值
        singleWheelView.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                singleContent = text;
                singleId = id;
            }

            @Override
            public void selecting(int id, String text) {

            }
        });

        meStoryEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isSave=false;
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    //region interest init and get
    private void initInterest(ArrayList<String> items,FlowLayout rootLayout,int itemId,int textViewId){
        rootLayout.removeAllViews();
        if(items == null)
            return;

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);

        for(int i = 0;i < items.size();i++){
            LinearLayout layout = (LinearLayout)inflater.inflate(itemId, null);
            TextView textView = (TextView)layout.findViewById(textViewId);
            textView.setText(items.get(i));
            rootLayout.addView(layout);
        }
    }

    private ArrayList<String> getInterest(FlowLayout rootLayout,int textViewId){
        ArrayList<String> lst = new ArrayList<String>();

       for(int i = 0;i < rootLayout.getChildCount();i++){
           LinearLayout layout = (LinearLayout)rootLayout.getChildAt(i);
           TextView textView = (TextView)layout.findViewById(textViewId);
           lst.add(textView.getText().toString());
       }

        return  lst;
    }

    //endregion

    //region init and save
    private JSONArray getJsonArrayObject(ArrayList<String> lst) {
        //JSONObject object=new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < lst.size(); i++) {
            jsonArray.put(lst.get(i));
        }
        //object.put("lstPhoto", jsonArray);
        return jsonArray;
    }

    private String buildSaveJsonString(){
        String jsonStr = "";

        try {
            int height = Integer.parseInt(meHeightTextView.getTag().toString());
            int weight = Integer.parseInt(meWeightTextView.getTag().toString());
            int residence = Integer.parseInt(meLocationTextView.getTag().toString());
            int nation = Integer.parseInt(meNationTextView.getTag().toString());
            int education = Integer.parseInt(meEducationTextView.getTag().toString());
            int maritalStatus = Integer.parseInt(meMarryTextView.getTag().toString());
            String graduateCollege = meSchoolEditText.getText().toString();
            int companyIndustry = Integer.parseInt(meTradesTextView.getTag().toString());
            int companyPosition = Integer.parseInt(meJobTextView.getTag().toString());
            int outsideExperience = Integer.parseInt(meOverseasTextView.getTag().toString());
            int yearIncome = Integer.parseInt(meYearlySalaryTextView.getTag().toString());
            int property = Integer.parseInt(meAssetsTextView.getTag().toString());
            int familyBackground = Integer.parseInt(meFamilyBackgroundTextView.getTag().toString());
            int houseState = Integer.parseInt(meHouseTextView.getTag().toString());
            int carState = Integer.parseInt(meCarTextView.getTag().toString());
            ArrayList<String> lstLoveExercise = getInterest(sportLayout, R.id.interest_sport_item_text_view);
            ArrayList<String> lstLoveFood = getInterest(foodLayout, R.id.interest_food_item_text_view);
            ArrayList<String> lstLoveMusic = getInterest(musicLayout, R.id.interest_music_item_text_view);
            ArrayList<String> lstLoveFilm = getInterest(movieLayout, R.id.interest_movie_item_text_view);
            ArrayList<String> lstLoveBook = getInterest(bookLayout, R.id.interest_book_item_text_view);
            ArrayList<String> lstLoveTouristDestination = getInterest(travelLayout, R.id.interest_travel_item_text_view);
            String userStory = meStoryEditText.getText().toString();

            JSONStringer jsonStringer = new JSONStringer().object()
                    .key("height").value(height)
                    .key("weight").value(weight)
                    .key("residence").value(residence)
                    .key("nation").value(nation)
                    .key("education").value(education)
                    .key("maritalStatus").value(maritalStatus)
                    .key("graduateCollege").value(graduateCollege)
                    .key("companyIndustry").value(companyIndustry)
                    .key("companyPosition").value(companyPosition)
                    .key("outsideExperience").value(outsideExperience)
                    .key("yearIncome").value(yearIncome)
                    .key("property").value(property)
                    .key("familyBackground").value(familyBackground)
                    .key("houseState").value(houseState)
                    .key("carState").value(carState)
                    .key("lstLoveExercise").value(getJsonArrayObject(lstLoveExercise))
                    .key("lstLoveFood").value(getJsonArrayObject(lstLoveFood))
                    .key("lstLoveMusic").value(getJsonArrayObject(lstLoveMusic))
                    .key("lstLoveFilm").value(getJsonArrayObject(lstLoveFilm))
                    .key("lstLoveBook").value(getJsonArrayObject(lstLoveBook))
                    .key("lstLoveTouristDestination").value(getJsonArrayObject(lstLoveTouristDestination))
                    .key("userStory").value(userStory)
                    .endObject();
            jsonStr = jsonStringer.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonStr;
    }

    /**
     * 保存资料
     */
    private void save() {
        try {
            if (TextUtils.isEmpty(Utils.getUserId())) {
                return;
            }
            if (TextUtils.isEmpty(Utils.getUserToken())) {
                return;
            }
            String url = InternetConstant.SERVER_URL + InternetConstant.USER_EDIT + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();
            String jsonStr = buildSaveJsonString();
            timeOutCloseDialog();   //网络获取超时设置
            //得到Observable并获取返回的数据(主线程中)
            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            if (null != dialog)
                                dialog.dismiss();
                            baseBeanJson = GsonTools.getBaseReqBean(s);
                            if (baseBeanJson.isSuccess() != false) {
                                isSave = true;
                                Toast.makeText(com.example.com.meimeng.activity.DataEdit.this, "修改资料成功", Toast.LENGTH_SHORT).show();
                            } else
                                DialogUtils.setDialog(DataEdit.this, baseBeanJson.getError());

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
     * 显示原有的用户详细信息
     */
    private void initView() {

        try {
            if (TextUtils.isEmpty(Utils.getUserId())) {
                return;
            }
            if (TextUtils.isEmpty(Utils.getUserToken())) {
                return;
            }
            String url = InternetConstant.SERVER_URL + InternetConstant.USER_ALL_INFO_URL + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();
            JSONStringer stringer = new JSONStringer().object().endObject();
            String jsonStr = stringer.toString();
            timeOutCloseDialog();
            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1() {
                        @Override
                        public void call(Object o) {
                            MyDetailInfoBean allInfoBean = GsonTools.getMyDetailInfoBean((String) o);
                            if (null != dialog)
                                dialog.dismiss();
                            if (allInfoBean.isSuccess()) {
                                initWedget(allInfoBean);
                            } else {
                                DialogUtils.setDialog(DataEdit.this, allInfoBean.getError());
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            Log.e("test", throwable.getMessage());
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 加载用户的信息
     */
    private void initWedget(MyDetailInfoBean allInfoBean) {
        MyDetailInfoItem infoItem = allInfoBean.getParam().getAllInfo();
        MeiMengApplication.sex = infoItem.getSex();

        meFirstNameTextView.setText(infoItem.getFirstName());
        meLastNameTextView.setText(infoItem.getLastName());
        meSexTextView.setText(infoItem.getSex()==0?"男":"女");
        //meBirthdayTextView.setText(Utils.getDisplayDate(infoItem.getBirthday()));
        String ageString=infoItem.getBirthday().substring(0,infoItem.getBirthday().indexOf(" "));
        meBirthdayTextView.setText(ageString);
        meChineseZodiacTextView.setText(infoItem.getZodiacValue());
        meChineseZodiacTextView.setTag(infoItem.getZodiacKey());
        meZodiacTextView.setText(infoItem.getConstellationValue());
        meZodiacTextView.setTag(infoItem.getConstellationKey());
        //基本信息
        if (infoItem.getHeight()  == 0) {
            meWeightTextView.setText("");
            meWeightTextView.setTag(infoItem.getHeight());
        }else{
            meHeightTextView.setText(infoItem.getHeight() + "cm");
            meHeightTextView.setTag(infoItem.getHeight());
        }
        if (infoItem.getWeight()  == 0) {
            meWeightTextView.setText("");
            meWeightTextView.setTag(infoItem.getWeight());
        }else{
            meWeightTextView.setText(infoItem.getWeight() + "kg");
            meWeightTextView.setTag(infoItem.getWeight());
        }
        meLocationTextView.setText(infoItem.getRediseValue());
        meLocationTextView.setTag(infoItem.getRediseKey());
        meNationTextView.setText(infoItem.getNationValue());
        meNationTextView.setTag(infoItem.getNationKey());
        meMarryTextView.setText(infoItem.getMaritalStatusValue());
        meMarryTextView.setTag(infoItem.getMaritalStatusKey());
        //教育工作
        meEducationTextView.setText(infoItem.getEducationValue());
        meEducationTextView.setTag(infoItem.getEducationKey());
        meSchoolEditText.setText(infoItem.getGraduateCollege());
        meTradesTextView.setText(infoItem.getCompanyIndustryValue());
        meTradesTextView.setTag(infoItem.getCompanyIndustryKey());
        meJobTextView.setText(infoItem.getCompanyPositionValue());
        meJobTextView.setTag(infoItem.getCompanyPositionKey());
        meOverseasTextView.setText(infoItem.getOutsideExperienceValue());
        meOverseasTextView.setTag(infoItem.getOutsideExperienceKey());
        //经济情况
        meYearlySalaryTextView.setText(infoItem.getYearIncomeValue());
        meYearlySalaryTextView.setTag(infoItem.getYearIncomeKey());
        meAssetsTextView.setText(infoItem.getPropertyValue());
        meAssetsTextView.setTag(infoItem.getPropertyKey());
        meFamilyBackgroundTextView.setText(infoItem.getFamilyBackgroundValue());
        meFamilyBackgroundTextView.setTag(infoItem.getFamilyBackgroundKey());
        meHouseTextView.setText(infoItem.getHouseStateValue());
        meHouseTextView.setTag(infoItem.getHouseStateKey());
        meCarTextView.setText(infoItem.getCarStateValue());
        meCarTextView.setTag(infoItem.getCarStateKey());

        //兴趣爱好
        initInterest((ArrayList<String>) infoItem.getLstLoveExercise(), sportLayout, R.layout.interest_sport_item, R.id.interest_sport_item_text_view);
        initInterest((ArrayList<String>) infoItem.getLstLoveMusic(), musicLayout, R.layout.interest_music_item, R.id.interest_music_item_text_view);
        initInterest((ArrayList<String>) infoItem.getLstLoveFood(), foodLayout, R.layout.interest_food_item, R.id.interest_food_item_text_view);
        initInterest((ArrayList<String>) infoItem.getLstLoveFilm(), movieLayout, R.layout.interest_movie_item, R.id.interest_movie_item_text_view);
        initInterest((ArrayList<String>) infoItem.getLstLoveBook(), bookLayout, R.layout.interest_book_item, R.id.interest_book_item_text_view);
        initInterest((ArrayList<String>) infoItem.getLstLoveTouristDestination(), travelLayout, R.layout.interest_travel_item,R.id.interest_travel_item_text_view);

        meStoryEditText.setText(infoItem.getUserStory());
    }

    //endregion

    //region others
    //提醒保存资料
    private void dataEditTipsDialog(final Context context) {

        View view = LayoutInflater.from(context).inflate(R.layout.register_tips, null);

        final Dialog dialog = new Dialog(context, R.style.CustomAlertDialog);
        dialog.setContentView(view);
        dialog.show();
        TextView tipstext = (TextView) view.findViewById(R.id.reguster_tips_text);
        tipstext.setText("是否放弃修改？");
        TextView cancelText = (TextView) view.findViewById(R.id.register_tips_cancel);
        cancelText.setText("继续编辑");
        cancelText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

        TextView open_upText = (TextView) view.findViewById(R.id.register_tips_sure);
        open_upText.setText("确定");
        open_upText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 & resultCode == 1) {
            if (data == null) {

            } else {
                if (data.getExtras().getInt("nationid") != 0) {
                    String nationstr = data.getExtras().getString("nation");//得到新Activity 关闭后返回的数据
                    meNationTextView.setTag(data.getExtras().getInt("nationid"));
                    meNationTextView.setText(nationstr);
                }
            }
        } else if (requestCode == 4 & resultCode == 4) {
            if (data == null) {
            } else {
                String companyIndustry = data.getExtras().getString("companyIndustry");//得到新Activity 关闭后返回的数据
                int companyIndustryId = data.getExtras().getInt("companyIndustryid");
                meTradesTextView.setText(companyIndustry);
                meTradesTextView.setTag(companyIndustryId);
            }

        } else  if (requestCode == 8 & resultCode == 8) {
            if (data == null) {

            } else {
                if (data.getExtras().getInt("educationid") != 0) {
                    meEducationTextView.setTag(data.getExtras().getInt("educationid"));
                    meEducationTextView.setText(data.getExtras().getString("education"));
                }
            }
        }else  if (requestCode == 9 & resultCode == 9) {
            if (data == null) {

            } else {
                if (data.getExtras().getInt("jobid") != 0) {
                    meJobTextView.setTag(data.getExtras().getInt("jobid"));
                    meJobTextView.setText(data.getExtras().getString("job"));
                }
            }
        }else  if (requestCode == 10 & resultCode == 10) {
            if (data == null) {

            } else {
                Object obj = data.getExtras().get("selectMap");
                if (obj != null) {
                    ArrayList<String> selects = (ArrayList<String>)obj;
                    initInterest(selects, sportLayout, R.layout.interest_sport_item,R.id.interest_sport_item_text_view);
                }
            }
        }else  if (requestCode == 11 & resultCode == 11) {
            if (data == null) {

            } else {
                Object obj = data.getExtras().get("selectMap");
                if (obj != null) {
                    ArrayList<String> selects = (ArrayList<String>)obj;
                    initInterest(selects, musicLayout, R.layout.interest_music_item,R.id.interest_music_item_text_view);
                }
            }
        }else  if (requestCode == 12 & resultCode == 12) {
            if (data == null) {

            } else {
                Object obj = data.getExtras().get("selectMap");
                if (obj != null) {
                    ArrayList<String> selects = (ArrayList<String>)obj;
                    initInterest(selects, foodLayout, R.layout.interest_food_item,R.id.interest_food_item_text_view);
                }
            }
        }else  if (requestCode == 13 & resultCode == 13) {
            if (data == null) {

            } else {
                Object obj = data.getExtras().get("selectMap");
                if (obj != null) {
                    ArrayList<String> selects = (ArrayList<String>)obj;
                    initInterest(selects, movieLayout, R.layout.interest_movie_item,R.id.interest_movie_item_text_view);
                }
            }
        }else  if (requestCode == 14 & resultCode == 14) {
            if (data == null) {

            } else {
                Object obj = data.getExtras().get("selectMap");
                if (obj != null) {
                    ArrayList<String> selects = (ArrayList<String>)obj;
                    initInterest(selects, bookLayout, R.layout.interest_book_item,R.id.interest_book_item_text_view);
                }
            }
        }else  if (requestCode == 15 & resultCode == 15) {
            if (data == null) {

            } else {
                Object obj = data.getExtras().get("selectMap");
                if (obj != null) {
                    ArrayList<String> selects = (ArrayList<String>)obj;
                    initInterest(selects, travelLayout, R.layout.interest_travel_item,R.id.interest_travel_item_text_view);
                }
            }
        }
    }

    //endregion

    //region layout listener

    //返回按钮
    @OnClick(R.id.title_left_arrow_layout)
    void leftArrowListener() {
        if (isSave == false) {
            dataEditTipsDialog(DataEdit.this);
        } else {
            finish();
        }
    }

    //身高点击事件
    @OnClick(R.id.me_height_layout)
    void onMeHeightLayoutClick(){
        singleContent = "170cm";
        singleId = 0;
        hideSelectDialog();
        typeWheel = 3;
        showSelectDialog("身高");
    }

    //婚姻状况点击事件
    @OnClick(R.id.me_marry_layout)
    void onMeMarryLayoutClick(){
        singleContent = "未婚";
        singleId = 0;
        hideSelectDialog();
        typeWheel = 58;
        showSelectDialog("婚姻状况");
    }

    //点击修改用户公司行业信息
    @OnClick(R.id.me_trades_layout)
    void companyIndustryListener() {
        Intent intent = new Intent(DataEdit.this, DataEditCompanyIndustry.class);
        startActivityForResult(intent, 4);
    }

    //点击修改体重信息
    @OnClick(R.id.me_weight_layout)
    void weightListener() {
        singleContent = "50kg";
        singleId = 0;
        hideSelectDialog();
        typeWheel = 11;
        showSelectDialog("体重");
    }

    //所在地
    @OnClick(R.id.me_location_layout)
    void residenceListener() {
        hideSelectDialog();
        typeWheel = 2;
        showSelectDialog("居住地");
    }

    //年收入
    @OnClick(R.id.me_yearly_salary_layout)
    void yearlySalaryListener() {
        singleContent = "30万以下";
        singleId = 0;
        hideSelectDialog();
        typeWheel = 4;
        showSelectDialog("年薪");
    }

    //学历
    @OnClick(R.id.me_education_layout)
    void educationListener() {
        Intent intent = new Intent(DataEdit.this, DataEditEducation.class);
        startActivityForResult(intent, 8);
        isSave=false;
    }

    // 职业
    @OnClick(R.id.me_job_layout)
    void industryListener() {
        Intent intent = new Intent(DataEdit.this, DataEditJob.class);
        startActivityForResult(intent, 9);
        isSave=false;
    }

    //住房情况
    @OnClick(R.id.me_house_layout)
    void houseStateListener() {
        singleContent = "有";
        singleId = 0;
        hideSelectDialog();
        typeWheel = 8;
        showSelectDialog("住房情况");
    }

    //购车情况
    @OnClick(R.id.me_car_layout)
    void carListener() {
        singleContent = "有";
        singleId = 0;
        hideSelectDialog();
        typeWheel = 9;
        showSelectDialog("购车情况");
    }

    //点击民族
    @OnClick(R.id.me_nation_layout)
    void nationListener() {
        Intent intent = new Intent(DataEdit.this, DataEditNation.class);
        startActivityForResult(intent, 1);
        isSave=false;
    }

    //资产
    @OnClick(R.id.me_asserts_layout)
    void assertsListener() {
        singleContent = "200万以下";
        singleId = 0;
        hideSelectDialog();
        typeWheel = 33;
        showSelectDialog("资产");
    }

    //家庭背景
    @OnClick(R.id.me_family_background_layout)
    void familyBackgroundListener() {
        singleContent = "一般";
        singleId = 0;
        hideSelectDialog();
        typeWheel = 35;
        showSelectDialog("家庭背景");
    }

    //喜欢的运动
    @OnClick(R.id.me_interest_sport_layout)
    void interestSportListener() {
        Intent intent = new Intent(DataEdit.this, DataEditSports.class);
        startActivityForResult(intent, 10);
        isSave=false;
    }

    //喜欢的食物
    @OnClick(R.id.me_interest_food_layout)
    void loveFoodListener() {
        Intent intent = new Intent(DataEdit.this, DataEditFood.class);
        startActivityForResult(intent, 12);
        isSave=false;
    }

    //喜欢的书
    @OnClick(R.id.me_interest_book_layout)
    void loveBookListener() {
        Intent intent = new Intent(DataEdit.this, DataEditBook.class);
        startActivityForResult(intent, 14);
        isSave=false;
    }

    //喜欢的音乐
    @OnClick(R.id.me_interest_music_layout)
    void loveMusicyListener() {
        Intent intent = new Intent(DataEdit.this, DataEditMusic.class);
        startActivityForResult(intent, 11);
        isSave=false;
    }

    //喜欢的电影
    @OnClick(R.id.me_interest_movie_layout)
    void loveFilmListener() {
        Intent intent = new Intent(DataEdit.this, DataEditMovie.class);
        startActivityForResult(intent, 13);
        isSave=false;
    }

    //喜欢的旅游去处
    @OnClick(R.id.me_interest_travel_layout)
    void loveTouristDestinationListener() {
        Intent intent = new Intent(DataEdit.this, DataEditTravel.class);
        startActivityForResult(intent, 15);
        isSave=false;
    }

    //海外经历
    @OnClick(R.id.me_overseas_layout)
    void outsideExperienceListener() {
        singleContent = "有";
        singleId = 0;
        hideSelectDialog();
        typeWheel = 56;
        showSelectDialog("海外经历");
    }


    @OnClick(R.id.wheel_cancel_text)
    void wheelCancelTextListener() {
        hideSelectDialog();
    }
    //endregion

    //region 点击滑动选择器确定
    //点击滑动选择器确定
    @OnClick(R.id.wheel_sure_text)
    void wheelSureTextListener() {
        switch (typeWheel) {
            case 2://居住地
                String address = cityPicker2.getAddress().substring(cityPicker2.getAddress().indexOf(",")+1);
                meLocationTextView.setText(address);
                meLocationTextView.setTag(cityPicker2.getAddressId());
                break;
            case 3://身高
                meHeightTextView.setText(singleContent);
                meHeightTextView.setTag(Integer.valueOf(singleContent.substring(0, 3)).intValue());
                break;
            case 4:
                meYearlySalaryTextView.setText(singleContent);
                meYearlySalaryTextView.setTag(singleId + 1);
                break;
            case 8:
                meHouseTextView.setText(singleContent);
                meHouseTextView.setTag(singleId + 1);
                break;
            case 9:
                meCarTextView.setText(singleContent);
                meCarTextView.setTag(singleId + 1);
                break;
            case 11://体重
                if (singleContent.equals("0kg")){
                    singleContent="";
                }
                meWeightTextView.setText(singleContent);
                meWeightTextView.setTag(Integer.valueOf(singleContent.substring(0, 2)).intValue());
                break;
            case 33:
                meAssetsTextView.setText(singleContent);
                meAssetsTextView.setTag(singleId + 1);
                break;
            case 35:
                meFamilyBackgroundTextView.setText(singleContent);
                meFamilyBackgroundTextView.setTag(singleId + 1);
                break;
            case 56:
                meOverseasTextView.setText(singleContent);
                meOverseasTextView.setTag(singleId + 1);
                break;
            case 58:
                meMarryTextView.setText(singleContent);
                meMarryTextView.setTag(singleId + 1);
                break;
            default:
                break;
        }
        hideSelectDialog();
    }
    //endregion

    //region dialog
    /**
     * 隐藏掉选择对话框
     */
    private void hideSelectDialog() {
        wheelLayout.setVisibility(View.INVISIBLE);
        wheelSelectLayout.setVisibility(View.GONE);
        wheelCoverButton.setVisibility(View.GONE);
        timePacker.setVisibility(View.INVISIBLE);
        cityPicker.setVisibility(View.INVISIBLE);
        cityPicker2.setVisibility(View.INVISIBLE);
        singleWheelView.setVisibility(View.INVISIBLE);
        wheelTiwnPicker.setVisibility(View.INVISIBLE);
    }

    /**
     * 显示选择对话框
     */
    private void showSelectDialog(String string) {
        isSave=false;
        wheelName.setText(string);
        wheelLayout.setVisibility(View.VISIBLE);
        wheelCoverButton.setVisibility(View.VISIBLE);
        wheelSelectLayout.setVisibility(View.VISIBLE);
        switch (typeWheel) {
            case 1:
                timePacker.setVisibility(View.VISIBLE);
                break;
            case 2:
                cityPicker2.setVisibility(View.VISIBLE);
                break;
            //身高
            case 3:
                singleWheelView.setVisibility(View.VISIBLE);
                singleWheelView.setData(getHeightList());
                singleWheelView.setDefault(20);
                break;
            //年收入
            case 4:
                singleWheelView.setVisibility(View.VISIBLE);
                ArrayList<String> yearList = getNationList(2);
                singleWheelView.setData(yearList);
                singleWheelView.setDefault(0);
                break;

            //学历
            case 5:
                singleWheelView.setVisibility(View.VISIBLE);
                singleWheelView.setData(getNationList(1));
                singleWheelView.setDefault(0);
                break;
            //职业
            case 6:
                singleWheelView.setVisibility(View.VISIBLE);
                singleWheelView.setData(getNationList(3));
                singleWheelView.setDefault(0);
                break;

            //婚姻状态
            case 7:
                singleWheelView.setVisibility(View.VISIBLE);
                singleWheelView.setData(getNationList(10));
                singleWheelView.setDefault(0);
                break;

            //住房情况
            case 8:
                singleWheelView.setVisibility(View.VISIBLE);
                singleWheelView.setData(getNationList(14));
                singleWheelView.setDefault(0);
                break;
            //购车情况
            case 9:
                singleWheelView.setVisibility(View.VISIBLE);
                singleWheelView.setData(getNationList(14));
                singleWheelView.setDefault(0);
                break;

            //体重
            case 11:
                singleWheelView.setVisibility(View.VISIBLE);
                singleWheelView.setData(getWeightList());
                singleWheelView.setDefault(10);
                break;

            //发型
            case 13:
                singleWheelView.setVisibility(View.VISIBLE);
                singleWheelView.setData(getNationList(19));
                singleWheelView.setDefault(0);
                break;
            //发色
            case 14:
                singleWheelView.setVisibility(View.VISIBLE);
                singleWheelView.setData(getNationList(20));
                singleWheelView.setDefault(0);
                break;
            //有无子女
            case 15:
                singleWheelView.setVisibility(View.VISIBLE);
                singleWheelView.setData(getNationList(6));
                singleWheelView.setDefault(0);
                break;

            //血型
            case 17:
                singleWheelView.setVisibility(View.VISIBLE);
                singleWheelView.setData(getNationList(9));
                singleWheelView.setDefault(0);
                break;
            //脸型
            case 18:
                singleWheelView.setVisibility(View.VISIBLE);
                singleWheelView.setData(getNationList(21));
                singleWheelView.setDefault(0);
                break;
            //体形
            case 19:

                singleWheelView.setVisibility(View.VISIBLE);
                singleWheelView.setData(getNationList(22));
                singleWheelView.setDefault(0);
                break;
            //眼睛颜色
            case 20:
                singleWheelView.setVisibility(View.VISIBLE);
                singleWheelView.setData(getNationList(23));
                singleWheelView.setDefault(0);
                break;
            //魅力部位
            case 21:
                singleWheelView.setVisibility(View.VISIBLE);
                singleWheelView.setData(getNationList(24));
                singleWheelView.setDefault(0);
                break;
            //公司类型
            case 23:
                singleWheelView.setVisibility(View.VISIBLE);
                singleWheelView.setData(getNationList(27));
                singleWheelView.setDefault(0);
                break;
            case 24:

                //工作状态
            case 25:
                singleWheelView.setVisibility(View.VISIBLE);
                singleWheelView.setData(getNationList(29));
                singleWheelView.setDefault(0);
                break;
            //收入描述
            case 26:
                singleWheelView.setVisibility(View.VISIBLE);
                singleWheelView.setData(getNationList(30));
                singleWheelView.setDefault(0);
                break;
            //入学年份
            case 27:
                singleWheelView.setVisibility(View.VISIBLE);
                singleWheelView.setData(getEnTranceYearList());
                singleWheelView.setDefault(0);
                break;
            //语言能力
            case 28:
                singleWheelView.setVisibility(View.VISIBLE);
                singleWheelView.setData(getNationList(31));
                singleWheelView.setDefault(0);
                break;
            //是否吸烟
            case 29:
                singleWheelView.setVisibility(View.VISIBLE);
                singleWheelView.setData(getNationList(32));
                singleWheelView.setDefault(0);
                break;
            //是否饮酒
            case 30:
                singleWheelView.setVisibility(View.VISIBLE);
                singleWheelView.setData(getNationList(33));
                singleWheelView.setDefault(0);
                break;
            //锻炼习惯
            case 31:
                singleWheelView.setVisibility(View.VISIBLE);
                singleWheelView.setData(getNationList(34));
                singleWheelView.setDefault(0);
                break;
            //作息习惯
            case 32:
                singleWheelView.setVisibility(View.VISIBLE);
                singleWheelView.setData(getNationList(35));
                singleWheelView.setDefault(0);
                break;
            //资产
            case 33:
                singleWheelView.setVisibility(View.VISIBLE);
                singleWheelView.setData(getNationList(15));
                singleWheelView.setDefault(0);
                break;
            //家中排行
            case 34:
                singleWheelView.setVisibility(View.VISIBLE);
                singleWheelView.setData(getNationList(36));
                singleWheelView.setDefault(0);
                break;
            //家庭背景
            case 35:
                singleWheelView.setVisibility(View.VISIBLE);
                singleWheelView.setData(getNationList(16));
                singleWheelView.setDefault(0);
                break;
            //是否愿意和ta的父母同住
            case 36:
                singleWheelView.setVisibility(View.VISIBLE);
                singleWheelView.setData(getNationList(37));
                singleWheelView.setDefault(0);
                break;
            //是否愿意要孩子
            case 37:
                singleWheelView.setVisibility(View.VISIBLE);
                singleWheelView.setData(getNationList(37));
                singleWheelView.setDefault(0);
                break;
            //宠物类型
            case 38:
                singleWheelView.setVisibility(View.VISIBLE);
                singleWheelView.setData(getNationList(38));
                singleWheelView.setDefault(0);
                break;
            //最大消费
            case 39:
                singleWheelView.setVisibility(View.VISIBLE);
                singleWheelView.setData(getNationList(39));
                singleWheelView.setDefault(0);
                break;
            //制造浪漫
            case 40:
                singleWheelView.setVisibility(View.VISIBLE);
                singleWheelView.setData(getNationList(40));
                singleWheelView.setDefault(0);
                break;
            //擅长生活技
            case 41:
                singleWheelView.setVisibility(View.VISIBLE);
                singleWheelView.setData(getNationList(41));
                singleWheelView.setDefault(0);
                break;
            //喜欢的运动
            case 42:
                singleWheelView.setVisibility(View.VISIBLE);
                singleWheelView.setData(getNationList(42));
                singleWheelView.setDefault(0);
                break;
            //喜欢的食物
            case 43:
                singleWheelView.setVisibility(View.VISIBLE);
                singleWheelView.setData(getNationList(43));
                singleWheelView.setDefault(0);
                break;
            //喜欢的书
            case 44:
                singleWheelView.setVisibility(View.VISIBLE);
                singleWheelView.setData(getNationList(44));
                singleWheelView.setDefault(0);
                break;
            //喜欢的音乐
            case 45:
                singleWheelView.setVisibility(View.VISIBLE);
                singleWheelView.setData(getNationList(45));
                singleWheelView.setDefault(0);
                break;
            //喜欢的电影
            case 46:
                singleWheelView.setVisibility(View.VISIBLE);
                singleWheelView.setData(getNationList(46));
                singleWheelView.setDefault(0);
                break;
            //关注的节目
            case 47:
                singleWheelView.setVisibility(View.VISIBLE);
                singleWheelView.setData(getNationList(47));
                singleWheelView.setDefault(0);
                break;
            //娱乐休闲
            case 48:
                singleWheelView.setVisibility(View.VISIBLE);
                singleWheelView.setData(getNationList(48));
                singleWheelView.setDefault(0);
                break;
            //业余爱好
            case 49:
                singleWheelView.setVisibility(View.VISIBLE);
                singleWheelView.setData(getNationList(49));
                singleWheelView.setDefault(0);
                break;
            //喜欢的旅游去处
            case 50:
                singleWheelView.setVisibility(View.VISIBLE);
                singleWheelView.setData(getNationList(50));
                singleWheelView.setDefault(0);
                break;
            //交友条件--年龄
            case 51:
                wheelTiwnPicker.setVisibility(View.VISIBLE);
                wheelTiwnPicker.setData(getdcAgeList());
                break;
            //交友条件--居住地
            case 52:
                cityPicker.setVisibility(View.VISIBLE);
                break;
            //交友条件--身高
            case 53:
                wheelTiwnPicker.setVisibility(View.VISIBLE);
                wheelTiwnPicker.setData(getdcHeightList());
                break;
            //交投条件--学历
            case 54:
                singleWheelView.setVisibility(View.VISIBLE);
                singleWheelView.setData(getdcEducationList());
                singleWheelView.setDefault(0);
                break;
            //交友条件--年收入收入
            case 55:
                singleWheelView.setVisibility(View.VISIBLE);
                singleWheelView.setData(getDcMonthlyIncomeList());
                singleWheelView.setDefault(0);
                break;
            //海外经历
            case 56:
                singleWheelView.setVisibility(View.VISIBLE);
                singleWheelView.setData(getNationList(14));
                singleWheelView.setDefault(0);
                break;
            case 57:
                singleWheelView.setVisibility(View.VISIBLE);
                singleWheelView.setData(getNationList(52));
                singleWheelView.setDefault(0);
                break;
            case 58:
                singleWheelView.setVisibility(View.VISIBLE);
                singleWheelView.setData(getMarryData());
                singleWheelView.setDefault(0);
                break;
            default:
                break;

        }

        ObjectAnimator.ofFloat(wheelLayout, "translationY", 240F * mDensity, 0).setDuration(500).start();
    }

    private ArrayList<String> getMarryData(){
        String[]  data={"未婚","离异未育","离异有孩子","丧偶"};
        ArrayList dataList=new ArrayList();
        if (data != null) {
            for (int i = 0; i <data.length ; i++) {
                dataList.add(data[i]);
            }
        }
        return dataList;
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
                    Toast.makeText(DataEdit.this, "当前网络不可用，请检查您的网络设置", Toast.LENGTH_SHORT).show();
                }
            } else {
                //不做操作
            }
        }
    };
    //endregion

    //region getXXXList
    /**
     * 得到交友年龄的数据
     *
     * @return
     */
    private ArrayList<Integer> getdcAgeList() {
        ArrayList<Integer> dcageList = new ArrayList<>();

        for (int i = 18; i < 60; i++) {
            dcageList.add(i);
        }
        return dcageList;
    }
    /**
     * 入学年份
     *
     */
    private ArrayList<String> getEnTranceYearList() {
        ArrayList<String> enTranceYearList = new ArrayList<>();
        Time time = new Time();
        time.setToNow();
        int iyear = time.year;
        for (int i = 1970; i < iyear + 1; i++) {
            enTranceYearList.add(String.valueOf(i));
        }
        return enTranceYearList;
    }

    /**
     * 得到身高的数据
     *
     */
    private ArrayList<String> getHeightList() {
        ArrayList<String> heightList = new ArrayList<>();
        for (int i = 150; i < 220; i++) {
            heightList.add(String.valueOf(i) + "cm");
        }
        return heightList;
    }
    /**
     * 得到交友身高的数据
     *
     */
    private ArrayList<Integer> getdcHeightList() {
        ArrayList<Integer> dcheightList = new ArrayList<>();

        for (int i = 150; i < 220; i++) {
            dcheightList.add(i);
        }
        return dcheightList;
    }

    /**
     * 得到交友学历的数据
     *
     */
    private ArrayList<String> getdcEducationList() {
        ArrayList<String> dceducationList = new ArrayList<>();
        dceducationList.add("不限");
        dceducationList.add("大专以下");
        dceducationList.add("大专");
        dceducationList.add("本科");
        dceducationList.add("硕士");
        dceducationList.add("博士");
        dceducationList.add("博士后");
        return dceducationList;
    }

    /**
     * 得到体重的数据
     *
     * @return
     */
    private ArrayList<String> getWeightList() {
        ArrayList<String> weightList = new ArrayList<>();
        for (int i = 40; i < 100; i++) {
            weightList.add(String.valueOf(i) + "kg");
        }
        return weightList;
    }

    /**
     * 得到月收入描述的数据
     *
     * @return
     */
    private ArrayList<String> getDcMonthlyIncomeList() {
        ArrayList<String> dcMonthlyIncomeList = new ArrayList<>();
        dcMonthlyIncomeList.add("不限");
        dcMonthlyIncomeList.add("30万以下");
        dcMonthlyIncomeList.add("30-50万");
        dcMonthlyIncomeList.add("50-100万");
        dcMonthlyIncomeList.add("100-300万");
        dcMonthlyIncomeList.add("300-500万");
        dcMonthlyIncomeList.add("500-1000万");
        dcMonthlyIncomeList.add("1000万以上");

        return dcMonthlyIncomeList;
    }

    /**
     * 滑动选择器里面的数据
     *
     * @param id
     * @return
     */
    private ArrayList<String> getNationList(int id) {
        nationHashMap.clear();

        DbOpenHelper dbOpenHelper = new DbOpenHelper(DataEdit.this);
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
    //endregion

    //region overrides
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (isSave == false) {
            dataEditTipsDialog(DataEdit.this);
        } else {
            finish();
        }
    }
    //endregion

}
