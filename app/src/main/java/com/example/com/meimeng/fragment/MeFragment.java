package com.example.com.meimeng.fragment;

import android.app.Dialog;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.com.meimeng.MeiMengApplication;
import com.example.com.meimeng.R;
import com.example.com.meimeng.activity.AboutMeimeng;
import com.example.com.meimeng.activity.CertificationActivity;
import com.example.com.meimeng.activity.DataEdit;
import com.example.com.meimeng.activity.GitManagerActivity;
import com.example.com.meimeng.activity.HomeActivity;
import com.example.com.meimeng.activity.InviteActivity;
import com.example.com.meimeng.activity.SettingActivity;
import com.example.com.meimeng.activity.VipActivity;
import com.example.com.meimeng.constants.CommonConstants;
import com.example.com.meimeng.dialog.AlbumUnLockDialog;
import com.example.com.meimeng.dialog.SelectPictureDialog;
import com.example.com.meimeng.dialog.SimpleDialog;
import com.example.com.meimeng.fragment.management.AllFragmentManagement;
import com.example.com.meimeng.gson.GsonTools;
import com.example.com.meimeng.gson.gsonbean.BaseBean;
import com.example.com.meimeng.gson.gsonbean.MyBaseInfoBean;
import com.example.com.meimeng.gson.gsonbean.MyBaseInfoItem;
import com.example.com.meimeng.gson.gsonbean.MyPhotoItem;
import com.example.com.meimeng.net.InternetConstant;
import com.example.com.meimeng.net.InternetUtils;
import com.example.com.meimeng.util.DialogUtils;
import com.example.com.meimeng.util.Utils;

import org.json.JSONStringer;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by 010 on 2015/7/13.
 */
public class MeFragment extends Fragment implements /*View.OnClickListener,*/ AlbumUnLockDialog.OnFinishUnlock{

    private static final String TAG = "MeFragment";

    @Bind(R.id.common_name_text_view)
    TextView nameTextView;

    @Bind(R.id.common_id_text_view)
    TextView idTextView;

    @Bind(R.id.common_age_text_view)
    TextView ageTextView;

    @Bind(R.id.common_height_text_view)
    TextView heightTextView;

    @Bind(R.id.common_location_text_view)
    TextView locationTextView;

    @Bind(R.id.common_vip_lvl_text_view)
    TextView vipText;

    @Bind(R.id.common_vip_lvl_image_view)
    ImageView vipImageView;

    @Bind(R.id.common_follow_num_text_view)
    TextView followNumTextView;

    @Bind(R.id.me_info_integrity_text_view)
    TextView infoIntegrityTextView;

    @Bind(R.id.me_image_view_1)
    ImageView albumImage1;

    @Bind(R.id.me_image_view_2)
    ImageView albumImage2;

    @Bind(R.id.me_image_view_3)
    ImageView albumImage3;

    @Bind(R.id.me_image_view_4)
    ImageView albumImage4;

    @Bind(R.id.me_image_view_5)
    ImageView albumImage5;

    @Bind(R.id.me_image_view_6)
    ImageView albumImage6;

    @Bind(R.id.me_info_integrity_progress)
    ProgressBar mProgressBar;


    private SelectPictureDialog selectPictureDialog;

    private SharedPreferences.Editor editor;
    private Dialog dialog;
    private Context context;


    private GestureDetector gestureDetector;
    private ImageView curImageView;
    private View meView;
    private ArrayList<ImageView> photoImageViews;

    private Handler myhandler;

    public Handler getMyhandler() {
        return myhandler;
    }

    public void setMyhandler(Handler myhandler) {
        this.myhandler = myhandler;
    }

    //图片解锁弹出窗口
    private PopupWindow unlockPopWindow;
    private LinearLayout passwordLayout;
    private ImageView lockedImageView;
    private EditText passwordEditText;
    private Button savePasswordButton;
    private LinearLayout savePasswordLayout;

    //删除图片的dialog
    private SimpleDialog deletePhotoDialog;

