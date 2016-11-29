package com.example.com.meimeng.bean;

/**
 * Created by Administrator on 2015/12/19.
 */
public class OfficePriceBean {
    private int _id;
    private long targetUid;
    private double price;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public long getTargetUid() {
        return targetUid;
    }

    public void setTargetUid(long targetUid) {
        this.targetUid = targetUid;
    }
}
