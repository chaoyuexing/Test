package com.tencent.liteav.demo.common.utils;

import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.Toast;

/**
 * @author：xing Data：2019/5/23
 * brief:
 **/
public class ToastUtils {


    public static void showToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

    public static void showToast(Context context, @StringRes int resId) {
        Toast.makeText(context, context.getResources().getText(resId), Toast.LENGTH_LONG).show();
    }
}
