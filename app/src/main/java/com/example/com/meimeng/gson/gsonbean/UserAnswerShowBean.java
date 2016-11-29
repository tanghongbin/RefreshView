package com.example.com.meimeng.gson.gsonbean;



import java.util.List;

/**
 * Created by lx on 2015/8/6.
 */
public class UserAnswerShowBean extends BaseBean {

    private List<UserAnswerData> returnValue ;

    public List<UserAnswerData> getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(List<UserAnswerData> returnValue) {
        this.returnValue = returnValue;
    }

}
