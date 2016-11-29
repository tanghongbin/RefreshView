package com.example.com.meimeng.activity;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.com.meimeng.R;
import com.example.com.meimeng.fragment.ServiceMessageFragment;

public class ServiceMessageActivity extends BaseActivity {

    private ServiceMessageFragment serviceMessageFragment;
    private android.app.FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private ImageView image_more;
    private TextView title_bar_text;
    private LinearLayout more_search_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_message);
        image_more = (ImageView) findViewById(R.id.image_more);
        title_bar_text = (TextView) findViewById(R.id.title_bar_text);
        more_search_layout = (LinearLayout) findViewById(R.id.more_search_layout);
        image_more.setVisibility(ImageView.GONE);
        title_bar_text.setText("系统消息");
        if (serviceMessageFragment == null) {
            serviceMessageFragment = new ServiceMessageFragment();
        }

        fragmentManager=getFragmentManager();
        transaction=fragmentManager.beginTransaction();
        transaction.add(R.id.fragment,serviceMessageFragment);
        transaction.commit();
        more_search_layout.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

}
