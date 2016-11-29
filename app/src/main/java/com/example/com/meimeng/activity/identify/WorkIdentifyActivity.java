package com.example.com.meimeng.activity.identify;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.com.meimeng.MeiMengApplication;
import com.example.com.meimeng.R;
import com.example.com.meimeng.activity.BaseActivity;
import com.example.com.meimeng.bean.ProgressBean;
import com.example.com.meimeng.constants.CommonConstants;
import com.example.com.meimeng.custom.RoundCornerImageView;
import com.example.com.meimeng.dialog.SelectPictureDialog;
import com.example.com.meimeng.fragment.SelectPictureFragment2;
import com.example.com.meimeng.gson.GsonTools;
import com.example.com.meimeng.gson.gsonbean.BaseBean;
import com.example.com.meimeng.gson.gsonbean.CertificateStatusBean;
import com.example.com.meimeng.gson.gsonbean.UploadPictureBean;
import com.example.com.meimeng.net.InternetConstant;
import com.example.com.meimeng.net.InternetUtils;
import com.example.com.meimeng.util.DialogUtils;
import com.example.com.meimeng.util.ImageUtil;
import com.example.com.meimeng.util.Utils;

import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by 010 on 2015/7/17.
 * 工作认证界面
 */
public class WorkIdentifyActivity extends BaseActivity{
    private static final String TAG = "WorkIdentifyActivity";

    @Bind(R.id.bg_example)
    RoundCornerImageView mBgExample;
    @Bind(R.id.is_show)
    RelativeLayout mIsShow;
    /**
     * 认证通过
     */
    private static final Integer HASCERTIFICATE = 1;
    /**
     * 认证不通过
     */
    private static final Integer UNCERTIFICATE = 2;


    /**
     * 记录是点击了哪个按钮，1,2
     */
    private int whichbutton = 0;
    @Bind(R.id.title_text)
    TextView titleText;

    @Bind(R.id.title_left_arrow_image_view)
    ImageView leftArrowImageView;

    @Bind(R.id.title_sure_text)
    TextView sureText;

    @Bind(R.id.bow_arrow_image_view)
    ImageView bowArrowImageView;


    @Bind(R.id.describe_image_view)
    ImageView describeImageView;



    @Bind(R.id.describe_2_text)
    TextView describe2Text;

    @Bind(R.id.describe_3_text)
    TextView describe3Text;
    @Bind(R.id.describe_house_text)
    TextView describehouseText;

    private ImageView flagImageView;
    @Bind(R.id.bg_frist)
    ImageView bg_first;
    @Bind(R.id.bg_second)
    ImageView bg_second;
    @Bind(R.id.bg_third)
    ImageView bg_third;



    private BaseBean basebeanJson;



    //选择图片的弹出框
    private SelectPictureDialog selectPictureDialog;

    //相机剪切图片的请求码
    private final int CAMEAR_CROP_PICTURE = 31;


    /**
     * 分别记录三张图片对应的id
     */
    @Bind(R.id.upload_identify_layout)
    RelativeLayout uploadLayout;

    @Bind(R.id.upload_house_identify_layout)
    RelativeLayout houseLayout;

    @Bind(R.id.upload_car_identify_layout)
    RelativeLayout carLayout;

    @Bind(R.id.main_dialog_layout)
    LinearLayout mainDialogLayout;

    @Bind(R.id.identify_upload_image_view)
    ImageView uploadImage;//上传收入证明按钮的图片
    @Bind(R.id.upload_house_image_view)
    ImageView uploadHouseImage;//上传房子证明按钮的图片
    @Bind(R.id.upload_car_image_view)
    ImageView uploadCarImage;//上传汽车证明按钮的图片

    //上传图片时的对话框
    private Dialog dialog;

    //进度条的文本
    private TextView progressTextView;
    @Bind(R.id.describe_car_text)
    TextView describecarText;

    private SelectPictureFragment2 selectPictureFragment;

    private HashMap<String, Long> picMap = new HashMap<>();

    private ArrayList<Long> picIdList=new ArrayList<>();

    private static final String MONEY_IDENTIFY = "money_identify";

