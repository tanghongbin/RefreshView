package com.publishproject.core.commonconfig.busconfigs;

/**
 * @author Android客户端组-tanghongbin
 * @Title: BusInterface
 * @Package com.commonconfig.busconfigs
 * Copyright: Copyright (c) 2016
 * Company: 成都壹柒互动科技有限公司
 * @date 2016/12/8 16:07
 * @Description: 所有事件注册库的接口，
 * 提供注册事件，取消注册事件，发送事件
 */
public interface BusInterface {
    void regist(Object o);
    void unRegiest(Object o);
    void postEvent(Object event);
    void postEvent(String tag, Object event);
}
