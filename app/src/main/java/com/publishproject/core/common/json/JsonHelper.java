package com.publishproject.core.common.json;


import com.publishproject.core.common.logger.LogUtil;

import java.util.List;

/**
 * @author Android客户端组-tanghongbin
 * @Title: JsonHelper
 * @Package com.commonconfig.jsonconfigs
 * Copyright: Copyright (c) 2016
 * Company: 成都壹柒互动科技有限公司
 * @date 2016/12/7 10:07
 *
 *
 *
 * @Description: 利用helper类方便以后升级更新,使用代理，
 * 提供方法：
 * 1.parsetObject(json,t),将指定的json字符串解析成指定类-json(json字符串),
 * t(指定解析的对象类型，这里指定object对象，不包括list，如想解析成集合，使用parseJSONArray)
 *
 *
 * 2.parseJSONArray(json,t)将指定json字符串解析成集合,json-json字符串,
 * t-指定解析的List容器内的对象的类型
 *
 * 3.toJSONString(t),t-将要解析成字符串的对象类型，可以是自定义类型或者是List
 * 容器子类，如果要将Map和JSON互换，则可以使用ConvertJSONFromMap类
 *
 */
public final class JsonHelper {
    private static GsonInterface gson;
    private static GsonInterface getInstance(){
        if(gson == null){
            synchronized (JsonHelper.class){
                if (gson == null) {
                    gson = new GoogleGson();
                }
            }
        }
        return gson;
    }

    /**
     *
     * @param json
     * @param t
     * @param <T>
     * @return
     */
    public static <T> T parseObject(String json,T t){
       try{
           if(json == null || t == null){
               LogUtil.detail(JsonHelper.class,"parseObject","指定解析类为空或者参数为空--classz:"+json+"---t:"+t);
               return null;
           }
           return   getInstance().parseObject(json,t);
       }catch (Exception e){
           LogUtil.e("TAG",e.getMessage());
           e.printStackTrace();
           return null;
       }
    }
    public static  <T> String toJSONString(T t){
        if(t == null){
            return null;
        }
       return getInstance().toJSONString(t);
    }
    public static <T> List<T> parseJSONArray(String json,T t){
        if(json == null || t == null){
            LogUtil.detail(JsonHelper.class,"parseJSONArray","指定解析类为空或者参数为空--classz:"+json+"---t:"+t);
            return null;
        }
       return getInstance().parseJSONArray(json,t);
    }
}
