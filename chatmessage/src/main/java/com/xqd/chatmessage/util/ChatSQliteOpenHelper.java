package com.xqd.chatmessage.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2015/9/19.
 * 打开数据库帮助类
 */
public class ChatSQliteOpenHelper extends SQLiteOpenHelper {

    private static String name = "chatSQ";

    public static String table = "chatMessage";

    public ChatSQliteOpenHelper(Context context ) {
        super(context, name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String publish = "create table chatMessage(_id integer primary key autoincrement,userid text,bitmap text,name text,time text,content text)";
        sqLiteDatabase.execSQL(publish);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        sqLiteDatabase.execSQL("DPOP TABLE IF EXISTS notes");
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }
}
