package com.homework.teacher.utils;

import android.content.Context;

/**
 *
 * @author CCL
 */
public class Toast {
	private static android.widget.Toast toast;

	/**
	 */
	public static void showShort(Context context, CharSequence message) {
		if (null == toast) {
			toast = android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_SHORT);
		} else {
			toast.setDuration(android.widget.Toast.LENGTH_SHORT);
			toast.setText(message);
		}
		toast.show();
	}

	public static void showShort(Context context, int message) {
		if (null == toast) {
			toast = android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_SHORT);
		} else {
			toast.setDuration(android.widget.Toast.LENGTH_SHORT);
			toast.setText(message);
		}
		toast.show();
	}

	/**
	 */
	public static void showLong(Context context, CharSequence message) {
		if (null == toast) {
			toast = android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_LONG);
		} else {
			toast.setDuration(android.widget.Toast.LENGTH_LONG);
			toast.setText(message);
		}
		toast.show();
	}

	public static void showLong(Context context, int message) {
		if (null == toast) {
			toast = android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_LONG);
		} else {
			toast.setDuration(android.widget.Toast.LENGTH_LONG);
			toast.setText(message);
		}
		toast.show();
	}

	/**
	 */
	public static void show(Context context, CharSequence message, int duration) {
		if (null == toast) {
			toast = android.widget.Toast.makeText(context, message, duration);
		} else {
			toast.setDuration(duration);
			toast.setText(message);
		}
		toast.show();
	}

	public static void show(Context context, int message, int duration) {
		if (null == toast) {
			toast = android.widget.Toast.makeText(context, message, duration);
		} else {
			toast.setDuration(duration);
			toast.setText(message);
		}
		toast.show();
	}

	public static void hideToast() {
		if (null != toast) {
			toast.cancel();
		}
	}
}