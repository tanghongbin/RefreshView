package com.publishproject.events;

import java.io.File;

/**
 * @author Android客户端组-tanghongbin
 * @Title: DownloadBaseEvent
 * @Package com.publishproject.core.commonconfig.netconfigs.callbacks
 * Copyright: Copyright (c) 2016
 * Company: 成都壹柒互动科技有限公司
 * @date 2016/12/14 17:04
 * @Description: TODO
 */
public class DownloadBaseEvent<ERROR extends ErrorEvent> extends JudgeResultEvent{
    private File file;
    private ERROR error;

    public DownloadBaseEvent() {
    }

    public DownloadBaseEvent(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public ERROR getError() {
        return error;
    }

    public void setError(ERROR error) {
        this.error = error;
    }

    @Override
    public boolean isSuccess() {
        return error == null;
    }
}
