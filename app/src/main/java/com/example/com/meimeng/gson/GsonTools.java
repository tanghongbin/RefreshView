package com.example.com.meimeng.gson;


import com.example.com.meimeng.MeiMengApplication;
import com.example.com.meimeng.gson.gsonbean.ActivityOriginateBean;
import com.example.com.meimeng.gson.gsonbean.ApplyUserListBean;
import com.example.com.meimeng.gson.gsonbean.BaseBean;
import com.example.com.meimeng.gson.gsonbean.CertificateStatusBean;
import com.example.com.meimeng.gson.gsonbean.ChatAuthBean;
import com.example.com.meimeng.gson.gsonbean.CheckOsVersionBean;
import com.example.com.meimeng.gson.gsonbean.ConversationInfoBean;
import com.example.com.meimeng.gson.gsonbean.DeleteInfoBean;
import com.example.com.meimeng.gson.gsonbean.EventDetail;
import com.example.com.meimeng.gson.gsonbean.EventDetailBean;
import com.example.com.meimeng.gson.gsonbean.EventListBean;
import com.example.com.meimeng.gson.gsonbean.ExplorejsonBean;
import com.example.com.meimeng.gson.gsonbean.FirstInfoEditBean;
import com.example.com.meimeng.gson.gsonbean.FollowBean;
import com.example.com.meimeng.gson.gsonbean.GiftMallListBean;
import com.example.com.meimeng.gson.gsonbean.HomeJsonBean;
import com.example.com.meimeng.gson.gsonbean.InviteBean;
import com.example.com.meimeng.gson.gsonbean.LoginBean;
import com.example.com.meimeng.gson.gsonbean.LoginTokenBean;
import com.example.com.meimeng.gson.gsonbean.MatchMakerBean;
import com.example.com.meimeng.gson.gsonbean.MatchMakerGetBean;
import com.example.com.meimeng.gson.gsonbean.MyBaseInfoBean;
import com.example.com.meimeng.gson.gsonbean.MyDetailInfoBean;
import com.example.com.meimeng.gson.gsonbean.MyEventObj;
import com.example.com.meimeng.gson.gsonbean.OrderStateWeixinBean;
import com.example.com.meimeng.gson.gsonbean.PeopleListBean;
import com.example.com.meimeng.gson.gsonbean.ReceivedGiftListBean;
import com.example.com.meimeng.gson.gsonbean.RequestCodeBean;
import com.example.com.meimeng.gson.gsonbean.SearchBean;
import com.example.com.meimeng.gson.gsonbean.ServiceMessageListBean;
import com.example.com.meimeng.gson.gsonbean.UnReadServiceMsgNumBean;
import com.example.com.meimeng.gson.gsonbean.UpdateStoryBean;
import com.example.com.meimeng.gson.gsonbean.UploadPictureBean;
import com.example.com.meimeng.gson.gsonbean.UserActivityListBean;
import com.example.com.meimeng.gson.gsonbean.UserAllInfoBean;
import com.example.com.meimeng.gson.gsonbean.UserAnswerShowBean;
import com.example.com.meimeng.gson.gsonbean.UserBaseInfoBean;
import com.example.com.meimeng.gson.gsonbean.UserChatInfoBean;
import com.example.com.meimeng.gson.gsonbean.UserPartInfoBean;
import com.example.com.meimeng.gson.gsonbean.UserPhotoListBean;
import com.example.com.meimeng.gson.gsonbean.UserStoryDetailBean;
import com.example.com.meimeng.gson.gsonbean.VipPriceListBean;
import com.example.com.meimeng.gson.gsonbean.WeiXinPayBean;
import com.example.com.meimeng.gson.gsonbean.ZhiFuBaoPayBean;
import com.example.com.meimeng.gson.gsonbean.ZhifubaoResultBean;
import com.example.com.meimeng.util.DialogUtils;
import com.google.gson.Gson;

/**
 * Gson工具类
 */
public class GsonTools {

    private static Gson gson = new Gson();

    /**
     * 解析首页接口获得的Json数据
     *
     * @param jsonstr 传进去的Json格式的字符串
     * @return 返回HomeJsonBean类的实例
     */
    public static HomeJsonBean getHomeJson(String jsonstr) {
        HomeJsonBean homejson = gson.fromJson(jsonstr, HomeJsonBean.class);
        return homejson;
    }

    /**
     * 解析系统未读消息的数目
     *
     * @param jsonstr 传进去的Json格式的字符串
     * @return 返回HomeJsonBean类的实例
     */
    public static UnReadServiceMsgNumBean getUnReadServiceMsgNumBean(String jsonstr) {
        UnReadServiceMsgNumBean unReadServiceMsgNumBean = gson.fromJson(jsonstr, UnReadServiceMsgNumBean.class);
        return unReadServiceMsgNumBean;
    }

