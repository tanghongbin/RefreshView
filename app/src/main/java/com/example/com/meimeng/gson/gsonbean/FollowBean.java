package com.example.com.meimeng.gson.gsonbean;

/**
 * Gson解析用
 * 关注/ 取消关注用户的Json数据的Bean
 */
public class FollowBean {

    public Param param;
    public  static  class Param{
        private int isFollowing;

        public int getIsFollowing() {
            return isFollowing;
        }

        public void setIsFollowing(int isFollowing) {
            this.isFollowing = isFollowing;
        }
    }
    private String error;
    private boolean success;
    private String returnValue;
/*
    public Map getParm() {
        return param;
    }

    public void setParm(Map parm) {
        this.param = parm;
    }*/

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

    public String getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(String returnValue) {
        this.returnValue = returnValue;
    }
}
