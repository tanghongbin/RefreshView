package com.publishproject.core.commonconfig.permissionconfigs;

import android.content.Context;


/**
 * @author Android客户端组-tanghongbin
 * @Title: PermissionHelper
 * @Package com.commonconfig.permissionconfigs
 * @Description: TODO
 * Copyright: Copyright (c) 2016
 * Company: 成都壹柒互动科技有限公司
 * @date 2016/12/6 17:57
 */
public final class PermissionHelper {
    public static void requestPermission(Context context, String deniedMessage, final PermissionListener listener, String... permissons){
        PermissionInterface permissionInterface = new AcpRequestPermission();
        permissionInterface.requestPermission(context,deniedMessage,listener,permissons);
    }
}
