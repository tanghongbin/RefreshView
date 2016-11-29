package com.example.com.meimeng.fragment;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avoscloud.leanchatlib.controller.ChatManager;
import com.example.com.meimeng.MeiMengApplication;
import com.example.com.meimeng.R;
import com.example.com.meimeng.activity.AuthIdentifyActivity;
import com.example.com.meimeng.activity.OthersSelfActivity;
import com.example.com.meimeng.adapter.SlideListAdapter;
import com.example.com.meimeng.bean.ChatMessageBean;
import com.example.com.meimeng.bean.LikePeopleBean;
import com.example.com.meimeng.constants.CommonConstants;
import com.example.com.meimeng.custom.slidelistview.SlidePauseListView;
import com.example.com.meimeng.gson.GsonTools;
import com.example.com.meimeng.gson.gsonbean.DeleteInfoBean;
import com.example.com.meimeng.gson.gsonbean.FollowBean;
import com.example.com.meimeng.gson.gsonbean.MatchMakerBean;
import com.example.com.meimeng.gson.gsonbean.PeopleListBean;
import com.example.com.meimeng.gson.gsonbean.PeopleListItem;
import com.example.com.meimeng.net.InternetConstant;
import com.example.com.meimeng.net.InternetUtils;
import com.example.com.meimeng.util.ChatSQliteDataUtil;
import com.example.com.meimeng.util.DialogUtils;
import com.example.com.meimeng.util.MessageUtils;
import com.example.com.meimeng.util.Utils;

import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by 010 on 2015/7/17.
 */
public class ListSlideFragment extends Fragment implements SlideListAdapter.OnSlideClickListener, SlidePauseListView.MyLoadListener {

    private static final String TAG = "ListSlideFragment";

    @Bind(R.id.list_view_slide_list)
    SlidePauseListView slidePauseListView;

    private volatile ArrayList<LikePeopleBean> likePeopleBeans = new ArrayList<>();

