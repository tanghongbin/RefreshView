package com.example.com.meimeng.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.example.com.meimeng.R;

import java.util.ArrayList;

/**
 * Created by 010 on 2015/7/29.
 */
public class TiwnPicker extends LinearLayout {


    private WheelView mProvincePicker;
    private WheelView mCityPicker;

    private int mCurrProvinceIndex = -1;
    private int mTemCityIndex = -1;



    private int finalData = 100;

    private volatile String firstData = "";
    private volatile String secondData = "";

    private ArrayList<Integer> firstDataList = new ArrayList<>();

    public TiwnPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        firstDataList.add(18);
    }

    public TiwnPicker(Context context) {
        this(context, null);
    }

    /**

     */
    public void setData(ArrayList<Integer> mFirstDataList){
        this.firstDataList = mFirstDataList;
        this.finalData = firstDataList.get(firstDataList.size()-1);
        onFinishInflate();
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater.from(getContext()).inflate(R.layout.city_picker, this);

        mProvincePicker = (WheelView) findViewById(R.id.province);
        mCityPicker = (WheelView) findViewById(R.id.city);

        ArrayList<String> pList = new ArrayList<>();

        pList.add("不限");

        for (int i=0;i<firstDataList.size();i++){
            int data = firstDataList.get(i);
            pList.add(String.valueOf(data));
        }
        mProvincePicker.setData(pList);
        mProvincePicker.setDefault(0);
        firstData = pList.get(0);


        ArrayList<String> cityList = new ArrayList<>();
        int first = firstDataList.get(0);

        cityList.add("不限");

        for (int i = first+1;i<finalData;i++){
            cityList.add(String.valueOf(i));
        }
        mCityPicker.setData(cityList);
        mCityPicker.setDefault(0);
        secondData = cityList.get(0);

        mProvincePicker.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                if (text.equals("") || text == null)
                    return;
                ArrayList<String> citys = new ArrayList<String>();
                if (mCurrProvinceIndex != id) {
                    mCurrProvinceIndex = id;
                    
                    if (text.equals("不限")){
                        citys.add("不限");
                    }else {
                        int first = Integer.valueOf(text);
                        for (int i = first+1;i<finalData;i++){
                            citys.add(String.valueOf(i));
                        }

                    }


                    if (citys.size() == 0) {
                        citys.add(text);
                    }

                    mCityPicker.setData(citys);
                    mCityPicker.setDefault(0);
                }

                firstData = text;

                if (citys.size() > 0) {
                    secondData = citys.get(0);
                }
            }

            @Override
            public void selecting(int id, String text) {

            }
        });

        mCityPicker.setOnSelectListener(new WheelView.OnSelectListener() {

            @Override
            public void endSelect(int id, String text) {
                if (text.equals("") || text == null)
                    return;
                if (mTemCityIndex != id) {
                    mTemCityIndex = id;
                    String selectCity = mCityPicker.getSelectedText();
                    if (selectCity == null || selectCity.equals(""))
                        return;
                    int lastIndex = Integer.valueOf(mCityPicker.getListSize());
                    if (id > lastIndex) {
                        mCityPicker.setDefault(lastIndex - 1);
                    }
                }
                secondData = text;
            }

            @Override
            public void selecting(int id, String text) {
            }
        });
    }

    /**

     * 杩斿洖绗竴涓粴鍔ㄨ疆鐨勬暟鎹?

     * @return
     */
    public String getFrirstData(){
        return firstData;
    }

    /**

     * 杩斿洖绗簩涓粴鍔ㄨ疆鐨勬暟鎹?

     * @return
     */
    public String getSecondData(){
        return secondData;
    }

}
