package com.homework.teacher.fragment;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.homework.teacher.Consts;
import com.homework.teacher.R;
import com.homework.teacher.activity.CustomerInfoActivity;
import com.homework.teacher.activity.LoginActivity;
import com.homework.teacher.activity.MyIntegralActivity;
import com.homework.teacher.activity.MyVoucherActivity;
import com.homework.teacher.activity.SettingActivity;
import com.homework.teacher.app.BaseApplication;
import com.homework.teacher.data.Remind;
import com.homework.teacher.data.Voucher;
import com.homework.teacher.http.WDStringRequest;
import com.homework.teacher.utils.StatusUtils;

/**
 * 我的
 * 
 * @author zhangkc
 * 
 */
public class MineFragment extends Fragment {
	private final static String TAG = "MineFragment";
	private final static String TOKEN = "token";
	private View view;
	protected BaseApplication mApp;
	private SharedPreferences sp;
	private ImageView mSettingIv, mMessageIv, mAvatarIv;
	private TextView mNickNameTv, mMobileNoTv, mCashTv, mVoucherTv,
			mIntegralTv, mServicePhoneTv, mServiceTimeTv, mCallPhoneTv;
	private LinearLayout mCashLy, mVoucherLy, mIntegralLy;
	private RelativeLayout mCustomerInfoRl, mMineCommentRl,
			mLackmealRegisterRl, mSuggestionRl, mOrderHelpRl;
	private double cash;// 余额
	private List<Voucher> vouchers;// 代金券
	private int integral;// 积分
	private String servicePhone;// 客服电话

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.mine_fragment, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mApp = BaseApplication.getInstance();
		sp = mApp.getSp();

		mSettingIv = (ImageView) view.findViewById(R.id.settingIv);
		mSettingIv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getActivity(), SettingActivity.class));
			}
		});
		mMessageIv = (ImageView) view.findViewById(R.id.messageIv);
		mAvatarIv = (ImageView) view.findViewById(R.id.avatarIv);
		mAvatarIv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (BaseApplication.getInstance().getCstID() != 0) {
					Toast.makeText(getActivity(), "暂无头像", Toast.LENGTH_SHORT)
							.show();
				} else {
					Intent intent = new Intent();
					intent.setClass(getActivity(), LoginActivity.class);
					startActivity(intent);
				}
			}
		});

		mNickNameTv = (TextView) view.findViewById(R.id.nickNameTv);
		mMobileNoTv = (TextView) view.findViewById(R.id.mobileNoTv);
		mCashTv = (TextView) view.findViewById(R.id.cashTv);
		mVoucherTv = (TextView) view.findViewById(R.id.voucherTv);
		mIntegralTv = (TextView) view.findViewById(R.id.integralTv);
		mServicePhoneTv = (TextView) view.findViewById(R.id.servicePhoneTv);
		mServiceTimeTv = (TextView) view.findViewById(R.id.serviceTimeTv);
		mCallPhoneTv = (TextView) view.findViewById(R.id.callPhoneTv);
		mCallPhoneTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Uri uri = Uri.parse("tel:"
						+ (TextUtils.isEmpty(servicePhone) ? "" : servicePhone));
				Intent intent = new Intent(Intent.ACTION_DIAL, uri);
				startActivity(intent);
			}
		});

		mCashLy = (LinearLayout) view.findViewById(R.id.cashLl);
		mVoucherLy = (LinearLayout) view.findViewById(R.id.voucherLl);
		mVoucherLy.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				if (BaseApplication.getInstance().getCstID() != 0) {
					intent.setClass(getActivity(), MyVoucherActivity.class);
				} else {
					intent.setClass(getActivity(), LoginActivity.class);
				}
				startActivity(intent);
			}
		});
		mIntegralLy = (LinearLayout) view.findViewById(R.id.integralLl);
		mIntegralLy.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				if (BaseApplication.getInstance().getCstID() != 0) {
					intent.setClass(getActivity(), MyIntegralActivity.class);
				} else {
					intent.setClass(getActivity(), LoginActivity.class);
				}
				startActivity(intent);
			}
		});

		mCustomerInfoRl = (RelativeLayout) view
				.findViewById(R.id.customerInfoRl);
		mMineCommentRl = (RelativeLayout) view.findViewById(R.id.mineCommentRl);
		mLackmealRegisterRl = (RelativeLayout) view
				.findViewById(R.id.lackmealRegisterRl);
		mSuggestionRl = (RelativeLayout) view.findViewById(R.id.suggestionRl);
		mOrderHelpRl = (RelativeLayout) view.findViewById(R.id.orderHelpRl);
		mCustomerInfoRl.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (BaseApplication.getInstance().getCstID() != 0) {
					startActivity(new Intent(getActivity(),
							CustomerInfoActivity.class));
					getCustomerInfo();
				} else {
					Intent intent = new Intent();
					intent.setClass(getActivity(), LoginActivity.class);
					startActivity(intent);
				}
			}
		});
		queryServicePhone();
		queryServiceTime();
	}

	@Override
	public void onResume() {
		super.onResume();
		if (BaseApplication.getInstance().getCstID() != 0) {
			getCustomerInfo();
		} else {
			mNickNameTv.setText("立即登录");// 昵称
			mMobileNoTv.setText("--");// 手机
			mVoucherTv.setText("--");// 代金券
			mIntegralTv.setText("--");// 积分
		}
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
								mMobileNoTv.setText(body.optString("mobileNo"));// 手机
								// cash = body.optDouble("cash");
								mCashTv.setText(cash + "元");// 余额
								vouchers = Voucher.parseJson(body
										.optJSONArray("voucherList"));
								mVoucherTv.setText(vouchers.size() + "张");// 代金券
								integral = body.optInt("integral");
								mIntegralTv.setText(integral + "分");// 积分
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

	private void queryServicePhone() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("key", "CSTSERVICE_PHONE"); // 客服电话
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String url = WDStringRequest.getUrl(Consts.SERVER_queryByKey,
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
							JSONArray body = null; // 响应体中的body
							if (jsonObject != null) {
								code = jsonObject.getInt("code");
								if (code == 0) {
									body = new JSONArray(jsonObject
											.getString("body"));
								}
							}
							if (body != null) {
								List<Remind> reminds = Remind.parseJson(body);
								servicePhone = reminds.get(0).getValue();
								mServicePhoneTv.setText("客服电话：" + servicePhone);
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

	private void queryServiceTime() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("key", "CSTSERVICE_TIME");// 服务时间
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String url = WDStringRequest.getUrl(Consts.SERVER_queryByKey,
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
							JSONArray body = null; // 响应体中的body
							if (jsonObject != null) {
								code = jsonObject.getInt("code");
								if (code == 0) {
									body = new JSONArray(
											jsonObject.getString("body"));
								}
							}
							if (body != null) {
								List<Remind> reminds = Remind.parseJson(body);
								String serviceTime = reminds.get(0).getValue();
								mServiceTimeTv.setText("服务时间：" + serviceTime);
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