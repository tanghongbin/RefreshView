package com.publishproject.util;

/**
 * @author Android客户端组-tanghongbin
 * @Title: ClassUtil
 * @Package com.publishproject.util
 * Copyright: Copyright (c) 2016
 * Company: 成都壹柒互动科技有限公司
 * @date 2016/12/23 14:21
 * @Description: TODO
 */
public class ClassUtil {
    public static  <T>T getInstance(Class<T> classType){
        T event = null;
        try {
            event = classType.newInstance();
            return event;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            return event;
        }
    }
}
