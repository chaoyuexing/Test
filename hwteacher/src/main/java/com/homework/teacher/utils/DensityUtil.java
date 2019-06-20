package com.homework.teacher.utils;

import android.content.Context;

import com.homework.teacher.app.BaseApplication;

/**
 * Created by xing
 * on 2019/6/8
 */
public class DensityUtil {


    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int dip2px(float dpValue) {
        Context context = BaseApplication.getInstance();
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
