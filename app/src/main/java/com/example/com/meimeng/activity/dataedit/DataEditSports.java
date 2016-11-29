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
import com.example.com.meimeng.util.GetDbUtils;
import com.example.com.meimeng.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2015/8/12.
 */
public class DataEditSports extends BaseActivity {
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

    private List<String> list = new ArrayList<String>();
    private HashMap<Integer, String> isSelectMap = new HashMap<>();
    private MultiSelectableAdapter adapter;
    private HashMap<String, Integer> masterHashMap = new HashMap<>();
    private int nationid;
    private String nationstr;

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
                // 取得ViewHolder对象，这样就省去了通过层层的findViewById去实例化我们需要的cb实例的步骤
                MultiSelectableAdapter.ViewHolder holder = (MultiSelectableAdapter.ViewHolder) view.getTag();
                // 改变CheckBox的状态
                holder.cb.toggle();
                // 将CheckBox的选中状况记录下来
                adapter.getIsSelected().put(position, holder.cb.isChecked());
                if (isSelectMap.get(position) == null) {
                    /*if(isSelectMap.size() >= 9){
                        listView.refreshDrawableState();
                    }else {
                    }*/
                    String description = adapter.getItem(position).toString();
                    isSelectMap.put(position, description);
                }
                //原先是已选中状态
                else {
                    isSelectMap.remove(position);
                }
            }
        });

    }


    private void initView() {
        leftArrowImageView.setVisibility(View.VISIBLE);
        // 初始化标题栏
        titleText.setText("我喜欢的运动");
        bowArrowImageView.setVisibility(View.GONE);
        suretext.setVisibility(View.VISIBLE);
        suretext.setText("确定");
        nextbutton.setVisibility(View.GONE);
        questiontitle.setVisibility(View.GONE);

/**
 * 区别改动区域
 */
        masterHashMap = GetDbUtils.getMsterHashMap(DataEditSports.this, 42);
        list = GetDbUtils.getListSort(masterHashMap);
        adapter = new MultiSelectableAdapter(list,this);
        listView.setAdapter(adapter);
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
                    nationid = masterHashMap.get(isSelectMap.get(position));
                }catch (NullPointerException e){
                    e.printStackTrace();
                }finally {
                    nationstr = isSelectMap.get(position);
                }
            }
        }

        if (isSelectMap.size()>9) {
            Utils.setPromptDialog(DataEditSports.this, "最多只能选择9项！");
        }else{
            Intent intent = new Intent(this, DataEdit.class);

            ArrayList<String> lst = new ArrayList<>();
            Iterator iter = isSelectMap.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                Object key = entry.getKey();
                Object val = entry.getValue();
                lst.add(val.toString());
            }

            intent.putExtra("selectMap", lst);
            DataEditSports.this.setResult(10, intent);
            //关闭Activity
            DataEditSports.this.finish();
        }
    }


}


