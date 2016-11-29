package com.example.com.meimeng.gson.gsonbean;

import java.util.List;

/**
 * Created by Administrator on 2016/2/2.
 */
public class ConversationResult {
    private  long uid;

    private List<String> conversations;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public List<String> getConversations() {
        return conversations;
    }

    public void setConversations(List<String> conversations) {
        this.conversations = conversations;
    }
}
