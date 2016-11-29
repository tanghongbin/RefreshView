package com.xqd.chatmessage.fragment;

import android.app.Fragment;
import android.content.Context;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avoscloud.leanchatlib.controller.AVIMTypedMessagesArrayCallback;
import com.avoscloud.leanchatlib.controller.ChatManager;
import com.avoscloud.leanchatlib.controller.MessageHelper;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xqd.chatmessage.R;
import com.xqd.chatmessage.activity.HomeActivity;
import com.xqd.chatmessage.adapter.ChatMessageAdapter;
import com.xqd.chatmessage.db.DbOpenHelper;
import com.xqd.chatmessage.gson.ChatMessageBean;
import com.xqd.chatmessage.gson.GsonTools;
import com.xqd.chatmessage.gson.SearchBean;
import com.xqd.chatmessage.gson.UserListBean;
import com.xqd.chatmessage.gson.UserListItem;
import com.xqd.chatmessage.net.InternetConstant;
import com.xqd.chatmessage.net.InternetUtils;
import com.xqd.chatmessage.util.ChatSQliteDataUtil;
import com.xqd.chatmessage.util.Utils;

import org.json.JSONStringer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by Administrator on 2015/9/9.
 */
public class MessageFragment extends Fragment {


    private String otherId;
    private Context context;
    public static ChatMessageAdapter chatMessageAdapter;

    private boolean flag = false;
    private int sine = 0;
    private int sine_x = 0;

    private final int REFRESHOVER = 10;
    private final  int  REFRESHERROR = 20;

    private ChatSQliteDataUtil sq = null;


    @Bind(R.id.chat_n_listview)
    PullToRefreshListView pullToRefreshListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AllFragmentManagement.fragmentList.add(this);

    }
    //key: "userid","bitmap" ,"name","time" ,"content"
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = getActivity();
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        ButterKnife.bind(this, view);


        sq = new ChatSQliteDataUtil();

        chatMessageAdapter = new ChatMessageAdapter(getActivity(), HomeActivity.dataList);
        pullToRefreshListView.setAdapter(chatMessageAdapter);
        //获取最近聊天列表
        if(HomeActivity.dataList.size() == 0){

            RefreshData();
        }



        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String time_label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("更新于：" + time_label);

                Log.i("msg","tk+1");
                RefreshData();

               mhandler.sendEmptyMessage(REFRESHOVER);
            }
        });

        return view;
    }

    private void RefreshData(){
//        if(!flag) {
//            Log.i("msg","tk+2");
//            flag = true;
//            HomeActivity.dataList.clear();
//            addRefreshData();
        HomeActivity.dataList.clear();
        for(int i = 0; i < sq.getDataAll().size() ; i++){
            ChatMessageBean bean = sq.getDataAll().get(i);
            if(bean.getContent() != null && !bean.getContent().equals("")){
                HomeActivity.dataList.add(bean);
            }
        }
            chatMessageAdapter.setDataList(HomeActivity.dataList);
            chatMessageAdapter.notifyDataSetChanged();

//        }
    };

    private Handler mhandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == REFRESHOVER){
                for(int i = 0; i < sq.getDataAll().size() ; i++){
                    Log.i("msg", sq.getDataAll().get(i).toString());
                }

                pullToRefreshListView.onRefreshComplete();
            }
        }
    };


//    private Handler mhandler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            if(msg.what == REFRESHOVER  ){
//                Log.i("msg", HomeActivity.dataList.size()+"");
//                chatMessageAdapter.setDataList(sq.getDataAll());
//                chatMessageAdapter.notifyDataSetChanged();
//                pullToRefreshListView.onRefreshComplete();
//            //    sine_x++;
//            //    if(sine_x == sine) {
//           //        sq.addData(HomeActivity.dataList.get(0));
//                    Log.i("msg", sq.getDataAll().get(0).toString());
//
//                    flag = false;
//                    sine_x = 0;
//           //     }
//            }
//
//            if(msg.what == REFRESHERROR){
//                Log.i("msg","Error");
//            //    Toast.makeText(context,"刷新失败！",Toast.LENGTH_SHORT).show();
//                pullToRefreshListView.onRefreshComplete();
//                sine_x++;
//                if(sine_x == sine) {
//            //       sq.addData(HomeActivity.dataList.get(0));
//                    sq.getDataAll().get(0).toString();
//                    flag = false;
//                    sine_x = 0;
//                }
//            }
//        }
//    };

    /**
     * 获取最近聊天列表
     */
