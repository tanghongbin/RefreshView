package com.xqd.chatmessage.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;
import com.xqd.chatmessage.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by 010 on 2015/7/27.
 */
public class DbOpenHelper {
    private final int BUFFER_SIZE = 400000;
    public static final String DB_NAME = "chatSQ";
    public static final String PACKAGE_NAME = "com.xqd.chatmessage";
    public static final String DB_PATH = "/data"
            + Environment.getDataDirectory().getAbsolutePath() +"/"
            + PACKAGE_NAME+ "/databases";

    public static String dbfile1 = DB_PATH+File.separator+DB_NAME;

    private Context context;

    public DbOpenHelper(Context context) {
        this.context = context;
    }

    public DbOpenHelper(){}

    public void setDatabase() {
        try {
            File myDataPath = new File(DB_PATH);
            if (!myDataPath.exists())
            {
                myDataPath.mkdirs();
            }
            String dbfile = myDataPath+"/"+DB_NAME;

            if (!(new File(dbfile).exists())) {
                InputStream is = context.getResources().openRawResource(R.raw.chatsq);
                FileOutputStream fos = new FileOutputStream(dbfile);
                byte[] buffer = new byte[BUFFER_SIZE];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.close();
                is.close();
            }


        } catch (FileNotFoundException e) {
            Log.e("Database", "File not found");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("Database", "IO exception");
            e.printStackTrace();
        }

    }

    public static SQLiteDatabase openDatabase(){
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbfile1, null);
        return db;
    };


}