package com.example.com.meimeng.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.com.meimeng.MeiMengApplication;
import com.example.com.meimeng.R;
import com.example.com.meimeng.fragment.management.AllFragmentManagement;
import com.example.com.meimeng.gson.GsonTools;
import com.example.com.meimeng.gson.gsonbean.UnReadServiceMsgNumBean;
import com.example.com.meimeng.net.InternetConstant;
import com.example.com.meimeng.net.InternetUtils;
import com.example.com.meimeng.util.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by 010 on 2015/7/13.
 */
public class MessageFragment extends Fragment {
    @Bind(R.id.service_text)
    TextView serviceText;

    @Bind(R.id.service_line_image_view)
    ImageView serviceLineImageView;

    @Bind(R.id.chat_text)
    TextView chatText;

    @Bind(R.id.chat_line_image_view)
    ImageView chatLineImageView;

    @Bind(R.id.service_unread_num_text)
    TextView serviceUnreadNumText;

    //TextView serviceUnreadNum;
    //记录当前的Fragment
    private int currentFragmentIndex = 0;

    private ServiceMessageFragment serviceMessageFragment;
    private ChatMessageFragment chatMessageFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AllFragmentManagement.fragmentList.add(this);
        Log.e("Fragment", "MessageFragment Create");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.message, container, false);
        ButterKnife.bind(this, view);

        MeiMengApplication.currentContext=getActivity();
        //判断进入到这些消息页面是正常进入，还是从通知栏点击消息进入的
        if (MeiMengApplication.messageIntentType == 0) {
            if (serviceMessageFragment == null) {
                serviceMessageFragment = new ServiceMessageFragment();
            }
            replaceFragment(serviceMessageFragment);
            currentFragmentIndex = 0;
            resetState(chatText, serviceText, chatLineImageView, serviceLineImageView);
        } else {
            if (chatMessageFragment == null) {
                chatMessageFragment = new ChatMessageFragment();
            }
            replaceFragment(chatMessageFragment);
            currentFragmentIndex = 1;
            resetState(serviceText, chatText, serviceLineImageView, chatLineImageView);
        }

        MeiMengApplication.serviceUnreadNumText=serviceUnreadNumText;
        //获取系统未读的消息
        getServiceUnreadMsgNum();
        return view;
    }

    private int getScreenWidth() {
        WindowManager manager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        return width;
    }

    /**
     * 点击切换服务消息
     */
    @OnClick(R.id.message_service_layout)
    void serviceLayoutListener() {
        if (currentFragmentIndex != 0) {
            if (serviceMessageFragment == null) {
                serviceMessageFragment = new ServiceMessageFragment();
            }
            replaceFragment(serviceMessageFragment);
            currentFragmentIndex = 0;
            resetState(chatText, serviceText, chatLineImageView, serviceLineImageView);
        }
    }

    /**
     * 点击切换聊天消息
     */
    @OnClick(R.id.message_chat_layout)
    void chatLayoutListener() {
        if (currentFragmentIndex != 1) {
            if (chatMessageFragment == null) {
                chatMessageFragment = new ChatMessageFragment();
            }
            replaceFragment(chatMessageFragment);
            currentFragmentIndex = 1;
            resetState(serviceText, chatText, serviceLineImageView, chatLineImageView);
        }

    }

    /**
     * 替换Fragment
     *
     * @param newFragment
     */
    private void replaceFragment(Fragment newFragment) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.message_fragment_layout, newFragment);
        transaction.commit();
    }

    /**
     * 修改标题栏的状态
     *
     * @param oldText
     * @param newText
     * @param oldLine
     * @param newLine
     */
    private void resetState(TextView oldText, TextView newText, ImageView oldLine, ImageView newLine) {
        oldText.setTextColor(getResources().getColor(R.color.text_dark));
        oldLine.setVisibility(View.GONE);

        newText.setTextColor(getResources().getColor(R.color.black_text_color));
        newLine.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AllFragmentManagement.fragmentList.remove(this);
        Log.e("Fragment", "MessageFragment Destroy");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden == false&&currentFragmentIndex==0) {
            serviceMessageFragment = new ServiceMessageFragment();
            replaceFragment(serviceMessageFragment);
        }else if(hidden == false&&currentFragmentIndex==1){
            //获取系统未读的消息
            getServiceUnreadMsgNum();
        }
    }

    /**
     * 获取系统未读的消息
     * @return
     */
    public static void getServiceUnreadMsgNum(){
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
                                if (unReadServiceMsgNumBean.getParam().getUnread()!=null){
                                    //系统未读消息数目
                                    Integer num = unReadServiceMsgNumBean.getParam().getUnread();
                                   MeiMengApplication.serviceUnreadNum=num;
                                    if (num!=null){
                                        MeiMengApplication.serviceUnreadNumText.setText(num+"");
                                        MeiMengApplication.serviceUnreadNumText.setVisibility(View.VISIBLE);
                                    }else {
                                        MeiMengApplication.serviceUnreadNumText.setVisibility(View.GONE);
                                    }
                                }else {
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
