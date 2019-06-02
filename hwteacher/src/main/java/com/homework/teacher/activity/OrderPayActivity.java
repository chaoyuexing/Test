package com.homework.teacher.activity;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.homework.teacher.Consts;
import com.homework.teacher.R;
import com.homework.teacher.app.BaseApplication;
import com.homework.teacher.data.PayResult;
import com.homework.teacher.http.WDStringRequest;
import com.homework.teacher.utils.StatusUtils;
import com.homework.teacher.widget.ProgressHUD;

/**
 * 订单结算页
 * 
 * @author zhangkc
 * 
 */
public class OrderPayActivity extends Activity {
	private final static String TAG = OrderPayActivity.class.getName();
	private ProgressHUD mProgressHUD;
	public final static String FROM = "from";
	public final static String PAYID = "payId";
	public final static String TOTALAMOUNT = "totalAmount";
	public final static String VOUAMOUNT = "vouAmount";
	public final static String WAITPAY = "waitPay";
	public final static String STATE = "state";
	public final static String STARTORDERDATE = "startOrderDate";
	public final static String ENDORDERDATE = "endOrderDate";
	private TextView mOrdNumTv, mTotalAmountTv, mVouNumTv, mVouAmountTv,
			mWaitPayTv, mAliPayCheckTv, mWechatPayCheckTv;

	private RelativeLayout mAliPayRl, mWechatPayRl;
	private Button mConfirmPayBtn;

	private int from;
	// from=1 OrderConfirmActivity（首页单个确认订单-提交订单）或者 OrderDetailActivity
	// from=2 BatchOrderActivity（一周食谱-批量下单）
	// from=3 OrderBatchOperateActivity（订单模块待支付批量）

