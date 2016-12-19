package com.publishproject.core.commonconfig.permissionconfigs;

import android.content.Context;

import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;

import java.util.List;

/**
 * @author Android客户端组-tanghongbin
 * @Title: AcpRequestPermission
 * @Package com.commonconfig.permissionconfigs
 * @Description: TODO
 * Copyright: Copyright (c) 2016
 * Company: 成都壹柒互动科技有限公司
 * @date 2016/12/8 16:20
 */
public class AcpRequestPermission implements PermissionInterface {
    @Override
    public void requestPermission(Context context, String deniedMessage, final PermissionListener listener, String... permissons) {
        Acp.getInstance(context).request(new AcpOptions.Builder().
                        setPermissions(permissons)
                        .setDeniedMessage(deniedMessage)
                        .build()
                , new AcpListener() {
                    @Override
                    public void onGranted() {
                        listener.onGranted();
                    }

                    @Override
                    public void onDenied(List<String> permissions) {
                        listener.onDenied(permissions);
                    }
                });
    }
}
