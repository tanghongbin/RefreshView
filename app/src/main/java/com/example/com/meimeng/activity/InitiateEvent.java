package com.example.com.meimeng.activity;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.com.meimeng.MeiMengApplication;
import com.example.com.meimeng.R;
import com.example.com.meimeng.custom.CityPicker2;
import com.example.com.meimeng.custom.ThreeWheelView;
import com.example.com.meimeng.fragment.SelectPictureFragment;
import com.example.com.meimeng.gson.GsonTools;
import com.example.com.meimeng.gson.gsonbean.BaseBean;
import com.example.com.meimeng.net.InternetConstant;
import com.example.com.meimeng.net.InternetUtils;
import com.example.com.meimeng.util.DialogUtils;
import com.example.com.meimeng.util.Utils;

import org.json.JSONStringer;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by Administrator on 2015/7/23.
 */

public class InitiateEvent extends FragmentActivity implements SelectPictureFragment.OnSelectPictureDialogListener {

    private final static String TAG = "InitiateEvent";
    @Bind(R.id.title_text)
    TextView titleText;

    @Bind(R.id.title_left_arrow_image_view)
    ImageView leftArrowImageView;

    @Bind(R.id.bow_arrow_image_view)
    ImageView bowArrowImageView;

    @Bind(R.id.title_sure_text)
    TextView sureText;


    @Bind(R.id.main_dialog_layout)
    LinearLayout mainDialogLayout;


    @Bind(R.id.activity_wheel_layout)
    RelativeLayout wheelLayout;

    @Bind(R.id.activity_wheel_cover_button)
    Button coverButton;

    @Bind(R.id.activity_wheel_select_layout)
    RelativeLayout wheelSelectLayout;

    @Bind(R.id.activity_wheel_cancel_text)
    TextView wheelCancelText;

    @Bind(R.id.activity_wheel_sure_text)
    TextView wheelSureText;

    @Bind(R.id.activity_wheel_three_picker)
    ThreeWheelView threeWheelView;

    @Bind(R.id.activity_wheel_city_picker)
    CityPicker2 cityPicker;


    @Bind(R.id.event_initiate_title)
    TextView themeText;

    @Bind(R.id.event_initiate_startTime)
    TextView timeText;

    @Bind(R.id.event_initiate_placeDetailbase)
    TextView placeDetailbase;

    @Bind(R.id.event_initiate_picture_image_view)
    ImageView pictureImageView;

    @Bind(R.id.event_initiate_uploadpicture)
    ImageButton eventinitiateuploadpicture;

    private SelectPictureFragment selectPictureFragment;

    private HashMap<String, Long> picMap = new HashMap<>();

    private static final String PIC_UPLOAD = "pic_upload";

    private TextView placeDetail;
    private TextView describe;
    private String describeStr = "";
    private String placeDetailStr = "";
    private String theme = "";
    private String themeStr = "";


    private int placeDetailbaseId = -1;

    private String timeText2;

    private BaseBean basebeanJson;

    private float mDenity = 1f;
    private int typeWheel = 1;

