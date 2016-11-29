package com.example.com.meimeng.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.com.meimeng.MeiMengApplication;
import com.example.com.meimeng.R;
import com.example.com.meimeng.constants.CommonConstants;
import com.example.com.meimeng.gson.GsonTools;
import com.example.com.meimeng.gson.gsonbean.BaseBean;
import com.example.com.meimeng.gson.gsonbean.EventDetail;
import com.example.com.meimeng.net.BuildString;
import com.example.com.meimeng.net.InternetConstant;
import com.example.com.meimeng.net.InternetUtils;
import com.example.com.meimeng.util.DialogUtils;
import com.example.com.meimeng.util.Utils;

import org.json.JSONStringer;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by Administrator on 2015/7/14.
 */
public class PrivateEventDetail extends Activity implements View.OnClickListener {

    //价格
    private double price;

    private final int type = 2;

    private long mActivityId;

    //活动名称
    private String name = "";
    private long oUid;
    private Button attend;

    @Bind(R.id.event_detail_title_text)
    TextView titleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.private_event_detail_final);
        ButterKnife.bind(this);

        MeiMengApplication.currentContext=this;
        titleText.setText("私人邀约");
        Intent intent = getIntent();
        mActivityId = intent.getExtras().getLong("mActivityId");
        init();
    }

    void init() {

        final ImageView post = (ImageView) findViewById(R.id.private_event_item_image);
        final TextView title = (TextView) findViewById(R.id.private_event_item_title);
        final TextView state = (TextView) findViewById(R.id.private_event_item_state);
        final TextView place = (TextView) findViewById(R.id.private_detail_text_location);
        final TextView time = (TextView) findViewById(R.id.private_detail_date);
        final TextView allNum = (TextView) findViewById(R.id.private_detail_num);
        final TextView describe = (TextView) findViewById(R.id.private_detail_describe);
        final TextView cost = (TextView) findViewById(R.id.private_detail_cost_text);
        final ImageView icon = (ImageView) findViewById(R.id.private_detail_icon);
        final ImageView vipIcon = (ImageView) findViewById(R.id.private_detail_vip_icon);
        final TextView vipLevel = (TextView) findViewById(R.id.private_detail_vip_text);
        final TextView nickname = (TextView) findViewById(R.id.private_detail_nickname);

        attend = (Button) findViewById(R.id.private_detail_attend_button);
        ImageView backBtn = (ImageView) findViewById(R.id.back_icon);
        attend.setOnClickListener(this);
        backBtn.setOnClickListener(this);

        try {
            //获取用户的uid,和token
            if (TextUtils.isEmpty(Utils.getUserId())) {
                return;
            }
            if (TextUtils.isEmpty(Utils.getUserToken())) {
                return;
            }
            //构造url和json格式的请求参数
            String url = BuildString.EventDetailUrl(Utils.getUserId(), Utils.getUserToken());
            String jsonStr = BuildString.EventDetailReqBody(mActivityId);

            //得到Observale并获取返回的数据(主线程中)
            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            EventDetail eventDetail = GsonTools.getEventDetailJson(s);
                          /*  if(eventDetail.getTitle().length()>14){
                                title.setText(eventDetail.getTitle().substring(0,14)+"...");
                            }else{*/
                            title.setText(eventDetail.getTitle());
                           /* }*/

                            //活动名称
                            name = eventDetail.getTitle();

                            place.setText(eventDetail.getPlace() + " " + eventDetail.getPlaceDetail());
                            time.setText(Utils.getDisplayDate(eventDetail.getStartTime()));
                            allNum.setText(String.valueOf(eventDetail.getApplyAllCount()));
                            cost.setText("免费");

                            //价格
                            price = eventDetail.getPrice();

                            String describetext = eventDetail.getDescribe();
                            String requirementtext = eventDetail.getRequirement();
                            if (describetext == null) {
                                describetext = "  ";
                            }
                            if (requirementtext == null) {
                                requirementtext = "  ";
                            }
                            describe.setText(describetext + "\n" + requirementtext);
                            nickname.setText(eventDetail.getoNickname());
                            oUid = eventDetail.getoUserId();
                            if (oUid == MeiMengApplication.sharedPreferences.getLong(CommonConstants.USER_ID, -1L)) {
                                attend.setVisibility(View.GONE);
                            }
                            if (eventDetail.getoLevel() == 0) {
                                vipLevel.setText("普通会员");
                                vipIcon.setImageDrawable(getResources().getDrawable(R.drawable.vip_0));
                            } else if (eventDetail.getoLevel() == 1) {
                                vipLevel.setText("银牌会籍");
                                vipIcon.setImageDrawable(getResources().getDrawable(R.drawable.vip_1));
                            } else if (eventDetail.getoLevel() == 2) {
                                vipLevel.setText("金牌会籍");
                                vipIcon.setImageDrawable(getResources().getDrawable(R.drawable.vip_2));
                            } else if (eventDetail.getoLevel() == 3) {
                                vipLevel.setText("黑牌会籍");
                                vipIcon.setImageDrawable(getResources().getDrawable(R.drawable.vip_3));
                            } else {
                                vipIcon.setVisibility(View.GONE);
                                vipLevel.setVisibility(View.GONE);
                            }


                            switch (eventDetail.getState()) {
                                //报名中
                                case 1:
                                    state.setText("报名中");
                                    state.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_4_shape));
                                    if (eventDetail.isHasApply()) {
                                        attend.setText("已参加");
//                                        attend.setBackgroundResource(R.color.hasjoin);
                                        attend.setClickable(false);
                                    }
                                    break;
                                //即将开始
                                case 2:
                                    state.setText("即将开始");
                                    state.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_4_shape));

