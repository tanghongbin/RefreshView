package com.publishproject.core.common.net.callbacks;

import android.text.TextUtils;

import com.lzy.okgo.convert.StringConvert;
import com.lzy.okgo.request.BaseRequest;
import com.publishproject.core.common.logger.LogUtil;
import com.publishproject.core.common.eventbus.BusHelper;
import com.publishproject.events.BaseEvent;
import com.publishproject.events.RequestEndEvent;
import com.publishproject.events.RequestStartEvent;

import okhttp3.Call;
import okhttp3.Response;

/**
 * @author Android客户端组-tanghongbin
 * @Title: HttpCallback
 * @Package com.publishproject.core.commonconfig.netconfigs.callbacks
 * Copyright: Copyright (c) 2016
 * Company: 成都壹柒互动科技有限公司
 * @date 2016/12/14 14:48
 * @Description: 请求字符串回调的基本封装，包装请求前验证网络，
 * 2.成功和错误处理
 */
public class HttpCallback<CLASS extends BaseEvent> extends com.lzy.okgo.callback.AbsCallback<String> {

    BaseRequest request;
    Class<CLASS> classType;

    public HttpCallback(BaseRequest request, Class<CLASS> classType) {
        this.request = request;
        this.classType = classType;
    }

    @Override
    public void onBefore(BaseRequest request) {
        //在网络请求开始前发送请求开始的事件
        BusHelper.postEvent(new RequestStartEvent());
        LogUtil.i("TAG","打印请求地址-->"+request.getUrl());
    }

    @Override
    public String convertSuccess(Response response) throws Exception {
        String s = StringConvert.create().convertSuccess(response);
        response.close();
        return s;
    }

    @Override
    public void onSuccess(String s, Call call, okhttp3.Response response) {
        try{
            LogUtil.i("TAG","打印返回结果-->"+s);
            BusHelper.postEvent(new RequestEndEvent());
            CLASS event = classType.newInstance();
            event.setData(s);
            BusHelper.postEvent(event);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void onError(Call call, okhttp3.Response response, Exception e) {
        try {
            LogUtil.i("TAG","请求错误-->");
            BusHelper.postEvent(new RequestEndEvent());
            CLASS event = classType.newInstance();
            String errorMessage = response == null ? "":response.message();
            if(TextUtils.isEmpty(errorMessage)){
                errorMessage = e == null ? "" : e.getMessage();
            }
            event.getError().setMessage(errorMessage);
            BusHelper.postEvent(event);
        } catch (InstantiationException e1) {
            e1.printStackTrace();
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        }
    }
}
