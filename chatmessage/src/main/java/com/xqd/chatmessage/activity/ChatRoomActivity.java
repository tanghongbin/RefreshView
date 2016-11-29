package com.xqd.chatmessage.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.avoscloud.leanchatlib.activity.ChatActivity;
import com.xqd.chatmessage.R;
import com.xqd.chatmessage.fragment.MessageFragment;
import com.xqd.chatmessage.gson.ChatMessageBean;
import com.xqd.chatmessage.gson.GsonTools;
import com.xqd.chatmessage.gson.SearchBean;
import com.xqd.chatmessage.net.InternetConstant;
import com.xqd.chatmessage.net.InternetUtils;
import com.xqd.chatmessage.util.ChatSQliteDataUtil;
import com.xqd.chatmessage.util.Utils;

import org.json.JSONStringer;

import java.util.ArrayList;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;


/**
 * Created by lzw on 15/4/27.
 */
public class ChatRoomActivity extends ChatActivity {

    //利用intent传两个个id进来，通过id设置各种数值

    private ChatSQliteDataUtil sq = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sq = new ChatSQliteDataUtil();
        Intent intent = getIntent();
        String otherid = intent.getStringExtra("otherid");
        adapter.setOtherid(otherid);
        getOtherNickname(otherid);

//        addLocationBtn.setVisibility(View.VISIBLE);
    }

    private void getOtherNickname(final String uid) {

        try {
            //获取用户的uid,和token
            if (TextUtils.isEmpty( Utils.getUserId() )) {
                return;
            }
            if (TextUtils.isEmpty( Utils.getUserToken() )) {
                return;
            }
            String targetUid = String.valueOf(uid);


            String url = InternetConstant.SERVER_URL + InternetConstant.SEARCH_URL + "?uid=" +Utils.getUserId() + "&token=" + Utils.getUserToken() ;
            JSONStringer stringer = new JSONStringer().object().key("targetUid").value(targetUid).endObject();
            String jsonStr = stringer.toString();
            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1() {
                        @Override
                        public void call(Object o) {
                            SearchBean searchBean = GsonTools.getSearchBean((String) o);
                            if (searchBean.isSuccess()) {

                                String name = searchBean.getParam().getUser().getNickname();
                                Long HeadPic = searchBean.getParam().getUser().getHeadPic();

//                                setOthersNameText(searchBean.getParam().getUser().getNickname());
//
//                                ChatRoomActivity.this.adapter.setOtherPic(InternetUtils.setPictureUrl(searchBean.getParam().getUser().getHeadPic()));
                                if(!(name == null)) {
                                    setOthersNameText(name);
                                }else {
                                    setOthersNameText("");
                                }
                                if(!(HeadPic == null)){
                                    ChatRoomActivity.this.adapter.setOtherPic(InternetUtils.setPictureUrl(HeadPic));
                                }

                                sq.upData("userid",uid,"bitmap",HeadPic+"");
                                sq.upData("userid",uid,"name",name);

                                ArrayList<ChatMessageBean> list = new ArrayList<ChatMessageBean>();
                                for (int i = 0; i < sq.getDataAll().size(); i++) {
                                    ChatMessageBean bean = sq.getDataAll().get(i);
                                    Log.i("qwe",uid +"  :  "+bean.getUserId() + "");
                                    if (uid.equals(bean.getUserId() + "")  ) {
                                        Log.i("qwe","1");
                                        bean.setName(name);
                                        bean.setHeadPic(HeadPic);
                                    }
                                    if(bean.getContent() !=null && !bean.getContent().equals("")){
                                        list.add(bean);
                                    }
                                }
                                HomeActivity.dataList = list;
                                if(MessageFragment.chatMessageAdapter != null){
                                    MessageFragment.chatMessageAdapter.setDataList(HomeActivity.dataList);
                                    MessageFragment.chatMessageAdapter.notifyDataSetChanged();
                                }
                                adapter.notifyDataSetChanged();

                            } else {
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            Log.e("获取用户基本信息失败了", throwable.getMessage());
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void onClickHeader(String uid) {

    }
}
