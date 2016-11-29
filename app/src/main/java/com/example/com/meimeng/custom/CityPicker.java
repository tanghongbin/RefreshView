package com.example.com.meimeng.custom;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.example.com.meimeng.R;
import com.example.com.meimeng.db.DbOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * CityPicker
 * put two WheelView into same Linearlayout
 *
 * @author JiangPing
 */
public class CityPicker extends LinearLayout {
    private static final int REFRESH_VIEW = 0x001;

    private WheelView mProvincePicker;
    private WheelView mCityPicker;

    private int mCurrProvinceIndex = -1;
    private int mTemCityIndex = -1;


    private HashMap<String, Integer> provinceCityHash = new HashMap<>();
    private HashMap<String, Integer> cityIdHash = new HashMap<>();

    private volatile String province = "";
    private volatile String city = "";

    private SQLiteDatabase database;
    private DbOpenHelper dbOpenHelper;

    public CityPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
//        getAreaInfo();
        dbOpenHelper = new DbOpenHelper(context);
        database = dbOpenHelper.openDatabase();
        Cursor cursor = database.query("city", null, "parent=?", new String[]{"1"}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String province = cursor.getString(cursor.getColumnIndex("province"));
                int parent = cursor.getInt(cursor.getColumnIndex("id"));
                provinceCityHash.put(province, parent);

            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    public CityPicker(Context context) {
        this(context, null);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater.from(getContext()).inflate(R.layout.city_picker, this);

        mProvincePicker = (WheelView) findViewById(R.id.province);
        mCityPicker = (WheelView) findViewById(R.id.city);


        Set<String> provinceSet = provinceCityHash.keySet();
        ArrayList<String> pList = new ArrayList<>();
        pList.add("不限");
        pList.addAll(provinceSet);
        mProvincePicker.setData(pList);
        mProvincePicker.setDefault(0);
        province = pList.get(0);

//        int cityId = provinceCityHash.get(pList.get(0));
        ArrayList<String> cityList = new ArrayList<>();
        cityList.add("不限");

        mCityPicker.setData(cityList);
        mCityPicker.setDefault(0);
        city = cityList.get(0);

        mProvincePicker.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                if (text.equals("") || text == null)
                    return;
                ArrayList<String> citys = new ArrayList<String>();
                if (mCurrProvinceIndex != id) {
                    mCurrProvinceIndex = id;
//                    String selectProvince = mProvincePicker.getSelectedText();
//                    if (selectProvince == null || selectProvince.equals(""))
//                        return;
//                    // get city names by province
                    if (text.equals("不限")) {
                        citys.add("不限");
                    } else {
                        int cityId = provinceCityHash.get(text);
                        citys = getCityList(cityId);
                    }

                    if (citys.size() == 0) {
                        citys.add(text);
                    }

                    mCityPicker.setData(citys);
                    mCityPicker.setDefault(0);
                }

                province = text;

                if (citys.size() > 0) {
                    city = citys.get(0);
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

                city = text;
            }

            @Override
            public void selecting(int id, String text) {
            }
        });
    }

    /**
     * 得到居住地
     *
     * @return
     */
    public String getAddress() {
        if (province.equals("不限")) {
            return province;
        }
        return province + "," + city;
    }

    public String getAddressofcity() {

        return city;
    }

    /**
     * 得到城市对应的id
     *
     * @return
     */
    public int getAddressId() {

        if (city.equals("不限")) {
            return -1;
        } else if (city.equals("上海市")) {
            return 857;
        }else if (city.equals("北京市")) {
            return 2;
        }else if (city.equals("天津市")) {
            return 19;
        }else if (city.equals("重庆市")) {
            return 2459;
        }
        return cityIdHash.get(city);


    }

    /**
     * 得到省对应的城市
     *
     * @param id
     * @return
     */
    private ArrayList<String> getCityList(int id) {
        cityIdHash.clear();
        Cursor curso = database.query("city", null, "parent=?", new String[]{String.valueOf(id)}, null, null, null);
        if (curso.moveToFirst()) {
            do {
                String city = curso.getString(curso.getColumnIndex("city"));
                int codeId = curso.getInt(curso.getColumnIndex("id"));
                cityIdHash.put(city, codeId);

            } while (curso.moveToNext());
        }
        curso.close();

        Set<String> citySet = cityIdHash.keySet();
        ArrayList<String> cityList = new ArrayList<>();
        cityList.addAll(citySet);
        return cityList;
    }
}
