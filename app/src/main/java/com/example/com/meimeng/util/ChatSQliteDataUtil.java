package com.example.com.meimeng.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.messages.AVIMAudioMessage;
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage;
import com.avos.avoscloud.im.v2.messages.AVIMLocationMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.avoscloud.leanchatlib.controller.MessageHelper;
import com.example.com.meimeng.MeiMengApplication;
import com.example.com.meimeng.bean.ChatMessageBean;
import com.example.com.meimeng.constants.CommonConstants;
import com.example.com.meimeng.db.DbOpenHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/9/19.
 * 数据库操作类
 */
public class ChatSQliteDataUtil implements Serializable{

    private Context context;
    private SQLiteDatabase mysp = null;

    private String tableName = null;

    private String table = null;
    private DbOpenHelper dbOpenHelper;

    public ChatSQliteDataUtil(Context context){
        this.context = context;
        tableName = MeiMengApplication.sharedPreferences.getLong(CommonConstants.USER_ID,-1)+"";
        Log.i("msg",tableName);
        table = "chatMessage_hn"+tableName;
        Log.i("msg",table);
        createNewtable(table);
    }

    public ChatSQliteDataUtil(){

    }

    public void openSql(){
//        ChatSQliteOpenHelper helper = new ChatSQliteOpenHelper(context);

        dbOpenHelper = new DbOpenHelper(context);
        this.mysp = dbOpenHelper.openDatabase();

    }

    public void closeSql(){
        this.mysp.close();
    }

    public long addData(String key ,String data){
        openSql();
        ContentValues values = new ContentValues();
        values.put(key,data);
        long result = mysp.insert(table, "_id", values);
        closeSql();
        return result;
    }

    public void addData(ChatMessageBean bean){
        if(isExist(bean.getUserId())){

            Log.i("qwe","isExist");
        }else {
            openSql();
            ContentValues values = new ContentValues();
            values.put("userid",bean.getUserId()+"");
            values.put("bitmap",String.valueOf(bean.getHeadPic()));
            values.put("name",bean.getName());
            values.put("time",bean.getTime());
            values.put("conversationId",bean.getConversationId());
            if ((boolean) (bean.getAttributes().get("isOpen"))==true) {
                values.put("isOpen", 1);
            }else {
                values.put("isOpen", 0);
            }
            values.put("content",bean.getContent().toString());
            long result = mysp.insert(table, "_id", values);
            closeSql();

        }
    }


    public Boolean isExist(Long uid){
//        long uid = bean.getUid();
        Boolean isexist = false;

        if (getDataAll().size() != 0) {
            for(int i = 0;i < getDataAll().size(); i++){
                ChatMessageBean cbean  = getDataAll().get(i);
                if(uid == cbean.getUserId()){
                    isexist = true;
                    return isexist;
                }

            }
        }
        return isexist;
    }

    public int deleteData(String where , String whereArg){
        openSql();
        String whereClause = where+" =?";
        String[] whereArgs = {whereArg};
        int result = mysp.delete(table,whereClause,whereArgs);
        closeSql();
        return result;
    }

    public void upData(String where , String whereArg ,String datainto ,String data){
        openSql();
        ContentValues values = new ContentValues();
        values.put(datainto,data);
        String whereClause = where+" =?";
        String[] whereArgs = {whereArg};
        mysp.update(table,values,whereClause,whereArgs);
        closeSql();
    }
    public void upData(String where , String whereArg ,String datainto ,int data){
        openSql();
        ContentValues values = new ContentValues();
        values.put(datainto, data);
        String whereClause = where+" =?";
        String[] whereArgs = {whereArg};
        mysp.update(table,values,whereClause,whereArgs);
        closeSql();
    }

