package com.example.com.meimeng.fragment;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.com.meimeng.MeiMengApplication;
import com.example.com.meimeng.R;
import com.example.com.meimeng.activity.AuthIdentifyActivity;
import com.example.com.meimeng.activity.OfficeEventDetail;
import com.example.com.meimeng.activity.OthersSelfActivity;
import com.example.com.meimeng.adapter.ServiceMessageAdapter;
import com.example.com.meimeng.bean.ServiceMessageBean;
import com.example.com.meimeng.constants.CommonConstants;
import com.example.com.meimeng.gson.GsonTools;
import com.example.com.meimeng.gson.gsonbean.ServiceMessageItem;
import com.example.com.meimeng.gson.gsonbean.ServiceMessageListBean;
import com.example.com.meimeng.net.InternetConstant;
import com.example.com.meimeng.net.InternetUtils;
import com.example.com.meimeng.util.BadgeUtil;
import com.example.com.meimeng.util.DialogUtils;
import com.example.com.meimeng.util.NewMessageState;
import com.example.com.meimeng.util.Utils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONException;
import org.json.JSONStringer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by 010 on 2015/7/13.
 */
public class ServiceMessageFragment extends Fragment {

    @Bind(R.id.list_view)
    PullToRefreshListView listview;

    private Context context;
    private Dialog dialog;
    private ServiceMessageAdapter serviceMessageAdapter;

    //private Handler myhandler;

