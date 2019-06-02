package com.linkage.lib.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.content.Context;

public class DateUtils
{
    private static DateFormat df = new SimpleDateFormat("E MMM d HH:mm:ss Z yyyy",
            Locale.ENGLISH);

    public static long parseDateStr(String dateTime)
    {
        long time = 0;
        if (dateTime != null)
        {
            try
            {
                time = df.parse(dateTime).getTime();
            }
            catch (Exception e)
            {
            }
        }
        return time;
    }

    public static String getRelativeDate(Context context, long dateTime)
    {
        Date now = new Date();
        // Seconds.
        float diff = (now.getTime() - dateTime) / 1000;
        if (diff < 60)
        {
        	return "几秒钟之前";
        }
        else if (diff < 3300)
        {
        	return "%1$s分钟前".replace("%1$s", String.valueOf(Math.round(diff / 60)));
        }
        else if (diff < 22 * 3600)
        {
            return "%1$s小时前".replace("%1$s", String.valueOf(Math.round(diff / 3600)));
        }
        else
        {
            return new SimpleDateFormat("MM-dd HH:mm").format(new Date(dateTime));
        }
    }
    
    public static SimpleDateFormat FORMAT_DEFAULT_ALL = new SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss", Locale.CHINA);
	
	/**
	 * format:yyyy年MM月dd日 hh:mm
	 */
	public static SimpleDateFormat FORMAT_DEFAULT = new SimpleDateFormat("yyyy年MM月dd日 hh:mm", Locale.CHINA);
	/**
	 * format:yyyy年MM月
	 */
	public static SimpleDateFormat FORMAT_YY_MM = new SimpleDateFormat("yyyy年MM月", Locale.CHINA);
	
	public static SimpleDateFormat FORMAT_SEND_SMS = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	
	public static SimpleDateFormat FORMAT_FEED = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	
	public static String nowToString(SimpleDateFormat format) {
		if(format == null) {
			return null;
		}
		return format.format(new Date());
	}
	
	public static String nowToStringYYMM() {
		return nowToString(FORMAT_YY_MM);
	}
	
	public static String getSmsMonthRequestFormat(Calendar c) {
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		month++;
		return String.format("%d%02d", year, month);
	}
	
	public static String getSmsMonthShowFormat(Calendar c) {
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		month++;
		return String.format("%d - %d月", year, month);
	}
	
	public static String getSchoolTimeDateShowFormat(Calendar c) {
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		month++;
		return String.format("%d-%02d-%02d", year, month, day);
	}
	
	public static String getSchoolTimeDateFormat(Calendar c) {
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		month++;
		return String.format("%d%02d%02d", year, month, day);
	}
	
	public static String getSchoolTimeWeekFormat(Context context, Calendar calendar) {
		int day = calendar.get(Calendar.DAY_OF_WEEK);
		String weekFormat = null;
		switch (day - 1) {
		case 0:
			weekFormat = "星期天";
			break;
		case 1:
			weekFormat = "星期一";
			break;
		case 2:
			weekFormat = "星期二";
			break;
		case 3:
			weekFormat = "星期三";
			break;
		case 4:
			weekFormat = "星期四";
			break;
		case 5:
			weekFormat = "星期五";
			break;
		case 6:
			weekFormat = "星期六";
			break;
		default:
			break;
		}
		return weekFormat;
	}
	
	public static String getRelativeDate(String str) {
		try {
			Date date = FORMAT_FEED.parse(str);
			return getRelativeDate(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if(str.endsWith(".0")) {
			return str.substring(0, str.length() - 2);
		}
		return str;
	}
	
	public static String getRelativeDate(Date date) {
		Date now = new Date();
        String sec = "秒";
        String min = "分";
        String hour = "小时";
        String day = "天";
        String suffix = "前";
        
        // seconds 
        long diff = (now.getTime() - date.getTime()) / 1000;
        
        if(diff < 0) {
        	diff = 0;
        }
        
        if(diff < 60)
        	return diff + sec + suffix;
        
        // minutes
        diff /= 60;
        if(diff < 60)
        	return diff + min + suffix;
        
        // hours
        diff /= 60;
        if(diff < 24)
        	return diff + hour + suffix;
        
        // days
        diff /= 24;
        if(diff < 15)
        	return diff + day + suffix;
        
        return FORMAT_DEFAULT_ALL.format(date);
	}
}