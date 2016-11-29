package com.example.com.meimeng.gson.gsonbean;

/**
 * Created by Administrator on 2016/1/5.
 */
public class UserChatInfoParam {
    private UserChatInfoItem userInfo ;
    private  boolean canChat;

    public boolean isCanChat() {
        return canChat;
    }

    public void setCanChat(boolean canChat) {
        this.canChat = canChat;
    }

    public UserChatInfoItem getUserChatInfoItem() {
        return userInfo;
    }

    public void setUserChatInfoItem(UserChatInfoItem userInfo) {
        this.userInfo = userInfo;
    }
}
