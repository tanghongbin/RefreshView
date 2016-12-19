package com.publishproject.events;

import android.app.Activity;

import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.thread.EventThread;
import com.publishproject.core.activitiesconfig.BaseActivity;
import com.publishproject.events.RequestEndEvent;
import com.publishproject.events.RequestStartEvent;

/**
 * @author Android客户端组-tanghongbin
 * @Title: ProgressDialogEvent
 * @Package com.publishproject.core.commonconfig.busconfigs.bustype
 * Copyright: Copyright (c) 2016
 * Company: 成都壹柒互动科技有限公司
 * @date 2016/12/15 17:05
 * @Description: TODO
 */
public class ProgressDialogEvent {
    BaseActivity activity;

    public ProgressDialogEvent(BaseActivity activity) {
        this.activity = activity;
    }

    @Subscribe(thread = EventThread.MAIN_THREAD)
    public void startRequest(RequestStartEvent event){
        if(!activity.isFinishing()){
            activity.showDialog();
        }
    }
    @Subscribe(thread = EventThread.MAIN_THREAD)
    public void endRequest(RequestEndEvent event){
        if(!activity.isFinishing()){
            activity.dismissDialog();
        }
    }
}
