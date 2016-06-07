package com.example.administrator.dictionary.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.dictionary.R;
import com.example.administrator.dictionary.adapter.AllAdapter;
import com.example.administrator.dictionary.config.ConfigFinal;
import com.example.administrator.dictionary.db.DbSqlite;
import com.example.administrator.dictionary.db.biz.ListbookSqliteHelper;
import com.example.administrator.dictionary.db.biz.SqliteHelper;
import com.example.administrator.dictionary.https.KingJson;
import com.example.administrator.dictionary.https.SendHttpRequest;
import com.example.administrator.dictionary.utils.Media;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Administrator on 2016/5/17.
 */
public class NotificationSearch extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, View.OnKeyListener {

    private EditText editText;
    private ImageButton button_search, edit_clear, listbookAdd;
    private TextView wordOutput, wordBig, english_text, american_text, english_text_back;
    private LinearLayout english, american;
    private String word;
    private String response;
    private String[] colums;
    private String[] array;

    private LinearLayout linearLayout;
    private RelativeLayout actionBar;
    private ImageButton actionBarCancel, actionBarDelete;
    private TextView actionBarAll, actionBarId;

    private static int sqlORjson = ConfigFinal.JSON;
    private static int search = ConfigFinal.NOT_SEARCH;
    public static int MODE = ConfigFinal.HISTORY_SINGLE;
    private static Set<Integer> selectionHistory;

