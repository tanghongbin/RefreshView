package com.xqd.chatmessage;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avoscloud.leanchatlib.controller.ChatManager;
import com.avoscloud.leanchatlib.controller.ChatManagerAdapter;
import com.avoscloud.leanchatlib.model.UserInfo;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.xqd.chatmessage.util.ChatUid;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/9/8.
 */
public class ChatAplication extends Application {
    public static volatile ArrayList<AVIMTypedMessage> messageAVIMlist = new ArrayList<>();
    public static volatile ArrayList<AVIMConversation> conversationList = new ArrayList<>();
    public static volatile LruCache<Long, Bitmap> mMemoryCache;
    public static String tel = "";
    public static String name = "";

    public static ImageView messageRed;

    public static int isSound = 1;
    public static int messageIntentType = 1;
    public static int messageCont = 0;
    public static ChatUid Uid = new ChatUid();

    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context)
                .threadPoolSize(3).threadPriority(Thread.NORM_PRIORITY - 2)
                        //.memoryCache(new WeakMemoryCache())
                .denyCacheImageMultipleSizesInMemory()
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();
        ImageLoader.getInstance().init(config);
    }

    //存储基本信息的对象
    public static SharedPreferences sharedPreferences;

    public static volatile ArrayList<Activity> loginActivity = new ArrayList<>();
    //利用sharedPreferences保存时的文件名
    public static final String FILE_NAME = "base_info";

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = getSharedPreferences(FILE_NAME, Context.MODE_APPEND);

        // 获取应用程序最大可用内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();

        int cacheSize = maxMemory / 8;

        mMemoryCache = new LruCache<Long, Bitmap>(cacheSize) {

            @Override
            protected int sizeOf(Long key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };

        AVOSCloud.initialize(this, "UQOegUWsnsfBdHSVQlrvcn6J", "CauURHoF4RRFAb4QWxGcQAxE");

        // 锟斤拷锟矫憋拷锟斤拷锟斤拷锟斤拷统锟斤拷
        AVAnalytics.enableCrashReport(this.getApplicationContext(), true);
        AVOSCloud.setLastModifyEnabled(true);
        AVOSCloud.setDebugLogEnabled(true);


        ChatManager.setDebugEnabled(true);// tag leanchatlib
        AVOSCloud.setDebugLogEnabled(true);  // set false when release

        ChatManager.getInstance().init(this);
        ChatManager.getInstance().setChatManagerAdapter(new ChatManagerAdapter() {
            @Override
            public UserInfo getUserInfoById(String userId) {
                UserInfo userInfo = new UserInfo();
                userInfo.setUsername(userId);
                userInfo.setAvatarUrl("http://ac-x3o016bx.clouddn.com/86O7RAPx2BtTW5zgZTPGNwH9RZD5vNDtPm1YbIcu");
                return userInfo;
            }

            @Override
            public void cacheUserInfoByIdsInBackground(List<String> userIds) throws Exception {
            }

            @Override
            public void shouldShowNotification(Context context, String selfId, AVIMConversation conversation, AVIMTypedMessage message) {

                messageAVIMlist.add(message);
                conversationList.add(conversation);

            }
        });
        initImageLoader(this);
    }
}
