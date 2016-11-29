package com.example.com.meimeng.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.com.meimeng.R;
import com.example.com.meimeng.util.ActivityCollector;

/**
 * Created by 010 on 2015/8/26.
 */
public class BaseActivity extends Activity {


    //??????
    private static int screenWidth;
    //??????
    private static int screenHeight;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
        ActivityCollector.addActivity(this);
    }

    /**
     * ??????
     */
    public static int getScreenWidth() {
        return screenWidth;
    }

    /**
     * ??????
     */
    public static int getScreenHeight() {
        return screenHeight;
    }

    /**
     * ????Title????
     * @param text  TextView title_default?????????
     * @param leftResID ?????????????   -1???????????
     * @param rightResID ?????????????  -1???????????
     * @param onClick ????????
     */
    protected void initTitleBar(String text, int leftResID,int rightResID,View.OnClickListener onClick){
        LinearLayout more_search_layout = (LinearLayout) findViewById(R.id.more_search_layout);//title???????

        TextView title_bar_text = (TextView) findViewById(R.id.title_bar_text); //title_default
        LinearLayout isHome_title = (LinearLayout) findViewById(R.id.isHome_title); //title_isHome

        RelativeLayout more_menu_layout = (RelativeLayout) findViewById(R.id.more_menu_layout); //title???????


        if(text.equals("isHome")){
            isHome_title.setVisibility(View.VISIBLE);
            title_bar_text.setVisibility(View.GONE);
            RelativeLayout explore = (RelativeLayout) isHome_title.findViewById(R.id.explore);
            RelativeLayout event = (RelativeLayout) isHome_title.findViewById(R.id.event);
            explore.setOnClickListener(onClick);
            event.setOnClickListener(onClick);

        }else {
            isHome_title.setVisibility(View.GONE);
            title_bar_text.setVisibility(View.VISIBLE);
            title_bar_text.setText(text);
        }


        if(leftResID == -1){
            more_search_layout.setVisibility(View.GONE);
        }else {
            more_search_layout.setVisibility(View.VISIBLE);
            ImageView search_image_view= (ImageView) more_search_layout.findViewById(R.id.search_image_view);
            search_image_view.setImageResource(leftResID);
            more_search_layout.setOnClickListener(onClick);
        }

        //title????????????
        if(rightResID == -1){
            more_menu_layout.setVisibility(View.GONE);
        }else {
            more_menu_layout.setVisibility(View.VISIBLE);
            ImageView image_more = (ImageView) more_menu_layout.findViewById(R.id.image_more);
            image_more.setImageResource(rightResID);
            more_menu_layout.setOnClickListener(onClick);
        }

    }

    /**
     * ?????????
     * @param rightResID
     */

    protected void setRightRes(int rightResID){

        RelativeLayout more_menu_layout = (RelativeLayout) findViewById(R.id.more_menu_layout);//title???????
        ImageView image_more = (ImageView) more_menu_layout.findViewById(R.id.image_more);
        if(more_menu_layout.getVisibility() == View.VISIBLE){
            image_more.setImageResource(rightResID);
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
