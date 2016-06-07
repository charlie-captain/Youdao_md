package com.example.administrator.dictionary.https;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/5/14.
 */
public class YouDaoJson {

    public  String webText;

    public  String AnalyzingOfJson(String result) throws Exception {

        String message = "";
        webText = "";
        JSONObject jsonObject = new JSONObject(result);
        if (jsonObject != null) {
            String errorCode = jsonObject.getString("errorCode");
            if (errorCode.equals("20")) {
                return "要翻译的文本过长";

            } else if (errorCode.equals("30 ")) {
                return "无法进行有效的翻译";

            } else if (errorCode.equals("40")) {
                return "不支持的语言类型";
            } else {
                if (jsonObject.has("translation")) {
                    String translation = jsonObject.getString("translation");
                    message += translation.replace("[", "").replace("]", "").replaceAll("\"", "");
                }
                if (jsonObject.has("basic")) {
                    JSONObject basic = jsonObject.getJSONObject("basic");
                    if (!basic.getString("explains").equals("")) {
                        String explains = basic.getString("explains");
                        webText+=explains.replace("[","").replace("]","").replaceAll("\"","").replace(",","\n")+"\n";
                    }

                }
                if (jsonObject.has("web")) {
                    JSONArray web = jsonObject.getJSONArray("web");
                    if (web.getJSONObject(0).has("value")) {
                        String value = web.getJSONObject(0).getString("value");
                        webText += value.replace("[", "").replace("]", "").replaceAll("\"", "").replace(",", ";  ") + "\n";

                    } else if (web.getJSONObject(1).has("key")) {
                        String key = web.getJSONObject(1).getString("key");
                        webText += key.replace("[", "").replace("]", "").replaceAll("\"", "").replace(",", ";  ");
                    }
                }
                Log.d("message", message);
                return message;
            }
        }

        return null;
    }
}
