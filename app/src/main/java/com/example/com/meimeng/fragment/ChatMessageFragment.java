package com.example.com.meimeng.fragment;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avoscloud.leanchatlib.controller.ChatManager;
import com.example.com.meimeng.MeiMengApplication;
import com.example.com.meimeng.R;
import com.example.com.meimeng.activity.HomeActivity;
import com.example.com.meimeng.activity.ServiceMessageActivity;
import com.example.com.meimeng.custom.slidelistview.SlidePauseListView;
import com.example.com.meimeng.fragment.management.AllFragmentManagement;
import com.example.com.meimeng.gson.GsonTools;
import com.example.com.meimeng.gson.gsonbean.ConversationInfoBean;
import com.example.com.meimeng.gson.gsonbean.MatchMakerGetBean;
import com.example.com.meimeng.gson.gsonbean.UnReadServiceMsgNumBean;
import com.example.com.meimeng.net.InternetConstant;
import com.example.com.meimeng.net.InternetUtils;
import com.example.com.meimeng.util.ChatSQliteDataUtil;
import com.example.com.meimeng.util.DialogUtils;
import com.example.com.meimeng.util.MessageUtils;
import com.example.com.meimeng.util.NewMessageState;
import com.example.com.meimeng.util.Utils;

import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by 010 on 2015/7/13.
 * 聊天列表
 */
public class ChatMessageFragment extends Fragment {

    private final String TAG = "ChatMessageFragment";
    protected AVIMConversation conversation;
    private Context context;
    private View viewHead;

    //红娘的信息
    private TextView matchmakerName;
    private String matchmakerNamestr;
    private TextView matchmakerTel;
    private TextView matchmakerTime;
    private TextView serviceUnreadNumText;
    private Long matchmakerId;
    private LinearLayout matchmakerLayout;
    private RelativeLayout service_information;
    private List conversations;

    public static com.example.com.meimeng.adapter.ChatMessageAdapter chatMessageAdapter;
    private boolean flag = false;
    private final int REFRESHOVER = 10;
    private final int ISREFRESH = 20;
    private final int TIMEOUT = 30;

    private boolean isCreate = false;

    //本地数据库
    private ChatSQliteDataUtil sq = null;
    private Dialog dialog;
    private DisplayMetrics dm;
    @Bind(R.id.chat_n_listview)
    SlidePauseListView slidePauseListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AllFragmentManagement.fragmentList.add(this);
        Log.e("Fragment", "MessageFragment Create");
        dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        isCreate = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = getActivity();
        View view = inflater.inflate(R.layout.chat_n, container, false);
        ButterKnife.bind(this, view);
        MeiMengApplication.currentContext = getActivity();
        sq = new ChatSQliteDataUtil(getActivity());
        conversations = new ArrayList();
        addViewHead();
        chatMessageAdapter = new com.example.com.meimeng.adapter.ChatMessageAdapter(getActivity(), handler, HomeActivity.dataList);
        slidePauseListView.setAdapter(chatMessageAdapter);
        slidePauseListView.setTouchLimit((dm.widthPixels * 1 / 5));
        chatMessageAdapter.setDataList(HomeActivity.dataList);
        chatMessageAdapter.notifyDataSetChanged();

