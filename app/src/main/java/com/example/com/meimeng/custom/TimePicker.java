package com.example.com.meimeng.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.example.com.meimeng.R;

import java.util.ArrayList;

/**
 * container 3 wheelView implement timePicker
 * Created by JiangPing on 2015/6/17.
 */
public class TimePicker extends LinearLayout {
    private WheelView mWheelYear;
    private WheelView mWheelMonth;
    private WheelView mWheelDay;

    public TimePicker(Context context) {
        this(context, null);
    }

    public TimePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private String year;

    private String month;

    private String day;

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater.from(getContext()).inflate(R.layout.time_picker, this);
        mWheelYear = (WheelView) findViewById(R.id.year);
        mWheelMonth = (WheelView) findViewById(R.id.month);
        mWheelDay = (WheelView) findViewById(R.id.day);

        mWheelYear.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                year = text;
            }

            @Override
            public void selecting(int id, String text) {

            }
        });

        mWheelMonth.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                month = text;
            }

            @Override
            public void selecting(int id, String text) {

            }
        });

        mWheelDay.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                day = text;
            }

            @Override
            public void selecting(int id, String text) {

            }
        });

        ArrayList<String> yearList = getYearData();
        mWheelYear.setData(yearList);
        mWheelYear.setDefault(10);
        year = yearList.get(10);

        ArrayList<String> monthList = getMonthData();
        mWheelMonth.setData(monthList);
        mWheelMonth.setDefault(0);
        month = monthList.get(0);

        ArrayList<String> dayList = getDayData();
        mWheelDay.setData(dayList);
        mWheelDay.setDefault(0);
        day = dayList.get(0);
    }

    private ArrayList<String> getYearData() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 1940; i < 2016; i++) {
            list.add(String.valueOf(i));
        }
        return list;
    }

    private ArrayList<String> getMonthData() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            list.add(String.valueOf(i));
        }
        return list;
    }

    private ArrayList<String> getDayData() {
        //ignore condition
        ArrayList<String> list = new ArrayList<>();
        for (int i = 1; i < 31; i++) {
            list.add(String.valueOf(i));
        }
        return list;
    }

    /**
     * 返回结果
     *
     * @return
     */
    public String getData() {
        return year + "-" + month + "-" + day + " " + "00:00:00";
    }
    public String getData2() {
        return year + "-" + month + "-" + day ;
    }

    public String getYear() {
        return year;
    }

    public String getMonth() {
        return month;
    }

    public String getDay() {
        return day;
    }
}
