package com.homework.teacher.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.homework.teacher.Consts;
import com.homework.teacher.R;
import com.homework.teacher.adapter.MealCarAdapter;
import com.homework.teacher.adapter.MealTimeAdapter;
import com.homework.teacher.app.BaseApplication;
import com.homework.teacher.data.MealCar;
import com.homework.teacher.data.MealTime;
import com.homework.teacher.http.WDStringRequest;
import com.homework.teacher.utils.StatusUtils;
import com.homework.teacher.widget.MyCommonDialog;
import com.homework.teacher.widget.MyListView;
import com.homework.teacher.widget.ProgressHUD;

/**
 * 确认订单页
 * 
 * @author zhangkc
 * 
 */
public class OrderConfirmActivity extends Activity {
	private final static String TAG = OrderConfirmActivity.class.getName();
	private ProgressHUD mProgressHUD;
	private final static String TOKEN = "token";
	private final static String TAKEPOINTID = "takePointId";
	private final static String TAKEPOINTNAME = "takePointName";
	protected BaseApplication mApp;
	private SharedPreferences sp;
	private TextView mMealCateTv, mTakePointTv, mMobileNoTv, mClearMealCarTv,
			mTotalPriceTv, mVoucherTv, mPayTv, mWaitPayTv, mCommitOrderTv;
	private TextView mDecreaseDinnerNumTv, mDinnerNumTv, mIncreaseDinnerNumTv;

	private GridView mMealTimeGirdView;
	private List<MealTime> mMealTimeList;
	private MealTimeAdapter mMealTimeAdapter;

