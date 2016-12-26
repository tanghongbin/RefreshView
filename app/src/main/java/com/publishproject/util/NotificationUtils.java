package com.publishproject.util;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;


/**
 * @author Android客户端组-tanghongbin
 * @Title: NotificationUtils
 * @Package com.publishproject.util
 * Copyright: Copyright (c) 2016
 * Company: 成都壹柒互动科技有限公司
 * @date 2016/12/20 10:14
 * @Description: 顶部通知栏工具类
 */
public class NotificationUtils {
    private static final int NOTIFI_ID = 1;

    private static void sendNofication(Context context,String title,String content,int icon){
        NotificationUtils.Builder builder = new NotificationUtils.Builder();
        builder.setTitle(title);
        builder.setContent(content);
        builder.setSmallIcon(icon);
        sendNofication(context,builder);
    }
    public static void sendNofication(Context context, Builder infoBuilder) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                //第一行内容  通常作为通知栏标题
                .setContentTitle(infoBuilder.getTitle())
                //第二行内容 通常是通知正文
                .setContentText(infoBuilder.getContent());
        //系统状态栏显示的小图标
        if (infoBuilder.getSmallIcon() != 0) {
            builder.setSmallIcon(infoBuilder.getSmallIcon());
        }
        if (infoBuilder.getDefalutOption() == 0) {//通知默认的声音 震动 呼吸灯
            builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        }

        ////设置点击通知时，要触发的activity或者broadcast
        if(infoBuilder.getPendingIntent() != null){
            builder.setContentIntent(infoBuilder.getPendingIntent());
        }
        Notification notifi = builder.build();
        notifi.flags |= Notification.FLAG_AUTO_CANCEL;//点击一次后自动消失
        mNotificationManager.notify(NOTIFI_ID, notifi);
    }

    public static class Builder {

        private String title;//标题
        private String content;//内容
        private int smallIcon;//系统状态小图标
        private int defalutOption;//通知的触发的是声音还是震动，闪光灯
        private PendingIntent pendingIntent;
        private int flags;//点击消失还是常驻
        public Builder(){

        }


        public PendingIntent getPendingIntent() {
            return pendingIntent;
        }

        public void setPendingIntent(PendingIntent pendingIntent) {
            this.pendingIntent = pendingIntent;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getSmallIcon() {
            return smallIcon;
        }

        public void setSmallIcon(int smallIcon) {
            this.smallIcon = smallIcon;
        }

        public int getDefalutOption() {
            return defalutOption;
        }

        public void setDefalutOption(int defalutOption) {
            this.defalutOption = defalutOption;
        }


        public int getFlags() {
            return flags;
        }

        public void setFlags(int flags) {
            this.flags = flags;
        }
    }
}
