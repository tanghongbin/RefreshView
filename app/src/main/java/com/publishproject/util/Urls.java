package com.publishproject.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.publishproject.BuildConfig;
import com.publishproject.YqApplication;

/**
 * @author Android客户端组-tanghongbin
 * @Title: Urls
 * @Package com.publishproject.util
 * Copyright: Copyright (c) 2016
 * Company: 成都壹柒互动科技有限公司
 * @date 2016/12/15 17:13
 * @Description: TODO
 */
public class Urls {
    private static final String SAVE_DEBUG_URL = "YG_save_debug_url";
    private static final String SAVE_DEBUG_URL_INSTANCE = "save_debug_url_instance";

    // 生产环境地址
    private String PUBLISH_SERVER = "http://114.55.173.214:8080";
//            "/jeecg/xingruiBackendController.do?general";

    // 获取当前环境的URL地址
    public String getServerUrl() {
        return BuildConfig.DEBUG ? ("http://" + getDebugDomain() + ":8080") : PUBLISH_SERVER;
    }

    public static String getDebugDomain() {
        SharedPreferences refrences = YqApplication.getInstance().getSharedPreferences(SAVE_DEBUG_URL, Context.MODE_PRIVATE);
        return refrences.getString(SAVE_DEBUG_URL_INSTANCE, "120.76.226.150");
    }


    public static void saveDebugDomain(String url) {
        SharedPreferences refrences = YqApplication.getInstance().getSharedPreferences(SAVE_DEBUG_URL, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = refrences.edit();
        editor.putString(SAVE_DEBUG_URL_INSTANCE,url);
        editor.commit();
    }

}
