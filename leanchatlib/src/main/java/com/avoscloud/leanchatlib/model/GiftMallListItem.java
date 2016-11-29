package com.avoscloud.leanchatlib.model;

/**
 * Created by 003 on 2015/7/25.
 */
public class GiftMallListItem {
    private long giftId;
    private long goodId;
    private String name;
    private String describe;
    private String price;
    private long giftPic;

    public long getGoodId() {
        return goodId;
    }

    public void setGoodId(long goodId) {
        this.goodId = goodId;
    }

    public long getGiftId() {
        return giftId;
    }

    public String getPrice() {
        return price;
    }

    public String getDescribe() {
        return describe;
    }

    public long getGiftPic() {
        return giftPic;
    }

    public String getName() {
        return name;
    }

    public void setGiftPic(long giftPic) {
        this.giftPic = giftPic;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGiftId(long giftId) {
        this.giftId = giftId;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public void setPrice(String price) {
        this.price = price;
    }

}
