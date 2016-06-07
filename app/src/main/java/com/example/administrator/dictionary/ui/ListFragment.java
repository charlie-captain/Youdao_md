package com.example.administrator.dictionary.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.dictionary.R;
import com.example.administrator.dictionary.adapter.AllAdapter;
import com.example.administrator.dictionary.base.ListbookReciteActivity;
import com.example.administrator.dictionary.config.ConfigFinal;
import com.example.administrator.dictionary.db.biz.SqliteHelper;
import com.example.administrator.dictionary.entity.ListbookWord;
import com.example.administrator.dictionary.utils.Media;
import com.example.administrator.dictionary.view.MainViewPager;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Administrator on 2016/5/5.
 */
public class ListFragment extends Fragment implements AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener, View.OnClickListener {

    public static int MODE = ConfigFinal.LISTBOOK_SINGLE;
    public static int QUERY = ConfigFinal.NOMAL;
    private String[] cursorGet;
    private String[] meas;
    private static Set<Integer> selectPositon;
    private View rootView;
    private ListView lv;
    private ImageButton listbookDelete, listbookBack;
    private TextView listbookAll, listbookSelect, listbookRecite, listbookHide;
    private LinearLayout pop_nomal, pop_character, listbookLy, listbookNoWord;
    private PopupWindow mpopupWindow = null;
    private AllAdapter listBookAdapter;
    private SQLiteDatabase db;
    private static Cursor cursor;
    private RelativeLayout actionBar, listBookQuery;
    private SharedPreferences findQuery;
    private SharedPreferences.Editor editor;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.listbook, container, false);
        init(rootView);
        return rootView;
    }

    private void init(View view) {
        lv = (ListView) view.findViewById(R.id.list_book);
        listbookSelect = (TextView) view.findViewById(R.id.list_book_actionbar_id);
        listbookRecite = (TextView) view.findViewById(R.id.listbook_recite);
        listbookHide = (TextView) view.findViewById(R.id.listbook_hide);
        listbookAll = (TextView) view.findViewById(R.id.list_book_actionbar_all);
        listbookDelete = (ImageButton) view.findViewById(R.id.list_book_actionbar_delete);
        listbookBack = (ImageButton) view.findViewById(R.id.list_book_actionbar_cancel);
        listbookLy = (LinearLayout) view.findViewById(R.id.listbook_ly);
        listbookNoWord = (LinearLayout) view.findViewById(R.id.listbook_noword);
        actionBar = (RelativeLayout) view.findViewById(R.id.list_book_actionbar);
        listBookQuery = (RelativeLayout) view.findViewById(R.id.list_book_query);
        initEvent();
    }

    private void initEvent() {
        cursorGet = new String[]{"word", "ph_en", "ph_en_mp3", "ph_am", "ph_am_mp3", "interpret"};
        db = MainViewPager.getDbSqlite().getWritableDatabase();                                          //打开输入法
        findQuery = MainViewPager.getContext().getSharedPreferences("query_data", 0);                    //获取排序配置
        QUERY = findQuery.getInt("query", 0);
        isNomal(QUERY);

        lv.setOnItemLongClickListener(this);
        lv.setOnItemClickListener(this);
        listbookBack.setOnClickListener(this);
        listbookAll.setOnClickListener(this);
        listbookDelete.setOnClickListener(this);
        listBookQuery.setOnClickListener(this);
        listbookRecite.setOnClickListener(this);
        listbookHide.setOnClickListener(this);
    }

    /*
    * 判断排序方式
    * */
    private void isNomal(int QUERY) {
        if (QUERY == ConfigFinal.NOMAL) {
            query();
        } else {
            queryCharacter();
        }
        if (cursor.getCount() != 0) {
            listbookNoWord.setVisibility(View.GONE);
        } else {
            listbookNoWord.setVisibility(View.VISIBLE);
        }
    }


    /*
    * 默认排序
    * */
    public void query() {
        cursor = db.query("listbook", new String[]{"id", "word", "ph_en", "ph_en_mp3", "ph_am", "ph_am_mp3", "interpret"}, null, null, null, null, "id desc");
        listBookAdapter = new AllAdapter(MainViewPager.getContext(), cursor);
        lv.setAdapter(listBookAdapter);

    }

    /*
    * 字母排序
    * */
    public void queryCharacter() {
        cursor = db.query(ConfigFinal.TB_LISTBOOK, new String[]{"word", "ph_en", "ph_en_mp3", "ph_am", "ph_am_mp3", "interpret"}, null, null, null, null, "word asc");
        listBookAdapter = new AllAdapter(MainViewPager.getContext(), cursor);
        lv.setAdapter(listBookAdapter);
    }


    /*
    * 启动多选模式
    * */
    public void hideImage() {
        if (MODE == ConfigFinal.LISTBOOK_MODE) {
            actionBar.setVisibility(View.VISIBLE);
            listBookAdapter.notifyDataSetChanged();
        } else {
            query();
            actionBar.setVisibility(View.GONE);
            selectPositon.clear();
        }
    }

    /*
    * 长按事件
    * */
    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
        MODE = ConfigFinal.LISTBOOK_MODE;
        selectPositon = new HashSet<Integer>();                         //存储选择的位置
        hideImage();                                                    //隐藏顶布局
        selectPositon.add(position);
        listbookSelect.setText("已选择 " + selectPositon.size() + " 项");
        listBookAdapter.setSelection(selectPositon, MODE);
        listBookAdapter.notifyDataSetChanged();
        return true;
    }


    /*
    * 短按事件
    * */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        if (MODE == ConfigFinal.LISTBOOK_MODE) {
            if (!selectPositon.contains(position)) {
                selectPositon.add(position);

            } else {
                selectPositon.remove(position);

            }
            if (selectPositon.isEmpty()) {
                MODE = ConfigFinal.LISTBOOK_SINGLE;
                hideImage();
            }
            listBookAdapter.setSelection(selectPositon, MODE);
            listBookAdapter.notifyDataSetChanged();
        } else {
            popupAlerDiaolog(position);
        }


    }


    /*
    * 显示单词--->AlertDialog
    * */
    private void popupAlerDiaolog(int position) {
        meas = new String[6];
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity(), R.style.ListbookDialog).create();
        alertDialog.show();
        Window window = alertDialog.getWindow();
        ListbookWord listbookWord = new ListbookWord(window);
        cursor.moveToPosition(position);
        for (int i = 0; i < cursorGet.length; i++) {
            meas[i] = cursor.getString(cursor.getColumnIndex(cursorGet[i]));
        }
        listbookWord.word.setText(meas[0]);
        if (!meas[2].equals("")) {
            listbookWord.en_ly.setVisibility(View.VISIBLE);
            listbookWord.en_text.setText(meas[1]);
        } else {
            listbookWord.en_ly.setVisibility(View.GONE);
        }
        if (!meas[4].equals("")) {
            listbookWord.am_ly.setVisibility(View.VISIBLE);
            listbookWord.am_text.setText(meas[3]);
        } else {
            listbookWord.am_ly.setVisibility(View.GONE);
        }
        if (listbookWord.en_ly.getVisibility() == View.VISIBLE &&
                listbookWord.am_ly.getVisibility() == View.VISIBLE) {
            listbookWord.en_textBack.setVisibility(View.VISIBLE);
        } else {
            listbookWord.en_textBack.setVisibility(View.GONE);
        }

        listbookWord.interpret.setText(meas[5]);

        listbookWord.en_ly.setOnClickListener(this);
        listbookWord.am_ly.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.list_book_actionbar_delete:                   //删除按钮
                SqliteHelper.deleteMore(ConfigFinal.TB_LISTBOOK, db, selectPositon);
                MODE = ConfigFinal.LISTBOOK_SINGLE;
                hideImage();
                isNomal(QUERY);
                break;
            case R.id.list_book_actionbar_all:                      //全选按钮
                if (selectPositon.size() < lv.getCount()) {
                    for (int i = 0; i < lv.getCount(); i++) {
                        selectPositon.add(i);
                    }
                    listBookAdapter.notifyDataSetChanged();
                }
                break;
            case R.id.list_book_actionbar_cancel:                   //返回按钮
                MODE = ConfigFinal.LISTBOOK_SINGLE;
                hideImage();
                break;
            case R.id.list_book_query:                              //排序按钮
                showPopupWindow();
                break;
            case R.id.listbook_recite:                              //背单词按钮
                if (cursor.getCount() > 0) {
                    Intent intent = new Intent(MainViewPager.getContext(), ListbookReciteActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainViewPager.getContext(), "还没有单词呢,赶紧添加吧", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.listbook_hide:                                //隐藏释义按钮
                if (listBookAdapter.getMode() == ConfigFinal.HIDE) {
                    listBookAdapter.setSelection(null, ConfigFinal.NOT_HIDE);
                } else {
                    listBookAdapter.setSelection(null, ConfigFinal.HIDE);
                }
                lv.setAdapter(listBookAdapter);
                break;
            case R.id.listbook_word_en_ly:                          //英语发音
                Media.musicSound(meas[2]);
                break;
            case R.id.listbook_word_am_ly:                          //美语发音
                Media.musicSound(meas[4]);
                break;
        }
    }


    /*
    * 显示弹出窗口
    * */
    private void showPopupWindow() {
        View contentView = LayoutInflater.from(MainViewPager.getContext()).inflate(R.layout.listbook_popupwindow, null);
        mpopupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mpopupWindow.setOutsideTouchable(true);
        mpopupWindow.setBackgroundDrawable(new BitmapDrawable());
        editor = findQuery.edit();                                                      //存入排序方式
        mpopupWindow.showAsDropDown(listBookQuery);
        pop_nomal = (LinearLayout) contentView.findViewById(R.id.pop_nomal_ly);
        pop_character = (LinearLayout) contentView.findViewById(R.id.pop_character_ly);
        pop_nomal.setOnClickListener(new popOnClick());
        pop_character.setOnClickListener(new popOnClick());
    }


    /*
    * PopupWindow点击事件
    */
    public class popOnClick implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.pop_nomal_ly:             //默认排序
                    QUERY = ConfigFinal.NOMAL;
                    query();
                    editor.putInt("query", QUERY);
                    editor.commit();
                    mpopupWindow.dismiss();
                    break;
                case R.id.pop_character_ly:         //字母排序
                    QUERY = ConfigFinal.CHAR;
                    queryCharacter();
                    editor.putInt("query", QUERY);
                    editor.commit();
                    mpopupWindow.dismiss();
                    break;

            }
        }
    }


}