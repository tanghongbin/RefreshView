package com.example.com.meimeng.net;

import android.text.TextUtils;

import com.example.com.meimeng.util.Utils;

import org.json.JSONException;
import org.json.JSONStringer;

/**
 * Created by Administrator on 2015/7/23.
 */
public class BuildString {

    /**
     * 活动列表的URL
     *
     * @return
     */
    public static String EventListUrl() {
        //获取用户的uid,和token
        if (TextUtils.isEmpty(Utils.getUserId())) {
            return "";
        }
        if (TextUtils.isEmpty( Utils.getUserToken() )) {
            return "";
        }
        return InternetConstant.SERVER_URL + InternetConstant.ACTIVITY_LIST_URL+"?uid="+Utils.getUserId()+"&token="+Utils.getUserToken();
    }


    /**
     * 活动详情的URL
     *
     * @param uid   用户id
     * @param token 登陆产生的token
     * @return
     */
    public static String EventDetailUrl(String uid, String token) {
        return InternetConstant.SERVER_URL + InternetConstant.ACTIVITY_DETAIL_URL + "?uid=" + String.valueOf(uid) + "&token=" + token;
    }

    /**
     * 返回活动页的json的请求体
     *
     * @param type        活动类型 1.官方活动，2.私人邀约，3.六人约会
     * @param currentPage 当前活动页
     * @param pageSize    每次请求个数
     * @return
     * @throws JSONException
     */
    public static String EventListReqBody(int type, int currentPage, int pageSize) throws JSONException {

        JSONStringer jsonStringer = new JSONStringer().object().key("type").value(String.valueOf(type))
                .key("currentPage").value(String.valueOf(currentPage))
                .key("pageSize").value(String.valueOf(pageSize))
                .endObject();
        return jsonStringer.toString();

    }

    /**
     * 获得全部的活动
     * @param currentPage 当前活动页
     * @param pageSize    每次请求个数
     * @return
     * @throws JSONException
     */

    public static String allEventListReqBody(int currentPage, int pageSize)throws JSONException{

        JSONStringer jsonStringer = new JSONStringer().object().key("type").value("1")
                .key("currentPage").value(String.valueOf(currentPage))
                .key("pageSize").value(String.valueOf(pageSize))
                .endObject();
        return jsonStringer.toString();
    }


    /**
     * 返回活动详情的请求体
     *
     * @param activityId 活动id
     * @param
     * @return
     * @throws JSONException
     */
    public static String EventDetailReqBody(long activityId) throws JSONException {

        JSONStringer jsonStringer = new JSONStringer().object().key("activityId").value(String.valueOf(activityId))

                .endObject();
        return jsonStringer.toString();
    }

    /**
     * "给用户个人故事点赞" 接口URL
     *
     * @param uid
     * @param token
     * @return
     */
    public static String VoteToUserStroryUrl(String uid, String token) {
        return InternetConstant.SERVER_URL + InternetConstant.USERSTORY_VOTE_URL + "?uid=" + String.valueOf(uid) + "&token=" + token;
    }

    /**
     * 给用户故事点赞的请求体
     *
     * @param voteUid
     * @return
     * @throws JSONException
     */
    public static String VoteStoryReqBody(long voteUid) throws JSONException {
        JSONStringer jsonStringer = new JSONStringer().object().key("voteByuid").value(String.valueOf(voteUid)).endObject();
        return jsonStringer.toString();
    }
}
