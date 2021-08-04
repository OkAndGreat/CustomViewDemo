package com.example.customview.test;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

/**
 * Author by OkAndGreat，Date on 2021/8/2.
 */
class MyLayoutManger extends LinearLayoutManager {
    private boolean isScrollEnabled = true;

    public MyLayoutManger(Context context) {
        super(context);
    }

    /*调用这个方法控制滑动*/
    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollVertically() {
        return isScrollEnabled && super.canScrollVertically();
    }
}

