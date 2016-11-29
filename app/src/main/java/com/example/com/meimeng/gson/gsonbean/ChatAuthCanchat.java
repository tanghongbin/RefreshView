package com.example.com.meimeng.gson.gsonbean;

/**
 * Created by lx on 2015/8/4.
 */
public class ChatAuthCanchat {
    private Boolean canChat;
    public Integer level;
    public Integer chatChance;

    public Boolean getCanChat() {
        return canChat;
    }

    public void setCanChat(Boolean canChat) {
        this.canChat = canChat;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getChatChance() {
        return chatChance;
    }

    public void setChatChance(Integer chatChance) {
        this.chatChance = chatChance;
    }
}