    private Dialog dialog;
    public String timetextstr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.event_initiate);
        ButterKnife.bind(this);

        MeiMengApplication.currentContext=this;

        bowArrowImageView.setVisibility(View.GONE);
        leftArrowImageView.setVisibility(View.VISIBLE);
        titleText.setText("发起活动");

        placeDetail = (TextView) findViewById(R.id.event_initiate_placeDetail);

        describe = (TextView) findViewById(R.id.event_initiate_describe);

        mDenity = Utils.getScreenMetrics(InitiateEvent.this).density;

        //输入标题
        View selectView = findViewById(R.id.event_initiate_title_side);
        selectView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(InitiateEvent.this, InitiateEventDescribeInput.class);
                intent.putExtra("type", "主题");
                if (themeStr.equals("") || themeStr == null) {
                    intent.putExtra("hint_type", 0);
                    intent.putExtra("content", "请输入活动主题（15字以内）");
                } else {
                    intent.putExtra("hint_type", 1);
                    intent.putExtra("content", themeStr);
                }
                startActivityForResult(intent, 2);
            }
        });

        //输入活动详细地点
        View selectView3 = findViewById(R.id.event_initiate_placeDetail_side);
        selectView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(InitiateEvent.this, InitiateEventDescribeInput.class);
                intent.putExtra("type", "地点");

                if (placeDetailStr.equals("") || placeDetailStr == null) {
                    intent.putExtra("hint_type", 0);
                    intent.putExtra("content", "请输入邀约地点");
                } else {
                    intent.putExtra("hint_type", 1);
                    intent.putExtra("content", placeDetailStr);
                }
                startActivityForResult(intent, 1);
            }
        });
        //输入活动描述
        View selectView4 = findViewById(R.id.event_initiate_describe_side);
        selectView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setClass(InitiateEvent.this, InitiateEventDescribeInput.class);
                intent.putExtra("type", "活动详情");

                if (describeStr.equals("") || describeStr == null) {
                    intent.putExtra("hint_type", 0);
                    intent.putExtra("content", "请编辑活动详情");
                } else {
                    intent.putExtra("hint_type", 1);
                    intent.putExtra("content", describeStr);
                }
                startActivityForResult(intent, 0);

            }
        });
        //点击发送按钮上传数据，判断是否连接成功
        sureText.setVisibility(View.VISIBLE);
        sureText.setText("发起");
        sureText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog = DialogUtils.createLoadingDialog(InitiateEvent.this);
                dialog.show();

                //主题
                theme = themeText.getText().toString();

                if (theme.length() > 15) {
                    theme = theme.substring(0, 15);
                }

                if (theme == null || theme.equals("")) {
                    Toast.makeText(InitiateEvent.this, "请输入活动主题", Toast.LENGTH_SHORT).show();
                    if (null != dialog)
                        dialog.dismiss();
                } else {
                    if (timeText.getText() == null || timeText.getText().equals("")) {
                        Toast.makeText(InitiateEvent.this, "请输入活动时间", Toast.LENGTH_SHORT).show();
                        if (null != dialog)
                            dialog.dismiss();
                    } else {
                        if (placeDetailbaseId == -1) {
                            Toast.makeText(InitiateEvent.this, "请选择活动地点", Toast.LENGTH_SHORT).show();
                            if (null != dialog)
                                dialog.dismiss();
                        } else {
                            if (picMap.get(PIC_UPLOAD) == null) {
                                Toast.makeText(InitiateEvent.this, "请上传活动图片", Toast.LENGTH_SHORT).show();
                                if (null != dialog)
                                    dialog.dismiss();
                            } else {
                                if (placeDetailStr == null || placeDetailStr.equals("")) {
                                    Toast.makeText(InitiateEvent.this, "请输入活动详细地点", Toast.LENGTH_SHORT).show();
                                    if (null != dialog)
                                        dialog.dismiss();
                                }else{
                                    if (describeStr == null || describeStr.equals("")) {
                                        Toast.makeText(InitiateEvent.this, "请输入活动详情", Toast.LENGTH_SHORT).show();
                                        if (null != dialog)
                                            dialog.dismiss();
                                    }else{
                                        sendUp();
                                    }
                                }
                            }

                        }
                    }
                }


            }
        });
    }

    //发起活动的接口
    public void sendUp() {
        try {
            if (TextUtils.isEmpty(Utils.getUserId())) {
                return;
            }
            if (TextUtils.isEmpty(Utils.getUserToken())) {
                return;
            }
            String url = InternetConstant.SERVER_URL + InternetConstant.USER_ACTIVITY_NEW + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();
            JSONStringer jsonStringer = new JSONStringer().object()
                    .key("title").value(theme)
                    .key("startTime").value("2015-" + timeText2 + ":00")
                    .key("place").value(placeDetailbaseId)
                    .key("placeDetail").value(placeDetailStr)
                    .key("describe").value(describeStr)
                    .key("pic").value(picMap.get(PIC_UPLOAD))
                    .endObject();
            String jsonStr = jsonStringer.toString();

            //得到Observable并获取返回的数据(主线程中)
            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            if (null != dialog)
                                dialog.dismiss();
                            basebeanJson = GsonTools.getBaseReqBean(s);
                            if (basebeanJson.isSuccess() != false) {
                                Toast.makeText(InitiateEvent.this, "活动发起成功", Toast.LENGTH_SHORT).show();
                                onBackPressed();
                            } else {

                                DialogUtils.setDialog(InitiateEvent.this, basebeanJson.getError());
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


    //返回按钮
    @OnClick(R.id.title_left_arrow_layout)
    void back() {
        this.finish();
    }

    //上传图片按钮
    @OnClick(R.id.event_initiate_uploadpicture)
    void uploadWorkListener() {
        addSelectPictureFragment(PIC_UPLOAD);
    }

    //上传图片按钮
    @OnClick(R.id.event_initiate_picture_image_view)
    void uploadWorkListener2() {
        addSelectPictureFragment(PIC_UPLOAD);
    }

    @Override
    public void cancelDialog() {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.remove(selectPictureFragment);
        transaction.commit();
        mainDialogLayout.setVisibility(View.GONE);
    }

    @Override
    public void getPicturePath(String path) {
        Bitmap bitmap = Utils.getBitmap(path);
        if (bitmap != null) {
            pictureImageView.setImageBitmap(bitmap);
            eventinitiateuploadpicture.setVisibility(View.GONE);
        }
    }

    @Override
    public void setUploadProgress(String pro) {

    }

    @Override
    public void sendResultJson(String type, long pid) {
        picMap.put(type, pid);
    }

    @Override
    public void cancelDialog2() {
        if (null != dialog)
            dialog.dismiss();
    }

    @Override
    public void requestifok() {

    }


    @OnClick(R.id.dialog_button)
    void dialogButtonLitener() {
        cancelDialog();
    }

    private void addSelectPictureFragment(String type) {
        if (selectPictureFragment == null) {
            selectPictureFragment = new SelectPictureFragment();
        }
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        selectPictureFragment.setArguments(bundle);
        mainDialogLayout.setVisibility(View.VISIBLE);
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.anim.bottom_in, 0);
        transaction.add(R.id.dialog_layout, selectPictureFragment);
        transaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == 0 & resultCode == 1) {
                describeStr = data.getStringExtra("des").toString();
                describe.setText(describeStr);
            }
            if (requestCode == 1 & resultCode == 1) {
                placeDetailStr = data.getStringExtra("des").toString();
                placeDetail.setText(placeDetailStr);
            }
            if (requestCode == 2 & resultCode == 1) {
                themeStr = data.getStringExtra("des").toString();
                themeText.setText(themeStr);

            }
        }
    }

    @OnClick(R.id.event_initiate_start_time_layout)
    void startTimeListener() {
        hideSelectDialog();
        typeWheel = 1;
        showSelectDialog();

    }

    //所在地
    @OnClick(R.id.event_initiate_placeDetailbase_side)
    void residenceListener() {
        hideSelectDialog();
        typeWheel = 2;
        showSelectDialog();
    }

    @OnClick(R.id.activity_wheel_cancel_text)
    void wheelCancelListener() {
        hideSelectDialog();
    }

    @OnClick(R.id.activity_wheel_sure_text)
    void wheelSureTextListener() {

        switch (typeWheel) {
            case 1:
                timetextstr = threeWheelView.getData();
                timeText.setText(timetextstr);
                timeText2 = timetextstr.replaceAll("月", "-").replaceAll("日", "");
                break;
            case 2:
                String address = cityPicker.getAddress();
                placeDetailbase.setText(address);
                //得到城市的id
                placeDetailbaseId = cityPicker.getAddressId();
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
        coverButton.setVisibility(View.GONE);
        cityPicker.setVisibility(View.INVISIBLE);
        threeWheelView.setVisibility(View.INVISIBLE);
    }

    /**
     * 显示选择对话框
     */
    private void showSelectDialog() {
        wheelLayout.setVisibility(View.VISIBLE);
        coverButton.setVisibility(View.VISIBLE);
        wheelSelectLayout.setVisibility(View.VISIBLE);
        switch (typeWheel) {
            case 1:
                threeWheelView.setVisibility(View.VISIBLE);
            case 2:
                cityPicker.setVisibility(View.VISIBLE);
                break;
        }

        ObjectAnimator.ofFloat(wheelLayout, "translationY", 240f * mDenity, 0f).setDuration(500).start();
    }

}
