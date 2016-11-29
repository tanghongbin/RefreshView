package com.xqd.chatmessage.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xqd.chatmessage.R;
import com.xqd.chatmessage.adapter.friendsAdapter;
import com.xqd.chatmessage.gson.ChatMessageBean;
import com.xqd.chatmessage.gson.GsonTools;
import com.xqd.chatmessage.gson.UserListBean;
import com.xqd.chatmessage.gson.UserListItem;
import com.xqd.chatmessage.gson.UserPeopleBean;
import com.xqd.chatmessage.net.InternetConstant;
import com.xqd.chatmessage.net.InternetUtils;
import com.xqd.chatmessage.util.ChatSQliteDataUtil;
import com.xqd.chatmessage.util.MessageUtils;
import com.xqd.chatmessage.util.Utils;

import org.json.JSONStringer;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by Administrator on 2015/9/9.
 */
public class Friendsfragment extends Fragment {
    @Bind(R.id.fragment_friends_listview)
    PullToRefreshListView fragmentFriendsListview;

    private friendsAdapter friendsadapter;
    private volatile ArrayList<UserPeopleBean> userPeopleBeans = new ArrayList<>();
    private Context context;
    private static int currentPage = 1;
    private ChatSQliteDataUtil sq;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AllFragmentManagement.fragmentList.add(this);

    }

    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        ButterKnife.bind(this, view);
        context = getActivity();
       sq = new ChatSQliteDataUtil();
        friendsadapter = new friendsAdapter(getActivity(), userPeopleBeans);
        fragmentFriendsListview.setAdapter(friendsadapter);
        getData();
        fragmentFriendsListview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //设置下拉头的时间
                String time_label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("更新于：" + time_label);
                userPeopleBeans.clear();
                getData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                refreshView.getLoadingLayoutProxy().setPullLabel("加载更多...");
                initFoot();
            }
        });
        fragmentFriendsListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                long uid = userPeopleBeans.get(position -1).getUid();

                MessageUtils.setLeanCloudOtherUid(context, uid);
            }
        });
        return view;
    }

    private void getData() {
        try {
            if (TextUtils.isEmpty(Utils.getUserId())) {
                return;
            }
            if (TextUtils.isEmpty(Utils.getUserToken())) {
                return;
            }
            String url = InternetConstant.SERVER_URL + InternetConstant.PEOPLE_LIST_URL + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();
            JSONStringer stringer = new JSONStringer().object()
                    .key("currentPage").value(1)
                    .key("pageSize").value(10)
                    .endObject();
            String jsonStr = stringer.toString();
            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1() {
                        @Override
                        public void call(Object o) {
                            UserListBean userListBean = GsonTools.getUserListBean((String) o);

                            if (userListBean.isSuccess()) {
                                userPeopleBeans.clear();
                                ArrayList<UserListItem> userListItems = userListBean.getParam().getLst();
                                if (userListItems != null) {
                                    for (int i = 0; i < userListItems.size(); i++) {
                                        UserPeopleBean bean = new UserPeopleBean();
                                        UserListItem item = userListItems.get(i);
                                        bean.setName(item.getNickname());
                                        bean.setHeadId(item.getHeadPic());
                                        bean.setUid(item.getUid());
                                        bean.setSex(item.getSex());
                                        bean.setTel(item.getTel());

                                        addData(bean);
                                        userPeopleBeans.add(bean);
                                    }
                                }
                                currentPage = 2;
                                friendsadapter.notifyDataSetChanged();

                            } else {
                                Toast.makeText(context, userListBean.getError(), Toast.LENGTH_LONG).show();
                            }
                            fragmentFriendsListview.onRefreshComplete();
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            Log.e(">>", throwable.getMessage());
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initFoot() {
        try {
            if (TextUtils.isEmpty(Utils.getUserId())) {
                return;
            }
            if (TextUtils.isEmpty(Utils.getUserToken())) {
                return;
            }
            String url = InternetConstant.SERVER_URL + InternetConstant.PEOPLE_LIST_URL + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();
            JSONStringer stringer = new JSONStringer().object()
                    .key("currentPage").value(currentPage)
                    .key("pageSize").value(10)
                    .endObject();
            String jsonStr = stringer.toString();
            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1() {
                        @Override
                        public void call(Object o) {
                            UserListBean userListBean = GsonTools.getUserListBean((String) o);
                            ArrayList<UserListItem> userListItems = userListBean.getParam().getLst();

                            if (userListBean.isSuccess() & (userListItems.size() != 0)) {
                                for (int i = 0; i < userListItems.size(); i++) {
                                    UserPeopleBean bean = new UserPeopleBean();
                                    UserListItem item = userListItems.get(i);
                                    bean.setName(item.getNickname());
                                    bean.setHeadId(item.getHeadPic());
                                    bean.setUid(item.getUid());
                                    bean.setSex(item.getSex());
                                    bean.setTel(item.getTel());
                                    addData(bean);
                                    userPeopleBeans.add(bean);
                                }
                                currentPage++;
                                friendsadapter.notifyDataSetChanged();

                            } else if (!userListBean.isSuccess()) {
                                Toast.makeText(context, userListBean.getError(), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getActivity(), "无更多内容", Toast.LENGTH_SHORT).show();
                            }
                            fragmentFriendsListview.onRefreshComplete();
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            Log.e(">>", throwable.getMessage());
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addData(UserPeopleBean bean){
        long uid = bean.getUid();
        Boolean flag = false;
        for(int i = 0;i < sq.getDataAll().size(); i++){
            ChatMessageBean cbean  = sq.getDataAll().get(i);
            if(uid == cbean.getUserId()){
                flag = true;
            }
            Log.i("msg",flag+"");
        }

        if(flag == false){
            ChatMessageBean cbean = new ChatMessageBean();
            String name = bean.getName();
            Long Uid = bean.getUid();
            Long HeadId = bean.getHeadId();
            if(bean.getName() == null){
                name = "";
            }
            if(Uid == null){
               Uid = Long.valueOf(-1);
            }
            if(HeadId == null){
                HeadId = Long.valueOf(-1);
            }
            cbean.setName(name);
            cbean.setUserId(Uid);
            cbean.setHeadPic(HeadId);
            cbean.setTime("");
            cbean.setContent("");
            sq.addData(cbean);
            for(int i = 0;i < sq.getDataAll().size(); i++){
                Log.i("msg",sq.getDataAll().get(i).toString());
            }

        }
    }


}