    /**
     * “ 根据条件筛选用户” 接口的数据
     *
     * @param jsonstr
     * @return
     */
    public static ExplorejsonBean getExploreJson(String jsonstr) {
        ExplorejsonBean explorejson = gson.fromJson(jsonstr, ExplorejsonBean.class);
        return explorejson;
    }

    /**
     * 启动页(版本检查)接口的数据解析
     * @param jsonStr
     * @return
     */
    public static CheckOsVersionBean getCheckOsVersionBean(String jsonStr){
        CheckOsVersionBean checkOsVersionBean = gson.fromJson(jsonStr,CheckOsVersionBean.class);
        return checkOsVersionBean;
    }

    /**
     * 删除消息会话数据
     * @param jsonStr
     * @return
     */
    public static DeleteInfoBean getDeleteBean(String jsonStr){
        DeleteInfoBean deleteInfoBean = gson.fromJson(jsonStr,DeleteInfoBean.class);
        return deleteInfoBean;
    }


    /**
     * 获取会话列表
     * @param jsonStr
     * @return
     */
    public static ConversationInfoBean getListConversationBean(String jsonStr){
        ConversationInfoBean conversationInfoBean = gson.fromJson(jsonStr,ConversationInfoBean.class);
        return conversationInfoBean;
    }

    //shy
    /**
     * "获取我的基本信息"接口的数据
     *
     * @param jsonStr
     * @return 解析好的对象
     */
    public static UserBaseInfoBean getUserBaseInfoBean(String jsonStr) {
        UserBaseInfoBean baseInfoBean = gson.fromJson(jsonStr, UserBaseInfoBean.class);
        return baseInfoBean;
    }


    /**
     * 用户第一次编辑资料
     * @param jsonStr
     * @return
     */
    public static FirstInfoEditBean getFirstInfoEditBean(String jsonStr){
        FirstInfoEditBean firstInfoEditBean = gson.fromJson(jsonStr,FirstInfoEditBean.class);
        return firstInfoEditBean;
    }

    /**
     * "获取我的基本信息"接口的数据
     *
     * @param jsonStr
     * @return 解析好的对象
     */
    public static MyBaseInfoBean getMyBaseInfoBean(String jsonStr) {
        MyBaseInfoBean baseInfoBean = gson.fromJson(jsonStr, MyBaseInfoBean.class);
        return baseInfoBean;
    }

    /**
     * "获取我的详细信息"接口的数据
     *
     * @param jsonStr
     * @return 解析好的对象
     */
    public static MyDetailInfoBean getMyDetailInfoBean(String jsonStr) {
        MyDetailInfoBean detailInfoBean = gson.fromJson(jsonStr, MyDetailInfoBean.class);
        return detailInfoBean;
    }

    /**
     * 关注/取消关注用户
     *
     * @param jsonstr 传进去的Json格式的字符
     * @return 返回FollowBean类的实例
     */
    public static FollowBean getFollowJson(String jsonstr) {
        FollowBean followBean = gson.fromJson(jsonstr, FollowBean.class);
        return followBean;
    }

    /**
     * 获取我的关注活动列表
     *
     * @param jsonstr 传进去的Json格式的字符
     * @return 返回UserActivityListBean类的实例
     */
    public static UserActivityListBean getUserActivityListJson(String jsonstr) {
        UserActivityListBean userActivityListBean = gson.fromJson(jsonstr, UserActivityListBean.class);
        return userActivityListBean;
    }

    /**
     * 我的活动Json
     * @param jsonstr
     * @return
     */
    public static MyEventObj getMyEventObjJson(String jsonstr){
        MyEventObj myEventObj = gson.fromJson(jsonstr,MyEventObj.class);
        return myEventObj;
    }

    /**
     * 获取我发起的活动的报名用户列表
     *
     * @param jsonstr 传进去的Json格式的字符
     * @return 返回UserActivityListBean类的实例
     */
    public static ApplyUserListBean getApplyUserListBean(String jsonstr) {
        ApplyUserListBean applyUserListBean = gson.fromJson(jsonstr, ApplyUserListBean.class);
        return applyUserListBean;
    }

    /**
     * 获取“邀请用户”接口
     *
     * @param jsonstr
     * @return
     */
    public static InviteBean getInviteBean(String jsonstr) {
        InviteBean inviteBean = gson.fromJson(jsonstr, InviteBean.class);
        return inviteBean;

    }

