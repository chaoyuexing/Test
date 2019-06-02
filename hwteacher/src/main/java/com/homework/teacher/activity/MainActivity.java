package com.homework.teacher.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.homework.teacher.Consts;
import com.homework.teacher.R;
import com.homework.teacher.app.BaseApplication;
import com.homework.teacher.fragment.CookBookFragment;
import com.homework.teacher.fragment.LessonGroupFragment;
import com.homework.teacher.fragment.LiveFragment;
import com.homework.teacher.fragment.MineFragment;
import com.homework.teacher.fragment.OrderFragment;
import com.homework.teacher.http.WDStringRequest;
import com.homework.teacher.utils.NetWorkUtils;
import com.homework.teacher.utils.StatusUtils;
import com.homework.teacher.utils.Utils;
import com.homework.teacher.widget.MyCommonDialog;

/**
 * 主页面
 *
 * @author zhangkc
 *
 */
public class MainActivity extends BaseFragmentActivity implements
		OnClickListener {
	private final static String TAG = MainActivity.class.getName();
	private RadioButton mTab1, mTab2, mTab3, mTab4, mTab5;
	private final static String CLIENTIP = "clientIP";
	private final static String SESSIONID = "sessionID";
	private SharedPreferences sp;
	private MyCommonDialog dialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		sp = BaseApplication.getInstance().getSp();
		String clientIp = BaseApplication.getInstance().getClientIP();
		if (TextUtils.isEmpty(clientIp)) {
			sp.edit().putString(CLIENTIP, NetWorkUtils.getIPAddress(this))
					.commit();
		} else {
			if (!clientIp.equals(NetWorkUtils.getIPAddress(this))) {
				sp.edit().putString(CLIENTIP, NetWorkUtils.getIPAddress(this))
						.commit();
			}
		}
//		sessionFetch();
		initUI();
	}

	private void sessionFetch() {
		String url = WDStringRequest.getUrl(Consts.SERVER_sessionFetch, null);
		String relative_url = WDStringRequest.getRelativeUrl();
		String sign_body = WDStringRequest.getSignBody();

		WDStringRequest mRequest = new WDStringRequest(Request.Method.GET, url,
				relative_url, sign_body, false,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						try {
							JSONObject jsonObject = new JSONObject(response);
							int code = 0;// 响应头中的code
							JSONObject body = null; // 响应体中的body
							if (jsonObject != null) {
								code = jsonObject.getInt("code");
								if (code == 0) {
									body = new JSONObject(jsonObject
											.getString("body"));
								}
							}
							if (body != null) {
								String sessionId = BaseApplication
										.getInstance().getSessionID();
								if (TextUtils.isEmpty(sessionId)) {
									sp.edit()
											.putString(SESSIONID,
													body.getString(SESSIONID))
											.commit();
									Log.i(TAG, "客户端获取sessionId成功！并存入本地~~~");
								} else {
									if (!sessionId.equals(body
											.getString(SESSIONID))) {
										sp.edit()
												.putString(
														SESSIONID,
														body.getString(SESSIONID))
												.commit();
										Log.i(TAG,
												"sessionId失效，客户端获取新 sessionId成功！并存入本地~~~");
									}
								}
								checkUpdate();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						StatusUtils.handleError(arg0, MainActivity.this);
					}
				});
		BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
	}

	@SuppressLint("NewApi")
	private void initUI() {
		mTab1 = (RadioButton) findViewById(R.id.tab_home);
//		Drawable drawable = this.getResources().getDrawable(
//				R.drawable.tab_home_click);
		Drawable drawable = this.getResources().getDrawable(
				R.drawable.tabbar_ic_home_selected);
		mTab1.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawable,
				null, null);
		mTab2 = (RadioButton) findViewById(R.id.tab_cookbook);
		mTab3 = (RadioButton) findViewById(R.id.tab_order);
		mTab4 = (RadioButton) findViewById(R.id.tab_live);
		mTab5 = (RadioButton) findViewById(R.id.tab_mine);
		mTab1.setOnClickListener(this);
		mTab2.setOnClickListener(this);
		mTab3.setOnClickListener(this);
		mTab4.setOnClickListener(this);
		mTab5.setOnClickListener(this);

		changeFragment(new LessonGroupFragment());
	}

	private void changeFragment(Fragment fm) {
		FragmentManager supportFragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = supportFragmentManager
				.beginTransaction();
		transaction.replace(R.id.content, fm);
		transaction.commit();
	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
//		Drawable drawable = this.getResources()
//				.getDrawable(R.drawable.tab_home);
		Drawable drawable = this.getResources()
				.getDrawable(R.drawable.tabbar_home);
		switch (v.getId()) {
		case R.id.tab_home:
			Log.e(TAG, "备课组tab被点击");
			changeFragment(new LessonGroupFragment());
			break;
		case R.id.tab_cookbook:
			Log.e(TAG, "一周食谱tab被点击");
			mTab1.setCompoundDrawablesRelativeWithIntrinsicBounds(null,
					drawable, null, null);
//			changeFragment(new CookBookFragment());
			changeFragment(new LessonGroupFragment());
			break;
		case R.id.tab_order:
			Log.e(TAG, "订单tab被点击");
			mTab1.setCompoundDrawablesRelativeWithIntrinsicBounds(null,
					drawable, null, null);
			Log.i(TAG, "客户(用户)ID： " + BaseApplication.getInstance().getCstID());
			if (BaseApplication.getInstance().getCstID() != 0) {
				changeFragment(new OrderFragment());
			} else {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, LoginActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.tab_live:
			Log.e(TAG, "后厨直播tab被点击");
			mTab1.setCompoundDrawablesRelativeWithIntrinsicBounds(null,
					drawable, null, null);
			changeFragment(new LiveFragment());
			break;
		case R.id.tab_mine:
			Log.e(TAG, "我的tab被点击");
			mTab1.setCompoundDrawablesRelativeWithIntrinsicBounds(null,
					drawable, null, null);
//			changeFragment(new MineFragment());
			changeFragment(new LessonGroupFragment());
			break;
		default:
			break;
		}
	}

	private void checkUpdate() {
		JSONObject jsonObject = new JSONObject();
		try {
			String curVersion = Utils.getVersion(MainActivity.this);
			jsonObject.put("curVersion", curVersion);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String url = WDStringRequest.getUrl(Consts.SERVER_checkUpdate,
				jsonObject);
		String relative_url = WDStringRequest.getRelativeUrl();
		String sign_body = WDStringRequest.getSignBody();

		WDStringRequest mRequest = new WDStringRequest(Request.Method.GET, url,
				relative_url, sign_body, false,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						try {
							JSONObject jsonObject = new JSONObject(response);
							int code = 0;// 响应头中的code
							JSONObject body = null; // 响应体中的body
							if (jsonObject != null) {
								code = jsonObject.getInt("code");
								if (code == 0) {
									body = new JSONObject(jsonObject
											.getString("body"));
								}
							}
							if (body != null) {
								int flag = body.optInt("flag");// 更新标识
																// 0：不更新，1：选择更新，2：强制更新
								String version = body.optString("version");// 最新版本号
								String info = body.optString("info");// 版本信息
								final String url = body.optString("url");// 下载地址
								// APP包下载地址
								if (flag == 0) {// 0：不更新
									initUI();
								} else if (flag == 1) {// 1：选择更新
									initUI();
									dialog = new MyCommonDialog(
											MainActivity.this,
											"新版本提示",
											"检测到最新版本:" + version + "\n " + info,
											"取消", "更新");
									dialog.setOkListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											if (dialog.isShowing()) {
												dialog.dismiss();
											}
											update(url);
											if (dialog.isShowing()) {
												dialog.dismiss();
											}
										}
									});
									dialog.setCancelListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											if (dialog.isShowing()) {
												dialog.dismiss();
											}
										}
									});
									dialog.show();
								} else if (flag == 2) {// 2：强制更新
									dialog = new MyCommonDialog(
											MainActivity.this, "新版本提示",
											"需更新到最新版本:" + version + "\n "
													+ info, "退出", "确定");
									dialog.setOkListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											if (dialog.isShowing()) {
												dialog.dismiss();
											}
											update(url);
											if (dialog.isShowing()) {
												dialog.dismiss();
											}
										}
									});
									dialog.setCancelListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											if (dialog.isShowing()) {
												dialog.dismiss();
												finish();
											}
										}
									});
									dialog.show();
									dialog.setCancelable(false);
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						StatusUtils.handleError(arg0, MainActivity.this);
					}
				});
		BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
	}

	private int update(String url) {
		int ret = -1;
		try {
			Uri uri = Uri.parse(url);
			startActivity(new Intent(Intent.ACTION_VIEW, uri));
			ret = 0;
		} catch (Exception e) {

		}
		return ret;
	}

}