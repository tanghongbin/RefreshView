package com.example.com.meimeng.bean;

import com.avos.avoscloud.im.v2.AVIMConversation;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by 010 on 2015/7/13.
 */
public class ChatMessageBean implements Serializable {

    private long userId;

    private String headerUrl;

    private Long headPic ;

    private String name;

    private String time;
    private String conversationId;
    private  Map attributes;

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public Map getAttributes() {
        return attributes;
    }

    public void setAttributes(Map attributes) {
        this.attributes = attributes;
    }

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
}