    private SlideListAdapter slideListAdapter;
    private int currentPage = 0;
    private Context context;
    private ChatSQliteDataUtil sq;
    private int ty;
    private String type;
    private  long otherUserId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_view_layout, container, false);

        ButterKnife.bind(this, view);
        MeiMengApplication.currentContext=getActivity();
        context = getActivity();
        Bundle bundle = getArguments();
        type = bundle.getString("type");
        //setGetData();

        slidePauseListView.setOnScrollListener(onScrollListener);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        setGetData();
    }



    private AbsListView.OnScrollListener onScrollListener=new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (AbsListView.OnScrollListener.SCROLL_STATE_IDLE == scrollState) {
                if (view.getFirstVisiblePosition()==0) {
                    setGetData();
                }

                if (view.getLastVisiblePosition() ==view.getCount()) {
                    //获取更多数据
                    getDataFoot(ty);
                }
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        }
    };
    private void setGetData() {
        if (type.equals("myLike")) {
            ty = 1;
            //我中意的
            getData(1);

        } else if (type.equals("likeMe")) {
            //中意我的
            ty = 2;
            //禁止滑动
            slidePauseListView.setIsSlide(false);
            getData(2);
        } else {
            ty = 3;
            //两情相悦的
            getData(3);

        }
        slidePauseListView.setInterface(this);
        setada();
    }

    private void setada() {
        slideListAdapter = new SlideListAdapter(getActivity(), likePeopleBeans, this);
        slidePauseListView.setAdapter(slideListAdapter);

    }

    /**
     * 从服务段获取数据
     * @param type
     */
    private void getData(int type) {
        try {
            if (TextUtils.isEmpty(Utils.getUserId())) {
                return;
            }
            if (TextUtils.isEmpty(Utils.getUserToken())) {
                return;
            }
            String url = InternetConstant.SERVER_URL + InternetConstant.PEOPLE_LIST_URL + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();

            //{"type":2,"currentPage":1}   1:我中意的，2:中意我的，3:两情相悦的
            JSONStringer stringer = new JSONStringer().object()
                    .key("type").value(type)
                    .key("currentPage").value(1)
                    .key("pageSize").value(30)
                    .endObject();
            String jsonStr = stringer.toString();
            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1() {
                        @Override
                        public void call(Object o) {
                            Log.i("data",o.toString());
                            PeopleListBean peopleListBean = GsonTools.getPeopleListBean((String) o);

                            System.out.println((String) o);
                            if (peopleListBean.isSuccess()) {
                                likePeopleBeans.clear();
                                ArrayList<PeopleListItem> peopleListItems = peopleListBean.getParam().getList();
                                for (int i = 0; i < peopleListItems.size(); i++) {
                                    LikePeopleBean bean = new LikePeopleBean();
                                    PeopleListItem item = peopleListItems.get(i);
                                    bean.setFirstName(item.getFirstName());
                                    bean.setHeadPic(item.getHeadPic());
                                    bean.setAge(item.getAge());
                                    bean.setHeight(item.getHeight());
                                    bean.setSex(item.getSex());
                                    bean.setUid(item.getUid());
                                    bean.setCity(item.getCity());
                                    bean.setType(item.getType());
                                    bean.setTy(ty);
                                    likePeopleBeans.add(bean);
                                }
                                currentPage = 2;
                                slideListAdapter.notifyDataSetChanged();

                            } else {
                                DialogUtils.setDialog(context, peopleListBean.getError());
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            Log.e(TAG, throwable.getMessage());
                       }
                  });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    /**
     * 上拉加载
     * @param type
     */
    private void getDataFoot(int type) {
        //setGetData();
        try {
            if (TextUtils.isEmpty(Utils.getUserId())) {
                return;
            }
            if (TextUtils.isEmpty(Utils.getUserToken())) {
                return;
            }
            String url = InternetConstant.SERVER_URL + InternetConstant.PEOPLE_LIST_URL + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();

            //{"type":2,"currentPage":1}   1:我中意的，2:中意我的，3:两情相悦的
            JSONStringer stringer = new JSONStringer().object()
                    .key("type").value(type)
                    .key("currentPage").value(currentPage)
                    .key("pageSize").value(30)
                    .endObject();
            String jsonStr = stringer.toString();
            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1() {
                        @Override
                        public void call(Object o) {
                            PeopleListBean peopleListBean = GsonTools.getPeopleListBean((String) o);
                            ArrayList<PeopleListItem> peopleListItems = peopleListBean.getParam().getList();

                            System.out.println((String) o);
                            if (peopleListBean.isSuccess()) {
                                if (peopleListItems.size() != 0) {
                                    for (int i = 0; i < peopleListItems.size(); i++) {
                                        LikePeopleBean bean = new LikePeopleBean();
                                        PeopleListItem item = peopleListItems.get(i);
                                        bean.setFirstName(item.getFirstName());
                                        bean.setHeadPic(item.getHeadPic());
                                        bean.setAge(item.getAge());
                                        bean.setHeight(item.getHeight());
                                        bean.setSex(item.getSex());
                                        bean.setUid(item.getUid());
                                        bean.setCity(item.getCity());
                                        bean.setType(item.getType());
                                        bean.setTy(ty);
                                        likePeopleBeans.add(bean);
                                    }
                                    currentPage++;
                                    slideListAdapter.notifyDataSetChanged();

                                } else {
                                    Toast.makeText(context, "无更多内容", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                DialogUtils.setDialog(context, peopleListBean.getError());
                            }
                            slidePauseListView.LoadComplet();
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            Log.e(TAG, throwable.getMessage());
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * 联系红娘
     *
     * @param position
     * @param item
     */
    @Override
    public void onFirstClick(final int position, View item) {
        try {
            //获取用户的uid,和token
            if (TextUtils.isEmpty(Utils.getUserId())) {
                return;
            }
            if (TextUtils.isEmpty(Utils.getUserToken())) {
                return;
            }
            String url = InternetConstant.SERVER_URL + InternetConstant.MATCHMAKER_DATE + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();
            JSONStringer jsonStringer = new JSONStringer().object()
                    .key("targetUid").value(likePeopleBeans.get(position).getUid()).endObject();
            String jsonStr = jsonStringer.toString();
            //得到Observable并获取返回的数据(主线程中)
            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            MatchMakerBean matchmakerdateJson = GsonTools.getMatchMakerDateJson(s);
                            if (matchmakerdateJson.isSuccess()) {

                                Utils.setDialog(context, likePeopleBeans.get(position).getUid(), matchmakerdateJson.getParam().getRestChance());
                            } else {

                                Utils.confirmMatchMakerDialog2(context);
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

    /**
     * 取消关注
     * @param position
     * @param item
     */
    @Override
    public void onThirdClick(int position, View item) {
        sq = new ChatSQliteDataUtil(context);
        try {
            if (TextUtils.isEmpty(Utils.getUserId())) {
                return;
            }
            if (TextUtils.isEmpty(Utils.getUserToken())) {
                return;
            }
            String url = InternetConstant.SERVER_URL + InternetConstant.PEOPLE_UNDOFOLLOW + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();
            JSONStringer jsonStringer = new JSONStringer().object().key("targetUid").value(likePeopleBeans.get(position).getUid()).endObject();
            otherUserId=likePeopleBeans.get(position).getUid();
            String jsonStr = jsonStringer.toString();
            //得到Observable并获取返回的数据(主线程中)
            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            FollowBean followBean = GsonTools.getFollowJson(s);
                            if (followBean.isSuccess()) {
                                Toast.makeText(getActivity(), "取消喜欢成功", Toast.LENGTH_SHORT).show();
                                getData(ty);
                                setada();
                                setData();
                            } else {
                                Toast.makeText(getActivity(), followBean.getError(), Toast.LENGTH_SHORT).show();
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
        ChatMessageBean bean=sq.getDataByUserid(String.valueOf(likePeopleBeans.get(position).getUid()));
        deleteFromService(position,String.valueOf(likePeopleBeans.get(position).getUid()),bean.getConversationId());
    }


    private void deleteFromService(final int position,String s,String conversationId) {
        try {
            //获取用户的uid,和token
            if (TextUtils.isEmpty(Utils.getUserId())) {
                return;
            }
            if (TextUtils.isEmpty(Utils.getUserToken())) {
                return;
            }

            String url = InternetConstant.SERVER_URL + InternetConstant.DELETERFROMSERVER + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();
            JSONStringer jsonStringer = new JSONStringer().object()
                    .key("uid").value(Long.parseLong(Utils.getUserId()))
                    .key("conversation").value(conversationId)
                    .endObject();
            String jsonStr = jsonStringer.toString();
            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            DeleteInfoBean deleteInfoBean= GsonTools.getDeleteBean(s);
                            if (deleteInfoBean.isSuccess()) {
                                //Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                            }else{
                                if (deleteInfoBean.getError().equals("NO-PARAMTER")) {
                                    //Toast.makeText(context, deleteInfoBean.getError(), Toast.LENGTH_SHORT).show();
                                }
                            }
                            sq.deleteUserById(String.valueOf(likePeopleBeans.get(position).getUid()));
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable error) {
                            sq.deleteUserById(String.valueOf(likePeopleBeans.get(position).getUid()));
                            Log.e("test:", error.getMessage());
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void setData() {

        final ChatManager chatManager = ChatManager.getInstance();
        Map attrs=new HashMap();
        attrs.put("isOpen", false);
        //TODO 需要review
        chatManager.fetchConversationWithUserId(String.valueOf(otherUserId),attrs,  new AVIMConversationCreatedCallback() {
            @Override
            public void done(AVIMConversation avimConversation, AVIMException e) {

            }



        });
    }

    /**
     * 点击头像进入他人页面
     * @param position
     * @param item
     */
    @Override
    public void onHeadPicClick(int position, View item) {
        onInOthersClick(position, 0);
    }

    /**
     * 点击进入他人页面
     * @param position
     * @param item
     */
    public void slideholdlayoutClick(int position, View item) {
        onInOthersClick(position,0);
    }

    private void onInOthersClick(int position,int condition) {
        if (MeiMengApplication.sharedPreferences.getBoolean(CommonConstants.MYSTATE, false) == false) {
           setAlertDialog_a();//资料未填写完成，提示完善资料
        } else {//资料填写完成
            Utils.getUerVerfiy();//重新网络获取审核状态
            switch (MeiMengApplication.sharedPreferences.getInt(CommonConstants.USER_VERFIY, 0)) {
                //0，用户待审核中
                case 0:
                    Utils.setPromptDialog(getActivity(), "您提交的资料正在审核状态。如有疑问，请联系美盟客服：400-8783-520");
                    break;
                case 1:
                    if (condition == 0) {//进入他人界面
                        Intent intent = new Intent(context, OthersSelfActivity.class);
                        intent.putExtra("targetUid", likePeopleBeans.get(position).getUid());
                        startActivity(intent);
                    }else{//聊天
                        MessageUtils.setLeanCloudSelfUid();
                        MessageUtils.setLeanCloudOtherUid(context, likePeopleBeans.get(position).getUid());
                    }
                    break;
                //2，未通过审核
                case 2:
                    Utils.setPromptDialog(getActivity(), "对不起，您暂不符合美盟会员要求，未通过审核。您可以关注美盟订阅号:meimeng88888，获取更多信息。");
                    break;
            }
        }


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

                Intent intent= new Intent(getActivity(),AuthIdentifyActivity.class);
                startActivity(intent);
                aldialog_a.dismiss();
            }
        });
        aldialog_a.show();
    }
    /**
     * 点击聊天
     * @param position
     * @param item
     */
    @Override
    public void slideconversationClick(int position, View item) {
        onInOthersClick(position, 1);
    }

    /**
     * 上来加载更多数据
     */
    @Override
    public void onLoad() {
        //获取更多数据
        getDataFoot(ty);

    }
}
