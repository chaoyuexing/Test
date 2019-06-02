package com.homework.teacher.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.homework.teacher.Consts;
import com.homework.teacher.R;
import com.homework.teacher.adapter.OrderProductListAdapter;
import com.homework.teacher.app.BaseApplication;
import com.homework.teacher.data.MealCar;
import com.homework.teacher.http.WDStringRequest;
import com.homework.teacher.utils.DateUtils;
import com.homework.teacher.utils.StatusUtils;
import com.homework.teacher.widget.MyCommonDialog;
import com.homework.teacher.widget.MyListView;
import com.homework.teacher.widget.ProgressHUD;

/**
 * 订单详情页
 * 
 * @author zhangkc
 * @date 2017-6-23
 */
public class OrderDetailActivity extends Activity {
	private final static String TAG = OrderDetailActivity.class.getName();
	private ProgressHUD mProgressHUD;
	public final static String ORDID = "ordId";
	public final static String STATE = "state";
	private final static String TOKEN = "token";
	protected BaseApplication mApp;
	private SharedPreferences sp;
	private FrameLayout mFl;
	private TextView mOrdIdTv, mMealCateTv, mTakeTimeTv, mTakePointTv,
			mMobileNoTv, mDinnerNumTv, mTotalPriceTv, mVoucherTv, mPayTv, mTv1,
			mTv2, mTv;

