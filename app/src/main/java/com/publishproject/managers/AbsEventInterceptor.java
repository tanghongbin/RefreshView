package com.publishproject.managers;

import com.publishproject.core.common.eventbus.BusHelper;

/**
 * @author Android客户端组-tanghongbin
 * @Title: AbsEventInterceptor
 * @Package com.publishproject.managers
 * Copyright: Copyright (c) 2016
 * Company: 成都壹柒互动科技有限公司
 * @date 2016/12/23 14:28
 * @Description: 事件拦截器,根据传入事件自己决定是否拦截，不进行发送
 */
public abstract class AbsEventInterceptor<T> {
    T event;

    public AbsEventInterceptor(T event) {
        this.event = event;
    }
    public abstract boolean isIntercept();
    //根据拦截条件判断是否拦截事件
    public void post(){
        if (!isIntercept()){
            BusHelper.postEvent(event);
        }
    }
}
