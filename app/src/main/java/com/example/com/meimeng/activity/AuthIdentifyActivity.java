package com.example.com.meimeng.activity;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.com.meimeng.MeiMengApplication;
import com.example.com.meimeng.R;
import com.example.com.meimeng.bean.ProgressBean;
import com.example.com.meimeng.bean.RegisterFirstInfoBean;
import com.example.com.meimeng.constants.CommonConstants;
import com.example.com.meimeng.custom.RoundCornerImageView;
import com.example.com.meimeng.custom.ThreeWheelView;
import com.example.com.meimeng.custom.WheelView;
import com.example.com.meimeng.db.DbOpenHelper;
import com.example.com.meimeng.dialog.SelectPictureDialog;
import com.example.com.meimeng.fragment.SelectPictureFragment;
import com.example.com.meimeng.gson.GsonTools;
import com.example.com.meimeng.gson.gsonbean.FirstInfoEditBean;
import com.example.com.meimeng.gson.gsonbean.UploadPictureBean;
import com.example.com.meimeng.net.InternetConstant;
import com.example.com.meimeng.net.InternetUtils;
import com.example.com.meimeng.util.DialogUtils;
import com.example.com.meimeng.util.GetDbUtils;
import com.example.com.meimeng.util.Utils;

import org.json.JSONStringer;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class AuthIdentifyActivity extends BaseActivity implements SelectPictureFragment.OnSelectPictureDialogListener{


    private final static String TAG = "AuthIdentifyActivity";
    @Bind(R.id.title_text)
    TextView titleText;

    @Bind(R.id.title_left_arrow_image_view)
    ImageView leftArrowImageView;

    @Bind(R.id.bow_arrow_image_view)
    ImageView bowArrowImageView;

    private HashMap<String, Integer> nationHashMap = new HashMap<>();

    private Dialog dialog_c;
    private RelativeLayout select_rl;
    private TextView user_birthday;
    private TextView user_marryStatus;
    private TextView userHeight;
    private TextView userSlary;
    private TextView describe_3_text;
    private ImageView identifyImageView;
    private ImageView addIdentifyImageView;
    //用户第一次编辑资料的信息
    private RegisterFirstInfoBean registerFirstInfoBean;

    private int firstOrSelfType = 0;
    //图片的类型（头像/认证图片）
    private int pictureType = 0;

    private Dialog mdialog;
    private Dialog dialog;
    //进度条的文本
    private TextView progressTextView;

    //相机剪切图片的请求码
    private final int CAMEAR_CROP_PICTURE = 31;

    private boolean isBack=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_auth_identify);
        ButterKnife.bind(this);
        titleText.setText("身份认证");
        leftArrowImageView.setVisibility(View.VISIBLE);
        bowArrowImageView.setVisibility(View.GONE);
        leftArrowImageView.setOnClickListener(nClickListener);
        registerFirstInfoBean=MeiMengApplication.registerFirstInfoBean;
        initView();
    }

    private void initView() {


        user_birthday = (TextView) findViewById(R.id.user_birthday);
        userHeight = (TextView) findViewById(R.id.user_height);
        userSlary = (TextView) findViewById(R.id.user_income);
        user_marryStatus = (TextView) findViewById(R.id.user_marry);
        describe_3_text = (TextView) findViewById(R.id.describe_3_text);
        RelativeLayout personHeight = (RelativeLayout) findViewById(R.id.person_height_layout);
        EditText firstEdit = (EditText) findViewById(R.id.first_name_edit);
        EditText lastEdit = (EditText) findViewById(R.id.last_name_edit);

        RoundCornerImageView bg_example= (RoundCornerImageView) findViewById(R.id.bg_example);
        identifyImageView = (ImageView) findViewById(R.id.identify_upload_picture_image_view);
        addIdentifyImageView = (ImageView) findViewById(R.id.identify_upload_image_view);

        RelativeLayout identifyLayout = (RelativeLayout) findViewById(R.id.linearlayout_first);

        Utils.readBitMap(AuthIdentifyActivity.this, bg_example,R.raw.identify_user);
        //初始化
        if (registerFirstInfoBean != null) {

            if (!TextUtils.isEmpty(registerFirstInfoBean.getFirstName())) {
                firstEdit.setText(registerFirstInfoBean.getFirstName());
            }

            if (!TextUtils.isEmpty(registerFirstInfoBean.getLastName())) {
                lastEdit.setText(registerFirstInfoBean.getLastName());
            }

            if (!TextUtils.isEmpty(registerFirstInfoBean.getBirthday())) {
                user_birthday.setText(registerFirstInfoBean.getBirthday());
            }

            if (registerFirstInfoBean.getHeight() != null&&registerFirstInfoBean.getHeight()!=0) {
                userHeight.setText(registerFirstInfoBean.getHeight() + "CM");
            }
           /* if (registerFirstInfoBean.getHeight() != null&&registerFirstInfoBean.getHeight()!=0) {
                user_marryStatus.setText();
            }*/
            if (registerFirstInfoBean.getYearIncome() != 0) {
                String income = null;
                switch (registerFirstInfoBean.getYearIncome()) {
                    case 1:
                        income = "30万以下";
                        break;
                    case 2:
                        income = "30-50万";
                        break;
                    case 3:
                        income = "50-100万";
                        break;
                    case 4:
                        income = "100-300万";
                        break;
                    case 5:
                        income = "300-500万";
                        break;
                    case 6:
                        income = "500-1000万";
                        break;
                    case 7:
                        income = "1000万以上";
                        break;
                    default:
                        break;
                }
                userSlary.setText(income);
            }
            if (registerFirstInfoBean.getIdentifyPicIdList() != null&&registerFirstInfoBean.getIdentifyPicIdList().size()!=0) {
                if (registerFirstInfoBean.getIdentifyPicIdList().get(0) != null) {
                    describe_3_text.setVisibility(View.VISIBLE);
                    if (registerFirstInfoBean.getIdeStatus() == 1) {//通过审核
                        Long headerPic = registerFirstInfoBean.getIdentifyPicIdList().get(0);
                        InternetUtils.getPicIntoView(100, 100, identifyImageView, headerPic);
                        identifyImageView.setVisibility(View.VISIBLE);
                        describe_3_text.setText("已认证");
                        identifyLayout.setEnabled(false);
                    }else if(registerFirstInfoBean.getIdeStatus() == 2){//未通过审核
                        Long headerPic = registerFirstInfoBean.getIdentifyPicIdList().get(0);
                        InternetUtils.getPicIntoView(100, 100, identifyImageView, headerPic);
                        identifyImageView.setVisibility(View.VISIBLE);
                    }else{//正在审核中
                        Long headerPic = registerFirstInfoBean.getIdentifyPicIdList().get(0);
                        InternetUtils.getPicIntoView(100, 100, identifyImageView, headerPic);
                        identifyImageView.setVisibility(View.VISIBLE);
                        describe_3_text.setText("待审核");
                        identifyLayout.setEnabled(false);
                    }
                }
            }

        }

        identifyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPictureDialog();

                pictureType = 1;
            }
        });

        //选择生日
        user_birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDialog_c(0);
            }
        });

        //选择身高
        personHeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDialog_c(1);
            }
        });
        //选择年收入
        userSlary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDialog_c(2);
            }
        });
        user_marryStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDialog_c(3);
            }
        });


        firstEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String firstName = String.valueOf(s);
                registerFirstInfoBean.setFirstName(firstName);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        lastEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String lastName = String.valueOf(s);
                registerFirstInfoBean.setLastName(lastName);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        TextView submitText = (TextView) findViewById(R.id.submit_first_info_text);
        submitText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    boolean b = checkFirstInfo(registerFirstInfoBean);
                    if (!b) {
                        return;
                    }else{
                        if (mdialog == null) {
                            mdialog = Utils.createLoadingDialog(AuthIdentifyActivity.this, "");
                        }
                        mdialog.show();
                    }
                    Long uidKey = MeiMengApplication.sharedPreferences.getLong(CommonConstants.USER_ID, 1l);
                    String token = MeiMengApplication.sharedPreferences.getString(CommonConstants.USER_TOKEN, "");


                    String url = InternetConstant.SERVER_URL + InternetConstant.FIRST_INFO_EDIT + "?uid=" + uidKey + "&token=" + token;
                    JSONStringer jsonStringer = new JSONStringer().object()
                            .key("firstName").value(registerFirstInfoBean.getFirstName())
                            .key("lastName").value(registerFirstInfoBean.getLastName())
                            .key("birthday").value(registerFirstInfoBean.getBirthday() + " 00:00:00")
                            .key("height").value(registerFirstInfoBean.getHeight())
                            .key("maritalStatus").value(registerFirstInfoBean.getMaritalStatus())
                            .key("yearIncome").value(registerFirstInfoBean.getYearIncome())
                            .key("identityPicId").array().value(registerFirstInfoBean.getIdentifyPicIdList().get(0))
                            .endArray()
                            .endObject();
                    Observable observable = InternetUtils.getJsonObservale(url, jsonStringer.toString());
                    observable.observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Action1<String>() {
                                @Override
                                public void call(String s) {

                                    if (mdialog != null) {
                                        mdialog.dismiss();
                                    }
                                    FirstInfoEditBean firstInfoEditBean = GsonTools.getFirstInfoEditBean(s);
                                    if (firstInfoEditBean.isSuccess()) {
                                        Toast.makeText(AuthIdentifyActivity.this, "提交成功", Toast.LENGTH_LONG).show();
                                        SharedPreferences.Editor editor = MeiMengApplication.sharedPreferences.edit();
                                        //2:用户资料审核中
                                        editor.putBoolean(CommonConstants.MYSTATE, true);
                                        editor.commit();
                                        isBack=true;
                                        finish();
                                    }else {
                                        Toast.makeText(AuthIdentifyActivity.this, "提交失败", Toast.LENGTH_LONG).show();
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
        });

    }


    /**
     * 检查信息是否输入完整
     *
     * @param registerFirstInfoBean
     * @return
     */
    private boolean checkFirstInfo(RegisterFirstInfoBean registerFirstInfoBean) {
        String errorMessage = "error";
        if (TextUtils.isEmpty(registerFirstInfoBean.getFirstName())) {
            errorMessage = "请输入姓";
        } else if (TextUtils.isEmpty(registerFirstInfoBean.getLastName())) {
            errorMessage = "请输入名";
        } else if (registerFirstInfoBean.getHeight() ==0) {
            errorMessage = "请输入身高";
        } else if (TextUtils.isEmpty(registerFirstInfoBean.getBirthday())) {
            errorMessage = "请输入生日";
        }else if (registerFirstInfoBean.getYearIncome() == 0) {
            errorMessage = "请输入年收入";
        } else if (registerFirstInfoBean.getIdentifyPicIdList() == null||registerFirstInfoBean.getIdentifyPicIdList().size()==0) {
            errorMessage = "请上传认证图片";
        } else {
            return true;
        }
        Toast.makeText(AuthIdentifyActivity.this, errorMessage, Toast.LENGTH_LONG).show();

        return false;
    }

    //选择图片的dialog
    private SelectPictureDialog selectPictureDialog;

    /**
     * 选择图片的dialog
     */
    private void setPictureDialog() {
        if (selectPictureDialog == null) {
            selectPictureDialog = new SelectPictureDialog(AuthIdentifyActivity.this, R.style.loading_dialog);
        }
        selectPictureDialog.show();
        selectPictureDialog.setCancelable(false);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = selectPictureDialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()); //设置宽度
        selectPictureDialog.getWindow().setAttributes(lp);
    }

    /**
     * 设置Dialog_c
     * 邓成博修改部分
     */
    private void setDialog_c(int type) {

        View view = LayoutInflater.from(this).inflate(R.layout.birthday_register_layout, null);
        dialog_c = new Dialog(this, R.style.loading_dialog);
        setDatatoView_c(view, type);
        dialog_c.setContentView(view);
        dialog_c.show();
        dialog_c.setCancelable(false);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog_c.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()); //设置宽度
        dialog_c.getWindow().setAttributes(lp);
    }


    /**
     * 设置身高数据
     *
     * @param personHeightWheelView
     */
    private void setHeightData(WheelView personHeightWheelView) {
        ArrayList<String> heightList = new ArrayList<>();
        for (int i = 150; i < 221; i++) {
            heightList.add(String.valueOf(i) + "CM");
        }
        personHeightWheelView.setData(heightList);
        personHeightWheelView.setDefault(0);
    }

    /**
     * 显示选择器
     * 邓成博修改部分
     */
    private void showSelectDialog(int num) {

        if (!(select_rl.getVisibility() == View.VISIBLE)) {
            select_rl.setVisibility(View.VISIBLE);
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_arrow_up);
            select_rl.setAnimation(animation);
        }

    }


    private void setDatatoView_c(View view, final int type) {
        titleText = (TextView) view.findViewById(R.id.birthday_register_title_text);
        TextView cancel = (TextView) view.findViewById(R.id.birthday_register_cancel_text);
        TextView sure = (TextView) view.findViewById(R.id.birthday_register_sure_text);
        final ThreeWheelView birthdayView = (ThreeWheelView) view.findViewById(R.id.birthday_register_wheel_picker);
        final WheelView personHeightWheelView = (WheelView) view.findViewById(R.id.person_height_wheel_view);

        //生日
        if (type == 0) {
            personHeightWheelView.setVisibility(View.GONE);
            birthdayView.setVisibility(View.VISIBLE);
            titleText.setText("生日");
        } else if (type == 2) {
            ArrayList<String> yearList = getNationList(2);
            personHeightWheelView.setData(yearList);
            personHeightWheelView.setDefault(0);
            personHeightWheelView.setVisibility(View.VISIBLE);
            birthdayView.setVisibility(View.GONE);
            titleText.setText("年收入");
        }else if (type == 3) {
            personHeightWheelView.setData(getMarryData());
            personHeightWheelView.setDefault(0);
            personHeightWheelView.setVisibility(View.VISIBLE);
            birthdayView.setVisibility(View.GONE);
            titleText.setText("婚姻状况");
        }
        //身高
        else {
            //初始化身高数据
            setHeightData(personHeightWheelView);
            personHeightWheelView.setDefault(20);
            personHeightWheelView.setVisibility(View.VISIBLE);
            birthdayView.setVisibility(View.GONE);
            titleText.setText("身高");
        }

        select_rl = (RelativeLayout) view.findViewById(R.id.select_layout);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_c.dismiss();
            }
        });
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //生日
                if (type == 0) {
                    String birthday = birthdayView.getData();
                    user_birthday.setText(birthday);
                    registerFirstInfoBean.setBirthday(birthday);

                } else if (type == 2) {
                    String income = personHeightWheelView.getSelectedText();
                    userSlary.setText(income);
                    int flag = 0;
                    if (income.equals("30万以下")) {
                        flag = 1;
                    } else if (income.equals("30-50万")) {
                        flag = 2;
                    } else if (income.equals("50-100万")) {
                        flag = 3;
                    } else if (income.equals("100-300万")) {
                        flag = 4;
                    } else if (income.equals("300-500万")) {
                        flag = 5;
                    } else if (income.equals("500-1000万")) {
                        flag = 6;
                    } else if (income.equals("1000万以上")) {
                        flag = 7;
                    }
                    registerFirstInfoBean.setYearIncome(flag);
                } else if (type == 3) {
                    String marryStatus = personHeightWheelView.getSelectedText();
                    user_marryStatus.setText(marryStatus);
                    int tag=0;
                    if (marryStatus.equals("未婚")) {
                        tag = 1;
                    } else if (marryStatus.equals("离异未育")) {
                        tag = 2;
                    } else if (marryStatus.equals("离异有孩子")) {
                        tag = 3;
                    } else if (marryStatus.equals("丧偶")) {
                        tag = 4;
                    }
                   registerFirstInfoBean.setMaritalStatus(tag);
                }
                //身高
                else {
                    String height = personHeightWheelView.getSelectedText();
                    userHeight.setText(height);
                    int length = height.length();
                    String heS = height.substring(0, length - 2);
                    registerFirstInfoBean.setHeight(Integer.valueOf(heS));

                }
                dialog_c.dismiss();
            }
        });

        //显示年月日
        showSelectDialog(3);


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
     * 滑动选择器里面的数据
     *
     * @param id
     * @return
     */
    private ArrayList<String> getNationList(int id) {
        nationHashMap.clear();

        DbOpenHelper dbOpenHelper = new DbOpenHelper(AuthIdentifyActivity.this);
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode==RESULT_OK){
            switch (requestCode) {
                //相册
                case SelectPictureDialog.CHOOSE_BIG_PICTURE:
                    String path = getPath(AuthIdentifyActivity.this, SelectPictureDialog.imageUri);
                    Bitmap bitmap = getBitmapFromPath(path);
                    if (firstOrSelfType == 0) {
                        if (pictureType != 0) {
                            identifyImageView.setImageBitmap(bitmap);
                            identifyImageView.setVisibility(View.VISIBLE);
                            addIdentifyImageView.setVisibility(View.GONE);
                        }
                    }
                    uploadPictureListener(path);
                    break;
                //相机
                case SelectPictureDialog.SELECT_CAMERA_CODE:
                    cropImageUri(SelectPictureDialog.cameraUri, 1300, 1300);
                    break;
                //剪切相机的图片
                case CAMEAR_CROP_PICTURE:
                    String path2 = getPath(AuthIdentifyActivity.this, SelectPictureDialog.cameraUri);
                    Bitmap bitmap2 = getBitmapFromPath(path2);
                    if (firstOrSelfType == 0) {
                        if (pictureType != 0) {
                            identifyImageView.setImageBitmap(bitmap2);
                            identifyImageView.setVisibility(View.VISIBLE);
                            addIdentifyImageView.setVisibility(View.GONE);
                        }
                    }
                    uploadPictureListener(path2);
                    break;
            }
        }
        selectPictureDialog.dismiss();
    }


    //用于将上传图片的进度传给主线程
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.e("++++++++++", "------------" + msg.obj);

            //显示上传进度
            setUploadProgress(msg.obj + "");
        }
    };



    /**
     * 取消dialog
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void cancelDialog() {
    }
    @Override
    public void sendResultJson(String type, long pid) {

    }

    @Override
    public void cancelDialog2() {

    }

    @Override
    public void requestifok() {

    }

    @Override
    public void getPicturePath(String path) {

    }

    @Override
    public void setUploadProgress(String pro) {
        if (progressTextView != null) {
            progressTextView.setText(pro);
            Log.e(TAG, "上传文件进度条正确");
        } else {
            Log.e(TAG, "上传文件进度条出错");
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

                            if (firstOrSelfType == 0) {
                                if (pictureType != 0) {
                                    //认证图片的id
                                    ArrayList<Long> picList = new ArrayList<Long>();
                                    picList.add(pid);
                                    registerFirstInfoBean.setIdentifyPicIdList(picList);
                                }
                            }
                            Log.e(TAG, "上传图片返回的id" + pid);
                            //取消进度条
                            if (null != dialog)
                                dialog.dismiss();

                        } else {
                            DialogUtils.setDialog(AuthIdentifyActivity.this, bean.getError());
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

    /**
     * 从路径中获取图片
     *
     * @param picturePath
     * @return
     */
    private Bitmap getBitmapFromPath(String picturePath) {
        //2、根据路径构造流的方法
        try {
            File file = new File(picturePath);
            InputStream inputStream = new FileInputStream(file);
            return BitmapFactory.decodeStream(inputStream, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;


    }

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

    /**
     * 根据URI得到路径
     *
     * @param context
     * @param uri
     * @return
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }


    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    private Dialog aldialog_a;
    private View.OnClickListener nClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setCondition();
        }
    };

    private void setCondition() {
        if (isBack==true){
            onBackPressed();
        }else{

            View view = LayoutInflater.from(AuthIdentifyActivity.this).inflate(R.layout.identify_dialog_layout, null);
            TextView cancle= (TextView) view.findViewById(R.id.simple_dialog_cancel_text);
            TextView message_dialog= (TextView) view.findViewById(R.id.message_dialog);
            TextView identify= (TextView) view.findViewById(R.id.simple_dialog_sure_text);
            aldialog_a = new Dialog(AuthIdentifyActivity.this, R.style.loading_dialog);
            aldialog_a.setCancelable(false);
            aldialog_a.setContentView(view);
            cancle.setText("遗憾离开");
            identify.setText("继续填写");
            message_dialog.setText("美盟为用户提供真实安全的社交环境，\n您还未提交认证，确定要离开？");
            cancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    aldialog_a.dismiss();
                    finish();
                }
            });
            identify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    aldialog_a.dismiss();
                }
            });
            aldialog_a.show();
        }
    }

    @Override
    public void onBackPressed() {
        setCondition();
    }
}
