package com.publishproject.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.publishproject.R;
import com.publishproject.core.common.logger.LogUtil;
import com.publishproject.core.common.permission.PermissionHelper;
import com.publishproject.core.common.permission.PermissionListener;

import java.io.File;
import java.util.List;

import pushlish.tang.com.commonutils.ConvertUriToFile;
import pushlish.tang.com.commonutils.others.FileUtils;

public class ImageSingleChooseActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA = 0x1000;
    private static final int REQUEST_CROP = 0x1001;
    private static final int REQUEST_GALLERY = 0x1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_single_choose);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.take_pic:
                camera(this);
                break;
            case R.id.gallery:
                gallery(this);
                break;
            case R.id.cancle:
                destroy();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int type = getIntent().getIntExtra(ImageChooseAndCropUtil.TYPE,-1);
        if(type == -1 || data == null){
            return;
        }
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case REQUEST_CAMERA://照相返回结果
                    if(type == ImageChooseAndCropUtil.TYPE_CHOOSE_AND_CROP){//启动裁剪
                        crop(ImageSingleChooseActivity.this,Uri.fromFile(createFileIfNotExists()));
                    }else {
                        ImageChooseAndCropUtil.getInstance(ImageSingleChooseActivity.this).getCropCallback().
                                call(createFileIfNotExists().getAbsolutePath());
                        destroy();
                    }
                    break;
                case REQUEST_CROP:
                    File file = createFileIfNotExists();
                    ImageChooseAndCropUtil.getInstance(ImageSingleChooseActivity.this).getCropCallback().call(file.getAbsolutePath());
                    destroy();
                    break;
                case REQUEST_GALLERY:
                    Uri uri = data.getData();
                    if(type == ImageChooseAndCropUtil.TYPE_CHOOSE_AND_CROP){//启动裁剪
                        crop(ImageSingleChooseActivity.this,uri);
                    }else {
                        ImageChooseAndCropUtil.getInstance(ImageSingleChooseActivity.this).getCropCallback().
                                call(ConvertUriToFile.getRealPathFromURIKK(ImageSingleChooseActivity.this,uri));
                        destroy();
                    }
                    break;
            }
        }

    }

    /**
     * 拍照
     */
    public static void camera(final Activity activity) {
        PermissionHelper.requestPermission(activity, "权限被拒绝", new PermissionListener() {
            @Override
            public void onGranted() {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // 判断存储卡是否可以用，可用进行存储
                // 从文件中创建uri
                Uri uri = Uri.fromFile(createFileIfNotExists());
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CAREMA
                activity.startActivityForResult(intent,REQUEST_CAMERA);
            }

            @Override
            public void onDenied(List<String> permissions) {
                LogUtil.i("权限被拒绝");
            }
        },PermissionHelper.CAMERA,PermissionHelper.WRITE_EXTERNAL_STORAGE);

    }

    private static File createFileIfNotExists() {
        try{
            File file = new File(Environment.getExternalStorageDirectory()+File.separator+"demo");
            if(!file.exists()){
                file.mkdirs();
            }
            File fileName = new File(file,"template.png");
            if(!fileName.exists()){
                fileName.createNewFile();
            }
            return fileName;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 从图库选择照片
     */

    public static void crop(Activity activity, Uri uri) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 240);
        intent.putExtra("outputY", 240);
        intent.putExtra("outputFormat", "JPEG");// 图片格式
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(createFileIfNotExists()));
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
        activity.startActivityForResult(intent, REQUEST_CROP);
    }

    /**
     * 从相册获取
     */
    public static void gallery(final Activity activity) {


        PermissionHelper.requestPermission(activity, "权限被拒绝", new PermissionListener() {
            @Override
            public void onGranted() {
                // 激活系统图库，选择一张图片
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
                activity.startActivityForResult(intent,REQUEST_GALLERY);
            }

            @Override
            public void onDenied(List<String> permissions) {
                LogUtil.i("权限被拒绝");
            }
        },PermissionHelper.CAMERA,PermissionHelper.WRITE_EXTERNAL_STORAGE);
    }

    public void destroy(){
        finish();
    }
}
