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
public class ThreeWheelView extends LinearLayout {
    private WheelView firstWheelView;
    private WheelView secondWheelView;
    private WheelView thirdWheelView;

    public ThreeWheelView(Context context) {
        this(context, null);
    }

    public ThreeWheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private String year;

    private String month;

    private String day;





    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater.from(getContext()).inflate(R.layout.time_picker, this);
        firstWheelView = (WheelView) findViewById(R.id.year);
        secondWheelView = (WheelView) findViewById(R.id.month);
        thirdWheelView = (WheelView) findViewById(R.id.day);


        firstWheelView.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                year = text;
            }

            @Override
            public void selecting(int id, String text) {
            }
        });

        secondWheelView.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                month = text;
            }

            @Override
            public void selecting(int id, String text) {

            }
        });

        thirdWheelView.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                day = text;
            }

            @Override
            public void selecting(int id, String text) {

            }
        });

        ArrayList<String> yearList = getYearData();
        firstWheelView.setData(yearList);
        firstWheelView.setDefault(30);
        year = yearList.get(30);

        ArrayList<String> monthList = getMonthData();
        secondWheelView.setData(monthList);
        secondWheelView.setDefault(6);
        month = monthList.get(6);

        ArrayList<String> dayList = getDayData();
        thirdWheelView.setData(dayList);
        thirdWheelView.setDefault(15);
        day = dayList.get(15);
    }

    /**
     * 获取年份
     *
     * @return
     */
    private ArrayList<String> getYearData() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 1950; i < 2016; i++) {
            list.add(i + "");
        }

        return list;
    }

    /**
     * 获取月份
     *
     * @return
     */
    private ArrayList<String> getMonthData() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            list.add("0" + i);
        }
        for (int i = 10; i < 13; i++) {
            list.add(String.valueOf(i));
        }
        return list;
    }

    /**
     * 获取日份
     *
     * @return
     */
    private ArrayList<String> getDayData() {
        //ignore condition
        ArrayList<String> list = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            list.add("0" + i);
        }
        for (int i = 10; i < 32; i++) {
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
        return year + "-" + month + "-" + day;
    }

    public String getDataYear() {
        return year;
    }

    public String getDatamoth() {
        return month;
    }

    public String getDataday() {
        return day;
    }

    /**
     * 返回
     *
     * @return
     */
    public String getYear() {
        return year;
    }

}
