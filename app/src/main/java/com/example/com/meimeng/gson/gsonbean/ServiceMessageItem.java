package com.example.com.meimeng.gson.gsonbean;

import com.example.com.meimeng.bean.ContextBean;

/**
 * Created by 010 on 2015/8/13.
 */
public class ServiceMessageItem {

    private int messageId;

    private long senderId;

    public long getSenderId() {
        return senderId;
    }

    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    private String senderNick;

    private String senderHeadPic;

//    private ServiceMessageContext context;

    private String status;

    private int type;

    private ContextBean context;

    private long sendTime;

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }



    public String getSenderNick() {
        return senderNick;
    }

    public void setSenderNick(String senderNick) {
        this.senderNick = senderNick;
    }

    public String getSenderHeadPic() {
        return senderHeadPic;
    }

    public void setSenderHeadPic(String senderHeadPic) {
        this.senderHeadPic = senderHeadPic;
    }

    public ContextBean getContext() {
        return context;
    }

    public void setContext(ContextBean context) {
        this.context = context;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getSendTime() {
        return sendTime;
    }

    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }
}
