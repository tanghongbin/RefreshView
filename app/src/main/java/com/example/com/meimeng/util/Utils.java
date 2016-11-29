package com.example.com.meimeng.util;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.com.meimeng.MeiMengApplication;
import com.example.com.meimeng.R;
import com.example.com.meimeng.activity.HomeActivity;
import com.example.com.meimeng.activity.InitiateEvent;
import com.example.com.meimeng.activity.InviteActivity;
import com.example.com.meimeng.activity.RegisterActivity;
import com.example.com.meimeng.activity.RegisterUpLoadHeadPic;
import com.example.com.meimeng.activity.VipActivity;
import com.example.com.meimeng.bean.ProgressBean;
import com.example.com.meimeng.bean.RegisterFirstInfoBean;
import com.example.com.meimeng.constants.CommonConstants;
import com.example.com.meimeng.gson.GsonTools;
import com.example.com.meimeng.gson.gsonbean.BaseBean;
import com.example.com.meimeng.gson.gsonbean.MyBaseInfoBean;
import com.example.com.meimeng.gson.gsonbean.MyBaseInfoItem;
import com.example.com.meimeng.net.InternetConstant;
import com.example.com.meimeng.net.InternetUtils;

import org.json.JSONStringer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by 010 on 2015/7/16.
 */
public class Utils {
    /**
     * @param context
     * @return
     */

    private static float tarWidth;
    private static float tarHeight;

    public static DisplayMetrics getScreenMetrics(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);

