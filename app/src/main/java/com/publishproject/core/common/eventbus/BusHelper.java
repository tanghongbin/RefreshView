package com.publishproject.core.common.eventbus;


import com.publishproject.core.common.eventbus.bustype.RxBus;

/**
 * @author Android客户端组-tanghongbin
 * @Title: BusHelper
 * @Package com.commonconfig.busconfigs
 * Copyright: Copyright (c) 2016
 * Company: 成都壹柒互动科技有限公司
 * @date 2016/12/6 17:35
 * @Description: 事件总线库的帮助类,使用代理控制对事件底层框架的访问，
 * 方便以后替换，提供#{regiest()}-注册事件;
 * unregiest-取消注册;
 * postEvent-发送事件
 *
 */
public class BusHelper {

    private static BusInterface bus;
    private static BusInterface getInstance(){
        if(bus == null){
            synchronized (BusHelper.class){
                if(bus == null){
                    bus = new RxBus();
                }
            }
        }
        return bus;
    }
    //注册事件消费者
    public static void registe(Object o){
        getInstance().regist(o);
    }
    //撤销事件消费者
    public static void unRegiste(Object o){
        getInstance().unRegiest(o);
    }
    /**
     * 发送事件
     * */
    public static void postEvent(Object o){
        getInstance().postEvent(o);
    }
    /**
     * 发送事件
     * */
    public static void postEvent(String tag,Object o){
        getInstance().postEvent(tag,o);
    }
}
