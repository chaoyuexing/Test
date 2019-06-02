package com.linkage.lib.util;

import android.app.ProgressDialog;
import android.content.Context;

public class ProgressDialogUtils {
	private static ProgressDialog proDialog;

	public static void showProgressDialog(int resId, Context context) {
		showProgressDialog(context.getString(resId),context, false);
	}
	
	public static void showProgressDialog(String msg, Context context, Boolean cancelable) {
		proDialog = new ProgressDialog(context);
		proDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置风格为圆形进度条
		proDialog.setMessage(msg);
		proDialog.setIndeterminate(false);// 设置进度条是否为不明确
		proDialog.setCancelable(cancelable);// 设置进度条是否可以按退回键取消
		proDialog.show();
	}

	public static void dismissProgressBar() {
		if (proDialog != null) {
			proDialog.dismiss();
		}
	}

}
