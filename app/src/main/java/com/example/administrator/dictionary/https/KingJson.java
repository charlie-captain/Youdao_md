package com.example.administrator.dictionary.https;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/5/14.
 */
public class KingJson {


    private static String word;
    private static String ph_en = "";
    private static String ph_en_mp3 = "";
    private static String ph_am = "";
    private static String ph_am_mp3 = "";


    public String AnalyzingKingJson(final String result) {

        String message = "";
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray symbols_array = jsonObject.getJSONArray("symbols");
            JSONObject music = symbols_array.getJSONObject(0);
            getMusic(music);

            if (!jsonObject.has("word_name")) {
                return "没有本地释义";
            }

            JSONArray parts_array = symbols_array.getJSONObject(0).getJSONArray("parts");           //获得翻译

            for (int j = 0; j < parts_array.length(); j++) {

                JSONObject parts_obj = parts_array.getJSONObject(j);

                String part = parts_obj.getString("part");
                message += part;

                String means = parts_obj.getString("means");
                message += " " + means.replace("[", "").replace("]", "").replaceAll("\"", "").replace(",", ";  ") + "\n";

            }

            if (jsonObject.has("exchange")) {
                String exChange = "";
                JSONObject exchange = jsonObject.getJSONObject("exchange");

                if (!exchange.getString("word_pl").equals("")) {
                    String word_pl = "复数 " + exchange.getString("word_pl");
                    exChange += word_pl.replace("[", "").replace("]", "").replaceAll("\"", "") + " ";
                }
                if (!exchange.getString("word_third").equals("")) {
                    String word_third = "第三人称单数 " + exchange.getString("word_third");
                    exChange += word_third.replace("[", "").replace("]", "").replaceAll("\"", "") + " ";
                }
                if (!exchange.getString("word_past").equals("")) {
                    String word_past = "过去式 " + exchange.getString("word_past");
                    exChange += word_past.replace("[", "").replace("]", "").replaceAll("\"", "") + " ";
                }
                if (!exchange.getString("word_done").equals("")) {
                    String word_done = "过去分词 " + exchange.getString("word_done");
                    exChange += word_done.replace("[", "").replace("]", "").replaceAll("\"", "") + " ";
                }
                if (!exchange.getString("word_ing").equals("")) {
                    String word_ing = "现在分词 " + exchange.getString("word_ing");
                    exChange += word_ing.replace("[", "").replace("]", "").replaceAll("\"", "") + " ";
                }
                if (!exchange.getString("word_er").equals("")) {
                    String word_er = "比较级 " + exchange.getString("word_er");
                    exChange += word_er.replace("[", "").replace("]", "").replaceAll("\"", "") + " ";
                }
                if (!exchange.getString("word_est").equals("")) {
                    String word_est = "最高级 " + exchange.getString("word_est");
                    exChange += word_est.replace("[", "").replace("]", "").replaceAll("\"", "") + " ";
                }
                if (!exChange.equals("")) {
                    message += "[" + exChange + "]";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }


    public void getMusic(JSONObject music) {
        ph_en = "";
        ph_en_mp3 = "";
        ph_am = "";
        ph_am_mp3 = "";
        try {
            if (!music.getString("ph_am_mp3").equals("")) {
                if (!music.getString("ph_am").equals("")) {
                    ph_am += "英[" + music.getString("ph_am") + "]";
                } else {
                    ph_am = "英";
                }
                ph_am_mp3 = music.getString("ph_am_mp3");
            }
            if (!music.getString("ph_en_mp3").equals("")) {
                if (!music.getString("ph_en").equals("")) {
                    ph_en += "美[" + music.getString("ph_en") + "]";
                } else {
                    ph_en = "美";
                }
                ph_en_mp3 = music.getString("ph_en_mp3");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String getPh_en() {
        Log.d("json1", ph_en);
        return ph_en;
    }

    public static String getPh_en_mp3() {
        return ph_en_mp3;
    }

    public static String getPh_am() {
        return ph_am;
    }

    public static String getPh_am_mp3() {
        return ph_am_mp3;
    }


}
