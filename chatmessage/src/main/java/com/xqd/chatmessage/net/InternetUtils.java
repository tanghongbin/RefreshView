package com.xqd.chatmessage.net;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.ImageView;

import com.avos.avoscloud.LogUtil;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.xqd.chatmessage.ChatAplication;
import com.xqd.chatmessage.util.ProcessPhoto;

import java.io.File;
import java.io.IOException;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.concurrency.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

/**
 * Created by 010 on 2015/7/20.
 */
public class InternetUtils {


    //Okhttp对象
    private static OkHttpClient okHttpClient = new OkHttpClient();

    //json
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private static final MediaType MEDIA_TYPE = MediaType.parse("image/jpg");

    private static final String TAG = "InternetUtils";

    /**
     * 得到JSON的方法
     *
     * @param url
     * @param jsonStr
     * @return
     */
    public static Observable getJsonObservale(final String url, final String jsonStr) {
        Log.e("request", "url:   " + url + "     param:   " + jsonStr);

        Observable observable = Observable.create(new Observable.OnSubscribeFunc<String>() {
            @Override
            public Subscription onSubscribe(Observer<? super String> observer) {
                try {
                    String s = getJsonData(url, jsonStr);
                    observer.onNext(s);
                    observer.onCompleted();
                    Log.e("getjson", s);
                } catch (Exception e) {
                    observer.onError(e);
                }

                return Subscriptions.empty();
            }
        }).subscribeOn(Schedulers.newThread());
        return observable;
    }

    /**
     * 网络请求得到json数据
     *
     * @param url
     * @param jsonStr
     */
    private static String getJsonData(String url, String jsonStr) throws Exception {

        RequestBody body = RequestBody.create(JSON, jsonStr);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            String s = response.body().string();

            return s;
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

    /**
     * 下载图片时，得到字节数组
     *
     * @param uid
     * @return
     */
    public static Observable getBytesObservale(final long uid) {

        Observable observable = Observable.create(new Observable.OnSubscribeFunc() {
            @Override
            public Subscription onSubscribe(Observer op) {
                try {
                    byte[] imageBytes = getBytesData(uid);

                    op.onNext(imageBytes);
                    op.onCompleted();

                } catch (Exception e) {
                    op.onError(e);
                }

                return Subscriptions.empty();
            }
        }).subscribeOn(Schedulers.newThread());

        return observable;
    }

    /**
     * 下载图片时，得到字节数组(200*200小图)
     *
     * @param uid
     * @return
     */
    public static Observable getBytesObservale(final long uid, final boolean issmall) {

        Observable observable = Observable.create(new Observable.OnSubscribeFunc() {
            @Override
            public Subscription onSubscribe(Observer op) {
                try {
                    byte[] imageBytes = getBytesData(uid, true);

                    op.onNext(imageBytes);
                    op.onCompleted();

                } catch (Exception e) {
                    op.onError(e);
                }

                return Subscriptions.empty();
            }
        }).subscribeOn(Schedulers.newThread());

        return observable;
    }

    /**
     * 从网络上获取字节数组
     *
     * @param uid
     */
    private static byte[] getBytesData(long uid) throws Exception {
        String url = setPictureUrl(uid);
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = okHttpClient.newCall(request).execute();

        byte[] imageBytes = response.body().bytes();

        return imageBytes;
    }

    /**
     * 从网络上获取字节数组（200*200小图）
     *
     * @param uid
     */
    private static byte[] getBytesData(long uid, boolean issmall) throws Exception {
        String url = setPictureUrl(uid, true);
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = okHttpClient.newCall(request).execute();

        byte[] imageBytes = response.body().bytes();

        return imageBytes;
    }

    /**
     * 构造图片的url
     *
     * @param uid
     * @return
     */
    public static String setPictureUrl(long uid) {
//        String url = "http://7xka9b.com1.z0.glb.clouddn.com/mm/image/" + uid + ".jpg";
        String url = "http://7xlz2u.com2.z0.glb.qiniucdn.com/mm/image/" + uid + ".jpg";

        return url;
    }

    /**
     * 构造图片的url(200*200小图)
     *
     * @param uid
     * @return
     */
    public static String setPictureUrl(long uid, boolean issmall) {
//        String url = "http://7xka9b.com1.z0.glb.clouddn.com/mm/image/" + uid + ".jpg?imageView2/1/w/200/h/200";
        String url = "http://7xlz2u.com2.z0.glb.qiniucdn.com/mm/image/" + uid + ".jpg?imageView2/1/w/200/h/200";
        return url;
    }


    /**
     * 得到上传图片的Observale对象
     *
     * @param uid
     * @param path
     */

    public static Observable getUploadPictureObservale(final String uid, final String path) {

        Observable observable = Observable.create(new Observable.OnSubscribeFunc<String>() {
            @Override
            public Subscription onSubscribe(Observer<? super String> op) {
                try {


                    String s = uploadPicture(uid, path);

                    op.onNext(s);
                    op.onCompleted();
                } catch (Exception e) {
                    op.onError(e);
                }
                return Subscriptions.empty();
            }
        }).subscribeOn(Schedulers.newThread());

        return observable;
    }

    /**
     * 上传图片
     *
     * @param uid
     * @param path
     */


    private static String uploadPicture(String uid, String path) throws Exception {

        String url = InternetConstant.SERVER_URL + InternetConstant.UPLOAD_PICTURE_URL + "?uid=" + uid;
        File file;
        file = new File(path);
      /*  RequestBody requestBody = new MultipartBuilder()
                .type(MultipartBuilder.FORM)
                .addPart(
                        Headers.of("Content-Disposition", "form-data; name=\"image\""),
                        RequestBody.create(MEDIA_TYPE, file))
                .build();*/
        //构造请求体
        RequestBody requestBody2 = new MultipartBuilder()
                .type(MultipartBuilder.FORM)
                .addFormDataPart("file", file.getName(), RequestBody.create(MEDIA_TYPE, file))
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody2)
                .build();

        Response response = okHttpClient.newCall(request).execute();
        return response.body().string();
    }


