package com.example.com.meimeng.gson.gsonbean;

/**
 * Created by Administrator on 2015/9/11.
 */
public class ActivityOriginateParam {
    private boolean canOriginateActivity;
    private int activityChance;

    public boolean isCanOriginateActivity() {
        return canOriginateActivity;
    }

    public void setCanOriginateActivity(boolean canOriginateActivity) {
        this.canOriginateActivity = canOriginateActivity;
    }

    public int getActivityChance() {
        return activityChance;
    }

    public void setActivityChance(int activityChance) {
        this.activityChance = activityChance;
    }
}
