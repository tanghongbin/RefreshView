package com.example.com.meimeng.gson.gsonbean;

import java.util.ArrayList;

/**
 * Created by wjw on 2015/7/25.
 * 分页获取商城礼物list的ReturnValue
 */
public class GiftMallListParam {
    private ArrayList<GiftMallListItem> giftList;


    public ArrayList<GiftMallListItem> getGiftList() {
        return giftList;
    }

    public void setGiftList(ArrayList<GiftMallListItem> giftList) {
        this.giftList = giftList;
    }
}
