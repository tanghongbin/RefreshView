package com.xqd.chatmessage.gson;

import com.avos.avoscloud.im.v2.AVIMConversation;

import java.io.Serializable;

/**
 * Created by 010 on 2015/7/13.
 */
public class ChatMessageBean implements Serializable {

    private long userId;

    private String headerUrl;

    private Long headPic ;

    private String name;

    private String time;

    private CharSequence content;

    public CharSequence getContent() {
        return content;
    }

    public void setContent(CharSequence content) {
        this.content = content;
    }

    private AVIMConversation conversation;

    public AVIMConversation getConversation() {
        return conversation;
    }

    public void setConversation(AVIMConversation conversation) {
        this.conversation = conversation;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getHeaderUrl() {
        return headerUrl;
    }

    public void setHeaderUrl(String headerUrl) {
        this.headerUrl = headerUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Long getHeadPic() {
        return headPic;
    }

    public void setHeadPic(Long headPic) {
        this.headPic = headPic;
    }

    @Override
    public String toString() {
        return "ChatMessageBean{" +
                "userId=" + userId +
                ", headPic=" + headPic +
                ", name='" + name + '\'' +
                ", time='" + time + '\'' +
                ", content=" + content +
                '}';
    }
}
