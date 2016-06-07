package com.example.administrator.dictionary.base;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.administrator.dictionary.R;
import com.example.administrator.dictionary.adapter.ListbookViewAdapter;
import com.example.administrator.dictionary.anim.DethPageTransformer;
import com.example.administrator.dictionary.config.ConfigFinal;
import com.example.administrator.dictionary.entity.ReciteHolder;
import com.example.administrator.dictionary.utils.Media;
import com.example.administrator.dictionary.view.MainViewPager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ListbookReciteActivity extends Activity implements View.OnClickListener, ViewPager.OnPageChangeListener, DialogInterface.OnKeyListener {

    private int mposition = 0;
    private Set<String> words;
    private String[] colums, array;
    private List<View> listView;
    private List<ReciteHolder> holders;
    private ReciteHolder holder;
    private ArrayList<String> en_mp3, am_mp3, mp3, interpret;
    private ViewPager reciteWord;
    private ImageButton back;
    private SQLiteDatabase db;
    private Cursor cursor;
    private ListbookViewAdapter madapter;
    private AlertDialog alertDialog;
    private SharedPreferences getWordCounts;
    private SharedPreferences.Editor setWordCounts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listbook_reciteword);

        init();

    }

    private void init() {
        reciteWord = (ViewPager) findViewById(R.id.listbook_recite);
        back = (ImageButton) findViewById(R.id.listbook_recite_back);

        initEvent();
    }

    private void initEvent() {
        listView = new ArrayList<View>();
        holders= new ArrayList<ReciteHolder>();
        interpret = new ArrayList<String>();
        mp3 = new ArrayList<String>();
        en_mp3 = new ArrayList<String>();
        am_mp3 = new ArrayList<String>();
        words = new HashSet<String>();
        colums = new String[]{"word", "ph_en", "ph_en_mp3", "ph_am", "ph_am_mp3", "interpret"};
        back.setOnClickListener(this);

        db = MainViewPager.getDbSqlite().getWritableDatabase();
        cursor = db.rawQuery("select * from " + ConfigFinal.TB_LISTBOOK, null);

        getWordCounts = getSharedPreferences("wordCounts", MODE_PRIVATE);

        Calendar today = Calendar.getInstance();

        if (!getWordCounts.getString("date", "").
                equals(String.valueOf(today.get(Calendar.YEAR)) + String.valueOf(today.get(Calendar.MONTH) + 1) + String.valueOf(today.get(Calendar.DAY_OF_MONTH)))) {
            setWordCounts = getWordCounts.edit();
            setWordCounts.putStringSet("words", new HashSet<String>());
            setWordCounts.putString("date", String.valueOf(today.get(Calendar.YEAR)) + String.valueOf(today.get(Calendar.MONTH) + 1) + String.valueOf(today.get(Calendar.DAY_OF_MONTH)));
            setWordCounts.apply();
        }

        Log.d("date", today.get(Calendar.YEAR) + "" + String.valueOf(today.get(Calendar.MONTH) + 1) + "" + today.get(Calendar.DAY_OF_MONTH));
        showWordCounts(0);

        for (int i = 0; i < cursor.getCount(); i++) {
            getView(i);
        }
        
        reciteWord.setOffscreenPageLimit(2);

        reciteWord.setPageMargin(5);
        reciteWord.setPageTransformer(true, new DethPageTransformer());

        madapter = new ListbookViewAdapter(this, listView);
        reciteWord.setAdapter(madapter);
        reciteWord.addOnPageChangeListener(this);

        reciteWord.setCurrentItem(0);


    }

    private void showWordCounts(int mode) {
        alertDialog = new AlertDialog.Builder(this, R.style.ListbookDialog).create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setBackgroundDrawableResource(R.color.white);
        window.setContentView(R.layout.listbook_wordcounts);
        TextView lbCounts = (TextView) window.findViewById(R.id.listbook_wordcounts_counts);
        TextView lbRecite = (TextView) window.findViewById(R.id.listbook_wordcounts_recite);
        words = getWordCounts.getStringSet("words", new HashSet<String>());
        if (mode == 1) {
            lbRecite.setText("按返回键退出");
        } else {
            lbRecite.setOnClickListener(this);
            lbRecite.setText("开始背单词");
        }
        lbCounts.setText(words.size() + "个");
        alertDialog.setOnKeyListener(this);

    }


    private void getView(int i) {
        array = new String[6];
        View view = LayoutInflater.from(ListbookReciteActivity.this).inflate(R.layout.listbook_reciteword_item, null);
        ReciteHolder getViewHolder=new ReciteHolder(view);
        getViewHolder.getViewInit();
        cursor.moveToPosition(i);
        for (int j = 0; j < colums.length; j++) {
            array[j] = cursor.getString(cursor.getColumnIndex(colums[j]));
        }
        getViewHolder.word.setText(array[0]);


        if (!array[2].equals("")) {
            getViewHolder.english.setVisibility(View.VISIBLE);
            getViewHolder.enText.setText(array[1]);
        } else {
            getViewHolder.english.setVisibility(View.GONE);
        }
        if (!array[4].equals("")) {
            getViewHolder.amarican.setVisibility(View.VISIBLE);
            getViewHolder.amText.setText(array[3]);
        } else {
            getViewHolder.amarican.setVisibility(View.GONE);
        }
        if (getViewHolder.english.getVisibility() == View.VISIBLE
                && getViewHolder.amarican.getVisibility() == View.VISIBLE) {
            getViewHolder.englishBack.setVisibility(View.VISIBLE);
        } else {
            getViewHolder.englishBack.setVisibility(View.GONE);
        }

        en_mp3.add(array[2].equals("") ? "" : array[2]);
        am_mp3.add(array[4].equals("") ? "" : array[4]);
        mp3.add(array[2].equals("") ? array[4].equals("") ? "" : array[4] : array[2]);

        interpret.add(array[5]);

        listView.add(view);
        holders.add(getViewHolder);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.listbook_recite_ok:
                if (holder.reciteOk.getCurrentTextColor() != getResources().getColor(R.color.text_color)) {
                    holder.reciteOk.setTextColor(getResources().getColor(R.color.text_color));
                    words.add(holder.word.getText().toString());
                }
                if (mposition == listView.size() - 1) {
                    setWordCounts = getWordCounts.edit();
                    setWordCounts.putStringSet("words", words);
                    setWordCounts.apply();
                    showWordCounts(1);
                }
                reciteWord.setCurrentItem(reciteWord.getCurrentItem() + 1);
                break;
            case R.id.listbook_recite_no:
                holder.reciteNo.setTextColor(getResources().getColor(R.color.red));
                holder.interpret.setText(interpret.get(mposition));
                holder.interpret.setVisibility(View.VISIBLE);
                break;
            case R.id.listbook_recite_en_ly:
                Media.musicSound(en_mp3.get(mposition));
                break;
            case R.id.listbook_recite_am_ly:
                Media.musicSound(am_mp3.get(mposition));
                break;
            case R.id.listbook_wordcounts_recite:
                alertDialog.dismiss();
                Media.musicSound(mp3.get(0));
                holder=holders.get(0);
                holder.english.setOnClickListener(this);
                holder.amarican.setOnClickListener(this);
                holder.reciteOk.setOnClickListener(this);
                holder.reciteNo.setOnClickListener(this);
                holder.wordId.setText(1 + "/" + listView.size());
                break;
            case R.id.listbook_recite_back:
                showWordCounts(1);
                break;
        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Media.musicSound(mp3.get(position));
        mposition = position;
        holder=holders.get(position);
        Log.d("what", holder.word.getText().toString());
        holder.wordId.setText((position + 1) + "/" + listView.size());

        holder.interpret.setVisibility(View.GONE);
        holder.english.setOnClickListener(this);
        holder.amarican.setOnClickListener(this);
        holder.reciteOk.setOnClickListener(this);
        holder.reciteNo.setOnClickListener(this);

    }

    @Override
    public void onPageScrollStateChanged(int state) {


    }


    @Override
    public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
        if (i == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return true;
    }


    
}
