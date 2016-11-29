package com.example.com.meimeng.fragment;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.example.com.meimeng.MeiMengApplication;
import com.example.com.meimeng.R;
import com.example.com.meimeng.activity.PayActivity;

/**
 * Created by Administrator on 2015/7/23.
 */
@SuppressLint("ValidFragment")
public class PayDialogFragment extends DialogFragment implements View.OnClickListener {

    private long mActivityId;
    private double mType;
    private String name;

    //支付完成返回记录
    private int aType = -1;

    //这里没错，是官方建议不要这样写
    @SuppressLint("ValidFragment")
    public PayDialogFragment(long activityId, double type, String name, int aType) {
        this.mActivityId = activityId;
        this.mType = type;
        this.name = name;
        this.aType = aType;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, 0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.payment_dialog, null);
        MeiMengApplication.currentContext=getActivity();
        ImageView cancel = (ImageView) view.findViewById(R.id.dialog_cancel_pay);
        Button cancelBtn = (Button) view.findViewById(R.id.dialog_cancle);
        Button sure = (Button) view.findViewById(R.id.dialog_sure);
        //TextView needToPay = (TextView) view.findViewById(R.id.dialog_price_text);
        cancel.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        sure.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialog_cancel_pay:
                break;
            case R.id.dialog_cancle:
                break;
            case R.id.dialog_sure:
                //支付页面
                MeiMengApplication.weixinPayCallBack = aType;
                Intent intent = new Intent(getActivity(), PayActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("price", "" + mType);
                intent.putExtra("goodId", -2l);
                intent.putExtra("targetUid", mActivityId);
                intent.putExtra("pay_type", 3);
                getActivity().startActivity(intent);
                dismiss();

                break;

        }
        dismiss();
    }


}
