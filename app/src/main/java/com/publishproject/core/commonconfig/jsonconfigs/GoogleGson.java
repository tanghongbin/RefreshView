package com.publishproject.core.commonconfig.jsonconfigs;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.publishproject.core.commonconfig.loggerconfigs.LogUtil;

import java.util.List;

/**
 * @author Android客户端组-tanghongbin
 * @Title: GoogleGson
 * @Package com.commonconfig.jsonconfigs
 * @Description: 具体json解析类
 * Copyright: Copyright (c) 2016
 * Company: 成都壹柒互动科技有限公司
 * @date 2016/12/8 15:58
 */
public class GoogleGson implements GsonInterface {

    private Gson gson;
    public GoogleGson() {
        gson = new Gson();
    }

    @Override
    public <T> T parseObject(String json, T t) {
        try{
            return (T) gson.fromJson(json,t.getClass());
        }catch (Exception e){
            LogUtil.e("TAG",e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public <T> String toJSONString(T t) {
        return gson.toJson(t);
    }

    @Override
    public <T> List<T> parseJSONArray(String jsonArray, T t) {
        return gson.fromJson(jsonArray, new TypeToken<List<T>>() {
        }.getType());
    }
}
