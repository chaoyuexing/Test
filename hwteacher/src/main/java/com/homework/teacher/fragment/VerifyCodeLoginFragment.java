package com.homework.teacher.fragment;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.homework.teacher.Consts;
import com.homework.teacher.R;
import com.homework.teacher.activity.MainActivity;
import com.homework.teacher.activity.WebViewActivity;
import com.homework.teacher.app.BaseApplication;
import com.homework.teacher.http.WDStringRequest;
import com.homework.teacher.utils.StatusUtils;
import com.homework.teacher.utils.StringUtils;
import com.homework.teacher.utils.Utils;

/**
 * 手机验证码登录页面
 * 
 * @author zhangkc
 * 
 */
public class VerifyCodeLoginFragment extends Fragment {
	private static final String TAG = "VerifyCodeLoginFragment";
	private final static String TOKEN = "token";
	private final static String LOGINFLAG = "loginFlag";// 登录状态 0：未登录，1：已登录
	private final static String CSTID = "cstId";
	private View view;
	private SharedPreferences sp;
	private EditText phoneNumberEt, verifyCodeEt;
	private TextView getVerifyCodeTv, reminderTv;
	private Button loginBtn;
	private int mTime;
	private static final int DISABLE_TIME = 90;
	private Handler mHander = new Handler();
	private String mobileNo, cerCode;
	private Runnable mDelayedTvEnable = new Runnable() {
		@Override
		public void run() {
			if (mTime == 0) {
				getVerifyCodeTv.setEnabled(true);
				getVerifyCodeTv.setText(R.string.get_verifycode);
			} else {
				if (isAdded()) {
					getVerifyCodeTv.setText(getResources().getString(
							R.string.get_verifycode_delayed_format,
							String.valueOf(mTime--)));
				}
				mHander.postDelayed(this, 1000);
			}
		}
	};
	@SuppressLint("HandlerLeak")
	Handler smsHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				verifyCodeEt.setText(msg.obj.toString());
			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.login_verifycode_fragment, container,
				false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		sp = BaseApplication.getInstance().getSp();
		phoneNumberEt = ((EditText) view.findViewById(R.id.phoneNumberEt));
		verifyCodeEt = ((EditText) view.findViewById(R.id.verifyCodeEt));
		verifyCodeEt.addTextChangedListener(new VerifyCodeLength());
		// SMSContentObserver smsContentObserver = new SMSContentObserver(
		// getActivity(), smsHandler);
		// getActivity().getContentResolver().registerContentObserver(
		// Uri.parse("content://sms/"), true, smsContentObserver);

