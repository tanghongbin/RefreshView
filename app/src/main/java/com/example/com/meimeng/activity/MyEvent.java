package com.example.com.meimeng.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.com.meimeng.MeiMengApplication;
import com.example.com.meimeng.R;
import com.example.com.meimeng.adapter.MyFragmentPagerAdapter;
import com.example.com.meimeng.custom.FixedSpeedScroller;
import com.example.com.meimeng.fragment.MyJoinActivityFragment;
import com.example.com.meimeng.fragment.MyPrivateActivityFragment;

import java.lang.reflect.Field;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2015/7/17.
 */
public class MyEvent extends FragmentActivity implements View.OnClickListener {
    private ViewPager viewPager;
    private Button joinButton;
    private Button privateButton;
    private ArrayList<Fragment> fragmentlist = new ArrayList<>();

    private static int currentPosition = 0;

    @Bind(R.id.title_text)
    TextView titleText;

    @Bind(R.id.title_left_arrow_image_view)
    ImageView leftArrowImageView;//返回图标

    @Bind(R.id.bow_arrow_image_view)
    ImageView bow_arrow_image_view;

    MyJoinActivityFragment myJoinActivityFragment;
    MyPrivateActivityFragment myPrivateActivityFragment;

    private MyFragmentPagerAdapter myFragmentPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.event_my);
        ButterKnife.bind(this);

        MeiMengApplication.currentContext=this;
        titleText.setText("我的活动");
        leftArrowImageView.setVisibility(View.VISIBLE);
        bow_arrow_image_view.setVisibility(View.INVISIBLE);

        viewPager = (ViewPager) findViewById(R.id.MyEventViewPager);
        myJoinActivityFragment = new MyJoinActivityFragment();
        myPrivateActivityFragment = new MyPrivateActivityFragment();
        fragmentlist.add(myJoinActivityFragment);
        fragmentlist.add(myPrivateActivityFragment);
        myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentlist);
        viewPager.setAdapter(myFragmentPagerAdapter);

        //button下面的金色线条
        final View line = findViewById(R.id.slide_line);
        ViewGroup.LayoutParams lineParams = line.getLayoutParams();
        final DisplayMetrics dm = getResources().getDisplayMetrics();
        lineParams.width = dm.widthPixels / 2;
        line.setLayoutParams(lineParams);

        joinButton = (Button) findViewById(R.id.myevent_join_button);
        privateButton = (Button) findViewById(R.id.myevent_private_button);

        joinButton.setOnClickListener(this);
        privateButton.setOnClickListener(this);

        //切换fragment时下面的线条变化
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) line.getLayoutParams();

                switch (currentPosition) {

                    case 0:
                        if (currentPosition == 0 && position == 0) {
                            mlp.leftMargin = (int) (currentPosition * 1.0 * dm.widthPixels / 2 + positionOffset * (1.0 * dm.widthPixels / 2));

                        }
                        break;
                    case 1:
                        if (currentPosition == 1 && position == 0) {
                            mlp.leftMargin = (int) (currentPosition * 1.0 * dm.widthPixels / 2 - (1 - positionOffset) * (1.0 * dm.widthPixels / 2));
                        } else if (currentPosition == 1 && position == 1) {
                            mlp.leftMargin = (int) (currentPosition * 1.0 * dm.widthPixels / 2 + (positionOffset) * (1.0 * dm.widthPixels / 2));
                        }
                        break;
                    default:
                        break;

                }

                line.setLayoutParams(mlp);

            }

            @Override
            public void onPageSelected(int position) {

                currentPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    changeButtonState(currentPosition);
                }
            }
        });

    }

    /**
     * 改变button的颜色和文字的颜色
     * @param currentView
     */
    void changeButtonState(int currentView) {
        switch (currentView) {
            case 0:
                joinButton.setTextColor(getResources().getColor(R.color.text));
                privateButton.setTextColor(getResources().getColor(R.color.text_gray));
                break;
            case 1:
                joinButton.setTextColor(getResources().getColor(R.color.text_gray));
                privateButton.setTextColor(getResources().getColor(R.color.text));
                break;
            default:
                break;

        }
    }

    @OnClick(R.id.title_left_arrow_image_view)
    void Myback() {
        onBackPressed();
    }

    @Override
    public void onClick(View v) {
        setScrollerSpeed(300);
        switch (v.getId()) {
            case R.id.myevent_join_button:
                viewPager.setCurrentItem(0, true);
                break;
            case R.id.myevent_private_button:
                viewPager.setCurrentItem(1, true);
                break;
        }
    }

    void setScrollerSpeed(int duration) {
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(viewPager.getContext(),
                    new AccelerateInterpolator());
            field.set(viewPager, scroller);
            scroller.setmDuration(duration);
        } catch (Exception e) {

        }

    }
}
