package com.example.com.meimeng.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

/**
 * Created by WangHaohan on 2016/5/25.
 *
 * @author android客户端-WangHaohan
 * @version V1.0
 * @Description: TODO
 * Copyright: Copyright (c) 2016
 * Company: 成都壹柒互动科技有限公司
 * @date 2016/5/25 13:30
 */
public class ChannelUtils {

    public static String getChannelCode(Context context) {

        String code = getMetaData(context, "CHANNEL");

        if (code != null) {

            return code;

        }

        return "toutiao";

    }


    private static String getMetaData(Context context, String key) {

        try {

            ApplicationInfo  ai = context.getPackageManager().getApplicationInfo(

                    context.getPackageName(), PackageManager.GET_META_DATA);

            Object value = ai.metaData.get(key);

            if (value != null) {

                return value.toString();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

}
