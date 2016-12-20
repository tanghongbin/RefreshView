package com.publishproject.util;

import android.text.TextUtils;

import com.publishproject.core.common.json.JsonHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author Android客户端组-tanghongbin
 * @Title: ConvertJSONFromMap
 * @Package com.publishproject.util
 * @Description: map 和json的转换
 * Copyright: Copyright (c) 2016
 * Company: 成都壹柒互动科技有限公司
 * @date 2016/12/12 14:28
 */
public class ConvertJSONFromMap {
    public static JSONObject convertToJSON(Map<String,Object> objectMap){
        if(objectMap == null){
            return null;
        }
        JSONObject object = new JSONObject();
        Set<Map.Entry<String, Object>> sets = objectMap.entrySet();
        for(Map.Entry<String, Object> entry : sets){
            if(TextUtils.isEmpty(entry.getKey())){
                continue;
            }
            try {
                object.put(entry.getKey(), JsonHelper.toJSONString(entry.getValue()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return object;
    }

    public static Map<String,Object> convertToMap(JSONObject jsonObject){
        if(jsonObject == null){
            return null;
        }
        Map<String,Object> map = new HashMap<>();
        Iterator<String> jsons = jsonObject.keys();
        while (jsons.hasNext()){
            String key = jsons.next();
            if(TextUtils.isEmpty(key)){
                continue;
            }
            try {
                Object value = jsonObject.get(key);
                map.put(key,value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return map;
    }
}
