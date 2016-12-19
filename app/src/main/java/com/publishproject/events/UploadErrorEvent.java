package com.publishproject.events;

import java.io.File;
import java.util.List;

/**
 * @author Android客户端组-tanghongbin
 * @Title: UploadErrorEvent
 * @Package com.publishproject.core.commonconfig.netconfigs.callbacks
 * Copyright: Copyright (c) 2016
 * Company: 成都壹柒互动科技有限公司
 * @date 2016/12/14 15:32
 * @Description: TODO
 */
public class UploadErrorEvent extends ErrorEvent{
    public UploadErrorEvent() {
    }

    public UploadErrorEvent(List<File> fileList) {
        this.fileList = fileList;
    }

    private List<File> fileList;

    public List<File> getFileList() {
        return fileList;
    }

    public void setFileList(List<File> fileList) {
        this.fileList = fileList;
    }
}
