package com.example.com.meimeng.gson.gsonbean;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by wjw on 2015/7/23.
 * 获取我发起的活动的报名用户列表解析Json时的Bean
 */
public class ApplyUserListBean {
    private Map param;
    private String error;
    private ArrayList<ApplyUserItem> returnValue;
    private boolean success;
    public Map getParam() {
        return param;
    }
    public void setParam(Map param) {
        this.param = param;
    }
    public String getError() {
        return error;
    }
    public void setError(String error) {
        this.error = error;
    }

    public ArrayList<ApplyUserItem> getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(ArrayList<ApplyUserItem> returnValue) {
        this.returnValue = returnValue;
    }

    public boolean isSuccess() {
        return success;
    }
    public void setSuccess(boolean success) {
        this.success = success;
    }

}
