package com.example.com.meimeng.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.com.meimeng.MeiMengApplication;
import com.example.com.meimeng.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 010 on 2015/7/18.
 */
public class ShareDialogFragment extends Fragment {

    @Bind(R.id.share_cancel_button)
    Button cancelButton;

    View view;

    private OnDialogListener dialogListener;

    public void setOnDialogListener(OnDialogListener listener) {
        dialogListener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.share_dialog, container, false);
        ButterKnife.bind(this, view);

        MeiMengApplication.currentContext=getActivity();
        return view;
    }

    @OnClick(R.id.share_cancel_button)
    void cancelBUttonListener() {
        dialogListener.onCancelListener();
    }

    @OnClick(R.id.share_weixin)
    public void shareweixinListener(){
        ((OnDialogListener)getActivity()).shareListener(1);
    }

    @OnClick(R.id.share_friends_circle_layout)
    public void sharefriendsListener(){
        ((OnDialogListener)getActivity()).shareListener(2);
    }

    @OnClick(R.id.share_qq_layout)
    public void shareqqListener(){
        ((OnDialogListener)getActivity()).shareListener(3);
    }
    @OnClick(R.id.share_email_layout)
    public void sharemailListener(){
        ((OnDialogListener)getActivity()).shareListener(4);
    }

    @OnClick(R.id.share_message_layout)
    public void sharemessageListener(){
        ((OnDialogListener)getActivity()).shareListener(5);
    }

    @OnClick(R.id.share_copy_layout)
    public void copyListener(){
        ((OnDialogListener)getActivity()).shareListener(6);
    }

    public interface OnDialogListener {
        public void onCancelListener();

        public void shareListener(int type);
    }
}