//    private void addRefreshData() {
//
//        try {
//            if (TextUtils.isEmpty(Utils.getUserId())) {
//                return;
//            }
//            if (TextUtils.isEmpty(Utils.getUserToken())) {
//                return;
//            }
//            String url = InternetConstant.SERVER_URL + InternetConstant.PEOPLE_LIST_URL + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();
//            JSONStringer stringer = new JSONStringer().object()
//                    .endObject();
//            String jsonStr = stringer.toString();
//            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
//            observable.observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Action1() {
//                        @Override
//                        public void call(Object o) {
//                            UserListBean peopleListBean = GsonTools.getUserListBean((String) o);
//                            if (peopleListBean.isSuccess()) {
//                                ArrayList<UserListItem> peopleListItems = peopleListBean.getParam().getLst();
//                                ArrayList<Long> userIdList = new ArrayList<Long>();
//                                for (int i = 0; i < peopleListItems.size(); i++) {
//                                    userIdList.add(peopleListItems.get(i).getUid());
//                                }
//                                sine = userIdList.size();
//                                Log.i("msg",sine+"");
//                                for(int i = 0 ; i < userIdList.size() ; i++){
//                                    final Long id = userIdList.get(i);
//                                    new Thread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            getRecentConversation(id);
//
//                                        }
//                                    }).start();
//                                }
//                 //               getRecentConversation(userIdList);
//                            } else {
//
//                                Toast.makeText(context, peopleListBean.getError(), Toast.LENGTH_LONG).show();
//                            }
//
//                         //   pullToRefreshListView.onRefreshComplete();
//                        }
//                    }, new Action1<Throwable>() {
//                        @Override
//                        public void call(Throwable throwable) {
//                            Log.e("TAG", throwable.getMessage());
//                        }
//                    });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 得到最近的会话记录
//     */
//    private void getRecentConversation(final Long targeUid) {
//
//        //从leanCloud获取最新消息
//
//        //更改成从服务器获取我的关注的和关注我的人的id
//        //从本地已打开过的会话中获取消息
//    //    for (final Long targeUid : userIdList) {
//
//            otherId = String.valueOf(targeUid);
//
//            if (TextUtils.isEmpty(otherId) == false) {
//
//                final ChatManager chatManager = ChatManager.getInstance();
//                chatManager.fetchConversationWithUserId(otherId, new AVIMConversationCreatedCallback() {
//                    @Override
//                    public void done(AVIMConversation avimConversation, AVException e) {
//                        if (e != null) {
//                     //       pullToRefreshListView.onRefreshComplete();
//                            Log.i("msg","e is not null    1");
//                            mhandler.sendEmptyMessage(REFRESHERROR);
//                        } else {
//                            System.out.println(avimConversation.getConversationId());
//                            ChatMessageBean chatMessageBean = new ChatMessageBean();
//                            chatMessageBean.setConversation(avimConversation);
//                            chatMessageBean.setUserId(targeUid);
//                            getMessage(chatMessageBean);
//                        }
//                     //   pullToRefreshListView.onRefreshComplete();
//                    }
//                });
//    //        }
//        }
//    }
//
//    /**
//     * 得到聊天消息
//     */
//    private void getMessage(final ChatMessageBean chatMessageBean) {
//
//        final ChatManager chatManager = ChatManager.getInstance();
//
//        chatManager.queryMessages(chatMessageBean.getConversation(), null, System.currentTimeMillis(), 1, new AVIMTypedMessagesArrayCallback() {
//            @Override
//            public void done(List<AVIMTypedMessage> typedMessages, AVException e) {
//                if (null != e) {
//                  //  pullToRefreshListView.onRefreshComplete();
//                    Log.i("msg","e is not null    2");
//                    mhandler.sendEmptyMessage(REFRESHERROR);
//                } else {
//                    if (typedMessages.get(0) != null) {
//
//                        Date date = new Date(typedMessages.get(0).getTimestamp());
//                        SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
//
//                        chatMessageBean.setTime(format.format(date));
//
//                        chatMessageBean.setContent(MessageHelper.outlineOfMsg(typedMessages.get(0)));
//               //         chatMessageAdapter.notifyDataSetChanged();
//                        getUserBaseInfo(chatMessageBean);
//
//                    }
//                //    pullToRefreshListView.onRefreshComplete();
//                }
//            }
//        });
//    }
//
//    /**
//     * 得到聊天对象的个人信息
//     */
//    private void getUserBaseInfo(final ChatMessageBean chatMessageBean) {
//        try {
//
//            //得到对象id
//            String targetUid = String.valueOf(chatMessageBean.getUserId());
//            if (TextUtils.isEmpty(Utils.getUserId())) {
//                return;
//            }
//            if (TextUtils.isEmpty(Utils.getUserToken())) {
//                return;
//            }
//
//            String url = InternetConstant.SERVER_URL + InternetConstant.SEARCH_URL + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();
//            JSONStringer stringer = new JSONStringer().object().key("targetUid").value(targetUid).endObject();
//            String jsonStr = stringer.toString();
//            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
//            observable.observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Action1() {
//                        @Override
//                        public void call(Object o) {
//                            SearchBean searchBean = GsonTools.getSearchBean((String) o);
//                            if (searchBean.isSuccess()) {
//                                chatMessageBean.setHeadPic(searchBean.getParam().getUser().getHeadPic());
//                                chatMessageBean.setName(searchBean.getParam().getUser().getNickname());
//                                HomeActivity.dataList.add(chatMessageBean);
//                                mhandler.sendEmptyMessage(REFRESHOVER);
//
//                            } else {
//                                Log.e("TAG", "初始化出错了");
//                                mhandler.sendEmptyMessage(REFRESHERROR);
//
//                            }
//                        }
//                    }, new Action1<Throwable>() {
//                        @Override
//                        public void call(Throwable throwable) {
//                            Log.e("获取用户基本信息失败了", throwable.getMessage());
//                        }
//                    });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }


}
