package com.example.administrator.dictionary.ui;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.dictionary.R;
import com.example.administrator.dictionary.config.ConfigFinal;
import com.example.administrator.dictionary.https.SendHttpRequest;
import com.example.administrator.dictionary.https.YouDaoJson;
import com.example.administrator.dictionary.view.MainViewPager;

import java.net.URLEncoder;


public class SentenceFragment extends Fragment implements View.OnClickListener, View.OnKeyListener, TextView.OnEditorActionListener {

    private View rootView;
    private LinearLayout searchBtn, webSentence;
    private EditText sentenceEdit;
    private TextView sentenceText, webText;
    private ImageButton clearSentence;
    private ScrollView scrollView;
    private YouDaoJson jsonGet;
    private SendHttpRequest httpSentence;
    private InputMethodManager imm;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.sentence_fragment, container, false);
            init(rootView);
        }

        return rootView;
    }

    private void init(View view) {
        sentenceEdit = (EditText) view.findViewById(R.id.sententce_edit);

        sentenceText = (TextView) view.findViewById(R.id.sententce_text);
        webText = (TextView) view.findViewById(R.id.sentence_web);

        searchBtn = (LinearLayout) view.findViewById(R.id.search_btn);
        webSentence = (LinearLayout) view.findViewById(R.id.web_sentence);

        clearSentence = (ImageButton) view.findViewById(R.id.sentence_clear_edit);
        scrollView = (ScrollView) view.findViewById(R.id.sentence_sco);


        initEvent();

    }

    private void initEvent() {
        YouDaoJson jsonGet=new YouDaoJson();
        httpSentence=new SendHttpRequest();

        sentenceEdit.setOnEditorActionListener(this);
        sentenceEdit.setOnKeyListener(this);
        sentenceEdit.addTextChangedListener(textWatcher);
        searchBtn.setOnClickListener(this);
        clearSentence.setOnClickListener(this);
        imm = (InputMethodManager) MainViewPager.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (!sentenceEdit.getText().toString().equals("")) {
                clearSentence.setVisibility(View.VISIBLE);
            } else {
                clearSentence.setVisibility(View.INVISIBLE);
            }


        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };


    private Handler handler = new Handler() {             //一个消息处理机制
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ConfigFinal.SHOW:
                    try {
                        scrollView.setVisibility(View.VISIBLE);
                        String response = (String) msg.obj;
                        sentenceText.setText(response);
                        if (!jsonGet.webText.equals("")) {
                            webText.setText(jsonGet.webText);
                            webSentence.setVisibility(View.VISIBLE);
                        } else {
                            webText.setText("");
                            webSentence.setVisibility(View.GONE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case ConfigFinal.NOT_CONNECT:
                    scrollView.setVisibility(View.VISIBLE);
                    sentenceText.setText("网络未连接， 请检查网络设置");
                    break;
            }
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_btn:
                searchWord();
                break;
            case R.id.sentence_clear_edit:
                sentenceEdit.setText("");
                imm.showSoftInput(sentenceEdit, 0);
                break;

        }
    }

    public void searchWord() {
        String word = sentenceEdit.getText().toString();
        if (!"".equals(word.trim())) {
            String youDaoUrl = ConfigFinal.YOUDAO_BASE + "?keyfrom=" + ConfigFinal.YOUDAO_KEY_FROM + "&key=" +
                    ConfigFinal.YOUDAO_KEY + "&type=" + ConfigFinal.YOUDAO_DOC_TYPE + "&doctype=" + ConfigFinal.YOUDAO_TYPE + "&version=" + ConfigFinal.YOUDAO_VERSION + "&q=";
            hideInputSoft();
            try {
                httpSentence.sendHttpRequest(youDaoUrl + URLEncoder.encode(word, "utf-8"), handler, null, word);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            Toast.makeText(MainViewPager.getContext(), "输入不能为空", Toast.LENGTH_SHORT).show();
        }
    }


    /*
       * 隐藏输入法
       * */
    public void hideInputSoft() {
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(sentenceEdit.getWindowToken(), 0);
        }
    }


    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if(i==KeyEvent.KEYCODE_ENTER){
            hideInputSoft();
            searchWord();
        }
        return false;
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if(KeyEvent.KEYCODE_ENTER==i){
            hideInputSoft();
            searchWord();
        }
        return false;
    }


}
