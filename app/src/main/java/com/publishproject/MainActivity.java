package com.publishproject;

import android.Manifest;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;


import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.lzy.okgo.request.BaseRequest;
import com.publishproject.core.activitiesconfig.BaseActivity;
import com.publishproject.core.commonconfig.loggerconfigs.LogUtil;
import com.publishproject.core.commonconfig.busconfigs.BusHelper;
import com.publishproject.events.ErrorEvent;
import com.publishproject.core.commonconfig.netconfigs.NetManager;
import com.publishproject.core.commonconfig.netconfigs.RequestFactory;
import com.publishproject.core.commonconfig.netconfigs.callbacks.ProgressCallback;
import com.publishproject.core.commonconfig.permissionconfigs.PermissionHelper;
import com.publishproject.core.commonconfig.permissionconfigs.PermissionListener;
import com.publishproject.databinding.ActivityMainBinding;
import com.publishproject.events.DownLoadEvent;
import com.publishproject.events.UploadEvent;


import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    private ImageView imageView;
    private PullToRefreshListView listView;

    @Override
    public int setContentLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        imageView = (ImageView) findViewById(R.id.imageView);
        listView = (PullToRefreshListView)findViewById(R.id.listview);
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,new String[]{
                "123","3232","443","343","2323",
                "123","3232","443","343","2323",
                "123","3232","443","343","2323",
                "123","3232","443","343","2323",
                "123","3232","443","343","2323", "123","3232","443","343","2323",
                "123","3232","443","343","2323",
                "123","3232","443","343","2323"
        }));
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

                PermissionHelper.requestPermission(this, "权限被拒绝", new PermissionListener() {
                    @Override
                    public void onGranted() {

                        String url = "https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=2147050784,3310809643&fm=58";
                        BaseRequest request = RequestFactory.createDownloadRequest(url)
                                .tag(this);
                        NetManager.downLoad( request, path, DownLoadEvent.class, new ProgressCallback() {
                            @Override
                            public void onProgress(long currentSize, long totalSize, float progress) {
                                Log.i("TAG","dsfdfsf");
                                LogUtil.i("TAG","current:"+currentSize+"\n  totalSize:"+totalSize+"\n  progress:"+progress);
                            }
                        });
                    }

                    @Override
                    public void onDenied(List<String> permissions) {

                    }
                }, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                BusHelper.postEvent(new ErrorEvent());
                break;
            case R.id.post:
                final String pathssss = Environment.getExternalStorageDirectory() + File.separator + "download.png";
                final String postUrl = "http://120.76.226.150:8080/service/api/file/upload";
                List<String> arrayList = Arrays.asList(pathssss, pathssss, pathssss, pathssss, pathssss);
                List<Object> objs = new ArrayList<>();
                objs.addAll(arrayList);
                List<BaseRequest> requestList = RequestFactory.createMutipleReuqst(objs);
                NetManager.upLoadMutipleFile( requestList, UploadEvent.class);
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
