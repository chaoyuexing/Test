package com.linkage.lib.util;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

public class MoblieUtils {
	
	public static String getIMSINumber(Context context)
    {
    	TelephonyManager tm = (TelephonyManager) 
		        context.getSystemService(Context.TELEPHONY_SERVICE);
		return  tm.getSubscriberId();
    }
	
	public static String getIMEI(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Activity.TELEPHONY_SERVICE);
		PackageManager pm = context.getPackageManager();
		int permissionState = pm.checkPermission(
				Manifest.permission.READ_PHONE_STATE, context.getPackageName());
		if (PackageManager.PERMISSION_GRANTED == permissionState) {
			return tm.getDeviceId();
		} else {
			return null;
		}
	}
	
	public static String getLocalMacAddress(Context context) 
    {
    	WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
    	if (wifiManager == null) 
    	{
			return null;
		}
    	WifiInfo info = wifiManager.getConnectionInfo();
    	if (info == null) 
    	{
			return null;
		}
    	if (info.getMacAddress() == null)
        {
    	    return null;
        }
    	return info.getMacAddress().replace(":", "");
    }
	
	public static String sdk() {
		return android.os.Build.VERSION.SDK;
	}

	public static String model() {
		return android.os.Build.MODEL;
	}
	
	/**
	 * 获取App安装包信息
	 */
	public static PackageInfo getPackageInfo(Context context) {
		PackageInfo info = null;
		try {
			info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		if (info == null) {
			info = new PackageInfo();
		}
		return info;
	}

	/**
	 * 获取屏幕高度
	 */
	public static int getWindowHeight(Activity activity) {
		DisplayMetrics metric = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
		return metric.widthPixels;
	}

	/**
	 * 获取屏幕宽度
	 */
	public static int getWindowWidth(Activity activity) {
		DisplayMetrics metric = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
		return metric.heightPixels;
	}

	/**
	 * 获取通知栏高度
	 */

	public static int getStatusBarHeight(Context context) {
		Class<?> c = null;
		Object obj = null;
		Field field = null;
		int x = 0, statusBarHeight = 0;
		try {
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			statusBarHeight = context.getResources().getDimensionPixelSize(x);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return statusBarHeight;
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dp2px(Context context, float dpValue) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dp(Context context, float pxValue) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	public static String formatTime(long time, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.getDefault());
		Date date = new Date();
		date.setTime(time);
		return sdf.format(date);
	}

	private final static double EARTH_RADIUS = 6378.137;

	private static double rad(double d) {
		return d * Math.PI / 180.0;
	}

	public static double GetDistance(double lat1, double lng1, double lat2, double lng2) {
		double radLat1 = rad(lat1);
		double radLat2 = rad(lat2);
		double a = radLat1 - radLat2;
		double b = rad(lng1) - rad(lng2);
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000) / 10000;
		return s;
	}
	
	public static boolean isSdPresent()
    {
		if (android.os.Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED)) 
		{
			return true;
		}
		else 
		{
			return false;
		}
    }
	
	public static String getSdPath()
    {
		return Environment.getExternalStorageDirectory().getAbsolutePath();
    }
	
}
