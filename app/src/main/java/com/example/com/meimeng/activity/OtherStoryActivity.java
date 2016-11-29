package com.example.com.meimeng.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.com.meimeng.MeiMengApplication;
import com.example.com.meimeng.R;
import com.example.com.meimeng.adapter.StoryListAdapter;
import com.example.com.meimeng.bean.StoryBean;
import com.example.com.meimeng.custom.CircleImageView;
import com.example.com.meimeng.custom.ScrollViewWithListView;
import com.example.com.meimeng.fragment.ReportFragment;
import com.example.com.meimeng.gson.GsonTools;
import com.example.com.meimeng.gson.gsonbean.BaseBean;
import com.example.com.meimeng.gson.gsonbean.FollowBean;
import com.example.com.meimeng.gson.gsonbean.MatchMakerBean;
import com.example.com.meimeng.gson.gsonbean.UserStoryDetailBean;
import com.example.com.meimeng.gson.gsonbean.UserStoryDetailItem;
import com.example.com.meimeng.net.BuildString;
import com.example.com.meimeng.net.InternetConstant;
import com.example.com.meimeng.net.InternetUtils;
import com.example.com.meimeng.util.DialogUtils;
import com.example.com.meimeng.util.Utils;

import org.json.JSONException;
import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by wjw
 * 他人用户故事界面
 */
public class OtherStoryActivity extends BaseActivity implements ReportFragment.ReportDialog {
    private static final String TAG = "OtherStoryActivity";
    @Bind(R.id.like_story)
    TableRow tableRow;//为该故事点赞的tabRow

    @Bind(R.id.like_story_image_view)
    ImageView vote_imageView;

    @Bind(R.id.like_story_text)
    TextView likeStoryText;//点赞数

    @Bind(R.id.bow_arrow_image_view)
    ImageView imageView;//右上角图标

    @Bind(R.id.title_left_arrow_image_view)
    ImageView leftArrowImageView;//返回图标

    @Bind(R.id.title_text)
    TextView titleText;//title_bar中间文字

    @Bind(R.id.other_story_user_info_layout)
    RelativeLayout otherInfoLayout;//除了故事之外的个人信息界面

    @Bind(R.id.other_story_cover_image_view)
    ImageView other_story_cover_image_view;//故事图片

    @Bind(R.id.other_story_list_view)
    ScrollViewWithListView otherStoryListView;//故事列表

    @Bind(R.id.like_people_layout_story)
    LinearLayout like_people_layout;//中意该用户的布局

    @Bind(R.id.like_people_imageView)
    ImageView like_star_imageView;//中意该用户的图标

    @Bind(R.id.other_header_image_view)
    CircleImageView head_pic_image_view;//用户头像c

    @Bind(R.id.nickname_textView_story)
    TextView nick_name_text;//用户昵称

    @Bind(R.id.city_text_view_story)
    TextView city_text;//城市名

    @Bind(R.id.lastlogintime_text_view_story)
    TextView lastlogintime_text;//在线时间

    @Bind(R.id.main_dialog_layout)
    LinearLayout dialogLayout;

    private ArrayList<StoryBean> storyBeans = new ArrayList<>();
    private StoryListAdapter storyListAdapter;

