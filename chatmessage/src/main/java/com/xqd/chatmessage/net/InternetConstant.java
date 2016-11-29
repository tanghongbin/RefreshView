package com.xqd.chatmessage.net;

import com.xqd.chatmessage.util.Utils;

/**
 * Created by Administrator on 2015/9/9.
 */
public class InternetConstant {
    public static final String USETOKEN = "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();

    //主机域名

//   public static final String SERVER_URL = "http://119.254.101.89:8080/meimeng/";    //外网
    public static final String SERVER_URL = "http://192.168.2.8:8080/meimeng/";   //内网

    public static final String UPLOAD_PICTURE_URL = "api/img/upload";

    public static final String PEOPLE_LIST_URL = "api/maker/getUserList";

    public static final String LOGIN_URL = "api/maker/login";

    public static final String AUTH_CHAT = "api/maker/isFriend";

    //搜索id
    public static final String SEARCH_URL="api/maker/findUser";
}
