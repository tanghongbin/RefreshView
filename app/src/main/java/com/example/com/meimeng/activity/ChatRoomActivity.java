package com.example.com.meimeng.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.im.v2.messages.AVIMLocationMessage;
import com.avoscloud.leanchatlib.activity.ChatActivity;
import com.example.com.meimeng.MeiMengApplication;
import com.example.com.meimeng.R;
import com.example.com.meimeng.bean.ChatMessageBean;
import com.example.com.meimeng.constants.CommonConstants;
import com.example.com.meimeng.fragment.GiveGiftFragment;
import com.example.com.meimeng.fragment.ReportFragment;
import com.example.com.meimeng.gson.GsonTools;
import com.example.com.meimeng.gson.gsonbean.BaseBean;
import com.example.com.meimeng.gson.gsonbean.UserBaseInfoBean;
import com.example.com.meimeng.gson.gsonbean.UserChatInfoBean;
import com.example.com.meimeng.net.InternetConstant;
import com.example.com.meimeng.net.InternetUtils;
import com.example.com.meimeng.util.ChatSQliteDataUtil;
import com.example.com.meimeng.util.DialogUtils;
import com.example.com.meimeng.util.Utils;

import org.json.JSONException;
import org.json.JSONStringer;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by lzw on 15/4/27.
 */
public class ChatRoomActivity extends ChatActivity implements SensorEventListener,GiveGiftFragment.OnGiftListener,ReportFragment.ReportDialog {

    private AudioManager audioManager;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private ChatSQliteDataUtil sq = null;

    //礼物和文字的图片路径
    private final static String ALBUM_PATH = Environment.getExternalStorageDirectory() + "/download_test/";

    //利用intent传两个个id进来，通过id设置各种数值
    private String matchmakername;

    //标识对方是否是红娘
    private boolean isLover = false;

    private String otherid;

    private String type;

    //礼物图片
    private int giftPicId;

    //礼物名称
    private String giftName;

    //礼物Fragment
    private GiveGiftFragment mGiveGiftFragment;

    //对方的昵称
    private String otherName;

    //自己的昵称
    private String selfName;

    @Bind(R.id.main_dialog_layout)
    protected LinearLayout dialogLayout;

    //举报功能
    private ReportFragment reportFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        MeiMengApplication.currentContext=this;
        audioManager = (AudioManager) this
                .getSystemService(Context.AUDIO_SERVICE);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        sq = new ChatSQliteDataUtil(this);
        Intent intent = getIntent();
        otherid = intent.getStringExtra("otherid");
        type = intent.getStringExtra("type");
        matchmakername = intent.getStringExtra("matchmakername");

        //信息保存
        SharedPreferences.Editor editor = MeiMengApplication.sharedPreferences.edit();
        editor.putString("chat_other_id",otherid);
        editor.putString("chat_type",type);
        editor.putString("chat_matchmaker_name", matchmakername);
        editor.commit();

        getSelfHeadPic();
        adapter.setOtherid(otherid);

        //普通用户
        if ("1".equals(type)) {
            getOtherNickname(otherid);
            super.addGiftBtnText.setVisibility(View.VISIBLE);
            super.reportLayout.setVisibility(View.VISIBLE);
        }
        //官方
        else {
            setMatchmakerName();
            super.addGiftBtnText.setVisibility(View.GONE);
            super.reportLayout.setVisibility(View.GONE);
            isLover = true;
        }



        if (!isLover){

            ChatMessageBean bean = new ChatMessageBean();
            bean.setUserId(Long.parseLong(otherid));
            bean.setName(" ");
            bean.setHeadPic(2l);
            bean.setContent(" ");
            bean.setTime(" ");
            Map attrs=new HashMap();
            attrs.put("isOpen", true);
            bean.setAttributes(attrs);
            sq.addData(bean);
        }

        addLocationBtn.setVisibility(View.GONE);

