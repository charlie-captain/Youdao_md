package com.example.administrator.dictionary.db.biz;

import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;

import com.example.administrator.dictionary.config.ConfigFinal;
import com.example.administrator.dictionary.https.KingJson;

/**
 * Created by Administrator on 2016/5/21.
 */
public class ListbookSqliteHelper {


    public static void isSelect(SQLiteDatabase db, Handler handler, String word, String[] array, String response,int sqlORjson) {
        db.beginTransaction();
        try {
            if (sqlORjson==ConfigFinal.JSON)
                SqliteHelper.insert("listbook", db, word, KingJson.getPh_en(), KingJson.getPh_en_mp3(), KingJson.getPh_am(), KingJson.getPh_am_mp3(), response);
            else {
                SqliteHelper.insert("listbook", db, word, array[1], array[2], array[3], array[4], array[5]);
            }
            Message message = new Message();
            message.what = ConfigFinal.ADD_LISTBOOK;
            message.obj = ConfigFinal.SELECT;
            handler.sendMessage(message);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Message message = new Message();
            message.obj = ConfigFinal.NO_SUCCESS;
            handler.sendMessage(message);
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public static void noSelect(SQLiteDatabase db, Handler handler, String word) {
        db.beginTransaction();
        try {
            SqliteHelper.delete(ConfigFinal.TB_LISTBOOK, db, word);
            Message message = new Message();
            message.what = ConfigFinal.ADD_LISTBOOK;
            message.obj = ConfigFinal.NO_SELECT;
            handler.sendMessage(message);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }


}
