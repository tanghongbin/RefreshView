package com.example.com.meimeng.activity;

import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.avoscloud.leanchatlib.controller.ChatManager;
import com.example.com.meimeng.MeiMengApplication;
import com.example.com.meimeng.R;
import com.example.com.meimeng.adapter.GiftAdapter;
import com.example.com.meimeng.adapter.OtherPhotosFragmentAdapter;
import com.example.com.meimeng.bean.ChatMessageBean;
import com.example.com.meimeng.bean.PhotoIdBean;
import com.example.com.meimeng.constants.CommonConstants;
import com.example.com.meimeng.custom.CircleWaveView;
import com.example.com.meimeng.custom.MyViewPager;
import com.example.com.meimeng.fragment.GiveGiftFragment;
import com.example.com.meimeng.fragment.ReportFragment;
import com.example.com.meimeng.gson.GsonTools;
import com.example.com.meimeng.gson.gsonbean.BaseBean;
import com.example.com.meimeng.gson.gsonbean.DeleteInfoBean;
import com.example.com.meimeng.gson.gsonbean.FollowBean;
import com.example.com.meimeng.gson.gsonbean.MatchMakerGetBean;
import com.example.com.meimeng.gson.gsonbean.ReceivedGiftItem;
import com.example.com.meimeng.gson.gsonbean.UserPartInfoBean;
import com.example.com.meimeng.gson.gsonbean.UserPartInfoItem;
import com.example.com.meimeng.net.BuildString;
import com.example.com.meimeng.net.InternetConstant;
import com.example.com.meimeng.net.InternetUtils;
import com.example.com.meimeng.util.ChatSQliteDataUtil;
import com.example.com.meimeng.util.DialogUtils;
import com.example.com.meimeng.util.MessageUtils;
import com.example.com.meimeng.util.Utils;
import com.melnykov.fab.ObservableScrollView;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;

import org.apmem.tools.layouts.FlowLayout;
import org.json.JSONException;
import org.json.JSONStringer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

//import com.melnykov.fab.FloatingActionButton;
//import com.melnykov.fab.ObservableScrollView;

/**
 * Created by 010 on 2015/7/16.
 * <p/>
 * 他人页面
 */

public class OthersSelfActivity extends FragmentActivity implements View.OnClickListener, GiveGiftFragment.OnGiftListener, ReportFragment.ReportDialog {

    //@Bind(R.id.others_self_grid_view)
    //GridView photoGridView;//展示用户照片

   /* @Bind(R.id.others_self_gift_grid_view)
    GridView giftGridView;//展示收到的用户礼物*/

    @Bind(R.id.photo_layout)
    LinearLayout photoLayout;//展示用户照片的外面的layout

    @Bind(R.id.main_dialog_layout)
    LinearLayout dialogLayout;

    @Bind(R.id.others_self_give_gift_layout)//展示收到的用户礼物外面的layout
            LinearLayout giveGiftLayout;

    @Bind(R.id.others_self_top_view)
    LinearLayout topLayout;//上面部分的界面

    @Bind(R.id.title_text)
    TextView titleText;//标题栏

    @Bind(R.id.others_more_menu_image_view)
    ImageView bowArrowImageView;//右上角图标


    OtherPhotosFragmentAdapter mAdapter;
    MyViewPager mPager;
    PageIndicator mIndicator;

    @Bind(R.id.others_vote_image_button)
    ImageButton voteButton;

   /* @Bind(R.id.others_follow_image_button)
    ImageButton followButton;*/

    @Bind(R.id.common_name_text_view)
    TextView commonNameTextView;

    @Bind(R.id.common_id_text_view)
    TextView commonIdTextView;

    @Bind(R.id.common_age_text_view)
    TextView commonAgeTextView;

    @Bind(R.id.common_height_text_view)
    TextView commonHeightTextView;

    @Bind(R.id.common_location_text_view)
    TextView commonLocationTextView;

    @Bind(R.id.common_vip_lvl_image_view)
    ImageView commonVipLvlImageView;

    @Bind(R.id.common_vip_lvl_text_view)
    TextView commonVipLvlTextView;

    @Bind(R.id.common_follow_num_text_view)
    TextView commonLikeStarTextView;

    @Bind(R.id.common_follow_num_image_view)
    ImageView commonLikeStarImageView;//星星图标

    @Bind(R.id.user_identify_image)
    ImageView user_identify_image;//身份证明
    @Bind(R.id.money_identify_image)
    ImageView money_identify_image;//财产认证
    @Bind(R.id.education_identify_image)
    ImageView education_identify_image;//学历认证
    @Bind(R.id.work_identify_image)
    ImageView work_identify_image;//工作证明
    @Bind(R.id.marriage_identify_image)
    ImageView marriage_identify_image;//婚姻证明
    //@Bind(R.id.person_sex)
    //TextView personSex;
    @Bind(R.id.user_identify_text)
    TextView useridentifytext;
    @Bind(R.id.gift_layout)
    LinearLayout gift_others;
    @Bind(R.id.money_identify_text)
    TextView moneyidentifytext;
    @Bind(R.id.marrige_identify_text)
    TextView marrigeidentifytext;
    @Bind(R.id.work_identify_text)
    TextView workidentifytext;
    @Bind(R.id.education_identify_text)
    TextView educationidentifytext;