      /*  slidePauseListView.setInterface(new SlidePauseListView.MyLoadListener() {
            @Override
            public void onLoad() {
                getData();
            }
        });*/
        return view;
    }

    private void addViewHead() {//ListView头部

        viewHead = LayoutInflater.from(getActivity()).inflate(R.layout.chat_n_head, null);
        serviceUnreadNumText = (TextView) viewHead.findViewById(R.id.message_red_matcher);
        matchmakerName = (TextView) viewHead.findViewById(R.id.chat_n_matcher_name);
        matchmakerTel = (TextView) viewHead.findViewById(R.id.chat_n_matcher_message);
        matchmakerTime = (TextView) viewHead.findViewById(R.id.chat_n_matcher_time);
        //红娘头像上的小红点
        MeiMengApplication.messageRedMatcher = (TextView) viewHead.findViewById(R.id.message_red_matcher);
        matchmakerTime.setVisibility(View.GONE);
        matchmakerLayout = (LinearLayout) viewHead.findViewById(R.id.chat_n_matcher);
        service_information = (RelativeLayout) viewHead.findViewById(R.id.service_information);
        getMatchmaker(REFRESHOVER);
        final NewMessageState nms = NewMessageState.getInstance(context);
        //点击红娘聊天
        matchmakerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (flag) {

                    // 正在执行中不可点击
                } else {
                    flag = true;
                    getMatchmaker(ISREFRESH);
                }
            }
        });
        //点击进入系统消息
        service_information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ServiceMessageActivity.class);
                startActivity(intent);
            }
        });
        MeiMengApplication.serviceUnreadNumText = serviceUnreadNumText;
        //获取系统未读的消息
        getServiceUnreadMsgNum();
        slidePauseListView.addHeaderView(viewHead);

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1000) {
                slidePauseListView.LoadComplet();
            }
        }
    };

    @Override
    public void onResume() {
        Log.i("qwe", "onResume");
        getMatchmaker(REFRESHOVER);
        getData();
        Log.e(TAG, "===================更新数据onResume");
        super.onResume();
    }

    //判断是否超时
    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            NewMessageState nms = NewMessageState.getInstance(context);
            switch (msg.what) {
                case REFRESHOVER:
                    //不做操作
                    break;
                case TIMEOUT:
                    try {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                            flag = false; //超时可点击

                            Toast.makeText(context, "当前网络不可用，请检查您的网络设置", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    }

                    break;
                case ISREFRESH:
                    MessageUtils.setLeanCloudSelfUid();
                    MessageUtils.setLeanCloudMatchmakerUid(context, matchmakerId, matchmakerNamestr);
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                        flag = false; //执行结束可点击
                    }
                    MeiMengApplication.messageRed.setVisibility(View.GONE);
                    nms.setState("hn" + matchmakerId, 0);
                    MeiMengApplication.messageRedMatcher.setVisibility(View.GONE);
                    nms.setState("new message", 0);
                    break;
            }
        }
    };

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden == false) {
            //获取系统未读的消息
            getServiceUnreadMsgNum();
            getData();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isCreate != true) {
            //获取系统未读的消息
            getServiceUnreadMsgNum();
            getData();
        }else{
            isCreate=false;
        }
    }

    //更新数据
    private void getData() {
        HomeActivity.dataList.clear();
        HomeActivity.dataList.addAll(sq.getDataAll());
        chatMessageAdapter.setDataList(HomeActivity.dataList);
        chatMessageAdapter.notifyDataSetChanged();

    }

    /**
     * 红娘信息
     */
    private void getMatchmaker(final int handlemesssage) {
        final NewMessageState nms = NewMessageState.getInstance(context);
        try {
            if (TextUtils.isEmpty(Utils.getUserId())) {
                return;
            }
            if (TextUtils.isEmpty(Utils.getUserToken())) {
                return;
            }
            if (handlemesssage == ISREFRESH) {
                timeOutCloseDialog();
                dialog = Utils.createLoadingDialog(getActivity(), "请稍后...");
                dialog.show();
                dialog.setCancelable(false);
            }
            String url = InternetConstant.SERVER_URL + InternetConstant.MATCHMAKER_CHAT + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();
            JSONStringer stringer = new JSONStringer().object().endObject();
            String jsonStr = stringer.toString();
            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1() {
                        @Override
                        public void call(Object o) {
                            MatchMakerGetBean matchMakerGetBean = GsonTools.getMatchMakerGetBean((String) o);
                            if (matchMakerGetBean.isSuccess()) {

                                //设置获取到的红娘信息
                                // matchmakerName.setText(matchMakerGetBean.getParam().getMatchmaker().getName());
                                matchmakerNamestr = matchMakerGetBean.getParam().getMatchmaker().getName();
                                matchmakerName.setText(matchmakerNamestr);
                                matchmakerTel.setText(matchMakerGetBean.getParam().getMatchmaker().getTel());
                                matchmakerId = matchMakerGetBean.getParam().getMatchmaker().getManagerId();
                                mhandler.sendEmptyMessage(handlemesssage);
                                int state = nms.getState("hn" + matchmakerId);
                                if (state == -1) {
                                    nms.setState("hn" + matchmakerId, 0);
                                } else if (state != 0) {
                                    MeiMengApplication.messageRedMatcher.setVisibility(View.VISIBLE);
                                    MeiMengApplication.messageRedMatcher.setText(state + "");
                                }

                            } else {
                                DialogUtils.setDialog(context, matchMakerGetBean.getError());
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            Log.e("TAG", throwable.getMessage());
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 超时设置
     */
    private void timeOutCloseDialog() {
        Timer timer = new Timer();
        TimerTask tk = new TimerTask() {
            @Override
            public void run() {
                mhandler.sendEmptyMessage(TIMEOUT);
            }
        };
        timer.schedule(tk, 8000);
    }

    @Override
    public void onPause() {
        super.onPause();
        getListConversations();//客服端排序该用户服务端会话

    }

    //服务端排序
    private void sortOfServer(List conversations) {
        try {
            //获取用户的uid,和token
            if (TextUtils.isEmpty(Utils.getUserId())) {
                return;
            }
            if (TextUtils.isEmpty(Utils.getUserToken())) {
                return;
            }

            String url = InternetConstant.SERVER_URL + InternetConstant.SORTSERVER + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();
            JSONStringer jsonStringer = new JSONStringer().object()
                    .key("uid").value(Utils.getUserId())
                    .key("conversations");
            for (int i = 0; i < conversations.size(); i++) {
                jsonStringer.value(conversations.get(i));
            }
            jsonStringer.endObject();
            String jsonStr = jsonStringer.toString();
            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                             Log.d("wz", "排序成功。。。。。。。。。。。");
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable error) {
                            //Log.e("test:", error.getMessage());

                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void getListConversations() {
        try {
            //获取用户的uid,和token
            if (TextUtils.isEmpty(Utils.getUserId())) {
                return;
            }
            if (TextUtils.isEmpty(Utils.getUserToken())) {
                return;
            }
            String url = InternetConstant.SERVER_URL + InternetConstant.GETRFROMSERVER + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();
            JSONStringer jsonStringer = new JSONStringer().object()
                    .key("uid").value(Long.parseLong(Utils.getUserId()))
                    .endObject();
            String jsonStr = jsonStringer.toString();
            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            ConversationInfoBean conversationInfoBean = GsonTools.getListConversationBean(s);
                            List conversations = conversationInfoBean.getParam().getResultDO().getConversations();
                            final ChatManager chatManager = ChatManager.getInstance();
                            chatManager.setOnConversation(new ChatManager.OnConversation() {
                                @Override
                                public void onResult(List<AVIMConversation> list) {
                                    sortOfServer(list);
                                }
                            });
                            chatManager.fetchConversationById(conversations);
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable error) {
                            Log.e("test:", error.getMessage());
                        }

                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取系统未读的消息
     *
     * @return
     */
    public static void getServiceUnreadMsgNum() {
        try {

            //获取用户的uid,和token
            if (TextUtils.isEmpty(Utils.getUserId())) {
                return;
            }
            if (TextUtils.isEmpty(Utils.getUserToken())) {
                return;
            }

            String url = InternetConstant.SERVER_URL + InternetConstant.UNREAD_SERVER_MESSAGE_NUM + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();


            String jsonStr = "{}";
            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            UnReadServiceMsgNumBean unReadServiceMsgNumBean = GsonTools.getUnReadServiceMsgNumBean(s);
                            if (unReadServiceMsgNumBean.isSuccess()) {
                                if (unReadServiceMsgNumBean.getParam().getUnread() != null) {
                                    //系统未读消息数目
                                    Integer num = unReadServiceMsgNumBean.getParam().getUnread();
                                    MeiMengApplication.serviceUnreadNum = num;
                                    if (num != null) {
                                        MeiMengApplication.serviceUnreadNumText.setText(num + "");
                                        MeiMengApplication.serviceUnreadNumText.setVisibility(View.VISIBLE);
                                    } else {
                                        MeiMengApplication.serviceUnreadNumText.setVisibility(View.GONE);
                                    }
                                } else {
                                    MeiMengApplication.serviceUnreadNumText.setVisibility(View.GONE);
                                }
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable error) {
                            Log.e("test:", error.getMessage());
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
