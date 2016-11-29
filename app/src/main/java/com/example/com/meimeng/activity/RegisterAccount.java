package com.example.com.meimeng.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.com.meimeng.MeiMengApplication;
import com.example.com.meimeng.R;
import com.example.com.meimeng.activity.dataedit.DataEditNation;
import com.example.com.meimeng.custom.CityPicker2;
import com.example.com.meimeng.custom.TimePicker;
import com.example.com.meimeng.custom.WheelView;
import com.example.com.meimeng.db.DbOpenHelper;
import com.example.com.meimeng.gson.GsonTools;
import com.example.com.meimeng.gson.gsonbean.BaseBean;
import com.example.com.meimeng.net.InternetConstant;
import com.example.com.meimeng.net.InternetUtils;
import com.example.com.meimeng.util.GetDbUtils;
import com.example.com.meimeng.util.Utils;

import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by Administrator on 2015/7/29.
 */
public class RegisterAccount extends BaseActivity {

    //title按钮
    @Bind(R.id.title_text)
    TextView titleText;

    @Bind(R.id.title_left_arrow_image_view)
    ImageView leftArrowImageView;

    @Bind(R.id.bow_arrow_image_view)
    ImageView bowArrowImageView;

    @Bind(R.id.title_sure_text)
    TextView sureText;

    @Bind(R.id.register_account_nextButton)
    Button nextButton;

    @Bind(R.id.wheel_layout)
    RelativeLayout wheelLayout;

    @Bind(R.id.wheel_cover_button)
    Button wheelCoverButton;

    @Bind(R.id.wheel_select_layout)
    RelativeLayout wheelSelectLayout;

    @Bind(R.id.wheel_city_picker2)
    CityPicker2 cityPicker;

    @Bind(R.id.wheel_single_view)
    WheelView singleWheelView;

    @Bind(R.id.wheel_time_picker)
    TimePicker timePacker;

    //需要编辑的内容
    @Bind(R.id.register_account_nickName)  //获取用户昵称
            EditText nicknameText;

    @Bind(R.id.register_account_realname)  //获取用户真实姓名
            EditText realnameText;

    @Bind(R.id.register_account_email)  //获取用户电子邮件
            EditText emailText;

    @Bind(R.id.register_account_nationality)  //获取用户国籍
            EditText nationalityText;

    @Bind(R.id.register_account_carBrand)  //获取用户汽车品牌信息
            EditText carBrandText;


    @Bind(R.id.register_account_outsideExperience)  //获取用户海外经历
            TextView outsideExperienceText;

    @Bind(R.id.register_account_property)  //获取用户资产信息
            TextView propertyText;

    @Bind(R.id.register_account_familyBackground)  //获取用户家庭背景
            TextView familyBackgroundText;

    @Bind(R.id.register_account_birthday) //获取年龄信息
            TextView ageText;

    @Bind(R.id.register_account_constellation)   //获取星座信息
            TextView constellationText;

    @Bind(R.id.register_account_zodiac)  //获取属相信息
            TextView zodiacText;

    @Bind(R.id.register_account_height)  //身高
            TextView heightText;

    @Bind(R.id.register_account_weight)  //体重
            TextView weightText;

    @Bind(R.id.register_account_education)   //学历
            TextView educationText;

    @Bind(R.id.register_account_maritalStatus)  //婚姻状况
            TextView maritalStatusText;

    @Bind(R.id.register_account_hasChildren)   //是否有孩子
            TextView hasChildrenText;

    @Bind(R.id.register_account_residence)   //居住地
            TextView residenceText;

    @Bind(R.id.register_account_bloodType)   //血型
            TextView bloodTypeText;

    @Bind(R.id.register_account_nation)  //民族
            TextView nationText;
    @Bind(R.id.register_account_yearIncome)   //年收入
            TextView yearIncomeText;

    @Bind(R.id.register_account_occupation)   //职业
            TextView occupationText;

    @Bind(R.id.register_account_houseState)  //住房情况
            TextView houseStatusText;

    @Bind(R.id.register_account_carState)   //购车情况
            TextView carText;

