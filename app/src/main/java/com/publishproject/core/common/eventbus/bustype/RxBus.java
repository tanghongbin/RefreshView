package com.publishproject.core.common.eventbus.bustype;

import com.hwangjr.rxbus.Bus;
import com.publishproject.core.common.eventbus.BusInterface;


/**
 * @author Android客户端组-tanghongbin
 * @Title: RxBus
 * @Package com.publishproject.core.commonconfig.busconfigs.bustype
 * Copyright: Copyright (c) 2016
 * Company: 成都壹柒互动科技有限公司
 * @date 2016/12/15 16:49
 * @Description: TODO
 */
public class RxBus implements BusInterface {
    private static Bus bus = new Bus();


    @Override
    public void regist(Object o) {
        bus.register(o);
    }

    @Override
    public void unRegiest(Object o) {
        bus.unregister(o);
    }

    @Override
    public void postEvent(Object event) {
        bus.post(event);
    }

    @Override
    public void postEvent(String tag, Object event) {
        bus.post(event);
    }
}
