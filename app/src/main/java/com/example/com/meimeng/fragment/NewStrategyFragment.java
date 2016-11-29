package com.example.com.meimeng.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.com.meimeng.MeiMengApplication;
import com.example.com.meimeng.R;
import com.example.com.meimeng.custom.RoundCornerImageView;

/**
 * Created by shi-02 on 2015/4/27.
 */
public class NewStrategyFragment extends Fragment {

    private View view;

    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_explore_item, container, false);

        MeiMengApplication.currentContext=getActivity();
        initView();
        return view;
    }

    private void initView() {
        context = getActivity();
        RoundCornerImageView img = (RoundCornerImageView) view.findViewById(R.id.img);
        RoundCornerImageView img_b = (RoundCornerImageView) view.findViewById(R.id.img_b);

        img.setAngie(10,10);
        img_b.setAngie(10,10);

    }
}
