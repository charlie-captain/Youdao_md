package com.example.administrator.dictionary.view;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.dictionary.R;
import com.example.administrator.dictionary.adapter.FramAdapter;
import com.example.administrator.dictionary.db.DbSqlite;
import com.example.administrator.dictionary.ui.ListFragment;
import com.example.administrator.dictionary.ui.MainFragment;
import com.example.administrator.dictionary.ui.SentenceFragment;
import com.example.administrator.dictionary.ui.SettingFragment;
import com.example.administrator.dictionary.utils.Media;

import java.util.ArrayList;
import java.util.List;

public class MainViewPager extends FragmentActivity implements View.OnClickListener {
    private android.support.v4.view.ViewPager viewpager;
    private View mview;
    private FramAdapter fm;
    private List<Fragment> mFragments;//适配器的数据源
    private Fragment out_main, out_sentence, out_list, out_setting;
    private LinearLayout ly_main, ly_sentence, ly_list, ly_setting;
    private TextView textView_word, textView_sentence, textView_list, textView_setting;
    private TextView color_word, color_sentence, color_list, color_setting;
    private static Context mcontext;
    private static DbSqlite dbSqlite;
    private InputMethodManager imm;
    private Media media;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_viewpager);
        mcontext = getApplication();
        init();
        intEvent();
        setSelect(0);
    }

    public static Context getContext() {
        return mcontext;
    }


    private void intEvent() {
        ly_main.setOnClickListener(this);
        ly_sentence.setOnClickListener(this);
        ly_list.setOnClickListener(this);
        ly_setting.setOnClickListener(this);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        media=new Media();

        dbSqlite = new DbSqlite(this, "Dictionary", null, 1);     //打开数据库

    }

    /*
    * 初始化控件
    * */
    public void init() {
        ly_main = (LinearLayout) findViewById(R.id.viewpager_word);
        ly_sentence = (LinearLayout) findViewById(R.id.viewpager_sentence);
        ly_list = (LinearLayout) findViewById(R.id.viewpager_listbook);
        ly_setting = (LinearLayout) findViewById(R.id.viewpager_settings);


        textView_word = (TextView) findViewById(R.id.viewpager_text1_bottom);
        textView_sentence = (TextView) findViewById(R.id.viewpager_text2_bottom);
        textView_list = (TextView) findViewById(R.id.viewpager_text3_bottom);
        textView_setting = (TextView) findViewById(R.id.viewpager_text4_bottom);

        color_word = (TextView) findViewById(R.id.viewpager_text1);
        color_sentence = (TextView) findViewById(R.id.viewpager_text2);
        color_list = (TextView) findViewById(R.id.viewpager_text3);
        color_setting = (TextView) findViewById(R.id.viewpager_text4);


        viewpager = (android.support.v4.view.ViewPager) findViewById(R.id.viewpager);
        mFragments = new ArrayList<Fragment>();
        mview = LayoutInflater.from(this).inflate(R.layout.main_viewpager, null);

        out_main = new MainFragment();
        out_sentence = new SentenceFragment();
        out_list = new ListFragment();
        out_setting = new SettingFragment();

        mFragments.add(out_main);
        mFragments.add(out_sentence);
        mFragments.add(out_list);
        mFragments.add(out_setting);

        fm = new FramAdapter(getSupportFragmentManager(), mFragments);
        viewpager.setAdapter(fm);
        viewpager.addOnPageChangeListener(new android.support.v4.view.ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int Current = viewpager.getCurrentItem();
                tab(Current);
            }

            @Override
            public void onPageSelected(int position) {
                hideInputSoft();
                Media.setMediaStop();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        reSet();
        switch (v.getId()) {
            case R.id.viewpager_word:
                setSelect(0);
                break;
            case R.id.viewpager_sentence:
                setSelect(1);
                break;
            case R.id.viewpager_listbook:
                setSelect(2);
                break;
            case R.id.viewpager_settings:
                setSelect(3);
                break;
        }

    }

    /*
    *设置选中
     *  */
    public void setSelect(int i) {
        reSet();
        switch (i) {
            case 0:
                color_word.setTextColor(Color.WHITE);
                textView_word.setBackgroundColor(Color.RED);
                break;
            case 1:
                color_sentence.setTextColor(Color.WHITE);
                textView_sentence.setBackgroundColor(Color.RED);
                break;
            case 2:
                color_list.setTextColor(Color.WHITE);
                textView_list.setBackgroundColor(Color.RED);
                break;
            case 3:
                color_setting.setTextColor(Color.WHITE);
                textView_setting.setBackgroundColor(Color.RED);
                break;
        }
        viewpager.setCurrentItem(i);

    }

    /*
    * 重置
    * */
    public void reSet() {
        color_word.setTextColor(getResources().getColor(R.color.gray));
        color_sentence.setTextColor(getResources().getColor(R.color.gray));
        color_list.setTextColor(getResources().getColor(R.color.gray));
        color_setting.setTextColor(getResources().getColor(R.color.gray));


        textView_word.setBackgroundColor(getResources().getColor(R.color.text_bottom));
        textView_sentence.setBackgroundColor(getResources().getColor(R.color.text_bottom));
        textView_list.setBackgroundColor(getResources().getColor(R.color.text_bottom));
        textView_setting.setBackgroundColor(getResources().getColor(R.color.text_bottom));
    }


    public void tab(int i) {
        reSet();
        switch (i) {
            case 0:
                color_word.setTextColor(Color.WHITE);
                textView_word.setBackgroundColor(Color.RED);
                break;
            case 1:
                color_sentence.setTextColor(Color.WHITE);
                textView_sentence.setBackgroundColor(Color.RED);
                break;
            case 2:
                color_list.setTextColor(Color.WHITE);
                textView_list.setBackgroundColor(Color.RED);
                break;
            case 3:
                color_setting.setTextColor(Color.WHITE);
                textView_setting.setBackgroundColor(Color.RED);
                break;
        }
    }


    public static DbSqlite getDbSqlite() {
        return dbSqlite;
    }


    /*
      * 隐藏输入法
      * */
    public void hideInputSoft() {
        View v = null;
        if (imm != null) {
            v = this.getCurrentFocus();
            if (v == null)
                return;
        }
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}