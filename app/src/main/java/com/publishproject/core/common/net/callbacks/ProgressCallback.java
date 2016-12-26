package com.publishproject.core.common.net.callbacks;

/**
 * @author Android客户端组-tanghongbin
 * @Title: ProgressCallback
 * @Package com.publishproject.core.commonconfig.netconfigs.callbacks
 * Copyright: Copyright (c) 2016
 * Company: 成都壹柒互动科技有限公司
 * @date 2016/12/14 15:29
 * @Description: TODO
 */
public interface ProgressCallback {
    void onProgress(long currentSize, long totalSize, float progress);
}
