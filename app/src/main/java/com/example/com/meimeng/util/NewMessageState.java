package com.example.com.meimeng.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.com.meimeng.MeiMengApplication;
import com.example.com.meimeng.constants.CommonConstants;

/**
 * Created by Administrator on 2015/9/18.
 * 编辑者：邓成博，此类用于接收消息状态的参数共享，采用单态模式。
 */
public class NewMessageState {
    private Context context;
    private static NewMessageState nms = null;
    private static SharedPreferences sp = null;
    private static String name;

    public static synchronized NewMessageState getInstance(Context context) {

        Log.i("mdsg", name + ",    " + MeiMengApplication.sharedPreferences.getLong(CommonConstants.USER_ID, -1) + "");
        if (nms == null || (!name.equals(MeiMengApplication.sharedPreferences.getLong(CommonConstants.USER_ID, -1) + ""))) {
            nms = new NewMessageState(context);
        }
        return nms;
    }

    private NewMessageState(Context context) {
        this.context = context;
        name = MeiMengApplication.sharedPreferences.getLong(CommonConstants.USER_ID, -1) + "";
        sp = this.context.getSharedPreferences("NewMessage_state_" + name, Context.MODE_PRIVATE);
    }

    public void setState(String UserId, int state) {
        sp.edit().putInt(UserId, state).commit();
    }

    public int getState(String UserId) {
        Log.d("wz","红点初始值："+sp.getInt(UserId, -1));
        return sp.getInt(UserId, -1);

    }

    public void deleteId(String UserId) {
        sp.edit().remove(UserId).commit();
    }


}