	private MyListView mMealCarListView;
	private List<MealCar> mMealCarList;
	private OrderProductListAdapter mOrderProductListAdapter;
	private int ordId;// 订单号（取餐码）
	private int state;// 订单状态 0待提交， 1：待支付，2：制作中，3：待领取，4：待点评，5：已完成，-1：已退订
	private String mobileNo;// 联系手机
	private int dinnerNum = 1;// 用餐人数
	private double totalPrice;// 订单总价
	private double vAmount;// 代金券金额
	private double waitPay;// 实付金额
	private MyCommonDialog dialog;
	private String mealCate;
	private String date = "";// 取餐时间
	private Date endDate;
	private long diff;
	// 取餐时间前1.5小时截止退订，把退订按钮隐藏。
	private long timeDifference = 5400000;// 1.5*60*60*1000 =
											// 5400000毫秒，即1.5小时时间差。
	// 新增了payId字段，用于支付平台的后续流程，对应支付平台的out_trade_no
	private int payId;// 付款单ID

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_orderdetail);

		if (getIntent() != null) {
			ordId = getIntent().getIntExtra(ORDID, 0);
			state = getIntent().getIntExtra(STATE, 0);
			Log.i(TAG, "ordId: " + ordId + " , state: " + state);
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
		((TextView) findViewById(R.id.title)).setText(R.string.order_detail);
		((TextView) findViewById(R.id.tvSet)).setVisibility(View.GONE);

		mOrdIdTv = ((TextView) findViewById(R.id.ordIdTv));
		mMealCateTv = ((TextView) findViewById(R.id.mealCateTv));
		mTakeTimeTv = ((TextView) findViewById(R.id.takeTimeTv));
		mTakePointTv = ((TextView) findViewById(R.id.takePointTv));
		mMobileNoTv = ((TextView) findViewById(R.id.mobileNoTv));
		mDinnerNumTv = ((TextView) findViewById(R.id.dinnerNumTv));

		mTotalPriceTv = ((TextView) findViewById(R.id.totalPriceTv));
		mVoucherTv = ((TextView) findViewById(R.id.voucherTv));
		mPayTv = ((TextView) findViewById(R.id.payTv));
		mFl = ((FrameLayout) findViewById(R.id.fl));
		mTv1 = ((TextView) findViewById(R.id.tv1));
		mTv2 = ((TextView) findViewById(R.id.tv2));
		mTv = ((TextView) findViewById(R.id.tv));

		mMealCarListView = (MyListView) findViewById(R.id.mealCarListView);
		if (mMealCarList == null) {
			mMealCarList = new ArrayList<MealCar>();
		}
		mOrderProductListAdapter = new OrderProductListAdapter(
				OrderDetailActivity.this, mMealCarList);
		mMealCarListView.setAdapter(mOrderProductListAdapter);
		if (BaseApplication.getInstance().getCstID() != 0) {
			orderQuery();
		}
	}

	private void initTvUI() {
		if (state == 0 || state == -1 || state == 5) {// 0待提交 或 -1已退订 或 5已完成
			mTv1.setVisibility(View.GONE);
			mTv2.setVisibility(View.GONE);
			mTv.setText("删除");
		} else if (state == 1) {// 待支付
			mTv1.setText("删除");
			mTv2.setText("支付");
			mTv.setVisibility(View.GONE);
		} else if (state == 2) {// 制作中
			mTv1.setVisibility(View.GONE);
			mTv2.setVisibility(View.GONE);
			Log.i(TAG, "取餐时间: " + date);
			endDate = DateUtils.stringToDate2(date);
			// 获得两个时间的毫秒时间差异
			diff = endDate.getTime() - System.currentTimeMillis();
			if (diff >= timeDifference) {
				mTv.setText("退订");
			} else {
				// mTv.setText("烹饪中...");
				mTv.setVisibility(View.GONE);
			}
		} else if (state == 3) {// 待领取
			mFl.setVisibility(View.GONE);
		} else if (state == 4) {// 待点评
			mTv1.setVisibility(View.GONE);
			mTv2.setVisibility(View.GONE);
			mTv.setText("点评");
		}
		mTv1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				orderDelete();
			}
		});
		mTv2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				tokenFetch("submitOrder");
			}
		});
		mTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (state == 0 || state == -1 || state == 5) {// 0待提交 或 -1已退订 或
																// 5已完成
					orderDelete();
				} else if (state == 2) {// 制作中
					if (diff >= timeDifference) {
						dialog = new MyCommonDialog(OrderDetailActivity.this,
								"提示消息", "确定要退订" + mealCate + "吗？", "取消", "确定");
						dialog.setOkListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								if (dialog.isShowing()) {
									dialog.dismiss();
								}
								tokenFetch("unSubscribe");
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
					}
				} else if (state == 4) {// 待点评
					Intent intent = new Intent();
					intent.putExtra(OrderDetailActivity.ORDID, ordId);// 订单号
					intent.setClass(OrderDetailActivity.this,
							OrderCommentActivity.class);
					startActivity(intent);
				}
			}
		});
	}

	private void orderQuery() {
		showMyDialog(true);
		JSONObject jsonObject = new JSONObject();
		try {
			int cstId = BaseApplication.getInstance().getCstID();
			jsonObject.put("cstId", cstId);// 客户ID
			jsonObject.put("ordId", ordId);// 订单号
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String url = WDStringRequest.getUrl(Consts.SERVER_orderQuery,
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
								mOrdIdTv.setText(ordId + " ");// 订单号

								mealCate = body.optString("dinnerDate") + " "
										+ body.optString("mealCateName");
								mMealCateTv.setText(mealCate);// 餐别

								mTakeTimeTv.setText(body
										.optString("takeTimeValue"));// 取餐时间

								date = body.optString("dinnerDate") + " "
										+ body.optString("takeTimeValue")
										+ ":59";// 取餐时间

								String takePointName = body
										.optString("takePointName");
								if (!TextUtils.isEmpty(takePointName)) {
									mTakePointTv.setText(takePointName);// 取餐点
								}
								mobileNo = body.optString("mobileNo");
								if (!TextUtils.isEmpty(mobileNo)) {
									mMobileNoTv.setText(mobileNo);// 联系号码
								}
								dinnerNum = body.optInt("dinnerNum");
								if (dinnerNum != 0) {
									mDinnerNumTv.setText(String
											.valueOf(dinnerNum) + "人");// 用餐人数
								}
								totalPrice = body.optDouble("totalPrice");
								mTotalPriceTv.setText(totalPrice + "元");// 订单总价
								vAmount = body.optDouble("voucherAmount");// 代金券金额
								mVoucherTv.setText(vAmount + "元");
								waitPay = body.optDouble("realPay");// 待支付 or 实付
								mPayTv.setText(waitPay + "元");
								List<MealCar> list = MealCar.parseJson(body
										.optJSONArray("prdList"));
								mMealCarList.clear();
								mMealCarList.addAll(list);
								mOrderProductListAdapter.notifyDataSetChanged();
								initTvUI();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						StatusUtils.handleError(arg0, OrderDetailActivity.this);
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
								if (method.equals("unSubscribe")) {
									unSubscribe();
								} else if (method.equals("submitOrder")) {
									submitOrder();
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						StatusUtils.handleError(arg0, OrderDetailActivity.this);
					}
				});
		BaseApplication.getInstance().addToRequestQueue(mRequest, "");
	}

	private void orderDelete() {
		JSONObject jsonObject = new JSONObject();
		try {
			int cstId = BaseApplication.getInstance().getCstID();
			jsonObject.put("cstId", cstId);// 客户ID
			jsonObject.put("ordId", ordId);// 订单号
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String url = WDStringRequest.getUrl(Consts.SERVER_orderDelete,
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
							if (jsonObject != null) {
								code = jsonObject.getInt("code");
								if (code == 0) {
									Toast.makeText(OrderDetailActivity.this,
											"删除成功", Toast.LENGTH_SHORT).show();
									finish();
								} else {
									Toast.makeText(OrderDetailActivity.this,
											"删除失败", Toast.LENGTH_SHORT).show();
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						StatusUtils.handleError(arg0, OrderDetailActivity.this);
					}
				});
		BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
	}

	private void unSubscribe() {
		JSONObject jsonObject = new JSONObject();
		try {
			int cstId = BaseApplication.getInstance().getCstID();
			jsonObject.put("cstId", cstId);// 客户ID
			jsonObject.put("ordId", ordId);// 订单号
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String url = WDStringRequest.getUrl(Consts.SERVER_unSubscribe,
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
									Toast.makeText(OrderDetailActivity.this,
											"退订成功", Toast.LENGTH_SHORT).show();
									finish();
								} else if (code == 10406) {
									Toast.makeText(OrderDetailActivity.this,
											"抱歉，已过退订截止时间，无法退订",
											Toast.LENGTH_SHORT).show();
								} else if (code == 10407) {
									Toast.makeText(OrderDetailActivity.this,
											"退订失败，可联系客服协助处理",
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
						StatusUtils.handleError(arg0, OrderDetailActivity.this);
					}
				});
		BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
	}

	private void submitOrder() {
		JSONObject jsonObject = new JSONObject();
		try {
			int cstId = BaseApplication.getInstance().getCstID();
			jsonObject.put("cstId", cstId);// 客户ID
			jsonObject.put("ordId", ordId);// 订单号
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String url = WDStringRequest.getUrl(Consts.SERVER_submitOrder,
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
								payId = body.optInt("payId");
								totalPrice = body.optDouble("totalPrice");
								vAmount = body.optDouble("vAmount");
								waitPay = body.optDouble("waitPay");
								Intent intent = new Intent();
								intent.setClass(OrderDetailActivity.this,
										OrderPayActivity.class);
								intent.putExtra(OrderPayActivity.FROM, 1);
								intent.putExtra(OrderPayActivity.PAYID, payId);
								intent.putExtra(OrderPayActivity.TOTALAMOUNT,
										totalPrice);
								intent.putExtra(OrderPayActivity.VOUAMOUNT,
										vAmount);
								intent.putExtra(OrderPayActivity.WAITPAY,
										waitPay);
								startActivity(intent);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						StatusUtils.handleError(arg0, OrderDetailActivity.this);
					}
				});
		BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
	}

	private void showMyDialog(boolean isCancelable) {
		mProgressHUD = ProgressHUD.show(OrderDetailActivity.this, "正在加载", true,
				isCancelable, null);
	}

}