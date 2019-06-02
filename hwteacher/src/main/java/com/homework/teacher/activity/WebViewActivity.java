package com.homework.teacher.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.homework.teacher.R;

/**
 * WebViewActivity
 * 
 * @author zhangkc
 * 
 */
public class WebViewActivity extends Activity {
	private final static String TAG = WebViewActivity.class.getName();
	public final static String TITLE = "title";
	public final static String URL = "url";
	private static WebView mWebView;
	private String title, url;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_orderhelper);

		if (getIntent().getExtras() != null) {
			Bundle bundle = getIntent().getExtras();
			title = bundle.getString(TITLE);
			url = bundle.getString(URL);
		}
		((Button) findViewById(R.id.back))
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
		((TextView) findViewById(R.id.title)).setText(title);
		((TextView) findViewById(R.id.tvSet)).setVisibility(View.GONE);

		mWebView = (WebView) findViewById(R.id.webView);
		// clearWebViewCache(mWebView);
		mWebView.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return false;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
			}
		});
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setBuiltInZoomControls(true);
		webSettings.setJavaScriptEnabled(true);
		// webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		// webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		mWebView.loadUrl(url);
	}
}