    @Bind(R.id.others_weight_textview)
    TextView weightTextView;
    @Bind(R.id.others_chinese_zodiac_textview)
    TextView chineseZodiacTextView;
    @Bind(R.id.others_zodiac_textview)
    TextView zodiacTextView;
    @Bind(R.id.others_nation_textview)
    TextView nationTextView;
    @Bind(R.id.others_marry_textview)
    TextView marryTextView;
    @Bind(R.id.others_education_textview)
    TextView educationTextView;
    @Bind(R.id.others_school_textview)
    TextView schoolTextView;
    @Bind(R.id.others_trades_textview)
    TextView tradesTextView;
    @Bind(R.id.others_job_textview)
    TextView jobTextView;
    @Bind(R.id.others_overseas_textview)
    TextView overseasTextView;
    @Bind(R.id.others_yearly_salary_textview)//年薪
            TextView yearlySalaryTextView;
    @Bind(R.id.others_assets_textview)//资产
            TextView assetsTextView;
    @Bind(R.id.others_family_background_textview)//家庭背景
            TextView familyBackgroundTextView;
    @Bind(R.id.others_house_textview)//住房情况
            TextView houseTextView;
    @Bind(R.id.others_car_textview)//购车情况
            TextView carTextView;
    @Bind(R.id.others_interest_sport_items_layout)
    FlowLayout sportLayout;
    @Bind(R.id.others_interest_music_items_layout)
    FlowLayout musicLayout;
    @Bind(R.id.others_interest_food_items_layout)
    FlowLayout foodLayout;
    @Bind(R.id.others_interest_movie_items_layout)
    FlowLayout movieLayout;
    @Bind(R.id.others_interest_book_items_layout)
    FlowLayout bookLayout;
    @Bind(R.id.others_interest_travel_items_layout)
    FlowLayout travelLayout;
    @Bind(R.id.others_story_edit_text)
    TextView storyTextView;

    //@Bind(R.id.self_header_image_view)
    // ImageView self_header_image_view;//用户头像

    //@Bind(R.id.self_header_background_image_view)
    //ImageView self_header_background_image_view;//用户大背景头像

    @Bind(R.id.others_title_left_arrow_image_view)
    ImageView leftArrowImageView;//返回图标

    private GiveGiftFragment giveGiftFragment;
    private ReportFragment reportFragment;
    private LinearLayout.LayoutParams photoParams;
    //private PictureAdapter photoAdapter;
    private ArrayList<PhotoIdBean> photoList = new ArrayList<>();
    private ArrayList<ReceivedGiftItem> gifts = new ArrayList<>();
    private GiftAdapter giftAdapter;
    private ArrayList<ReceivedGiftItem> giftList = new ArrayList<>();
    private boolean isfollow = false;
    private boolean hasvoted = false;
    private FollowBean followBean;
    private long targetUid;
    private long voteByuid;
    private DisplayMetrics dm;
    private int attentionId;
    private int AClick = 0;
    private int BClick = 0;
    private UserPartInfoItem infoItem;
    private String userName;

    private SharedPreferences sp;
    private boolean isFirstOthers;
    private boolean  flag;
    private ImageView guideImage;


    private ChatSQliteDataUtil sq;

    //more弹出菜单
    private ImageView moreMenuImageView;
    private PopupWindow menuPopWindow;// popupwindow
    private LinearLayout reportLayout;
    //private FloatingActionButton fab;
    private ImageView fab;
    private CircleWaveView circle;

    private Dialog dialog;
    private long otherUserId;
   /* //图片解锁弹出窗口
    private PopupWindow unlockPopWindow;
    private LinearLayout passwordLayout;
    private ImageView lockedImageView;
    private EditText passwordEditText1;
    private EditText passwordEditText2;
    private EditText passwordEditText3;
    private EditText passwordEditText4;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.others_self);
        ButterKnife.bind(this);
        MeiMengApplication.currentContext = this;
        //从前一个Activity中传进来targetUid
        Intent intent = this.getIntent();
        targetUid = intent.getExtras().getLong("targetUid");
        //从用户信息中知道是不是关注了该用户，改变UI
        dialog = Utils.createLoadingDialog(OthersSelfActivity.this, "载入中...");
        dialog.show();
        initView();
    }

    /**
     * 取消礼物dialog
     */
    @OnClick(R.id.dialog_button)
    public void dialogListener() {
        cancelGiftDialog();
    }

