package com.example.com.meimeng.gson.gsonbean;

import java.util.Map;

/**
 * Created by lx on 2015/8/5.
 */
public class DeleteInfoBean  {

    private Map param ;
    private String error ;
    private boolean success ;
    private Object returnValue ;

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

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(Object returnValue) {
        this.returnValue = returnValue;
    }
}
