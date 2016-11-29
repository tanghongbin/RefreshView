package com.example.com.meimeng.activity;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.com.meimeng.MeiMengApplication;
import com.example.com.meimeng.R;
import com.example.com.meimeng.adapter.StoryListAdapter;
import com.example.com.meimeng.bean.ProgressBean;
import com.example.com.meimeng.bean.StoryBean;
import com.example.com.meimeng.custom.ScrollViewWithListView;
import com.example.com.meimeng.fragment.EditFragment;
import com.example.com.meimeng.fragment.SelectPictureFragment;
import com.example.com.meimeng.gson.GsonTools;
import com.example.com.meimeng.gson.gsonbean.UpdateStoryBean;
import com.example.com.meimeng.gson.gsonbean.UserStoryDetailBean;
import com.example.com.meimeng.gson.gsonbean.UserStoryDetailItem;
import com.example.com.meimeng.net.InternetConstant;
import com.example.com.meimeng.net.InternetUtils;
import com.example.com.meimeng.util.DialogUtils;
import com.example.com.meimeng.util.Utils;
import com.google.gson.Gson;

import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by 010 on 2015/7/17.
 */
public class StoryActivity extends BaseActivity implements EditFragment.OnEditListener, SelectPictureFragment.OnSelectPictureDialogListener {

    private static final String TAG = "StoryActivity";

    //进度条的文本
    private TextView progressTextView;

    @Bind(R.id.story_list_view)
    ScrollViewWithListView storyListView;

    @Bind(R.id.story_upload_cover_layout)
    RelativeLayout uploadLayout;

    @Bind(R.id.title_sure_text)
    TextView saveText;

    @Bind(R.id.bow_arrow_image_view)
    ImageView imageView;

    @Bind(R.id.title_left_arrow_image_view)
    ImageView leftArrowImageView;

    @Bind(R.id.title_text)
    TextView titleText;

    @Bind(R.id.story_edit_layout)
    LinearLayout storyEditLayout;

    @Bind(R.id.story_layout)
    LinearLayout storyLayout;

    @Bind(R.id.cover_pic_image_view)
    ImageView coverImageView;

    @Bind(R.id.main_dialog_layout)
    LinearLayout mainDialogLayout;

    @Bind(R.id.story_upload_cover_picture)
    ImageView coverPicture;

    private SelectPictureFragment selectPictureFragment;

    private EditFragment editFragment;

    private ArrayList<StoryBean> storyBeans = new ArrayList<>();

    private StoryListAdapter storyListAdapter;

    //要修改的故事内容
    private int currentIndex = -1;

