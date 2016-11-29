package com.xqd.chatmessage.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xqd.chatmessage.R;
import com.xqd.chatmessage.gson.GsonTools;
import com.xqd.chatmessage.gson.SearchBean;
import com.xqd.chatmessage.gson.SearchUser;
import com.xqd.chatmessage.net.InternetConstant;
import com.xqd.chatmessage.net.InternetUtils;
import com.xqd.chatmessage.util.CircleImageView;
import com.xqd.chatmessage.util.MessageUtils;
import com.xqd.chatmessage.util.Utils;

import org.json.JSONStringer;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by Administrator on 2015/9/10.
 */
public class SearchActivity extends BaseActivity {

    @Bind(R.id.leftarrow_image_view)
    ImageView leftarrow;

    @Bind(R.id.search_image_view)
    ImageView searchimg;

    @Bind(R.id.title_bar_text)
    TextView titletext;

    @Bind(R.id.search_id_edit)
    EditText searchIdEdit;

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

    @Bind(R.id.search_btn1)
    Button searchbtn1;

    @Bind(R.id.search_btn2)
    Button searchbtn2;
    //当前的uid
    private Long targetUid = -1L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        initView();
        searchimg.setVisibility(View.GONE);
        leftarrow.setImageResource(R.drawable.arrow_left);
        leftarrow.setVisibility(View.VISIBLE);
        leftarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        titletext.setText("查找会员");
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
                if (TextUtils.isEmpty(s.toString())) {
                    resetResultWidget();
                    return;
                }
                searchListener(Long.valueOf(s.toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });
    }

    /**
     * 搜索id
     *
     * @param uid
     */
    private void searchListener(Long uid) {
        if (uid == null) {
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
//                                DialogUtils.setDialog(context, searchBean.getError());
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
        searchbtn1.setVisibility(View.VISIBLE);
//        searchbtn2.setVisibility(View.VISIBLE);
        if (user.getHeadPic() != null) {
            InternetUtils.getPicIntoView(200, 200, searchHeaderImageView, user.getHeadPic(), 1, true);
        }
        nicknameText.setText(user.getNickname());
        searchIdText.setText(String.valueOf(user.getUid()));
        ageText.setText(String.valueOf(user.getAge()));
        heightText.setText(String.valueOf(user.getHeight()));
        addressText.setText(user.getCity());
    }

    /**
     * 重置结果控件
     */
    private void resetResultWidget() {

        resultText.setVisibility(View.GONE);
        searchResultLayout.setVisibility(View.GONE);
        searchbtn1.setVisibility(View.GONE);
        searchbtn2.setVisibility(View.GONE);
        searchHeaderImageView.setImageBitmap(null);
        nicknameText.setText(null);
        searchIdText.setText(null);
        ageText.setText(null);
        heightText.setText(null);
        addressText.setText(null);
    }

    @OnClick(R.id.search_result_layout)
    void clickResultListener() {
        if (targetUid == -1L) {
            return;
        }
        MessageUtils.setLeanCloudOtherUid(SearchActivity.this, targetUid);
          /*  Intent intent = new Intent(getActivity(), OthersSelfActivity.class);
            intent.putExtra("targetUid", targetUid);
            startActivity(intent);*/

    }

    //发送消息
    @OnClick(R.id.search_btn1)
    void btn1Listener() {
        if (targetUid == -1L) {
            return;
        }
        MessageUtils.setLeanCloudOtherUid(SearchActivity.this, targetUid);
    }
}
