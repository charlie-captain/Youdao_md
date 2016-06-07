package com.example.administrator.dictionary.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Administrator on 2016/5/23.
 */
public class ListbookViewAdapter extends PagerAdapter {

    private Context mcontext;
    private List<View> listView;


    public ListbookViewAdapter(Context context, List<View> listView) {
        mcontext=context;
        this.listView=listView;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(listView.get(position));
    }

    @Override
    public int getCount() {
        return listView.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(listView.get(position));
        return listView.get(position);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }
}
