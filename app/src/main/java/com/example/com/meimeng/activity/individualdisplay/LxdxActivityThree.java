package com.example.com.meimeng.activity.individualdisplay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.com.meimeng.MeiMengApplication;
import com.example.com.meimeng.R;
import com.example.com.meimeng.activity.BaseActivity;
import com.example.com.meimeng.adapter.GxzsListAdapter1;
import com.example.com.meimeng.gson.gsonbean.LstUserAnswer;
import com.example.com.meimeng.util.GetDbUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by 003 on 2015/8/1.
 * 理想对象3
 */
public class LxdxActivityThree extends BaseActivity {
    @Bind(R.id.gxzs_gridview)
    GridView gridView;
    @Bind(R.id.next_button)
    Button button;
    @Bind(R.id.question_title)
    TextView question_title;

    @Bind(R.id.title_text)
    TextView titleText;//标题栏
    @Bind(R.id.bow_arrow_image_view)
    ImageView bowArrowImageView;//右上角图标
    @Bind(R.id.title_left_arrow_image_view)
    ImageView leftArrowImageView;//返回图标
    @Bind(R.id.title_sure_text)
    TextView jumpText;



    private List<String> list = new ArrayList<>();
    private HashMap<Integer, String> isSelectMap = new HashMap<>();
    private GxzsListAdapter1 gxzsListAdapter1;
    private List<Integer> aswidlist = new ArrayList<>();
    private HashMap<Integer, String> questionHashMap = new HashMap<>();
    private HashMap<String, Integer> answerHashMap = new HashMap<>();
    private boolean hasselectedbefore = false;
    private LstUserAnswer lstUserAnswer = new LstUserAnswer();
    /**
     * 问题id
     */
    private int thisqid;
    /**
     * 问题类型id
     */
    private int thistype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.gxzs_layout1);
        ButterKnife.bind(this);
        MeiMengApplication.LxdxManageActivity.add(this);
        button.setClickable(false);
        initView();
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("postion", String.valueOf(position));
                ImageView stateimage = (ImageView) view.findViewById(R.id.state_image);
                String description = gxzsListAdapter1.getItem(position);
                if (isSelectMap.get(position) == null) {
                    stateimage.setVisibility(View.VISIBLE);
                    isSelectMap.put(position, description);
                }
                //选中
                else {
                    stateimage.setVisibility(View.INVISIBLE);
                    isSelectMap.remove(position);
                }
                if (isSelectMap.size() == 0) {
                    button.setBackgroundResource(R.drawable.buttonstyle_nextbutton_gray);
                    button.setClickable(false);
                } else {
                    button.setBackgroundResource(R.drawable.buttonstyle_radius2);
                    button.setClickable(true);
                }
            }
        });

    }


    private void initView() {
        leftArrowImageView.setVisibility(View.VISIBLE);
        // 初始化标题栏
        titleText.setText("理想对象");
        bowArrowImageView.setVisibility(View.GONE);
        jumpText.setVisibility(View.VISIBLE);
        jumpText.setText("跳过");


        /**
         * 区别改动区域
         */
        thistype = 7;
        questionHashMap = GetDbUtils.getQuestionHashMap(LxdxActivityThree.this, thistype);
        thisqid = 37;
        question_title.setText("3/5" + questionHashMap.get(thisqid)+"（多选）");
        answerHashMap = GetDbUtils.getAnswerHashMap(LxdxActivityThree.this, thisqid);
        list = GetDbUtils.getListSort(answerHashMap);
        gxzsListAdapter1 = new GxzsListAdapter1(this, list);
        gridView.setAdapter(gxzsListAdapter1);
    }

    /**
     * 返回按钮
     */
    @OnClick(R.id.title_left_arrow_image_view)
    void back() {
        onBackPressed();
    }

    /**
     * 下一个
     */
    @OnClick(R.id.next_button)
    void gotonext() {
        int i;
        aswidlist.clear();
        for (i = 0; i < list.size(); i++) {
            if (isSelectMap.get(i) != null) {
                Log.e("i", String.valueOf(i));
                Log.e("answerHashMap", String.valueOf(answerHashMap.get(isSelectMap.get(i))));
                aswidlist.add(answerHashMap.get(isSelectMap.get(i)));
            }
        }
        LstUserAnswer lstUserAnswer = new LstUserAnswer();
        lstUserAnswer.setAswid(aswidlist);
        lstUserAnswer.setQid(thisqid);

        if (!hasselectedbefore) {
            MeiMengApplication.lxdxLstUserAnswer.add(lstUserAnswer);
        }

        hasselectedbefore = true;
        Intent intent = new Intent(this, LxdxActivityFour.class);
        startActivity(intent);

    }

    /**
     * 跳过
     */
    @OnClick(R.id.title_sure_text)
    void jumpnext() {
        Intent intent = new Intent(this, LxdxActivityFour.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MeiMengApplication.lxdxLstUserAnswer.remove(lstUserAnswer);
        MeiMengApplication.LxdxManageActivity.remove(this);
    }
}
