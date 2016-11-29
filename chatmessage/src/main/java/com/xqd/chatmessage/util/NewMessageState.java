package com.xqd.chatmessage.util;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by Administrator on 2015/9/18.
 * 编辑者：邓成博，此类用于接收消息状态的参数共享，采用单态模式。
 */
public class NewMessageState {
    private Context context;
    private static NewMessageState nms = null;
    private static SharedPreferences sp = null;
    private static String name = "HN";

    public static synchronized NewMessageState getInstance(Context context) {


        if (nms == null ) {
            nms = new NewMessageState(context);
        }
        return nms;
    }

    private NewMessageState(Context context) {
        this.context = context;

        sp = this.context.getSharedPreferences("NewMessage_state_" + name, Context.MODE_PRIVATE);
    }

    public void setState(String UserId, int state) {
        sp.edit().putInt(UserId, state).commit();
    }

    public int getState(String UserId) {
        return sp.getInt(UserId, -1);
    }

    public void deleteId(String UserId) {
        sp.edit().remove(UserId).commit();
    }


}
