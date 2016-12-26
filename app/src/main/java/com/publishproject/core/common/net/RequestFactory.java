package com.publishproject.core.common.net;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.BaseRequest;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okgo.request.PostRequest;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pushlish.tang.com.commonutils.others.FileUtils;

/**
 * @author Android客户端组-tanghongbin
 * @Title: RequestFactory
 * @Package com.publishproject.core.commonconfig.netconfigs
 * Copyright: Copyright (c) 2016
 * Company: 成都壹柒互动科技有限公司
 * @date 2016/12/14 17:18
 * @Description: 构建参数的工厂类,可自行配置
 */
public final class RequestFactory extends AbsRequestFactory{
    private RequestFactory(){

    }

    private static final RequestFactory requestFactory = new RequestFactory();
    public static RequestFactory getInstance(){
        return requestFactory;
    }



    @Override
    public GetRequest getRequest(String url) {
        return OkGo.get(url);
    }

    @Override
    public PostRequest postRequest(String url) {
        return OkGo.post(url);
    }

    @Override
    public PostRequest uploadRequest(String url) {
        return OkGo.post(url);
    }

    @Override
    public GetRequest downloadRequest(String url) {
        return OkGo.get(url);
    }


    public Map<String,PostRequest> mutipleFileReuqst(List<Object> objs) {
        final String postUrl = "http://120.76.226.150:8080/service/api/file/upload";
        List<PostRequest> fileList = new ArrayList<>();
        HashMap<String,PostRequest> hashMap = new HashMap<>();
        for(Object o:objs){
            PostRequest http = OkGo.post(postUrl)
                    .params("name", "file");
            File file = null;
            if(o instanceof String && FileUtils.isFileExists((String)o)){
                file = FileUtils.getFileByPath((String)o);
                http.params("filename",file);
            }else if(o instanceof File && FileUtils.isFileExists((File)o)){
                file = (File)o;
                http.params("filename",file);
            }else {
                continue;
            }
            hashMap.put(file.getAbsolutePath(),http);
        }
        return hashMap;
    }
}
