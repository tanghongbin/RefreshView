package com.xqd.chatmessage.gson;


import com.google.gson.Gson;

/**
 * Gson工具类
 */
public class GsonTools {

    /**
     * 解析首页接口获得的Json数据
     *
     * @param jsonstr 传进去的Json格式的字符串
     * @return 返回HomeJsonBean类的实例
     */


    private static Gson gson = new Gson();

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
     * "分页获取相关好友关系list" 接口的数据
     *
     * @param jsonStr
     * @return
     */
    public static UserListBean getUserListBean(String jsonStr) {
        UserListBean bean = gson.fromJson(jsonStr, UserListBean.class);
        return bean;
    }

    /**
     * 解析搜索结果的json数据
     * @param jsonStr
     * @return
     */
    public static SearchBean getSearchBean(String jsonStr){
        return gson.fromJson(jsonStr,SearchBean.class);
    }

    public static IsFriendBean getIsFriendBean(String jsonStr){
        IsFriendBean bean = gson.fromJson(jsonStr,IsFriendBean.class);
        return bean;
    }
}
