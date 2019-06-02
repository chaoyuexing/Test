package com.homework.teacher.activity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.homework.teacher.Consts;
import com.homework.teacher.R;
import com.homework.teacher.app.BaseApplication;
import com.homework.teacher.http.WDStringRequest;
import com.homework.teacher.utils.StatusUtils;
import com.homework.teacher.utils.StringUtils;
import com.homework.teacher.widget.JudgeDate;
import com.homework.teacher.widget.ProgressHUD;
import com.homework.teacher.widget.ScreenInfo;
import com.homework.teacher.widget.WheelMain;

/**
 * 基本资料页
 * 
 * @author zhangkc
 * 
 */
public class CustomerInfoActivity extends Activity {
	private final static String TAG = CustomerInfoActivity.class.getName();
	private ProgressHUD mProgressHUD;
	private final static String TOKEN = "token";
	protected BaseApplication mApp;
	private SharedPreferences sp;
	private ImageView mAvatarIv;
	private WheelMain mWheelMain;
	private PopupWindow mPop;
	private View view;
	private TextView mNickNameTv, mGenderTv, mBirthdayTv, mMobileNoTv,
			mWechatTv, mQqTv, mLoginNameTv, mLoginPasswordTv,
			mPresentPasswordTv;
	private RelativeLayout mAvatarRl, mNickNameRl, mGenderRl, mBirthdayRl,
			mMobileNoRl, mWechatRl, mQqRl, mLoginNameRl, mLoginPasswordRl,
			mPresentPasswordRl;
	private int gender;// 性别 1：男，2：女
	private String nickName, birthday, loginName;
	private int loginPwdFlag;// 登录密码设置状态 0：未设置，1：已设置
	private int payPwdFlag;// 提现密码设置状态 0：未设置，1：已设置

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_customerinfo);

		mApp = BaseApplication.getInstance();
		sp = mApp.getSp();
		((Button) findViewById(R.id.back))
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
		((TextView) findViewById(R.id.title)).setText(R.string.custom_info);
		((TextView) findViewById(R.id.tvSet)).setVisibility(View.GONE);
		mAvatarIv = (ImageView) findViewById(R.id.avatarIv);
		mNickNameTv = (TextView) findViewById(R.id.nickNameTv);
		mGenderTv = (TextView) findViewById(R.id.genderTv);
		mBirthdayTv = (TextView) findViewById(R.id.birthdayTv);
		mMobileNoTv = (TextView) findViewById(R.id.mobileNoTv);
		mWechatTv = (TextView) findViewById(R.id.wechatTv);
		mQqTv = (TextView) findViewById(R.id.qqTv);
		mLoginNameTv = (TextView) findViewById(R.id.loginNameTv);
		mLoginPasswordTv = (TextView) findViewById(R.id.loginPasswordTv);
		mPresentPasswordTv = (TextView) findViewById(R.id.presentPasswordTv);

		mAvatarRl = (RelativeLayout) findViewById(R.id.avatarRl);
		mNickNameRl = (RelativeLayout) findViewById(R.id.nickNameRl);
		mNickNameRl.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				nickName = mNickNameTv.getText().toString().trim();
				if (StringUtils.isEmpty(nickName)) {
					intent.putExtra(UpdateInfoTextActivity.OLDINFO, "");
				} else {
					intent.putExtra(UpdateInfoTextActivity.OLDINFO, nickName);
				}
				intent.putExtra(UpdateInfoTextActivity.ISPASSWORD, false);
				intent.putExtra(UpdateInfoTextActivity.INFOTYPE, 1);
				intent.setClass(CustomerInfoActivity.this,
						UpdateInfoTextActivity.class);
				startActivity(intent);
			}
		});
		mGenderRl = (RelativeLayout) findViewById(R.id.genderRl);
		mGenderRl.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mPop.isShowing()) {
					mPop.dismiss();
				} else {
					mPop.showAtLocation(findViewById(R.id.customerinfo),
							Gravity.BOTTOM, 0, 0);
				}
			}
		});
		mBirthdayRl = (RelativeLayout) findViewById(R.id.birthdayRl);
		mBirthdayRl.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LayoutInflater inflater = LayoutInflater
						.from(CustomerInfoActivity.this);
				final View timepickerview = inflater.inflate(
						R.layout.timepicker, null);
				ScreenInfo screenInfo = new ScreenInfo(
						CustomerInfoActivity.this);
				mWheelMain = new WheelMain(timepickerview);
				mWheelMain.screenheight = screenInfo.getHeight();
				String time = mBirthdayTv.getText().toString();
				Calendar calendar = Calendar.getInstance();
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd",
						Locale.getDefault());
				if (JudgeDate.isDate(time, "yyyy-MM-dd")) {
					try {
						calendar.setTime(dateFormat.parse(time));
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				int year = calendar.get(Calendar.YEAR);
				int month = calendar.get(Calendar.MONTH);
				int day = calendar.get(Calendar.DAY_OF_MONTH);
				mWheelMain.initDateTimePicker(year, month, day);
				new AlertDialog.Builder(CustomerInfoActivity.this)
						.setTitle("请选择日期")
						.setView(timepickerview)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										birthday = mWheelMain.getTime();
										String s[] = birthday.split("-");
										int month = Integer.valueOf(s[1])
												.intValue();
										int day = Integer.valueOf(s[2])
												.intValue();
										if (month < 10) {
											s[1] = "-0" + month;
										} else {
											s[1] = "-" + month;
										}
										if (day < 10) {
											s[2] = "-0" + day;
										} else {
											s[2] = "-" + day;
										}
										birthday = s[0] + s[1] + s[2];
										mBirthdayTv.setText(birthday);
										tokenFetch("updateInfo");
									}
								})
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
									}
								}).show();

			}
		});
		mMobileNoRl = (RelativeLayout) findViewById(R.id.mobileNoRl);
		mWechatRl = (RelativeLayout) findViewById(R.id.wechatRl);
		mQqRl = (RelativeLayout) findViewById(R.id.qqRl);
		mLoginNameRl = (RelativeLayout) findViewById(R.id.loginNameRl);
		mLoginNameRl.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				loginName = mLoginNameTv.getText().toString().trim();
				if (StringUtils.isEmpty(loginName)) {
					intent.putExtra(UpdateInfoTextActivity.OLDINFO, "");
				} else {
					intent.putExtra(UpdateInfoTextActivity.OLDINFO, loginName);
				}
				intent.putExtra(UpdateInfoTextActivity.ISPASSWORD, false);
				intent.putExtra(UpdateInfoTextActivity.INFOTYPE, 2);
				intent.setClass(CustomerInfoActivity.this,
						UpdateInfoTextActivity.class);
				startActivity(intent);
			}
		});
		mLoginPasswordRl = (RelativeLayout) findViewById(R.id.loginPasswordRl);
		mLoginPasswordRl.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				String s = mLoginPasswordTv.getText().toString().trim();
				if (loginPwdFlag == 0) {
					intent.putExtra(UpdateInfoTextActivity.OLDINFO, "");
					intent.putExtra(UpdateInfoTextActivity.PASSWORDFLAG, 0);
				} else {
					intent.putExtra(UpdateInfoTextActivity.OLDINFO, s);
					intent.putExtra(UpdateInfoTextActivity.PASSWORDFLAG, 1);
				}
				intent.putExtra(UpdateInfoTextActivity.ISPASSWORD, true);
				intent.putExtra(UpdateInfoTextActivity.PASSWORDTYPE, 1);
				intent.setClass(CustomerInfoActivity.this,
						UpdateInfoTextActivity.class);
				startActivity(intent);
			}
		});
		mLoginPasswordTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				String s = mLoginPasswordTv.getText().toString().trim();
				if (loginPwdFlag == 0) {
					intent.putExtra(UpdateInfoTextActivity.OLDINFO, "");
					intent.putExtra(UpdateInfoTextActivity.PASSWORDFLAG, 0);
				} else {
					intent.putExtra(UpdateInfoTextActivity.OLDINFO, s);
					intent.putExtra(UpdateInfoTextActivity.PASSWORDFLAG, 1);
				}
				intent.putExtra(UpdateInfoTextActivity.ISPASSWORD, true);
				intent.putExtra(UpdateInfoTextActivity.PASSWORDTYPE, 1);
				intent.setClass(CustomerInfoActivity.this,
						UpdateInfoTextActivity.class);
				startActivity(intent);
			}
		});
		mPresentPasswordRl = (RelativeLayout) findViewById(R.id.presentPasswordRl);
		mPresentPasswordRl.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				String s = mPresentPasswordTv.getText().toString().trim();
				if (payPwdFlag == 0) {
					intent.putExtra(UpdateInfoTextActivity.OLDINFO, "");
					intent.putExtra(UpdateInfoTextActivity.PASSWORDFLAG, 0);
				} else {
					intent.putExtra(UpdateInfoTextActivity.OLDINFO, s);
					intent.putExtra(UpdateInfoTextActivity.PASSWORDFLAG, 1);
				}
				intent.putExtra(UpdateInfoTextActivity.ISPASSWORD, true);
				intent.putExtra(UpdateInfoTextActivity.PASSWORDTYPE, 2);
				intent.setClass(CustomerInfoActivity.this,
						UpdateInfoTextActivity.class);
				startActivity(intent);
			}
		});
		initPopupWindow();
		if (BaseApplication.getInstance().getCstID() != 0) {
			showMyDialog(true);
			getCustomerInfo();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (BaseApplication.getInstance().getCstID() != 0) {
			getCustomerInfo();
		}
	}

	private void initPopupWindow() {
		view = this.getLayoutInflater()
				.inflate(R.layout.popup_window_sex, null);
		mPop = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT, true);
		mPop.setOutsideTouchable(true);
		ColorDrawable dw = new ColorDrawable(-00000);
		mPop.setBackgroundDrawable(dw);
		mPop.setAnimationStyle(R.style.popupWindowAnimation);
		mPop.update();
		Button pop_cancel = (Button) view.findViewById(R.id.btn_pop_cancel);
		pop_cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mPop.dismiss();
			}
		});
		Button pop_male = (Button) view.findViewById(R.id.btn_pop_male);
		pop_male.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mGenderTv.setText("男");
				gender = 1;
				tokenFetch("updateInfo");
				mPop.dismiss();
			}
		});
		Button pop_female = (Button) view.findViewById(R.id.btn_pop_female);
		pop_female.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mGenderTv.setText("女");
				gender = 2;
				tokenFetch("updateInfo");
				mPop.dismiss();
			}
		});
	}

	private void getCustomerInfo() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("cstId", BaseApplication.getInstance().getCstID());// 客户ID
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String url = WDStringRequest.getUrl(Consts.SERVER_getCustomerInfo,
				jsonObject);
		String relative_url = WDStringRequest.getRelativeUrl();
		String sign_body = WDStringRequest.getSignBody();

		WDStringRequest mRequest = new WDStringRequest(Request.Method.GET, url,
				relative_url, sign_body, false,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						mProgressHUD.dismiss();
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
								mNickNameTv.setText(body.optString("nickName"));// 昵称
								gender = body.optInt("gender");
								if (gender == 1) {// 性别
									mGenderTv.setText("男");
								} else if (gender == 2) {
									mGenderTv.setText("女");
								}
								birthday = body.optString("birthday");
								mBirthdayTv.setText(birthday);// 生日

								StringBuilder mobileNo = new StringBuilder(body
										.optString("mobileNo"));
								mobileNo.setCharAt(3, '*');
								mobileNo.setCharAt(4, '*');
								mobileNo.setCharAt(5, '*');
								mobileNo.setCharAt(6, '*');
								mMobileNoTv.setText(mobileNo);// 绑定手机

								mWechatTv.setText(body.optString("weChat"));// 微信OpenID
								mQqTv.setText(body.optString("qq"));// QQ
								mLoginNameTv.setText(body
										.optString("loginName"));// 登录名
								loginPwdFlag = body.optInt("loginPwdFlag");
								if (loginPwdFlag == 0) {// 登录密码
									mLoginPasswordTv.setText("");
								}
								// payPwdFlag = body.optInt("payPwdFlag");
								if (payPwdFlag == 0) {// 提现密码
									mPresentPasswordTv.setText("");
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						StatusUtils
								.handleError(arg0, CustomerInfoActivity.this);
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
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						StatusUtils
								.handleError(arg0, CustomerInfoActivity.this);
					}
				});
		BaseApplication.getInstance().addToRequestQueue(mRequest, "");
	}

	private void updateInfo() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("cstId", BaseApplication.getInstance().getCstID());// 客户ID
			jsonObject.put("gender", gender);
			jsonObject.put("birthday", birthday);
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
							JSONObject body = null; // 响应体中的body
							if (jsonObject != null) {
								code = jsonObject.getInt("code");
								if (code == 0) {
									body = new JSONObject(
											jsonObject.getString("body"));
								}
							}
							if (body != null) {

							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						StatusUtils
								.handleError(arg0, CustomerInfoActivity.this);
					}
				});
		BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
	}

	public boolean onTouchEvent(MotionEvent event) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (CustomerInfoActivity.this.getCurrentFocus() != null) {
				if (CustomerInfoActivity.this.getCurrentFocus()
						.getWindowToken() != null) {
					imm.hideSoftInputFromWindow(CustomerInfoActivity.this
							.getCurrentFocus().getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
				}
			}
		}
		return super.onTouchEvent(event);
	}

	private void showMyDialog(boolean isCancelable) {
		mProgressHUD = ProgressHUD.show(CustomerInfoActivity.this, "正在加载",
				true, isCancelable, null);
	}
}