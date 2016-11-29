package com.example.com.meimeng.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.com.meimeng.MeiMengApplication;
import com.example.com.meimeng.R;
import com.example.com.meimeng.activity.OthersSelfActivity;
import com.example.com.meimeng.constants.CommonConstants;
import com.example.com.meimeng.custom.CircleImageView;
import com.example.com.meimeng.gson.GsonTools;
import com.example.com.meimeng.gson.gsonbean.SearchBean;
import com.example.com.meimeng.gson.gsonbean.SearchUser;
import com.example.com.meimeng.net.InternetConstant;
import com.example.com.meimeng.net.InternetUtils;
import com.example.com.meimeng.util.DialogUtils;
import com.example.com.meimeng.util.Utils;

import org.json.JSONStringer;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by 010 on 2015/8/28.
 * 搜索id
 */
public class SearchFragment extends Fragment {

    private static final String TAG = "SearchFragment";

    private Context context;

    @Bind(R.id.search_left_arrow_image_view)
    ImageView leftArrowImageView;

    @Bind(R.id.search_id_ico_image_view)
    ImageView searchIdImageView;

    @Bind(R.id.search_id_name_text)
    TextView searchIdNameText;

    @Bind(R.id.search_id_edit)
    EditText searchIdEdit;

    @Bind(R.id.search_cancel_text)
    TextView cancelText;

    @Bind(R.id.search_result_text)
    TextView resultText;

    @Bind(R.id.search_result_layout)
    LinearLayout searchResultLayout;

    @Bind(R.id.search_header_image_view)
    CircleImageView searchHeaderImageView;

    @Bind(R.id.search__nickname_text)
    TextView nicknameText;

    @Bind(R.id.search_id_text)
    TextView searchIdText;

    @Bind(R.id.search_age_text)
    TextView ageText;

    @Bind(R.id.search_height_text)
    TextView heightText;

    @Bind(R.id.search_address_text)
    TextView addressText;

    //当前的uid
    private Long targetUid = -1L;
    private String searchId = "1";

    public interface OnSearchFragmentListener {

        //退出搜索
        public void cancelSearch();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_layout, container, false);
        ButterKnife.bind(this, view);
        searchId = searchIdEdit.toString();
        context = getActivity();

        MeiMengApplication.currentContext=getActivity();
        initView();

        return view;
    }


    /**
     * 初始化界面
     */
    private void initView() {

        searchIdEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s.toString()) || s.toString().length() < 8) {
                    Log.e(TAG, "此时的字符串不能转成Long型的");
                    resetResultWidget();
                    return;
                }

                searchListener(Long.valueOf(s.toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {
                searchIdImageView.setVisibility(View.GONE);
                searchIdNameText.setVisibility(View.VISIBLE);

                cancelText.setVisibility(View.GONE);
                leftArrowImageView.setVisibility(View.VISIBLE);


            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();

        searchIdImageView.setVisibility(View.VISIBLE);
        searchIdNameText.setVisibility(View.GONE);

        cancelText.setVisibility(View.VISIBLE);
        leftArrowImageView.setVisibility(View.GONE);
    }

    /**
     * 搜索id
     *
     * @param uid
     */
    private void searchListener(Long uid) {
        if (uid == null) {
            Log.e(TAG, "搜索的uid是空的");
            return;
        }
        targetUid = uid;
        try {

            //获取用户的uid,和token
            if (TextUtils.isEmpty(Utils.getUserId())) {
                return;
            }
            if (TextUtils.isEmpty(Utils.getUserToken())) {
                return;
            }
            String url = InternetConstant.SERVER_URL + InternetConstant.SEARCH_URL + "?uid=" + Utils.getUserId() + "&token=" + Utils.getUserToken();
            JSONStringer stringer = new JSONStringer().object().key("targetUid").value(uid).endObject();
            String jsonStr = stringer.toString();

            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1() {
                        @Override
                        public void call(Object o) {
                            SearchBean searchBean = GsonTools.getSearchBean((String) o);
                            if (searchBean.isSuccess()) {
                                if (searchBean.getParam().getUser() != null) {
                                    setResultData(searchBean);
                                } else {
                                    resetResultWidget();
                                }
                            } else {
                                resetResultWidget();
                                DialogUtils.setDialog(context, searchBean.getError());
                            }
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 给控件赋值搜索结果
     *
     * @param bean
     */
    private void setResultData(SearchBean bean) {

        SearchUser user = bean.getParam().getUser();

        //显示搜索结果
        resultText.setVisibility(View.VISIBLE);

        searchResultLayout.setVisibility(View.VISIBLE);

        if (user.getHeadPic() != null) {
            InternetUtils.getPicIntoView(200, 200, searchHeaderImageView, user.getHeadPic(), 1, true);
        }

        nicknameText.setText(user.getNickname());
        searchIdText.setText(String.valueOf(user.getUid()));
        if (user.getAge() == null || user.getAge() == 0) {
            ageText.setText("0");
        } else {
            ageText.setText(String.valueOf(user.getAge()));
        }
        Integer tmp = user.getHeight();
        if (tmp == null || tmp == 0) {
            heightText.setText("0");
        } else {
            heightText.setText(String.valueOf(user.getHeight()));
        }
        addressText.setText(user.getCity());
    }

    /**
     * 重置结果控件
     */
    private void resetResultWidget() {

        resultText.setVisibility(View.GONE);
        searchResultLayout.setVisibility(View.GONE);

        searchHeaderImageView.setImageBitmap(null);
        nicknameText.setText(null);
        searchIdText.setText(null);
        ageText.setText(null);
        heightText.setText(null);
        addressText.setText(null);
    }

    @OnClick(R.id.search_left_arrow_image_view)
    void leftArrowListener() {
        searchIdEdit.setText(null);
        ((OnSearchFragmentListener) context).cancelSearch();
    }

    @OnClick(R.id.search_cancel_text)
    void cancelTextListener() {
        searchIdEdit.setText(null);
        ((OnSearchFragmentListener) context).cancelSearch();
    }

    @OnClick(R.id.search_result_layout)
    void clickResultListener() {
        if (targetUid == -1L) {
            Log.e(TAG, "没有搜索到用户");
            return;
        }
        if (MeiMengApplication.sharedPreferences.getInt(CommonConstants.USER_VERFIY, 0) == 0) {
            Utils.JudgeUserVerfiy(context, 0);
        } else if (MeiMengApplication.sharedPreferences.getInt(CommonConstants.USER_VERFIY, 0) == 2) {
            Utils.JudgeUserVerfiy(context, 2);
        } else {
            Intent intent = new Intent(getActivity(), OthersSelfActivity.class);
            intent.putExtra("targetUid", targetUid);
            startActivity(intent);
        }
    }
}
