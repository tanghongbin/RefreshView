package com.publishproject;

import android.app.Application;

/**
 * @author Android客户端组-tanghongbin
 * @Title: YqApplication
 * @Package com.publishproject
 * Copyright: Copyright (c) 2016
 * Company: 成都壹柒互动科技有限公司
 * @date 2016/12/15 17:15
 * @Description: TODO
 */
public class YqApplication extends Application {
    private static Application instance;
    public static Application getInstance(){
        return instance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
