package com.xqd.chatmessage.activity;

import android.app.Activity;
import android.os.Bundle;

import com.xqd.chatmessage.util.ActivityCollector;

/**
 * Created by 010 on 2015/8/26.
 */
public class BaseActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
