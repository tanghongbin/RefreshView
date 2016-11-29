package com.example.com.meimeng.net;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.avos.avoscloud.LogUtil;
import com.example.com.meimeng.MeiMengApplication;
import com.example.com.meimeng.gson.GsonTools;
import com.example.com.meimeng.gson.gsonbean.BaseBean;
import com.example.com.meimeng.gson.gsonbean.ZhiFuBaoPayBean;
import com.example.com.meimeng.pay.PayResult;
import com.example.com.meimeng.util.ProcessPhoto;
import com.example.com.meimeng.util.Utils;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONStringer;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;

import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;
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
                    Log.d("whh", "url:   " + url + " |" + s);
                    setLogin(s);
                }catch (ConnectException e){
                    Log.d("wz","getJsonObservale网络连接异常："+e.toString());
                }catch (Exception e) {
                    observer.onError(e);
                    Log.e("getjson","错误信息"+e.toString());
                }
                return Subscriptions.empty();
            }
        }).subscribeOn(Schedulers.newThread());
        return observable;
    }

    private static void setLogin(String s){

        BaseBean bean= GsonTools.getBaseReqBean(s);
        if (bean.getError()!=null&&bean.getError().equals("异地登陆")) {
            Log.d("whh", "-------s:" + s);
            Looper.prepare();
            Utils.setRePromptDialog(MeiMengApplication.currentContext, "你的账号在另一台设备上登录，请重新登录。");
            Looper.loop();
        }
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
        String url = "http://7xlz2u.com2.z0.glb.qiniucdn.com/mm/image/" + uid + ".jpg"+"-sy";

        return url;
    }

    /**
     * 构造图片的url(200*200小图)
     *
     * @param uid
     * @return
     */
    public static String setPictureUrl(long uid, boolean issmall) {
        String url = "http://7xlz2u.com2.z0.glb.qiniucdn.com/mm/image/" + uid + ".jpg?imageView2/1/w/200/h/200"+"-sy";
        return url;
    }


    /**
     * 得到上传图片的Observale对象
     *
     * @param uid
     * @param path
     */

    public static Observable getUploadPictureObservale(final String uid, final String path, final Handler handler) {

        Observable observable = Observable.create(new Observable.OnSubscribeFunc<String>() {
            @Override
            public Subscription onSubscribe(Observer<? super String> op) {
                try {
                    String s = uploadPicture(uid, path,handler);

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

    private static String uploadPicture(String uid, String path,final Handler handler) throws Exception {

        String url = InternetConstant.SERVER_URL + InternetConstant.UPLOAD_PICTURE_URL + "?uid=" + uid;

        MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);

        File file = new File(path);
        builder.addFormDataPart("file", file.getName(), createCustomRequestBody(MultipartBuilder.FORM, file, new ProgressListener() {
            @Override public void onProgress(long totalBytes, long remainingBytes, boolean done) {
                Log.e("进度条：", "-------------" + (totalBytes - remainingBytes) * 100 / totalBytes + "%");
                String progressBar = (totalBytes - remainingBytes) * 100 / totalBytes + "%";
                Message message = Message.obtain();
                message.arg1 = 1;
                message.obj = progressBar;
                handler.sendMessage(message);

            }
        }));

        RequestBody requestBody = builder.build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        Response response = okHttpClient.newCall(request).execute();
        return response.body().string();
    }

    public static RequestBody createCustomRequestBody(final MediaType contentType, final File file, final ProgressListener listener) {
        return new RequestBody() {
            @Override public MediaType contentType() {
                return contentType;
            }

            @Override public long contentLength() {
                return file.length();
            }

            @Override public void writeTo(BufferedSink sink) throws IOException {
                Source source;
                try {
                    source = Okio.source(file);
                    //sink.writeAll(source);
                    Buffer buf = new Buffer();
                    Long remaining = contentLength();
                    for (long readCount; (readCount = source.read(buf, 2048)) != -1; ) {
                        sink.write(buf, readCount);
                        listener.onProgress(contentLength(), remaining -= readCount, remaining == 0);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public interface ProgressListener {
        void onProgress(long totalBytes, long remainingBytes, boolean done);
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
     * 特制的图片加载方法
     * @param width
     * @param imageView
     * @param picid
     */
    public static void setPhotoBitmap(final int width,final ImageView imageView, final long picid) {
        Bitmap bitmap = getBitmapFromMemoryCache(picid);
        if (bitmap != null) {
            Bitmap b = zoomBitmap(bitmap,width);
            imageView.setImageBitmap(b);
        } else {
            Observable observable = getBytesObservale(picid);
            observable.observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<byte[]>() {

                @Override
                public void call(byte[] bytes) {
                    // Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//                    InputStream inputStream = new ByteArrayInputStream(bytes);
                    Bitmap b = ProcessPhoto.decodeFromByte(bytes, 0, bytes.length, width, 750);
                    if (b != null) {
                        b = zoomBitmap(b,width);
                        imageView.setImageBitmap(b);
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

    public static Bitmap zoomBitmap(Bitmap bitmap,int width) {

        //获得Bitmap的高和宽
        int bmpWidth = bitmap.getWidth();
        int bmpHeight = bitmap.getHeight();

        //这边假设imageView的宽和高是一样的

        //设置缩小比例,imageViewWidth 就是我们需要的宽度
        double scale = (double) width / bmpWidth;


        //计算出长宽要缩放的比例
        float scaleWidth = (float) (1 * scale);
        float scaleHeight = (float) (1 * scale);
        //产生resize后的Bitmap对象
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth,scaleHeight);
        Bitmap resizeBmp = null;
        resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bmpWidth, bmpHeight, matrix, true);

        //修改色彩模式
        Bitmap zoomBitmap = resizeBmp.copy
                (Bitmap.Config.RGB_565,false);
        if (resizeBmp != null&&!resizeBmp.isRecycled()) {
            resizeBmp.recycle();
            System.gc();
        }
        return zoomBitmap;
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

    public static void getPicIntoView(final int width, final int height, final ImageView imageView, final long picid, final int position,final int mod) {
        Bitmap bitmap = getBitmapFromMemoryCache(picid);
        if (bitmap != null) {
            if ((int) imageView.getTag() == position)

//                imageView.setImageBitmap(bitmap);
                switch (mod){
                    case 1:
                        imageView.setImageBitmap(bitmap);
                        break;
                    case 2:
                        imageView.setBackgroundDrawable(new BitmapDrawable(bitmap));
                        break;
                    default:
                        break;
                }
        } else {

            Observable observable = getBytesObservale(picid);
            observable.observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<byte[]>() {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void call(byte[] bytes) {
                    // Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    Bitmap bitmap = ProcessPhoto.decodeFromByte(bytes, 0, bytes.length, width, height);//此按照活动页的大小设置，应该在getPicIntoView里添加期望的长宽参数
                    if (bitmap != null) {
                        addBitmapToMemoryCache(picid, bitmap);
                        switch (mod){
                            case 1:
                                imageView.setImageBitmap(bitmap);
                                break;
                            case 2:
                                imageView.setBackgroundDrawable(new BitmapDrawable(bitmap));
                                break;
                            default:
                                break;
                        }


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

    public static void getPicIntoView(final int width, final int height, final ImageView imageView, final long picid, final int position,final View view,final Context context) {
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


                        Utils.applyBlur(context, imageView, view,1);

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
        return MeiMengApplication.mMemoryCache.get(picId);
    }

    /**
     * 添加图片到内存缓存区缓存大图
     */
    public static void addBitmapToMemoryCache(Long picId, Bitmap bitmap) {
        // 缓存中无此图片则存入
        if (getBitmapFromMemoryCache(picId) == null) {
            MeiMengApplication.mMemoryCache.put(picId, bitmap);
        }
    }

    /**
     * 支付宝支付的结果
     *
     * @param url
     * @param jsonStr
     * @return
     */
    public static Observable getZhifubaoObserable(final Context context, final String url, final String jsonStr) {
        Log.e("request", "url:   " + url + "     param:   " + jsonStr);

        Observable observable = Observable.create(new Observable.OnSubscribeFunc<String>() {
            @Override
            public Subscription onSubscribe(Observer<? super String> observer) {
                try {

                    Log.e("订单url:", url);
                    Log.e("请求参数：", jsonStr);

                    //现在服务端请求支付订单信息
                    String s = getJsonData(url, jsonStr);
                    ZhiFuBaoPayBean zhiFuBaoPayBean = GsonTools.getZhifubaoPayBean(s);
                    String preOrder = zhiFuBaoPayBean.getParam().getPreOrder().getParam();

//                    preOrder = "_input_charset=utf-8&app_id=8.8.8.8&appenv=system=android^version=3.0.1.2&body=这是白金会员&it_b_pay=30m&notify_url=http://www.baidu.com&out_trade_no=39&partner=2088302393237297&payment_type=1&seller_id=2088302393237297&service=mobile.securitypay.pay&sign_type=RSA&subject=会员（2W）&total_fee=20000.0";
                    // 构造PayTask 对象
                    PayTask alipay = new PayTask((Activity) context);
                    // 调用支付接口，获取支付结果
                    String result = alipay.pay(preOrder);
                    //订单号
//                    String orderNumStr = praiseZhifubaoResult(context,result);
                    long orderNumStr = zhiFuBaoPayBean.getParam().getOutTradeNo();


                    //在服务端确认支付宝支付结果
                    String url = InternetConstant.SERVER_URL + InternetConstant.CONFIRM_ZHIFUBAO_PAY_RESULT + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();
                    JSONStringer stringer = new JSONStringer().object().key("out_trade_no").value(orderNumStr).endObject();
                    String orderJson = stringer.toString();
                    //这就是支付宝支付的最后结果
                    String payResult = getJsonData(url, orderJson);

                    //把此结果传入主线程，进行对应的ui更新
                    observer.onNext(payResult);
                    observer.onCompleted();

                } catch (Exception e) {
                    observer.onError(e);
                }

                return Subscriptions.empty();
            }
        }).subscribeOn(Schedulers.newThread());
        return observable;
    }

    /**
     * 从支付结果中获取订单号
     *
     * @param context
     * @param resultStr
     * @return
     */
    private static String praiseZhifubaoResult(Context context, String resultStr) {

        PayResult payResult = new PayResult(resultStr);

        // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
        String resultInfo = payResult.getResult();

        String resultStatus = payResult.getResultStatus();

        // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
        //支付成功
        if (TextUtils.equals(resultStatus, "9000")) {

            //订单号
            String orderInfoNum = payResult.getOrderInfo();


            Toast.makeText(context, "支付成功",
                    Toast.LENGTH_SHORT).show();

            //返回订单号
            return orderInfoNum;
        } else {
            // 判断resultStatus 为非“9000”则代表可能支付失败
            // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
            if (TextUtils.equals(resultStatus, "8000")) {
                Toast.makeText(context, "支付结果确认中",
                        Toast.LENGTH_SHORT).show();
            } else {
                // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                Toast.makeText(context, "支付失败",
                        Toast.LENGTH_SHORT).show();
            }
        }
        return null;
    }

}
