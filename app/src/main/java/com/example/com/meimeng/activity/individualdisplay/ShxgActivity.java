package com.example.com.meimeng.activity.individualdisplay;

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
import com.example.com.meimeng.adapter.GxzsListAdapter1;
import com.example.com.meimeng.adapter.GxzsListAdapter2;
import com.example.com.meimeng.gson.gsonbean.LstUserAnswer;
import com.example.com.meimeng.util.GetDbUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by 003 on 2015/8/1.
 * 生活习惯1
 */
public class ShxgActivity extends BaseActivity {
    @Bind(R.id.gxzs_listview)
    ListView listView;
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
    private GxzsListAdapter2 gxzsListAdapter2;
    private GxzsListAdapter1 gxzsListAdapter1;
    private List<Integer> aswidlist = new ArrayList<>();
    private HashMap<Integer, String> questionHashMap = new HashMap<>();
    private HashMap<String, Integer> answerHashMap = new HashMap<>();
    private LstUserAnswer lstUserAnswer = new LstUserAnswer();
    private boolean hasselectedbefore = false;
    /**
     * 问题id
     */
    private int thisqid;
    /**
     * 问题类型id
     */
    private int thistype;
    private int sex = MeiMengApplication.sex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.gxzs_layout2);
        ButterKnife.bind(this);
        MeiMengApplication.currentContext=this;
        MeiMengApplication.ShxgManageActivity.add(this);
        MeiMengApplication.shxgLstUserAnswer.clear();
        button.setClickable(false);
        initView();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.e("postion", String.valueOf(position));
              /*  SingleSelectView singleSelectView = (SingleSelectView) view;
                String description = gxzsListAdapter1.getItem(position);
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
                    button.setBackgroundResource(R.drawable.buttonstyle_nextbutton_gray);
                    button.setClickable(false);
                } else {
                    button.setBackgroundResource(R.drawable.buttonstyle_radius2);
                    button.setClickable(true);
                }*/
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
        titleText.setText("生活习惯");
        bowArrowImageView.setVisibility(View.GONE);
        jumpText.setVisibility(View.VISIBLE);
        jumpText.setText("跳过");

/**
 * 区别改动区域
 */
        thistype = 2;
        questionHashMap = GetDbUtils.getQuestionHashMap(ShxgActivity.this, thistype);
            thisqid = 7;
        //这部分只是界面展示用的
//        thisqid = 7;
        question_title.setText("1/5" + questionHashMap.get(thisqid) + "（多选）");
        answerHashMap = GetDbUtils.getAnswerHashMap(ShxgActivity.this, thisqid);
        Set<String> aSet = answerHashMap.keySet();
        list.addAll(aSet);
        //特殊处理，交换两个选项的位置
      /*  String ss=list.get(6);
        list.remove(6);
        list.add(ss);*/

        gxzsListAdapter1 = new GxzsListAdapter1(this, list);
        listView.setAdapter(gxzsListAdapter1);
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

        aswidlist.clear();
        int position = listView.getCheckedItemPosition();
        aswidlist.add(answerHashMap.get(isSelectMap.get(position)));

        lstUserAnswer.setAswid(aswidlist);
        lstUserAnswer.setQid(thisqid);

        if (!hasselectedbefore) {
            MeiMengApplication.shxgLstUserAnswer.add(lstUserAnswer);
        }
        hasselectedbefore = true;
        Intent intent = new Intent(this, ShxgActivityTwo.class);
        startActivity(intent);

    }

    /**
     * 跳过
     */
    @OnClick(R.id.title_sure_text)
    void jumpnext() {
        Intent intent = new Intent(this, ShxgActivityTwo.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //将答案从list中移除
        MeiMengApplication.shxgLstUserAnswer.remove(lstUserAnswer);
        MeiMengApplication.ShxgManageActivity.remove(this);
    }
}
