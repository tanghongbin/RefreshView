package com.example.com.meimeng.activity.PhotoDisplay;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.com.meimeng.R;
import com.example.com.meimeng.activity.BaseActivity;
import com.example.com.meimeng.adapter.ViewsPagerAdapter;
import com.example.com.meimeng.gson.GsonTools;
import com.example.com.meimeng.gson.gsonbean.UserBaseInfoBean;
import com.example.com.meimeng.net.InternetConstant;
import com.example.com.meimeng.net.InternetUtils;
import com.example.com.meimeng.util.DialogUtils;
import com.example.com.meimeng.util.Utils;

import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by 003 on 2015/8/3.
 * 有删除图片按钮的图片展示页面
 */
public class PhotoDisplayEditActivity extends BaseActivity {
    private long currentPicId;
    private Dialog dialog;
    private int type;
    private ArrayList<Long> pictureIdList = new ArrayList<>();

    @Bind(R.id.photodisplay2_view_pager)
    ViewPager viewPager;

    @Bind(R.id.delete_button)
    Button deleteButton;

    private ViewsPagerAdapter pictureViewAdapter;

    //图片对应的View
    private ArrayList<View> viewArrayList = new ArrayList<>();

    //当前图片的位置，默认为0
    private int currentPosition = 0;

    //图片id,图片View ，HashMap
    private HashMap<Long, ImageView> pictureViewHash = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photodisplay2);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        type = intent.getIntExtra("type", -1);
        if (type != -1) {
            deleteButton.setVisibility(View.GONE);
        } else {
            deleteButton.setVisibility(View.VISIBLE);
        }
        currentPicId = intent.getExtras().getLong("ImageId");
        pictureIdList = (ArrayList<Long>) intent.getSerializableExtra("allImageId");
        currentPosition = intent.getIntExtra("position", 0);

        //加载图片
        initView();
    }


    @OnClick(R.id.delete_button)
    void delete() {
        deletepicture(currentPicId);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.pic_in2, R.anim.pic_out2);
    }

    public void deletepicture(final long picId) {
        dialog = DialogUtils.createLoadingDialog(this, "正在删除...");
        dialog.show();
        try {
            if (TextUtils.isEmpty(Utils.getUserId())) {
                return;
            }
            if (TextUtils.isEmpty(Utils.getUserToken())) {
                return;
            }
            String url = InternetConstant.SERVER_URL + InternetConstant.DELETER + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();
            JSONStringer stringer = new JSONStringer().object().key("picId").value(picId).endObject();
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
                                Toast.makeText(PhotoDisplayEditActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                                Intent mIntent = new Intent();
                                mIntent.setAction("action.meimengrefreshpicture");
                                sendBroadcast(mIntent);
                                finish();
                                overridePendingTransition(R.anim.pic_in2, R.anim.pic_out2);
                            } else {
                                DialogUtils.setDialog(PhotoDisplayEditActivity.this, baseInfoBean.getError());
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            Log.e("获取用户基本信息失败了", throwable.getMessage());
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化界面
     */
    private void initView() {
        int idnum = pictureIdList.size();
        if (type != -1 || idnum == 8) {
            idnum += 1;
        }

        for (int i = 0; i < idnum - 1; i++) {
            View view = LayoutInflater.from(PhotoDisplayEditActivity.this).inflate(R.layout.big_image_view, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.big_image_image_view);

            long id = pictureIdList.get(i);
            pictureViewHash.put(id, imageView);
            InternetUtils.getPicIntoView(1080, 1920, imageView, id, PhotoDisplayEditActivity.this);

            viewArrayList.add(view);
        }

        pictureViewAdapter = new ViewsPagerAdapter(viewArrayList);
        viewPager.setAdapter(pictureViewAdapter);
        viewPager.setCurrentItem(currentPosition);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPicId = pictureIdList.get(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

}
