package com.example.com.meimeng.activity;

import android.os.Bundle;
import android.view.View;

import com.example.com.meimeng.MeiMengApplication;
import com.example.com.meimeng.R;

/**
 * Created by Administrator on 2015/11/23.
 */
public class ExploreScreenActivity extends BaseActivity implements View.OnClickListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_explore_screen);
        MeiMengApplication.currentContext=this;
        initTitleBar("搜索",R.drawable.icon_nav_back,-1,this);

    }

    @Override
    public void onClick(View v) {

    }
}
