package com.example.administrator.dictionary.entity;

import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.dictionary.R;

/**
 * Created by Administrator on 2016/5/25.
 */
public class ListbookWord {


   public TextView word, en_text, am_text, en_textBack, interpret;
   public ImageView english, amarican;
   public LinearLayout en_ly, am_ly;


    public ListbookWord(Window window) {
        window.setContentView(R.layout.listbook_word_showword);
        word = (TextView) window.findViewById(R.id.listbook_word_word);
        en_text = (TextView) window.findViewById(R.id.lb_word_english_text);
        am_text = (TextView) window.findViewById(R.id.lb_word_american_text);
        en_textBack = (TextView) window.findViewById(R.id.lb_word_english_text_back);
        interpret = (TextView) window.findViewById(R.id.listbook_word_interpret);
        english = (ImageView) window.findViewById(R.id.lb_word_english);
        amarican = (ImageView) window.findViewById(R.id.lb_word_american);
        en_ly = (LinearLayout) window.findViewById(R.id.listbook_word_en_ly);
        am_ly = (LinearLayout) window.findViewById(R.id.listbook_word_am_ly);
    }
}
