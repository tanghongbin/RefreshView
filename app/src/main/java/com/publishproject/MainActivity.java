package com.publishproject;

import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;


import com.hwangjr.rxbus.annotation.Subscribe;
import com.publishproject.core.activities.BaseActivity;
import com.publishproject.core.common.logger.LogUtil;
import com.publishproject.core.views.BindingListView;
import com.publishproject.core.common.net.NetManager;
import com.publishproject.databinding.ActivityMainBinding;
import com.publishproject.events.DownLoadEvent;
import com.publishproject.events.UploadEvent;
import com.publishproject.util.ImageChooseAndCropUtil;
import com.publishproject.util.ImageCropCallback;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    private ImageView imageView;
    private BindingListView listView;
    private ArrayList<String> list;
    private List<String> array = Arrays.asList("123","11","22","33","44","55","66");

    @Override
    public int setContentLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        list = new ArrayList<String>();
        list.addAll(array);
        imageView = (ImageView) findViewById(R.id.imageView);
        listView = (BindingListView)findViewById(R.id.listView);
        listView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,list));
        listView.bindList(list);
    }

    @Override
    public void loadData() {
        binding.setEvents(this);
        NetManager.init(this);
    }

    @Override
    public void onClick(View view) {
        final String path = "download.png";
        switch (view.getId()) {

            case R.id.get:

                for (int i = 0;i<10;i++){
                    list.add(new Random(1000).nextInt()+"");
                }
                listView.notifyObserverDataChanged();
                break;
            case R.id.post:
                ImageChooseAndCropUtil.getInstance(this).choosePic(new ImageCropCallback() {
                    @Override
                    public void call(String url) {
                        imageView.setImageBitmap(BitmapFactory.decodeFile(url));
                    }
                });
                break;
        }
    }
    @Subscribe
    public void down(DownLoadEvent downLoadEvent){
        if(downLoadEvent.isSuccess()){
            LogUtil.i("AG",downLoadEvent.getFile().getAbsolutePath());
        }else {
            LogUtil.i("TAG",downLoadEvent.getError().getMessage());
        }
    }
    @Subscribe
    public void upload(UploadEvent uploadEvent){
        if(uploadEvent.isSuccess()){
            LogUtil.i("TAG","打印上传的字符串--"+uploadEvent.getData());
        }else {
            LogUtil.i("TAG",uploadEvent.getError().getMessage());
        }
    }
}
