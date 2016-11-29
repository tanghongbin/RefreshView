package com.example.com.meimeng.util;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avoscloud.leanchatlib.activity.ChatActivity;
import com.avoscloud.leanchatlib.controller.ChatManager;
import com.example.com.meimeng.MeiMengApplication;
import com.example.com.meimeng.R;
import com.example.com.meimeng.activity.ChatRoomActivity;
import com.example.com.meimeng.bean.ProgressBean;
import com.example.com.meimeng.constants.CommonConstants;
import com.example.com.meimeng.gson.GsonTools;
import com.example.com.meimeng.gson.gsonbean.BaseBean;
import com.example.com.meimeng.gson.gsonbean.ChatAuthBean;
import com.example.com.meimeng.gson.gsonbean.ChatAuthCanchat;
import com.example.com.meimeng.net.InternetConstant;
import com.example.com.meimeng.net.InternetUtils;

import org.json.JSONStringer;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by 010 on 2015/7/31.
 */
public class MessageUtils {

    private static ChatSQliteDataUtil sq;
    /**
     * 聊天功能，把自己的uid传给leanCloud
     */
    public static void setLeanCloudSelfUid() {
        if (TextUtils.isEmpty(Utils.getUserId())) {
            return;
        }
        if (TextUtils.isEmpty(Utils.getUserToken())) {
            return;
        }
        String selfId = String.valueOf(Utils.getUserId());

        if (!TextUtils.isEmpty(selfId)) {
            ChatManager chatManager = ChatManager.getInstance();
            chatManager.setupDatabaseWithSelfId(selfId);
            chatManager.openClientWithSelfId(selfId, new AVIMClientCallback() {


                @Override
                public void done(AVIMClient avimClient, AVIMException e) {
                    if (e != null) {

                    }
                }
            });
        }
    }

