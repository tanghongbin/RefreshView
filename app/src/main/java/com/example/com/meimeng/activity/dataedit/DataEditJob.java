package com.example.com.meimeng.activity.dataedit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.com.meimeng.MeiMengApplication;
import com.example.com.meimeng.R;
import com.example.com.meimeng.activity.BaseActivity;
import com.example.com.meimeng.activity.DataEdit;
import com.example.com.meimeng.adapter.GxzsListAdapter3;
import com.example.com.meimeng.custom.SingleSelectView;
import com.example.com.meimeng.util.GetDbUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by songhuaiyu on 2015/11/30.
 */
public class DataEditJob extends BaseActivity {
    @Bind(R.id.gxzs_listview)
    ListView listView;
    @Bind(R.id.next_button)
    Button nextbutton;
    @Bind(R.id.question_title)
    TextView questiontitle;

    @Bind(R.id.title_text)
    TextView titleText;//标题栏
    @Bind(R.id.bow_arrow_image_view)
    ImageView bowArrowImageView;//右上角图标
    @Bind(R.id.title_left_arrow_image_view)
    ImageView leftArrowImageView;//返回图标
    @Bind(R.id.title_sure_text)
    TextView suretext;

    private List<String> list = new ArrayList<>();
    private HashMap<Integer, String> isSelectMap = new HashMap<>();
    private GxzsListAdapter3 gxzsListAdapter3;
    private HashMap<String, Integer> masterHashMap = new HashMap<>();
    private int jobid;
    private String jobstr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.gxzs_layout2);
        ButterKnife.bind(this);
        MeiMengApplication.currentContext=this;
        initView();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.e("postion", String.valueOf(position));
                SingleSelectView singleSelectView = (SingleSelectView) view;
                String description = gxzsListAdapter3.getItem(position);
                if (isSelectMap.get(position) == null) {
                    isSelectMap.clear();
                    isSelectMap.put(position, description);
                }
                //原先是已选中状态
                else {
                    singleSelectView.setChecked(false);
                    isSelectMap.remove(position);
                }

            }
        });

    }


    private void initView() {
        leftArrowImageView.setVisibility(View.VISIBLE);
        // 初始化标题栏
        titleText.setText("职位选择");
        bowArrowImageView.setVisibility(View.GONE);
        suretext.setVisibility(View.VISIBLE);
        suretext.setText("确定");
        nextbutton.setVisibility(View.GONE);
        questiontitle.setVisibility(View.GONE);

/**
 * 区别改动区域
 */
        masterHashMap = GetDbUtils.getMsterHashMap(DataEditJob.this, 3);
        list = GetDbUtils.getListSort(masterHashMap);
        gxzsListAdapter3 = new GxzsListAdapter3(this, list);
        listView.setAdapter(gxzsListAdapter3);
    }

    /**
     * 返回按钮
     */
    @OnClick(R.id.title_left_arrow_image_view)
    void back() {
        onBackPressed();
    }

    /**
     * 确定
     */
    @OnClick(R.id.title_sure_text)
    void suretext() {
        int position = listView.getCheckedItemPosition();
        Log.e("position>>>>>>>>", String.valueOf(position));
        if (position == -1) {
            onBackPressed();
        } else {
            if (isSelectMap.size() == 0) {
                onBackPressed();
            } else {
                try{
                    jobid = masterHashMap.get(isSelectMap.get(position));
                }catch (NullPointerException e){
                    e.printStackTrace();
                }finally {
                    jobstr = isSelectMap.get(position);
                }
            }
        }

        Intent intent = new Intent(this, DataEdit.class);
        intent.putExtra("job", jobstr);
        intent.putExtra("jobid", jobid);
        DataEditJob.this.setResult(9, intent);
        //关闭Activity
        DataEditJob.this.finish();
    }


}


