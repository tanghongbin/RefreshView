package com.example.com.meimeng.gson.gsonbean;

/**
 * Created by 010 on 2015/8/14.
 */
public class WeiXinPayParam {

    private WeiXinPayItem preOrder;

    private long outTradeNo;

    public long getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(long outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public WeiXinPayItem getPreOrder() {
        return preOrder;
    }

    public void setPreOrder(WeiXinPayItem preOrder) {
        this.preOrder = preOrder;
    }
}
