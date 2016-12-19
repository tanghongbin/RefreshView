package com.publishproject.core.commonconfig.permissionconfigs;

import android.content.Context;



/**
 * @author Android客户端组-tanghongbin
 * @Title: PermissionInterface
 * @Package com.commonconfig.permissionconfigs
 * @Description: TODO
 * Copyright: Copyright (c) 2016
 * Company: 成都壹柒互动科技有限公司
 * @date 2016/12/8 16:19
 */
public interface PermissionInterface {
    void requestPermission(Context context, String deniedMessage, PermissionListener listener, String... permissons);
}