    /**
     * “获取活动Lsit”接口的数据
     *
     * @param jsonString
     * @return
     */
    public static EventListBean OfficeEventJson(String jsonString) {
        EventListBean elb = gson.fromJson(jsonString, EventListBean.class);
        return elb;
    }


    /**
     * “获取活动详情”接口的数据
     *
     * @param jsonString
     * @return
     */
    public static EventDetail getEventDetailJson(String jsonString) {
        EventDetailBean edb = gson.fromJson(jsonString, EventDetailBean.class);
        if (!edb.getSuccess()) {
            DialogUtils.setDialog(MeiMengApplication.currentContext, edb.getError());
            return null;
        }
        return edb.getDetail();

    }

    /**
     * "获取用户基本信息"接口的数据
     *
     * @param jsonStr
     * @return 解析好的对象
     */
    //public static UserBaseInfoBean getUserBaseInfoBean(String jsonStr) {
    //    UserBaseInfoBean baseInfoBean = gson.fromJson(jsonStr, UserBaseInfoBean.class);
    //    return baseInfoBean;
    //}

    /**
     * 获取认证状态信息
     *
     * @param jsonStr
     * @return
     */

    public static CertificateStatusBean getCertificateStatusBean(String jsonStr) {
        CertificateStatusBean certificateStatusBean = gson.fromJson(jsonStr, CertificateStatusBean.class);
        return certificateStatusBean;
    }

    /**
     * “登录”接口的数据
     *
     * @param jsonStr
     * @return
     */
    public static LoginBean getLoginBean(String jsonStr) {
        LoginBean loginBean = gson.fromJson(jsonStr, LoginBean.class);

        return loginBean;
    }
    /**
     *
     *
     * @param jsonStr
     * @return
     */
    public static LoginTokenBean getLoginTokenBean(String jsonStr) {
        LoginTokenBean loginBean = gson.fromJson(jsonStr, LoginTokenBean.class);

        return loginBean;
    }

    /**
     * "获取用户资料详情（all）"接口的数据
     *
     * @param jsonStr
     * @return
     */
    public static UserAllInfoBean getUserAllInfoBean(String jsonStr) {
        UserAllInfoBean bean = gson.fromJson(jsonStr, UserAllInfoBean.class);

        return bean;
    }

    /**
     * 获取他人用户详情数据
     *
     * @param jsonStr
     * @return
     */
    public static UserPartInfoBean getUserPartInfoParam(String jsonStr) {

        return gson.fromJson(jsonStr, UserPartInfoBean.class);
    }
    /**
     * 获取他人用户详情数据
     *
     * @param jsonStr
     * @return
     */
    public static UserChatInfoBean getUserChatInfoParam(String jsonStr) {

        return gson.fromJson(jsonStr, UserChatInfoBean.class);
    }

    /**
     * "分页获取相关好友关系list" 接口的数据
     *
     * @param jsonStr
     * @return
     */
    public static PeopleListBean getPeopleListBean(String jsonStr) {
        PeopleListBean bean = gson.fromJson(jsonStr, PeopleListBean.class);
        return bean;
    }

    /**
     * "消息页面红娘信息接口数据
     *
     * @param jsonStr
     * @return
     */
    public static MatchMakerGetBean getMatchMakerGetBean(String jsonStr) {
        MatchMakerGetBean bean = gson.fromJson(jsonStr, MatchMakerGetBean.class);
        return bean;
    }

    /**
     * "获取用户照片list" 接口的数据
     *
     * @param jsonStr
     * @return
     */
    public static UserPhotoListBean getUserPhotoListBean(String jsonStr) {
        UserPhotoListBean bean = gson.fromJson(jsonStr, UserPhotoListBean.class);
        return bean;
    }

    /**
     * “上传图片”接口返回的数据
     *
     * @param jsonStr
     * @return
     */
    public static UploadPictureBean getUploadPhototBean(String jsonStr) {
        UploadPictureBean bean = gson.fromJson(jsonStr, UploadPictureBean.class);
        return bean;
    }

    /**
     * " 获取用户个人故事详情" 接口的数据
     *
     * @param jsonStr
     * @return
     */
    public static UserStoryDetailBean getUserStoryDetailBean(String jsonStr) {
        UserStoryDetailBean bean = gson.fromJson(jsonStr, UserStoryDetailBean.class);
        return bean;
    }

    /**
     * 返回类型为BaseBean时调用
     *
     * @param jsonStr
     * @return
     */
    public static BaseBean getBaseReqBean(String jsonStr) {
        return gson.fromJson(jsonStr, BaseBean.class);
    }

