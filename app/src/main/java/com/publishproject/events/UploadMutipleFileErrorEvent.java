package com.publishproject.events;

import com.lzy.okgo.request.PostRequest;

import java.io.File;
import java.util.List;

/**
 * @author Android客户端组-tanghongbin
 * @Title: UploadMutipleFileErrorEvent
 * @Package com.publishproject.core.commonconfig.netconfigs.callbacks
 * Copyright: Copyright (c) 2016
 * Company: 成都壹柒互动科技有限公司
 * @date 2016/12/14 15:32
 * @Description: 上传多文件失败发送的事件
 */
public class UploadMutipleFileErrorEvent extends ErrorEvent{


    public UploadMutipleFileErrorEvent(List<String> fileList) {
        this.fileList = fileList;
    }

    private List<String> fileList;

    public List<String> getFileList() {
        return fileList;
    }

    public void setFileList(List<String> fileList) {
        this.fileList = fileList;
    }
}
