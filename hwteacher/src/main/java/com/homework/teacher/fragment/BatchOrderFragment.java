package com.homework.teacher.fragment;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.homework.teacher.Consts;
import com.homework.teacher.R;
import com.homework.teacher.activity.BatchOrderActivity;
import com.homework.teacher.activity.SelectDatePopupWindow;
import com.homework.teacher.app.BaseApplication;
import com.homework.teacher.http.WDStringRequest;
import com.homework.teacher.utils.DateUtils;
import com.homework.teacher.utils.StatusUtils;
import com.homework.teacher.widget.ProgressHUD;

/**
 * 批量下单
 * 
 * @author zhangkc
 * 
 */
public class BatchOrderFragment extends Fragment {
	private final static String TAG = "BatchOrderFragment";
	private ProgressHUD mProgressHUD;
	private final static String TOKEN = "token";
	private final static String BGNDATE = "bgnDate";
	private final static String ENDDATE = "endDate";
	private View view;
	protected BaseApplication mApp;
	private SharedPreferences sp;

	private TextView mStartDateTv, mEndDateTv;
	private Button mBatchOrderBtn;
	private String startOrderDate = "";// 下单开始日期，默认明天
	private String endOrderDate = "";// 下单结束日期

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.batchorder_fragment, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.e(TAG, "onActivityCreated");
		mApp = BaseApplication.getInstance();
		sp = mApp.getSp();

		mStartDateTv = (TextView) view.findViewById(R.id.startDateTv);
		startOrderDate = DateUtils.getTomorrowDate();
		mStartDateTv.setText(startOrderDate);
		sp.edit().putString(BGNDATE, startOrderDate).commit();
		mStartDateTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent(getActivity(),
						SelectDatePopupWindow.class), 1);
			}
		});
		mEndDateTv = (TextView) view.findViewById(R.id.endDateTv);
		mEndDateTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent(getActivity(),
						SelectDatePopupWindow.class), 2);
			}
		});
		mBatchOrderBtn = (Button) view.findViewById(R.id.batchOrderBtn);
		mBatchOrderBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if ("".equals(endOrderDate)) {
					Toast.makeText(getActivity(), "请选择订餐结束日期",
							Toast.LENGTH_SHORT).show();
				} else {
					Date dt1 = DateUtils.stringToDate(startOrderDate);
					Date dt2 = DateUtils.stringToDate(endOrderDate);
					if (dt1.getTime() > dt2.getTime()) {
						Toast.makeText(getActivity(), "订餐结束日期不能早于开始日期",
								Toast.LENGTH_SHORT).show();
					} else {
						tokenFetch();
					}
				}
			}
		});
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {
			Bundle bundle = data.getExtras();
			switch (resultCode) {
			case Activity.RESULT_OK:
				int year = bundle.getInt("year");
				int month = bundle.getInt("month");
				int day = bundle.getInt("day");
				String month1,
				day1;
				if (month < 10) {
					month1 = "0" + month;
				} else {
					month1 = "" + month;
				}
				if (day < 10) {
					day1 = "0" + day;
				} else {
					day1 = "" + day;
				}
				if (requestCode == 1) {
					startOrderDate = year + "-" + month1 + "-" + day1;
					Log.e(TAG, "startOrderDate: " + startOrderDate);
					mStartDateTv.setText(startOrderDate);
					sp.edit().putString(BGNDATE, startOrderDate).commit();
				} else if (requestCode == 2) {
					endOrderDate = year + "-" + month1 + "-" + day1;
					Log.e(TAG, "endOrderDate: " + endOrderDate);
					mEndDateTv.setText(endOrderDate);
					sp.edit().putString(ENDDATE, endOrderDate).commit();
				}
				break;
			default:
				break;
			}
		}
	}

	private void tokenFetch() {
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
								batchOrder();
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

	private void batchOrder() {
		showMyDialog(true);
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("cstId", BaseApplication.getInstance().getCstID());// 客户ID
			jsonObject.put("bgnDate", startOrderDate);// 订餐开始日期
			jsonObject.put("endDate", endOrderDate);// 订餐结束日期
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String url = WDStringRequest.getUrl(Consts.SERVER_batchOrder,
				jsonObject);
		String relative_url = WDStringRequest.getRelativeUrl();
		String sign_body = WDStringRequest.getSignBody();

		WDStringRequest mRequest = new WDStringRequest(Request.Method.GET, url,
				relative_url, sign_body, true, new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						mProgressHUD.dismiss();
						try {
							JSONObject jsonObject = new JSONObject(response);
							int code = 0;// 响应头中的code
							if (jsonObject != null) {
								code = jsonObject.getInt("code");
								if (code == 0) {
									Intent intent = new Intent();
									intent.putExtra(
											BatchOrderActivity.STARTORDERDATE,
											startOrderDate);
									intent.putExtra(
											BatchOrderActivity.ENDORDERDATE,
											endOrderDate);
									intent.setClass(getActivity(),
											BatchOrderActivity.class);
									startActivity(intent);
								} else if (code == 10501) {
									Toast.makeText(getActivity(),
											"一周食谱未设置，请设置您的一周食谱",
											Toast.LENGTH_SHORT).show();
								} else if (code == 10502) {
									Toast.makeText(getActivity(),
											"用餐计划缺失，请完善您的用餐计划",
											Toast.LENGTH_SHORT).show();
								} else if (code == 10503) {
									Toast.makeText(getActivity(),
											"订餐周期不允许超过36天", Toast.LENGTH_SHORT)
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
						StatusUtils.handleError(arg0, getActivity());
					}
				});
		BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
	}

	private void showMyDialog(boolean isCancelable) {
		mProgressHUD = ProgressHUD.show(getActivity(), "正在下单", true,
				isCancelable, null);
	}

}