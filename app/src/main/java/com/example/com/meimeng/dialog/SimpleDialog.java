package com.example.com.meimeng.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import com.example.com.meimeng.R;
import com.example.com.meimeng.gson.GsonTools;
import com.example.com.meimeng.gson.gsonbean.UserBaseInfoBean;
import com.example.com.meimeng.net.InternetConstant;
import com.example.com.meimeng.net.InternetUtils;
import com.example.com.meimeng.util.DialogUtils;
import com.example.com.meimeng.util.Utils;

import org.json.JSONStringer;

import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by Administrator on 2015/12/19.
 */
public class SimpleDialog extends Dialog {


    //图片id
    private long picId;

    private Context context;

    public interface OnFinishListener{
        public void successListener();

        public void failureListener();

    }

    public long getPicId() {
        return picId;
    }

    public void setPicId(long picId) {
        this.picId = picId;
    }

    private OnFinishListener mOnFinishListener;

    public void setOnFinishListener(OnFinishListener onFinishListener){

        mOnFinishListener = onFinishListener;
    }
    private  Handler handler;
    public SimpleDialog(Handler savaHandler,Context context, int theme) {
        super(context, theme);
        this.context = context;
        this.handler=savaHandler;
    }

    public SimpleDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_dialog_layout);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.simple_dialog_cancel_text)
    protected void cancelDialog() {
        dismiss();
    }

    @OnClick(R.id.simple_dialog_sure_text)
    protected void sureDialogListener() {
        dismiss();

        final Dialog dialog = DialogUtils.createLoadingDialog(context, "正在删除...");
        dialog.show();
        try {
            if (TextUtils.isEmpty(Utils.getUserId())) {
                return;
            }
            if (TextUtils.isEmpty(Utils.getUserToken())) {
                return;
            }
            String url = InternetConstant.SERVER_URL + InternetConstant.DELETER + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();
            JSONStringer stringer = new JSONStringer().object()
                    .key("picId").value(picId)
                    .key("uid").value(Utils.getUserId())
                    .endObject();
            String jsonStr = stringer.toString();
            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1() {
                        @Override
                        public void call(Object o) {
                            if (null != dialog) {
                                dialog.dismiss();
                            }
                            UserBaseInfoBean baseInfoBean = GsonTools.getUserBaseInfoBean((String) o);
                            if (baseInfoBean.isSuccess()) {
                                mOnFinishListener.successListener();
                                handler.sendEmptyMessage(999);
                            }else {
                                mOnFinishListener.failureListener();
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
