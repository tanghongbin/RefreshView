package com.example.com.meimeng.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.com.meimeng.MeiMengApplication;
import com.example.com.meimeng.bean.CityBean;
import com.example.com.meimeng.bean.ProvinceBean;
import com.example.com.meimeng.custom.PickerView;
import com.example.com.meimeng.db.DbOpenHelper;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/12/2.
 */
public class DataUtil {


    private Context context;
    private  DbOpenHelper dbOpenHelper;
    private  SQLiteDatabase database;

    public DataUtil(Context context){
        this.context = context;
        dbOpenHelper = new DbOpenHelper(context);
        database = dbOpenHelper.openDatabase();

    }


    /**
     *
     */
    public void getProvinceList(){
        dbOpenHelper = new DbOpenHelper(context);
        database = dbOpenHelper.openDatabase();

        ArrayList<ProvinceBean> provinceBeans = new ArrayList<>();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Cursor cursor = database.query("city", null, "parent=?", new String[]{"1"}, null, null, null);
                if (cursor.moveToFirst()) {
                    do {
                        String province = cursor.getString(cursor.getColumnIndex("province"));
                        int parent = cursor.getInt(cursor.getColumnIndex("id"));
                        MeiMengApplication.provinces.add(new ProvinceBean(province, parent, context));

                    } while (cursor.moveToNext());
                }
                cursor.close();
            }

        }).start();

    }

    public ArrayList<CityBean> getCityList(int id) {


        ArrayList<CityBean> cityBeans = new ArrayList<>();
        Cursor curso = database.query("city", null, "parent=?", new String[]{String.valueOf(id)}, null, null, null);
        if (curso.moveToFirst()) {
            do {
                String city = curso.getString(curso.getColumnIndex("city"));
                int codeId = curso.getInt(curso.getColumnIndex("id"));
                cityBeans .add(new CityBean(city,codeId));

            } while (curso.moveToNext());
        }
        curso.close();
        return cityBeans;
    }


    public void setRegionData(PickerView seleckData_a, final PickerView seleckData_b) {
//        provinces.clear();


        final ArrayList<ProvinceBean> provinces = new ArrayList<>();
        provinces.addAll(MeiMengApplication.provinces);

//        MeiMengApplication.cityBean =  provinces.get(0).getCityBeans().get(0);

        ArrayList<String> province = new ArrayList<>();

        for (ProvinceBean bean : provinces) {
            province.add(Utils.setString(bean.getProvinceName(),5));
        }

        final ArrayList<CityBean> citys = new ArrayList<>();
        seleckData_a.setData(province);


        ArrayList<String> citystrlist = new ArrayList<>();
        for(CityBean cbean : provinces.get(0).getCityBeans() ){

            citystrlist.add(cbean.getCityName());
        }
        seleckData_b.setData(citystrlist);

        Log.i("data", province.toString());
        seleckData_a.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                for (ProvinceBean bean : provinces) {
                    if (text.equals(Utils.setString(bean.getProvinceName(), 5))) {
                        citys.clear();
                        citys.addAll(bean.getCityBeans());
                        ArrayList<String> citystrlist = new ArrayList<String>();
                        for (CityBean cbean : citys) {
                            citystrlist.add(Utils.setString(cbean.getCityName(),5));
                        }
                        seleckData_b.setData(citystrlist);
                        MeiMengApplication.cityBean = citys.get(0);


                    }
                }
            }
        });

        seleckData_b.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {

                for (CityBean bean : citys) {
                    if (text.equals(bean.getCityName())) {
                        Log.i("data", "name : " + bean.getCityName() + "id  :  " + bean.getCityId());
                        MeiMengApplication.cityBean = bean;
                    }
                }
            }
        });

    }
    public void setRegionData(PickerView seleckData_a, final PickerView seleckData_b,boolean isSex) {
//        provinces.clear();


        final ArrayList<ProvinceBean> provinces = new ArrayList<>();
        provinces.addAll(MeiMengApplication.provinces);

//        MeiMengApplication.cityBean =  provinces.get(0).getCityBeans().get(0);

        ArrayList<String> province = new ArrayList<>();

        for (ProvinceBean bean : provinces) {
            province.add(Utils.setString(bean.getProvinceName(),5));
        }

        final ArrayList<CityBean> citys = new ArrayList<>();
        if (province.size()!= 0) {
            province.remove(0);
        }
        seleckData_a.setData(province);


        ArrayList<String> citystrlist = new ArrayList<>();
        if (provinces.size() != 0) {
            for(CityBean cbean : provinces.get(0).getCityBeans() ){
                citystrlist.add(cbean.getCityName());
            }
        }
        if (citystrlist.size()!= 0) {
            citystrlist.remove(0);
        }
        if (province.size()!=0&&province.get(0).equals("北京市")) {
            citystrlist.clear();
            citystrlist.add("北京市");
        }

        seleckData_b.setData(citystrlist);

        Log.i("data", province.toString());
        seleckData_a.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                for (ProvinceBean bean : provinces) {
                    if (text.equals(Utils.setString(bean.getProvinceName(), 5))) {
                        citys.clear();
                        citys.addAll(bean.getCityBeans());
                        ArrayList<String> citystrlist = new ArrayList<String>();
                        for (CityBean cbean : citys) {
                            citystrlist.add(Utils.setString(cbean.getCityName(),5));
                        }
                        seleckData_b.setData(citystrlist);
                        MeiMengApplication.cityBean = citys.get(0);


                    }
                }
            }
        });

        seleckData_b.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {

                for (CityBean bean : citys) {
                    if (text.equals(bean.getCityName())) {
                        Log.i("data", "name : " + bean.getCityName() + "id  :  " + bean.getCityId());
                        MeiMengApplication.cityBean = bean;
                    }
                }
            }
        });

    }

}
