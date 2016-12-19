package com.publishproject.core.commonconfig.loggerconfigs;

import android.text.TextUtils;
import android.util.Log;

import com.publishproject.BuildConfig;


/**
 * @author Android客户端组-tanghongbin
 * @Title: LogUtil
 * @Package com.commonconfig.internallog
 * Copyright: Copyright (c) 2016
 * Company: 成都壹柒互动科技有限公司
 * @date 2016/12/7 10:27
 * @Description: 日志工具类
 * i()普通打印
 * e()错误打印
 * detail（）详细打印
 * setTag()可使用此类提供的标签或者设置标签
 * openLogger()是否开启日志打印,
 * ,如果设置logEnum状态：
 * NONE,则默认在debug状态下开启，release状态下关闭
 * OPEN，不管是发布还是调试都打开
 * CLOSE，不管是发布还是调试都关闭
 */
public final class LogUtil {
    private static String APP_TAG = "App_TAG";
    private static LoggerInterface loggerInterface;
    private static LogEnum logEnum = LogEnum.NONE;

    private static LoggerInterface getInstance() {
        if (loggerInterface == null) {
            synchronized (LogUtil.class) {
                if (loggerInterface == null) {
                    loggerInterface = new MyLogger();
                }
            }
        }
        return loggerInterface;
    }

    public static void setTag(String tag) {
        APP_TAG = tag;
    }

    public static void i(final String message) {


        if(!controlCondition()){
            return;
        }
        getInstance().i(APP_TAG, message);

    }

    /**
     * 普通打印
     *
     * @param tag
     * @param message
     */
    public static void i(String tag, String message) {
        if (TextUtils.isEmpty(message)) {
            return;
        }
        if(!controlCondition()){
            return;
        }
        if (TextUtils.isEmpty(tag)) {
            i(message);
        }else {
            getInstance().i(tag,message);
        }
    }

    public static void e(String message) {
        if(!controlCondition()){
            return;
        }
        getInstance().e(APP_TAG, message);
    }

    public static void e(String tag, String message) {
        if (TextUtils.isEmpty(message)) {
            return;
        }
        if(!controlCondition()){
            return;
        }
        if(TextUtils.isEmpty(tag)){
            e(message);
        }else {
            getInstance().e(tag,message);
        }
    }

    /**
     * 日志详细打印
     *
     * @param c          出错的类名
     * @param methodName 方法名
     * @param error      错误的详细信息
     */
    public static void detail(Class c, String methodName, String error) {
        if (TextUtils.isEmpty(error)) {
            return;
        }
        if(!controlCondition()){
            return;
        }
        getInstance().detail(c, methodName, error);
    }

    private static boolean controlCondition() {

        switch (logEnum) {
            case NONE:
                if (BuildConfig.DEBUG) {
                    return true;
                } else {
                    return false;
                }
            case OPEN:
                return true;
            case CLOSE:

                return false;
            default:

                return true;
        }

    }


    public void setLoggerStatus(LogEnum loggerStatus) {
        logEnum = loggerStatus;
    }

    /**
     * NONE,则默认在debug状态下开启，release状态下关闭
     * OPEN，不管是发布还是调试都打开
     * CLOSE，不管是发布还是调试都关闭
     */
    public enum LogEnum {
        NONE,
        OPEN,
        CLOSE
    }


}
