package com.example.com.meimeng.gson.gsonbean;

/**
 * Created by 010 on 2015/8/14.
 */
public class ZhiFuBaoPayParam {

    private ZhifubaoPayItem preOrder;

    private long outTradeNo;

    public long getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(long outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public ZhifubaoPayItem getPreOrder() {
        return preOrder;
    }

    public void setPreOrder(ZhifubaoPayItem preOrder) {
        this.preOrder = preOrder;
    }
}
