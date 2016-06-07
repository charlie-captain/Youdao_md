package com.example.administrator.dictionary.db.biz;

import android.database.sqlite.SQLiteDatabase;

import com.example.administrator.dictionary.config.ConfigFinal;
import com.example.administrator.dictionary.db.DbSqlite;

/**
 * Created by Administrator on 2016/5/20.
 */
public class DailySqliteHelper {


    public static void insert(final DbSqlite dbSqlite, final String content, final String note, final String mp3, final String time) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase db = dbSqlite.getWritableDatabase();
                db.beginTransaction();
                try {
                    db.execSQL("insert into " + ConfigFinal.TB_DAILY + "(content,note,mp3,time) values(?,?,?,?)", new Object[]{content, note, mp3, time});
                    db.setTransactionSuccessful();

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    db.endTransaction();
                }
            }
        }).start();
    }

}
