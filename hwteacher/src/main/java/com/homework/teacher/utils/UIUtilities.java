package com.homework.teacher.utils;

import android.content.Context;
import android.widget.Toast;

public class UIUtilities {

	public static void showToast(Context context, int resId) {
		showToast(context, resId, false);
	}

	public static void showToast(Context context, int resId,
			boolean durationLong) {
		int duration;
		if (durationLong) {
			duration = Toast.LENGTH_LONG;
		} else {
			duration = Toast.LENGTH_SHORT;
		}
		Toast.makeText(context, resId, duration).show();
	}

	public static void showToast(Context context, String msg) {
		if (!StringUtils.isEmpty(msg))
			showToast(context, msg, false);
	}

	public static void showToast(Context context, String msg,
			boolean durationLong) {
		int duration;
		if (durationLong) {
			duration = Toast.LENGTH_LONG;
		} else {
			duration = Toast.LENGTH_SHORT;
		}
		if (!StringUtils.isEmpty(msg))
			Toast.makeText(context, msg, duration).show();
	}

}