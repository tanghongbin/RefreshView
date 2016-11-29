package com.example.com.meimeng.dialog;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.com.meimeng.R;
import com.example.com.meimeng.gson.GsonTools;
import com.example.com.meimeng.gson.gsonbean.BaseBean;
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
 * Created by Administrator on 2015/12/18.
 */
public class AlbumUnLockDialog extends Dialog {

    private Context mContext;

    public AlbumUnLockDialog(Context context){
        super(context);
        mContext = context;
    }



    public interface OnFinishUnlock{
        public void unlock(int num);
    }

    private OnFinishUnlock mOnFinishUnlock;

    public void setOnFinishUnlock(OnFinishUnlock onFinishUnlock){
        mOnFinishUnlock = onFinishUnlock;
    }
    public AlbumUnLockDialog(Context context,int style){
        super(context, style);
        mContext = context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_unlock_layout);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.cancel_text)
    protected void cancelTextListener(){
        dismiss();
    }

    @OnClick(R.id.unlock_text)
    protected void unlockTextListener(){
        try {
            if (TextUtils.isEmpty(Utils.getUserId())) {
                return;
            }
            if (TextUtils.isEmpty(Utils.getUserToken())) {
                return;
            }

            String url = InternetConstant.SERVER_URL + InternetConstant.UNLOCK_ALBUM + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();

            //解锁
            int isLock = 0;
            JSONStringer jsonStringer = new JSONStringer().object()
                    .key("photoLock").value(isLock)
                    .endObject();
            String jsonStr = jsonStringer.toString();

            //得到Observable并获取返回的数据(主线程中)
            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {

                            BaseBean baseBeanJson = GsonTools.getBaseReqBean(s);
                            if (baseBeanJson.isSuccess() != false) {
                                Toast.makeText(mContext, "解锁成功", Toast.LENGTH_SHORT).show();
                                mOnFinishUnlock.unlock(0);
                            } else {
                                Toast.makeText(mContext, "解锁失败", Toast.LENGTH_SHORT).show();
                                mOnFinishUnlock.unlock(1);
                            }
                            dismiss();
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
