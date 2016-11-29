package com.example.com.meimeng.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.com.meimeng.R;
import com.example.com.meimeng.activity.OthersSelfActivity;
import com.example.com.meimeng.custom.CircleImageView;
import com.example.com.meimeng.gson.GsonTools;
import com.example.com.meimeng.gson.gsonbean.ApplyUserItem;
import com.example.com.meimeng.gson.gsonbean.InviteBean;
import com.example.com.meimeng.net.InternetConstant;
import com.example.com.meimeng.net.InternetUtils;
import com.example.com.meimeng.util.DialogUtils;
import com.example.com.meimeng.util.Utils;

import org.json.JSONStringer;

import java.util.ArrayList;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * 已报名用户list的Adapter
 */
public class ApplyListAdapter extends ArrayAdapter implements View.OnClickListener {

    /**
     * 未邀请用户
     */
    private static final int UNINVITED = 1;
    /**
     * 已邀请用户
     */
    private static final int INVITED = 2;

    private int mresource;
    private Context mContext;
    private ArrayList<ApplyUserItem> datalist;
    private long activityId;
    private long inviteUid;
    private Dialog dialog;
    private int aState;

    public ApplyListAdapter(Context context, int resource, ArrayList<ApplyUserItem> datalist, long activityId, int aState) {
        super(context, resource, datalist);
        this.mresource = resource;
        this.mContext = context;
        this.datalist = datalist;
        this.activityId = activityId;
        this.aState = aState;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ApplyUserItem data = datalist.get(position);
        inviteUid = data.getUid();//需要传进去id
        View view;
        final ViewHold viewhold;
        if (convertView == null) {
            view = LayoutInflater.from(mContext).inflate(mresource, null);
            viewhold = new ViewHold();
            viewhold.headpic = (CircleImageView) view.findViewById(R.id.imageview_round_head);
            viewhold.nickname = (TextView) view.findViewById(R.id.nickname_textView);
            viewhold.state = (ImageView) view.findViewById(R.id.invite_imageview);
            view.setTag(viewhold);
        } else {
            view = convertView;
            viewhold = (ViewHold) view.getTag();
        }
        viewhold.headpic.setTag(position);
        viewhold.headpic.setOnClickListener(this);
        viewhold.nickname.setTag(position);
        viewhold.nickname.setOnClickListener(this);
        viewhold.nickname.setText(data.getNickname());
        viewhold.state.setTag(position);
        InternetUtils.getPicIntoView(200, 200, viewhold.headpic, data.getHeadPic(), position, true);
        if(aState==1){
            viewhold.state.setImageResource(R.drawable.me_event_btn_invite);
        }else{
            setstate(viewhold.state, data.getState());
        }

        viewhold.state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getJson(inviteUid, activityId, viewhold.state);
            }
        });

        return view;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nickname_textView:
                gotoOthersSelfActivity(v.getTag());
                break;
            case R.id.imageview_round_head:
                gotoOthersSelfActivity(v.getTag());
                break;
            default:
                break;

        }


    }

    private void gotoOthersSelfActivity(Object object) {
        Intent intent = new Intent(mContext, OthersSelfActivity.class);
        intent.putExtra("targetUid", datalist.get((int) object).getUid());
        mContext.startActivity(intent);

    }

    /**
     * 获得“邀请用户”后的返回结果的
     *
     * @param inviteUid  其它用户的id 一个活动只能邀请一个人参加
     * @param activityId 活动id
     */

    private void getJson(final long inviteUid, final long activityId, final ImageView stateImageView) {
        dialog = DialogUtils.createLoadingDialog(mContext);
        dialog.show();
        try {
            if (TextUtils.isEmpty(Utils.getUserId())) {
                return;
            }
            if (TextUtils.isEmpty( Utils.getUserToken() )) {
                return;
            }
            //构造url和json格式的请求参数
            String url = InternetConstant.SERVER_URL + InternetConstant.USERACTIVITY_INVITE + "?uid=" + Utils.getUserId() + "&token=" +  Utils.getUserToken();
            JSONStringer jsonStringer = new JSONStringer().object().key("inviteUid").value(datalist.get((int) (stateImageView.getTag())).getUid()).key("activityId").value(activityId).endObject();
            String jsonStr = jsonStringer.toString();
            //得到Observable并获取返回的数据(主线程中)
            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            if (null != dialog)
                                dialog.dismiss();
                            //解析json数据，更新UI
                            Log.e("邀请用户", s);
                            InviteBean inviteBean = GsonTools.getInviteBean(s);
                            if (inviteBean.isSuccess()) {
                                Toast.makeText(mContext, "邀请成功！", Toast.LENGTH_SHORT).show();
                                stateImageView.setImageResource(R.drawable.me_event_btn_invited);
                            } else {
                                if (inviteBean.getError().equals("EA001")) {
                                    Toast.makeText(mContext, "活动已经在进行中!", Toast.LENGTH_SHORT).show();
                                } else if (inviteBean.getError().equals("EA002")) {
                                    Toast.makeText(mContext, "活动已经结束!", Toast.LENGTH_SHORT).show();
                                } else {
                                    if (inviteBean.getError().equals("未登录")) {
                                        DialogUtils.setDialog(getContext(), inviteBean.getError());
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
        } catch (
                Exception e
                )

        {
            e.printStackTrace();
        }
    }


    private void setstate(ImageView imageView, int state) {
        switch (state) {
            case UNINVITED:
                imageView.setImageResource(R.drawable.me_event_btn_invite_unclick);
                break;
            case INVITED:
                imageView.setImageResource(R.drawable.me_event_btn_invited);
                break;
            default:
                break;
        }

    }

    class ViewHold {
        private CircleImageView headpic;
        private TextView nickname;
        private ImageView state;
    }

}