	private int payId;// 付款单ID
	private double totalAmount;
	private double vouAmount;
	private double waitPay;// 待支付金额（元）关联查询最新产品价格计算得出订单总价，减去代金券支付
	private int state;// 批量下单结算时传0 订单模块待支付订单结算传1
	private String bgnDate = "";// 订餐开始日期 （批量下单结算时传）
	private String endDate = "";// 订餐结束日期（批量下单结算时传）
	private int payChannel = 1;// 支付渠道 1：支付宝，2：微信
	private String payString = "";// 付款单加签字符串 对应支付宝平台的orderString
	private Handler mHandler = new Handler() {
		@SuppressWarnings("unused")
		public void handleMessage(Message msg) {
			PayResult payResult = new PayResult((Map<String, String>) msg.obj);
			/**
			 * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
			 */
			String resultInfo = payResult.getResult();// 同步返回需要验证的信息
			Log.e("resultInfo", resultInfo);
			String resultStatus = payResult.getResultStatus();
			// 判断resultStatus 为9000则代表支付成功
			if (TextUtils.equals(resultStatus, "9000")) {
				// 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
				Toast.makeText(OrderPayActivity.this, "支付成功，可在订单模块查看详情哦~",
						Toast.LENGTH_SHORT).show();
				syncPayinfo(resultInfo);
			} else {
				// 该笔订单真实的支付结果，需要依赖服务端的异步通知。
				Toast.makeText(OrderPayActivity.this, "支付失败",
						Toast.LENGTH_SHORT).show();
			}
		};

		private void syncPayinfo(String payResult) {
			JSONObject jsonObject = new JSONObject();
			try {
				int cstId = BaseApplication.getInstance().getCstID();
				jsonObject.put("cstId", cstId);// 客户ID
				jsonObject.put("payChannel", payChannel);// 支付渠道 1：支付宝，2：微信
				jsonObject.put("payResult", payResult);// 支付结果信息
			} catch (JSONException e) {
				e.printStackTrace();
			}
			String url = WDStringRequest.getUrl(Consts.SERVER_syncPayinfo,
					jsonObject);
			String relative_url = WDStringRequest.getRelativeUrl();
			String sign_body = WDStringRequest.getSignBody();

			WDStringRequest mRequest = new WDStringRequest(Request.Method.GET,
					url, relative_url, sign_body, false,
					new Response.Listener<String>() {
						@Override
						public void onResponse(String response) {
							try {
								JSONObject jsonObject = new JSONObject(response);
								int code = 0;// 响应头中的code
								if (jsonObject != null) {
									code = jsonObject.getInt("code");
									if (code == 0) {
										finish();
										Intent intent = new Intent();
										intent.setClass(OrderPayActivity.this,
												MainActivity.class);
										startActivity(intent);
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
									.handleError(arg0, OrderPayActivity.this);
						}
					});
			BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_orderpay);

		if (getIntent() != null) {
			from = getIntent().getIntExtra(FROM, 0);
			Log.i(TAG, "from: " + from);
			if (from == 1) {
				payId = getIntent().getIntExtra(PAYID, 0);
				totalAmount = getIntent().getDoubleExtra(TOTALAMOUNT, 0);
				vouAmount = getIntent().getDoubleExtra(VOUAMOUNT, 0);
				waitPay = getIntent().getDoubleExtra(WAITPAY, 0);
				Log.i(TAG, "payId: " + payId + " ,totalAmount: " + totalAmount
						+ " , vouAmount: " + vouAmount + " , waitPay: "
						+ waitPay);
			} else {// from == 2 || from ==3
				{
					state = getIntent().getIntExtra(STATE, 0);
					Log.i(TAG, "state: " + state);
					if (state == 0) {
						bgnDate = getIntent().getStringExtra(STARTORDERDATE);
						endDate = getIntent().getStringExtra(ENDORDERDATE);
					}
					Log.i(TAG, "bgnDate: " + bgnDate + " , endDate: " + endDate);
				}
			}
		}
		((Button) findViewById(R.id.back))
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
		((TextView) findViewById(R.id.title)).setText(R.string.order_pay);
		((TextView) findViewById(R.id.tvSet)).setVisibility(View.GONE);

		mOrdNumTv = (TextView) findViewById(R.id.ordNumTv);
		mTotalAmountTv = (TextView) findViewById(R.id.totalAmountTv);
		mVouNumTv = (TextView) findViewById(R.id.vouNumTv);
		mVouAmountTv = (TextView) findViewById(R.id.vouAmountTv);
		mWaitPayTv = (TextView) findViewById(R.id.waitPayTv);
		mAliPayCheckTv = (TextView) findViewById(R.id.aliPayCheckTv);
		mWechatPayCheckTv = (TextView) findViewById(R.id.wechatPayCheckTv);
		mAliPayRl = ((RelativeLayout) findViewById(R.id.aliPayRl));
		mAliPayRl.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				payChannel = 1;
				Log.i(TAG, "payChannel: " + payChannel);
				mAliPayCheckTv.setBackgroundDrawable(getResources()
						.getDrawable(R.drawable.mealcate_check));
				mWechatPayCheckTv.setBackgroundDrawable(getResources()
						.getDrawable(R.drawable.mealcate_uncheck));
			}
		});
		mWechatPayRl = ((RelativeLayout) findViewById(R.id.wechatPayRl));
		mWechatPayRl.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				payChannel = 2;
				Log.i(TAG, "payChannel: " + payChannel);
				mAliPayCheckTv.setBackgroundDrawable(getResources()
						.getDrawable(R.drawable.mealcate_uncheck));
				mWechatPayCheckTv.setBackgroundDrawable(getResources()
						.getDrawable(R.drawable.mealcate_check));
			}
		});
		mConfirmPayBtn = ((Button) findViewById(R.id.confirmPayBtn));
		mConfirmPayBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getPaymentString();
			}
		});
		if (from == 1) {
			mOrdNumTv.setText("1单");// 订单总数
			mTotalAmountTv.setText(totalAmount + "元");// 订单总额
			if (vouAmount > 0) {
				mVouNumTv.setText("1张");// 可用代金券张数
			} else {
				mVouNumTv.setText("0张");// 可用代金券张数
			}
			mVouAmountTv.setText(vouAmount + "元");// 代金券支付总额
			mWaitPayTv.setText(waitPay + "元");// 待支付金额（元）
			mConfirmPayBtn.setText("确认支付 ￥" + waitPay);
		} else {// from == 2 || from ==3
			orderSettle();
		}
	}

	private void orderSettle() {
		JSONObject jsonObject = new JSONObject();
		try {
			int cstId = BaseApplication.getInstance().getCstID();
			jsonObject.put("cstId", cstId);// 客户ID
			jsonObject.put("state", state);// 订单状态
			if (from == 2) {
				jsonObject.put("bgnDate", bgnDate);
				jsonObject.put("endDate", endDate);
				jsonObject.put("addType", 2);// 下单类型 1：单个，2：批量
												// （一周食谱-批量下单结算时传2，订单模块批量操作订单结算时不传）
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String url = WDStringRequest.getUrl(Consts.SERVER_orderSettle,
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
									body = new JSONObject(
											jsonObject.getString("body"));
								}
							}
							if (body != null) {
								payId = body.optInt("payId");// //
																// 出参增加payId，用于支付平台后续流程

								mOrdNumTv.setText(body.optInt("ordNum") + "单");// 订单总数

								totalAmount = body.optDouble("totalAmount");// 订单总额
								mTotalAmountTv.setText(totalAmount + "元");

								mVouNumTv.setText(body.optInt("vouNum") + "张");// 可用代金券张数

								vouAmount = body.optDouble("vouAmount");// 代金券支付总额
								mVouAmountTv.setText(vouAmount + "元");

								waitPay = body.optDouble("waitPay");// 待支付金额（元）
								mWaitPayTv.setText(waitPay + "元");
								mConfirmPayBtn.setText("确认支付 ￥" + waitPay);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						StatusUtils.handleError(arg0, OrderPayActivity.this);
					}
				});
		BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
	}

	private void getPaymentString() {
		mConfirmPayBtn.setEnabled(false);
		showMyDialog(true);
		JSONObject jsonObject = new JSONObject();
		try {
			int cstId = BaseApplication.getInstance().getCstID();
			jsonObject.put("cstId", cstId);// 客户ID
			jsonObject.put("payChannel", payChannel);// 支付渠道 1：支付宝，2：微信
			jsonObject.put("payId", payId);// 付款单ID
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String url = WDStringRequest.getUrl(Consts.SERVER_getPaymentString,
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
									body = new JSONObject(
											jsonObject.getString("body"));
								}
							}
							if (body != null) {
								payString = body.optString("payString");
								if (payChannel == 1) {
									payV2(payString);
								} else if (payChannel == 2) {
									JSONObject payJsonObject = new JSONObject(
											payString);
									payWX(payJsonObject);
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						mConfirmPayBtn.setEnabled(true);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						StatusUtils.handleError(arg0, OrderPayActivity.this);
					}
				});
		BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
	}

	/**
	 * 支付宝支付业务
	 * 
	 * @param payString
	 */
	public void payV2(final String payString) {
		Runnable payRunnable = new Runnable() {
			@Override
			public void run() {
				PayTask alipay = new PayTask(OrderPayActivity.this);
				Map<String, String> result = alipay.payV2(payString, true);
				Log.i("msp", result.toString());

				Message msg = new Message();
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

	/**
	 * 微信支付业务
	 * 
	 * @param payJsonObject
	 */
	public void payWX(final JSONObject payJsonObject) {
		IWXAPI api;// IWXAPI 是第三方app和微信通信的openapi接口
		api = WXAPIFactory.createWXAPI(this, Consts.APP_ID, false); // 通过WXAPIFactory工厂，获取IWXAPI的实例
		api.registerApp(Consts.APP_ID);

		PayReq req = new PayReq();
		req.appId = payJsonObject.optString("appid");
		req.partnerId = payJsonObject.optString("partnerid");
		req.prepayId = payJsonObject.optString("prepayid");
		req.nonceStr = payJsonObject.optString("noncestr");
		req.timeStamp = payJsonObject.optString("timestamp");
		req.packageValue = payJsonObject.optString("package");
		req.sign = payJsonObject.optString("sign");
		req.extData = "payJsonObject data"; // optional
		// Toast.makeText(OrderPayActivity.this, "正常调起支付", Toast.LENGTH_SHORT)
		// .show();
		api.sendReq(req);
	}

	private void showMyDialog(boolean isCancelable) {
		mProgressHUD = ProgressHUD.show(OrderPayActivity.this, "获取订单中", true,
				isCancelable, null);
	}
}