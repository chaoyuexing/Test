package com.homework.teacher.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.homework.teacher.Consts;
import com.homework.teacher.R;
import com.homework.teacher.app.BaseApplication;
import com.homework.teacher.http.WDStringRequest;
import com.homework.teacher.utils.CleanMessageUtil;
import com.homework.teacher.utils.StatusUtils;
import com.homework.teacher.utils.Utils;
import com.homework.teacher.widget.MyCommonDialog;

/**
 * 设置页
 * 
 * @author zhangkc
 * 
 */
public class SettingActivity extends Activity {
	private final static String TAG = SettingActivity.class.getName();
	private final static String LOGINFLAG = "loginFlag";// 登录状态 0：未登录，1：已登录
	private final static String CSTID = "cstId";
	private SharedPreferences sp;
	private RelativeLayout mClearCacheRl, mCheckVersionRl, mAboutRl;
	private TextView mClearCacheTv, mVersionTv;
	private Button mExitBtn;
	private MyCommonDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);

		((Button) findViewById(R.id.back))
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
		((TextView) findViewById(R.id.title)).setText(R.string.setting);
		((TextView) findViewById(R.id.tvSet)).setVisibility(View.GONE);
		sp = BaseApplication.getInstance().getSp();
		mClearCacheRl = (RelativeLayout) findViewById(R.id.clearCacheRl);
		mClearCacheRl.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					CleanMessageUtil.clearAllCache(getApplicationContext());
					Toast.makeText(SettingActivity.this, "清除缓存完毕",
							Toast.LENGTH_SHORT).show();
					mClearCacheTv.setText(CleanMessageUtil
							.getTotalCacheSize(getApplicationContext()));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		mCheckVersionRl = (RelativeLayout) findViewById(R.id.checkVersionRl);
		mCheckVersionRl.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				checkUpdate();
			}
		});
		mAboutRl = (RelativeLayout) findViewById(R.id.aboutRl);
		mAboutRl.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(SettingActivity.this, AboutVsichuActivity.class);
				startActivity(intent);
			}
		});
		mClearCacheTv = (TextView) findViewById(R.id.clearCacheTv);
		try {
			mClearCacheTv.setText(CleanMessageUtil
					.getTotalCacheSize(getApplicationContext()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		mVersionTv = (TextView) findViewById(R.id.versionTv);
		mVersionTv.setText("v" + Utils.getVersion(SettingActivity.this));
		mExitBtn = (Button) findViewById(R.id.exitBtn);
		if (BaseApplication.getInstance().getCstID() != 0) {
			mExitBtn.setVisibility(View.VISIBLE);
		}
		mExitBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				sp.edit().putInt(LOGINFLAG, 0).commit();
				Log.i(TAG, "loginFlag: "
						+ BaseApplication.getInstance().getLoginFlag());
				sp.edit().putInt(CSTID, 0).commit();
				finish();
				Intent intent = new Intent();
				intent.setClass(SettingActivity.this, LoginActivity.class);
				startActivity(intent);
			}
		});
	}

	private void checkUpdate() {
		JSONObject jsonObject = new JSONObject();
		try {
			String curVersion = Utils.getVersion(SettingActivity.this);
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
									Toast.makeText(SettingActivity.this,
											"已经是最新版本了", Toast.LENGTH_SHORT)
											.show();
								} else if (flag == 1) {// 1：选择更新
									dialog = new MyCommonDialog(
											SettingActivity.this,
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
											SettingActivity.this, "新版本提示",
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
						StatusUtils.handleError(arg0, SettingActivity.this);
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