   /* private void onPasswordInputTextChanged() {
        String password1 = passwordEditText1.getText().toString();
        String password2 = passwordEditText2.getText().toString();
        String password3 = passwordEditText3.getText().toString();
        String password4 = passwordEditText4.getText().toString();

        if (!password1.equals("") && !password2.equals("") && !password3.equals("") && !password4.equals("")) {
            unLockPhoto(password1 + password2 + password3 + password4);
        }
    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.others_more_menu_image_view://右上角快捷入口

                reportUser();
                break;
            case R.id.others_report_layout:
                reportUser();
                break;
           /* case R.id.others_unlock_image_view:
                if (unlockPopWindow.isShowing()) {

                    unlockPopWindow.dismiss();// 关闭
                } else {
                    unlockPopWindow.showAtLocation(lockedImageView, Gravity.CENTER, 0, -40);
                }
                break;*/
            default:
                break;
        }
    }

   /* private void initUnlockPopupWindow() {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.others_unlock_popmenuview, null);
        passwordLayout = (LinearLayout) layout.findViewById(R.id.others_password_layout);
        unlockPopWindow = new PopupWindow(layout);
        unlockPopWindow.setFocusable(true);// 加上这个popupwindow中才可以接收点击事件
        passwordLayout.setOnClickListener(this);
        passwordLayout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        unlockPopWindow.setWidth(passwordLayout.getMeasuredWidth());
        unlockPopWindow.setHeight((passwordLayout.getMeasuredHeight()));
        unlockPopWindow.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.top_bg_popup));// 设置背景图片，不能在布局中设置，要通过代码来设置
        unlockPopWindow.setOutsideTouchable(true);

        passwordEditText1 = (EditText) passwordLayout.findViewById(R.id.others_unlock_password_text_view_1);
        passwordEditText2 = (EditText) passwordLayout.findViewById(R.id.others_unlock_password_text_view_2);
        passwordEditText3 = (EditText) passwordLayout.findViewById(R.id.others_unlock_password_text_view_3);
        passwordEditText4 = (EditText) passwordLayout.findViewById(R.id.others_unlock_password_text_view_4);

        passwordEditText1.addTextChangedListener(new PasswordTextWatcher(passwordEditText1));
        passwordEditText2.addTextChangedListener(new PasswordTextWatcher(passwordEditText2));
        passwordEditText3.addTextChangedListener(new PasswordTextWatcher(passwordEditText3));
        passwordEditText4.addTextChangedListener(new PasswordTextWatcher(passwordEditText4));
    }*/

   /* private class PasswordTextWatcher implements TextWatcher {
        private EditText editText;

        public PasswordTextWatcher(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s != null && !s.equals("")) {
                if (editText == passwordEditText1) {
                    passwordEditText2.setFocusable(true);
                    passwordEditText2.setFocusableInTouchMode(true);
                    passwordEditText2.requestFocus();
                } else if (editText == passwordEditText2) {
                    passwordEditText3.setFocusable(true);
                    passwordEditText3.setFocusableInTouchMode(true);
                    passwordEditText3.requestFocus();
                } else if (editText == passwordEditText3) {
                    passwordEditText4.setFocusable(true);
                    passwordEditText4.setFocusableInTouchMode(true);
                    passwordEditText4.requestFocus();
                }
            }

            onPasswordInputTextChanged();
        }
    }*/

    /**
     * 初始化右上角点击弹窗
     */
    private void initMorePopupWindow() {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.others_more_popmenuview, null);
        reportLayout = (LinearLayout) layout.findViewById(R.id.others_report_layout);
        menuPopWindow = new PopupWindow(layout);
        menuPopWindow.setFocusable(true);// 加上这个popupwindow中才可以接收点击事件

        //添加点击事件
        reportLayout.setOnClickListener(this);
        // 控制popupwindow的宽度和高度自适应
        reportLayout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        menuPopWindow.setWidth(reportLayout.getMeasuredWidth());
        menuPopWindow.setHeight((reportLayout.getMeasuredHeight()) * 1);
        // 控制popupwindow点击屏幕其他地方消失
        menuPopWindow.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.top_bg_popup));// 设置背景图片，不能在布局中设置，要通过代码来设置
        menuPopWindow.setOutsideTouchable(true);// 触摸popupwindow外部，popupwindow消失。这个要求你的popupwindow要有背景图片才可以成功，如上

        //测量右上角图标的大小
        moreMenuImageView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
    }

    /**
     * 初始化界面
     */
    private void initView() {
        moreMenuImageView = (ImageView) findViewById(R.id.others_more_menu_image_view);
        guideImage= (ImageView) findViewById(R.id.guide_xinshou);
        moreMenuImageView.setOnClickListener(this);
      /*  lockedImageView = (ImageView) findViewById(R.id.others_unlock_image_view);
        lockedImageView.setOnClickListener(this);*/
        leftArrowImageView.setVisibility(View.VISIBLE);

        //设置默认图片
        int sex = MeiMengApplication.sharedPreferences.getInt(CommonConstants.USER_SEX, 0);
        ImageView missingImageView = (ImageView) findViewById(R.id.others_missing_image_view);
        missingImageView.setVisibility(View.VISIBLE);
        if (sex == 1) {
            missingImageView.setImageResource(R.drawable.meimeng_ico_user_missing);
        } else {
            missingImageView.setImageResource(R.drawable.meimeng_ico_user_missing_wo);
        }

        //初始化悬浮按钮：约
        ObservableScrollView scrollView = (ObservableScrollView) findViewById(R.id.others_scroll_view2);
        circle = (CircleWaveView) findViewById(R.id.others_matchmaker);
        circle.setMaxRadius(0.3f);
        fab = (ImageView) findViewById(R.id.others_matchmaker_1);
       // fab = (FloatingActionButton) findViewById(R.id.others_matchmaker);
        //fab.attachToScrollView(scrollView);
        //初始化右上角更多菜单
        initMorePopupWindow();
        //初始化解锁弹出窗口
        //initUnlockPopupWindow();
        //展示用户基本信息
        getPartInfo(targetUid);

        // 初始化标题栏
        titleText.setText("TA的资料");
        giveGiftLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addGiftDialogLayout();

            }
        });

        dm = Utils.getScreenMetrics(OthersSelfActivity.this);
        final float density = dm.density;
        photoLayout.post(new Runnable() {
            @Override
            public void run() {
                photoParams = (LinearLayout.LayoutParams) photoLayout.getLayoutParams();
                if (photoList.size() > 4) {
                    photoParams.height = (int) (201 * density);
                } else {
                    photoParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                }
                /**
                 * 防止自动滑动到GridView的底部
                 */
                topLayout.setFocusable(true);
                topLayout.setFocusableInTouchMode(true);
                topLayout.requestFocus();
            }
        });

        sp=MeiMengApplication.sharedPreferences;
        isFirstOthers=sp.getBoolean("geuide_others",false);
        setMyselfGuide();

    }


    private void setMyselfGuide() {
        if (isFirstOthers == false) {
            flag=true;
            guideImage.setVisibility(View.VISIBLE);
            Utils.readBitMap(OthersSelfActivity.this, guideImage, R.raw.guide_like);
            guideImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (flag == true) {
                        Utils.readBitMap(OthersSelfActivity.this,guideImage,R.raw.guide_chat);
                        flag = false;
                    } else {
                        guideImage.setVisibility(View.GONE);
                        isFirstOthers = true;
                        sp.edit().putBoolean("geuide_others", true).commit();
                    }
                }
            });

            guideImage.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // TODO Auto-generated method stub
                    if (!(event.getX() > (5 * dm.widthPixels / 18)
                            && event.getX() < (5 * dm.widthPixels / 9)
                            && event.getY() > (65 * dm.heightPixels /128)
                            && event.getY()<(72*dm.heightPixels/128))){
                        return true;
                    }
                    return false;
                }
            });
        }

    }
   /* *//**
     * 设置giftGridView水平滚动
     *//*
    private void setHorizontalGridView() {
        final float density = dm.density;
        int size = giftList.size();
        int width = 40;
        int horizontalSpacing = 20;

        int allWidth = (int) (horizontalSpacing * (size - 1) * density + width * size * density);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                allWidth, LinearLayout.LayoutParams.MATCH_PARENT);
        *//*giftGridView.setLayoutParams(params);
        giftGridView.setColumnWidth(width);
        giftGridView.setHorizontalSpacing(horizontalSpacing);
        giftGridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        giftGridView.setNumColumns(size);
*//*
    }*/

    /**
     * 赠送礼物
     */
    private void addGiftDialogLayout() {
        giveGiftFragment = new GiveGiftFragment();
        dialogLayout.setVisibility(View.VISIBLE);

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.anim.bottom_in, 0);
        transaction.add(R.id.dialog_layout, giveGiftFragment);
        transaction.commit();
    }

