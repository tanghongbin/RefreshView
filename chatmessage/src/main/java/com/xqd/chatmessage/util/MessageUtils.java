package com.xqd.chatmessage.util;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avoscloud.leanchatlib.activity.ChatActivity;
import com.avoscloud.leanchatlib.controller.ChatManager;
import com.xqd.chatmessage.ChatAplication;
import com.xqd.chatmessage.activity.ChatRoomActivity;
import com.xqd.chatmessage.net.InternetConstant;
import com.xqd.chatmessage.net.InternetUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by Administrator on 2015/9/9.
 */
public class MessageUtils {

    /**
     * 聊天功能，把自己的uid传给leanCloud
     */
    public static void setLeanCloudSelfUid() {
        if (TextUtils.isEmpty(Utils.getUserId())) {
            return;
        }
        if (TextUtils.isEmpty(Utils.getUserToken())) {
            return;
        }
        String selfId = "hn" + String.valueOf(Utils.getUserId());

        if (!TextUtils.isEmpty(selfId)) {
            ChatManager chatManager = ChatManager.getInstance();
            chatManager.setupDatabaseWithSelfId(selfId);
            chatManager.openClientWithSelfId(selfId, new AVIMClientCallback() {

                @Override
                public void done(AVIMClient avimClient, AVIMException e) {
                    if (e != null) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public static void setLeanCloudOtherUid(final Context context, final long uid) {
        final String s ;
        final String otherId = String.valueOf(uid);

        if (TextUtils.isEmpty(otherId) == true) {
            return;
        }
        try {
            if (TextUtils.isEmpty(Utils.getUserId())) {
                return;
            }
            if (TextUtils.isEmpty(Utils.getUserToken())) {
                return;
            }
            String url = InternetConstant.SERVER_URL + InternetConstant.AUTH_CHAT + "?token=" + Utils.getUserToken() + "&uid=" + Utils.getUserId();
            JSONStringer jsonStringer = new JSONStringer().object().key("uid").value(uid).endObject();
            String jsonStr = jsonStringer.toString();
            //得到Observable并获取返回的数据(主线程中)
            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
                            observable.observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Action1<String>() {

                                        @Override
                                        public void call(String s) {
                                            Log.i("qwe", s);
                                            switch (getJsonData(s)) {

                                                case 1:
                                                    Utils.setDialog(context);
                                                    break;
                                                case 2:
                                                final ChatManager chatManager = ChatManager.getInstance();
                                                chatManager.fetchConversationWithUserId(otherId,new AVIMConversationCreatedCallback() {
                                                    @Override
                                                    public void done(AVIMConversation avimConversation, AVIMException e) {
                                                        if (e != null) {
                                                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                                                        } else {
                                                            ChatAplication.Uid.setUid(uid + "");
                                                            chatManager.registerConversation(avimConversation);
                                                            Intent intent = new Intent(context, ChatRoomActivity.class);
                                                            intent.putExtra(ChatActivity.CONVID, avimConversation.getConversationId());
                                                            intent.putExtra("otherid", String.valueOf(uid));
                                                            context.startActivity(intent);
                                                            ChatAplication.isSound = 2;
                                                        }
                                                    }


                                                });
                                                    break;
                                                case 3:
                                                    Utils.convercationtips(context, uid);
                                                    break;
                                            }

                                        }
                                    });
        }catch (Exception e){
                Toast.makeText(context, "出错了",Toast.LENGTH_SHORT).show();
        }
    }

    public static int getJsonData(String s){
        Boolean flag_param = false ;
        Boolean flag_success = false;
        int num = 0;
        try {
            JSONObject obj = new JSONObject(s);
           JSONObject param = obj.getJSONObject("param");
            flag_param = param.getBoolean("canChat");
            flag_success = obj.getBoolean("success");
   //         Log.i("qwe",flag_success+"");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(!flag_success){
            num = 1;    //表示以异地登陆
        }else  if (flag_param){
            num = 2;    //表示正常
        }else {
            num = 3;    //表示不可聊天
        }
        Log.i("qwe",num+"");
        return num;
    }
}
