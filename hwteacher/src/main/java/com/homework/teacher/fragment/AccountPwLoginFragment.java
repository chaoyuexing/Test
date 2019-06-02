package com.homework.teacher.fragment;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Base64;
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
import com.homework.teacher.activity.PasswordResetActivity;
import com.homework.teacher.app.BaseApplication;
import com.homework.teacher.http.WDStringRequest;
import com.homework.teacher.utils.MD5Utils;
import com.homework.teacher.utils.StatusUtils;
import com.homework.teacher.utils.StringUtils;
import com.homework.teacher.widget.XEditText;
import com.homework.teacher.widget.XEditText.DrawableRightListener;

/**
 * 账号密码登录页面
 * 
 * @author zhangkc
 * 
 */
public class AccountPwLoginFragment extends Fragment {
	private static final String TAG = "AccountPwLoginFragment";
	private final static String TOKEN = "token";
	private final static String LOGINFLAG = "loginFlag";// 登录状态 0：未登录，1：已登录
	private final static String SALT = "salt";
	private final static String CSTID = "cstId";
	private View view;
	private SharedPreferences sp;
	private EditText loginnameEt;
	private XEditText passwordEt;
	private Button loginBtn;
	private TextView forgetPasswordTv;
	private boolean mIsShowPassword;
	private String loginname, pwd, password;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.login_accountpw_fragment, container,
				false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		sp = BaseApplication.getInstance().getSp();
		loginnameEt = ((EditText) view.findViewById(R.id.loginnameEt));
		passwordEt = ((XEditText) view.findViewById(R.id.passwordEt));
		passwordEt.setDrawableRightListener(new DrawableRightListener() {
			@Override
			public void onDrawableRightClick(View view) {
				if (mIsShowPassword) {
					passwordEt.setCompoundDrawablesWithIntrinsicBounds(
							R.drawable.password, 0,
							R.drawable.password_visiable, 0);
					passwordEt.setInputType(InputType.TYPE_CLASS_TEXT
							| InputType.TYPE_TEXT_VARIATION_PASSWORD);
					mIsShowPassword = false;
				} else {
					passwordEt.setCompoundDrawablesWithIntrinsicBounds(
							R.drawable.password, 0,
							R.drawable.password_invisiable, 0);
					passwordEt
							.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
					mIsShowPassword = true;
				}
			}
		});
		loginBtn = ((Button) view.findViewById(R.id.loginBtn));
		loginBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				loginname = loginnameEt.getText().toString();
				if (StringUtils.isEmpty(loginname)) {
					Toast.makeText(getActivity(),
							R.string.loginname_cannot_null, Toast.LENGTH_SHORT)
							.show();
				} else {
					pwd = passwordEt.getText().toString();
					if (StringUtils.isEmpty(pwd)) {
						Toast.makeText(getActivity(),
								R.string.password_cannot_null,
								Toast.LENGTH_SHORT).show();
					} else {
						getSalt();
					}
				}
			}
		});
		forgetPasswordTv = ((TextView) view.findViewById(R.id.forgetPasswordTv));
		forgetPasswordTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), PasswordResetActivity.class);
				startActivity(intent);
			}
		});
	}

	private void getSalt() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("loginName", loginname);// 客户ID
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
								} else {// code==1 响应错误
									Toast.makeText(getActivity(),
											"账户名或密码错误，请重新输入",
											Toast.LENGTH_SHORT).show();
								}
							}
							if (body != null) {
								sp.edit()
										.putString(SALT, body.optString("salt"))
										.commit();
								Log.i(TAG, "盐存入本地~~~");
								tokenFetch("accountLogin");
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
								if (method.equals("accountLogin")) {
									accountLogin();
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

	private void accountLogin() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("loginName", loginname);// 登录名
			String salt = BaseApplication.getInstance().getSalt();
			String pwdsalt = pwd + "$" + salt;
			try {
				password = MD5Utils.getMd5Value(Base64.encodeToString(
						pwdsalt.getBytes("utf-8"), Base64.NO_WRAP));
			} catch (Exception e) {
				e.printStackTrace();
			}
			jsonObject.put("passWord", password);// 密码
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String url = WDStringRequest.getUrl(Consts.SERVER_accountLogin,
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
							JSONObject body = null; // 响应体中的body
							if (jsonObject != null) {
								code = jsonObject.getInt("code");
								if (code == 0) {
									body = new JSONObject(
											jsonObject.getString("body"));
								} else {// code==1 响应错误
									Toast.makeText(getActivity(),
											"账户名或密码错误，请重新输入",
											Toast.LENGTH_SHORT).show();
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