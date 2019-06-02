package com.linkage.lib.util;

import java.io.IOException;
import java.net.SocketTimeoutException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

public class HttpExecuter 
{
	private static final int CONNECTION_TIMEOUT = 13000;
	private static final int SOCKET_TIMEOUT = 45000;
	
	public static HttpResponse executeGet(HttpGet get) throws ClientProtocolException, IOException, SocketTimeoutException {
		return getHttpClient().execute(get);
	}
	
	public static HttpResponse executePost(HttpPost post) throws ClientProtocolException, IOException {
		return getHttpClient().execute(post);
	}
	
	public static HttpClient getHttpClient()
	{
		HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, CONNECTION_TIMEOUT);
        HttpConnectionParams.setSoTimeout(params, SOCKET_TIMEOUT);
		return new DefaultHttpClient(params);
	}
}
