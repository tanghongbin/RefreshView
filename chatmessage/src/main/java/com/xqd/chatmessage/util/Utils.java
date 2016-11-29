package com.xqd.chatmessage.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.text.TextUtils;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avoscloud.leanchatlib.controller.MessageHelper;
import com.xqd.chatmessage.ChatAplication;
import com.xqd.chatmessage.R;

import com.xqd.chatmessage.activity.LoginActivity;


/**
 * Created by Administrator on 2015/9/9.
 */
public class Utils {
    public static String getUserId() {
        long uid = ChatAplication.sharedPreferences.getLong( CommonConstants.USER_ID, -1l);
        if (uid == -1l) {

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
        String token = ChatAplication.sharedPreferences.getString(CommonConstants.USER_TOKEN, "");
        if (TextUtils.isEmpty(token)) {
//            DialogUtils.setDialog(MeiMengApplication.currentContext, "用户token出错");
            return "";
        }
        return token;
    }

    /**
     * 聊天时提示不是好友
     */
    public static void convercationtips(final Context context,final Long uid) {
        View view = LayoutInflater.from(context).inflate(R.layout.end_register, null);
        TextView endsure = (TextView) view.findViewById(R.id.end_register_text);
        TextView texte2 = (TextView) view.findViewById(R.id.end_register_text2);
  //      texte2.setText("该用户已经不是您所负责用户，点击确认，删除该用户的会话信息。");
        texte2.setText("该用户已经不是您所负责用户。");
        final Dialog dialog = new Dialog(context, R.style.CustomAlertDialog);
        dialog.setContentView(view);

        dialog.show();
        endsure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               ChatSQliteDataUtil sp = new ChatSQliteDataUtil();
//                for (int i=0 ; i < HomeActivity.dataList.size() ; i++){
//                   Long id = HomeActivity.dataList.get(i).getUserId();
//
//                    if(uid.equals(id)){
//                        HomeActivity.dataList.remove(i);
//                    }
//                }
//                if(MessageFragment.chatMessageAdapter != null){
//
//                    MessageFragment.chatMessageAdapter.setDataList(HomeActivity.dataList);
//                    MessageFragment.chatMessageAdapter.notifyDataSetChanged();
//                }
//                sp.deleteData("userid",uid+"");
                dialog.hide();
            }
        });
    }

    public static void setDialog(final Context context) {


            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setMessage("未登录");
            dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                    ActivityCollector.finishAll();

                }
            }).show();

    }

    public CharSequence getContent(AVIMTypedMessage message) {
        CharSequence content = MessageHelper.outlineOfMsg(message);
        if (content.toString().matches("^((:[a-z]+:)*)$")) {
            content = "[表情]";
        }
        return content;
    }
}
