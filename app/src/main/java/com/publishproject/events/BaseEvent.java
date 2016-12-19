package com.publishproject.events;

/**
 * @author Android客户端组-tanghongbin
 * @Title: BaseEvent
 * @Package com.publishproject.core.commonconfig.netconfigs.callbacks
 * Copyright: Copyright (c) 2016
 * Company: 成都壹柒互动科技有限公司
 * @date 2016/12/14 16:15
 * @Description: 基础事件类型，请求结果：data，错误事件类型ERROR,su
 */
public class BaseEvent<ERROR extends ErrorEvent> extends JudgeResultEvent{
    private String data;
    private ERROR error;

    public BaseEvent() {
    }

    public BaseEvent(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public ERROR getError() {
        return error;
    }

    public void setError(ERROR error) {
        this.error = error;
    }

    @Override
    public boolean isSuccess() {
        return error == null;
    }
}
