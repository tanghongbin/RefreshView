package com.example.com.meimeng.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.com.meimeng.MeiMengApplication;
import com.example.com.meimeng.R;
import com.example.com.meimeng.gson.GsonTools;
import com.example.com.meimeng.gson.gsonbean.UploadPictureBean;
import com.example.com.meimeng.net.InternetUtils;
import com.example.com.meimeng.util.DialogUtils;
import com.example.com.meimeng.util.Utils;

import java.io.File;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by shi-02 on 2015/4/17.

 * 相册、拍照

 */
public class SelectPictureFragment2 extends Fragment implements View.OnClickListener {

    private static final String TAG = "SelectPictureFragment";

    private TextView cameraText;

    private TextView albumText;

    private TextView cancelText;

    private View view;

    private static Context context;

    private String type;


    //保存图片的路径及名字
    private static String IMAGE_FILE_LOCATION = "file:///sdcard/Meimeng/temp.jpg";

    //存储大图片的URI
    private Uri imageUri;
    private static String CAMERA_PICTURE_LOCATION="file:///sdcard/Meimeng/camera.jpg";
    private Uri cameraUri;

    //请求大图片的请求码
    private final int CHOOSE_BIG_PICTURE = 1;

    private final int CAMEAR_CROP_PICTURE= 31;

    private final int SELECT_CAMERA_CODE = 21;

    //相机
    public static final int CAMERA_REQUEST_CODE = 0;

    public interface OnSelectPictureDialogListener {
        public void cancelDialog();

        public void sendResultJson(String type, long pid);

        public void cancelDialog2();

        public void requestifok();


        public void getPicturePath(String path);

        public void setUploadProgress(String pro);
    }

    public static Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
//            ((SelectPictureFragment.OnSelectPictureDialogListener) MeiMengApplication.currentContext).getPicturePath(msg.obj+"");
            Log.e("++++++++++","------------"+msg.obj);
            ((OnSelectPictureDialogListener) context).setUploadProgress(msg.obj + "");
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.select_picture_dialog_layout, container, false);

        MeiMengApplication.currentContext=getActivity();
        initView();
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            type = bundle.getString("type");
        }

        //创建文件
        createFile();

        return view;
    }

    private void initView() {

        context = getActivity();

        cameraText = (TextView) view.findViewById(R.id.select_picture_dialog_camera_text);
        cameraText.setOnClickListener(this);

        albumText = (TextView) view.findViewById(R.id.select_picture_dialog_album_text);
        albumText.setOnClickListener(this);

        cancelText = (TextView) view.findViewById(R.id.select_picture_dialog_cancel_text);
        cancelText.setOnClickListener(this);

        IMAGE_FILE_LOCATION = "file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/" + "small.jpg";
        CAMERA_PICTURE_LOCATION = "file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/" + "camera.jpg";
        imageUri = Uri.parse(IMAGE_FILE_LOCATION);
        cameraUri = Uri.parse(CAMERA_PICTURE_LOCATION);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select_picture_dialog_camera_text:
                cameraButtonListener();
                break;
            case R.id.select_picture_dialog_album_text:
                selectOneAlbumWay();
                break;
            case R.id.select_picture_dialog_cancel_text:
                ((OnSelectPictureDialogListener) context).cancelDialog();
                break;
            default:
                break;
        }
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

     /*   //X方向上的比例
        intent.putExtra("aspectX", 1);

        //Y方向上的比例
        intent.putExtra("aspectY", 1);*/

        //裁剪区的宽
        intent.putExtra("outputX", 300);

        //裁剪区的高
        intent.putExtra("outputY", 300);

        //是否保留比例
        intent.putExtra("scale", true);

        //是否将数据保留在Bitmap中返回
        intent.putExtra("return-data", false);

        //将URI指向相应的file:///...
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

        intent.putExtra("noFaceDetection", true);

        startActivityForResult(intent, CHOOSE_BIG_PICTURE);
    }

    /**
     * 拍照
     */
    private void cameraButtonListener(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,cameraUri);
        startActivityForResult(intent,SELECT_CAMERA_CODE);
    }

    /**
     * 剪切图片
     * @param uri
     * @param outputX
     * @param outputY
     */
    private void cropImageUri(Uri uri,int outputX,int outputY){
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");

        //通知剪切图片
        intent.putExtra("crop",true);

     /*   //X方向的比例
        intent.putExtra("aspectX", 1);

        //Y方向的比例
        intent.putExtra("aspectY",1);*/

        //裁剪区的宽度
        intent.putExtra("outputX",outputX);

        //裁剪区的高度
        intent.putExtra("outputY",outputY);

        //按照比例裁剪
        intent.putExtra("scale",true);

        //裁剪后的图片存放在uri中
        intent.putExtra(MediaStore.EXTRA_OUTPUT,uri);

        //裁剪后的图片不以Bitmap的形式返回
        intent.putExtra("return-data",false);

        intent.putExtra("outputFormat",Bitmap.CompressFormat.JPEG.toString());

        intent.putExtra("noFaceDetection",true);

        startActivityForResult(intent,CAMEAR_CROP_PICTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK){

            switch (requestCode){
                case CHOOSE_BIG_PICTURE:
                    String path = getPath(context,imageUri);
                    uploadPictureListener(path);
                    break;
                case SELECT_CAMERA_CODE:
                    cropImageUri(cameraUri,400,400);
                    break;
                case CAMEAR_CROP_PICTURE:
                    String path2 = getPath(context,cameraUri);
                    uploadPictureListener(path2);
                    break;
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 上传图片
     * @param path
     */
    private void uploadPictureListener(String path){
        if (TextUtils.isEmpty(Utils.getUserId())) {
            return;
        }
        Observable observable = InternetUtils.getUploadPictureObservale(Utils.getUserId(), path,handler);
        ((OnSelectPictureDialogListener) context).getPicturePath(path);

        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1() {
                    @Override
                    public void call(Object o) {
                        UploadPictureBean bean = GsonTools.getUploadPhototBean((String) o);
                        if (bean.isSuccess()) {
                            long pid = bean.getParam().getPid();
                            Log.e("Select:pid=", pid + ";" + o);
                            ((OnSelectPictureDialogListener) context).sendResultJson(type, pid);
                            ((OnSelectPictureDialogListener) context).requestifok();

                        } else {
                            DialogUtils.setDialog(context, bean.getError());
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e("Select", throwable.getMessage());
                        ((OnSelectPictureDialogListener) context).cancelDialog2();
                    }
                });
        ((OnSelectPictureDialogListener) context).cancelDialog();
    }


    /**

     * 根据URI得到路径
     * @param context

     * @param uri
     * @return
     */
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
