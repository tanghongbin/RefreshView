package com.example.com.meimeng.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.view.View;

import com.avos.avoscloud.AVOSCloud;
import com.example.com.meimeng.MeiMengApplication;
import com.example.com.meimeng.R;
import com.example.com.meimeng.activity.HomeActivity;

import org.json.JSONObject;

public class CustomReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            if (intent.getAction().equals("com.example.com.meimeng.activity.HomeActivity")) {
                JSONObject json = new JSONObject(intent.getExtras().getString("com.avos.avoscloud.Data"));
                final String message = json.getString("alert");
                Intent resultIntent = new Intent(AVOSCloud.applicationContext, HomeActivity.class);

                PendingIntent pendingIntent = PendingIntent.getActivity(AVOSCloud.applicationContext, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(AVOSCloud.applicationContext)
                        .setSmallIcon(R.drawable.ico_launcher)
                        .setDefaults(MeiMengApplication.sharedPreferences.getInt("sound_mode",Notification.COLOR_DEFAULT))
                        .setContentTitle(AVOSCloud.applicationContext.getResources().getString(R.string.app_name))
                        .setContentText(message)
                        .setTicker(message);
                mBuilder.setContentIntent(pendingIntent);
                mBuilder.setAutoCancel(true);
                MeiMengApplication.messageCont++;
                MeiMengApplication.messageIntentType = 0;
                BadgeUtil.setBadgeCount(context, MeiMengApplication.messageCont);
                MeiMengApplication.messageRed.setVisibility(View.VISIBLE);
                int mNotificationId = 10086;
                NotificationManager mNotifyMgr = (NotificationManager) AVOSCloud.applicationContext.getSystemService(Context.NOTIFICATION_SERVICE);
                mNotifyMgr.notify(mNotificationId, mBuilder.build());
            }
        } catch (Exception e) {

        }
    }
}
