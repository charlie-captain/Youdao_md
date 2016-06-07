package com.example.administrator.dictionary.entity;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.dictionary.R;

/**
 * Created by Administrator on 2016/5/23.
 */
public class ReciteHolder {

    public TextView word;
    public TextView interpret;
    public TextView englishBack;
    public TextView reciteOk;
    public TextView reciteNo;
    public LinearLayout english;
    public LinearLayout amarican;
    public TextView enText;
    public TextView amText;
    public TextView wordId;

    public View mView;

    public ReciteHolder(View mView) {
        this.mView = mView;
    }


    public void getViewInit() {
        word = (TextView) mView.findViewById(R.id.listbook_recite_word);
        enText = (TextView) mView.findViewById(R.id.lb_recite_english_text);
        englishBack = (TextView) mView.findViewById(R.id.lb_recite_english_text_back);
        amText = (TextView) mView.findViewById(R.id.lb_recite_american_text);
        english = (LinearLayout) mView.findViewById(R.id.listbook_recite_en_ly);
        amarican = (LinearLayout) mView.findViewById(R.id.listbook_recite_am_ly);
        interpret = (TextView) mView.findViewById(R.id.listbook_recite_interpret);
        wordId = (TextView) mView.findViewById(R.id.listbook_recite_id);
        reciteOk = (TextView) mView.findViewById(R.id.listbook_recite_ok);
        reciteNo = (TextView) mView.findViewById(R.id.listbook_recite_no);
    }



}
