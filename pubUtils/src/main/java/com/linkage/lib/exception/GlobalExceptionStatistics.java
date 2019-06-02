package com.linkage.lib.exception;

import java.lang.Thread.UncaughtExceptionHandler;

import com.linkage.lib.util.LogUtils;

import android.content.Context;

public class GlobalExceptionStatistics implements UncaughtExceptionHandler {

    private static GlobalExceptionStatistics instance = new GlobalExceptionStatistics();
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private Context mContext; 
    
    private GlobalExceptionStatistics() {
    }

    public static GlobalExceptionStatistics getInstance() {
        return instance;
    }
    
    public void init(Context context) {
        mContext = context;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }
    
	@Override
	public void uncaughtException(Thread thread, Throwable tr) 
	{
		LogUtils.logFile("UncaughtException", tr);
		mDefaultHandler.uncaughtException(thread, tr);
	}
	
}
