package com.example.com.meimeng.activity;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.com.meimeng.MeiMengApplication;
import com.example.com.meimeng.R;
import com.example.com.meimeng.adapter.GiftListAdapter;
import com.example.com.meimeng.fragment.GiveGiftFragment;
import com.example.com.meimeng.fragment.IsMyGiftFragment;
import com.example.com.meimeng.fragment.IsTomeGiftFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lx on 2015/8/5.
 */
public class GitManagerActivity extends BaseActivity implements GiveGiftFragment.OnGiftListener, GiftListAdapter.GetTargetUid {

    @Bind(R.id.title_text)
    TextView titleText;

    @Bind(R.id.title_left_arrow_image_view)
    ImageView leftArrowImageView;//返回图标

    @Bind(R.id.bow_arrow_image_view)
    ImageView bow_arrow_image_view;

    @Bind(R.id.main_dialog_layout)
    LinearLayout dialogLayout;

    private TextView gift_my_btn;
    private TextView gift_tome_btn;


    private GiveGiftFragment giveGiftFragment;
    private long targetUid;


    //用来记录是否有dialog
    private boolean hasdialog = false;


    private IsMyGiftFragment isMyGiftFragment;
    private IsTomeGiftFragment isTomeGiftFragment;
    private boolean addIsMyGiftOk = false;
    private boolean addIsTomeGiftOk = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.git_manager);
        ButterKnife.bind(this);

        MeiMengApplication.currentContext=this;
        //初始化界面
        initview();
        //获取我的礼物的JSON数据
        isFirst();

    }

    private void isFirst() {
        if (isTomeGiftFragment == null) {
            isTomeGiftFragment = new IsTomeGiftFragment();
            addIsTomeGiftOk = true;
        } else {
            addIsTomeGiftOk = false;
        }
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.fragment, isTomeGiftFragment);
        transaction.commit();
    }

    /**
     * 初始化界面
     */
    private void initview() {

        titleText.setText("我的礼物");
        leftArrowImageView.setVisibility(View.VISIBLE);
        bow_arrow_image_view.setVisibility(View.INVISIBLE);

        gift_my_btn = (TextView) findViewById(R.id.gift_my_btn);
        gift_tome_btn = (TextView) findViewById(R.id.gift_tome_btn);
    }

    @OnClick(R.id.gift_my_btn)
    void isMyGift() {
        initGiftText();
        gift_my_btn.setTextColor(this.getResources().getColor(R.color.gify_btn_text_color_pressed));

        if (isMyGiftFragment == null) {
            isMyGiftFragment = new IsMyGiftFragment();
            addIsMyGiftOk = true;
        } else {
            addIsMyGiftOk = false;
        }

        replaceFragment(isMyGiftFragment);

    }

    @OnClick(R.id.gift_tome_btn)
    void isTomeGift() {
        initGiftText();
        gift_tome_btn.setTextColor(this.getResources().getColor(R.color.gify_btn_text_color_pressed));

        if (isTomeGiftFragment == null) {
            isTomeGiftFragment = new IsTomeGiftFragment();
            addIsTomeGiftOk = true;
        } else {
            addIsTomeGiftOk = false;
        }
        replaceFragment(isTomeGiftFragment);
//        AllFragmentManagement.ChangeFragment(1, this, R.id.fragment, isTomeGiftFragment, addIsTomeGiftOk);


    }

    private void initGiftText() {
        gift_my_btn.setTextColor(this.getResources().getColor(R.color.gify_btn_text_color_normal));
        gift_tome_btn.setTextColor(this.getResources().getColor(R.color.gify_btn_text_color_normal));
    }

    @OnClick(R.id.dialog_button)
    protected void cancelGiveGiftFragment(){
        cancelGiftDialog();
    }

    /**
     * 回赠礼物
     */
    private void addGiftDialogLayout() {

        dialogLayout.setVisibility(View.VISIBLE);

        if (giveGiftFragment == null){
            giveGiftFragment = new GiveGiftFragment();
        }

        hasdialog = true;

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.anim.bottom_in, 0);
        transaction.add(R.id.dialog_layout, giveGiftFragment);
        transaction.commit();
    }

    /**
     * 增送礼物
     */
    @Override
    public void giveListener(int gitPicId, long goodId, String name, String price) {

        MeiMengApplication.weixinPayCallBack = 1;
        Intent intent = new Intent(GitManagerActivity.this, PayActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("price", price);
        intent.putExtra("goodId", goodId);
        intent.putExtra("targetUid", targetUid);
        intent.putExtra("pay_type", 2);
        startActivity(intent);
        cancelGiftDialog();

    }

    /**
     * 取消弹出框
     */
    private void cancelGiftDialog() {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.remove(giveGiftFragment);
        transaction.commit();
        dialogLayout.setVisibility(View.GONE);
        hasdialog = false;
    }

    @OnClick(R.id.title_left_arrow_layout)
    void back() {
        onBackPressed();
    }

    /**
     * 获得对方的ID
     *
     * @param uid
     */
    @Override
    public void getTargetUid(long uid) {
        this.targetUid = uid;
        addGiftDialogLayout();
    }

    /**
     * 返回
     */
    @Override
    public void onBackPressed() {

        if (hasdialog) {
            cancelGiftDialog();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 替换fragment
     * @param newFragment
     */
    private void replaceFragment(Fragment newFragment){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment,newFragment);
        transaction.commit();
    }


}
