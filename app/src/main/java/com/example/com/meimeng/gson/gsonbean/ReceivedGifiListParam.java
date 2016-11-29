package com.example.com.meimeng.gson.gsonbean;

import java.util.ArrayList;

/**
 * Created by wjw on 2015/7/25.
 * 获取用户收到的礼物list ReturnValue字段返回值
 */
public class ReceivedGifiListParam {
    private ArrayList<ReceivedGiftItem> gifts;
    private int totalNum;

    public ArrayList<ReceivedGiftItem> getGifts() {
        return gifts;
    }

    public void setGifts(ArrayList<ReceivedGiftItem> gifts) {
        this.gifts = gifts;
    }

    public int getTotalNum() {
        return totalNum;
    }


    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }
}
