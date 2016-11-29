package com.example.com.meimeng.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.com.meimeng.MeiMengApplication;
import com.example.com.meimeng.R;
import com.example.com.meimeng.activity.PayActivity;
import com.example.com.meimeng.activity.RegisterActivity;
import com.example.com.meimeng.bean.ProgressBean;
import com.example.com.meimeng.constants.CommonConstants;
import com.example.com.meimeng.gson.gsonbean.MatchMakerGetBean;

/**
 * Created by 003 on 2015/8/11.
 * 用于各种dialog的显示
 */
public class DialogUtils {

    /**
     * 不带文字的dialog
     * 在用到的地方加上
     * dialog = DialogUtils.createLoadingDialog(this,"正在上传图片...");
     * dialog.show();
     *
     * @param context
     * @return
     */

    public static Dialog createLoadingDialog(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.progress_custom, null);   // 得到加载view
        TextView text = (TextView) v.findViewById(R.id.message);
        text.setVisibility(View.GONE);
        ImageView imageView = (ImageView) v.findViewById(R.id.spinnerImageView);
        AnimationDrawable ad = (AnimationDrawable) imageView.getBackground();
        ad.start();//开始动画
        Dialog loadingDialog = new Dialog(context, R.style.loading_dialog); // 创建自定义样式dialog
        //loadingDialog.setCancelable(false);// 不可以用"返回键"取消
        loadingDialog.setContentView(v);
        return loadingDialog;
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


    public static ProgressBean createLoadingDialog2(Context context, String message) {
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

        ProgressBean bean = new ProgressBean();
        bean.setDialog(loadingDialog);
        bean.setTextView(text);
        return bean;
    }
    public static void matchMakerDialog(final Context context, final MatchMakerGetBean baseBean) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_of_other_dialog_1, null);        // 得到加载view
        TextView tel= (TextView) view.findViewById(R.id.other_telphone);
        TextView cancle= (TextView) view.findViewById(R.id.item_of_other_cancle);
        TextView telphone= (TextView) view.findViewById(R.id.item_of_other_tel);
        final Dialog loadingDialog = new Dialog(context, R.style.loading_dialog); // 创建自定义样式dialog
        loadingDialog.setCancelable(false);// 不可以用"返回键"取消
        loadingDialog.setContentView(view);

        ProgressBean bean = new ProgressBean();
        bean.setDialog(loadingDialog);
        tel.setText(baseBean.getParam().getMatchmaker().getTel());
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.dismiss();
            }
        });
        telphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + baseBean.getParam().getMatchmaker().getTel()));
                context.startActivity(intent);
                loadingDialog.dismiss();
            }
        });
        loadingDialog.show();
    }
    public static void vipDialog(final Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_of_other_dialog_1, null);        // 得到加载view
        TextView tel= (TextView) view.findViewById(R.id.other_telphone);
        TextView cancle= (TextView) view.findViewById(R.id.item_of_other_cancle);
        TextView telphone= (TextView) view.findViewById(R.id.item_of_other_tel);
        final Dialog loadingDialog = new Dialog(context, R.style.loading_dialog); // 创建自定义样式dialog
        //loadingDialog.setCancelable(false);// 不可以用"返回键"取消
        loadingDialog.setContentView(view);

        ProgressBean bean = new ProgressBean();
        bean.setDialog(loadingDialog);
        tel.setText("tel:400-8783-520");
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.dismiss();
            }
        });
        telphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:400-8783-520"));
                context.startActivity(intent);
                loadingDialog.dismiss();
            }
        });
        loadingDialog.show();
    }
    public static void setActDialog(final Context context, final long activityId, final double type, final String name, final int aType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_of_other_dialog_1, null);        // 得到加载view
        TextView tel= (TextView) view.findViewById(R.id.other_telphone);
        TextView top= (TextView) view.findViewById(R.id.top);
        TextView cancle= (TextView) view.findViewById(R.id.item_of_other_cancle);
        TextView telphone= (TextView) view.findViewById(R.id.item_of_other_tel);
        final Dialog loadingDialog = new Dialog(context, R.style.loading_dialog); // 创建自定义样式dialog
        //loadingDialog.setCancelable(false);// 不可以用"返回键"取消
        loadingDialog.setContentView(view);

        ProgressBean bean = new ProgressBean();
        bean.setDialog(loadingDialog);
        tel.setText("报名成功");
        top.setText("温馨提示");
        telphone.setText("支付");
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.dismiss();
            }
        });
        telphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //支付页面
                MeiMengApplication.weixinPayCallBack = aType;
                Intent intent = new Intent(context, PayActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("price", "" + type);
                intent.putExtra("goodId", -2l);
                intent.putExtra("targetUid", activityId);
                intent.putExtra("pay_type", 3);
                context.startActivity(intent);
                loadingDialog.dismiss();
            }
        });
        loadingDialog.show();
    }

    /**
     * 弹出重新登录的对话框
     * @param context
     */
    public static void setDialog(final Context context,String message) {
        final SharedPreferences.Editor editor = MeiMengApplication.sharedPreferences.edit();
        if (message.equals("未登录")){
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setMessage(message);
            dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    editor.putBoolean(CommonConstants.LOGGIN_STATE,false).commit();
                    editor.putLong(CommonConstants.USER_ID, -1).commit();
                    Intent intent = new Intent(context, RegisterActivity.class);
                    context.startActivity(intent);
                    ActivityCollector.finishAll();
                }
            }).show();

        }

        if(message.equals("帮约中")){
            Utils.confirmMatchMakerDialog2(context);
        }
      /*  else{
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }*/

    }
}
