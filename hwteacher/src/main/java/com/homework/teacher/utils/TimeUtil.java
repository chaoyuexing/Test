package com.homework.teacher.utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * 时间工具：获取当前设备时间
 * Created by Admin on 2016/12/5.
 */
public class TimeUtil {

    private static final String TAG = "TimeUtil";

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getNowTime() {
        return new SimpleDateFormat("yyyy_MM_dd").format(new Date()).toString();
    }

    public static String getNowTime(String type) {//null
        return new SimpleDateFormat(type).format(new Date());
    }


    public static String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String t = format.format(new Date());
        return t;
    }

    public static String getNowDate() {
        String[] split = getTime().split(":");
        return split[0];
    }

    /**
     * 获取这个礼拜的开始和结束时间
     *
     * @return
     */
    public static Map<String, String> getDayWeek() {
        Map<String, String> weekMap = new HashMap<>();
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        //当前星期几-3
        int mWay = c.get(Calendar.DAY_OF_WEEK);
//        XLogger.e("今天礼拜" + mWay);
        //当前时间戳
        long currentTime = System.currentTimeMillis();
        //一天的时间戳天数___减去礼拜几,,取获礼拜一是几号
        long weekStart = currentTime - ((1000 * 60 * 60 * 24) * (mWay - 1 ))+(1000 * 60 * 60 * 24)*7;//礼拜天-2
        long weekStop = currentTime + ((1000 * 60 * 60 * 24) * (7 - mWay ))+(1000 * 60 * 60 * 24)*7;//礼拜六4
        String startTime = getOutTradeNo(weekStart);
        String stopTime = getOutTradeNo(weekStop);
        Log.i(TAG, "getDayWeek-startTime: "+startTime);
        Log.i(TAG, "getDayWeek-stopTime: "+stopTime);
//        XLogger.e(startTime);
//        XLogger.e(stopTime);
        weekMap.put("startTime", startTime);
        weekMap.put("stopTime", stopTime);
        return weekMap;
    }

    private static String getOutTradeNo(long weekStart) {
        return new SimpleDateFormat("yyyy.MM.dd").format(weekStart).toString();

    }
}