//                                    attend.setBackgroundResource(R.color.hasjoin);
//                                    Utils.eventtipsDialog(PrivateEventDetail.this, "活动还未开始，请耐心等待");
                                    attend.setText("报名截止");
                                    attend.setClickable(false);

                                    break;
                                //进行中
                                case 3:
                                    state.setText("进行中");
                                    state.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_on_shape));
//                                    attend.setBackgroundResource(R.color.hasjoin);
//                                    Utils.eventtipsDialog(PrivateEventDetail.this, "活动正在进行，报名结束");
                                    attend.setText("报名截止");
                                    attend.setClickable(false);
                                    break;
                                //过期
                                case 4:
                                    state.setText("活动结束");
                                    state.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_end_shape));
//                                    attend.setBackgroundResource(R.color.hasjoin);
//                                    Utils.eventtipsDialog(PrivateEventDetail.this, "活动已经结束，非常抱歉");
                                    attend.setText("报名截止");
                                    attend.setClickable(false);
                                    break;
                                default:
                                    break;
                            }
                            InternetUtils.getPicIntoView(200, 200, icon, eventDetail.getoHeadPic(), true);
                            InternetUtils.getPicIntoView(375, 200, post, eventDetail.getPic());
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_icon:
                finish();
                break;
            case R.id.private_detail_attend_button:
                if (TextUtils.isEmpty(Utils.getUserId())) {
                    return;
                }
                if (TextUtils.isEmpty(Utils.getUserToken())) {
                    return;
                }
                String url = InternetConstant.SERVER_URL + InternetConstant.ACTIVITY_DOAPPLY_URL + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();
                //构造url和json格式的请求参数
                try {
                    JSONStringer jsonStringer = new JSONStringer().object().key("activityId").value(mActivityId).endObject();
                    String jsonStr = jsonStringer.toString();
                    //得到Observable并获取返回的数据(主线程中)
                    Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
                    observable.observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Action1<String>() {
                                @Override
                                public void call(String s) {
                                    BaseBean baseBean = GsonTools.getBaseReqBean(s);
                                    if (baseBean.isSuccess()) {

                                        attend.setText("已参加");
                                        attend.setBackgroundResource(R.color.hasjoin);
                                        attend.setClickable(false);
                                        Toast.makeText(getApplicationContext(), "参加活动成功", Toast.LENGTH_SHORT).show();

                                    } else {
                                        DialogUtils.setDialog(PrivateEventDetail.this, baseBean.getError());
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

                break;
        }
    }

    @OnClick(R.id.person_information)
    public void toOther() {
        Intent intent = new Intent(PrivateEventDetail.this, OthersSelfActivity.class);
        intent.putExtra("targetUid", oUid);
        startActivity(intent);
    }

}
