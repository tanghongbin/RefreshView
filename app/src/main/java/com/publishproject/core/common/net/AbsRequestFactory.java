package com.publishproject.core.common.net;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.BaseRequest;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okgo.request.PostRequest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pushlish.tang.com.commonutils.others.FileUtils;

/**
 * @author Android客户端组-tanghongbin
 * @Title: AbsRequestFactory
 * @Package com.publishproject.core.common.net
 * Copyright: Copyright (c) 2016
 * Company: 成都壹柒互动科技有限公司
 * @date 2016/12/22 10:10
 * @Description: TODO
 */
public abstract class AbsRequestFactory {
    /**
     * get请求
     * @param url
     * @return
     */
    public abstract GetRequest getRequest(String url);

    /**
     * post请求
     * @param url
     * @return
     */
    public abstract PostRequest postRequest(String url);
    /**
     * 单文件上传请求
     * @param url
     * @return
     */
    public abstract PostRequest uploadRequest(String url);

    /**
     * 下载文件请求
     * @param url
     * @return
     */
    public abstract GetRequest downloadRequest(String url);


    /**
     * 多文件上传请求
     * @param objs path路径集合或者File对象集合
     * @return
     */
    public abstract Map<String,PostRequest> mutipleFileReuqst(List<Object> objs);
}
