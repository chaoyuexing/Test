package com.linkage.lib.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetWorkUtils 
{
	public static boolean isNetworkAvailable(Context context) 
	{
		NetworkInfo networkInfo = ((ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE))
				.getActiveNetworkInfo();
		return networkInfo != null && networkInfo.isAvailable();
	}
	
}

