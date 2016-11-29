package com.example.com.meimeng.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.com.meimeng.db.DbOpenHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 
 
import java.util.Set;
 

/**
 * Created by 003 on 2015/8/5.
 */
public class GetDbUtils {


    public static HashMap<Integer, String> getQuestionHashMap(Context context, int type) {
        HashMap<Integer, String> individualHashMap = new HashMap<>();
        individualHashMap.clear();
        DbOpenHelper dbOpenHelper = new DbOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.openDatabase();
        Cursor curso = database.query("sys_question", null, "type=?", new String[]{String.valueOf(type)}, null, null, null);
        if (curso.moveToFirst()) {
            do {
                String value = curso.getString(curso.getColumnIndex("value"));
                int qid = curso.getInt(curso.getColumnIndex("qid"));
                individualHashMap.put(qid, value);
            } while (curso.moveToNext());
        }
        curso.close();

        return individualHashMap;
    }

    public static HashMap<String, Integer> getAnswerHashMap(Context context, int qid) {
        HashMap<String, Integer> individualHashMap = new HashMap<>();
        individualHashMap.clear();
        DbOpenHelper dbOpenHelper = new DbOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.openDatabase();
        Cursor curso = database.query("sys_answer", null, "qid=?", new String[]{String.valueOf(qid)}, null, null, null);
        if (curso.moveToFirst()) {
            do {
                String value = curso.getString(curso.getColumnIndex("value"));
                int aswid = curso.getInt(curso.getColumnIndex("aswid"));
                individualHashMap.put(value, aswid);

            } while (curso.moveToNext());
        }
        curso.close();

        return individualHashMap;
    }

 
    public static HashMap<String, Integer> getMsterHashMap(Context context, int type) {
        HashMap<String, Integer> individualHashMap = new HashMap<>();
        individualHashMap.clear();
        DbOpenHelper dbOpenHelper = new DbOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.openDatabase();
        Cursor curso = database.query("sys_master", null, "type=?", new String[]{String.valueOf(type)}, null, null, null);
        if (curso.moveToFirst()) {
            do {
                String value = curso.getString(curso.getColumnIndex("value"));
                int key = curso.getInt(curso.getColumnIndex("key"));
                individualHashMap.put(value, key);

            } while (curso.moveToNext());
        }
        curso.close();
        Set<String> Set = individualHashMap.keySet();
        ArrayList<String> list = new ArrayList<>();
        list.addAll(Set);
        return individualHashMap;
    }
 
    public static List<String> getListSort(HashMap<String, Integer> hashMap) {
        List<String> list = new ArrayList<>();
        List<Map.Entry<String, Integer>> infoIds = new ArrayList<Map.Entry<String, Integer>>(
                hashMap.entrySet());
        Collections.sort(infoIds, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2) {
                return (o1.getValue() - o2.getValue());
            }
        });
        for (int i = 0; i < infoIds.size(); i++) {
            Map.Entry<String, Integer> ent = infoIds.get(i);
            list.add(ent.getKey());
        }
        return list;
    }
 

 
    public static ArrayList<String> getArrayListSort(HashMap<String, Integer> hashMap) {
        ArrayList<String> list = new ArrayList<>();
        List<Map.Entry<String, Integer>> infoIds = new ArrayList<Map.Entry<String, Integer>>(
                hashMap.entrySet());
        Collections.sort(infoIds, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2) {
                return (o1.getValue() - o2.getValue());
            }
        });
        for (int i = 0; i < infoIds.size(); i++) {
            Map.Entry<String, Integer> ent = infoIds.get(i);
            list.add(ent.getKey());
        }
        return list;
    }
 

}
