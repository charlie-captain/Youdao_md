package com.example.administrator.dictionary.utils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.administrator.dictionary.config.ConfigFinal;
import com.example.administrator.dictionary.db.DbSqlite;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Administrator on 2016/5/19.
 */
public class HandleImage {

    private static String content = "";
    private static String note = "";
    private static String mp3 = "";
    private static Bitmap bitmap;


    public static void saveImage(final Bitmap bitmap, final int year, final int month, final int day) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    try {
                        File sdCard = Environment.getExternalStorageDirectory();
                        String path = sdCard.getCanonicalPath() + "/DCIM/Dictionary/";
                        File dir = new File(path);
                        if (!dir.exists()) {
                            dir.mkdir();
                        }
                        String fileName = path + year + "-" + month + "-" + day + ".jpg";
                        File file = new File(fileName);
                        FileOutputStream out = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                        out.flush();
                        out.close();
                        Log.d("sd", fileName);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }


    public static void getImageSentence(final DbSqlite dbSqlite, final Handler handler, final int year, final int month, final int day) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    try {
                        File sdCard = Environment.getExternalStorageDirectory();
                        String fileName = sdCard.getCanonicalPath() + "/DCIM/Dictionary/" + year + "-" + month + "-" + day + ".jpg";
                        File file = new File(fileName);


                        SQLiteDatabase db = dbSqlite.getWritableDatabase();
                        Cursor cursor = db.rawQuery("select * from " + ConfigFinal.TB_DAILY + " where time like ?", new String[]{year + "-" + month + "-" + day});
                        cursor.moveToNext();

                        content = cursor.getString(cursor.getColumnIndex("content"));
                        note = cursor.getString(cursor.getColumnIndex("note"));
                        mp3 = cursor.getString(cursor.getColumnIndex("mp3"));

                        if (file.exists()) {
                            bitmap = BitmapFactory.decodeFile(fileName);
                        }
                        Message message = new Message();
                        message.what = ConfigFinal.SD_IMAGE;
                        message.obj = bitmap;
                        handler.sendMessage(message);

                    } catch (Exception e) {
                        handler.sendEmptyMessage(ConfigFinal.EROOR);
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /*
    * 在卡里是否有图片
    * */
    public static boolean isBitmapHere(int year, int month, int day) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            try {
                File sdCard = Environment.getExternalStorageDirectory();
                String fileName = sdCard.getCanonicalPath() + "/DCIM/Dictionary/" + year + "-" + month + "-" + day + ".jpg";
                File file = new File(fileName);
                if (file.exists()) {
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    public static String getContent() {
        return content;
    }

    public static String getNote() {
        return note;
    }

    public static String getMp3() {
        return mp3;
    }
}