    private String nickname;
    private String realname = null;
    private String email = null;
    private String wxNo = null;
    private String birthDay = null;
    private int height;
    private int weight;
    private String nationality = null;
    private int outsideExperienceId = 0;
    private String carBrand = null;
    private int constellId = 0;
    private int zodiacId = 0;
    private int educationId = 0;
    private int maritalId = 0;
    private String nationstr;
    private int hasChildrenId = 0;
    private int residenceId = 0;
    private int bloodId = 0;
    private int nationId = 0;
    private int occupationId = 0;
    private int propertyId = 0;
    private int familyBackgroundId = 0;
    private int houseStatusId = 0;
    private int carId = 0;
    private int yearIncomeId = 0;

    private BaseBean basebeanJson;

    //正在编辑的类型
    private int typeWheel = 1;
    private float mDensity = 1f;

    //记录当前单个滚动条的内容
    private volatile String singleContent = null;
    private volatile int singleId = 0;

    private HashMap<String, Integer> nationHashMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.register_account);
        ButterKnife.bind(this);
        MeiMengApplication.currentContext=this;
        MeiMengApplication.loginActivity.add(this);

        bowArrowImageView.setVisibility(View.GONE);
        leftArrowImageView.setVisibility(View.VISIBLE);
        titleText.setText("完善资料");
        sureText.setVisibility(View.GONE);

        //下一步
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nickname = nicknameText.getText().toString();
                realname = realnameText.getText().toString();
                email = emailText.getText().toString();
                nationality = nationalityText.getText().toString();
                carBrand = carBrandText.getText().toString();
                if (nickname.equals("") || nickname == null) {
                    Toast.makeText(com.example.com.meimeng.activity.RegisterAccount.this, "昵称不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    if (birthDay == null) {
                        Toast.makeText(com.example.com.meimeng.activity.RegisterAccount.this, "出生日期不能为空", Toast.LENGTH_SHORT).show();
                    } else {
                        if (residenceId == 0) {
                            Toast.makeText(com.example.com.meimeng.activity.RegisterAccount.this, "所在地区不能为空", Toast.LENGTH_SHORT).show();
                        } else {
                            if (email.equals("") || email == null) {
                                iniview();
                            } else {
                                if (!email.matches("\\w+@\\w+\\.\\w+")) {
                                    Toast.makeText(com.example.com.meimeng.activity.RegisterAccount.this, "邮箱格式不对", Toast.LENGTH_SHORT).show();
                                } else {
                                    iniview();
                                }
                            }
                        }

                    }
                }
            }
        });

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

    }

    /**
     * 提交资料
     */
    private void iniview() {
        try {
            //获取用户的uid,和token
            if (TextUtils.isEmpty(Utils.getUserId())) {
                return;
            }
            if (TextUtils.isEmpty(Utils.getUserToken())) {
                return;
            }
            String url = InternetConstant.SERVER_URL + InternetConstant.USER_EDIT + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();
            JSONStringer jsonStringer = new JSONStringer().object()
                    .key("nickname").value(nickname)
                    .key("realname").value(realname)
                    .key("email").value(email)
                    .key("wxNo").value(wxNo)
                    .key("birthday").value(birthDay)
                    .key("constellation").value(String.valueOf(constellId))
                    .key("zodiac").value(String.valueOf(zodiacId))
                    .key("height").value(height)
                    .key("weight").value(weight)
                    .key("education").value(String.valueOf(educationId))
                    .key("maritalStatus").value(String.valueOf(maritalId))
                    .key("hasChildren").value(String.valueOf(hasChildrenId))
                    .key("residence").value(String.valueOf(residenceId))
                    .key("nationality").value(nationality)
                    .key("outsideExperience").value(outsideExperienceId)
                    .key("bloodType").value(String.valueOf(bloodId))
                    .key("nation").value(String.valueOf(nationId))
                    .key("occupation").value(String.valueOf(occupationId))
                    .key("yearIncome").value(yearIncomeId)
                    .key("property").value(String.valueOf(propertyId))
                    .key("familyBackground").value(String.valueOf(familyBackgroundId))
                    .key("houseState").value(String.valueOf(houseStatusId))
                    .key("carState").value(String.valueOf(carId))
                    .key("carBrand").value(carBrand)
                    .endObject();
            String jsonStr = jsonStringer.toString();
            //得到Observable并获取返回的数据(主线程中)
            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            basebeanJson = GsonTools.getBaseReqBean(s);
                            if (basebeanJson.isSuccess() != false) {
                            } else
                                Toast.makeText(com.example.com.meimeng.activity.RegisterAccount.this, basebeanJson.getError(), Toast.LENGTH_SHORT).show();
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
        Utils.registertipsDialog(RegisterAccount.this);

    }

    //返回按钮
    @OnClick(R.id.title_left_arrow_layout)
    void leftArrowListener() {
        onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MeiMengApplication.loginActivity.remove(this);
    }

    //点击修改生日信息
    @OnClick(R.id.register_account_birthday_layout)
    void ageListener() {
        hideSelectDialog();
        typeWheel = 1;
        showSelectDialog();
    }

    //点击修改体重信息
    @OnClick(R.id.register_account_weight_layout)
    void weightListener() {
        singleContent = "50kg";
        singleId = 0;
        hideSelectDialog();
        typeWheel = 11;
        showSelectDialog();
    }

    //所在地
    @OnClick(R.id.register_account_residencee_layout)
    void residenceListener() {
        hideSelectDialog();
        typeWheel = 2;
        showSelectDialog();
    }

    //身高
    @OnClick(R.id.register_account_height_layout)
    void heightListener() {
        singleContent = "170cm";
        singleId = 0;
        hideSelectDialog();
        typeWheel = 3;
        showSelectDialog();
    }

    //年收入
    @OnClick(R.id.register_account_yearIncome_layout)
    void mouthLyListener() {
        singleContent = "30万以下";
        singleId = 0;
        hideSelectDialog();
        typeWheel = 4;
        showSelectDialog();
    }

    //学历
    @OnClick(R.id.register_account_education_layout)
    void educationListener() {
        singleContent = "大专以下";
        singleId = 0;
        hideSelectDialog();
        typeWheel = 5;
        showSelectDialog();
    }

    // 职业
    @OnClick(R.id.register_account_occupation_layout)
    void industryListener() {
        singleContent = "普通职工";
        singleId = 0;
        hideSelectDialog();
        typeWheel = 6;
        showSelectDialog();
    }

    @OnClick(R.id.register_account_maritalStatus_layout)
    void maritalListener() {
        singleContent = "未婚";
        singleId = 0;
        hideSelectDialog();
        typeWheel = 7;
        showSelectDialog();
    }

    @OnClick(R.id.register_account_houseState_layout)
    void houseStateListener() {
        singleContent = "已购房";
        singleId = 0;
        hideSelectDialog();
        typeWheel = 8;
        showSelectDialog();
    }

    @OnClick(R.id.register_account_carState_layout)
    void carListener() {
        singleContent = "无";
        singleId = 0;
        hideSelectDialog();
        typeWheel = 9;
        showSelectDialog();
    }

    @OnClick(R.id.register_account_nation_layout)
    void nationListener() {
        Intent intent = new Intent(RegisterAccount.this, DataEditNation.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 & resultCode == 1) {
            if (data == null) {
                nationstr = null;
                nationId = 0;
            } else {
                nationstr = data.getExtras().getString("nation");//得到新Activity 关闭后返回的数据
                nationId = data.getExtras().getInt("nationid");
            }
            nationText.setText(nationstr);
        }

    }

    @OnClick(R.id.register_account_hasChildren_layout)
    void hasChildrenListener() {
        singleContent = "无小孩";
        singleId = 0;
        hideSelectDialog();
        typeWheel = 15;
        showSelectDialog();
    }

    @OnClick(R.id.register_account_bloodType_layout)
    void bloodListener() {
        singleContent = "A型";
        singleId = 0;
        hideSelectDialog();
        typeWheel = 17;
        showSelectDialog();
    }

    //资产
    @OnClick(R.id.register_account_property_layout)
    void propertyListener() {
        singleContent = "200万以下";
        singleId = 0;
        hideSelectDialog();
        typeWheel = 33;
        showSelectDialog();
    }

    //家庭背景
    @OnClick(R.id.register_account_familyBackground_layout)
    void familyBackgroundListener() {
        singleContent = "一般";
        singleId = 0;
        hideSelectDialog();
        typeWheel = 35;
        showSelectDialog();
    }

    //海外经历
    @OnClick(R.id.register_account_outsideExperience_layout)
    void outsideExperienceListener() {
        singleContent = "有";
        singleId = 0;
        hideSelectDialog();
        typeWheel = 56;
        showSelectDialog();
    }

    @OnClick(R.id.wheel_cancel_text)
    void wheelCancelTextListener() {
        hideSelectDialog();
    }

    //点击滑动选择器确定
    @OnClick(R.id.wheel_sure_text)
    void wheelSureTextListener() {

        switch (typeWheel) {
            case 1:
                birthDay = timePacker.getData();
                ageText.setText(timePacker.getData2());
                int yearm = Integer.valueOf(timePacker.getYear()) - 1935;
                int monthm = Integer.valueOf(timePacker.getMonth());
                int daym = Integer.valueOf(timePacker.getDay());

                if (yearm < 13) {
                    switch (yearm) {
                        case 5:
                            zodiacText.setText("龙");
                            zodiacId = yearm;
                            break;
                        case 6:
                            zodiacText.setText("蛇");
                            zodiacId = yearm;
                            break;
                        case 7:
                            zodiacText.setText("马");
                            zodiacId = yearm;
                            break;
                        case 8:
                            zodiacText.setText("羊");
                            zodiacId = yearm;
                            break;
                        case 9:
                            zodiacText.setText("猴");
                            zodiacId = yearm;
                            break;
                        case 10:
                            zodiacText.setText("鸡");
                            zodiacId = yearm;
                            break;
                        case 11:
                            zodiacText.setText("狗");
                            zodiacId = yearm;
                            break;
                        case 12:
                            zodiacText.setText("猪");
                            zodiacId = yearm;
                            break;
                    }
                } else {
                    switch (yearm % 12) {
                        case 1:
                            zodiacText.setText("鼠");
                            zodiacId = yearm % 12;
                            break;
                        case 2:
                            zodiacText.setText("牛");
                            zodiacId = yearm % 12;
                            break;
                        case 3:
                            zodiacText.setText("虎");
                            zodiacId = yearm % 12;
                            break;
                        case 4:
                            zodiacText.setText("兔");
                            zodiacId = yearm % 12;
                            break;
                        case 5:
                            zodiacText.setText("龙");
                            zodiacId = yearm % 12;
                            break;
                        case 6:
                            zodiacText.setText("蛇");
                            zodiacId = yearm % 12;
                            break;
                        case 7:
                            zodiacText.setText("马");
                            zodiacId = yearm % 12;
                            break;
                        case 8:
                            zodiacText.setText("羊");
                            zodiacId = yearm % 12;
                            break;
                        case 9:
                            zodiacText.setText("猴");
                            zodiacId = yearm % 12;
                            break;
                        case 10:
                            zodiacText.setText("鸡");
                            zodiacId = yearm % 12;
                            break;
                        case 11:
                            zodiacText.setText("狗");
                            zodiacId = yearm % 12;
                            break;
                        case 12:
                            zodiacText.setText("猪");
                            zodiacId = yearm % 12;
                            break;
                    }
                }

                if (monthm == 1 && daym >= 20 || monthm == 2 && daym <= 18) {
                    constellationText.setText("水瓶座");
                    constellId = 11;
                }
                if (monthm == 2 && daym >= 19 || monthm == 3 && daym <= 20) {
                    constellId = 12;
                    constellationText.setText("双鱼座");
                }
                if (monthm == 3 && daym >= 21 || monthm == 4 && daym <= 19) {
                    constellId = 1;
                    constellationText.setText("白羊座");
                }
                if (monthm == 4 && daym >= 20 || monthm == 5 && daym <= 20) {
                    constellId = 2;
                    constellationText.setText("金牛座");
                }
                if (monthm == 5 && daym >= 21 || monthm == 6 && daym <= 21) {
                    constellId = 3;
                    constellationText.setText("双子座");
                }
                if (monthm == 6 && daym >= 22 || monthm == 7 && daym <= 22) {
                    constellId = 4;
                    constellationText.setText("巨蟹座");
                }
                if (monthm == 7 && daym >= 23 || monthm == 8 && daym <= 22) {
                    constellId = 5;
                    constellationText.setText("狮子座");
                }
                if (monthm == 8 && daym >= 23 || monthm == 9 && daym <= 22) {
                    constellId = 6;
                    constellationText.setText("处女座");
                }
                if (monthm == 9 && daym >= 23 || monthm == 10 && daym <= 22) {
                    constellId = 7;
                    constellationText.setText("天秤座");
                }
                if (monthm == 10 && daym >= 23 || monthm == 11 && daym <= 21) {
                    constellId = 8;
                    constellationText.setText("天蝎座");
                }
                if (monthm == 11 && daym >= 22 || monthm == 12 && daym <= 21) {
                    constellId = 9;
                    constellationText.setText("射手座");
                }
                if (monthm == 12 && daym >= 22 || monthm == 1 && daym <= 19) {
                    constellId = 10;
                    constellationText.setText("摩羯座");
                }
                break;
            case 2:
                String address = cityPicker.getAddress();
                residenceText.setText(address);
                //得到城市的id
                residenceId = cityPicker.getAddressId();
                break;

            case 3:
                heightText.setText(singleContent);
                height = Integer.valueOf(singleContent.substring(0, 3)).intValue();
                break;
            case 4:
                yearIncomeText.setText(singleContent);
                yearIncomeId = singleId + 1;
                break;
            case 5:
                educationText.setText(singleContent);
                educationId = singleId + 1;

                break;
            case 6:
                occupationText.setText(singleContent);
                occupationId = singleId + 1;

                break;
            case 7:
                maritalStatusText.setText(singleContent);
                maritalId = singleId + 1;

                break;
            case 8:
                houseStatusText.setText(singleContent);
                houseStatusId = singleId + 1;

                break;
            case 9:
                carText.setText(singleContent);
                carId = singleId + 1;

                break;
            case 11:
                weightText.setText(singleContent);
                weight = Integer.valueOf(singleContent.substring(0, 2)).intValue();
                break;
            case 15:
                hasChildrenText.setText(singleContent);
                hasChildrenId = singleId + 1;
                break;
            case 17:
                bloodTypeText.setText(singleContent);
                bloodId = singleId + 1;
                break;

            case 33:
                propertyText.setText(singleContent);
                propertyId = singleId + 1;
                break;
            case 35:
                familyBackgroundText.setText(singleContent);
                familyBackgroundId = singleId + 1;
                break;
            case 56:
                outsideExperienceText.setText(singleContent);
                outsideExperienceId = singleId + 1;
                break;

            default:
                break;
        }
        hideSelectDialog();
    }

    /**
     * 隐藏掉选择对话框
     */
    private void hideSelectDialog() {
        wheelLayout.setVisibility(View.INVISIBLE);
        wheelSelectLayout.setVisibility(View.GONE);
        wheelCoverButton.setVisibility(View.GONE);
        timePacker.setVisibility(View.INVISIBLE);
        cityPicker.setVisibility(View.INVISIBLE);
        singleWheelView.setVisibility(View.INVISIBLE);
    }

    /**
     * 显示选择对话框
     */
    private void showSelectDialog() {
        wheelLayout.setVisibility(View.VISIBLE);
        wheelCoverButton.setVisibility(View.VISIBLE);
        wheelSelectLayout.setVisibility(View.VISIBLE);
        switch (typeWheel) {
            case 1:
                timePacker.setVisibility(View.VISIBLE);
                break;
            case 2:
                cityPicker.setVisibility(View.VISIBLE);
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
                singleWheelView.setData(getNationList(2));
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
                singleWheelView.setData(getNationList(11));
                singleWheelView.setDefault(0);
                break;
            //购车情况
            case 9:
                singleWheelView.setVisibility(View.VISIBLE);
                singleWheelView.setData(getNationList(17));
                singleWheelView.setDefault(0);
                break;
            //体重
            case 11:
                singleWheelView.setVisibility(View.VISIBLE);
                singleWheelView.setData(getWeightList());
                singleWheelView.setDefault(10);
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
            //资产
            case 33:
                singleWheelView.setVisibility(View.VISIBLE);
                singleWheelView.setData(getNationList(15));
                singleWheelView.setDefault(0);
                break;
            //家庭背景
            case 35:
                singleWheelView.setVisibility(View.VISIBLE);
                singleWheelView.setData(getNationList(16));
                singleWheelView.setDefault(0);
                break;

            //海外经历
            case 56:
                singleWheelView.setVisibility(View.VISIBLE);
                singleWheelView.setData(getNationList(14));
                singleWheelView.setDefault(0);
                break;
            default:
                break;

        }

        ObjectAnimator.ofFloat(wheelLayout, "translationY", 240F * mDensity, 0).setDuration(500).start();
    }

    /**
     * 得到身高的数据
     *
     * @return
     */
    private ArrayList<String> getHeightList() {
        ArrayList<String> heightList = new ArrayList<>();
        for (int i = 150; i < 220; i++) {
            heightList.add(String.valueOf(i) + "cm");
        }
        return heightList;
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
     * 滑动选择器里面的数据
     *
     * @param id
     * @return
     */
    private ArrayList<String> getNationList(int id) {
        nationHashMap.clear();

        DbOpenHelper dbOpenHelper = new DbOpenHelper(RegisterAccount.this);
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

}