    private DbSqlite dbSqlite;
    private SQLiteDatabase db;
    private static ListView lv;
    private static AllAdapter cursorAdapter;
    private Cursor cursor;
    private Media media;
    private SendHttpRequest jsonKing;
    private InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstancestate) {
        super.onCreate(savedInstancestate);
        Intent intent = getIntent();
        if (intent.getStringExtra("theme") != null) {
            if (intent.getStringExtra("theme").equals("main")) {
                this.setTheme(R.style.ImageTranslucentTheme);
            }
        }
        setContentView(R.layout.word_search);
        init();
    }


    private void init() {

        editText = (EditText) findViewById(R.id.word_edit);

        wordBig = (TextView) findViewById(R.id.word_big);
        wordOutput = (TextView) findViewById(R.id.word_output);
        english_text = (TextView) findViewById(R.id.english_text);
        american_text = (TextView) findViewById(R.id.american_text);
        english_text_back = (TextView) findViewById(R.id.english_text_back);
        actionBarId = (TextView) findViewById(R.id.wordsearch_actionbar_id);

        button_search = (ImageButton) findViewById(R.id.word_search_btn_search);
        edit_clear = (ImageButton) findViewById(R.id.word_clear_edit);
        listbookAdd = (ImageButton) findViewById(R.id.listbook_add);

        english = (LinearLayout) findViewById(R.id.en_ly);
        american = (LinearLayout) findViewById(R.id.am_ly);
        linearLayout = (LinearLayout) findViewById(R.id.edit_search);

        actionBar = (RelativeLayout) findViewById(R.id.wordsearch_actionbar);

        imm = (InputMethodManager) NotificationSearch.this.getSystemService(Context.INPUT_METHOD_SERVICE);

        lv = (ListView) findViewById(R.id.word_list_view);

        initEvent();

    }

    private void initEvent() {
        jsonKing=new SendHttpRequest();

        colums = new String[]{"word", "ph_en", "ph_en_mp3", "ph_am", "ph_am_mp3", "interpret"};
        dbSqlite = new DbSqlite(this, ConfigFinal.SQL_NAME, null, ConfigFinal.SQL_VERSION);
        db = dbSqlite.getWritableDatabase();
        Query();

        media=new Media();

        editText.addTextChangedListener(textWatcher);          //监听EditText的文本输入

        button_search.setOnClickListener(this);
        english.setOnClickListener(this);
        american.setOnClickListener(this);
        listbookAdd.setOnClickListener(this);
        edit_clear.setOnClickListener(this);

        editText.setOnKeyListener(this);                     //监听按钮，回车改为隐藏输入法和按钮

        lv.setOnItemClickListener(this);


        lv.setOnItemLongClickListener(this);
    }


    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ConfigFinal.SHOW:
                    try {
                        response = msg.obj.toString();
                        wordBig.setText(word);
                        showImage(word, KingJson.getPh_en(), KingJson.getPh_en_mp3(), KingJson.getPh_am(), KingJson.getPh_am_mp3());
                        if (!response.equals("没有本地释义")){
                            listbookAdd.setVisibility(View.VISIBLE);
                        }else{
                            listbookAdd.setVisibility(View.INVISIBLE);
                        }
                        wordOutput.setText(response);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case ConfigFinal.SQL_MESSAGE:
                    String response = msg.obj.toString();
                    wordBig.setText(word);
                    listbookAdd.setVisibility(View.VISIBLE);
                    showImage(word, array[1], array[2], array[3], array[4]);
                    wordOutput.setText(response);
                    break;
                case ConfigFinal.ADD_LISTBOOK:
                    if (msg.obj.toString().equals(ConfigFinal.SELECT)) {
                        listbookAdd.setSelected(true);
                        Toast.makeText(NotificationSearch.this, "添加到单词本成功", Toast.LENGTH_SHORT).show();
                    } else if (msg.obj.toString().equals(ConfigFinal.NO_SELECT)) {
                        listbookAdd.setSelected(false);
                        Toast.makeText(NotificationSearch.this, "删除成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(NotificationSearch.this, "添加失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case ConfigFinal.NOT_CONNECT:
                    listbookAdd.setVisibility(View.INVISIBLE);
                    wordOutput.setText("网络未连接， 请检查网络设置");
                    break;
            }
        }
    };

    private TextWatcher textWatcher = new TextWatcher() {                       //监听文本输入
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            lv.setVisibility(View.VISIBLE);
            String findWord = editText.getText().toString();
            if (!findWord.equals(word)) {                                       //模糊搜索
                cursor = db.query("history", new String[]{"word", "interpret"}, "word like ?", new String[]{"%" + findWord + "%"}, null, null, "word asc");
                cursorAdapter = new AllAdapter(NotificationSearch.this, cursor);
                search = ConfigFinal.SEARCH;
                lv.setAdapter(cursorAdapter);
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {                         //更新listview--->可优化
            if (editText.length() == 0) {
                Query();
                lv.setVisibility(View.VISIBLE);
                edit_clear.setVisibility(View.INVISIBLE);
                search = ConfigFinal.NOT_SEARCH;
            }
            if (!editText.getText().toString().equals("")) {
                edit_clear.setVisibility(View.VISIBLE);
            } else {
                wordBig.setText("");
                wordOutput.setText("");
                english.setVisibility(View.GONE);
                english_text_back.setVisibility(View.GONE);
                american.setVisibility(View.GONE);

                editText.setFocusable(true);
                editText.setFocusableInTouchMode(true);
                editText.requestFocus();                            //EditText获取焦点
                imm.showSoftInput(editText, 0);
            }


        }
    };


    public void Query() {                                   //查询数据库
        cursor = db.query(ConfigFinal.TB_HISTORY, new String[]{"id", "word", "interpret"}, null, null, null, null, "id desc");
        cursorAdapter = new AllAdapter(NotificationSearch.this, cursor);
        lv.setAdapter(cursorAdapter);
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.word_search_btn_search:
                searchTheWord();
                hideInputSoft();
                break;
            case R.id.en_ly:
                if (sqlORjson == ConfigFinal.JSON) {
                    Media.musicSound(KingJson.getPh_en_mp3());        //如果数据库的为空，就联网
                    Log.d("what", KingJson.getPh_en_mp3());
                } else {
                    Media.musicSound(array[2]);
                }
                break;
            case R.id.am_ly:
                if (sqlORjson == ConfigFinal.JSON) {
                    Media.musicSound(KingJson.getPh_am_mp3());
                } else {
                    Media.musicSound(array[4]);
                }
                break;
            case R.id.word_clear_edit:
                editText.setText("");
                break;
            case R.id.listbook_add:
                hand_ListBook(word);
                break;
        }
    }


    /*
    * 先查询历史数据库，如果没有就联网搜词
    * */
    private void searchTheWord() {
        word = editText.getText().toString().toLowerCase().trim();
        if (!"".equals(word.trim())) {
            lv.setVisibility(View.GONE);                //设置listview消失
            final String url = ConfigFinal.KING_BASE + "w=" + word + "&key=" + ConfigFinal.KING_KEY + "&type=" + ConfigFinal.KING_TYPE;
            cursor = SqliteHelper.queryTheWord(ConfigFinal.TB_HISTORY, db, word);
            if (cursor != null) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        sqlORjson = ConfigFinal.SQL;
                        array = new String[]{"", "", "", "", "", ""};
                        for (int i = 0; i < colums.length; i++) {
                            array[i] = cursor.getString(cursor.getColumnIndex(colums[i]));
                        }
                        SqliteHelper.insert(ConfigFinal.TB_HISTORY, db, word, array[1], array[2], array[3], array[4], array[5]);
                        Message sqlMessage = new Message();
                        sqlMessage.what = ConfigFinal.SQL_MESSAGE;
                        sqlMessage.obj = array[5];                                                       //传递interpret
                        handler.sendMessage(sqlMessage);
                    }
                }).start();
            } else {
                sqlORjson = ConfigFinal.JSON;
               jsonKing.sendHttpRequest(url, handler, db, word);
            }
        }else {
            Toast.makeText(NotificationSearch.this, "输入不能为空哦!", Toast.LENGTH_SHORT).show();
        }

    }

    /*
    * 隐藏输入法
    * */
    public void hideInputSoft() {
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(editText.getApplicationWindowToken(), 0);
        }
    }

    /*
    * 加入单词本
    * */
    public void hand_ListBook(final String word) {

        if (!listbookAdd.isSelected()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ListbookSqliteHelper.isSelect(db, handler, word, array, response, sqlORjson);
                }
            }).start();
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ListbookSqliteHelper.noSelect(db, handler, word);
                }
            }).start();

        }
    }


    /*
    * 显示音频
    * */
    public void showImage(String word, String ph_en, String ph_en_mp3, String ph_am, String ph_am_mp3) {

        if (!ph_en_mp3.equals("")) {
            english_text.setText(ph_en);
            english.setVisibility(View.VISIBLE);
        } else {
            english_text.setText("");
            english.setVisibility(View.GONE);
        }

        if (!ph_am_mp3.equals("")) {
            american_text.setText(ph_am);
            american.setVisibility(View.VISIBLE);
        } else {
            american_text.setText("");
            american.setVisibility(View.GONE);
        }
        if (american.getVisibility() == View.VISIBLE && english.getVisibility() == View.VISIBLE) {
            english_text_back.setVisibility(View.VISIBLE);
        } else {
            english_text_back.setVisibility(View.GONE);
        }

        if (SqliteHelper.queryTheWord(ConfigFinal.TB_LISTBOOK, db, word) != null) {
            listbookAdd.setSelected(true);
        } else {
            listbookAdd.setSelected(false);
        }

    }


    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if (i == KeyEvent.KEYCODE_ENTER) {
            button_search.performClick();
        }
        return false;
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

        /*判断是否多选模式
        *
        * */
        if (MODE == ConfigFinal.HISTORY_MODE) {
            if (!selectionHistory.contains(position)) {
                selectionHistory.add(position);
            } else {
                selectionHistory.remove(position);
            }
            if (selectionHistory.isEmpty()) {
                MODE = ConfigFinal.HISTORY_SINGLE;
                modeHideImage();
            }
            actionBarId.setText("已选择 " + selectionHistory.size() + " 项");
            cursorAdapter.setSelection(selectionHistory, MODE);
            cursorAdapter.notifyDataSetChanged();
        } else {
            if (search == ConfigFinal.SEARCH) {                                                //模糊搜索提取数据库
                cursor.moveToPosition(position);
                String edittext_word = cursor.getString(cursor.getColumnIndex("word"));
                editText.setText(edittext_word);
                editText.setSelection(edittext_word.length());      //设置光标到尾部
            } else {
                Cursor cur = db.rawQuery("select * from history", null);
                cur.moveToPosition(cur.getCount() - position - 1);
                String edittext_word = cur.getString(cur.getColumnIndex("word"));
                editText.setText(edittext_word);
                editText.setSelection(edittext_word.length());      //设置光标到尾部
            }

            button_search.performClick();
        }
    }

    /*
    * 长按进入多选模式
    * */
    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
        MODE = ConfigFinal.HISTORY_MODE;
        modeHideImage();
        selectionHistory = new HashSet<Integer>();
        selectionHistory.add(position);
        actionBarId.setText("已选择 " + selectionHistory.size() + " 项");
        cursorAdapter.setSelection(selectionHistory, MODE);
        cursorAdapter.notifyDataSetChanged();
        return true;
    }


    public void modeHideImage() {
        if (MODE == ConfigFinal.HISTORY_MODE) {
            actionBar.setVisibility(View.VISIBLE);
            actionBarCancel = (ImageButton) actionBar.findViewById(R.id.wordsearch_actionbar_cancel);
            actionBarAll = (TextView) actionBar.findViewById(R.id.wordsearch_actionbar_all);
            actionBarDelete = (ImageButton) actionBar.findViewById(R.id.wordsearch_actionbar_delete);
            actionBarId = (TextView) actionBar.findViewById(R.id.wordsearch_actionbar_id);

            actionBarCancel.setOnClickListener(new actionBarClick());
            actionBarDelete.setOnClickListener(new actionBarClick());
            actionBarAll.setOnClickListener(new actionBarClick());
        } else {
            Query();
            actionBar.setVisibility(View.GONE);
        }
    }


    public class actionBarClick implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.wordsearch_actionbar_cancel:
                    MODE = ConfigFinal.HISTORY_SINGLE;
                    modeHideImage();
                    break;
                case R.id.wordsearch_actionbar_all:
                    if (selectionHistory.size() < lv.getCount()) {
                        for (int i = 0; i < lv.getCount(); i++) {
                            selectionHistory.add(i);
                        }
                        actionBarId.setText("已选择 " + selectionHistory.size() + " 项");
                        cursorAdapter.notifyDataSetChanged();
                    }
                    break;
                case R.id.wordsearch_actionbar_delete:
                    SqliteHelper.deleteMore(ConfigFinal.TB_HISTORY, db, selectionHistory);
                    MODE = ConfigFinal.HISTORY_SINGLE;
                    modeHideImage();
                    break;

            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }


}
