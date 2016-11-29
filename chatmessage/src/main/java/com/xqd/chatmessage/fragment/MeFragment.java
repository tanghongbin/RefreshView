package com.xqd.chatmessage.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xqd.chatmessage.ChatAplication;
import com.xqd.chatmessage.R;
import com.xqd.chatmessage.activity.LoginActivity;
import com.xqd.chatmessage.util.ActivityCollector;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by Administrator on 2015/9/9.
 */
public class MeFragment extends Fragment {
    private Context context;

    @Bind(R.id.fragment_me_number)
    TextView fragmentMeNumber;

    @Bind(R.id.fragment_me_name)
    TextView fragmentMeName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AllFragmentManagement.fragmentList.add(this);

    }

    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        ButterKnife.bind(this, view);

        fragmentMeNumber.setText(ChatAplication.tel);
        fragmentMeName.setText(ChatAplication.name);
        return view;
    }

    @OnClick(R.id.fragment_me_exitLayout)
    void exitLiatener() {
        ActivityCollector.finishAll();
        Intent intent = new Intent(context, LoginActivity.class);
        startActivity(intent);
    }
}