    private ArrayList<ServiceMessageItem> serviceDataList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.service_m, container, false);
        ButterKnife.bind(this, view);
        MeiMengApplication.currentContext=getActivity();
        context = getActivity();
        serviceMessageAdapter = new ServiceMessageAdapter(context, serviceDataList);
        listview.setAdapter(serviceMessageAdapter);
        /* myhandler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                MessageFragment.getServiceUnreadMsgNum();
                String numServiceUnreadNumText = MeiMengApplication.serviceUnreadNumText.getText() + "";
                if (MeiMengApplication.serviceUnreadNum==null||numServiceUnreadNumText.equals("") || numServiceUnreadNumText == null) {
                    MeiMengApplication.serviceUnreadNumText.setVisibility(View.GONE);
                }
            }
        };*/
        listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                getMessageList();
            }
        });
        final NewMessageState nms = NewMessageState.getInstance(context);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ServiceMessageItem item = (ServiceMessageItem) serviceMessageAdapter.getItem(--position);
                //Utils.getUerVerfiy();
                ImageView redPointImageView = (ImageView) view.findViewById(R.id.service_red_point_image_view);

                Log.d("whh", "getType=" + item.getType());
                if (redPointImageView.getVisibility() == View.VISIBLE) {
                    try {
                        redPointImageView.setVisibility(View.GONE);
//                            CharSequence cs = MeiMengApplication.serviceUnreadNumText.getText();
                        int number = Integer.parseInt(MeiMengApplication.serviceUnreadNumText.getText() + "");
                        if (number > 0) {
                            if (number != 1) {
                                MeiMengApplication.serviceUnreadNumText.setText((Integer.parseInt(MeiMengApplication.serviceUnreadNumText.getText() + "") - 1) + "");
                            } else {
                                MeiMengApplication.serviceUnreadNumText.setText("");
                                MeiMengApplication.serviceUnreadNum = null;
                                MeiMengApplication.serviceUnreadNumText.setVisibility(View.GONE);
                                MeiMengApplication.messageRed.setVisibility(View.GONE);
                            }

                            // MessageFragment.getServiceUnreadMsgNum();
                        }
                        String url = InternetConstant.SERVER_URL + InternetConstant.UNREAD_SERVER_MESSAGE_STATE + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();
                        JSONStringer jsonStringer = new JSONStringer().object().key("messageId").value(item.getMessageId()).endObject();
                        Observable observable = InternetUtils.getJsonObservale(url, jsonStringer.toString());
                        observable.observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Action1() {
                                    @Override
                                    public void call(Object o) {
                                           /* UserPartInfoBean baseInfoBean = GsonTools.getUserPartInfoParam((String) o);*/
                                        Log.d("whh", "...................");
                                    }
                                }, new Action1<Throwable>() {
                                    @Override
                                    public void call(Throwable throwable) {

                                    }
                                });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (MeiMengApplication.sharedPreferences.getBoolean(CommonConstants.MYSTATE, false) == false) {
                    setAlertDialog_a();//资料未填写完成，提示完善资料
                } else {//资料填写完成
                    switch (item.getType()) {

                        //跳转到他人页面
                        case 1://被人关注
                        case 2://故事获赞
                        case 6://获得礼物
                            if (MeiMengApplication.sharedPreferences.getInt(CommonConstants.USER_VERFIY, 0) == 0) {
                                Utils.JudgeUserVerfiy(context, 0);
                            } else if (MeiMengApplication.sharedPreferences.getInt(CommonConstants.USER_VERFIY, 0) == 2) {
                                Utils.JudgeUserVerfiy(context, 2);
                            } else {
                                long senderId = item.getSenderId();
                                nms.setState("new message", 0);
                                BadgeUtil.resetBadgeCount(context);
                                MeiMengApplication.messageRed.setVisibility(View.GONE);
                                Intent intent_1 = new Intent(context, OthersSelfActivity.class);
                                intent_1.putExtra("targetUid", senderId);
                                context.startActivity(intent_1);
                            }
                            break;

                        //跳转到活动页面
                        case 3://被邀请参加活动
                        case 4://活动支付
                            if (MeiMengApplication.sharedPreferences.getInt(CommonConstants.USER_VERFIY, 0) == 0) {
                                Utils.JudgeUserVerfiy(context, 0);
                            } else if (MeiMengApplication.sharedPreferences.getInt(CommonConstants.USER_VERFIY, 0) == 2) {
                                Utils.JudgeUserVerfiy(context, 2);
                            } else {
                                Intent intent_2 = new Intent(context, OfficeEventDetail.class);
                                intent_2.putExtra("mActivityId", item.getContext().getActivityId());

                                context.startActivity(intent_2);
                            }
                            break;

                        //弹出通知
                        case 5://聊天次数增加
                        case 7://审核通知
                            Utils.setPromptDialog(context, item.getContext().getContent());

                            //官方审核类型 审核通过
                            if (item.getStatus() == "1") {
                                //1代表审核消息未读
                                switch (item.getContext().getSubType()) {
                                    case 3://3代表会员审核通过
                                        MeiMengApplication.sharedPreferences.edit().putInt(CommonConstants.USER_VERFIY, 1);
                                        break;
                                    case 2://2代表照片审核通过
                                        MeFragment meFragment = new MeFragment();
                                        meFragment.getMyhandler().sendEmptyMessage(21);
                                        break;
                                    default:
                                        break;
                                }
                            }
                            break;
                        case 8://红娘消息
                            Utils.setPromptDialogSystemMessage(context, item.getContext().getContent());

                            //官方审核类型 审核通过
                            if (item.getStatus() == "1") {
                                //1代表审核消息未读
                                switch (item.getContext().getSubType()) {
                                    case 3://1代表会员审核通过
                                        MeiMengApplication.sharedPreferences.edit().putInt(CommonConstants.USER_VERFIY, 1);
                                        break;
                                    case 2://2代表照片审核通过
                                        MeFragment meFragment = new MeFragment();
                                        meFragment.getMyhandler().sendEmptyMessage(21);
                                        break;
                                    default:
                                        break;
                                }
                            }
                            break;
                    }
                }

            }
        });
        dialog = Utils.createLoadingDialog(getActivity(), "载入中...");
        dialog.show();
        //获取系统消息
        getMessageList();
        return view;
    }



    private Dialog aldialog_a;
    /**
     * 设置Dialog_a
     * 美盟3认证
     */
    private void setAlertDialog_a() {

        //首次編輯資料
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.identify_dialog_layout, null);
        TextView cancle= (TextView) view.findViewById(R.id.simple_dialog_cancel_text);
        TextView identify= (TextView) view.findViewById(R.id.simple_dialog_sure_text);
        aldialog_a = new Dialog(getActivity(), R.style.loading_dialog);
        aldialog_a.setCancelable(false);
        aldialog_a.setContentView(view);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aldialog_a.dismiss();
            }
        });
        identify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), AuthIdentifyActivity.class);
                startActivity(intent);
                aldialog_a.dismiss();
            }
        });
        aldialog_a.show();
    }


  /*  @Override
    public void onResume() {
        Log.i("qwe", "onResume");
        getMessageList();
        Log.e("wz", "===================更新数据onResume");
        super.onResume();
    }*/
    /**
     * 获取系统消息
     */
    public void getMessageList() {
        ChatMessageFragment.getServiceUnreadMsgNum();
        //判断网络是否连接
        if (InternetUtils.isNetworkConnected(context)) {
            try {
                serviceDataList.clear();
                if (TextUtils.isEmpty(Utils.getUserId())) {
                    return;
                }
                if (TextUtils.isEmpty(Utils.getUserToken())) {
                    return;
                }
                String url = InternetConstant.SERVER_URL + InternetConstant.GET_MESSAGELIST_URL + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();

                JSONStringer stringer = new JSONStringer().object()
                        .key("currentPage").value(1)
                        .key("pageSize").value(240)
                        .endObject();
                String jsonData = stringer.toString();
                timeOutCloseDialog();
                Observable observable = InternetUtils.getJsonObservale(url, jsonData);
                observable.observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1() {
                            @Override
                            public void call(Object o) {
                                ServiceMessageListBean bean = GsonTools.getServiceMessageBean((String) o);
                                Log.d("whh", o.toString());

                                if (null != dialog)
                                    dialog.dismiss();

                                if (bean.isSuccess()) {
                                    serviceDataList.clear();
                                    ArrayList<ServiceMessageItem> messageList = bean.getParam().getMsgList();
                                    if (messageList.size() == 0) {
                                        Toast.makeText(getActivity(), "服务消息为空", Toast.LENGTH_LONG).show();
                                    }
                                    for (int i = 0; i < messageList.size(); i++) {
                                        ServiceMessageBean sericeMessageBean = new ServiceMessageBean();
                                        ServiceMessageItem itemBean = messageList.get(i);
                                        long senderId = itemBean.getSenderId();
                                        sericeMessageBean.setSenderId(senderId);

                                        //头像
                                        String picIdStr = itemBean.getSenderHeadPic();
                                        if (picIdStr != null) {
                                            long picId = Long.valueOf(picIdStr);
                                            sericeMessageBean.setHeadPicId(picId);
                                        } else {
                                            //没有头像时的默认头像
                                            sericeMessageBean.setHeadPicId(Long.parseLong("2"));
                                        }

                                        String nickname = itemBean.getSenderNick();
                                        sericeMessageBean.setNickname(nickname);

                                        long time = itemBean.getSendTime();
                                        Date d = new Date(time);
                                        SimpleDateFormat formatdate = new SimpleDateFormat("MM-dd HH:mm");
                                        String timeStr = formatdate.format(d);
                                        sericeMessageBean.setTimeStr(timeStr);

                                        int type = itemBean.getType();

                                        //设置消息类型
                                        sericeMessageBean.setType(type);
//

                                    }
                                    serviceDataList.addAll(messageList);
                                    serviceMessageAdapter.notifyDataSetChanged();
                                } else {
                                    DialogUtils.setDialog(getActivity(), bean.getError());
                                }

                                listview.onRefreshComplete();
                                //myhandler.sendEmptyMessage(0);
                            }
                        });

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (null != dialog)
                dialog.dismiss();
            Toast.makeText(getActivity(), "网络没有连接，请检查您的网络", Toast.LENGTH_LONG).show();
        }


    }

    /**
     * 网络超时提示
     */
    private void timeOutCloseDialog() {
        Timer timer = new Timer();
        TimerTask tk = new TimerTask() {
            @Override
            public void run() {
                mhandler.sendEmptyMessage(20);
            }
        };
        timer.schedule(tk, 7000);
    }

    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 20) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                    Toast.makeText(getActivity(), "当前网络不可用，请检查您的网络设置", Toast.LENGTH_SHORT).show();
                }
            } else {
                //不做操作
            }
        }
    };
}