    /**
     * 分别记录三张图片对应的id
     */
    private long picId_0 = 0;
    private long picId_1 = 0;
    private long picId_2 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.identify_user);
        ButterKnife.bind(this);

        describeImageView.setImageResource(R.drawable.icon_occupation);
//        describe1Text.setText("职位认证");
        describe2Text.setText("请点击上传本人名片或者职位证明。您上传的资料将经过系统加密处理，保障个人信息安全，请放心上传。");
        describe3Text.setText("点击上传");

        titleText.setText("职位认证");
        leftArrowImageView.setVisibility(View.VISIBLE);
        leftArrowImageView.setVisibility(View.VISIBLE);
        bowArrowImageView.setVisibility(View.GONE);
        houseLayout.setVisibility(View.VISIBLE);
        Utils.readBitMap(WorkIdentifyActivity.this, mBgExample, R.raw.identify_work);

        initImageView();
    }


    /**
     * 初始化上传照片部分的界面显示
     */
    private void initImageView() {
        //如果有网
        if (InternetUtils.isNetworkConnected(WorkIdentifyActivity.this)) {

            try {
                //获取用户的uid,和token
                if (TextUtils.isEmpty(Utils.getUserId())) {
                    return;
                }
                if (TextUtils.isEmpty( Utils.getUserToken() )) {
                    return;
                }
                String url = InternetConstant.SERVER_URL + InternetConstant.CERTIFICATE_GETSTATUS + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();
                JSONStringer stringer = new JSONStringer().object()
                        .key("targetUid").value(Utils.getUserId())
                        .endObject();
                String jsonStr = stringer.toString();

                Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
                observable.observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1() {
                            @Override
                            public void call(Object o) {
                                CertificateStatusBean certificateStatusBean = GsonTools.getCertificateStatusBean((String) o);
                                if (certificateStatusBean.isSuccess()) {
                                    initWedget(certificateStatusBean);
                                } else {
                                    DialogUtils.setDialog(WorkIdentifyActivity.this, certificateStatusBean.getError());
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
    }

    /**
     * 调用接口后的初始化界面
     *
     * @param certificateStatusBean
     */
    private void initWedget(CertificateStatusBean certificateStatusBean) {

        if (certificateStatusBean.getParam().getStatus().getJobPicId() != null) {
            //已经上传了照片，正在审核中，也不能再上传照片了，点击无效
            List<Long> pics=certificateStatusBean.getParam().getStatus().getJobPicId() ;
            int state=certificateStatusBean.getParam().getStatus().getIdeStatus();
            if (pics.size()!=0&&state!=HASCERTIFICATE&&state!=UNCERTIFICATE) {
                for (int i = 0; i <pics.size(); i++) {
                    picIdList.add(pics.get(i));
                }
            }else {
                sureText.setVisibility(View.VISIBLE);
            }
            //判断有几张照片
            switch (certificateStatusBean.getParam().getStatus().getJobPicId().size()) {
                case 1:
                    sureText.setVisibility(View.VISIBLE);
                    mIsShow.setVisibility(View.GONE);
                    uploadLayout.setVisibility(View.VISIBLE);
                    if (certificateStatusBean.getParam().getStatus().getJobStatus() == HASCERTIFICATE) {
                        describe3Text.setText("审核通过");
                        //认证通过了就不能再上传，点击无效
                        setButton(certificateStatusBean);
                        picId_0 = certificateStatusBean.getParam().getStatus().getJobPicId().get(0);
                        InternetUtils.getPicIntoView(300, 100, bg_first, picId_0);
                        describe3Text.setTextColor(getResources().getColor(R.color.white_text_color));
                        uploadImage.setVisibility(View.INVISIBLE);
                    } else if (certificateStatusBean.getParam().getStatus().getJobStatus() == UNCERTIFICATE) {
                        describe3Text.setText("认证失败，请重新上传");
                        picId_0 = certificateStatusBean.getParam().getStatus().getJobPicId().get(0);
                        InternetUtils.getPicIntoView(300, 100, bg_first, picId_0);
                        uploadImage.setVisibility(View.INVISIBLE);
                    } else {
                        uploadLayout.setClickable(false);
                        picId_0 = certificateStatusBean.getParam().getStatus().getJobPicId().get(0);
                        InternetUtils.getPicIntoView(300, 100, bg_first, picId_0);
                        describe3Text.setTextColor(getResources().getColor(R.color.white_text_color));
                        uploadImage.setVisibility(View.INVISIBLE);
                        describe3Text.setText("上传成功，正在审核");
                    }
                    break;
                case 2:
                    sureText.setVisibility(View.VISIBLE);
                    mIsShow.setVisibility(View.GONE);
                    uploadLayout.setVisibility(View.VISIBLE);
                    uploadImage.setVisibility(View.INVISIBLE);
                    uploadHouseImage.setVisibility(View.INVISIBLE);
                    if (certificateStatusBean.getParam().getStatus().getJobStatus() == HASCERTIFICATE) {
                        describe3Text.setText("审核通过");
                        describehouseText.setText("审核通过");
                        //认证通过了就不能再上传，点击无效
                        setButton(certificateStatusBean);
                        picId_0 = certificateStatusBean.getParam().getStatus().getJobPicId().get(0);
                        picId_1 = certificateStatusBean.getParam().getStatus().getJobPicId().get(1);
                        InternetUtils.getPicIntoView(300, 100, bg_first, picId_0);
                        InternetUtils.getPicIntoView(300, 100, bg_second, picId_1);
                        describe3Text.setTextColor(getResources().getColor(R.color.white_text_color));
                        describehouseText.setTextColor(getResources().getColor(R.color.white_text_color));
                    } else if (certificateStatusBean.getParam().getStatus().getJobStatus() == UNCERTIFICATE) {
                        describe3Text.setText("认证失败，请重新上传");
                        describehouseText.setText("认证失败，请重新上传");
                        picId_0 = certificateStatusBean.getParam().getStatus().getJobPicId().get(0);
                        picId_1 = certificateStatusBean.getParam().getStatus().getJobPicId().get(1);
                        InternetUtils.getPicIntoView(300, 100, bg_first, picId_0);
                        InternetUtils.getPicIntoView(300, 100, bg_second, picId_1);
                    } else {
                        uploadLayout.setClickable(false);
                        houseLayout.setClickable(false);
                        picId_0 = certificateStatusBean.getParam().getStatus().getJobPicId().get(0);
                        picId_1 = certificateStatusBean.getParam().getStatus().getJobPicId().get(1);
                        InternetUtils.getPicIntoView(300, 100, bg_first, picId_0);
                        InternetUtils.getPicIntoView(300, 100, bg_second, picId_1);
                        describe3Text.setTextColor(getResources().getColor(R.color.white_text_color));
                        describe3Text.setText("上传成功，正在审核");
                        describehouseText.setTextColor(getResources().getColor(R.color.white_text_color));
                        describehouseText.setText("上传成功，正在审核");
                    }
                    break;
                default:
                    mIsShow.setVisibility(View.GONE);
                    uploadLayout.setVisibility(View.VISIBLE);
                    uploadCarImage.setVisibility(View.INVISIBLE);
                    uploadHouseImage.setVisibility(View.INVISIBLE);
                    uploadImage.setVisibility(View.INVISIBLE);
                    if (certificateStatusBean.getParam().getStatus().getJobStatus() == HASCERTIFICATE) {
                        uploadLayout.setClickable(false);
                        houseLayout.setClickable(false);
                        carLayout.setClickable(false);
                        describe3Text.setText("审核通过");
                        describehouseText.setText("审核通过");
                        describecarText.setText("审核通过");
                        //认证通过了就不能再上传，点击无效
                        setButton(certificateStatusBean);
                        describe3Text.setTextColor(getResources().getColor(R.color.white_text_color));
                        describehouseText.setTextColor(getResources().getColor(R.color.white_text_color));
                        describecarText.setTextColor(getResources().getColor(R.color.white_text_color));
                        picId_0 = certificateStatusBean.getParam().getStatus().getJobPicId().get(0);
                        picId_1 = certificateStatusBean.getParam().getStatus().getJobPicId().get(1);
                        picId_2 = certificateStatusBean.getParam().getStatus().getJobPicId().get(2);
                        InternetUtils.getPicIntoView(300, 100, bg_first, picId_0);
                        InternetUtils.getPicIntoView(300, 100, bg_second, picId_1);
                        InternetUtils.getPicIntoView(300, 100, bg_third, picId_2);
                        uploadCarImage.setVisibility(View.INVISIBLE);
                    } else if (certificateStatusBean.getParam().getStatus().getJobStatus() == UNCERTIFICATE) {
                        describe3Text.setText("认证失败，请重新上传");
                        describehouseText.setText("认证失败，请重新上传");
                        describecarText.setText("认证失败，请重新上传");
                        picId_0 = certificateStatusBean.getParam().getStatus().getJobPicId().get(0);
                        picId_1 = certificateStatusBean.getParam().getStatus().getJobPicId().get(1);
                        picId_2 = certificateStatusBean.getParam().getStatus().getJobPicId().get(2);
                        InternetUtils.getPicIntoView(300, 100, bg_first, picId_0);
                        InternetUtils.getPicIntoView(300, 100, bg_second, picId_1);
                        InternetUtils.getPicIntoView(300, 100, bg_third, picId_2);
                        uploadCarImage.setVisibility(View.INVISIBLE);
                    } else {
                        uploadLayout.setClickable(false);
                        houseLayout.setClickable(false);
                        carLayout.setClickable(false);
                        picId_0 = certificateStatusBean.getParam().getStatus().getJobPicId().get(0);
                        picId_1 = certificateStatusBean.getParam().getStatus().getJobPicId().get(1);
                        picId_2 = certificateStatusBean.getParam().getStatus().getJobPicId().get(2);
                        InternetUtils.getPicIntoView(300, 100, bg_first, picId_0);
                        InternetUtils.getPicIntoView(300, 100, bg_second, picId_1);
                        InternetUtils.getPicIntoView(300, 100, bg_third, picId_2);
                        describe3Text.setTextColor(getResources().getColor(R.color.white_text_color));
                        describehouseText.setTextColor(getResources().getColor(R.color.white_text_color));
                        describecarText.setTextColor(getResources().getColor(R.color.white_text_color));
                        describe3Text.setText("上传成功，正在审核");
                        describehouseText.setText("上传成功，正在审核");
                        describecarText.setText("上传成功，正在审核");
                    }
                    break;
            }


        }
    }

    /**
     * 设置认证控件点击状态
     */
    private void setButton(CertificateStatusBean certificateStatusBean) {
        switch (certificateStatusBean.getParam().getStatus().getPropertyPicId().size()) {
            case 1:
                uploadLayout.setClickable(false);
                break;
            case 2:
                uploadLayout.setClickable(false);
                houseLayout.setClickable(false);
                break;
            case 3:
                uploadLayout.setClickable(false);
                houseLayout.setClickable(false);
                carLayout.setClickable(false);
                break;
            default:
                break;
        }
    }

    //确定按钮
    @OnClick(R.id.title_sure_text)
    void sureListener() {
        if(picIdList.size()!=0){
            identifyWork(picIdList);
        }
    }

    /**
     * 上传失败的展示
     */

    private void displayAfterUploadfailed() {

        if (null != dialog) {
            dialog.dismiss();
            dialog = null;
        }

        switch (whichbutton) {
            case 0:
                describe3Text.setText("上传失败，请重新上传");
                describe3Text.setTextColor(getResources().getColor(R.color.text_dark));
                uploadImage.setVisibility(View.INVISIBLE);
                break;
            case 1:
                describehouseText.setText("上传失败，请重新上传");
                describehouseText.setTextColor(getResources().getColor(R.color.text_dark));
                uploadHouseImage.setVisibility(View.INVISIBLE);
                break;
            case 2:
                describecarText.setText("上传失败，请重新上传");
                describecarText.setTextColor(getResources().getColor(R.color.text_dark));
                uploadCarImage.setVisibility(View.INVISIBLE);
                break;
            default:
                break;
        }
    }

    /**
     * 上传成功的显示
     */
    private void displayAfterUploadsucceed() {
        if (null != dialog) {
            dialog.dismiss();
            dialog = null;
        }
        switch (whichbutton) {
            case 0:
                describe3Text.setText("上传成功，正在审核");
                describe3Text.setTextColor(getResources().getColor(R.color.white_text_color));
                uploadImage.setVisibility(View.INVISIBLE);
                uploadLayout.setClickable(false);
                break;
            case 1:
                describehouseText.setText("上传成功，正在审核");
                describehouseText.setTextColor(getResources().getColor(R.color.white_text_color));
                uploadHouseImage.setVisibility(View.INVISIBLE);
                houseLayout.setClickable(false);
                break;
            case 2:
                describecarText.setText("上传成功，正在审核");
                describecarText.setTextColor(getResources().getColor(R.color.white_text_color));
                uploadCarImage.setVisibility(View.INVISIBLE);
                carLayout.setClickable(false);
                break;
            default:
                break;
        }
    }

    //返回按钮
    @OnClick(R.id.title_left_arrow_layout)
    void leftArrowListener() {
        onBackPressed();
    }

    //证明按钮1
    @OnClick(R.id.upload_identify_layout)
    void uploadMoneyListener() {
        flagImageView = bg_first;
        whichbutton = 0;
        setSelectPictureDialog();
    }

    //证明按钮2
    @OnClick(R.id.upload_house_identify_layout)
    void uploadHouseListener() {
        if (mIsShow.getVisibility()==View.VISIBLE) {
            flagImageView = bg_first;
            whichbutton = 0;
            setSelectPictureDialog();
            mIsShow.setVisibility(View.GONE);
            uploadLayout.setVisibility(View.VISIBLE);
        }else {
            flagImageView = bg_second;
            whichbutton = 1;
            setSelectPictureDialog();
        }
    }

    //证明按钮3
    @OnClick(R.id.upload_car_identify_layout)
    void uploadCarListener() {
        if (mIsShow.getVisibility()==View.VISIBLE) {
            flagImageView = bg_first;
            whichbutton = 0;
            setSelectPictureDialog();
            mIsShow.setVisibility(View.GONE);
            uploadLayout.setVisibility(View.VISIBLE);
        }else {
            flagImageView = bg_third;
            whichbutton = 2;
            setSelectPictureDialog();
        }
    }




    /**
     * 工作认证
     * @param pidList
     */
    public void identifyWork(ArrayList<Long> pidList) {
        try {
            //获取用户的uid,和token
            if (TextUtils.isEmpty( Utils.getUserId() )) {
                return;
            }
            if (TextUtils.isEmpty( Utils.getUserToken() )) {
                return;
            }
            String url = InternetConstant.SERVER_URL + InternetConstant.CERTIFICATE_EDIT + "?uid=" + Utils.getUserId()  + "&token=" + Utils.getUserToken();



            JSONStringer jsonStringer = new JSONStringer().object()
                    .key("jobPicId")
                    .array();
            for (int i = 0; i < pidList.size(); i++) {
                jsonStringer.value(pidList.get(i));
            }
            jsonStringer .endArray().endObject();
            String jsonStr = jsonStringer.toString();
            //得到Observable并获取返回的数据(主线程中)
            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            basebeanJson = GsonTools.getBaseReqBean(s);
                            if (basebeanJson.isSuccess()) {
                                displayAfterUploadsucceed();
                                onBackPressed();
                            } else {
                                displayAfterUploadfailed();
                                if (basebeanJson.getError().equals("未登录")) {
                                    DialogUtils.setDialog(WorkIdentifyActivity.this, basebeanJson.getError());
                                }
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






    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
    }

    /**
     * 设置图片选择框
     */
    private void setSelectPictureDialog() {
        if (selectPictureDialog == null) {
            selectPictureDialog = new SelectPictureDialog(WorkIdentifyActivity.this, R.style.loading_dialog);
        }
        selectPictureDialog.show();
        selectPictureDialog.setCancelable(false);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = selectPictureDialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()); //设置宽度
        selectPictureDialog.getWindow().setAttributes(lp);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            switch (requestCode) {
                //相册
                case SelectPictureDialog.CHOOSE_BIG_PICTURE:
                    String path = ImageUtil.getPath(WorkIdentifyActivity.this, SelectPictureDialog.imageUri);
                    Bitmap bitmap = ImageUtil.getBitmapFromPath(path);
                    flagImageView.setImageBitmap(bitmap);


                    uploadPictureListener(path);
                    break;
                //相机
                case SelectPictureDialog.SELECT_CAMERA_CODE:
                    cropImageUri(SelectPictureDialog.cameraUri, 1300, 1300);
                    break;
                //剪切相机的图片
                case CAMEAR_CROP_PICTURE:
                    String path2 = ImageUtil.getPath(WorkIdentifyActivity.this, SelectPictureDialog.cameraUri);
                    Bitmap bitmap2 = ImageUtil.getBitmapFromPath(path2);
                    flagImageView.setImageBitmap(bitmap2);

                    uploadPictureListener(path2);
                    break;
            }
        }
        if (selectPictureDialog != null) {
            selectPictureDialog.dismiss();
        }
    }


    /**
     * 设置图片的显示
     * @param pid
     */
    private void setAfterUploadsucceed(long pid) {
        sureText.setVisibility(View.VISIBLE);
        switch (whichbutton) {
            case 0:
                if (picIdList.size()>0) {
                    picIdList.add(0,pid);
                }else {
                    picIdList.add(pid);
                }
                describe3Text.setVisibility(View.INVISIBLE);
                uploadImage.setVisibility(View.INVISIBLE);
                break;
            case 1:
                if (picIdList.size()>1) {
                    picIdList.add(1,pid);
                }else {
                    picIdList.add(pid);
                }
                describehouseText.setVisibility(View.INVISIBLE);
                uploadHouseImage.setVisibility(View.INVISIBLE);
                break;
            case 2:
                if (picIdList.size()>2) {
                    picIdList.add(2,pid);
                }else {
                    picIdList.add(pid);
                }
                describecarText.setVisibility(View.INVISIBLE);
                uploadCarImage.setVisibility(View.INVISIBLE);
                break;
            default:
                break;
        }
    }

    /**
     * 上传图片
     *
     * @param path
     */
    private void uploadPictureListener(String path) {
        if (TextUtils.isEmpty(Utils.getUserId())) {
            return;
        }

        SharedPreferences.Editor editor = MeiMengApplication.sharedPreferences.edit();
        editor.putString(CommonConstants.UPLOAD_PIC_PATH, path);
        editor.commit();

        Log.e("SelectPicture", "-------上传图片");

        //显示进度条（刚开始上传）
        ProgressBean proBean = DialogUtils.createLoadingDialog2(this, "正在上传...");
        progressTextView = proBean.getTextView();
        dialog = proBean.getDialog();
        dialog.show();

//        ((OnSelectPictureDialogListener) context).getPicturePath(path);
        Observable observable = InternetUtils.getUploadPictureObservale(Utils.getUserId(), path, handler);

        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1() {
                    @Override
                    public void call(Object o) {
                        UploadPictureBean bean = GsonTools.getUploadPhototBean((String) o);
                        if (bean.isSuccess()) {
                            long pid = bean.getParam().getPid();
                            setAfterUploadsucceed(pid);
                            //取消进度条
                            if (null != dialog)
                                dialog.dismiss();

                        } else {
                            DialogUtils.setDialog(WorkIdentifyActivity.this, bean.getError());
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e("Select", throwable.getMessage());
//                        ((OnSelectPictureDialogListener) context).cancelDialog2();
                    }
                });
//        ((OnSelectPictureDialogListener) context).cancelDialog();
    }

    //用于将上传图片的进度传给主线程
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.e("++++++++++", "------------" + msg.obj);

            //显示上传进度
            if (progressTextView != null) {
                progressTextView.setText(msg.obj + "");

            }
        }
    };

    /**
     * 剪切图片
     *
     * @param uri
     * @param outputX
     * @param outputY
     */
    private void cropImageUri(Uri uri, int outputX, int outputY) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");

        //通知剪切图片
        intent.putExtra("crop", true);

        //X方向的比例
        intent.putExtra("aspectX", 1);

        //Y方向的比例
        intent.putExtra("aspectY", 1);

        //裁剪区的宽度
        intent.putExtra("outputX", outputX);

        //裁剪区的高度
        intent.putExtra("outputY", outputY);

        //按照比例裁剪
        intent.putExtra("scale", true);

        //裁剪后的图片存放在uri中
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

        //裁剪后的图片不以Bitmap的形式返回
        intent.putExtra("return-data", false);

        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

        intent.putExtra("noFaceDetection", true);

        startActivityForResult(intent, CAMEAR_CROP_PICTURE);
    }
}
