package com.example.com.meimeng.activity;

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
 * Created by Administrator on 2015/8/23.
 */
public class MessageAttentionActivity extends BaseActivity {
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

    private int attentionId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.gxzs_layout2);
        ButterKnife.bind(this);
        MeiMengApplication.currentContext=this;
        //初始化界面
        initView();

        itemClick();

    }

    /**
     * ListView监听
     */
    private void itemClick(){
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
                if (isSelectMap.size() == 0) {
                    nextbutton.setBackgroundResource(R.drawable.buttonstyle_nextbutton_gray);
                    nextbutton.setClickable(false);
                } else {
                    nextbutton.setBackgroundResource(R.drawable.buttonstyle_radius2);
                    nextbutton.setClickable(true);
                }

            }
        });
    }

    /**
     * 初始化界面
     */
    private void initView() {
        leftArrowImageView.setVisibility(View.VISIBLE);
        // 初始化标题栏
        titleText.setText("中意人选");
        bowArrowImageView.setVisibility(View.GONE);
        suretext.setVisibility(View.VISIBLE);
        suretext.setVisibility(View.GONE);
        questiontitle.setVisibility(View.GONE);
        nextbutton.setBackgroundResource(R.drawable.buttonstyle_nextbutton_gray);
        nextbutton.setText("确定");
        nextbutton.setClickable(true);
       /**
        * 区别改动区域
        */
        masterHashMap = GetDbUtils.getMsterHashMap(MessageAttentionActivity.this, 53);
        list = GetDbUtils.getListSort(masterHashMap);
        gxzsListAdapter3 = new GxzsListAdapter3(this, list);
        Log.e(">>>>.>>>>>>", String.valueOf(list));
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
    @OnClick(R.id.next_button)
    void suretext() {
        int position = listView.getCheckedItemPosition();
        Log.e("position>>>>>>>", String.valueOf(position));
        if (position == -1) {
            attentionId = 0;
        } else {
            attentionId = masterHashMap.get(isSelectMap.get(position));
        }
        Log.e("attentionId>>>>>>>", String.valueOf(attentionId));
        Intent intent = new Intent(this, DataEdit.class);
        intent.putExtra("attentionId", attentionId);
        setResult(1, intent);
        //关闭Activity
        finish();
    }


}





