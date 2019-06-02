package com.homework.teacher.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",
			Locale.CHINA);
	public static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM",
			Locale.CHINA);
	public static SimpleDateFormat sdf2 = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss", Locale.CHINA);

	public static String getDate() {
		Date date = new Date();
		return sdf.format(date);
	}

	public static String getMonthYear() {
		Date date = new Date();
		return sdf1.format(date);
	}

	public static String getTomorrowDate() {
		// 通过日历获取明天日期
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(sdf.parse(sdf.format(date)));
			cal.add(Calendar.DAY_OF_YEAR, +1);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return sdf.format(cal.getTime());
	}

	public static Date stringToDate(String str) {
		Date date = null;
		try {
			date = sdf.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static Date stringToDate2(String str) {
		Date date = null;
		try {
			date = sdf2.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
}