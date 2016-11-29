package com.example.com.meimeng.fragment.management;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.com.meimeng.R;
import com.example.com.meimeng.fragment.ListSlideFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2015/11/25.
 */
public class FriendsFragment extends Fragment implements View.OnClickListener{

    private View view;
    private Context context;

    @Bind(R.id.friends_manage_my_like_layout)
    RelativeLayout myLikeLayout;

    @Bind(R.id.friends_manage_my_like_text)
    TextView myLikeText;

    @Bind(R.id.friends_manage_my_like_image_view)
    ImageView myLikeImageView;

    @Bind(R.id.friends_manage_like_me_layout)
    RelativeLayout likeMeLayout;

    @Bind(R.id.friends_manage_like_me_text)
    TextView likeMeText;

    @Bind(R.id.friends_manage_like_me_image_view)
    ImageView likeMeImageView;

    @Bind(R.id.friends_manage_two_layout)
    RelativeLayout twoLayout;

    @Bind(R.id.friends_manage_two_text)
    TextView twoText;

    @Bind(R.id.friends_manage_two_image_view)
    ImageView twoImageView;

//    @Bind(R.id.title_left_arrow_image_view)
//    ImageView leftArrowImageView;

//    @Bind(R.id.bow_arrow_image_view)
//    ImageView bow_arrow_image_view;

//    @Bind(R.id.title_text)
//    TextView titleText;

    private ListSlideFragment myLikeFragment;

    private ListSlideFragment likeMeFragment;

    private ListSlideFragment twoFragment;

    private int index = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AllFragmentManagement.fragmentList.add(this);
        Log.e("Fragment", "FriendsFragment Create");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.from(getActivity()).inflate(R.layout.friends_manage,null);
        ButterKnife.bind(this, view);
        context = getActivity();
        //初始化视图
        initView();
        return view;
    }

    /**
     * 初始化视图
     */
    private void initView() {
        myLikeLayout.setOnClickListener(this);
        likeMeLayout.setOnClickListener(this);
        twoLayout.setOnClickListener(this);
//        bow_arrow_image_view.setVisibility(View.INVISIBLE);
//        leftArrowImageView.setVisibility(View.VISIBLE);
//        titleText.setText("关注列表");
        Intent intent = getActivity().getIntent();
        int initType = intent.getIntExtra("type", -1);
        FragmentManager fragmentManager = getFragmentManager();
        if (initType == 1) {
            if (twoFragment == null) {
                twoFragment = new ListSlideFragment();
                Bundle bundle = new Bundle();
                bundle.putString("type", "two");
                twoFragment.setArguments(bundle);
            }
            fragmentManager.beginTransaction().add(R.id.friends_manage_fragment_layout, twoFragment).commit();
            resetState(myLikeText, myLikeImageView, twoText, twoImageView);
            index = 2;
        } else {
            if (myLikeFragment == null) {
                myLikeFragment = new ListSlideFragment();
                Bundle bundle = new Bundle();
                bundle.putString("type", "myLike");
                myLikeFragment.setArguments(bundle);
            }
            fragmentManager.beginTransaction().add(R.id.friends_manage_fragment_layout, myLikeFragment).commit();
        }


    }

    public void startFragment(Fragment oldFragment, Fragment newFragment) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.friends_manage_fragment_layout, newFragment);
        transaction.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.friends_manage_my_like_layout:
                if (myLikeFragment == null) {
                    myLikeFragment = new ListSlideFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("type", "myLike");
                    myLikeFragment.setArguments(bundle);
                }
                switch (index) {
                    case 0:
                        break;
                    case 1:
                        startFragment(likeMeFragment, myLikeFragment);
                        resetState(likeMeText, likeMeImageView, myLikeText, myLikeImageView);
                        break;
                    case 2:
                        startFragment(twoFragment, myLikeFragment);
                        resetState(twoText, twoImageView, myLikeText, myLikeImageView);
                        break;
                    default:
                        break;
                }
                index = 0;
                break;
            case R.id.friends_manage_like_me_layout:
                if (likeMeFragment == null) {
                    likeMeFragment = new ListSlideFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("type", "likeMe");
                    likeMeFragment.setArguments(bundle);
                }
                switch (index) {
                    case 0:
                        startFragment(myLikeFragment, likeMeFragment);
                        resetState(myLikeText, myLikeImageView, likeMeText, likeMeImageView);
                        break;
                    case 1:
                        break;
                    case 2:
                        startFragment(twoFragment, likeMeFragment);
                        resetState(twoText, twoImageView, likeMeText, likeMeImageView);
                        break;
                }
                index = 1;
                break;
            case R.id.friends_manage_two_layout:
                if (twoFragment == null) {
                    twoFragment = new ListSlideFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("type", "two");
                    twoFragment.setArguments(bundle);
                }
                switch (index) {
                    case 0:
                        startFragment(myLikeFragment, twoFragment);
                        resetState(myLikeText, myLikeImageView, twoText, twoImageView);
                        break;
                    case 1:
                        startFragment(likeMeFragment, twoFragment);
                        resetState(likeMeText, likeMeImageView, twoText, twoImageView);
                        break;
                    case 2:
                        break;
                    default:
                        break;
                }
                index = 2;
                break;
            default:
                break;
        }
    }

    private void resetState(TextView oldText, ImageView oldImageView, TextView newText, ImageView newImageView) {
        oldText.setTextColor(getResources().getColor(R.color.like_text_color));
        oldImageView.setVisibility(View.GONE);

        newText.setTextColor(getResources().getColor(R.color.black_text_color));
        newImageView.setVisibility(View.VISIBLE);
    }
}
