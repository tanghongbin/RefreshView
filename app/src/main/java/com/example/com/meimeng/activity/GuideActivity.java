package com.example.com.meimeng.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.com.meimeng.MeiMengApplication;
import com.example.com.meimeng.R;
import com.example.com.meimeng.adapter.ViewsPagerAdapter;
import com.example.com.meimeng.constants.CommonConstants;
import com.example.com.meimeng.gson.GsonTools;
import com.example.com.meimeng.gson.gsonbean.LoginBean;
import com.example.com.meimeng.gson.gsonbean.LoginItem;
import com.example.com.meimeng.net.InternetConstant;
import com.example.com.meimeng.net.InternetUtils;
import com.example.com.meimeng.util.Utils;

import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * https://github.com/sacot41/SCViewPager
 * 参考
 */

public class GuideActivity extends BaseActivity {

    ViewPager viewPager;
    List<View>  pageList;
    ImageView[] ivs=new ImageView[4];
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        editor = MeiMengApplication.sharedPreferences.edit();
        initView();//��ʼ������
        pageChangeListener();
    }
    private void initView() {
        viewPager= (ViewPager) findViewById(R.id.guide_pager);
        pageList=new ArrayList<>();
        for (int i = 0; i <4 ; i++) {
            int layout=getResources().getIdentifier("item_guide_"+(i+1),"layout",getPackageName());
            int id=getResources().getIdentifier("guide_iv_"+(i+1),"id",getPackageName());
            View view=getLayoutInflater().inflate(layout, null);
            ImageView iv= (ImageView) findViewById(id);
            pageList.add(view);
            ivs[i]=iv;
        }
        setViewBitmap();
        ViewsPagerAdapter adapter=new ViewsPagerAdapter((ArrayList<View>) pageList);
        viewPager.setAdapter(adapter);


    }


    /**
     * ����ͼ����ͼƬ
     * */
    private void setViewBitmap() {
        ImageView location = (ImageView) pageList.get(0).findViewById(R.id.guide_location);
        ImageView standard = (ImageView) pageList.get(1).findViewById(R.id.guide_standard);
        ImageView examine = (ImageView) pageList.get(2).findViewById(R.id.guide_examine);
        ImageView harvest = (ImageView) pageList.get(3).findViewById(R.id.guide_harvest);

        setBitmap(location, R.raw.guide_1);
        setBitmap(standard, R.raw.guide_2);
        setBitmap(examine, R.raw.guide_3);
        setBitmap(harvest, R.raw.guide_4);
    }

    private void setBitmap(ImageView iv, int redId) {
         Utils.readBitMap(GuideActivity.this,iv,redId);
    }

    /**
     * ����ҳ�滬��״̬����
     * */
    private void pageChangeListener() {

        viewPager.setOnPageChangeListener(onpageChangeListener);
    }

    ViewPager.OnPageChangeListener  onpageChangeListener=new ViewPager.SimpleOnPageChangeListener(){

        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);

            for (int i = 0; i < ivs.length; i++) {
                if (i ==position) {
                    ivs[i].setBackgroundResource(R.drawable.yuandian_f);
                }else{
                    ivs[i].setBackgroundResource(R.drawable.yuandian_fn);
                }
            }
            if (position == 3) {
                Timer pageTime=new Timer();
                TimerTask task=new TimerTask() {
                    @Override
                    public void run() {

                        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                        // ����������ӣ������������ã��Ͳ���Ҫ��������������
                        NetworkInfo info = manager.getActiveNetworkInfo();
                        if (info == null || !manager.getBackgroundDataSetting()) {
                            startActivity(new Intent(GuideActivity.this, RegisterActivity.class));
                            GuideActivity.this.finish();
                        } else {
                            initIntent();
                        }
                    }
                };
                pageTime.schedule(task,1*1000);
            }
        }
    };

    private void initIntent() {

        try {
            String url = InternetConstant.SERVER_URL + InternetConstant.LOGIN_URL;
            JSONStringer stringer = new JSONStringer().object()
                    .key("tel").value(MeiMengApplication.sharedPreferences.getString(CommonConstants.USER_ACCOUNT, ""))
                    .key("psw").value(MeiMengApplication.sharedPreferences.getString(CommonConstants.USER_PASSWORD, ""))
                    .key("deviceTag").value(MeiMengApplication.sharedPreferences.getString(CommonConstants.DEVICE_ID, ""))
                    .endObject();
            String jsonStr = stringer.toString();
            Observable observable = InternetUtils.getJsonObservale(url, jsonStr);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1() {
                        @Override
                        public void call(Object o) {
                            Log.i("qwe", "startactivity  :  " + o.toString());
                            LoginBean loginBean = GsonTools.getLoginBean((String) o);
                            if (loginBean.isSuccess()) {
                                LoginItem item = loginBean.getReturnValue();
//                                LoginItem item = loginBean.getResult();
                                MeiMengApplication.sex = item.getSex();

                                editor.putInt(CommonConstants.USER_VERFIY, item.getUserVerfiy()).commit();
                                editor.putInt(CommonConstants.INFO_STATE,item.getInfoState()).commit();
                                if(MeiMengApplication.sex == -1){
                                    Intent intent = new Intent(GuideActivity.this, RegisterGender.class);
                                    startActivity(intent);
                                }else {
                                    Intent intent = new Intent(GuideActivity.this, HomeActivity.class);
                                    intent.putExtra("init_type", 1);
                                    startActivity(intent);
                                }
                                for (Activity activity : MeiMengApplication.loginActivity) {
                                    activity.finish();
                                }

                                //��¼�û���id��token,����DEVICE_ID
                                editor.putLong(CommonConstants.USER_ID, item.getUid());
                                editor.putString(CommonConstants.USER_TOKEN, item.getToken());
                                editor.putInt(CommonConstants.USER_LEVEL, item.getLevel());
                                //editor.putString(CommonConstants.DEVICE_ID, DEVICE_ID);
                                editor.commit();

                            } else {
                                startActivity(new Intent(GuideActivity.this, RegisterActivity.class));
                                GuideActivity.this.finish();
                            }
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
