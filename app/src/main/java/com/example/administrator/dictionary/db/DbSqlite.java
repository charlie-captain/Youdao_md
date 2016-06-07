package com.example.administrator.dictionary.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.administrator.dictionary.config.ConfigFinal;

/**
 * Created by Administrator on 2016/5/7.
 */
public class DbSqlite extends SQLiteOpenHelper {

    private static final String TB_HISTORY = "create table history" + "(id integer primary key autoincrement," + "word text," + "ph_en text," + "ph_en_mp3 text," + "ph_am text," + "ph_am_mp3 text," + "interpret text)";        //搜索历史

    private static final String TB_LISTBOOK = "create table listbook" + "(id integer primary key autoincrement," + "word text," + "ph_en text," + "ph_en_mp3 text," + "ph_am text," + "ph_am_mp3 text," + "interpret text)";            //单词本

    private static final String TB_DAILY = "create table daily " + "(id integer primary key autoincrement," + "content text," + "note text," + "mp3 text," + "time text)";

    public DbSqlite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /*
     * 数据库第一次被创建时调用onCreate
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TB_HISTORY);
        db.execSQL(TB_LISTBOOK);
        db.execSQL(TB_DAILY);
    }

    /*
     * 如果 DATABASE_VERSION 改变 系统会调用onUpgrade
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + ConfigFinal.TB_HISTORY);
        db.execSQL("drop table if exists " + ConfigFinal.TB_LISTBOOK);
        db.execSQL("drop table if exists " + TB_DAILY);
        onCreate(db);
    }

}
