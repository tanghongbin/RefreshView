package com.example.com.meimeng.activity;

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
import com.example.com.meimeng.fragment.PayDialogFragment;
import com.example.com.meimeng.gson.GsonTools;
import com.example.com.meimeng.gson.gsonbean.BaseBean;
import com.example.com.meimeng.gson.gsonbean.EventDetail;
import com.example.com.meimeng.net.BuildString;
import com.example.com.meimeng.net.InternetConstant;
import com.example.com.meimeng.net.InternetUtils;
import com.example.com.meimeng.util.DialogUtils;
import com.example.com.meimeng.util.Utils;

import org.json.JSONStringer;

import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by Administrator on 2015/7/14.
 */
public class SixPersonDateDetail extends BaseActivity implements View.OnClickListener {
    //活动ID
    private long mActivityId;

    //活动名称
    private String name = "";

    //价格
    private double price;

    private final int type = 3;

//    @Bind(R.id.event_detail_title_text)
//    TextView titleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.office_event_detail_final);
        ButterKnife.bind(this);

        MeiMengApplication.currentContext=this;
//        titleText.setText("跨界合作");
        Intent intent = getIntent();
        mActivityId = intent.getExtras().getLong("mActivityId");
        init();

    }

    void init() {

        final ImageView post = (ImageView) findViewById(R.id.office_event_detail_image);
        final TextView title = (TextView) findViewById(R.id.office_event_detail_title);
//        final TextView state = (TextView) findViewById(R.id.office_event_detail_state);
        final TextView place = (TextView) findViewById(R.id.office_event_detail_text_location);
        final TextView time = (TextView) findViewById(R.id.office_detail_date);
//        final TextView describe = (TextView) findViewById(R.id.event_detail_describe);
        final TextView cost = (TextView) findViewById(R.id.office_detail_cost_text);
        final TextView allNum = (TextView) findViewById(R.id.office_detail_num);
//        final TextView requirment = (TextView) findViewById(R.id.event_detail_requirment);
        ImageView backBtn = (ImageView) findViewById(R.id.back_icon);

        MeiMengApplication.attend = (Button) findViewById(R.id.office_detail_attend_button);
        MeiMengApplication.attend.setOnClickListener(this);
        backBtn.setOnClickListener(this);

        try {
            //获取用户的uid,和token
            if (TextUtils.isEmpty(Utils.getUserId())) {
                return;
            }
            if (TextUtils.isEmpty(Utils.getUserToken())) {
                return;
            }
            String url = BuildString.EventDetailUrl(Utils.getUserId(), Utils.getUserToken());
            String jsonStr = BuildString.EventDetailReqBody(mActivityId);//测试用

            //得到Observale并获取返回的数据(主线程中)
            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            EventDetail eventDetail = GsonTools.getEventDetailJson(s);
                            //活动名称
                            name = eventDetail.getTitle();
                            title.setText(name);
                            place.setText(eventDetail.getPlace() + " " + eventDetail.getPlaceDetail());
                            time.setText(Utils.getDisplayDate(eventDetail.getStartTime()));
                            allNum.setText(String.valueOf(eventDetail.getApplyAllCount()));
                            //价格
                            price = eventDetail.getPrice();
                            if (String.valueOf(price).equals("0.0")) {
                                cost.setText("免费");
                            } else {
                                cost.setText(String.valueOf(price));
                            }

                            String describetext = eventDetail.getDescribe();
                            String requirementtext = eventDetail.getRequirement();
                            if (describetext == null) {
                                describetext = "  ";
                            }
                            if (requirementtext == null) {
                                requirementtext = "  ";
                            }
//                            describe.setText(describetext);
//                            requirment.setText(requirementtext);

                            switch (eventDetail.getState()) {
                                //报名中
                                case 1:
//                                    state.setText("报名中");
//                                    state.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_4_shape));
                                    if (eventDetail.isHasApply()) {
                                        MeiMengApplication.attend.setText("已参加");
                                        MeiMengApplication.attend.setClickable(false);
                                    }
                                    break;
                                //即将开始
                                case 2:
//                                    state.setText("即将开始");
//                                    state.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_4_shape));
                                    MeiMengApplication.attend.setText("报名截止");
                                    MeiMengApplication.attend.setClickable(false);
                                    break;
                                //进行中
                                case 3:
//                                    state.setText("进行中");
//                                    state.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_on_shape));
                                    MeiMengApplication.attend.setText("报名截止");
                                    MeiMengApplication.attend.setClickable(false);
                                    break;
                                //过期
                                case 4:
//                                    state.setText("活动结束");
//                                    state.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_end_shape));
                                    MeiMengApplication.attend.setText("报名截止");
                                    MeiMengApplication.attend.setClickable(false);
                                    break;
                                default:
                                    break;
                            }

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
            case R.id.office_detail_attend_button:
                //点击参加活动，如果免费，直接调接口参加，不免费就调支付页面
                if (price == 0.0) {
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

                                            MeiMengApplication.attend.setText("已参加");
                                            MeiMengApplication.attend.setBackgroundResource(R.color.hasjoin);
                                            MeiMengApplication.attend.setClickable(false);
                                            Toast.makeText(getApplicationContext(), "参加活动成功", Toast.LENGTH_SHORT).show();

                                        } else {
                                            DialogUtils.setDialog(SixPersonDateDetail.this, baseBean.getError());
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
                    //支付页面
                    PayDialogFragment dialog = new PayDialogFragment(mActivityId, price, name, 4);
                    dialog.show(getFragmentManager(), null);
                }
                break;
        }
    }
}