        return metrics;
    }

    public static void getUerVerfiy(){
            try {
                String url = InternetConstant.SERVER_URL + InternetConstant.GET_USER_BASE_INFO_URL + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();
                JSONStringer stringer = new JSONStringer().object().key("targetUid").value(Utils.getUserId()).endObject();
                String jsonStr = stringer.toString();
                Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
                observable.observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1() {
                            @Override
                            public void call(Object o) {
                                MyBaseInfoBean baseInfoBean = GsonTools.getMyBaseInfoBean((String) o);
                                MyBaseInfoItem myBaseInfoItem=baseInfoBean.getParam().getUserSimpleInfo();
                                MeiMengApplication.sharedPreferences.edit().putInt(CommonConstants.USER_VERFIY, myBaseInfoItem.getUserVerfiy()).commit();
                                setIdenttityData(myBaseInfoItem);
                                sureState(myBaseInfoItem);

                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                //Log.e("whh", throwable.getMessage());
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    private static void sureState(MyBaseInfoItem myBaseInfoItem) {
        SharedPreferences.Editor editor = MeiMengApplication.sharedPreferences.edit();
        RegisterFirstInfoBean registerFirstInfoBean=new RegisterFirstInfoBean();
        registerFirstInfoBean.setBirthday(myBaseInfoItem.getBirthday());
        registerFirstInfoBean.setFirstName(myBaseInfoItem.getFirstName());
        registerFirstInfoBean.setLastName(myBaseInfoItem.getLastName());
        registerFirstInfoBean.setYearIncome(myBaseInfoItem.getYearIncome());
        registerFirstInfoBean.setHeight(myBaseInfoItem.getHeight());
        registerFirstInfoBean.setIdentifyPicIdList((ArrayList<Long>) myBaseInfoItem.getIdentityPicId());
        if (checkFirstInfo(registerFirstInfoBean)==true) {
            editor.putBoolean(CommonConstants.MYSTATE, true);
        }else{
            editor.putBoolean(CommonConstants.MYSTATE,false);
        }
        editor.commit();
    }


    /**
     * 检查信息是否输入完整
     *
     * @param registerFirstInfoBean
     * @return
     */
    private static boolean checkFirstInfo(RegisterFirstInfoBean registerFirstInfoBean) {
        if (TextUtils.isEmpty(registerFirstInfoBean.getFirstName())) {
            return false;
        } else if (TextUtils.isEmpty(registerFirstInfoBean.getLastName())) {
            return false;
        } else if (registerFirstInfoBean.getHeight() ==0) {
            return false;
        } else if (TextUtils.isEmpty(registerFirstInfoBean.getBirthday())) {
            return false;
        }else if (registerFirstInfoBean.getYearIncome() == 0) {
            return false;
        } else if (registerFirstInfoBean.getIdentifyPicIdList() == null||registerFirstInfoBean.getIdentifyPicIdList().size()==0) {
            return false;
        } else {
            return true;
        }
    }

    private static void setIdenttityData(MyBaseInfoItem myBaseInfoItem){

        RegisterFirstInfoBean registerFirstInfoBean=new RegisterFirstInfoBean();
        registerFirstInfoBean.setFirstName(myBaseInfoItem.getFirstName());
        registerFirstInfoBean.setLastName(myBaseInfoItem.getLastName());
        registerFirstInfoBean.setHeight(myBaseInfoItem.getHeight());
        registerFirstInfoBean.setYearIncome(myBaseInfoItem.getYearIncome());
        registerFirstInfoBean.setIdentifyPicIdList((ArrayList<Long>) myBaseInfoItem.getIdentityPicId());
        registerFirstInfoBean.setIdeStatus(myBaseInfoItem.getIdeStatus());
        String[] birth=myBaseInfoItem.getBirthday().split(" ");
        registerFirstInfoBean.setBirthday(birth[0]);
        MeiMengApplication.registerFirstInfoBean=registerFirstInfoBean;
    }
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    /**
     * 他人页面联系红娘（非会员））
     *
     * @param context
     */
    @SuppressLint("NewApi")
    public static void setDialog(final Context context, final long targetuid, int timesget) {

        View view = LayoutInflater.from(context).inflate(R.layout.maker_invite, null);
        final TextView times = (TextView) view.findViewById(R.id.maker_num_times_text);
        final Dialog dialog = new Dialog(context, R.style.CustomAlertDialog);
        dialog.setContentView(view);
        times.setText(String.valueOf(timesget));
        dialog.show();

        final TextView sureText = (TextView) view.findViewById(R.id.maker_sure_text);
        final TextView text1 = (TextView) view.findViewById(R.id.maker_invite_text1);
        final TextView text2 = (TextView) view.findViewById(R.id.maker_invite_text2);
        final TextView text3 = (TextView) view.findViewById(R.id.maker_invite_text3);
        if (MeiMengApplication.sharedPreferences.getInt(CommonConstants.USER_LEVEL, 0) > 1) {
            sureText.setText("立即使用");
            text1.setText("私人红娘服务");
            text2.setText("您本周还剩");
            text3.setText("次私人红娘服务机会");
        }
        if (timesget < 1) {
            sureText.setClickable(false);
            sureText.setBackground(context.getResources().getDrawable(R.drawable.text_end_shape));
        } else {
            sureText.setBackground(context.getResources().getDrawable(R.drawable.text_4_shape));
            sureText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (TextUtils.isEmpty(Utils.getUserId())) {
                            return;
                        }
                        if (TextUtils.isEmpty(Utils.getUserToken())) {
                            return;
                        }
                        String url = InternetConstant.SERVER_URL + InternetConstant.MATCHMAKER_CONFIRM + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();
                        JSONStringer jsonStringer = new JSONStringer().object()
                                .key("targetUid").value(targetuid).endObject();
                        String jsonStr = jsonStringer.toString();
                        //得到Observable并获取返回的数据(主线程中)
                        Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
                        observable.observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Action1<String>() {
                                    @Override
                                    public void call(String s) {
                                        BaseBean basebean = GsonTools.getBaseReqBean(s);
                                        if (basebean.isSuccess()) {
                                            Utils.confirmMatchMakerDialog2(context);
                                            dialog.dismiss();
                                        } else {
                                            Toast.makeText(context, basebean.getError(), Toast.LENGTH_LONG).show();

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

        TextView openupText = (TextView) view.findViewById(R.id.open_up_text);
        if (MeiMengApplication.sharedPreferences.getInt(CommonConstants.USER_LEVEL, 0) > 1) {
            openupText.setVisibility(View.GONE);
        } else {
            openupText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                Toast.makeText(context, "跳到开通页面", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(context, VipActivity.class);
                    context.startActivity(intent);
                    dialog.dismiss();
                }
            });
        }

        TextView make_chance_text = (TextView) view.findViewById(R.id.make_chance_text);
        make_chance_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, InviteActivity.class);
                intent.putExtra("type", 1);
                context.startActivity(intent);
                dialog.dismiss();
            }
        });
        ImageView xImageView = (ImageView) view.findViewById(R.id.maker_invite_x_image_view);
        xImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    /**
     * 他人页面联系红娘（会员）
     *
     * @param context
     */
    public static void datatMatchMakerDialog(final Context context, final Long targetuid) {
        View view = LayoutInflater.from(context).inflate(R.layout.maker_invite, null);

        TextView texte1 = (TextView) view.findViewById(R.id.maker_invite_text1);
        texte1.setText("私人红娘服务");
        TextView texte2 = (TextView) view.findViewById(R.id.maker_invite_text2);
        texte2.setText("您的私人红娘将为您了解和联络该会员");
        TextView times = (TextView) view.findViewById(R.id.maker_num_times_text);
        times.setVisibility(View.GONE);
        TextView text3 = (TextView) view.findViewById(R.id.maker_invite_text3);
        text3.setVisibility(View.GONE);
        final Dialog dialog = new Dialog(context, R.style.CustomAlertDialog);
        dialog.setContentView(view);
        dialog.show();
        TextView openUpText = (TextView) view.findViewById(R.id.open_up_text);
        openUpText.setVisibility(View.GONE);
        TextView makeChanceText = (TextView) view.findViewById(R.id.make_chance_text);
        makeChanceText.setVisibility(View.GONE);
        TextView sureText = (TextView) view.findViewById(R.id.maker_sure_text);
        sureText.setText("立即使用");
        sureText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (TextUtils.isEmpty(Utils.getUserId())) {
                        return;
                    }
                    if (TextUtils.isEmpty(Utils.getUserToken())) {
                        return;
                    }
                    String url = InternetConstant.SERVER_URL + InternetConstant.MATCHMAKER_CONFIRM + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();
                    JSONStringer jsonStringer = new JSONStringer().object()
                            .key("targetUid").value(targetuid).endObject();
                    String jsonStr = jsonStringer.toString();
                    //得到Observable并获取返回的数据(主线程中)
                    Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
                    observable.observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Action1<String>() {
                                @Override
                                public void call(String s) {
                                    BaseBean basebean = GsonTools.getBaseReqBean(s);
                                    if (basebean.isSuccess()) {
                                        Utils.confirmMatchMakerDialog2(context);
                                        dialog.dismiss();
                                    } else {
                                        DialogUtils.setDialog(context, basebean.getError());
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
     * 根据出生日期计算年龄
     *
     * @param strBirthDay
     * @return 未来日期返回0
     * @throws Exception
     */
    public static int getAge(String strBirthDay) throws Exception {

        DateFormat df = new SimpleDateFormat("yy-MM-dd");
        Date birthDay = df.parse(strBirthDay);
        Calendar cal = Calendar.getInstance();

        if (cal.before(birthDay)) {
            return 0;
        }

        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(birthDay);

        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) {
                    age--;
                }
            } else {
                age--;
            }
        }

        return age;
    }

    /**
     * 将毫秒时间转换为显示格式的时间
     *
     * @param time
     * @return
     */
    public static String getDisplayDate(long time) {
        Date d = new Date(time);
        SimpleDateFormat formatdate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return formatdate.format(d);
    }

    /**
     *
     * @param time
     * @param Mod 转换的模式
     * @return
     */
    public static String getDisplayDate(long time,String Mod) {
        Date d = new Date(time);
        SimpleDateFormat formatdate = new SimpleDateFormat(Mod);
        return formatdate.format(d);
    }

    public static String getStrTime(String cc_time) {
        String re_StrTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        long lcc_time = Long.valueOf(cc_time);

        re_StrTime = sdf.format(new Date(lcc_time * 1L));

        return re_StrTime;

    }

    /**
     * 得到不会发生oom异常的图片
     *
     * @param path
     * @return
     */
    public static Bitmap getBitmap(String path) {

        BitmapFactory.Options opt = new BitmapFactory.Options();

        opt.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, opt);

        int w = opt.outWidth;
        int h = opt.outHeight;

        opt.inJustDecodeBounds = false;

        int imageViewWidth = 720;
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > imageViewWidth) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (w / imageViewWidth);
        } else if (w < h && h > imageViewWidth) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (h / imageViewWidth);
        }
        //be不能是小数，必须是整型的
        if (be <= 0)
            be = 1;


        opt.inSampleSize = be;

        //最低位的配置
        opt.inPreferredConfig = Bitmap.Config.RGB_565;

        //inPurgeable设为true的话表示使用BitmapFactory创建的Bitmap用于存储Pixel的内存空间在系统内存不足时可以被回收
        opt.inPurgeable = true;

        //是否深拷贝???
        opt.inInputShareable = true;

        //根据路径构造流的方法
        try {
            File file = new File(path);
            InputStream inputStream = new FileInputStream(file);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, opt);

            /**
             * 获取图片的旋转角度，有些系统把拍照的图片旋转了，有的没有旋转
             */
            int degree = readPictureDegree(path);
            /**
             * 把图片旋转为正的方向
             */
            bitmap = rotaingImageView(degree, bitmap);

            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    private static int readPictureDegree(String path) {

        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }


    /**
     * 旋转图片
     *
     * @param angle
     * @param bitmap
     * @return Bitmap
     */
    private static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        //旋转图片 动作
        Matrix matrix = new Matrix();

        matrix.postRotate(angle);
        System.out.println("angle2=" + angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }

    /**
     * 弹出未审核对话框
     */
    public static void JudgeUserVerfiy(final Context context, final int verfiyType) {

        View view = LayoutInflater.from(context).inflate(R.layout.no_verfiy_dialog, null);

        TextView back = (TextView) view.findViewById(R.id.no_verfiy_back);
        TextView sure = (TextView) view.findViewById(R.id.no_verfiy_sure);
        TextView text = (TextView) view.findViewById(R.id.no_verfiy_text);
        if (verfiyType == 2) {
            text.setText("对不起，您暂不符合美盟会员要求，未通过审核。您可以关注美盟订阅号:meimeng88888，获取更多信息。");
        }
        final Dialog dialog = new Dialog(context, R.style.CustomAlertDialog);
        dialog.setContentView(view);

        dialog.show();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    /**
     * 注册完成后的弹窗，提示未审核
     */
    public static void EndRegister(final Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.end_register, null);
        TextView endsure = (TextView) view.findViewById(R.id.end_register_text);
        final Dialog dialog = new Dialog(context, R.style.CustomAlertDialog);
        dialog.setContentView(view);

        dialog.show();
        endsure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HomeActivity.class);
                intent.putExtra("init_type", 1);
                context.startActivity(intent);
                for (Activity activity : MeiMengApplication.loginActivity) {
                    activity.finish();

                }
            }
        });
    }

    /**
     * 首页私人红娘的弹窗(非会员)
     *
     * @param context
     */
    public static void setHomeDialog(final Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.homedialog, null);

        final Dialog dialog = new Dialog(context, R.style.CustomAlertDialog);
        dialog.setContentView(view);

        dialog.show();

        TextView cancelText = (TextView) view.findViewById(R.id.cancel_textbtn);
        cancelText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

        TextView open_upText = (TextView) view.findViewById(R.id.open_up_textbtn);
        open_upText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VipActivity.class);
                context.startActivity(intent);
                dialog.dismiss();
            }
        });

    }

    /**
     * 发布私人邀约功能的弹窗(非会员)
     *
     * @param context
     */
    public static void setInitiateEventDialog(final Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.homedialog, null);
        final Dialog dialog = new Dialog(context, R.style.CustomAlertDialog);
        dialog.setContentView(view);
        dialog.show();

        TextView cancelText = (TextView) view.findViewById(R.id.cancel_textbtn);
        cancelText.setText("取消");
        cancelText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        TextView open_upText = (TextView) view.findViewById(R.id.open_up_textbtn);
        open_upText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VipActivity.class);
                context.startActivity(intent);
                dialog.dismiss();
            }
        });

    }

    /**
     * 发布私人邀约功能的弹窗(会员)
     *
     * @param context
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void getInitiateEventChance(final Context context, int chance) {
        View view = LayoutInflater.from(context).inflate(R.layout.buy_convercation, null);
        final Dialog dialog = new Dialog(context, R.style.CustomAlertDialog);
        dialog.setContentView(view);
        dialog.show();
        TextView text1 = (TextView) view.findViewById(R.id.buy_convercation_text1);
        text1.setText("私人邀约");
        TextView text2 = (TextView) view.findViewById(R.id.buy_convercation_text2);
        text2.setText("您本周还剩");
        TextView times = (TextView) view.findViewById(R.id.buy_convercation_times);
        times.setText(String.valueOf(chance));
        TextView text3 = (TextView) view.findViewById(R.id.buy_convercation_text3);
        text3.setText("次机会发布私人邀约活动");
        TextView sure = (TextView) view.findViewById(R.id.buy_convercation_buy);
        sure.setText("发布");
        if (chance < 1) {
            sure.setClickable(false);
            sure.setBackground(context.getResources().getDrawable(R.drawable.text_end_shape));
        } else {
            sure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent();
                    intent.setClass(context, InitiateEvent.class);
                    context.startActivity(intent);
                    dialog.dismiss();
                }
            });
        }
        TextView cancle = (TextView) view.findViewById(R.id.buy_convercation_cancle);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    /**
     * 首页红娘专属电话（会员）
     *
     * @param context
     */
    public static void connectMatchMakerDialog(final Context context, final String number) {
        View view = LayoutInflater.from(context).inflate(R.layout.buy_convercation, null);
        final String num = number;
        TextView texte1 = (TextView) view.findViewById(R.id.buy_convercation_text1);
        texte1.setVisibility(View.GONE);
        TextView texte2 = (TextView) view.findViewById(R.id.buy_convercation_text2);
        texte2.setText("专属红娘电话：");
        TextView times = (TextView) view.findViewById(R.id.buy_convercation_times);
        times.setText(num);
        TextView text3 = (TextView) view.findViewById(R.id.buy_convercation_text3);
        text3.setVisibility(View.GONE);
        final Dialog dialog = new Dialog(context, R.style.CustomAlertDialog);
        dialog.setContentView(view);

        dialog.show();

        TextView cancelText = (TextView) view.findViewById(R.id.buy_convercation_cancle);
        cancelText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        TextView open_upText = (TextView) view.findViewById(R.id.buy_convercation_buy);
        open_upText.setText("拨打");
        open_upText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + num));
                //通知activtity处理传入的call服务
                context.startActivity(intent);
            }
        });


    }

    /**
     * 聊天时提示不是好友
     */
    public static void convercationtips(final Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.end_register, null);
        TextView endsure = (TextView) view.findViewById(R.id.end_register_text);
        TextView texte2 = (TextView) view.findViewById(R.id.end_register_text2);
        texte2.setText("您和该用户互为“中意人选”后才能开通聊天通道！");
        final Dialog dialog = new Dialog(context, R.style.CustomAlertDialog);
        dialog.setContentView(view);

        dialog.show();
        endsure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    /**
     * 系统正在分配红娘（会员）
     */
    public static void connectMatchMakerDialog2(final Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.end_register, null);
        TextView endsure = (TextView) view.findViewById(R.id.end_register_text);
        TextView texte2 = (TextView) view.findViewById(R.id.end_register_text2);

        TextView texte3 = (TextView) view.findViewById(R.id.end_register_text3);
        texte3.setVisibility(View.VISIBLE);
        texte3.setText("系统正在分配您的专属红娘，请耐心等待");
        texte2.setText("私人红娘专线：");
        final Dialog dialog = new Dialog(context, R.style.CustomAlertDialog);
        dialog.setContentView(view);

        dialog.show();
        endsure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    /**
     * 系统正在帮约（会员）
     */
    public static void confirmMatchMakerDialog2(final Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.end_register, null);
        TextView endsure = (TextView) view.findViewById(R.id.end_register_text);
        TextView texte2 = (TextView) view.findViewById(R.id.end_register_text2);
        TextView texte3 = (TextView) view.findViewById(R.id.end_register_text3);
        texte2.setText("帮约提示");
        texte3.setText("帮约中");
        texte3.setVisibility(View.VISIBLE);
        final Dialog dialog = new Dialog(context, R.style.CustomAlertDialog);
        dialog.setContentView(view);

        dialog.show();
        endsure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


    /**
     * 系统正在帮约（非会员）
     *
     * @param context
     */
    public static void confirmMatchMakerDialog(final Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.buy_convercation, null);
        TextView texte1 = (TextView) view.findViewById(R.id.buy_convercation_text1);
        texte1.setText("帮约中,请耐心等待");
        TextView texte2 = (TextView) view.findViewById(R.id.buy_convercation_text2);
        texte2.setText("官方电话：400-8783-520");
        TextView times = (TextView) view.findViewById(R.id.buy_convercation_times);
        times.setVisibility(View.GONE);
        TextView text3 = (TextView) view.findViewById(R.id.buy_convercation_text3);
        text3.setVisibility(View.GONE);
        final Dialog dialog = new Dialog(context, R.style.CustomAlertDialog);
        dialog.setContentView(view);

        dialog.show();

        TextView cancelText = (TextView) view.findViewById(R.id.buy_convercation_cancle);
        cancelText.setText("确定");
        cancelText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        TextView open_upText = (TextView) view.findViewById(R.id.buy_convercation_buy);
        open_upText.setText("购买会员");
        open_upText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, VipActivity.class);
                context.startActivity(intent);
            }
        });


    }

    /**
     * 购买聊天通道
     *
     * @param context
     */
    public static void buyConvercationDialog(final Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.buy_convercation, null);

        final Dialog dialog = new Dialog(context, R.style.CustomAlertDialog);
        dialog.setContentView(view);

        dialog.show();

        TextView cancelText = (TextView) view.findViewById(R.id.buy_convercation_cancle);
        cancelText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        TextView open_upText = (TextView) view.findViewById(R.id.buy_convercation_buy);
        open_upText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VipActivity.class);
                context.startActivity(intent);
            }
        });


    }
    /**
     * 购买聊天通道
     *
     * @param context
     */
    public static void buyOtherConvercationDialog(final Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_of_other_dialog_1, null);
        TextView tel= (TextView) view.findViewById(R.id.other_telphone);
        TextView top= (TextView) view.findViewById(R.id.top);
        TextView cancle= (TextView) view.findViewById(R.id.item_of_other_cancle);
        TextView telphone= (TextView) view.findViewById(R.id.item_of_other_tel);

        final Dialog loadingDialog = new Dialog(context, R.style.loading_dialog); // 创建自定义样式dialog
        loadingDialog.setCancelable(false);
        loadingDialog.setContentView(view);

        ProgressBean bean = new ProgressBean();
        bean.setDialog(loadingDialog);
        tel.setVisibility(View.GONE);
        cancle.setText("取消");
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.dismiss();
            }
        });
        top.setText("你 的 聊 天 机 会 已 用 完，开 通 美 盟 婚 恋 定 制 服 务，享 受 无 限 畅 聊？");
        telphone.setText("确定");
        telphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VipActivity.class);
                context.startActivity(intent);
                loadingDialog.dismiss();
            }
        });
        loadingDialog.show();


    }

    /**
     * 完善资料后确认提交
     *
     * @return
     */
    public static void registertipsDialog(final Context context) {

        View view = LayoutInflater.from(context).inflate(R.layout.register_tips, null);
        final Dialog dialog = new Dialog(context, R.style.CustomAlertDialog);
        dialog.setContentView(view);
        dialog.show();

        TextView cancelText = (TextView) view.findViewById(R.id.register_tips_cancel);
        cancelText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        TextView sureText = (TextView) view.findViewById(R.id.register_tips_sure);
        sureText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RegisterUpLoadHeadPic.class);
                context.startActivity(intent);
            }
        });

    }

    /**
     * 获取用户的uid
     *
     * @return
     */
    public static String getUserId() {
        long uid = MeiMengApplication.sharedPreferences.getLong(CommonConstants.USER_ID, -1l);
        if (uid == -1l) {
            DialogUtils.setDialog(MeiMengApplication.currentContext, "用户uid出错");
            return "";
        }
        return String.valueOf(uid);
    }

    /**
     * 获取用户的token
     *
     * @return
     */
    public static String getUserToken() {
        String token = MeiMengApplication.sharedPreferences.getString(CommonConstants.USER_TOKEN, "");
        if (TextUtils.isEmpty(token)) {
            DialogUtils.setDialog(MeiMengApplication.currentContext, "用户token出错");
            return "";
        }
        return token;
    }

    /**
     * 以最省内存的方式读取本地资源的图片
     *
     * @param context
     * @param resId
     * @return
     */
    public static void readBitMap(Context context,ImageView img, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        // 获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        Bitmap bmp=BitmapFactory.decodeStream(is,null,opt);
        img.setImageBitmap(bmp);
    }


    /**
     * 以最省内存的方式读取本地资源的图片
     *
     * @param context
     * @param resId
     * @return
     */
    public static void readBitMap(Context context,LinearLayout img, int resId) {


        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        // 获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        BitmapFactory.decodeStream(is, null, opt);
        Bitmap bmp=BitmapFactory.decodeStream(is,null,opt);
        img.setBackgroundDrawable(new BitmapDrawable(bmp));
    }

    /**
     * 质量压缩方法
     */
    private Bitmap compressQualityImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        //100代表不压缩图片，把图片保存到baos中
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        int options = 100;

        while (baos.toByteArray().length / 1024 > 1024) {

            //清空baos
            baos.reset();

            options -= 10;
            if (options < 0) {
                break;
            }

            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }

        //把压缩后的图片放在inBm中
        ByteArrayInputStream inBm = new ByteArrayInputStream(baos.toByteArray());

        //生成图片
        Bitmap bitmap = BitmapFactory.decodeStream(inBm, null, null);

        return bitmap;
    }

    /**
     * 根据图片路径按比例压缩
     *
     * @param srcPath
     * @return
     */
    private Bitmap getimage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);//此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800;
        float ww = 800;
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }

        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressQualityImage(bitmap);//压缩好比例大小后再进行质量压缩

        //直接返回，不用质量压缩也可以展示