    //记录当前的故事图片
    private long currentStoryPicId = 0;

    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.story);
        ButterKnife.bind(this);
        MeiMengApplication.currentContext=this;

        leftArrowImageView.setVisibility(View.VISIBLE);
        titleText.setText("编辑故事");
        imageView.setVisibility(View.GONE);
        saveText.setVisibility(View.VISIBLE);
        saveText.setText("保存");

        initView();
    }

    private void initView() {

         //获取用户故事资料
        addTestData();

        storyListAdapter = new StoryListAdapter(StoryActivity.this, storyBeans);
        storyListView.setAdapter(storyListAdapter);
        storyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StoryBean beans = (StoryBean) storyListAdapter.getItem(position);
                currentIndex = position;
                modifyFragment(beans);

            }
        });
        uploadLayout.setFocusable(true);
        uploadLayout.setFocusableInTouchMode(true);
        uploadLayout.requestFocus();

    }

    private void addTestData() {
        try {
            //获取用户的uid,和token
            if (TextUtils.isEmpty(Utils.getUserId())) {
                return;
            }
            if (TextUtils.isEmpty(Utils.getUserToken())) {
                return;
            }
            String url = InternetConstant.SERVER_URL + InternetConstant.USER_STORY_DETAIL + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();
            JSONStringer stringer = new JSONStringer().object().key("targetUid").value(Utils.getUserId()).endObject();
            String jsonStr = stringer.toString();
            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1() {
                        @Override
                        public void call(Object o) {
                            UserStoryDetailBean bean = GsonTools.getUserStoryDetailBean((String) o);
                            if (bean.isSuccess()) {
                                UserStoryDetailItem item = bean.getParam().getUserStory();
                                if (item != null) {
                                    Long pid = item.getPicId();
                                    if (pid == null || pid == 0) {
                                        coverPicture.setVisibility(View.VISIBLE);
                                    }
                                    currentStoryPicId = pid;
                                    InternetUtils.getPicIntoView(375, 375, coverImageView, pid);
                                    LinkedHashMap<String, String> storyMap = item.getMapStory();

                                    for (Map.Entry<String, String> entry : storyMap.entrySet()) {
                                        StoryBean storyBean = new StoryBean();
                                        String key = entry.getKey();
                                        storyBean.setStoryType(key);
                                        storyBean.setContent(storyMap.get(key));
                                        storyBeans.add(storyBean);
                                    }
                                    storyListAdapter.notifyDataSetChanged();

                                }

                            } else {
                                DialogUtils.setDialog(StoryActivity.this, bean.getError());

                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            Log.e(TAG, throwable.getMessage());
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cancelListener() {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.remove(editFragment);
        transaction.commit();
        storyLayout.setVisibility(View.VISIBLE);
        storyEditLayout.setVisibility(View.GONE);
    }

    @Override
    public void saveListener(String name, String content) {
        if (currentIndex == -1) {
            StoryBean bean = new StoryBean();
            bean.setStoryType(name);
            bean.setContent(content);

            storyBeans.add(bean);
            storyListAdapter.notifyDataSetChanged();
        } else {

            StoryBean bean = storyBeans.get(currentIndex);
            bean.setStoryType(name);
            bean.setContent(content);

            storyListAdapter.notifyDataSetChanged();
            currentIndex = -1;


        }

        cancelListener();

    }

    @Override
    public void onBackPressed() {
        FragmentManager manager = getFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.story_edit_layout);
        if (fragment == null) {
            super.onBackPressed();
        } else {
            cancelListener();
        }
    }

    @Override
    public void cancelDialog() {

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.remove(selectPictureFragment);
        transaction.commit();
        mainDialogLayout.setVisibility(View.GONE);
    }

    @Override
    public void getPicturePath(String path) {

    }

    @Override
    public void setUploadProgress(String pro) {
        if (progressTextView != null) {
            progressTextView.setText(pro);
            Log.e(TAG, "上传文件进度条正确");
        } else {
            Log.e(TAG, "上传文件进度条出错");
        }
    }

    @Override
    public void sendResultJson(String type, long pid) {
        InternetUtils.getPicIntoView(375, 375, coverImageView, pid);
        currentStoryPicId = pid;
        coverPicture.setVisibility(View.GONE);
    }

    @Override
    public void cancelDialog2() {

    }

    @Override
    public void requestifok() {

    }


    @OnClick(R.id.story_upload_cover_picture)
    void editPicture() {
        if (selectPictureFragment == null) {
            selectPictureFragment = new SelectPictureFragment();
        }
        mainDialogLayout.setVisibility(View.VISIBLE);
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.anim.bottom_in, 0);
        transaction.add(R.id.dialog_layout, selectPictureFragment);
        transaction.commit();
    }

    @OnClick(R.id.cover_pic_image_view)
    void editpicture() {

        if (selectPictureFragment == null) {
            selectPictureFragment = new SelectPictureFragment();
        }
        mainDialogLayout.setVisibility(View.VISIBLE);
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.anim.bottom_in, 0);
        transaction.add(R.id.dialog_layout, selectPictureFragment);
        transaction.commit();

    }

    @OnClick(R.id.title_sure_text)
    void saveStoryListener() {
        if (currentStoryPicId == 0) {
            Toast.makeText(StoryActivity.this, "请上传故事图片", Toast.LENGTH_LONG).show();
            return;
        }

        ProgressBean proBean = DialogUtils.createLoadingDialog2(this, "正在上传...");
        progressTextView = proBean.getTextView();
        dialog = proBean.getDialog();
        dialog.show();

        try {
            //获取用户的uid,和token
            if (TextUtils.isEmpty(Utils.getUserId())) {
                return;
            }
            if (TextUtils.isEmpty(Utils.getUserToken())) {
                return;
            }
            String url = InternetConstant.SERVER_URL + InternetConstant.USERSTORY_UPDATE + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();
            LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();
            for (int i = 0; i < storyBeans.size(); i++) {
                StoryBean bean = storyBeans.get(i);

                linkedHashMap.put(bean.getStoryType(), bean.getContent());
            }

            /**
             * 怎么构造story的字符串
             */

            Gson gson = new Gson();

            JSONStringer stringer = new JSONStringer().object()
                    .key("picId").value(currentStoryPicId)
                    .key("story").value(gson.toJson(linkedHashMap))
                    .endObject();
            String jsonStr = stringer.toString();
            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1() {
                        @Override
                        public void call(Object o) {
                            if (null != dialog)
                                dialog.dismiss();
                            UpdateStoryBean bean = GsonTools.getUpdateStoryBean((String) o);
                            if (bean.isSuccess()) {
                                Toast.makeText(StoryActivity.this, "修改故事成功", Toast.LENGTH_LONG).show();
                                onBackPressed();
                            } else {
                                DialogUtils.setDialog(StoryActivity.this, bean.getError());
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            Log.e(TAG, throwable.getMessage());
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.story_add_image_view)
    void addStoryListener() {
        addEditFragment();
    }

    @OnClick(R.id.title_left_arrow_image_view)
    void BackListener() {
        finish();
    }

    private void addEditFragment() {

        editFragment = new EditFragment(null, null);
        storyEditLayout.setVisibility(View.VISIBLE);
        storyLayout.setVisibility(View.GONE);
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.story_edit_layout, editFragment);
        transaction.commit();
    }

    private void modifyFragment(StoryBean beans) {
        String tilte = beans.getStoryType();
        String content = beans.getContent();
        editFragment = new EditFragment(tilte, content);
        storyEditLayout.setVisibility(View.VISIBLE);
        storyLayout.setVisibility(View.GONE);
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.story_edit_layout, editFragment);
        transaction.commit();
    }
}