    /**
     * 发送验证码钱查看号码是否已经注册的接口时调用
     *
     * @param jsonStr
     * @return
     */
    public static RequestCodeBean getRequestCodeBean(String jsonStr) {
        return gson.fromJson(jsonStr, RequestCodeBean.class);
    }

    /**
     * 获取用户收到的礼物list
     *
     * @param jsonstr 传进去的Json格式的字符
     * @return 返回UserActivityListBean类的实例
     */
    public static ReceivedGiftListBean getReceivedGiftListJson(String jsonstr) {
        ReceivedGiftListBean receivedGiftListBean = gson.fromJson(jsonstr, ReceivedGiftListBean.class);
        return receivedGiftListBean;
    }

    /**
     * 获得私人红娘电话
     *
     * @param jsonstr
     * @return
     */
    public static MatchMakerBean getMatchMakerJson(String jsonstr) {
        MatchMakerBean matchMakerBean = gson.fromJson(jsonstr, MatchMakerBean.class);
        return matchMakerBean;
    }

    /**
     * 查看剩余红娘帮约次数
     *
     * @param jsonstr
     * @return
     */
    public static MatchMakerBean getMatchMakerDateJson(String jsonstr) {
        MatchMakerBean matchMakerBean = gson.fromJson(jsonstr, MatchMakerBean.class);
        return matchMakerBean;
    }

    /**
     * 发布私人邀约的次数
     *
     * @param jsonstr
     * @return
     */
    public static ActivityOriginateBean getactivityoriginateJson(String jsonstr) {
        ActivityOriginateBean activityoriginateBean = gson.fromJson(jsonstr, ActivityOriginateBean.class);
        return activityoriginateBean;
    }

    /**
     * 分页获取商城礼物list
     *
     * @param jsonstr 传进去的Json格式的字符
     * @return 返回UserActivityListBean类的实例
     */
    public static GiftMallListBean getGiftMallListJson(String jsonstr) {
        GiftMallListBean giftMallListBean = gson.fromJson(jsonstr, GiftMallListBean.class);
        return giftMallListBean;
    }

    /**
     * 新建/编辑用户个人故事 接口的数据
     *
     * @param jsonStr
     * @return
     */
    public static UpdateStoryBean getUpdateStoryBean(String jsonStr) {

        return gson.fromJson(jsonStr, UpdateStoryBean.class);
    }


    /**
     * 获取通知list
     *
     * @param jsonStr
     * @return
     */
    public static ServiceMessageListBean getServiceMessageBean(String jsonStr) {

        return gson.fromJson(jsonStr, ServiceMessageListBean.class);
    }

    /**
     * 获取聊天权限
     *
     * @param jsonstr
     * @return
     */
    public static ChatAuthBean getChatAuthBean(String jsonstr) {
        ChatAuthBean chatAuthBeann = gson.fromJson(jsonstr, ChatAuthBean.class);
        return chatAuthBeann;
    }

    /**
     * 用户个人展示答案接口
     *
     * @param jsonStr
     * @return
     */
    public static UserAnswerShowBean getUserAnswerShowBean(String jsonStr) {

        return gson.fromJson(jsonStr, UserAnswerShowBean.class);
    }

    /**
     * 获取微信支付的参数
     *
     * @param jsonStr
     * @return
     */
    public static WeiXinPayBean getWeiXinPayBean(String jsonStr) {

        return gson.fromJson(jsonStr, WeiXinPayBean.class);
    }

    /**
     * 获取支付宝支付的订单信息
     *
     * @param jsonStr
     * @return
     */
    public static ZhiFuBaoPayBean getZhifubaoPayBean(String jsonStr) {
        return gson.fromJson(jsonStr, ZhiFuBaoPayBean.class);
    }

    /**
     * 得到支付宝支付结果的数据
     *
     * @param jsonStr
     * @return
     */
    public static ZhifubaoResultBean getZhifubaoResultBean(String jsonStr) {
        return gson.fromJson(jsonStr, ZhifubaoResultBean.class);
    }

    /**
     * 得到会员价格的数据
     *
     * @param jsonStr
     * @return
     */
    public static VipPriceListBean getVipPriceListBean(String jsonStr) {
        return gson.fromJson(jsonStr, VipPriceListBean.class);
    }

    /**
     * 得到微信支付确认的结果
     *
     * @param jsonStr
     * @return
     */
    public static OrderStateWeixinBean getOrderStateWeixinBean(String jsonStr) {
        return gson.fromJson(jsonStr, OrderStateWeixinBean.class);
    }

    /**
     * 解析搜索结果的json数据
     *
     * @param jsonStr
     * @return
     */
    public static SearchBean getSearchBean(String jsonStr) {
        return gson.fromJson(jsonStr, SearchBean.class);
    }
}
