package com.homework.teacher.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.homework.teacher.utils.Utils;

/**
 * 重置密码
 * 
 * @author zhangkc
 * 
 */
public class PasswordResetActivity extends Activity {
	private final static String TAG = PasswordResetActivity.class.getName();
	private final static String TOKEN = "token";
	private final static String SALT = "salt";
	private SharedPreferences sp;
	private EditText mNewPasswordEt, mConfirmPasswordEt, mPhoneNumberEt,
			mVerifyCodeEt;
	private TextView mGetVerifyCodeTv;
	private Button mOkBtn;
	private String newPassword, confirmPassword, newPwd, phoneNumber,
			verifyCode;
	private int mTime;
	private static final int DISABLE_TIME = 90;
	private Handler mHander = new Handler();
	private Runnable mDelayedTvEnable = new Runnable() {
		@Override
		public void run() {
			if (mTime == 0) {
				mGetVerifyCodeTv.setEnabled(true);
				mGetVerifyCodeTv.setText(R.string.get_verifycode);
			} else {
				mGetVerifyCodeTv.setText(getResources().getString(
						R.string.get_verifycode_delayed_format,
						String.valueOf(mTime--)));
				mHander.postDelayed(this, 1000);
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_password_reset);

		((Button) findViewById(R.id.back))
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
		((TextView) findViewById(R.id.title)).setText(R.string.password_reset);
		((TextView) findViewById(R.id.tvSet)).setVisibility(View.GONE);
		sp = BaseApplication.getInstance().getSp();
		mNewPasswordEt = (EditText) findViewById(R.id.newPasswordEt);
		mConfirmPasswordEt = (EditText) findViewById(R.id.confirmPasswordEt);
		mPhoneNumberEt = (EditText) findViewById(R.id.phoneNumberEt);
		mVerifyCodeEt = (EditText) findViewById(R.id.verifyCodeEt);
		mGetVerifyCodeTv = (TextView) findViewById(R.id.getVerifyCodeTv);
		mGetVerifyCodeTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				phoneNumber = mPhoneNumberEt.getText().toString();
				if (StringUtils.isEmpty(phoneNumber)) {
					Toast.makeText(PasswordResetActivity.this,
							R.string.phonenumner_cannot_null,
							Toast.LENGTH_SHORT).show();
				} else {
					if (!Utils.isPhone(phoneNumber)) {
						Toast.makeText(PasswordResetActivity.this,
								R.string.input_correct_phonenumber,
								Toast.LENGTH_SHORT).show();
					} else {
						delayedEnableTv();
						getSalt();
						tokenFetch("getSmsvercode");
					}
				}

			}
		});
		mOkBtn = (Button) findViewById(R.id.okBtn);
		mOkBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				newPassword = mNewPasswordEt.getText().toString();
				if (StringUtils.isEmpty(newPassword)) {
					Toast.makeText(PasswordResetActivity.this,
							R.string.newpwd_cannot_null, Toast.LENGTH_SHORT)
							.show();
				} else {
					if (newPassword.length() <= 6
							|| StringUtils.isNumeric(newPassword)) {
						Toast.makeText(PasswordResetActivity.this,
								"新密码需大于6位且不为纯数字！", Toast.LENGTH_SHORT).show();
					} else {
						confirmPassword = mConfirmPasswordEt.getText()
								.toString();
						if (StringUtils.isEmpty(confirmPassword)) {
							Toast.makeText(PasswordResetActivity.this,
									R.string.compwd_cannot_null,
									Toast.LENGTH_SHORT).show();
						} else {
							if (!confirmPassword.equals(newPassword)) {
								Toast.makeText(PasswordResetActivity.this,
										"两次密码输入不一致！", Toast.LENGTH_SHORT)
										.show();
							} else {
								verifyCode = mVerifyCodeEt.getText().toString();
								if (StringUtils.isEmpty(verifyCode)) {
									Toast.makeText(PasswordResetActivity.this,
											R.string.verifycode_cannot_null,
											Toast.LENGTH_SHORT).show();
								} else {
									tokenFetch("passwordReset");
								}
							}
						}
					}
				}
			}
		});
	}

	private void delayedEnableTv() {
		mGetVerifyCodeTv.setEnabled(false);
		mTime = DISABLE_TIME;
		mHander.post(mDelayedTvEnable);
	}

	private void getSalt() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("loginName", phoneNumber);// 登录名
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
								PasswordResetActivity.this);
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
								if (method.equals("getSmsvercode")) {
									getSmsvercode();
								} else if (method.equals("passwordReset")) {
									passwordReset();
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
								PasswordResetActivity.this);
					}
				});
		BaseApplication.getInstance().addToRequestQueue(mRequest, "");
	}

	private void getSmsvercode() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("mobileNo", phoneNumber);
			jsonObject.put("type", 2);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String url = WDStringRequest.getUrl(Consts.SERVER_getSmsvercode,
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
									Toast.makeText(PasswordResetActivity.this,
											R.string.get_verifycode_already,
											Toast.LENGTH_SHORT).show();
								} else {// code==1 响应错误
									Toast.makeText(PasswordResetActivity.this,
											"90秒内不可重复获取", Toast.LENGTH_SHORT)
											.show();
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
								PasswordResetActivity.this);
					}
				});
		BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
	}

	private void passwordReset() {
		JSONObject jsonObject = new JSONObject();
		try {
			String salt = BaseApplication.getInstance().getSalt();
			String pwdsalt = newPassword + "$" + salt;
			try {
				newPwd = MD5Utils.getMd5Value(Base64.encodeToString(
						pwdsalt.getBytes("utf-8"), Base64.NO_WRAP));
			} catch (Exception e) {
				e.printStackTrace();
			}
			jsonObject.put("passWord", newPwd);// 密码
			jsonObject.put("mobileNo", phoneNumber);// 手机号
			jsonObject.put("type", 2);// 验证码类型 1.登录注册 2.重置密码
			jsonObject.put("cerCode", verifyCode);// 验证码
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String url = WDStringRequest.getUrl(Consts.SERVER_passwordReset,
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
									Toast.makeText(PasswordResetActivity.this,
											"重置成功", Toast.LENGTH_SHORT).show();
									finish();
								} else {// code==1 响应错误
									Toast.makeText(PasswordResetActivity.this,
											"重置失败", Toast.LENGTH_SHORT).show();
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
								PasswordResetActivity.this);
					}
				});
		BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
	}

}