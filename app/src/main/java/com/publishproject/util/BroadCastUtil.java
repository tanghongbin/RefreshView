package com.publishproject.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;

/**
 * @author Android客户端组-tanghongbin
 * @Title: BroadCastUtil
 * @Package com.publishproject.util
 * Copyright: Copyright (c) 2016
 * Company: 成都壹柒互动科技有限公司
 * @date 2016/12/15 17:47
 * @Description: 广播处理类
 */
public class BroadCastUtil {
    /**
     * 注册广播
     * @param context
     * @param receiver
     * @param actions
     */
    public static void registReceiver(Context context, BroadcastReceiver receiver,String... actions){
        IntentFilter filter = new IntentFilter();
        for(String action:actions){
            filter.addAction(action);
        }
        context.registerReceiver(receiver,filter);
    }

    public static void registReceiver(Context context, BroadcastReceiver receiver,String[] categories,String... actions){
        IntentFilter filter = new IntentFilter();
        for(String category:categories){
            filter.addCategory(category);
        }
        for(String action:actions){
            filter.addAction(action);
        }
        context.registerReceiver(receiver,filter);
    }

    /**
     * 取消注册
     * @param context
     * @param receiver
     */
    public static void unRegistReceiver(Context context,BroadcastReceiver receiver){
        context.unregisterReceiver(receiver);
    }
}
