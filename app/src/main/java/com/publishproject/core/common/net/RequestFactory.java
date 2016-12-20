package com.publishproject.core.common.net;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.BaseRequest;
import com.lzy.okgo.request.PostRequest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
public final class RequestFactory {
    private RequestFactory(){

    }
    public static BaseRequest createGetRequest(String url){
       return OkGo.get(url);
    }
    public static BaseRequest createPostRequest(String url){
        return OkGo.post(url);
    }
    public static BaseRequest createUploadRequest(String url){
        return OkGo.post(url);
    }
    public static BaseRequest createDownloadRequest(String url){
        return OkGo.get(url);
    }


    public static List<BaseRequest> createMutipleReuqst(List<Object> objs) {
        final String postUrl = "http://120.76.226.150:8080/service/api/file/upload";
        List<BaseRequest> fileList = new ArrayList<>();
        for(Object o:objs){
            PostRequest http = OkGo.post(postUrl)
                    .params("name", "file");
            if(o instanceof String && FileUtils.isFileExists((String)o)){
                http.params("filename",FileUtils.getFileByPath((String)o));
            }else if(o instanceof File && FileUtils.isFileExists((File)o)){
                http.params("filename",(File)o);
            }else {
                continue;
            }
            fileList.add(http);
        }
        return fileList;
    }
}