    /**
     * 根据图片id给ImageView设置图片
     *
     * @param imageView
     * @param picid
     */

    public static void getPicIntoView(final int width, final int height, final ImageView imageView, final long picid) {
        Bitmap bitmap = getBitmapFromMemoryCache(picid);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            Observable observable = getBytesObservale(picid);
            observable.observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<byte[]>() {

                @Override
                public void call(byte[] bytes) {
                    // Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    Bitmap bitmap = ProcessPhoto.decodeFromByte(bytes, 0, bytes.length, width, height);//此按照活动页的大小设置，应该在getPicIntoView里添加期望的长宽参数
                    if (bitmap != null) {
                        addBitmapToMemoryCache(picid, bitmap);
                        imageView.setImageBitmap(bitmap);
                    }

                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    LogUtil.log.e(TAG, throwable.getMessage());
                }
            });
        }
    }

    /**
     * 根据图片id给ImageView设置图片,专门用来展示大图
     *
     * @param imageView
     * @param picid
     */

    public static void getPicIntoView(final int width, final int height, final ImageView imageView, final long picid, final Context context) {

//        final Dialog dialog = DialogUtils.createLoadingDialog(context);
//        dialog.show();
        Observable observable = getBytesObservale(picid);
        observable.observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<byte[]>() {

            @Override
            public void call(byte[] bytes) {
                // Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                Bitmap bitmap = ProcessPhoto.decodeFromByte(bytes, 0, bytes.length, width, height);//此按照活动页的大小设置，应该在getPicIntoView里添加期望的长宽参数
                if (bitmap != null) {
                    addBitmapToMemoryCache(picid, bitmap);
                    imageView.setImageBitmap(bitmap);
//                    dialog.dismiss();
                }

            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                LogUtil.log.e(TAG, throwable.getMessage());
            }
        });
    }

    /**
     * 根据图片id给ImageView设置图片(大小为200*200小图)
     *
     * @param imageView
     * @param picid
     */

    public static void getPicIntoView(final int width, final int height, final ImageView imageView, final long picid, final boolean issmall) {
        Bitmap bitmap = getBitmapFromMemoryCache(picid);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            Observable observable = getBytesObservale(picid, true);
            observable.observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<byte[]>() {

                @Override
                public void call(byte[] bytes) {
                    // Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    Bitmap bitmap = ProcessPhoto.decodeFromByte(bytes, 0, bytes.length, width, height);//此按照活动页的大小设置，应该在getPicIntoView里添加期望的长宽参数
                    if (bitmap != null) {
                        addBitmapToMemoryCache(picid, bitmap);
                        imageView.setImageBitmap(bitmap);
                    }

                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    LogUtil.log.e(TAG, throwable.getMessage());
                }
            });
        }
    }


    /**
     * 根据图片id给ImageView设置图片 加Tag的版本,用于ListView等的防错位
     *
     * @param imageView
     * @param picid
     */

    public static void getPicIntoView(final int width, final int height, final ImageView imageView, final long picid, final int position) {
        Bitmap bitmap = getBitmapFromMemoryCache(picid);
        if (bitmap != null) {
            if ((int) imageView.getTag() == position)
                imageView.setImageBitmap(bitmap);
        } else {

            Observable observable = getBytesObservale(picid);
            observable.observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<byte[]>() {
                @Override
                public void call(byte[] bytes) {
                    // Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    Bitmap bitmap = ProcessPhoto.decodeFromByte(bytes, 0, bytes.length, width, height);//此按照活动页的大小设置，应该在getPicIntoView里添加期望的长宽参数
                    if (bitmap != null) {
                        addBitmapToMemoryCache(picid, bitmap);
                        imageView.setImageBitmap(bitmap);
                    }

                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    LogUtil.log.e(TAG, throwable.getMessage());
                }
            });
        }
    }

    /**
     * 根据图片id给ImageView设置图片 加Tag的版本(200*200小图)用于ListView中头像等的防错位
     *
     * @param imageView
     * @param picid
     */

    public static void getPicIntoView(final int width, final int height, final ImageView imageView, final long picid, final int position, final boolean issmall) {
        Bitmap bitmap = getBitmapFromMemoryCache(picid);
        if (bitmap != null) {
//            if ((int) imageView.getTag() == position)
                imageView.setImageBitmap(bitmap);
        }

        else {
            Observable observable = getBytesObservale(picid, true);
            observable.observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<byte[]>() {

                @Override
                public void call(byte[] bytes) {
                    // Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    Bitmap bitmap = ProcessPhoto.decodeFromByte(bytes, 0, bytes.length, width, height);//此按照活动页的大小设置，应该在getPicIntoView里添加期望的长宽参数
                    if (bitmap != null) {
                        addBitmapToMemoryCache(picid, bitmap);
                        if ((int) imageView.getTag() == position)
                            imageView.setImageBitmap(bitmap);
//                        else {
//                            getPicIntoView(width, height, imageView, picid, position, true);
//                        }
                    }
//                    else
//                        getPicIntoView(width, height, imageView, picid, position, true);

                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    LogUtil.log.e(TAG, throwable.getMessage());
                }
            });
        }
    }

    /**
     * 判断有无网络
     *
     * @param context
     * @return
     */

    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 获取缓存中图片的方法,缓存大图
     *
     * @param picId
     * @return
     */
    public static Bitmap getBitmapFromMemoryCache(Long picId) {
        if (picId == null) {
            return null;
        }
        return ChatAplication.mMemoryCache.get(picId);
    }

    /**
     * 添加图片到内存缓存区缓存大图
     */
    public static void addBitmapToMemoryCache(Long picId, Bitmap bitmap) {
        // 缓存中无此图片则存入
        if (getBitmapFromMemoryCache(picId) == null) {
            ChatAplication.mMemoryCache.put(picId, bitmap);
        }
    }

    /**
     * 获取缓存中图片的方法,缓存小图
     *
     * @param picId
     * @return
     */
//    public static Bitmap getBitmapFromMemoryCacheSmall(Long picId) {
//        return MeiMengApplication.mMemoryCacheSmall.get(picId);
//    }
//
//    /**
//     * 添加图片到内存缓存区缓存小图
//     */
//    public static void addBitmapToMemoryCacheSmall(Long picId, Bitmap bitmap) {
//        // 缓存中无此图片则存入
//        if (getBitmapFromMemoryCache(picId) == null) {
//            MeiMengApplication.mMemoryCacheSmall.put(picId, bitmap);
//        }
//    }


}
