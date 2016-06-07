package com.example.administrator.dictionary.db.biz;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Set;

/**
 * Created by Administrator on 2016/5/14.
 */
public class SqliteHelper {


    public static void insert(String tableName, SQLiteDatabase db, String word, String ph_en, String ph_en_mp3,
                              String ph_am, String ph_am_mp3, String interpret) {               //用来判断插入的数据是否重复-->可优化

        Cursor cursor = db.rawQuery("select * from " + tableName, null);

        db.beginTransaction();
        try {

            if (cursor.getCount() > 0) {
                 cursor = db.rawQuery("select * from " + tableName + " where word like ?", new String[]{word});

                if (cursor.getCount() > 0) {
                    db.execSQL("delete from " + tableName + " where word like  ?", new String[]{word});
                }
            }
            db.execSQL("insert into " + tableName + "(word,ph_en,ph_en_mp3,ph_am,ph_am_mp3,interpret) values(?,?,?,?,?,?)",     //插入
                    new Object[]{word, ph_en, ph_en_mp3, ph_am, ph_am_mp3, interpret});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }

    }

    public static void delete(String tableName, SQLiteDatabase db, String word) {
        //Cursor cursor=queryTheWord(tableName,db,word);
        db.beginTransaction();
        try {
            db.execSQL("delete from " + tableName + " where word like ?", new String[]{word});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }

    }

    public static void deleteMore(String tableName, SQLiteDatabase db, Set<Integer> selectPositon) {

        Cursor cursor = db.rawQuery("select * from " + tableName, null);
        db.beginTransaction();
        try {
            for (int i : selectPositon) {
                cursor.moveToPosition(cursor.getCount() - 1 - i);
                db.execSQL("delete from " + tableName + " where word like ?", new String[]{cursor.getString(cursor.getColumnIndex("word"))});
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.endTransaction();
    }


    public static Cursor queryTheWord(String tableName, SQLiteDatabase db, String word) {

        Cursor cursor = db.rawQuery("select * from " + tableName + " where word like ?", new String[]{word});
        if (cursor.getCount() > 0) {
            cursor.moveToNext();
            return cursor;
        }
        return null;
    }


}