    //图片锁，0--没有锁，1---有锁
    private int photoLockFlag = 0;
    /**
     * 属性Me界面的广播接收器
     */
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("action:MeimengRefreshMe") || action.equals("action.meimengrefreshpicture")) {
                initView(false);
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate");
        AllFragmentManagement.fragmentList.add(this);
        context = getActivity();
        ReflshBroadcast();

    }

    private void ReflshBroadcast() {
        //动态注册广播消息，刷新照片信息
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("action:MeimengRefreshMe");
        intentFilter.addAction("action.meimengrefreshpicture");
        context.registerReceiver(broadcastReceiver, intentFilter);
    }

    private LinearLayout getAduitView(ImageView imgView) {
        RelativeLayout parentView = (RelativeLayout) imgView.getParent();
        LinearLayout auditingView = (LinearLayout) parentView.findViewById(R.id.me_auditing_layout);
        return auditingView;
    }

    private void initImageViews() {

        photoImageViews = new ArrayList<>();
        albumImage1.setTag(0);
        albumImage2.setTag(0);
        albumImage3.setTag(0);
        albumImage4.setTag(0);
        albumImage5.setTag(0);
        albumImage6.setTag(0);

//        albumImage1.setOnClickListener(this);
//        albumImage2.setOnClickListener(this);
//        albumImage3.setOnClickListener(this);
//        albumImage4.setOnClickListener(this);
//        albumImage5.setOnClickListener(this);
//        albumImage6.setOnClickListener(this);

        albumImage1.setOnTouchListener(onImageViewTouchListener);
        albumImage1.setOnDragListener(onImageViewDragListener);

        albumImage2.setOnTouchListener(onImageViewTouchListener);
        albumImage2.setOnDragListener(onImageViewDragListener);

        albumImage3.setOnTouchListener(onImageViewTouchListener);
        albumImage3.setOnDragListener(onImageViewDragListener);

        albumImage4.setOnTouchListener(onImageViewTouchListener);
        albumImage4.setOnDragListener(onImageViewDragListener);

        albumImage5.setOnTouchListener(onImageViewTouchListener);
        albumImage5.setOnDragListener(onImageViewDragListener);

        albumImage6.setOnTouchListener(onImageViewTouchListener);
        albumImage6.setOnDragListener(onImageViewDragListener);


        photoImageViews.add(albumImage1);
        photoImageViews.add(albumImage2);
        photoImageViews.add(albumImage3);
        photoImageViews.add(albumImage4);
        photoImageViews.add(albumImage5);
        photoImageViews.add(albumImage6);
    }


    /**
     * 初始化设置相册密码点击弹窗
     *//*
    private void initPasswordPopupWindow() {
        LayoutInflater inflater = (LayoutInflater) this.getActivity().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.me_photo_password_layout, null);
        passwordLayout = (LinearLayout) layout.findViewById(R.id.me_password_layout);
        lockedImageView = (ImageView) this.meView.findViewById(R.id.me_lock_image_view);
        savePasswordButton = (Button) passwordLayout.findViewById(R.id.me_photo_set_password_button);
        savePasswordLayout = (LinearLayout) layout.findViewById(R.id.me_photo_password_sure_layout);
        savePasswordLayout.setOnClickListener(this);
        passwordEditText = (EditText) passwordLayout.findViewById(R.id.me_photo_password_text_view);

        unlockPopWindow = new PopupWindow(this.getActivity());
        unlockPopWindow.setContentView(layout);
        unlockPopWindow.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.top_bg_popup));
        unlockPopWindow.setFocusable(true);

        savePasswordButton.setOnClickListener(this);
        passwordLayout.setOnClickListener(this);
        passwordLayout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        WindowManager wm = (WindowManager) this.getActivity().getSystemService(Activity.WINDOW_SERVICE);
        Point size = new Point();
        wm.getDefaultDisplay().getSize(size);
        unlockPopWindow.setWidth(size.x - 60);
        unlockPopWindow.setHeight(passwordLayout.getMeasuredHeight());

        unlockPopWindow.setOutsideTouchable(true);
        lockedImageView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

        unlockPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 1f;
                getActivity().getWindow().setAttributes(lp);
            }
        });
    }
*/
/*    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.me_lock_image_view:

                //没有锁
                if (photoLockFlag == 0) {
                    if (unlockPopWindow.isShowing()) {
                        unlockPopWindow.dismiss();
                    } else {
                        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                        lp.alpha = 0.5f;
                        getActivity().getWindow().setAttributes(lp);
                        unlockPopWindow.showAtLocation(lockedImageView, Gravity.CENTER, 0, -80);
                    }
                }
                //是否解锁
                else {
                    AlbumUnLockDialog dialog = new AlbumUnLockDialog(context, R.style.loading_dialog);
                    dialog.setOnFinishUnlock(this);
                    dialog.show();
                }

                break;

            case R.id.me_photo_password_sure_layout:

                String pass = passwordEditText.getText().toString();
                if (pass.length() < 4) {
                    Toast.makeText(getActivity(), "请输入完整密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                passwordEditText.setText(null);
                updatePhotoPassword(pass);
                unlockPopWindow.dismiss();
                break;

            default:
                break;
        }
    }*/

    @Override
    public void onStart() {
        super.onStart();
        initView(true);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.d("wz", "hidden=" + hidden);
        if (hidden == false) {
            initView(false);
        }
    }

    public void  getArrayList(){
        ArrayList<Long> picList = new ArrayList<>();
        if (!String.valueOf(albumImage1.getTag()).equals("0")) {
            String pid = String.valueOf(albumImage1.getTag());
            picList.add(Long.valueOf(pid));
        }
        if (!String.valueOf(albumImage2.getTag()).equals("0")) {
            String pid = String.valueOf(albumImage2.getTag());
            picList.add(Long.valueOf(pid));
        }
        if (!String.valueOf(albumImage3.getTag()).equals("0")) {
            String pid = String.valueOf(albumImage3.getTag());
            picList.add(Long.valueOf(pid));
        }
        if (!String.valueOf(albumImage4.getTag()).equals("0")) {
            String pid = String.valueOf(albumImage4.getTag());
            picList.add(Long.valueOf(pid));
        }
        if (!String.valueOf(albumImage5.getTag()).equals("0")) {
            String pid = String.valueOf(albumImage5.getTag());
            picList.add(Long.valueOf(pid));
        }
        if (!String.valueOf(albumImage6.getTag()).equals("0")) {
            String pid = String.valueOf(albumImage6.getTag());
            picList.add(Long.valueOf(pid));
        }

        //调接口，保存图片
        try {
            if (TextUtils.isEmpty(Utils.getUserId())) {
                return;
            }
            if (TextUtils.isEmpty(Utils.getUserToken())) {
                return;
            }
            String url = InternetConstant.SERVER_URL + InternetConstant.USER_EDIT + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();
            JSONStringer jsonStringer = new JSONStringer().object()
                    .key("lstPhoto").array();
            for (int i = 0; i < picList.size(); i++) {
                jsonStringer.value(picList.get(i));
            }
            jsonStringer.endArray().endObject();
            String jsonStr = jsonStringer.toString();

            timeOutCloseDialog();   //网络获取超时设置
            //得到Observable并获取返回的数据(主线程中)
            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            if (null != dialog)
                                dialog.dismiss();
                            BaseBean baseBeanJson = GsonTools.getBaseReqBean(s);
                            if (baseBeanJson.isSuccess() != false) {
                               //Toast.makeText(context, "相册保存成功", Toast.LENGTH_SHORT).show();
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
    /**
     * 相册保存按钮的功能
     * 是根据控件imageView的tag来获取图片的id
     */
    @OnClick(R.id.me_header_save_text)
    protected void saveUserPhoto() {
        ArrayList<Long> picList = new ArrayList<>();
        if (!String.valueOf(albumImage1.getTag()).equals("0")) {
            String pid = String.valueOf(albumImage1.getTag());
            picList.add(Long.valueOf(pid));
        }
        if (!String.valueOf(albumImage2.getTag()).equals("0")) {
            String pid = String.valueOf(albumImage2.getTag());
            picList.add(Long.valueOf(pid));
        }
        if (!String.valueOf(albumImage3.getTag()).equals("0")) {
            String pid = String.valueOf(albumImage3.getTag());
            picList.add(Long.valueOf(pid));
        }
        if (!String.valueOf(albumImage4.getTag()).equals("0")) {
            String pid = String.valueOf(albumImage4.getTag());
            picList.add(Long.valueOf(pid));
        }
        if (!String.valueOf(albumImage5.getTag()).equals("0")) {
            String pid = String.valueOf(albumImage5.getTag());
            picList.add(Long.valueOf(pid));
        }
        if (!String.valueOf(albumImage6.getTag()).equals("0")) {
            String pid = String.valueOf(albumImage6.getTag());
            picList.add(Long.valueOf(pid));
        }

        //调接口，保存图片
        try {
            if (TextUtils.isEmpty(Utils.getUserId())) {
                return;
            }
            if (TextUtils.isEmpty(Utils.getUserToken())) {
                return;
            }
            String url = InternetConstant.SERVER_URL + InternetConstant.USER_EDIT + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();
            JSONStringer jsonStringer = new JSONStringer().object()
                    .key("lstPhoto").array();
            for (int i = 0; i < picList.size(); i++) {
                jsonStringer.value(picList.get(i));
            }
            jsonStringer.endArray().endObject();
            String jsonStr = jsonStringer.toString();

            timeOutCloseDialog();   //网络获取超时设置
            //得到Observable并获取返回的数据(主线程中)
            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            if (null != dialog)
                                dialog.dismiss();
                            BaseBean baseBeanJson = GsonTools.getBaseReqBean(s);
                            if (baseBeanJson.isSuccess() != false) {
                                Toast.makeText(context, "相册保存成功", Toast.LENGTH_SHORT).show();
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

    private long getPhotoId(ImageView imageView) {
        if (imageView.getTag() == null)
            return 0;

        return Long.parseLong(imageView.getTag().toString());
    }

    private ArrayList<Long> getPhotoIdList() {
        ArrayList<Long> lst = new ArrayList<Long>();
        //lst.add(getPhotoId(headPicImageView));
        long picId;
        for (int i = 0; i < photoImageViews.size(); i++) {
            picId = getPhotoId(photoImageViews.get(i));
            if (picId != 0)
                lst.add(picId);
        }

        return lst;
    }

    private ArrayList<String> getPhotoIdStrList() {
        ArrayList<String> lst = new ArrayList<String>();
        //lst.add(getPhotoId(headPicImageView));
        long picId;
        for (int i = 0; i < photoImageViews.size(); i++) {
            picId = getPhotoId(photoImageViews.get(i));
            if (picId != 0)
                lst.add(String.valueOf(picId));
        }

        return lst;
    }

  /*  private void startPhotoDisplayEditActivity() {
        Intent intent = new Intent(context, PhotoDisplayEditActivity.class);

        int curIndex = 0;
        long picId;
        for (int i = 0; i < photoImageViews.size(); i++) {
            picId = getPhotoId(photoImageViews.get(i));
            if (picId == 0)
                continue;

            curIndex++;
            if (photoImageViews.get(i) == curImageView) {
                break;
            }
        }

        intent.putExtra("ImageId", getPhotoId(curImageView));
        intent.putExtra("position", curIndex);
        intent.putExtra("allImageId", getPhotoIdList());
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.pic_in, R.anim.pic_out);
    }*/

    private View.OnTouchListener onImageViewTouchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            curImageView = (ImageView) v;

            if (gestureDetector.onTouchEvent(event))
                return true;
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_UP:
                    break;
            }
            return false;
        }
    };


    Handler saveHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what ==999) {
                getArrayList();
            }
        }
    };
    private View.OnDragListener onImageViewDragListener = new View.OnDragListener() {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // Do nothing
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    v.setAlpha(0.5F);
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    v.setAlpha(1F);
                    break;
                case DragEvent.ACTION_DROP:
                    View view = (View) event.getLocalState();
                    exchangeImageView(curImageView, (ImageView) v);
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    v.setAlpha(1F);
                default:
                    break;
            }
            return true;
        }
    };

    @Override
    public void unlock(int num) {
        photoLockFlag = num;
    }


    private class DrapGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);
            ClipData data = ClipData.newPlainText("", "");
            MyDragShadowBuilder shadowBuilder = new MyDragShadowBuilder(curImageView);
            curImageView.startDrag(data, shadowBuilder, curImageView, 0);
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            Object tag = curImageView.getTag();
            if (tag == null)
                return true;

            if (String.valueOf(tag).equals("0")) {
                ((OnMeListener) context).uploadPicture(curImageView, 2);
            } else {

                String tagStr = String.valueOf(tag);
                if (deletePhotoDialog == null) {
                    deletePhotoDialog = new SimpleDialog(saveHandler,context, R.style.loading_dialog);
                    deletePhotoDialog.setOnFinishListener(new SimpleDialog.OnFinishListener() {
                        @Override
                        public void successListener() {
                            deletePhotoDialog.dismiss();
                            curImageView.setImageResource(R.drawable.add_photo);
                            curImageView.setTag(0);
                            LinearLayout auditView = getAduitView(curImageView);
                            auditView.setVisibility(View.GONE);
                        }

                        @Override
                        public void failureListener() {
                            deletePhotoDialog.dismiss();
                        }
                    });
                }
                deletePhotoDialog.setPicId(Long.valueOf(tagStr));
                deletePhotoDialog.show();

            }


            return true;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
    }

    private class MyDragShadowBuilder extends View.DragShadowBuilder {

        private final WeakReference<View> mView;

        public MyDragShadowBuilder(View view) {
            super(view);
            mView = new WeakReference<View>(view);
        }

        @Override
        public void onDrawShadow(Canvas canvas) {
            super.onDrawShadow(canvas);
        }

        @Override
        public void onProvideShadowMetrics(Point shadowSize,
                                           Point shadowTouchPoint) {

            final View view = mView.get();
            if (view != null) {
                shadowSize.set((int) (view.getWidth() * 1.5F),
                        (int) (view.getHeight() * 1.5F));
                shadowTouchPoint.set(shadowSize.x / 2, shadowSize.y / 2);
            } else {
            }
        }
    }

    private void exchangeImageView(ImageView sourceView, ImageView destView) {


        //交换图片
        Drawable dr1 = sourceView.getDrawable();
        Drawable dr2 = destView.getDrawable();
        sourceView.setImageDrawable(dr2);
        destView.setImageDrawable(dr1);

        //交换审核信息
        LinearLayout sourceLayout = (LinearLayout) getAduitView(sourceView);
        LinearLayout destLayout = (LinearLayout) getAduitView(destView);
        int sourceVisibile = sourceLayout.getVisibility();
        int destVisibile = destLayout.getVisibility();
        sourceLayout.setVisibility(destVisibile);
        destLayout.setVisibility(sourceVisibile);

        //交换tag信息
        Object tag1 = sourceView.getTag();
        Object tag2 = destView.getTag();
        sourceView.setTag(tag2);
        destView.setTag(tag1);


    }

    /*private void updatePhotoPassword(String albumPsw) {
        try {
            if (TextUtils.isEmpty(Utils.getUserId())) {
                return;
            }
            if (TextUtils.isEmpty(Utils.getUserToken())) {
                return;
            }

            String url = InternetConstant.SERVER_URL + InternetConstant.UNLOCK_ALBUM + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();

            int isLock = 1;

            JSONStringer jsonStringer = new JSONStringer().object()
                    .key("photoLock").value(isLock)
                    .key("photoPsw").value(albumPsw)
                    .endObject();
            String jsonStr = jsonStringer.toString();

            timeOutCloseDialog();   //网络获取超时设置
            //得到Observable并获取返回的数据(主线程中)
            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            if (null != dialog)
                                dialog.dismiss();

                            BaseBean baseBeanJson = GsonTools.getBaseReqBean(s);
                            if (baseBeanJson.isSuccess() != false) {
                                Toast.makeText(getActivity(), "设置成功", Toast.LENGTH_SHORT).show();
                                photoLockFlag = 1;
                                //DialogUtils.setDialog(getActivity(), "设置成功");
                            } else {
                                Toast.makeText(getActivity(), "设置失败", Toast.LENGTH_SHORT).show();
                                DialogUtils.setDialog(getActivity(), baseBeanJson.getError());
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

  /*  private void updatePhotoIndex() {
        try {
            if (TextUtils.isEmpty(Utils.getUserId())) {
                return;
            }
            if (TextUtils.isEmpty(Utils.getUserToken())) {
                return;
            }

            ArrayList<String> photoIds = getPhotoIdStrList();

            String url = InternetConstant.SERVER_URL + InternetConstant.USER_EDIT + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();

            JSONObject object = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < photoIds.size(); i++) {
                jsonArray.put(photoIds.get(i));
            }
            object.put("lstPhoto", jsonArray);

            String jsonStr = object.toString();
            timeOutCloseDialog();   //网络获取超时设置
            //得到Observable并获取返回的数据(主线程中)
            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            if (null != dialog)
                                dialog.dismiss();

                            BaseBean baseBeanJson = GsonTools.getBaseReqBean(s);
                            if (baseBeanJson.isSuccess() != false) {

                            } else {
                                DialogUtils.setDialog(getActivity(), baseBeanJson.getError());
                            }

                            //重新加载
                            initView();
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

    public interface OnMeListener {
        //上传图片
        void uploadPicture(ImageView imageView, int sort);

        //上传图片
        void uploadHeadpicture(ImageView imageView);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.me, container, false);
        ButterKnife.bind(this, view);
        MeiMengApplication.currentContext=getActivity();
        Log.e(TAG, "onCreateView");
        dialog = Utils.createLoadingDialog(getActivity(), "载入中...");
        dialog.show();
        this.meView = view;
        gestureDetector = new GestureDetector(context, new DrapGestureListener());

        HomeActivity.setMeHandler(saveHandler);//自动保存相册
        //初始化相册密码弹窗菜单
       // lockedImageView = (ImageView) this.meView.findViewById(R.id.me_lock_image_view);
       // lockedImageView.setOnClickListener(this);
       // initPasswordPopupWindow();
        initImageViews();

        initView(false);
        myhandler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                initView(false);
            }
        };
        return view;
    }

    //初始化界面
    public void initView(final boolean info) {
        //如果有网
        if (InternetUtils.isNetworkConnected(context)) {
            try {
                if (TextUtils.isEmpty(Utils.getUserId())) {
                    return;
                }
                if (TextUtils.isEmpty(Utils.getUserToken())) {
                    return;
                }
                String url = InternetConstant.SERVER_URL + InternetConstant.GET_USER_BASE_INFO_URL + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();
                JSONStringer stringer = new JSONStringer().object().key("targetUid").value(Utils.getUserId()).endObject();
                String jsonStr = stringer.toString();
                timeOutCloseDialog();
                Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
                observable.observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1() {
                            @Override
                            public void call(Object o) {
                                MyBaseInfoBean baseInfoBean = GsonTools.getMyBaseInfoBean((String) o);
                                if (null != dialog)
                                    dialog.dismiss();
                                if (baseInfoBean.isSuccess()) {
                                    initWedget(baseInfoBean,info);
                                } else {
                                    if (baseInfoBean.getError().equals("未登陆")) {
                                        DialogUtils.setDialog(context, baseInfoBean.getError());
                                    } else {
//                                        Toast.makeText(getActivity(), baseInfoBean.getError(), Toast.LENGTH_LONG).show();
                                    }

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
        } else {
            Toast.makeText(getActivity(), "网络没有连接，请检查您的网络", Toast.LENGTH_LONG).show();
        }
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
                    Toast.makeText(getActivity(), "当前网络不可用，请检查您的网络设置", Toast.LENGTH_SHORT).show();
                }
            } else {
                //不做操作
            }
        }
    };

    private void initPhotos(MyBaseInfoBean baseInfoBean) {
        MyBaseInfoItem infoItem = baseInfoBean.getParam().getUserSimpleInfo();

        if (infoItem.getMyPhotos() == null)
            return;

        int size = infoItem.getMyPhotos().size();

        for (int i = 0; i < size; i++) {

            MyPhotoItem item = infoItem.getMyPhotos().get(i);

            photoImageViews.get(i).setTag(item.getPicId());
            InternetUtils.getPicIntoView(244, 244, photoImageViews.get(i), item.getPicId());

            LinearLayout auditView = getAduitView(photoImageViews.get(i));
            if(item.getVerifyState() == 0){
                auditView.setVisibility(View.VISIBLE);
            }else{
                auditView.setVisibility(View.GONE);
            }

        }

        for (int i = size; i < 6; i++) {
            photoImageViews.get(i).setImageResource(R.drawable.add_photo);
            photoImageViews.get(i).setTag(0);
        }
    }

    private void initWedget(MyBaseInfoBean baseInfoBean,boolean info) {



        //图片锁
        photoLockFlag = baseInfoBean.getParam().getUserSimpleInfo().getPhotoLock();

        if (info==false){
            //我的照片初始化
            initPhotos(baseInfoBean);
        }

        MyBaseInfoItem infoItem = baseInfoBean.getParam().getUserSimpleInfo();
        editor = MeiMengApplication.sharedPreferences.edit();
        editor.putInt(CommonConstants.USER_LEVEL, infoItem.getVipLevel()).commit();
        editor.putInt(CommonConstants.USER_SEX, infoItem.getSex()).commit();

        setVipLevel(infoItem.getVipLevel());

        String userName = "";
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

        nameTextView.setText(userName);
        idTextView.setText("ID: " + infoItem.getUid());
        if (infoItem.getAge() == 0) {
            ageTextView.setText("未知");
        }else{
            ageTextView.setText(infoItem.getAge() + "岁");
        }
        if (infoItem.getHeight() == 0) {
            heightTextView.setText("未知");
        }else{
            heightTextView.setText(infoItem.getHeight() + "CM");
        }
        locationTextView.setText((infoItem.getCity()==null||infoItem.getCity().equals(""))?"未知":infoItem.getCity());
        followNumTextView.setText(infoItem.getFollowerNum() + "");

        infoIntegrityTextView.setText("资料完成" + infoItem.getInfoSize() + "%");

        mProgressBar.setProgress(infoItem.getInfoSize());
    }

    /**
     * 设置会员等级图标
     *
     * @param level 等级
     */
    private void setVipLevel(int level) {
        switch (level) {
            case 0:
                vipText.setText("普通会籍");
                vipImageView.setImageDrawable(getResources().getDrawable(R.drawable.vip_0));
                break;
            case 1:
                vipText.setText("银牌会籍");
                vipImageView.setImageDrawable(getResources().getDrawable(R.drawable.vip_1));
                break;
            case 2:
                vipText.setText("金牌会籍");
                vipImageView.setImageDrawable(getResources().getDrawable(R.drawable.vip_2));
                break;
            case 3:
                vipText.setText("黑牌会籍");
                vipImageView.setImageDrawable(getResources().getDrawable(R.drawable.vip_3));
                break;

            default:
                break;
        }
    }

    /**
     * 启动另一个activity
     *
     * @param c
     */
    private void myStartActivity(Class<?> c) {
        Intent intent = new Intent(context, c);
        context.startActivity(intent);
    }

    /**
     * 进入我的活动
     */
    @OnClick(R.id.me_my_event_layout)
    void myEventListener() {
        myStartActivity(CertificationActivity.class);
    }

    /**
     * 进入我的礼物
     */
    @OnClick(R.id.me_gift)
    void meGiftListener() {
        myStartActivity(GitManagerActivity.class);
    }

    /**
     * 邀请好友
     */
    @OnClick(R.id.me_privilege_layout)
    void meprivilegelayoutListener() {
        Intent intent = new Intent(context, InviteActivity.class);
        intent.putExtra("type", 1);
        context.startActivity(intent);
    }

    /**
     * 会员服务
     */
    @OnClick(R.id.me_vip_service_layout)
    void vipServiceListener() {
        myStartActivity(VipActivity.class);

    }

    /**
     * 编辑资料
     */
    @OnClick(R.id.me_edit_button)
    void selfEditListener() {
        myStartActivity(DataEdit.class);
    }

    /**
     * 设置
     */
    @OnClick(R.id.me_setting_Layout)
    void settingListener() {
        Intent intent = new Intent(context, SettingActivity.class);
        context.startActivity(intent);
    }

    /**
     * 关于美盟
     */
    @OnClick(R.id.me_aboutmeimeng_Layout)
    void aboutmeimengListener() {
        myStartActivity(AboutMeimeng.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AllFragmentManagement.fragmentList.remove(this);
        Log.e("Fragment", "MeFragment Destroy");
        context.unregisterReceiver(broadcastReceiver);
    }

}