    public  ChatMessageBean getDataByUserid(String userid){
        openSql();
        ChatMessageBean bean = new ChatMessageBean();
        String s="select * from "+table+" where userid like '"+userid+"%'";
        Cursor c =mysp.rawQuery(s,null);
        while (c.moveToNext()) {
            String userId = c.getString(c.getColumnIndex("userid"));
            bean.setUserId(Long.parseLong(userId));
            String bitmap = c.getString(c.getColumnIndex("bitmap"));
            if(!bitmap.equals("null")){
                bean.setHeadPic(Long.parseLong(bitmap));
            }
            String name = c.getString(c.getColumnIndex("name"));
            bean.setName(name);
            String time = c.getString(c.getColumnIndex("time"));
            bean.setTime(time);
            String conversationId = c.getString(c.getColumnIndex("conversationId"));
            Log.d("wz","conversationId="+conversationId);
            bean.setConversationId(conversationId);
            int isOpen = c.getInt(c.getColumnIndex("isOpen"));
            Map attrs=new HashMap();
            if (isOpen == 0) {
                attrs.put("isOpen",false);
            }else{
                attrs.put("isOpen",true);
            }
            bean.setAttributes(attrs);
            String content = c.getString(c.getColumnIndex("content"));
            bean.setContent(content);
        }
        return bean;
    }
    public  ArrayList<ChatMessageBean> getDataAll(){
        ArrayList<ChatMessageBean> list = new ArrayList<ChatMessageBean>();
        openSql();
        Cursor c = mysp.query(table,new String[]{"userid","bitmap" ,"name","time" ,"conversationId","isOpen" ,"content"},null,null,null,null,"time desc");
        while (c.moveToNext()) {
            ChatMessageBean bean = new ChatMessageBean();
            String userid = c.getString(c.getColumnIndex("userid"));
            bean.setUserId(Long.parseLong(userid));
            String bitmap = c.getString(c.getColumnIndex("bitmap"));
            if(!bitmap.equals("null")){
                bean.setHeadPic(Long.parseLong(bitmap));
            }
            String name = c.getString(c.getColumnIndex("name"));
            bean.setName(name);
            String time = c.getString(c.getColumnIndex("time"));
            bean.setTime(time);
            String conversationId = c.getString(c.getColumnIndex("conversationId"));
            Log.d("wz","conversationId="+conversationId);
            bean.setConversationId(conversationId);
            int isOpen = c.getInt(c.getColumnIndex("isOpen"));
            Map attrs=new HashMap();
            if (isOpen == 0) {
                attrs.put("isOpen",false);
            }else{
                attrs.put("isOpen",true);
            }
            bean.setAttributes(attrs);
            String content = c.getString(c.getColumnIndex("content"));
            bean.setContent(content);
            if(content.equals(" ")||content == null||content.equals("")){

            }else {
                if (list.size()!= 0) {
                    if (isRepeat(list, bean)==false) {
                        list.add(bean);
                    }
                }else{
                    list.add(bean);
                }
            }

		}

        return list;
    }

    private boolean isRepeat(ArrayList<ChatMessageBean> list, ChatMessageBean bean) {
        for (int i = 0; i <list.size() ; i++) {
            if (list.get(i).getUserId()==bean.getUserId()) {
                return  true;
            }
        }
        return false;
    }

    public void createNewtable(String tablename) {
        try {
            openSql();
            StringBuffer str =new StringBuffer();
//		String str = "create table if not exists chatMessage2(_id integer primary key autoincrement,userid text,bitmap text,name text,time text,content text)";
            str.append("create table if not exists ").append(tablename).append(" (_id integer primary key autoincrement,userid text,bitmap text,name text,time text,conversationId text, isOpen integer,content text)");
            this.mysp.execSQL(str.toString());
            closeSql();
        }catch (SQLiteException e){
            e.printStackTrace();
        }
	}
    
    public void deleteTable(String tablename) {
		openSql();
		StringBuffer str =new StringBuffer();
//		String str = "drop table if exists"+taglename;
		str.append("drop table if exists ").append(tablename);
		this.mysp.execSQL(str.toString());
		closeSql();
	}
    public void deleteUserById(String userId) {
		openSql();
		StringBuffer str =new StringBuffer();
//		String str = "drop table if exists"+taglename;
		str.append("delete from ").append("chatMessage_hn")
                .append(MeiMengApplication.sharedPreferences.getLong(CommonConstants.USER_ID,-1)+" ")
                .append("where userid like '").append(userId+"%'");
		this.mysp.execSQL(str.toString());
		closeSql();
	}

    public CharSequence getContent(String content) {
        int type = 0;
        CharSequence content_char = null;
        AVIMTypedMessage message;
        try {
            JSONObject obj_content = new JSONObject(content);
            type = obj_content.getInt("_lctype");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        switch (type){
            case -1:
                message =new AVIMTextMessage();
                message.setContent(content);
                content_char = MessageHelper.outlineOfMsg(message);
                break;
            case -2:
                message =new AVIMImageMessage();
                message.setContent(content);
                content_char = MessageHelper.outlineOfMsg(message);
                break;
            case -3:
                message =new AVIMAudioMessage();
                message.setContent(content);
                content_char = MessageHelper.outlineOfMsg(message);
                break;
            case -5:
                message =new AVIMLocationMessage();
                message.setContent(content);
                content_char = MessageHelper.outlineOfMsg(message);
                break;
            default:
                break;
        }

        return content_char;

    }

}