    /**
     * 开始聊天
     *
     * @param context
     * @param uid     对方的id
     */
    public static void setLeanCloudOtherUid(final Context context, final long uid) {
        final String otherId = String.valueOf(uid);

        if (TextUtils.isEmpty(otherId) == true) {
            return;
        }
        try {
            if (TextUtils.isEmpty(Utils.getUserId())) {
                return;
            }
            if (TextUtils.isEmpty(Utils.getUserToken())) {
                return;
            }
            String url = InternetConstant.SERVER_URL + InternetConstant.AUTH_CHAT + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();
            JSONStringer jsonStringer = new JSONStringer().object().key("targetUid").value(uid).endObject();
            String jsonStr = jsonStringer.toString();
            //得到Observable并获取返回的数据(主线程中)
            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {

                        @Override
                        public void call(String s) {
                            //解析json数据，更新UI
                            Log.i("json",s);
                            ChatAuthBean chatAuthBean = GsonTools.getChatAuthBean(s);
                            if (chatAuthBean.isSuccess()) {
                                ChatAuthCanchat chatAuthCanchat = chatAuthBean.getParam();
                                if (chatAuthCanchat.getLevel() == null) {
                                    chatAuthCanchat.setLevel(-1);
                                }

                                SharedPreferences.Editor editor;
                                editor = MeiMengApplication.sharedPreferences.edit();
                                editor.putInt(CommonConstants.USER_LEVEL, chatAuthBean.getParam().getLevel()).commit();
                                if (chatAuthCanchat.getCanChat() == null) {
                                    chatAuthCanchat.setCanChat(false);
                                }
                                if (chatAuthCanchat.getCanChat() || chatAuthCanchat.getLevel() > 0) {
                                    final ChatManager chatManager = ChatManager.getInstance();
                                    Map attrs=new HashMap();
                                    attrs.put("isOpen", true);
                                    chatManager.fetchConversationWithUserId(otherId, attrs,new AVIMConversationCreatedCallback() {
                                        @Override
                                        public void done(AVIMConversation conversation, AVIMException e) {
                                            if (e != null) {
                                                //     Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                                                Toast.makeText(context, "当前网络不可用，请检查您的网络设置", Toast.LENGTH_SHORT).show();

                                                Log.i("qwe", e.getMessage());
                                            } else {
                                                Log.i("qwe", "NN_______________________12345");
                                                sq=new ChatSQliteDataUtil(context);
                                                sq.upData("userid", String.valueOf(uid), "isOpen", 1);
                                                chatManager.registerConversation(conversation);
                                                Intent intent = new Intent(context, ChatRoomActivity.class);
                                                intent.putExtra(ChatActivity.CONVID, conversation.getConversationId());
                                                intent.putExtra("otherid", String.valueOf(uid));
                                                intent.putExtra("type", "1");
                                                context.startActivity(intent);
                                                MeiMengApplication.isSound = 2;
                                            }
                                        }
                                    });
                                } else {
                                    if (chatAuthBean.getParam().getChatChance() == null) {
                                        Utils.buyOtherConvercationDialog(context);
                                    } else {

                                        View view = LayoutInflater.from(context).inflate(R.layout.item_of_other_dialog_1, null);
                                        TextView tel= (TextView) view.findViewById(R.id.other_telphone);
                                        TextView top= (TextView) view.findViewById(R.id.top);
                                        TextView cancle= (TextView) view.findViewById(R.id.item_of_other_cancle);
                                        TextView telphone= (TextView) view.findViewById(R.id.item_of_other_tel);

                                        final Dialog loadingDialog = new Dialog(context, R.style.loading_dialog); // 创建自定义样式dialog
                                        loadingDialog.setCancelable(false);
                                        loadingDialog.setContentView(view);

                                        ProgressBean bean = new ProgressBean();
                                        bean.setDialog(loadingDialog);
                                        tel.setVisibility(View.GONE);
                                        cancle.setText("取消");
                                        cancle.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                loadingDialog.dismiss();
                                            }
                                        });
                                        top.setText("你 还 剩 " + chatAuthBean.getParam().getChatChance() + " 次 聊 天 机 会，\n是 否 立 即 对 TA 开 通 聊 天？");
                                        telphone.setText("确定");
                                        telphone.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                try {
                                                    if (TextUtils.isEmpty(Utils.getUserId())) {
                                                        return;
                                                    }
                                                    if (TextUtils.isEmpty(Utils.getUserToken())) {
                                                        return;
                                                    }
                                                    String url = InternetConstant.SERVER_URL + InternetConstant.CHAT_CONFIRM + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();
                                                    JSONStringer stringer = new JSONStringer().object()
                                                            .key("targetUid").value(uid)
                                                            .endObject();
                                                    String jsonStr = stringer.toString();
                                                    Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
                                                    observable.observeOn(AndroidSchedulers.mainThread())
                                                            .subscribe(new Action1<String>() {
                                                                @Override
                                                                public void call(String s) {
                                                                    BaseBean basebean = GsonTools.getBaseReqBean(s);
                                                                    if (basebean.isSuccess() != false) {
                                                                        sq=new ChatSQliteDataUtil(context);
                                                                        sq.upData("userid", String.valueOf(uid), "isOpen", 1);
                                                                        Toast.makeText(context, "使用了一次聊天通道", Toast.LENGTH_SHORT).show();
                                                                        final ChatManager chatManager = ChatManager.getInstance();
                                                                        Map attrs = new HashMap();
                                                                        attrs.put("isOpen", true);
                                                                        chatManager.fetchConversationWithUserId(otherId, attrs, new AVIMConversationCreatedCallback() {
                                                                            @Override
                                                                            public void done(AVIMConversation conversation, AVIMException e) {
                                                                                if (e != null) {
                                                                                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                                                                                } else {
                                                                                    chatManager.registerConversation(conversation);
                                                                                    Intent intent = new Intent(context, ChatRoomActivity.class);
                                                                                    intent.putExtra(ChatActivity.CONVID, conversation.getConversationId());
                                                                                    intent.putExtra("otherid", String.valueOf(uid));
                                                                                    intent.putExtra("type", "1");
                                                                                    context.startActivity(intent);
                                                                                    MeiMengApplication.isSound = 2;
                                                                                }
                                                                            }
                                                                        });
                                                                    } else
                                                                        Toast.makeText(context, basebean.getError(), Toast.LENGTH_LONG).show();

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
                                                loadingDialog.dismiss();
                                            }
                                        });
                                        loadingDialog.show();
                                    }
                                }


                            } else {
                                if (chatAuthBean.getError().equals("未登录")) {
                                    DialogUtils.setDialog(context, chatAuthBean.getError());
                                } else {
                                    if (chatAuthBean.getError().equals("非好友") || chatAuthBean.getError().equals("没有喜欢过")) {
                                        //Utils.convercationtips(context);
                                        Toast.makeText(context,"非好友",Toast.LENGTH_LONG).show();
                                    }
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
    /**
     * 开始聊天
     *
     * @param context
     * @param uid     对方的id
     */
    public static void setOtherLeanCloudOtherUid(final Context context, final long uid) {
        final String otherId = String.valueOf(uid);

        if (TextUtils.isEmpty(otherId) == true) {
            return;
        }
        try {
            if (TextUtils.isEmpty(Utils.getUserId())) {
                return;
            }
            if (TextUtils.isEmpty(Utils.getUserToken())) {
                return;
            }
            String url = InternetConstant.SERVER_URL + InternetConstant.AUTH_CHAT + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();
            JSONStringer jsonStringer = new JSONStringer().object().key("targetUid").value(uid).endObject();
            String jsonStr = jsonStringer.toString();
            //得到Observable并获取返回的数据(主线程中)
            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            //解析json数据，更新UI
                            Log.i("json",s);
                            ChatAuthBean chatAuthBean = GsonTools.getChatAuthBean(s);
                            if (chatAuthBean.isSuccess()) {
                                ChatAuthCanchat chatAuthCanchat = chatAuthBean.getParam();
                                if (chatAuthCanchat.getLevel() == null) {
                                    chatAuthCanchat.setLevel(-1);
                                }

                                SharedPreferences.Editor editor;
                                editor = MeiMengApplication.sharedPreferences.edit();
                                editor.putInt(CommonConstants.USER_LEVEL, chatAuthBean.getParam().getLevel()).commit();
                                if (chatAuthCanchat.getCanChat() == null) {
                                    chatAuthCanchat.setCanChat(false);
                                }
                                if (chatAuthCanchat.getCanChat() || chatAuthCanchat.getLevel() > 0) {
                                    final ChatManager chatManager = ChatManager.getInstance();
                                    chatManager.fetchConversationWithUserId(otherId,null, new AVIMConversationCreatedCallback() {
                                        @Override
                                        public void done(AVIMConversation conversation, AVIMException e) {
                                            if (e != null) {
                                                //     Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                                                Toast.makeText(context, "当前网络不可用，请检查您的网络设置", Toast.LENGTH_SHORT).show();

                                                Log.i("qwe", e.getMessage());
                                            } else {
                                                Log.i("qwe", "NN_______________________12345");
                                                chatManager.registerConversation(conversation);
                                                Intent intent = new Intent(context, ChatRoomActivity.class);
                                                intent.putExtra(ChatActivity.CONVID, conversation.getConversationId());
                                                intent.putExtra("otherid", String.valueOf(uid));
                                                intent.putExtra("type", "1");
                                                context.startActivity(intent);
                                                MeiMengApplication.isSound = 2;
                                            }
                                        }
                                    });
                                } else {
                                    if (chatAuthBean.getParam().getChatChance() == null) {
                                        Utils.buyOtherConvercationDialog(context);
                                    } else {

                                        View view = LayoutInflater.from(context).inflate(R.layout.item_of_other_dialog_1, null);
                                        TextView tel= (TextView) view.findViewById(R.id.other_telphone);
                                        TextView top= (TextView) view.findViewById(R.id.top);
                                        TextView cancle= (TextView) view.findViewById(R.id.item_of_other_cancle);
                                        TextView telphone= (TextView) view.findViewById(R.id.item_of_other_tel);

                                        final Dialog loadingDialog = new Dialog(context, R.style.loading_dialog); // 创建自定义样式dialog
                                        loadingDialog.setCancelable(false);
                                        loadingDialog.setContentView(view);

                                        ProgressBean bean = new ProgressBean();
                                        bean.setDialog(loadingDialog);
                                        tel.setVisibility(View.GONE);
                                        cancle.setText("取消");
                                        cancle.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                loadingDialog.dismiss();
                                            }
                                        });
                                        top.setText("你 还 剩 "+chatAuthBean.getParam().getChatChance()+" 次 聊 天 机 会，\n是 否 立 即 对 TA 开 通 聊 天？");
                                        telphone.setText("确定");
                                        telphone.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                try {
                                                    if (TextUtils.isEmpty(Utils.getUserId())) {
                                                        return;
                                                    }
                                                    if (TextUtils.isEmpty(Utils.getUserToken())) {
                                                        return;
                                                    }
                                                    String url = InternetConstant.SERVER_URL + InternetConstant.CHAT_CONFIRM + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();
                                                    JSONStringer stringer = new JSONStringer().object()
                                                            .key("targetUid").value(uid)
                                                            .endObject();
                                                    String jsonStr = stringer.toString();
                                                    Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
                                                    observable.observeOn(AndroidSchedulers.mainThread())
                                                            .subscribe(new Action1<String>() {
                                                                @Override
                                                                public void call(String s) {
                                                                    BaseBean basebean = GsonTools.getBaseReqBean(s);
                                                                    if (basebean.isSuccess() != false) {
                                                                        sq=new ChatSQliteDataUtil(context);
                                                                        sq.upData("userid", String.valueOf(uid), "isOpen", 1);
                                                                        Toast.makeText(context, "使用了一次聊天通道", Toast.LENGTH_SHORT).show();
                                                                        final ChatManager chatManager = ChatManager.getInstance();
                                                                        Map attrs=new HashMap();
                                                                        attrs.put("isOpen", true);
                                                                        chatManager.fetchConversationWithUserId(otherId, attrs,new AVIMConversationCreatedCallback() {
                                                                            @Override
                                                                            public void done(AVIMConversation conversation, AVIMException e) {
                                                                                if (e != null) {
                                                                                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                                                                                } else {
                                                                                    chatManager.registerConversation(conversation);
                                                                                    Intent intent = new Intent(context, ChatRoomActivity.class);
                                                                                    intent.putExtra(ChatActivity.CONVID, conversation.getConversationId());
                                                                                    intent.putExtra("otherid", String.valueOf(uid));
                                                                                    intent.putExtra("type", "1");
                                                                                    context.startActivity(intent);
                                                                                    MeiMengApplication.isSound = 2;
                                                                                }
                                                                            }
                                                                        });
                                                                    } else
                                                                        Toast.makeText(context, basebean.getError(), Toast.LENGTH_LONG).show();

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
                                                loadingDialog.dismiss();
                                            }
                                        });
                                        loadingDialog.show();
                                    }
                                }

                            } else {
                                if (chatAuthBean.getError().equals("未登录")) {
                                    DialogUtils.setDialog(context, chatAuthBean.getError());
                                } else {
                                    if (chatAuthBean.getError().equals("非好友") || chatAuthBean.getError().equals("没有喜欢过")) {
                                        Utils.convercationtips(context);
                                    }
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

    public static void setLeanCloudMatchmakerUid(final Context context, final Long uid, final String name) {
        final String otherId = "hn" + String.valueOf(uid);

        final ChatManager chatManager = ChatManager.getInstance();
        chatManager.fetchConversationWithUserId(otherId,null, new AVIMConversationCreatedCallback() {
            @Override
            public void done(AVIMConversation conversation, AVIMException e) {
                if (e != null) {

                    Toast.makeText(context, "当前网络不可用，请检查您的网络设置", Toast.LENGTH_SHORT).show();
                    Log.i("qwe", e.getMessage());
                } else {
                    Log.i("qwe", "NN_______________________1234");
                    chatManager.registerConversation(conversation);
                    Intent intent = new Intent(context, ChatRoomActivity.class);
                    intent.putExtra(ChatActivity.CONVID, conversation.getConversationId());
                    intent.putExtra("otherid", String.valueOf(uid));
                    intent.putExtra("type", "2");
                    intent.putExtra("matchmakername", name);
                    context.startActivity(intent);
                    MeiMengApplication.isSound = 2;
                }

            }
        });

    }

}