//        return bitmap;
    }


    /**
     * 带文字的dialog
     *
     * @param context
     * @param message 要显示的文字内容
     * @return
     */
    public static Dialog createLoadingDialog(Context context, String message) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.progress_custom, null);        // 得到加载view
        TextView text = (TextView) v.findViewById(R.id.message);
        text.setText(message);
        ImageView imageView = (ImageView) v.findViewById(R.id.spinnerImageView);
        AnimationDrawable ad = (AnimationDrawable) imageView.getBackground();
        ad.start();//开始动画
        Dialog loadingDialog = new Dialog(context, R.style.loading_dialog); // 创建自定义样式dialog
        //loadingDialog.setCancelable(false);// 不可以用"返回键"取消
        loadingDialog.setContentView(v);
        return loadingDialog;
    }

    /**
     * 处理过长的字符
     */
    public static String  setString(String str,int num){

        StringBuffer sb = new StringBuffer();
        if(str.length() > num){

            sb.append(str.substring(0,num-1)).append("...");
        }else {
            sb.append(str);
        }

        return sb.toString();

    }

    /**
     * 对图片进行高斯模糊处理
     * @param context 上下文对象
     * @param image 要处理的图片ImageView
     * @param view  要设置的View
     */
    public static void applyBlur(final Context context,final ImageView image,final View view,final float radius) {
        image.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                image.getViewTreeObserver().removeOnPreDrawListener(this);
                image.buildDrawingCache();
                Bitmap bmp = image.getDrawingCache();
                blur(context,bmp, view,radius);
                return true;
            }
        });

    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private static void blur(final Context context,final Bitmap bkg,final View view,final float radiusnum) {
        long startMs = System.currentTimeMillis();
        float scaleFactor = 8;
        float radius = radiusnum;   //设置模糊的程度
////        if (downScale.isChecked()) {
//            scaleFactor = 8;
//            radius = 2;
// //       }

        Bitmap overlay = Bitmap.createBitmap((int) (view.getMeasuredWidth()/scaleFactor),
                (int) (view.getMeasuredHeight()/scaleFactor), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.translate(-view.getLeft()/scaleFactor, -view.getTop()/scaleFactor);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(bkg, 0, 0, paint);

        overlay = FastBlur.doBlur(overlay, (int)radius, true);
       //view.setBackground(new BitmapDrawable(context.getResources(), overlay));

    }

    /**
     * 过滤网址
     */
    public static ArrayList<String> getUrlStr(String str){
        String[] strs =str.split("\"");
        ArrayList<String> URLS = new ArrayList<>();
        for(String strbean : strs){

            if(strbean.contains("http")){
                URLS.add(strbean);
            }
        }
        return URLS;
    }

    public static void setPromptDialog(Context context,String str){
        //Utils.getUerVerfiy();//重新网络获取审核状态
        View view = LayoutInflater.from(context).inflate(R.layout.layout_prompt, null);

        final Dialog promptdialog  = new Dialog(context,R.style.loading_dialog);
        TextView text = (TextView) view.findViewById(R.id.prompt_text);
        text.setText(str);
        RelativeLayout sure = (RelativeLayout) view.findViewById(R.id.prompt_btn);
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptdialog.dismiss();
            }
        });
        promptdialog.setContentView(view);
        promptdialog.show();

    }
    public static void setRePromptDialog(final Context context,String str){
        //Utils.getUerVerfiy();//重新网络获取审核状态
        View view = LayoutInflater.from(context).inflate(R.layout.layout_promptre, null);
        final Dialog promptdialog  = new Dialog(context,R.style.loading_dialog);
        promptdialog.setCancelable(false);
        TextView myText = (TextView)view.findViewById(R.id.prompt_text_re);
        myText.setText(str);
        RelativeLayout sure = (RelativeLayout) view.findViewById(R.id.prompt_btnre);
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SharedPreferences.Editor editor = MeiMengApplication.sharedPreferences.edit();
                editor.putLong(CommonConstants.USER_ID, -1).commit();
                editor.putString(CommonConstants.USER_PASSWORD, "").commit();
                Intent intent = new Intent(context, RegisterActivity.class);
                context.startActivity(intent);
                promptdialog.dismiss();
                timeOutCloseDialog();
            }
        });
        promptdialog.setContentView(view);
        promptdialog.show();
    }



    /**
     * 网络超时提示
     */
    private static void timeOutCloseDialog() {
        Timer timer = new Timer();
        TimerTask tk = new TimerTask() {
            @Override
            public void run() {
                mhandler.sendEmptyMessage(74747);
            }
        };
        timer.schedule(tk, 2000);
    }

    private static Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 74747) {
                ActivityCollector.finishAll();
            } else {
                //不做操作
            }
        }
    };
    public static void setPromptDialogSystemMessage(Context context,String str){
       //Utils.getUerVerfiy();//重新网络获取审核状态
        View view = LayoutInflater.from(context).inflate(R.layout.layout_prompt_system_message, null);

       final Dialog promptdialog  = new Dialog(context,R.style.loading_dialog);
        TextView text = (TextView) view.findViewById(R.id.prompt_text_system_message);
        text.setText(str);
        RelativeLayout sure = (RelativeLayout) view.findViewById(R.id.prompt_btn);
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptdialog.dismiss();
            }
        });
        promptdialog.setContentView(view);
        promptdialog.show();

    }

}
