package com.publishproject.core.commonconfig.permissionconfigs;

import java.util.List;

/**
 * @author Android客户端组-tanghongbin
 * @Title: PermissionListener
 * @Package com.commonconfig.permissionconfigs
 * @Description: TODO
 * Copyright: Copyright (c) 2016
 * Company: 成都壹柒互动科技有限公司
 * @date 2016/12/6 18:05
 */
public interface PermissionListener {
    void onGranted();
    void onDenied(List<String> permissions);

}