/*
    @OnClick(R.id.others_follow_image_button)
    void onFollowClick() {
        followPeople();
    }*/

    @OnClick(R.id.others_vote_image_button)
    void onVoteClick() {
        //获取用户的uid,和token
        if (TextUtils.isEmpty(Utils.getUserId())) {
            return;
        }
        if (TextUtils.isEmpty(Utils.getUserToken())) {
            return;
        }
        String reportUrl = InternetConstant.SERVER_URL + InternetConstant.USER_PHOTO_URL + "?uid=" + String.valueOf(Utils.getUserId()) + "&token=" + Utils.getUserToken();
        String reqBody = null;
        try {
            reqBody = BuildString.VoteStoryReqBody(voteByuid);
            Observable observable = InternetUtils.getJsonObservale(reportUrl, reqBody);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            BaseBean baseBean = GsonTools.getBaseReqBean(s);
                            if (baseBean.isSuccess()) {
                                commonLikeStarTextView.setText(String.valueOf(Integer.parseInt((String) commonLikeStarTextView.getText()) + 1));
                                hasvoted = true;
                                voteButton.setImageResource(R.drawable.icon_have_praise);
                                voteButton.setEnabled(false);
                                Toast.makeText(OthersSelfActivity.this, "已赞", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(OthersSelfActivity.this, "已经点赞", Toast.LENGTH_SHORT).show();
                                DialogUtils.setDialog(OthersSelfActivity.this, baseBean.getError());
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable error) {
                            Log.e("test:", error.getMessage());
                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.others_matchmaker)
    void onMatchMakerClick() {
        if (isfollow) {//已经喜欢
            //如果已经喜欢调用接口判断是否相互喜欢、是否有聊天机会
            if (isFollowing==false) {//喜欢别人
                likeToOthers();
            } else {//互相喜欢
                MessageUtils.setLeanCloudSelfUid();
                MessageUtils.setOtherLeanCloudOtherUid(OthersSelfActivity.this,otherUserId);
            }
        } else {//没有喜欢
            followPeople();
        }
    }

    private void likeToOthers() {
        //获取用户的uid,和token
        if (TextUtils.isEmpty(Utils.getUserId())) {
            return;
        }
        if (TextUtils.isEmpty(Utils.getUserToken())) {
            return;
        }
        String reportUrl = InternetConstant.SERVER_URL + InternetConstant.MATCHMAKER_GET + "?uid=" + String.valueOf(Utils.getUserId()) + "&token=" + Utils.getUserToken();
        try {
            JSONStringer jsonStringer = new JSONStringer().object()
                    .key("uid").value(targetUid)
                    .endObject();
            String jsonStr = jsonStringer.toString();
            Observable observable = InternetUtils.getJsonObservale(reportUrl, jsonStr);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            MatchMakerGetBean baseBean = GsonTools.getMatchMakerGetBean(s);
                            if (baseBean.isSuccess()) {
                                DialogUtils.matchMakerDialog(OthersSelfActivity.this, baseBean);
                            } else {
                                DialogUtils.setDialog(OthersSelfActivity.this, baseBean.getError());
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable error) {
                            Log.e("test:", error.getMessage());
                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.others_title_left_arrow_image_view)
    void back() {
        onBackPressed();
    }


    /**
     * 获取上一个Activity返回的attentionId
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 & resultCode == 1) {
            if (data == null) {
                attentionId = 0;
            } else {
                attentionId = data.getExtras().getInt("attentionId");

            }
          /*  if (attentionId > 0)
                followPeople();*/
        }
    }

    /**
     * 获取TA的个人资料
     *
     * @param targetUid 目标用户的id
     */
    private void getPartInfo(final long targetUid) {
        try {
            //获取用户的uid,和token
            if (TextUtils.isEmpty(Utils.getUserId())) {
                return;
            }
            if (TextUtils.isEmpty(Utils.getUserToken())) {
                return;
            }
            String url = InternetConstant.SERVER_URL + InternetConstant.USER_GETPARTINFO + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();
            JSONStringer stringer = new JSONStringer().object().key("targetUid").value(targetUid).endObject();
            String jsonStr = stringer.toString();
            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1() {
                        @Override
                        public void call(Object o) {
                            if (null != dialog){
                                dialog.dismiss();
                            }
                            Log.d("whh", (String) o);
                            UserPartInfoBean baseInfoBean = GsonTools.getUserPartInfoParam((String) o);
                            if (baseInfoBean.getParam().getUserPartInfoItem().getGifts() != null) {
                                for (int i = 0; i < baseInfoBean.getParam().getUserPartInfoItem().getGifts().size(); i++) {
                                    gifts.add(baseInfoBean.getParam().getUserPartInfoItem().getGifts().get(i));
                                }
                                for (int i = 0; i < gifts.size(); i++) {
                                    View view = LayoutInflater.from(OthersSelfActivity.this).inflate(R.layout.item_of_gift_grid, null);
                                    TextView tv = (TextView) view.findViewById(R.id.gift_name);
                                    ImageView iv = (ImageView) view.findViewById(R.id.gift_picture);
                                    setAtrr(tv, iv, gifts.get(i));
                                    gift_others.addView(view);
                                }
                            }
                            // MeiMengApplication.sharedPreferences.edit().putInt(CommonConstants.USER_SEX,baseInfoBean.getParam().getUserPartInfoItem().getSex());
                            if (baseInfoBean.isSuccess()) {
                                initWedget(baseInfoBean);
                            } else {
                                DialogUtils.setDialog(OthersSelfActivity.this, baseInfoBean.getError());
                                Toast.makeText(OthersSelfActivity.this, "获取用户基本信息失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            Log.e("获取用户基本信息失败", throwable.getMessage());
                            Toast.makeText(OthersSelfActivity.this, "获取用户基本信息失败", Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(OthersSelfActivity.this, "获取用户基本信息失败", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 初始化界面上他人礼物
     *
     * @param textView  礼物名称
     * @param imageView 礼物图片
     */
    private void setAtrr(TextView textView, ImageView imageView, ReceivedGiftItem item) {
        switch ((int) item.getGiftId()) {
            case 1:
                resetViewHolder(textView, imageView, R.drawable.g_4441, "巧克力×" + item.getGiftCount());
                break;
            case 2:
                resetViewHolder(textView, imageView, R.drawable.g_4442, "玫瑰花×" + item.getGiftCount());
                break;
            case 4:
                resetViewHolder(textView, imageView, R.drawable.g_4443, "手表×" + item.getGiftCount());
                break;
            case 5:
                resetViewHolder(textView, imageView, R.drawable.g_4444, "香水×" + item.getGiftCount());
                break;
            case 7:
                resetViewHolder(textView, imageView, R.drawable.g_4445, "燕尾服×" + item.getGiftCount());
                break;
            case 8:
                resetViewHolder(textView, imageView, R.drawable.g_4446, "晚礼服×" + item.getGiftCount());
                break;
            case 10:
                resetViewHolder(textView, imageView, R.drawable.g_4447, "跑车×" + item.getGiftCount());
                break;
            case 11:
                resetViewHolder(textView, imageView, R.drawable.g_4448, "项链×" + item.getGiftCount());
                break;
            default:
                break;
        }
    }

    private void resetViewHolder(TextView textView, ImageView imageView, int img, String name) {
        textView.setText(name);
        imageView.setImageResource(img);
    }


    /**
     * 初始化界面上半部分的界面
     *
     * @param baseInfoBean 用户基本信息
     */
    private void initWedget(UserPartInfoBean baseInfoBean) {
        infoItem = baseInfoBean.getParam().getUserPartInfoItem();

        //展示照片
        if (infoItem.getMyPhotos().size() > 0) {
            ImageView missingImageView = (ImageView) findViewById(R.id.others_missing_image_view);
            missingImageView.setVisibility(View.GONE);
        }

        mAdapter = new OtherPhotosFragmentAdapter(getSupportFragmentManager(), infoItem.getMyPhotos());
        mPager = (MyViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
        mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);
/*
        //图片加锁
        if (infoItem.getMyPhotos().size() > 0) {
            int lock = infoItem.getPhotoLock();
            if (lock == 1) {
                lockedImageView.setVisibility(View.VISIBLE);
                //图片不可滑动
                mPager.setNoScroll(true);
                //图片模糊
                //BlurImages.blur()
            } else {
                lockedImageView.setVisibility(View.GONE);
                mPager.setNoScroll(false);
            }
        }*/

        //点赞和喜欢、是否互相喜欢的状态设置
        isfollow = infoItem.isHasFollow();
        hasvoted = infoItem.isHasVote();
        isFollowing=infoItem.isFollowing();
        if (hasvoted) {
            voteButton.setEnabled(false);
            voteButton.setImageResource(R.drawable.icon_have_praise);
        } else {
            voteButton.setEnabled(true);
            voteButton.setImageResource(R.drawable.icon_praise);
        }

        if (isfollow) {
            fab.setImageResource(R.drawable.icon_have_date);
            fab.setVisibility(View.VISIBLE);
        } else {
            fab.setImageResource(R.drawable.icon_date);
            fab.setVisibility(View.VISIBLE);
        }
        circle.setVisibility(View.VISIBLE);

       /* int sex = baseInfoBean.getParam().getUserPartInfoItem().getSex();
        commonNameTextView.setText(infoItem.getFirstName() + (sex == 0 ? "先生" : "女士"));
        commonIdTextView.setText("ID:" + infoItem.getUid());
        commonAgeTextView.setText(infoItem.getAge() + "岁");
        commonHeightTextView.setText(infoItem.getHeight() + "CM");
        commonLocationTextView.setText(infoItem.getResidenceValue());*/
        otherUserId=infoItem.getUid();
        voteByuid = infoItem.getUid();

        setVipLevel(infoItem.getVipLevel());

        String firstName = infoItem.getFirstName();
        int sexNum = infoItem.getSex();
        if (firstName == null) {
            if (sexNum == 0) {
                userName = "先生";
            } else {
                userName = "女士";
            }
        } else {
            if (sexNum == 0) {
                userName = firstName + "先生";
            } else {
                userName = firstName + "女士";
            }
        }
        commonNameTextView.setText(userName);
        commonIdTextView.setText("ID: " + infoItem.getUid());
        if (infoItem.getAge() == 0) {
            commonAgeTextView.setText("未知");
        }else{
            commonAgeTextView.setText(infoItem.getAge() + "岁");
        }
        if (infoItem.getHeight() == 0) {
            commonHeightTextView.setText("未知");
        }else{
            commonHeightTextView.setText(infoItem.getHeight() + "CM");
        }
        commonLocationTextView.setText((infoItem.getResidenceValue() == null || infoItem.getResidenceValue().equals("")) ? "未知" : infoItem.getResidenceValue());
        /*if (!isfollow) {
            commonLikeStarImageView.setImageResource(R.drawable.like_ico);
        } else {
            commonLikeStarImageView.setImageResource(R.drawable.like_ico_haslike);
        }*/

        commonLikeStarTextView.setText(String.valueOf(infoItem.getFollowerNum()));

      /*  //我的礼物
        //giftList = receivedGiftListBean.getParam().getMyRevGift();
        giftAdapter = new GiftAdapter(OthersSelfActivity.this, gifts);
        giftGridView.setAdapter(giftAdapter);
        setHorizontalGridView();*/

        //认证信息
        if (infoItem.getIdeStatus() == 1) {
            user_identify_image.setImageResource(R.drawable.user_ico);
            useridentifytext.setTextColor(getResources().getColor(R.color.gold_textcolor));
        }
        if (infoItem.getMarStatus() == 1) {
            marriage_identify_image.setImageResource(R.drawable.marriage_ico);
            marrigeidentifytext.setTextColor(getResources().getColor(R.color.gold_textcolor));
        }
        if (infoItem.getEduStatus() == 1) {
            education_identify_image.setImageResource(R.drawable.education_ico);
            educationidentifytext.setTextColor(getResources().getColor(R.color.gold_textcolor));
        }
        if (infoItem.getPropertyStatus() == 1) {
            money_identify_image.setImageResource(R.drawable.money_ico);
            moneyidentifytext.setTextColor(getResources().getColor(R.color.gold_textcolor));
        }
        if (infoItem.getJobStatus() == 1) {
            work_identify_image.setImageResource(R.drawable.work_ico);
            workidentifytext.setTextColor(getResources().getColor(R.color.gold_textcolor));
        }

        //基本信息
        if (infoItem.getWeight() == 0) {
            weightTextView.setText("");
        } else {
            weightTextView.setText(infoItem.getWeight() + "kg");
        }
        chineseZodiacTextView.setText(infoItem.getZodiacValue());
        zodiacTextView.setText(infoItem.getConstellationValue());
        nationTextView.setText(infoItem.getNationValue());
        marryTextView.setText(infoItem.getMaritalStatusValue());
        //教育工作
        educationTextView.setText(infoItem.getEducationValue());
        schoolTextView.setText(infoItem.getGraduateCollege());
        tradesTextView.setText(infoItem.getCompanyIndustryValue());
        jobTextView.setText(infoItem.getCompanyPositionValue());
        overseasTextView.setText(infoItem.getOutsideExperienceValue());
        //经济情况
        yearlySalaryTextView.setText(infoItem.getYearIncomeValue());
        assetsTextView.setText(infoItem.getPropertyValue());
        familyBackgroundTextView.setText(infoItem.getFamilyBackgroundValue());
        houseTextView.setText(infoItem.getHouseStateValue());
        carTextView.setText(infoItem.getCarStateValue());

        //兴趣爱好
        initInterest((ArrayList<String>) infoItem.getLstLoveExercise(), sportLayout, R.layout.interest_sport_item, R.id.interest_sport_item_text_view);
        initInterest((ArrayList<String>) infoItem.getLstLoveMusic(), musicLayout, R.layout.interest_music_item, R.id.interest_music_item_text_view);
        initInterest((ArrayList<String>) infoItem.getLstLoveFood(), foodLayout, R.layout.interest_food_item, R.id.interest_food_item_text_view);
        initInterest((ArrayList<String>) infoItem.getLstLoveFilm(), movieLayout, R.layout.interest_movie_item, R.id.interest_movie_item_text_view);
        initInterest((ArrayList<String>) infoItem.getLstLoveBook(), bookLayout, R.layout.interest_book_item, R.id.interest_book_item_text_view);
        initInterest((ArrayList<String>) infoItem.getLstLoveTouristDestination(), travelLayout, R.layout.interest_travel_item, R.id.interest_travel_item_text_view);

        if (infoItem.getUserStory() == null || infoItem.getUserStory().equals("")) {
            storyTextView.setText("未填写");
        } else {
            storyTextView.setText(infoItem.getUserStory());
        }
    }

    private void initInterest(ArrayList<String> items, FlowLayout rootLayout, int itemId, int textViewId) {
        rootLayout.removeAllViews();
        if (items == null)
            return;

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);

        for (int i = 0; i < items.size(); i++) {
            LinearLayout layout = (LinearLayout) inflater.inflate(itemId, null);
            TextView textView = (TextView) layout.findViewById(textViewId);
            textView.setText(items.get(i));
            rootLayout.addView(layout);
        }
    }

    /**
     * 设置照片
     */
    private void setPhotos() {

    }

    /**
     * 设置会员等级图标
     *
     * @param level 等级
     */
    private void setVipLevel(int level) {
        switch (level) {
            case 0:
                commonVipLvlTextView.setText("普通会籍");
                commonVipLvlImageView.setImageDrawable(getResources().getDrawable(R.drawable.vip_0));
                break;
            case 1:
                commonVipLvlTextView.setText("银牌会籍");
                commonVipLvlImageView.setImageDrawable(getResources().getDrawable(R.drawable.vip_1));
                break;
            case 2:
                commonVipLvlTextView.setText("金牌会籍");
                commonVipLvlImageView.setImageDrawable(getResources().getDrawable(R.drawable.vip_2));
                break;
            case 3:
                commonVipLvlTextView.setText("黑牌会籍");
                commonVipLvlImageView.setImageDrawable(getResources().getDrawable(R.drawable.vip_3));
                break;

            default:
                break;
        }
    }
/*
    private void unLockPhoto(String password) {
        //获取用户的uid,和token
        if (TextUtils.isEmpty(Utils.getUserId())) {
            return;
        }
        if (TextUtils.isEmpty(Utils.getUserToken())) {
            return;
        }
        String url = InternetConstant.SERVER_URL + InternetConstant.LOOK_PHOTO + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();
        //构造url和json格式的请求参数
        try {
            JSONStringer jsonStringer = new JSONStringer().object()
                    .key("targetUid").value(targetUid)
                    .key("photoPsw").value(password)
                    .endObject();
            String jsonStr = jsonStringer.toString();
            //得到Observable并获取返回的数据(主线程中)
            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            followBean = GsonTools.getFollowJson(s);
                            if (followBean.isSuccess()) {
                                Toast.makeText(getApplicationContext(), "解锁成功", Toast.LENGTH_SHORT).show();
                                unlockPopWindow.dismiss();
                                mPager.setNoScroll(false);
                                lockedImageView.setVisibility(View.GONE);
                                //initView();
                            } else {
                                Toast.makeText(getApplicationContext(), "解锁失败", Toast.LENGTH_SHORT).show();
                                DialogUtils.setDialog(OthersSelfActivity.this, followBean.getError());
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
    }*/

    private boolean isFollowing;//喜欢状态

    /**
     * 喜欢他人
     */
    private void followPeople() {
        if (!isfollow) {
            //关注该用户
            //获取用户的uid,和token
            if (TextUtils.isEmpty(Utils.getUserId())) {
                return;
            }
            if (TextUtils.isEmpty(Utils.getUserToken())) {
                return;
            }
            String url = InternetConstant.SERVER_URL + InternetConstant.PEOPLE_DOFOLLOW + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();
            //构造url和json格式的请求参数
            try {
                JSONStringer jsonStringer = new JSONStringer().object()
                        .key("targetUid").value(targetUid)
                        .endObject();
                String jsonStr = jsonStringer.toString();
                //得到Observable并获取返回的数据(主线程中)
                Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
                observable.observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<String>() {
                            @Override
                            public void call(String s) {
                                followBean = GsonTools.getFollowJson(s);
                                if (followBean.isSuccess()) {
                                    fab.setImageResource(R.drawable.icon_have_date);
                                    commonLikeStarTextView.setText(String.valueOf(Integer.parseInt((String) commonLikeStarTextView.getText()) + 1));
                                    isfollow = true;
                                    if (followBean.param.getIsFollowing() == 0) {
                                        Toast.makeText(getApplicationContext(), "已成功喜欢", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "已互相喜欢", Toast.LENGTH_SHORT).show();
                                        setChatMessage();
                                    }
                                } else {
                                    DialogUtils.setDialog(OthersSelfActivity.this, followBean.getError());
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

    private void setChatMessage() {
        sq=new ChatSQliteDataUtil(OthersSelfActivity.this);
        final ChatManager chatManager = ChatManager.getInstance();
        Map attrs=new HashMap();
        attrs.put("isOpen", false);
        chatManager.fetchConversationWithUserId( String.valueOf(otherUserId),attrs, new AVIMConversationCreatedCallback() {
            @Override
            public void done(final AVIMConversation avimConversation, AVIMException e) {

                AVIMTextMessage msg = new AVIMTextMessage();
                msg.setText("我们已经是好友了，赶快来聊天吧~");
                uploadToServer(avimConversation.getConversationId());//更新到服务器
                setDataTodb(avimConversation.getConversationId());//添加到本地
                avimConversation.sendMessage(msg, new AVIMConversationCallback() {
                    @Override
                    public void done(AVIMException e) {
                    }
                });

            }

        });
    }

    private void setDataTodb(String conversationId) {

        ChatMessageBean chatMessageBean = new ChatMessageBean();
        chatMessageBean.setHeadPic(infoItem.getHeadPic());

        chatMessageBean.setName(userName);

        chatMessageBean.setUserId(Long.valueOf(targetUid));

        chatMessageBean.setContent("我们已经是好友了，赶快来聊天吧~");

        Long time = System.currentTimeMillis(); //接收到消息时的时间（毫秒值）
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
        String timestr = format.format(date);
        chatMessageBean.setTime(timestr);
        chatMessageBean.setConversationId(conversationId);
        Map attrs=new HashMap();
        attrs.put("isOpen",false);
        chatMessageBean.setAttributes(attrs);
        sq.addData(chatMessageBean);
    }

    private void uploadToServer(String conversationId) {

        if (TextUtils.isEmpty(Utils.getUserId())) {
            return;
        }
        if (TextUtils.isEmpty(Utils.getUserToken())) {
            return;
        }
        String url = InternetConstant.SERVER_URL + InternetConstant.UPTOSERVER + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();
        //构造url和json格式的请求参数
        try {
            JSONStringer jsonStringer = new JSONStringer().object()
                    .key("uid").value(Utils.getUserId())
                    .key("conversation").value(conversationId)
                    .endObject();
            String jsonStr = jsonStringer.toString();
            //得到Observable并获取返回的数据(主线程中)
            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            DeleteInfoBean deleteInfoBean= GsonTools.getDeleteBean(s);
                            if (deleteInfoBean.isSuccess()) {
                                //Toast.makeText(OthersSelfActivity.this, "成功", Toast.LENGTH_SHORT).show();
                            }else{
                                if (deleteInfoBean.getError().equals("NO-PARAMTER")) {
                                   // Toast.makeText(OthersSelfActivity.this, deleteInfoBean.getError(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable error) {
                            //Log.e("test:", error.getMessage());
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 举报用户
     *
     * @throws JSONException
     */
    //@OnClick(R.id.report_layout)
    void reportUser() {
        if (reportFragment == null) {
            reportFragment = new ReportFragment();
        }
        dialogLayout.setVisibility(View.VISIBLE);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.bottom_in, 0);
        transaction.add(R.id.dialog_layout, reportFragment);
        transaction.commit();
    }


    /**
     * 增送礼物
     */
    @Override
    public void giveListener(int giftPicId, long goodId, String name, String price) {

        MeiMengApplication.weixinPayCallBack = 0;
        Intent intent = new Intent(OthersSelfActivity.this, PayActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("price", price);
        intent.putExtra("goodId", goodId);
        intent.putExtra("targetUid", targetUid);
        intent.putExtra("pay_type", 2);
        startActivity(intent);
        cancelGiftDialog();
    }

    /**
     * 取消弹出框
     */
    private void cancelGiftDialog() {
        if (null != giveGiftFragment && giveGiftFragment.isAdded()) {
            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.remove(giveGiftFragment);
            transaction.commit();

        } else if (null != reportFragment && reportFragment.isAdded()) {

            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.remove(reportFragment);
            transaction.commit();
        }
        dialogLayout.setVisibility(View.GONE);

    }

    /**
     * 聊天按钮
     */
    @OnClick(R.id.message_layout)
    void chatListener() {
        MessageUtils.setLeanCloudSelfUid();
        MessageUtils.setLeanCloudOtherUid(OthersSelfActivity.this, targetUid);
    }

    @Override
    public void onBackPressed() {
        if (null != giveGiftFragment && giveGiftFragment.isAdded()) {
            cancelGiftDialog();
        } else if (null != reportFragment && reportFragment.isAdded()) {
            cancelGiftDialog();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void cancel() {
        FragmentManager fragmentManager = this.getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.remove(reportFragment);
        transaction.commit();

        dialogLayout.setVisibility(View.GONE);
    }

    /**
     * 举报按钮
     *
     * @param type
     */
    @Override
    public void Report(int type) {
        if (TextUtils.isEmpty(Utils.getUserId())) {
            return;
        }
        if (TextUtils.isEmpty(Utils.getUserToken())) {
            return;
        }
        //这里调用举报接口
        String url = InternetConstant.SERVER_URL + InternetConstant.REPORT_NEW_URL + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();
        try {
            JSONStringer jsonStringer = new JSONStringer().object().key("reportUid").value(targetUid).key("type").value(type).endObject();
            String jsonStr = jsonStringer.toString();
            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            BaseBean baseBean = GsonTools.getBaseReqBean(s);
                            if (!baseBean.isSuccess()) {
                                DialogUtils.setDialog(OthersSelfActivity.this, baseBean.getError());
                            } else {
                                Toast.makeText(OthersSelfActivity.this, "已经成功举报该用户！", Toast.LENGTH_SHORT).show();
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
        cancelGiftDialog();


    }


}


