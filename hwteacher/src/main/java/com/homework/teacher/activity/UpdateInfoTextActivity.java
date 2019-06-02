package com.homework.teacher.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.homework.teacher.Consts;
import com.homework.teacher.R;
import com.homework.teacher.app.BaseApplication;
import com.homework.teacher.http.WDStringRequest;
import com.homework.teacher.utils.MD5Utils;
import com.homework.teacher.utils.StatusUtils;
import com.homework.teacher.utils.StringUtils;
import com.homework.teacher.widget.XEditText;
import com.homework.teacher.widget.XEditText.DrawableRightListener;

/**
 * 修改信息页 （此页面修改的信息为文本格式：昵称 、登录名 、登录密码 、提现密码）
 * 
 * @author zhangkc
 * 
 */
public class UpdateInfoTextActivity extends Activity {
	private final static String TAG = UpdateInfoTextActivity.class.getName();
	private final static String TOKEN = "token";
	private final static String SALT = "salt";
	public final static String OLDINFO = "oldInfo";
	public final static String INFOTYPE = "infoType";// 1：昵称，2：登录名
	public final static String ISPASSWORD = "isPassword";
	public final static String PASSWORDTYPE = "passwordType";// 1：登录密码，2：提现密码
	public final static String PASSWORDFLAG = "passwordFlag";// 密码设置状态
																// 0：未设置，1：已设置
	protected BaseApplication mApp;
	private SharedPreferences sp;
	private XEditText mOldInfoEt, mNewInfoEt;
	private Button mConfirmUpdateBtn;
	private String oldInfo, newInfo, oldPwd, newPwd;
	private boolean isPassword, isShowOldPassword, isShowNewPassword;
	private int infoType, type, passwordFlag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_updateinfotext);

		if (getIntent() != null) {
			oldInfo = getIntent().getStringExtra(OLDINFO);
			isPassword = getIntent().getBooleanExtra(ISPASSWORD, false);
			infoType = getIntent().getIntExtra(INFOTYPE, 0);
			type = getIntent().getIntExtra(PASSWORDTYPE, 0);
			passwordFlag = getIntent().getIntExtra(PASSWORDFLAG, 0);
			if (type != 0) {
				getSalt();
			}
			Log.i(TAG, "oldInfo: " + oldInfo + " isPassword: " + isPassword
					+ " infoType: " + infoType + " type: " + type
					+ " passwordFlag: " + passwordFlag);
		}
		mApp = BaseApplication.getInstance();
		sp = mApp.getSp();
		((Button) findViewById(R.id.back))
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
		((TextView) findViewById(R.id.tvSet)).setVisibility(View.GONE);
		mOldInfoEt = (XEditText) findViewById(R.id.oldInfoEt);
		mNewInfoEt = (XEditText) findViewById(R.id.newInfoEt);
		if (isPassword) {
			((TextView) findViewById(R.id.title))
					.setText(R.string.update_password);
			((TextView) findViewById(R.id.oldInfoIv)).setText("原密码：");
			((TextView) findViewById(R.id.newInfoIv)).setText("新密码：");
			mOldInfoEt.setCompoundDrawablesWithIntrinsicBounds(0, 0,
					R.drawable.password_visiable, 0);
			mOldInfoEt.setInputType(InputType.TYPE_CLASS_TEXT
					| InputType.TYPE_TEXT_VARIATION_PASSWORD);
			mNewInfoEt.setCompoundDrawablesWithIntrinsicBounds(0, 0,
					R.drawable.password_visiable, 0);
			mNewInfoEt.setInputType(InputType.TYPE_CLASS_TEXT
					| InputType.TYPE_TEXT_VARIATION_PASSWORD);
			mOldInfoEt.setDrawableRightListener(new DrawableRightListener() {
				@Override
				public void onDrawableRightClick(View view) {
					if (isShowOldPassword) {
						mOldInfoEt.setCompoundDrawablesWithIntrinsicBounds(0,
								0, R.drawable.password_visiable, 0);
						mOldInfoEt.setInputType(InputType.TYPE_CLASS_TEXT
								| InputType.TYPE_TEXT_VARIATION_PASSWORD);
						isShowOldPassword = false;
					} else {
						mOldInfoEt.setCompoundDrawablesWithIntrinsicBounds(0,
								0, R.drawable.password_invisiable, 0);
						mOldInfoEt
								.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
						isShowOldPassword = true;
					}
				}
			});
			mNewInfoEt.setDrawableRightListener(new DrawableRightListener() {
				@Override
				public void onDrawableRightClick(View view) {
					if (isShowNewPassword) {
						mNewInfoEt.setCompoundDrawablesWithIntrinsicBounds(0,
								0, R.drawable.password_visiable, 0);
						mNewInfoEt.setInputType(InputType.TYPE_CLASS_TEXT
								| InputType.TYPE_TEXT_VARIATION_PASSWORD);
						isShowNewPassword = false;
					} else {
						mNewInfoEt.setCompoundDrawablesWithIntrinsicBounds(0,
								0, R.drawable.password_invisiable, 0);
						mNewInfoEt
								.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
						isShowNewPassword = true;
					}
				}
			});
		} else {
			((TextView) findViewById(R.id.title)).setText(R.string.update_info);
		}
		if (isPassword) {
			if (passwordFlag == 0) {
				mOldInfoEt.setEnabled(false);
				mOldInfoEt.setTextColor(getResources().getColor(
						R.color.light_blue));
				mOldInfoEt.setText("未设置");
			} else {
				mOldInfoEt.setInputType(InputType.TYPE_CLASS_TEXT
						| InputType.TYPE_TEXT_VARIATION_PASSWORD);
			}
		} else {
			mOldInfoEt.setEnabled(false);
			if (StringUtils.isEmpty(oldInfo)) {
				mOldInfoEt.setTextColor(getResources().getColor(
						R.color.light_blue));
				mOldInfoEt.setText("未设置");
			} else {
				mOldInfoEt.setText(oldInfo);
			}
		}
		if (isPassword) {
			mNewInfoEt.setInputType(InputType.TYPE_CLASS_TEXT
					| InputType.TYPE_TEXT_VARIATION_PASSWORD);
			mNewInfoEt.setHint("请输入新密码");
		}
		mConfirmUpdateBtn = (Button) findViewById(R.id.confirmUpdateBtn);
		mConfirmUpdateBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (infoType != 0) {
					newInfo = mNewInfoEt.getText().toString();
					if (StringUtils.isEmpty(newInfo)) {
						Toast.makeText(UpdateInfoTextActivity.this,
								R.string.set_cannot_null, Toast.LENGTH_SHORT)
								.show();
					} else {
						if (infoType == 2
								&& (StringUtils.isNumeric(newInfo) || newInfo
										.contains("@"))) {
							Toast.makeText(UpdateInfoTextActivity.this,
									"登录名不为纯数字且不能含@字符", Toast.LENGTH_SHORT)
									.show();
						} else {
							tokenFetch("updateInfo");
						}
					}
				} else {
					if (type != 0) { // 密码修改
						if (passwordFlag == 1) {
							oldInfo = mOldInfoEt.getText().toString();
							if (StringUtils.isEmpty(oldInfo)) {
								Toast.makeText(UpdateInfoTextActivity.this,
										R.string.oldpwd_cannot_null,
										Toast.LENGTH_SHORT).show();
							}
						}
						newInfo = mNewInfoEt.getText().toString();
						if (StringUtils.isEmpty(newInfo)) {
							Toast.makeText(UpdateInfoTextActivity.this,
									R.string.newpwd_cannot_null,
									Toast.LENGTH_SHORT).show();
						} else {
							if (newInfo.length() <= 6
									|| StringUtils.isNumeric(newInfo)) {
								Toast.makeText(UpdateInfoTextActivity.this,
										"新密码需大于6位且不为纯数字！", Toast.LENGTH_SHORT)
										.show();
							} else {
								tokenFetch("changePassword");
							}
						}
					}
				}
			}
		});
	}

	private void getSalt() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("cstId", BaseApplication.getInstance().getCstID());// 客户ID
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String url = WDStringRequest.getUrl(Consts.SERVER_getSalt, jsonObject);
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
								sp.edit()
										.putString(SALT, body.optString("salt"))
										.commit();
								Log.i(TAG, "盐存入本地~~~");
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						StatusUtils.handleError(arg0,
								UpdateInfoTextActivity.this);
					}
				});
		BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
	}

	private void tokenFetch(final String method) {
		String url = WDStringRequest.getUrl(Consts.SERVER_tokenFetch, null);
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
									body = new JSONObject(
											jsonObject.getString("body"));
								}
							}
							if (body != null) {
								sp.edit()
										.putString(TOKEN,
												body.optString("token"))
										.commit();
								Log.i(TAG, "接口访问令牌存入本地~~~");
								if (method.equals("updateInfo")) {
									updateInfo();
								} else if (method.equals("changePassword")) {
									changePassword();
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						StatusUtils.handleError(arg0,
								UpdateInfoTextActivity.this);
					}
				});
		BaseApplication.getInstance().addToRequestQueue(mRequest, "");
	}

	private void updateInfo() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("cstId", BaseApplication.getInstance().getCstID());// 客户ID
			if (infoType == 1) {
				jsonObject.put("nickName", newInfo);
			} else if (infoType == 2) {
				jsonObject.put("loginName", newInfo);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String url = WDStringRequest.getUrl(Consts.SERVER_updateInfo,
				jsonObject);
		String relative_url = WDStringRequest.getRelativeUrl();
		String sign_body = WDStringRequest.getSignBody();

		WDStringRequest mRequest = new WDStringRequest(Request.Method.GET, url,
				relative_url, sign_body, true, new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						try {
							JSONObject jsonObject = new JSONObject(response);
							int code = 0;// 响应头中的code
							if (jsonObject != null) {
								code = jsonObject.getInt("code");
								if (code == 0) {
									Toast.makeText(UpdateInfoTextActivity.this,
											"修改成功", Toast.LENGTH_SHORT).show();
									finish();
								} else if (code == 10107) {
									Toast.makeText(UpdateInfoTextActivity.this,
											"登录名已存在", Toast.LENGTH_SHORT)
											.show();
								} else {// code==1 响应错误
									Toast.makeText(UpdateInfoTextActivity.this,
											"修改失败", Toast.LENGTH_SHORT).show();
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						StatusUtils.handleError(arg0,
								UpdateInfoTextActivity.this);
					}
				});
		BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
	}

	private void changePassword() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("cstId", BaseApplication.getInstance().getCstID());// 客户ID

			String salt = BaseApplication.getInstance().getSalt();
			String oldPwdsalt = oldInfo + "$" + salt;
			try {
				oldPwd = MD5Utils.getMd5Value(Base64.encodeToString(
						oldPwdsalt.getBytes("utf-8"), Base64.NO_WRAP));
			} catch (Exception e) {
				e.printStackTrace();
			}
			jsonObject.put("oldPwd", oldPwd);// 原密码

			String newPwdsalt = newInfo + "$" + salt;
			try {
				newPwd = MD5Utils.getMd5Value(Base64.encodeToString(
						newPwdsalt.getBytes("utf-8"), Base64.NO_WRAP));
			} catch (Exception e) {
				e.printStackTrace();
			}
			jsonObject.put("newPwd", newPwd);// 新密码
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String url = WDStringRequest.getUrl(Consts.SERVER_changePassword,
				jsonObject);
		String relative_url = WDStringRequest.getRelativeUrl();
		String sign_body = WDStringRequest.getSignBody();

		WDStringRequest mRequest = new WDStringRequest(Request.Method.GET, url,
				relative_url, sign_body, true, new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						try {
							JSONObject jsonObject = new JSONObject(response);
							int code = 0;// 响应头中的code
							if (jsonObject != null) {
								code = jsonObject.getInt("code");
								if (code == 0) {
									Toast.makeText(UpdateInfoTextActivity.this,
											"修改成功", Toast.LENGTH_SHORT).show();
									finish();
								} else if (code == 10303) {// code==010303 原密码错误
									Toast.makeText(UpdateInfoTextActivity.this,
											"原密码错误", Toast.LENGTH_SHORT).show();
								} else {// code==1 响应错误
									Toast.makeText(UpdateInfoTextActivity.this,
											"修改失败", Toast.LENGTH_SHORT).show();
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						StatusUtils.handleError(arg0,
								UpdateInfoTextActivity.this);
					}
				});
		BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
	}

	public boolean onTouchEvent(MotionEvent event) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (UpdateInfoTextActivity.this.getCurrentFocus() != null) {
				if (UpdateInfoTextActivity.this.getCurrentFocus()
						.getWindowToken() != null) {
					imm.hideSoftInputFromWindow(UpdateInfoTextActivity.this
							.getCurrentFocus().getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
				}
			}
		}
		return super.onTouchEvent(event);
	}

}