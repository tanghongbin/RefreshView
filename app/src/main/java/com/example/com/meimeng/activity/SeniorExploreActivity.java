package com.example.com.meimeng.activity;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.com.meimeng.R;
import com.example.com.meimeng.bean.FilterBean;
import com.example.com.meimeng.fragment.ExploreFragment;

public class SeniorExploreActivity extends BaseActivity {
    private ExploreFragment exploreFragment;
    private android.app.FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private ImageView image_more;
    private TextView title_bar_text;
    private LinearLayout more_search_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_senior_explore);
        image_more = (ImageView) findViewById(R.id.image_more);
        title_bar_text = (TextView) findViewById(R.id.title_bar_text);
        more_search_layout = (LinearLayout) findViewById(R.id.more_search_layout);
        image_more.setVisibility(ImageView.GONE);
        title_bar_text.setText("高级搜索");
        if (exploreFragment == null) {
            exploreFragment = new ExploreFragment();
        }
        Intent intent=getIntent();

        FilterBean mFilterBean= (FilterBean) intent.getSerializableExtra("mFilterBean");
        String jsonstr=intent.getStringExtra("jsonstr");
        Bundle bundle=new Bundle();
        bundle.putSerializable("mFilterBean",mFilterBean);
        bundle.putString("jsonstr", jsonstr);
        exploreFragment.setArguments(bundle);
        fragmentManager=getFragmentManager();
        transaction=fragmentManager.beginTransaction();
        transaction.add(R.id.fragment,exploreFragment);
        transaction.commit();
        more_search_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

}
