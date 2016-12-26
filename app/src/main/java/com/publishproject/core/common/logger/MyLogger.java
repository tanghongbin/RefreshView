package com.publishproject.core.common.logger;

import android.util.Log;

import com.publishproject.BuildConfig;

/**
 * @author Android客户端组-tanghongbin
 * @Title: MyLogger
 * @Package com.publishproject.core.commonconfig.loggerconfigs
 * Copyright: Copyright (c) 2016
 * Company: 成都壹柒互动科技有限公司
 * @date 2016/12/15 11:44
 * @Description: TODO
 */
public class MyLogger implements LoggerInterface {

    @Override
    public void i(String tag, String message) {
        if(isDebug()){
            Log.i(tag,message);
        }
    }

    @Override
    public void e(String tag, String message) {
        if(isDebug()){
            Log.e(tag,message);
        }
    }
    @Override
    public void detail(Class c, String name, String error){
        e("TAG","抛出异常-->类:"+c.getSimpleName()+"-----方法:"+name+"---错误详情:"+error);
    }
    @Override
    public  boolean isDebug(){
        return BuildConfig.DEBUG;
    }
}
