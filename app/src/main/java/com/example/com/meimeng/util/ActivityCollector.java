package com.example.com.meimeng.util;

import android.app.Activity;

import java.util.ArrayList;

/**
 * Created by 010 on 2015/8/26.
 * activity收集器，及处理器
 */
public class ActivityCollector {

    public static ArrayList<Activity> activityCollector = new ArrayList<>();

    /**
     * 添加activity
     * @param activity
     */
    public static void addActivity(Activity activity){
        activityCollector.add(activity);
    }

    /**
     * 移除某个activity
     *@param activity
     */
    public static void removeActivity(Activity activity){
        activityCollector.remove(activity);
    }


    /**
     * 销毁所有的activity
     */
    public static void finishAll(){
        for (Activity activity:activityCollector){
            if (!activity.isFinishing()){
                activity.finish();
            }
        }
    }
}
