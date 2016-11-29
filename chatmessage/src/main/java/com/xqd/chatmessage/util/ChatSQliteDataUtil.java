package com.xqd.chatmessage.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.messages.AVIMAudioMessage;
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage;
import com.avos.avoscloud.im.v2.messages.AVIMLocationMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.avoscloud.leanchatlib.controller.MessageHelper;
import com.xqd.chatmessage.ChatAplication;
import com.xqd.chatmessage.db.DbOpenHelper;
import com.xqd.chatmessage.gson.ChatMessageBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2015/9/19.
 * 数据库操作类
 */
public class ChatSQliteDataUtil implements Serializable{

    private Context context;
    private SQLiteDatabase mysp = null;

    private String tableName = null;

    private String table = null;

    public ChatSQliteDataUtil(Context context){
        this.context = context;
    }

    public ChatSQliteDataUtil(){
       tableName = ChatAplication.sharedPreferences.getLong(CommonConstants.USER_ID,-1)+"";
        Log.i("msg",tableName);
        table = "chatMessage_hn"+tableName;
        Log.i("msg",table);
        createNewtable(table);
    }

    public void openSql(){
//        ChatSQliteOpenHelper helper = new ChatSQliteOpenHelper(context);
        this.mysp = DbOpenHelper.openDatabase();

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

    public long addData(ChatMessageBean bean){
        openSql();
        ContentValues values = new ContentValues();
        values.put("userid",bean.getUserId()+"");

        values.put("bitmap",String.valueOf(bean.getHeadPic()));
        values.put("name",bean.getName());
        values.put("time",bean.getTime());
        values.put("content",bean.getContent().toString());
        long result = mysp.insert(table, "_id", values);
        closeSql();
        return result;
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

    public ArrayList<ChatMessageBean> getDataAll(){
        ArrayList<ChatMessageBean> list = new ArrayList<ChatMessageBean>();
        openSql();
        Cursor c = mysp.query(table,new String[]{"userid","bitmap" ,"name","time" ,"content"},null,null,null,null,null);
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
            String content = c.getString(c.getColumnIndex("content"));

            if(content.equals("")||content == null){
                bean.setContent(content);
            }else {
                bean.setContent(getContent(content));
            }
            list.add(bean);
		}

        return list;
    }
    
    public void createNewtable(String tablename) {
    	openSql();
    	StringBuffer str =new StringBuffer();
//		String str = "create table if not exists chatMessage2(_id integer primary key autoincrement,userid text,bitmap text,name text,time text,content text)";
    	str.append("create table if not exists ").append(tablename).append("(_id integer primary key autoincrement,userid text,bitmap text,name text,time text,content text)");
    	this.mysp.execSQL(str.toString());
		closeSql();
	}
    
    public void deleteTable(String tablename) {
		openSql();
		StringBuffer str =new StringBuffer();
//		String str = "drop table if exists"+taglename;
		str.append("drop table if exists ").append(tablename);
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
