package com.example.com.meimeng.gson.gsonbean;

/**
 * Created by 010 on 2015/7/22.
 */
public class LoginBean extends BaseBean {

    private LoginObj param;

    private LoginItem returnValue;

    public LoginItem getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(LoginItem returnValue) {
        this.returnValue = returnValue;
    }

    public LoginObj getParam() {
        return param;
    }

    public void setParam(LoginObj param) {
        this.param = param;
    }
}
