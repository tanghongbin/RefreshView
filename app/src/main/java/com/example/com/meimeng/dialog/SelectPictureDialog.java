package com.example.com.meimeng.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import com.example.com.meimeng.R;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2015/12/8.
 */
public class SelectPictureDialog extends Dialog {

    private static Activity mContext;


    //保存图片的路径及名字
    private static String IMAGE_FILE_LOCATION = "";
    //存储大图片的URI
    public static Uri imageUri;
    private static String CAMERA_PICTURE_LOCATION = "";

    public static Uri cameraUri;

    //请求大图片的请求码
    public static final int CHOOSE_BIG_PICTURE = 1;

    //相机的请求码
    public static final int SELECT_CAMERA_CODE = 21;






    public SelectPictureDialog(Activity context, int theme){
        super(context, theme);
        this.mContext = context;
    }

    public SelectPictureDialog(Activity context){
        super(context);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_picture_dialog_layout);
        ButterKnife.bind(this);

        //创建文件
        createFile();

        //初始化图片路径及URI
        IMAGE_FILE_LOCATION = "file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/" + "small.jpg";
        CAMERA_PICTURE_LOCATION = "file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/" + "camera.jpg";
        imageUri = Uri.parse(IMAGE_FILE_LOCATION);
        cameraUri = Uri.parse(CAMERA_PICTURE_LOCATION);
    }

    @OnClick(R.id.select_picture_dialog_camera_text)
    protected void getCameraListener(){
        cameraButtonListener();
    }

    @OnClick(R.id.select_picture_dialog_album_text)
    protected void getAlbumListener(){
        selectOneAlbumWay();
    }

    @OnClick(R.id.select_picture_dialog_cancel_text)
    protected void cancelListener(){
        this.dismiss();
    }


    /**
     * 单张图片的选择,并裁剪,使用于大图片，并可以按比例剪切
     */
    private void selectOneAlbumWay() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        //发送剪切信号
        intent.putExtra("crop", "true");

        //X方向上的比例
        intent.putExtra("aspectX", 1);

        //Y方向上的比例
        intent.putExtra("aspectY", 1);

        //裁剪区的宽
        intent.putExtra("outputX", 1300);

        //裁剪区的高
        intent.putExtra("outputY", 1300);

        //是否保留比例
        intent.putExtra("scale", true);

        //是否将数据保留在Bitmap中返回
        intent.putExtra("return-data", false);

        //将URI指向相应的file:///...
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

        intent.putExtra("noFaceDetection", true);

        mContext.startActivityForResult(intent, CHOOSE_BIG_PICTURE);
    }

    /**
     * 拍照
     */
    private void cameraButtonListener() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
        mContext.startActivityForResult(intent, SELECT_CAMERA_CODE);
    }

    //创建文件夹及文件
    public void createFile() {

        File file = new File(IMAGE_FILE_LOCATION);
        if (!file.exists()) {
            try {
                //按照指定的路径创建文件夹
                file.mkdirs();
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
        File dir = new File(CAMERA_PICTURE_LOCATION);
        if (!dir.exists()) {
            try {
                //在指定的文件夹中创建文件
                dir.createNewFile();
            } catch (Exception e) {
            }
        }

    }
}