        //赠礼物功能
        super.addGiftBtnText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addGiftDialogLayout();
//                Toast.makeText(ChatRoomActivity.this,"送礼物ChatRoomActivity",Toast.LENGTH_SHORT).show();
            }
        });

        //举报用户
        super.reportLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportUser();
            }
        });
    }

    /**
     * 举报用户
     *
     * @throws JSONException
     */
    void reportUser() {
        if (reportFragment == null) {
            reportFragment = new ReportFragment();
        }
        dialogLayout.setVisibility(View.VISIBLE);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.bottom_in, 0);
        transaction.replace(R.id.dialog_layout, reportFragment);
        transaction.commit();
    }


    /**
     * 赠送礼物
     */
    private void addGiftDialogLayout() {
        if (mGiveGiftFragment == null){
            mGiveGiftFragment = new GiveGiftFragment();
        }
        dialogLayout.setVisibility(View.VISIBLE);
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.anim.bottom_in, 0);
        transaction.add(R.id.dialog_layout, mGiveGiftFragment);
        transaction.commit();

    }

    /**
     * 取消礼物dialog
     */
    @OnClick(R.id.dialog_button)
    public void dialogListener() {
        cancelGiftDialog();
    }

    //头像的点击事件
    @Override
    protected void onClickHeader(String type) {

        //左边头像的点击事件
        if (type.equals("left")) {
            //不是红娘的时候
            if (!isLover){
                Intent intent = new Intent(this, OthersSelfActivity.class);
                intent.putExtra("targetUid", Long.parseLong(otherid));
                startActivity(intent);
            }
        }
        //右边头像的点击事件
        else {

        }

    }

    @Override
    protected void sendText() {


        if (!isLover){
            if (sq.isExist(Long.parseLong(otherid))) {
                Log.e("ChatRoomActivity", "----------------存在");
            } else {
                Log.e("ChatRoomActivity", "----------------bu存在");
            }
            SharedPreferences.Editor editor = MeiMengApplication.sharedPreferences.edit();
            editor.putString(CommonConstants.OTHER_MESSAGE, contentEdit.getText().toString());
            editor.putString(CommonConstants.OTHER_TIME, getCurrentDate());
            sq.upData("userid", otherid, "content", getContent(contentEdit.getText().toString()));
            sq.upData("userid", otherid, "time", getCurrentDate());
            editor.commit();

            Log.e("ChatRoomActivity", "----------------sendText");
        }


        super.sendText();
    }

    public String getContent(String content) {


        if (content.matches("^((:[A-Za-z0-9_]+:)*)$")) {
            content = "[表情]";
        }
        return content;
    }

    @Override
    public void selectImageFromCamera() {
        super.selectImageFromCamera();

        if (!isLover){
            SharedPreferences.Editor editor = MeiMengApplication.sharedPreferences.edit();
            editor.putString(CommonConstants.OTHER_MESSAGE, "[图片]");
            editor.putString(CommonConstants.OTHER_TIME, getCurrentDate());
            sq.upData("userid", otherid, "content", "[图片]");
            sq.upData("userid", otherid, "time", getCurrentDate());
            editor.commit();
            Log.e("ChatRoomActivity", "----------------selectImageFromCamera");
        }

    }

    @Override
    public void selectImageFromLocal() {
        super.selectImageFromLocal();

        if (!isLover){
            SharedPreferences.Editor editor = MeiMengApplication.sharedPreferences.edit();
            editor.putString(CommonConstants.OTHER_MESSAGE, "[图片]");
            editor.putString(CommonConstants.OTHER_TIME, getCurrentDate());
            sq.upData("userid", otherid, "content", "[图片]");
            sq.upData("userid", otherid, "time", getCurrentDate());
            editor.commit();
            Log.e("ChatRoomActivity", "----------------selectImageFromLocal");
        }

    }

    @Override
    public void showAudioLayout() {
        super.showAudioLayout();

        if (!isLover){
            SharedPreferences.Editor editor = MeiMengApplication.sharedPreferences.edit();
            editor.putString(CommonConstants.OTHER_MESSAGE, "[语音]");
            editor.putString(CommonConstants.OTHER_TIME, getCurrentDate());
            sq.upData("userid", otherid, "content", "[语音]");
            sq.upData("userid", otherid, "time", getCurrentDate());
            editor.commit();
            Log.e("ChatRoomActivity", "----------------showAudioLayout");
        }
    }

    private String getCurrentDate() {
        SimpleDateFormat df = new SimpleDateFormat("MM-dd HH:mm");
        String time = df.format(new Date());

        return time;
    }

    private void getSelfHeadPic() {

        try {

            //获取用户的uid,和token
            if (TextUtils.isEmpty(Utils.getUserId())) {
                return;
            }
            if (TextUtils.isEmpty(Utils.getUserToken())) {
                return;
            }
            String targetUid = String.valueOf(Utils.getUserId());

            String url = InternetConstant.SERVER_URL + InternetConstant.GET_USER_BASE_INFO_URL + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();
            JSONStringer stringer = new JSONStringer().object().key("targetUid").value(targetUid).endObject();
            String jsonStr = stringer.toString();
            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1() {
                        @Override
                        public void call(Object o) {
                            UserBaseInfoBean baseInfoBean = GsonTools.getUserBaseInfoBean((String) o);
                            if (baseInfoBean.isSuccess()) {

                                //自己的昵称
                                if (baseInfoBean.getParam().getUserSimpleInfo().getSex()==0){
                                    selfName = baseInfoBean.getParam().getUserSimpleInfo().getFirstName()+"先生";
                                }else {
                                    selfName = baseInfoBean.getParam().getUserSimpleInfo().getFirstName()+"女士";
                                }
                                ChatRoomActivity.this.adapter.setuPic(InternetUtils.setPictureUrl(baseInfoBean.getParam().getUserSimpleInfo().getHeadPic()));
                                adapter.notifyDataSetChanged();

                            } else {
                                DialogUtils.setDialog(ChatRoomActivity.this, baseInfoBean.getError());
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            Log.e("获取用户基本信息失败了", throwable.getMessage());
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void getOtherNickname(final String uid) {

        try {
            //获取用户的uid,和token
            if (TextUtils.isEmpty(Utils.getUserId())) {
                return;
            }
            if (TextUtils.isEmpty(Utils.getUserToken())) {
                return;
            }
            String targetUid = String.valueOf(uid);

            String url = InternetConstant.SERVER_URL + InternetConstant.PEOPLE_LIST_CHAT + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();
            JSONStringer stringer = new JSONStringer().object().key("targetUid").value(targetUid).endObject();
            String jsonStr = stringer.toString();
            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1() {
                        @Override
                        public void call(Object o) {
                            UserChatInfoBean userChatInfoBean = GsonTools.getUserChatInfoParam(o.toString());
                            if (userChatInfoBean.isSuccess()) {
                                Log.d("wz","."+userChatInfoBean);
                                Log.d("wz",".."+userChatInfoBean.getParam());
                                Log.d("wz","..."+userChatInfoBean.getParam().getUserChatInfoItem().getSex());
                                //男
                                if (userChatInfoBean.getParam().getUserChatInfoItem().getSex()==0){
                                    otherName = userChatInfoBean.getParam().getUserChatInfoItem().getFirstName()+"先生";
                                }
                                //女
                                else {
                                    otherName = userChatInfoBean.getParam().getUserChatInfoItem().getFirstName()+"女士";
                                }
                                setOthersNameText(otherName);
                                ChatRoomActivity.this.adapter.setOtherPic(InternetUtils.setPictureUrl(userChatInfoBean.getParam().getUserChatInfoItem().getHeadPic()));

                                String headerPicID = String.valueOf(userChatInfoBean.getParam().getUserChatInfoItem().getHeadPic());
                                if ((!TextUtils.isEmpty(otherName)) && (!TextUtils.isEmpty(headerPicID)) && (!isLover)) {

                                    sq.upData("userid", uid, "name", otherName);
                                    sq.upData("userid", uid, "bitmap", headerPicID);
                                }
                                adapter.notifyDataSetChanged();

                            } else {
                                DialogUtils.setDialog(ChatRoomActivity.this, userChatInfoBean.getError());
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            Log.e("获取用户基本信息失败了", throwable.getMessage());
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 官方
     */
    private void setMatchmakerName() {
        if(matchmakername.equals("官方电话")){
            matchmakername="私人红娘";
        }
        setOthersNameText(matchmakername);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onAddLocationButtonClicked(View v) {
        toast("这里可以跳转到地图界面，选取地址");
    }

    @Override
    protected void onLocationMessageViewClicked(AVIMLocationMessage locationMessage) {
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MeiMengApplication.isSound = 1;
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        float range = event.values[0];

        if (range == 0.0) {
            //    Toast.makeText(this, "听筒模式", Toast.LENGTH_SHORT).show();
            audioManager.setSpeakerphoneOn(false);//关闭扬声器
            audioManager.setRouting(AudioManager.MODE_NORMAL, AudioManager.ROUTE_EARPIECE, AudioManager.ROUTE_ALL);
            setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
            //把声音设定成Earpiece（听筒）出来，设定为正在通话中
            audioManager.setMode(AudioManager.MODE_IN_CALL);
        } else {
            //   Toast.makeText(this, "正常模式", Toast.LENGTH_SHORT).show();
            audioManager.setSpeakerphoneOn(true);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onResume() {
        mSensorManager.registerListener(this, mSensor,
                SensorManager.SENSOR_DELAY_NORMAL);
        super.onResume();

        //赠送礼物成功后，发送图文信息给对方
        Integer chatFlag = MeiMengApplication.sharedPreferences.getInt("chat_flag",-1);
        if (chatFlag==1){
            makePicture();
            messageAgent.sendImage(ALBUM_PATH + "gift_content.jpg");

            SharedPreferences.Editor editor = MeiMengApplication.sharedPreferences.edit();
            editor.putInt("chat_flag",-1);
            editor.commit();
        }


    }

    /**
     * 构造礼物图片和文字的图片
     */
    private void makePicture(){
        View view = LayoutInflater.from(ChatRoomActivity.this).inflate(R.layout.picture_layout,null);
        ImageView imageView = (ImageView) view.findViewById(R.id.pic_image_view);
        TextView textView = (TextView) view.findViewById(R.id.pic_text_view);
        LinearLayout picLayout = (LinearLayout) view.findViewById(R.id.pic_Layout);
        imageView.setImageResource(giftPicId);
        textView.setText(selfName+"送给"+otherName+giftName);

        picLayout.setDrawingCacheEnabled(true);
        picLayout.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        );
        picLayout.layout(0, 0, picLayout.getMeasuredWidth(), picLayout.getMeasuredHeight());
        picLayout.buildDrawingCache();

        Bitmap bitmap = picLayout.getDrawingCache();


        saveFile(bitmap);

    }

    /**
     * 将图片保存在本地
     * @param bm
     */
    public void saveFile(Bitmap bm) {
        try{
            File dirFile = new File(ALBUM_PATH);
            if(!dirFile.exists()){
                dirFile.mkdir();
            }
            File myCaptureFile = new File(ALBUM_PATH + "gift_content.jpg");
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
            bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
            bos.flush();
            bos.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(this);
        super.onPause();
    }

    /**
     * 送礼物功能
     * @param goodId
     * @param name
     * @param price
     */
    @Override
    public void giveListener(int  picId,long goodId, String name, String price) {

        giftPicId = picId;
        giftName = name;

        MeiMengApplication.weixinPayCallBack = 6;
        Intent intent = new Intent(ChatRoomActivity.this, PayActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("price", price);
        intent.putExtra("goodId", goodId);
        intent.putExtra("targetUid", Long.valueOf(otherid));
        intent.putExtra("pay_type", 2);
        startActivity(intent);
        cancelGiftDialog();
    }

    /**
     * 取消弹出框
     */
    private void cancelGiftDialog() {
        if (null != mGiveGiftFragment && mGiveGiftFragment.isAdded()) {
            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.remove(mGiveGiftFragment);
            transaction.commit();
        }
        dialogLayout.setVisibility(View.GONE);

    }

    @Override
    public void cancel() {
        FragmentManager fragmentManager = this.getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.remove(reportFragment);
        transaction.commit();

        dialogLayout.setVisibility(View.GONE);
    }

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
            JSONStringer jsonStringer = new JSONStringer().object().key("reportUid").value(Long.valueOf(otherid)).key("type").value(type).endObject();
            String jsonStr = jsonStringer.toString();
            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            BaseBean baseBean = GsonTools.getBaseReqBean(s);
                            if (!baseBean.isSuccess()) {
                                DialogUtils.setDialog(ChatRoomActivity.this, baseBean.getError());
                            } else {
                                Toast.makeText(ChatRoomActivity.this, "已经成功举报该用户！", Toast.LENGTH_SHORT).show();
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
        //取消对话框
        cancel();
    }
}
