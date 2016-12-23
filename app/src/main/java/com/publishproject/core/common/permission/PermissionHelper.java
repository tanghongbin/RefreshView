package com.publishproject.core.common.permission;

import android.Manifest;
import android.content.Context;


/**
 * @author Android客户端组-tanghongbin
 * @Title: PermissionHelper
 * @Package com.commonconfig.permissionconfigs
 * Copyright: Copyright (c) 2016
 * Company: 成都壹柒互动科技有限公司
 * @date 2016/12/6 17:57
 * @Description: 权限访问帮助类，现暂时提供requestPermission方法，传入上下文对象，拒绝消息
 * ，权限反馈回调，需要请求的权限列表如下，后面可自行扩展
 */
public final class PermissionHelper {

    public static final String CALANDER = Manifest.permission.READ_CALENDAR;//读取日历
    public static final String CAMERA = Manifest.permission.CAMERA;//照相机权限
    public static final String CONTACTS = Manifest.permission.READ_CONTACTS;//读，写联系人
    public static final String ACCOUNTS = Manifest.permission.GET_ACCOUNTS;//获取账户信息
    public static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;//位置信息
    public static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    public static final String RECORD_AUDIO = Manifest.permission.RECORD_AUDIO;//音频记录
    public static final String PHONE_STATE = Manifest.permission.READ_PHONE_STATE;//电话状态
    public static final String CALL_PHONE = Manifest.permission.CALL_PHONE;//拨打电话
    public static final String CALL_LOG = Manifest.permission.READ_CALL_LOG;//查看拨打记录
    public static final String ADD_VOICEMAIL = Manifest.permission.ADD_VOICEMAIL;
    public static final String USE_SIP = Manifest.permission.USE_SIP;
    public static final String PROCESS_OUTGOING_CALLS = Manifest.permission.PROCESS_OUTGOING_CALLS;
    public static final String SEND_SMS = Manifest.permission.SEND_SMS;//发送短信
    public static final String READ_SMS = Manifest.permission.READ_SMS;//阅读短信
    public static final String RECEIVE_WAP_PUSH = Manifest.permission.RECEIVE_WAP_PUSH;
    public static final String RECEIVE_MMS = Manifest.permission.RECEIVE_MMS;//接受短信
    public static final String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;//读写外部存储

    public static void requestPermission(Context context, String deniedMessage, final PermissionListener listener, String... permissons){
        PermissionInterface permissionInterface = new AcpRequestPermission();
        permissionInterface.requestPermission(context,deniedMessage,listener,permissons);
    }


}
