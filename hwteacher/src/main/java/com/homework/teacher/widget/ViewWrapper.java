package com.homework.teacher.widget;

import android.view.View;

/**
 * @author：xing Data：2019/6/21
 * brief:
 **/
public class ViewWrapper {
    private View rView;

    public ViewWrapper(View target) {
        rView = target;
    }

    public int getWidth() {
        return rView.getLayoutParams().width;
    }

    public void setWidth(int width) {
        rView.getLayoutParams().width = width;
        rView.requestLayout();
    }

    public int getHeight() {
        return rView.getLayoutParams().height;
    }

    public void setHeight(int height) {
        rView.getLayoutParams().height = height;
        rView.requestLayout();
    }

}
