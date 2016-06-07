package com.example.administrator.dictionary.ui;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.administrator.dictionary.R;
import com.example.administrator.dictionary.service.NtfService;
import com.example.administrator.dictionary.view.MainViewPager;

/**
 * Created by Administrator on 2016/5/5.
 */
public class SettingFragment extends Fragment implements View.OnClickListener {

    private RelativeLayout fastSearch;
    private ImageView fastSearchBtn;


    InputMethodManager imm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.settings_fragment, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        fastSearch = (RelativeLayout) view.findViewById(R.id.fast_setting);
        fastSearchBtn = (ImageView) view.findViewById(R.id.fast_setting_btn);

        imm= (InputMethodManager) MainViewPager.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (NtfService.isStart()) {
            fastSearchBtn.setSelected(true);
        } else {
            fastSearch.setSelected(false);
        }

        fastSearch.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (!NtfService.isStart()) {
            fastSearchBtn.setSelected(true);
            Intent startIntent = new Intent(MainViewPager.getContext(), NtfService.class);
            MainViewPager.getContext().startService(startIntent);
        } else {
            fastSearchBtn.setSelected(false);
            Intent stopIntent = new Intent(MainViewPager.getContext(), NtfService.class);
            MainViewPager.getContext().stopService(stopIntent);
        }
    }
}