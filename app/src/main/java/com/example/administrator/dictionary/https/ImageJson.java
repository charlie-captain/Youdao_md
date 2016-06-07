package com.example.administrator.dictionary.https;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016/5/17.
 */
public class ImageJson {

    private static String content = "";
    private static String note = "";
    private static String picture = "";
    private static String mp3="";

    public static void getImageJson(String result) {
        try {

            JSONObject jsonObject = new JSONObject(result);

            mp3=jsonObject.getString("tts");
            content = jsonObject.getString("content");
            note = jsonObject.getString("note");
            picture = jsonObject.getString("picture2");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getContent() {
        return content;
    }

    public static String getNote() {
        return note;
    }

    public static String getPicture() {
        return picture;
    }

    public static String getMp3(){
        return mp3;
    }

}
