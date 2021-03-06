package com.publishproject.core.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.publishproject.R;
import com.publishproject.core.compenent.BaseActivityCompenent;
import com.publishproject.core.compenent.DaggerBaseActivityCompenent;
import com.publishproject.core.module.BaseActivityModule;
import com.publishproject.events.ProgressDialogEvent;
import com.publishproject.core.common.loadingdialog.LoadingDialogUtil;
import com.publishproject.core.common.logger.LogUtil;
import com.publishproject.core.common.eventbus.BusHelper;
import com.publishproject.core.common.loadingdialog.LoadingDialogInterface;
import com.publishproject.core.views.HeadView;

import javax.inject.Inject;

import pushlish.tang.com.commonutils.others.KeyboardUtils;


/**
 * @author Android客户端组-tanghongbin
 * @Title: BaseActivity
 * @Package com.publishproject
 * @Description: 基类Activity
 * Copyright: Copyright (c) 2016
 * Company: 成都壹柒互动科技有限公司
 * @date 2016/12/8 14:31
 * @desc 基类，默认使用数据绑定进行解析，也可以不使用，
 * 1.注册和销毁事件库（Rxbus）;
 * 2.提供标题头操作（可直接利用headview.setCenterTItle(‘’)设置标题头）;
 * 3.进度加载框
 * 4.（可自定义加载框）;
 * 在initView()里面进行赋值初始化操作
 * loaddata（）中加载数据
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener{
    protected boolean isNeedBinding(){
        return true;
    }
    @Inject
    public LoadingDialogInterface dialogInterface;
    public abstract int setContentLayout();
    @Inject
    public HeadView headView;

    //自行进行数据加载或者布局初始化
    public abstract void initView();
    public abstract void loadData();
    @Inject
    public ProgressDialogEvent dialogEvent;

    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            setContentView(setContentLayout());
        }catch (Exception e){
            LogUtil.i("TAG","资源没有找到");
            e.printStackTrace();
        }

        BaseActivityCompenent component =
                DaggerBaseActivityCompenent.builder().baseActivityModule(new BaseActivityModule(this)).build();
        component.inject(this);

        headView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        KeyboardUtils.hideSoftInput(this);//如果有软键盘弹出，则隐藏
        BusHelper.registe(this);
        BusHelper.registe(dialogEvent);
        initView();
        loadData();
    }




    public void showDialog(){
        if(!isFinishing()){
            dialogInterface.showDialog();
        }
    }

    public void showDialog(String text){
        if(!isFinishing()) {
            dialogInterface.showDialog(text);
        }
    }

    public void dismissDialog(){
        dialogInterface.dismissXDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BusHelper.unRegiste(this);
        BusHelper.unRegiste(dialogEvent);
    }
    public void onClick(View view){

    }

}