		getVerifyCodeTv = ((TextView) view.findViewById(R.id.getVerifyCodeTv));
		getVerifyCodeTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mobileNo = phoneNumberEt.getText().toString();
				if (StringUtils.isEmpty(mobileNo)) {
					Toast.makeText(getActivity(),
							R.string.phonenumner_cannot_null,
							Toast.LENGTH_SHORT).show();
				} else {
					if (!Utils.isPhone(mobileNo)) {
						Toast.makeText(getActivity(),
								R.string.input_correct_phonenumber,
								Toast.LENGTH_SHORT).show();
					} else {
						delayedEnableTv();
						tokenFetch("getSmsvercode");
					}
				}
			}
		});
		reminderTv = ((TextView) view.findViewById(R.id.reminderTv));
		String s = reminderTv.getText().toString();
		SpannableStringBuilder builder = new SpannableStringBuilder(s);
		// ForegroundColorSpan为文字前景色，BackgroundColorSpan为文字背景色
		ForegroundColorSpan blueSpan = new ForegroundColorSpan(Color.BLUE);
		builder.setSpan(blueSpan, 32, s.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		reminderTv.setText(builder);
		reminderTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), WebViewActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString(WebViewActivity.TITLE, getResources()
						.getString(R.string.user_service));
				bundle.putString(WebViewActivity.URL, Consts.USER_SERVICE_URL);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

		loginBtn = ((Button) view.findViewById(R.id.loginBtn));
		loginBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mobileNo = phoneNumberEt.getText().toString();
				if (StringUtils.isEmpty(mobileNo)) {
					Toast.makeText(getActivity(),
							R.string.phonenumner_cannot_null,
							Toast.LENGTH_SHORT).show();
				} else {
					cerCode = verifyCodeEt.getText().toString();
					if (StringUtils.isEmpty(cerCode)) {
						Toast.makeText(getActivity(),
								R.string.verifycode_cannot_null,
								Toast.LENGTH_SHORT).show();
					} else {
						tokenFetch("doLogin");
					}
				}
			}
		});
	}

	private class VerifyCodeLength implements TextWatcher {
		@Override
		public void afterTextChanged(Editable s) {// 表示最终内容

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start/* 开始的位置 */,
				int count/* 被改变的旧内容数 */, int after/* 改变后的内容数量 */) {
			// 这里的s表示改变之前的内容，通常start和count组合，可以在s中读取本次改变字段中被改变的内容。而after表示改变后新的内容的数量。
		}

		@Override
		public void onTextChanged(CharSequence s, int start/* 开始位置 */,
				int before/* 改变前的内容数量 */, int count/* 新增数 */) {
			// 这里的s表示改变之后的内容，通常start和count组合，可以在s中读取本次改变字段中新的内容。而before表示被改变的内容的数量。
			if (s.length() == 6) {
				mobileNo = phoneNumberEt.getText().toString();
				if (StringUtils.isEmpty(mobileNo)) {
					Toast.makeText(getActivity(),
							R.string.phonenumner_cannot_null,
							Toast.LENGTH_SHORT).show();
				} else {
					cerCode = verifyCodeEt.getText().toString();
					if (StringUtils.isEmpty(cerCode)) {
						Toast.makeText(getActivity(),
								R.string.verifycode_cannot_null,
								Toast.LENGTH_SHORT).show();
					} else {
						tokenFetch("doLogin");
					}
				}
			}
		}
	};

	private void delayedEnableTv() {
		getVerifyCodeTv.setEnabled(false);
		mTime = DISABLE_TIME;
		mHander.post(mDelayedTvEnable);
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
								} else if (method.equals("doLogin")) {
									doLogin();
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						StatusUtils.handleError(arg0, getActivity());
					}
				});
		BaseApplication.getInstance().addToRequestQueue(mRequest, "");
	}

	private void getSmsvercode() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("mobileNo", mobileNo);
			jsonObject.put("type", 1);
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
									Toast.makeText(getActivity(),
											R.string.get_verifycode_already,
											Toast.LENGTH_SHORT).show();
								} else {// code==1 响应错误
									Toast.makeText(getActivity(), "90秒内不可重复获取",
											Toast.LENGTH_SHORT).show();
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						StatusUtils.handleError(arg0, getActivity());
					}
				});
		BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
	}

	private void doLogin() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("mobileNo", mobileNo);
			jsonObject.put("type", 1);
			jsonObject.put("cerCode", cerCode);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String url = WDStringRequest.getUrl(Consts.SERVER_doLogin, jsonObject);
		String relative_url = WDStringRequest.getRelativeUrl();
		String sign_body = WDStringRequest.getSignBody();

		WDStringRequest mRequest = new WDStringRequest(Request.Method.GET, url,
				relative_url, sign_body, true, new Response.Listener<String>() {
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
								} else {// code==1 响应错误
									Toast.makeText(getActivity(),
											"验证码错误，请重新输入", Toast.LENGTH_SHORT)
											.show();
								}
							}
							if (body != null) {
								sp.edit().putInt(LOGINFLAG, 1).commit();
								Log.i(TAG, "loginFlag: "
										+ BaseApplication.getInstance()
												.getLoginFlag());
								sp.edit().putInt(CSTID, body.optInt("cstId"))
										.commit();
								Log.i(TAG, "客户(用户)ID： " + body.optInt("cstId")
										+ " 存入本地~~~");
								getActivity().finish();
								Intent intent = new Intent();
								intent.setClass(getActivity(),
										MainActivity.class);
								startActivity(intent);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						StatusUtils.handleError(arg0, getActivity());
					}
				});
		BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
	}
}