	private MyListView mMealCarListView;
	private List<MealCar> mMealCarList;
	private MealCarAdapter mMealCarAdapter;
	private int mealCateId;// 餐别ID
	private int takeTimeId;// 取餐时间ID
	private int takePointId;// 取餐点ID
	private String mobileNo;// 联系手机
	private int dinnerNum = 1;// 用餐人数
	// 新增了payId字段，用于支付平台的后续流程，对应支付平台的out_trade_no
	private int payId;// 付款单ID
	private double totalPrice;// 订单总价
	private double vAmount;// 代金券金额
	private double waitPay;// 待支付金额
	@SuppressLint("HandlerLeak")
	private Handler myHandler = new Handler() {
		// 接收到消息后处理
		public void handleMessage(Message msg) {
			Bundle bundle = msg.getData();
			if (bundle != null) {
				totalPrice = bundle.getDouble("totalPrice", 0.0);
				vAmount = bundle.getDouble("vAmount", 0.0);
				waitPay = bundle.getDouble("waitPay", 0.0);
				Log.i(TAG, "totalPrice: " + totalPrice + " , vAmount: "
						+ vAmount + " , waitPay: " + waitPay);
				mTotalPriceTv.setText(totalPrice + "元");
				mVoucherTv.setText(vAmount + "元");
				mPayTv.setText(waitPay + "元");
				mWaitPayTv.setText(waitPay + "元");
			}
			if (msg.arg1 != 0) {
				if (mMealCarList.size() > 1) {
					for (int i = 0; i < mMealCarList.size(); i++) {
						if (mMealCarList.get(i).getPrdId() == msg.arg1) {
							mMealCarList.remove(mMealCarList.get(i));
							mMealCarAdapter.notifyDataSetChanged();
						}
					}
				} else {
					finish();
				}
			}
		}
	};
	private MyCommonDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_confirmorder);

		mApp = BaseApplication.getInstance();
		sp = mApp.getSp();
		((Button) findViewById(R.id.back))
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
		((TextView) findViewById(R.id.title)).setText(R.string.confirm_order);
		((TextView) findViewById(R.id.tvSet)).setVisibility(View.GONE);

		mMealCateTv = ((TextView) findViewById(R.id.mealCateTv));
		mTakePointTv = ((TextView) findViewById(R.id.takePointTv));
		String lastMealPlaceName = BaseApplication.getInstance()
				.getLastMealPlaceName();
		takePointId = BaseApplication.getInstance().getLastMealPlaceID();
		Log.i(TAG, "本地缓存取餐点名称：" + lastMealPlaceName + " , 本地缓存取餐点ID："
				+ takePointId);
		if (!TextUtils.isEmpty(lastMealPlaceName)) {
			mTakePointTv.setText(lastMealPlaceName);// 显示本地缓存的取餐点
		}
		mTakePointTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent(OrderConfirmActivity.this,
						SelectMealPlaceActivity.class), 0);
			}
		});
		mMobileNoTv = ((TextView) findViewById(R.id.mobileNoTv));
		mDecreaseDinnerNumTv = ((TextView) findViewById(R.id.decreaseDinnerNumTv));
		mDecreaseDinnerNumTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (dinnerNum > 1) {
					dinnerNum--;
					mDinnerNumTv.setText(String.valueOf(dinnerNum));
					tokenFetch("saveOrder");
				} else if (dinnerNum == 1) {
					dialog = new MyCommonDialog(OrderConfirmActivity.this,
							"提示消息", "用餐人数为0，将不为您提供餐具", "取消", "确定");
					dialog.setOkListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							if (dialog.isShowing()) {
								dialog.dismiss();
							}
							dinnerNum--;
							mDinnerNumTv.setText(String.valueOf(dinnerNum));
							tokenFetch("saveOrder");
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
			}
		});
		mDinnerNumTv = ((TextView) findViewById(R.id.dinnerNumTv));
		mIncreaseDinnerNumTv = ((TextView) findViewById(R.id.increaseDinnerNumTv));
		mIncreaseDinnerNumTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dinnerNum++;
				mDinnerNumTv.setText(String.valueOf(dinnerNum));
				tokenFetch("saveOrder");
			}
		});
		mClearMealCarTv = ((TextView) findViewById(R.id.clearMealCarTv));
		mClearMealCarTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog = new MyCommonDialog(OrderConfirmActivity.this, "提示消息",
						"您确认清空购物车吗？", "取消", "确定");
				dialog.setOkListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (dialog.isShowing()) {
							dialog.dismiss();
						}
						tokenFetch("clearCart");// 清空购物车
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
		});
		mTotalPriceTv = ((TextView) findViewById(R.id.totalPriceTv));
		mVoucherTv = ((TextView) findViewById(R.id.voucherTv));
		mPayTv = ((TextView) findViewById(R.id.payTv));
		mWaitPayTv = ((TextView) findViewById(R.id.waitPayTv));
		mCommitOrderTv = ((TextView) findViewById(R.id.commitOrderTv));
		mCommitOrderTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String takePointName = mTakePointTv.getText().toString();
				if (!TextUtils.isEmpty(takePointName)) {
					tokenFetch("submitOrder");
				} else {
					Toast.makeText(OrderConfirmActivity.this, "取餐点不能为空，请选择取餐点",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		mMealTimeGirdView = (GridView) findViewById(R.id.mealTimeList);
		if (mMealTimeList == null) {
			mMealTimeList = new ArrayList<MealTime>();
		}
		mMealTimeAdapter = new MealTimeAdapter(OrderConfirmActivity.this, 0,
				mMealTimeList);
		mMealTimeGirdView.setAdapter(mMealTimeAdapter);
		mMealTimeGirdView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						mMealTimeAdapter = new MealTimeAdapter(
								OrderConfirmActivity.this, position,
								mMealTimeList);
						mMealTimeAdapter.notifyDataSetChanged();
						mMealTimeGirdView.setAdapter(mMealTimeAdapter);
						takeTimeId = mMealTimeList.get(position).getId();
						Log.i(TAG, "选择的takeTimeId: " + takeTimeId);
						tokenFetch("saveOrder");
					}
				});
		mMealCarListView = (MyListView) findViewById(R.id.mealCarListView);
		if (mMealCarList == null) {
			mMealCarList = new ArrayList<MealCar>();
		}
		mMealCarAdapter = new MealCarAdapter(OrderConfirmActivity.this,
				myHandler, mMealCarList, sp);
		mMealCarListView.setAdapter(mMealCarAdapter);
		if (BaseApplication.getInstance().getCstID() != 0) {
			orderQuery();
		}
	}

	private void orderQuery() {
		showMyDialog(true);
		JSONObject jsonObject = new JSONObject();
		try {
			int cstId = BaseApplication.getInstance().getCstID();
			jsonObject.put("cstId", cstId);// 客户ID
			int ordId = BaseApplication.getInstance().getORDID();
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
								mMealCateTv.setText(body
										.optString("dinnerDate")
										+ " "
										+ body.optString("mealCateName"));// 餐别
								mealCateId = body.optInt("mealCateId");
								getTakeTime();// 取餐时间
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
								mDinnerNumTv.setText(String.valueOf(dinnerNum));// 用餐人数
								totalPrice = body.optDouble("totalPrice");
								mTotalPriceTv.setText(totalPrice + "元");// 订单总价
								vAmount = body.optDouble("voucherAmount");// 代金券金额
								mVoucherTv.setText(vAmount + "元");
								waitPay = body.optDouble("realPay");// 待支付 or 实付
								mPayTv.setText(waitPay + "元");
								mWaitPayTv.setText(waitPay + "元");
								List<MealCar> list = MealCar.parseJson(body
										.optJSONArray("prdList"));
								mMealCarList.clear();
								mMealCarList.addAll(list);
								mMealCarAdapter.notifyDataSetChanged();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						StatusUtils
								.handleError(arg0, OrderConfirmActivity.this);
					}
				});
		BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
	}

	private void getTakeTime() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("mealCateId", mealCateId);// 餐别ID
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String url = WDStringRequest.getUrl(Consts.SERVER_getTakeTime,
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
								mMealTimeList.clear();
								mMealTimeList.addAll(MealTime.parseJson(body));
								if (mMealTimeList != null
										&& mMealTimeList.size() > 0) {
									takeTimeId = mMealTimeList.get(0).getId();
								}
								mMealTimeAdapter.notifyDataSetChanged();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						StatusUtils
								.handleError(arg0, OrderConfirmActivity.this);
					}
				});
		BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {
			Bundle bundle = data.getExtras();
			switch (resultCode) {
			case 1:
				String takePointName = bundle.getString(TAKEPOINTNAME);
				mTakePointTv.setText(takePointName);
				sp.edit().putString(TAKEPOINTNAME, takePointName).commit();
				takePointId = bundle.getInt(TAKEPOINTID);
				sp.edit().putInt(TAKEPOINTID, takePointId).commit();
				tokenFetch("saveOrder");
				break;
			default:
				break;
			}
		}
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
								if (method.equals("saveOrder")) {
									saveOrder();
								} else if (method.equals("clearCart")) {
									clearCart();
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
						StatusUtils
								.handleError(arg0, OrderConfirmActivity.this);
					}
				});
		BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
	}

	private void saveOrder() {
		JSONObject jsonObject = new JSONObject();
		try {
			int cstId = BaseApplication.getInstance().getCstID();
			jsonObject.put("cstId", cstId);// 客户ID
			int ordId = BaseApplication.getInstance().getORDID();
			jsonObject.put("ordId", ordId);// 订单号
			jsonObject.put("takeTimeId", takeTimeId);// 取餐时间ID
			jsonObject.put("takePointId", takePointId);// 取餐点ID
			jsonObject.put("mobileNo", mobileNo);// 联系手机
			jsonObject.put("dinnerNum", dinnerNum);// 用餐人数
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String url = WDStringRequest
				.getUrl(Consts.SERVER_saveOrder, jsonObject);
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
								.handleError(arg0, OrderConfirmActivity.this);
					}
				});
		BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
	}

	private void clearCart() {
		JSONObject jsonObject = new JSONObject();
		try {
			int cstId = BaseApplication.getInstance().getCstID();
			jsonObject.put("cstId", cstId);// 客户ID
			int ordId = BaseApplication.getInstance().getORDID();
			jsonObject.put("ordId", ordId);// 订单号
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String url = WDStringRequest
				.getUrl(Consts.SERVER_clearCart, jsonObject);
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
									mMealCarList.clear();
									mMealCarAdapter.notifyDataSetChanged();
									totalPrice = 0;
									mTotalPriceTv.setText(totalPrice + "元");
									waitPay = 0;
									mPayTv.setText(waitPay + "元");
									mWaitPayTv.setText("￥" + waitPay);
									finish();
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
								.handleError(arg0, OrderConfirmActivity.this);
					}
				});
		BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
	}

	private void submitOrder() {
		JSONObject jsonObject = new JSONObject();
		try {
			int cstId = BaseApplication.getInstance().getCstID();
			jsonObject.put("cstId", cstId);// 客户ID
			int ordId = BaseApplication.getInstance().getORDID();
			jsonObject.put("ordId", ordId);// 订单号
			jsonObject.put("takeTimeId", takeTimeId);// 取餐时间ID
			jsonObject.put("takePointId", takePointId);// 取餐点ID
			jsonObject.put("mobileNo", mobileNo);// 联系手机
			jsonObject.put("dinnerNum", dinnerNum);// 用餐人数
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
								intent.setClass(OrderConfirmActivity.this,
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
						StatusUtils
								.handleError(arg0, OrderConfirmActivity.this);
					}
				});
		BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
	}

	private void showMyDialog(boolean isCancelable) {
		mProgressHUD = ProgressHUD.show(OrderConfirmActivity.this, "正在加载",
				true, isCancelable, null);
	}
}