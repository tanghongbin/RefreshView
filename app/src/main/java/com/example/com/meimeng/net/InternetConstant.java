package com.example.com.meimeng.net;

import com.example.com.meimeng.util.Utils;

/**
 * Created by 010 on 2015/7/20.
 */
public class InternetConstant {
    public static final String USETOKEN = "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();

    //主机域名
    //public static final String SERVER_URL = "http://119.254.101.89:8080/meimeng/";   //外网
//    public static final String SERVER_URL = "http://121.201.29.133:8080/meimeng/";//2.0
  //    public static final String SERVER_URL = "http://192.168.2.8:8080/meimeng/";   //内网
  //  public static final String SERVER_URL = "http://192.168.2.3:8080/meimeng2/";//2.3内网
    //public static final String SERVER_URL = "http://119.254.101.227:8080/meimeng3/";//3.0.2测试
    //public static final String SERVER_URL = "http://121.42.61.242:8080/meimeng3/";//3.0.4测试
    public static final String SERVER_URL = "http://121.201.29.133:8080/meimeng3/";//3.0.2线上
   //public static final String SERVER_URL = "http://121.42.184.247:8080/meimeng3/";//3.0.4最新测试
  // public static final String SERVER_URL = "http://120.26.63.68:8080/meimeng3/";//3.1.0最新测试

    public static final String HOME_LIST_URL = "api/home/list";

    public static final String EXPLORE_LIST_URL = "api/find/list";

    public static final String UPLOAD_PICTURE_URL = "api/img/upload";

    public static final String USER_GETPARTINFO = "api/user/getPartInfo";

    public static final String PEOPLE_DOFOLLOW = "api/people/dofollow";

    public static final String PEOPLE_UNDOFOLLOW = "api/people/undofollow";

//    public static final String USERACTIVITY_LIST = "api/useractivity/list";

    public static final String USERACTIVITY_LIST = "api/myActivity/list";

    public static final String USERACTIVITY_APPLYLIST = "api/useractivity/applylist";

    public static final String ACTIVITY_LIST_URL = "api/activity/list";

    public static final String ACTIVITY_DETAIL_URL = "api/activity/detail";

    public static final String ACTIVITY_DOAPPLY_URL = "api/activity/apply";

    public static final String GET_MESSAGELIST_URL = "api/message/list";
    //shy
    public static final String GET_USER_BASE_INFO_URL = "api/user/getBaseInfo";
    //shy
    public static final String USER_ALL_INFO_URL = "api/user/getMyInfo";

    public static final String PEOPLE_LIST_URL = "api/people/list";
    public static final String PEOPLE_LIST_CHAT = "api/chat/authRefresh";

    public static final String VENSER = "api/vender/put";
    public static final String LOGIN_URL = "api/auth/login";
    //token验证登录
    public static final String CHECK_BY_TOKEN= "api/auth/checkLoginByToken";

    public static final String USER_PHOTO_LIST = "api/userphoto/list";

    //获取用户个人故事详情
    public static final String USER_STORY_DETAIL = "api/userstory/detail";

    public static final String USER_ACTIVITY_NEW = "api/useractivity/new";

    //"举报用户" 接口
    public static final String REPORT_NEW_URL = "api/report/new";

    //shy
    //解锁他人相册
    public static final String LOOK_PHOTO = "/api/user/lookPhoto";
    //end shy

    //" 给用户个人故事点赞" 接口
    public static final String USERSTORY_VOTE_URL = "api/userstory/vote";

    //" 给用户相册点赞" 接口
    public static final String USER_PHOTO_URL = "api/user/votephoto";
    //邀请用户参加活动
    public static final String USERACTIVITY_INVITE = "api/useractivity/invite";

    // 分页获取商城礼物list
    public static final String GIFT_LIST = "api/gift/list";
    // 获取用户收到的礼物list
    public static final String GIFT_RECEIVED_LIST = "api/gift/myRevGift";

    //"新建/编辑用户个人故事" 接口
    public static final String USERSTORY_UPDATE = "api/userstory/update";

    //身份认证
    public static final String CERTIFICATE_EDIT = "api/certificate/edit";

    //注册接口
    public static final String REGIST = "/api/auth/regist";

    //注销接口
    public static final String EXIT = "/api/auth/logout";

    //发送验证码
    public static final String REQUESTCODE = "api/auth/requestCode";

    //修改密码
    public static final String UPDATEPASSWD = "api/auth/updatePasswd";

    //删除照片
    public static final String DELETER = "api/userphoto/delete";

    //删除服务器会话
    public static final String DELETERFROMSERVER = "api/session/deleteFromServer";


    //得到服务器会话
    public static final String GETRFROMSERVER = "api/session/getList";


    //插入会话到服务器
    public static final String UPTOSERVER = "api/session/uploadToServer";
    //服务端会话排序
    public static final String SORTSERVER = "api/session/sortOFserver";

    //添加照片
    public static final String USERPHOTO_ADD = "api/userphoto/add";

    //获取聊天权限
    public static final String AUTH_CHAT = "api/chat/auth";
    //编辑资料
    public static final String USER_EDIT = "api/user/edit";

    //个人展示页面提交资料
    public static final String USER_ANSWER_INSERT = "api/useranswer/insert";

    //显示个性展示答案
    public static final String USER_ANSWER_SHOW = "api/useranswer/show";

    //重置密码
    public static final String AUTH_RESET = "api/auth/reset";

    //获取证件状态
    public static final String CERTIFICATE_GETSTATUS = "api/certificate/getstatus";

    //请求订单信息
    public static final String PAY_GETPREORDER = "api/pay/getPreOrder";

    public static final String SHARCODE_GET = "api/sharecode/get";

    //确认支付宝支付情况
    public static final String CONFIRM_ZHIFUBAO_PAY_RESULT = "api/alipay/orderSelect";

    //确认微信支付情况
    public static final String CONFIRM_WEIXIN_PAY_RESULT = "api/wxpay/orderSelect";

    public static final String CHAT_CONFIRM = "api/chat/confirm";

    public static final String MATCHMAKER_CONNECT = "api/matchmaker/connect";

    public static final String MATCHMAKER_DATE = "api/matchmaker/date";

    public static final String MATCHMAKER_CONFIRM = "api/matchmaker/confirm";

    //会员价格
    public static final String VIP_PRICE_LIST = "api/vip/list";

    //搜索id
    public static final String SEARCH_URL = "api/find/user";

    public static final String ACTIVITY_ORIGINATE = "api/auth/activityOriginate";

    //消息页面红娘信息接口
    public static final String MATCHMAKER_GET = "api/matchmaker/get";
    public static final String MATCHMAKER_CHAT = "api/matchmaker/chat";

    //验证码验证手机号号码是否已经注册
    public static final String USER_HAS = "api/user/has";


    //用户第一次编辑资料
    public static final String FIRST_INFO_EDIT = "api/user/next";

    //启动页(版本检查)
    public static final String CHECK_OS_VERSION = "api/os/checkversion";



    //活动分享
    public static final String SHARE_ACTIVITY = "api/activity/shareActivity";

    //未读的所有系统消息的数目
    public static final String UNREAD_SERVER_MESSAGE_NUM = "api/message/msgcount";
    public static final String UNREAD_SERVER_MESSAGE_STATE = "api/message/readmsg";

    public static final String UNLOCK_ALBUM = "api/user/lockphoto";
}