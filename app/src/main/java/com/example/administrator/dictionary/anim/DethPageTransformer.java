package com.example.administrator.dictionary.anim;

import android.support.v4.view.ViewPager;
import android.view.View;



public class DethPageTransformer implements ViewPager.PageTransformer {
    private static final float MIN_SCALE = 0.85f;


    @Override
    public void transformPage(View view, float position) {
        float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));

        view.setScaleX(scaleFactor);
        view.setScaleY(scaleFactor);
    }
}