    private long targetUid;
    private boolean isfollow = false;
    private boolean hasvoted = false;
    private ReportFragment reportFragment;
    private int attentionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.other_story);
        ButterKnife.bind(this);

        MeiMengApplication.currentContext=this;
        Intent intent = this.getIntent();
        targetUid = intent.getExtras().getLong("targetUid");
        //初始化界面
        initView();

    }

    /**
     * 初始化界面
     */
    private void initView() {

        leftArrowImageView.setVisibility(View.VISIBLE);

        titleText.setText("个人故事");
        imageView.setVisibility(View.GONE);

        storyListAdapter = new StoryListAdapter(OtherStoryActivity.this, storyBeans);
        otherStoryListView.setAdapter(storyListAdapter);
        addStoryData(targetUid);

        otherInfoLayout.setFocusable(true);
        otherInfoLayout.setFocusableInTouchMode(true);
        otherInfoLayout.requestFocus();

    }

    /**
     * 结束该activity
     */
    @OnClick(R.id.title_left_arrow_image_view)
    void back() {
        this.finish();
    }


    /**
     * 添加头像的点击事件
     */
    @OnClick(R.id.other_header_image_view)
    void gogogoimage_view() {
        gotoOthersSelfActivity();
    }

    /**
     * 添加昵称的点击事件
     */
    @OnClick(R.id.nickname_textView_story)
    void gogogo() {
        gotoOthersSelfActivity();
    }

    /**
     * 跳转到他人界面
     */
    private void gotoOthersSelfActivity() {
        Intent intent = new Intent(this, OthersSelfActivity.class);
        intent.putExtra("targetUid", targetUid);
        startActivity(intent);
    }

    /**
     * 红娘邀约
     */
    @OnClick(R.id.bottom_story_maker_layout)
    void makerLayoutListener() {
        try {
            //获取用户的uid,和token
            if (TextUtils.isEmpty(Utils.getUserId())) {
                return;
            }
            if (TextUtils.isEmpty(Utils.getUserToken())) {
                return;
            }
            String url = InternetConstant.SERVER_URL + InternetConstant.MATCHMAKER_DATE + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();
            JSONStringer jsonStringer = new JSONStringer().object()
                    .key("targetUid").value(targetUid).endObject();
            String jsonStr = jsonStringer.toString();
            //得到Observable并获取返回的数据(主线程中)
            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            MatchMakerBean matchmakerdateJson = GsonTools.getMatchMakerDateJson(s);
                         /*   if (MeiMengApplication.sharedPreferences.getInt(CommonConstants.USER_LEVEL, 0) == 3) {
                                if (matchmakerdateJson.isSuccess()) {
                                    if (matchmakerdateJson.getParam().getMatchmaker() == null) {
                                        Utils.connectMatchMakerDialog2(OtherStoryActivity.this);
                                    } else
                                        Utils.datatMatchMakerDialog(OtherStoryActivity.this, targetUid);

                                } else {
                                    DialogUtils.setDialog(OtherStoryActivity.this, matchmakerdateJson.getError());
                                }
                            } else {*/
                            if (matchmakerdateJson.isSuccess()) {
                                Utils.setDialog(OtherStoryActivity.this, targetUid, matchmakerdateJson.getParam().getRestChance());
                            } else {
                                Utils.confirmMatchMakerDialog2(OtherStoryActivity.this); //帮约中，请耐心等待
                            }
                        }

//                        }
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
     * 点赞按钮
     */
    @OnClick(R.id.like_story)
    void likeStoryClick() {
        if (!hasvoted) {
            //获取用户的uid,和token
            if (TextUtils.isEmpty(Utils.getUserId())) {
                return;
            }
            if (TextUtils.isEmpty(Utils.getUserToken())) {
                return;
            }
            String reportUrl = BuildString.VoteToUserStroryUrl(Utils.getUserId(), Utils.getUserToken());
            String reqBody = null;
            try {
                reqBody = BuildString.VoteStoryReqBody(targetUid);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Observable observable = InternetUtils.getJsonObservale(reportUrl, reqBody);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            BaseBean baseBean = GsonTools.getBaseReqBean(s);
                            if (baseBean.isSuccess()) {
                                likeStoryText.setText(String.valueOf(Integer.parseInt((String) likeStoryText.getText()) + 1));
                                Toast.makeText(OtherStoryActivity.this, "已赞", Toast.LENGTH_SHORT).show();
                                vote_imageView.setImageResource(R.drawable.ico_story_fav);
                                hasvoted = true;
                            } else {
                                DialogUtils.setDialog(OtherStoryActivity.this, baseBean.getError());
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable error) {
                            Log.e("test:", error.getMessage());
                        }
                    });
        } else {
            Toast.makeText(OtherStoryActivity.this, "请勿重复点赞", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 关注/取消关注该用户
     */
    @OnClick(R.id.like_people_layout_story)
    void followpeople() {
        if (!isfollow) {
            Intent intent = new Intent(OtherStoryActivity.this, MessageAttentionActivity.class);
            startActivityForResult(intent, 1);
        } else {
            followPeople(5);
        }


    }

    private void followPeople(int type) {

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
                        .key("contextId").value(type)
                        .endObject();
                String jsonStr = jsonStringer.toString();
                //得到Observable并获取返回的数据(主线程中)
                Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
                observable.observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<String>() {
                            @Override
                            public void call(String s) {
                                FollowBean followBean = GsonTools.getFollowJson(s);
                                if (followBean.isSuccess()) {
                                    Toast.makeText(getApplicationContext(), "关注成功", Toast.LENGTH_SHORT).show();
                                    like_star_imageView.setImageResource(R.drawable.like_ico);
                                    isfollow = true;
                                } else {
                                    DialogUtils.setDialog(OtherStoryActivity.this, followBean.getError());
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
        } else {
            //取消关注该用户
            //构造url和json格式的请求参数
            try {
                //获取用户的uid,和token
                if (TextUtils.isEmpty(Utils.getUserId())) {
                    return;
                }
                if (TextUtils.isEmpty(Utils.getUserToken())) {
                    return;
                }
                String url = InternetConstant.SERVER_URL + InternetConstant.PEOPLE_UNDOFOLLOW + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();
                JSONStringer jsonStringer = new JSONStringer().object().key("targetUid").value(targetUid).endObject();
                String jsonStr = jsonStringer.toString();
                //得到Observable并获取返回的数据(主线程中)
                Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
                observable.observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<String>() {
                            @Override
                            public void call(String s) {
                                FollowBean followBean = GsonTools.getFollowJson(s);
                                if (followBean.isSuccess()) {
                                    Toast.makeText(getApplicationContext(), "取消关注成功", Toast.LENGTH_SHORT).show();
                                    like_star_imageView.setImageResource(R.drawable.like_ico);
                                    isfollow = false;
                                } else {
                                    DialogUtils.setDialog(OtherStoryActivity.this, followBean.getError());
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

    /**
     * 获取上一个Activity返回的attentionId
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
            if (attentionId > 0)
                followPeople(attentionId);

        }
    }

    /**
     * 获取他人故事列表
     */
    private void addStoryData(final long targetUid) {
        try {
            //获取用户的uid,和token
            if (TextUtils.isEmpty(Utils.getUserId())) {
                return;
            }
            if (TextUtils.isEmpty(Utils.getUserToken())) {
                return;
            }
            String url = InternetConstant.SERVER_URL + InternetConstant.USER_STORY_DETAIL + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();
            JSONStringer stringer = new JSONStringer().object().key("targetUid").value(targetUid).endObject();
            String jsonStr = stringer.toString();

            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1() {
                        @Override
                        public void call(Object o) {
                            Log.i("time2", o.toString());
                            UserStoryDetailBean bean = GsonTools.getUserStoryDetailBean((String) o);
                            if (bean.isSuccess()) {
                                UserStoryDetailItem item = bean.getParam().getUserStory();
                                long pid = item.getPicId();
                                Log.e("picId", String.valueOf(pid));
                                InternetUtils.getPicIntoView(375, 375, other_story_cover_image_view, pid);
                                InternetUtils.getPicIntoView(200, 200, head_pic_image_view, item.getHeadPic(), true);
                                if (item.getNickname().length() > 3) {
                                    nick_name_text.setText(item.getNickname().substring(0, 3) + "...");
                                } else {
                                    nick_name_text.setText(item.getNickname());
                                }

                                hasvoted = item.isHasVote();
                                if (hasvoted) {
                                    vote_imageView.setImageResource(R.drawable.ico_story_fav);
                                } else {
                                    vote_imageView.setImageResource(R.drawable.ico_story_fav_pressed);
                                }
                                isfollow = item.isHasFollow();
                                if (!isfollow) {
                                    like_star_imageView.setImageResource(R.drawable.like_ico);
                                } else {
                                    like_star_imageView.setImageResource(R.drawable.like_ico);
                                }

                                city_text.setText(item.getCity());
                                lastlogintime_text.setText("| " + item.getLastLoginTime());

                                likeStoryText.setText(String.valueOf(item.getVoteNum()));
                                LinkedHashMap<String, String> storyMap = item.getMapStory();
                                for (Map.Entry<String, String> entry : storyMap.entrySet()) {
                                    StoryBean storyBean = new StoryBean();
                                    String key = entry.getKey();
                                    storyBean.setStoryType(key);
                                    storyBean.setContent(storyMap.get(key));
                                    storyBean.setType("other");
                                    storyBeans.add(storyBean);
                                }

                                storyListAdapter.notifyDataSetChanged();
                            } else {
                                DialogUtils.setDialog(OtherStoryActivity.this, bean.getError());
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            Log.e(TAG, throwable.getMessage());
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
    @OnClick(R.id.report_layout)
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
     * 取消弹出框
     */
    private void cancelGiftDialog() {
        if (null != reportFragment && reportFragment.isAdded()) {
            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.remove(reportFragment);
            transaction.commit();
        }
        dialogLayout.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {

        if (null != reportFragment && reportFragment.isAdded()) {
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
                                DialogUtils.setDialog(OtherStoryActivity.this, baseBean.getError());
                            } else {
                                Toast.makeText(OtherStoryActivity.this, "已经成功举报该用户！", Toast.LENGTH_SHORT).show();
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
