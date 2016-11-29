package com.example.com.meimeng;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avoscloud.leanchatlib.controller.ChatManager;
import com.avoscloud.leanchatlib.controller.ChatManagerAdapter;
import com.avoscloud.leanchatlib.model.UserInfo;
import com.example.com.meimeng.bean.CityBean;
import com.example.com.meimeng.bean.ProvinceBean;
import com.example.com.meimeng.bean.RegisterFirstInfoBean;
import com.example.com.meimeng.constants.CommonConstants;
import com.example.com.meimeng.gson.gsonbean.LstUserAnswer;
import com.flurry.android.FlurryAgent;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzw on 15/4/27.
 */
public class MeiMengApplication extends Application {
    public static volatile ArrayList<AVIMTypedMessage> messageAVIMlist = new ArrayList<>();
    public static volatile ArrayList<AVIMConversation> conversationList = new ArrayList<>();
    public static volatile List<LstUserAnswer> gxmsLstUserAnswer = new ArrayList<>();
    public static volatile List<LstUserAnswer> shxgLstUserAnswer = new ArrayList<>();
    public static volatile List<LstUserAnswer> aqgdLstUserAnswer = new ArrayList<>();
    public static volatile List<LstUserAnswer> yhlxLstUserAnswer = new ArrayList<>();
    public static volatile List<LstUserAnswer> hyqwLstUserAnswer = new ArrayList<>();
    public static volatile List<LstUserAnswer> hhshLstUserAnswer = new ArrayList<>();
    public static volatile List<LstUserAnswer> lxdxLstUserAnswer = new ArrayList<>();

    public static volatile ArrayList<Activity> GxmsManageActivity = new ArrayList<>();
    public static volatile ArrayList<Activity> ShxgManageActivity = new ArrayList<>();
    public static volatile ArrayList<Activity> AqgdManageActivity = new ArrayList<>();
    public static volatile ArrayList<Activity> YhlxManageActivity = new ArrayList<>();
    public static volatile ArrayList<Activity> HyqwManageActivity = new ArrayList<>();
    public static volatile ArrayList<Activity> HhshManageActivity = new ArrayList<>();
    public static volatile ArrayList<Activity> LxdxManageActivity = new ArrayList<>();

    //小红点
    public static ImageView messageRed;
    //支付页面的支付按钮
    public static Button payButton;
    //手机像素密度比
    public static float density;
    //活动详情页面报名活动的按钮
    public static Button attend;
    //红娘聊天的头像上面的数字提醒
    public static TextView messageRedMatcher;
    public static volatile ArrayList<Activity> loginActivity = new ArrayList<>();
    public static volatile Integer sex =0;   //性别
      public static Integer residence = -1;//所在地
    public static volatile LruCache<Long, Bitmap> mMemoryCache;    //图片缓存

    public static ArrayList<ProvinceBean> provinces = new ArrayList<>();//地区数据集合
    public static CityBean cityBean = new CityBean();//选中的城市


    public static int isSound = 1;   //判断是否在聊天面板
    public static int messageCont = 0;    //桌面app图标的数字提醒
    //用户第一次编辑资料的信息
    public static RegisterFirstInfoBean registerFirstInfoBean = new RegisterFirstInfoBean();
    public static boolean checkFirstInfo;//判断认证身份是否为空


    //短暂记录支付当前信息
    public static String payTitle;
    public static long payGoodsId;
    public static long payActivityId;
    public static double payPrice;
    public static int patType;
    public static int payGoodsPicId;

    public static TextView serviceUnreadNumText;
    public static Integer serviceUnreadNum;
    //记录微信的订单号
    public static long weixinOutTradeNo = -1l;

    //当前的上下文
    public static Context currentContext;
    public static int messageIntentType = 1;

    public static final ChatManager chatManager = ChatManager.getInstance();

    //记录当前支付要返回的活动
    //0---他人页面中送礼物(OthersSelfActivity)；1---我的礼物页面中送他人礼物(GitManagerActivity)；2---报名参加活动页面(OfficeEventDetail)；
    // 3---报名参加活动页面(PrivateEventDetail);4---报名参加活动页面(SixPersonDateDetail);5---会员等级界面;6----聊天界面
    public static int weixinPayCallBack = -1;

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

    @Override
    public void onCreate() {
        super.onCreate();

        FlurryAgent.setLogEnabled(false);
        FlurryAgent.init(this, "3Z353FG5ZGGXGJGVR5NH");
        //得到当前的上下文
        currentContext = getApplicationContext();
    //    userVerfiy = MeiMengApplication.sharedPreferences.getInt(CommonConstants.USER_VERFIY, 0);
        //实例化存储基本信息的对象
        sharedPreferences = getSharedPreferences(CommonConstants.FILE_NAME, Context.MODE_APPEND);

        // 获取应用程序最大可用内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 8;

        density=getResources().getDisplayMetrics().density;
        mMemoryCache = new LruCache<Long, Bitmap>(cacheSize) {

            @Override
            protected int sizeOf(Long key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };

        AVOSCloud.initialize(this, "UQOegUWsnsfBdHSVQlrvcn6J", "CauURHoF4RRFAb4QWxGcQAxE");
        AVAnalytics.enableCrashReport(this.getApplicationContext(), true);
        AVOSCloud.setLastModifyEnabled(true);
        AVOSCloud.setDebugLogEnabled(true);
        ChatManager.setDebugEnabled(true);// tag leanchatlib
        AVOSCloud.setDebugLogEnabled(true);  // set false when release

        chatManager.init(this);
        chatManager.setChatManagerAdapter(new ChatManagerAdapter() {
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
