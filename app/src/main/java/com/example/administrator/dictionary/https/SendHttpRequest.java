package com.example.administrator.dictionary.https;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.administrator.dictionary.config.ConfigFinal;
import com.example.administrator.dictionary.db.biz.DailySqliteHelper;
import com.example.administrator.dictionary.db.biz.SqliteHelper;
import com.example.administrator.dictionary.utils.HandleImage;
import com.example.administrator.dictionary.view.MainViewPager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2016/5/14.
 */
public class SendHttpRequest {

    /*
    * 翻译单词
    * */
    public  void sendHttpRequest(final String url, final Handler handler, final SQLiteDatabase db, final String word) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection httpURLConnection = null;
                try {
                    String response = "";
                    URL Url = new URL(url);
                    httpURLConnection = (HttpURLConnection) Url.openConnection();
                    httpURLConnection.setConnectTimeout(5000);
                    httpURLConnection.setReadTimeout(5000);
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    StringBuilder result = new StringBuilder();
                    String line = null;
                    while ((line = bufferedReader.readLine()) != null) {
                        result.append(line);
                    }
                    if (db != null) {
                        response = new KingJson().AnalyzingKingJson(result.toString());           //耗时
                        if (!response.equals("没有本地释义")) {
                            SqliteHelper.insert(ConfigFinal.TB_HISTORY, db, word, KingJson.getPh_en(), KingJson.getPh_en_mp3(), KingJson.getPh_am(), KingJson.getPh_am_mp3(), response);
                        }
                    } else {
                        YouDaoJson youDaoJson=new YouDaoJson();
                        response = youDaoJson.AnalyzingOfJson(result.toString());
                    }
                    Message message = new Message();
                    message.what = ConfigFinal.SHOW;
                    message.obj = response;
                    handler.sendMessage(message);
                    Log.d("what", result.toString());
                } catch (Exception e) {
                    handler.sendEmptyMessage(ConfigFinal.NOT_CONNECT);
                    e.printStackTrace();
                } finally {
                    if (httpURLConnection != null) {
                        httpURLConnection.disconnect();
                    }
                }
            }
        }).start();                     //忘记添加，bug

    }


    /*
    * 获取每日一句
    * */
    public static void sendHttpRequestImage(final String url, final Handler handler, final int year, final int month, final int day, final int mode) {

        new Thread(new Runnable() {

            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL Url = new URL(url);
                    connection = (HttpURLConnection) Url.openConnection();
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder result = new StringBuilder();
                    String line = "";
                    while ((line = bufferedReader.readLine()) != null) {
                        result.append(line);
                    }
                    ImageJson.getImageJson(result.toString());
                    if (mode == ConfigFinal.IMAGE) {
                        DailySqliteHelper.insert(MainViewPager.getDbSqlite(), ImageJson.getContent(), ImageJson.getNote(), ImageJson.getMp3(), year + "-" + month + "-" + day);
                    }
                    getImage(ImageJson.getPicture(), handler, year, month, day, mode);

                } catch (Exception e) {
                    handler.sendEmptyMessage(ConfigFinal.NOT_CONNECT);
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }


    /*
    * 获取图片
    * */
    public static void getImage(final String url, final Handler handler, final int year, final int month, final int day, final int mode) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    InputStream in = null;
                    URL Url = new URL(url);
                    connection = (HttpURLConnection) Url.openConnection();
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);
                    in = connection.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(in);
                    if (mode == ConfigFinal.IMAGE) {
                        HandleImage.saveImage(bitmap, year, month, day);
                    }
                    Message message = new Message();
                    message.what = ConfigFinal.IMAGE;
                    message.obj = bitmap;
                    handler.sendMessage(message);

                } catch (Exception e) {
                    handler.sendEmptyMessage(ConfigFinal.NOT_CONNECT);
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }


}
