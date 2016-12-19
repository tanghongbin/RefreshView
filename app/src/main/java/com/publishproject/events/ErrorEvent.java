package com.publishproject.events;

/**
 * @author Android客户端组-tanghongbin
 * @Title: ErrorEvent
 * @Package com.publishproject.core.commonconfig.busconfigs.bustype
 * Copyright: Copyright (c) 2016
 * Company: 成都壹柒互动科技有限公司
 * @date 2016/12/13 17:55
 * @Description: TODO
 */
public class ErrorEvent {
    private String message;

    public ErrorEvent() {
    }

    public ErrorEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
