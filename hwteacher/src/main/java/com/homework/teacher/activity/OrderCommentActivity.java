package com.homework.teacher.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.homework.teacher.Consts;
import com.homework.teacher.R;
import com.homework.teacher.adapter.ProductCommentAdapter;
import com.homework.teacher.app.BaseApplication;
import com.homework.teacher.data.ProductComment;
import com.homework.teacher.http.WDStringRequest;
import com.homework.teacher.utils.StatusUtils;

/**
 * 订单点评页
 * 
 * @author zhangkc
 * @date 2017-6-26
 */
public class OrderCommentActivity extends Activity {
	private final static String TAG = OrderCommentActivity.class.getName();
	private final static String TOKEN = "token";
	public final static String ORDID = "ordId";
	private SharedPreferences mSp;
	private ListView mProductCommentListView;
	private List<ProductComment> mProductCommentList;
	private ProductCommentAdapter mProductCommentAdapter;
	private TextView mTv;
	private int ordId;// 订单号
	private int evaluationLv;// 综合评分 1：差评，2：中评，3：好评
	private String evaluationDesc;// 综合评价
	private int disEvaluationLv;// 配送评分 1：差评，2：中评，3：好评
	private String disEvaluationDesc;// 配送评价
//	private boolean isAllGoodEvaluation;// 是否全部好评
	private int integral;// 积分

	private int position;
	private int lv;
	private String desc;
	@SuppressLint("HandlerLeak")
	private Handler myHandler = new Handler() {
		// 接收到消息后处理
		public void handleMessage(Message msg) {
			Bundle bundle = msg.getData();
			if (bundle != null) {
				position = bundle.getInt("position", 0);
				lv = bundle.getInt("lv", 0);
				desc = bundle.getString("desc");
				Log.i(TAG, "position: " + position + " , lv: " + lv
						+ " , desc: " + desc);
				if (lv == 0) {// 评价处理
					mProductCommentList.get(position)
							.setPrdEvaluationDesc(desc);
				} else {// 评分处理
					mProductCommentList.get(position).setPrdEvaluationLv(lv);
				}
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ordercomment);

		if (getIntent() != null) {
			ordId = getIntent().getIntExtra(ORDID, 0);
			Log.i(TAG, "ordId: " + ordId);
		}
		((Button) findViewById(R.id.back))
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
		((TextView) findViewById(R.id.title)).setText(R.string.order_comment);
		((TextView) findViewById(R.id.tvSet)).setText("全部好评");
		((TextView) findViewById(R.id.tvSet)).setVisibility(View.GONE);
		((TextView) findViewById(R.id.tvSet))
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
//						isAllGoodEvaluation = true;
//						tokenFetch();
						for (int i = 0; i < mProductCommentList.size(); i++) {
							mProductCommentList.get(i).setPrdEvaluationLv(3);
						}
						mProductCommentAdapter.notifyDataSetChanged();
					}
				});
		mSp = BaseApplication.getInstance().getSp();
		mProductCommentListView = (ListView) findViewById(R.id.productCommentListView);
		if (mProductCommentList == null) {
			mProductCommentList = new ArrayList<ProductComment>();
		}
		mProductCommentAdapter = new ProductCommentAdapter(
				OrderCommentActivity.this, myHandler, mProductCommentList);
		mProductCommentListView.setAdapter(mProductCommentAdapter);
		mTv = (TextView) findViewById(R.id.tv);
		mTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				tokenFetch();
			}
		});
		if (BaseApplication.getInstance().getCstID() != 0) {
			orderQuery();
		}
	}

	private void orderQuery() {
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
								List<ProductComment> list = ProductComment
										.parseJson(body);
								mProductCommentList.clear();
								mProductCommentList.addAll(list);
								mProductCommentAdapter.notifyDataSetChanged();
								((TextView) findViewById(R.id.tvSet))
										.setVisibility(View.VISIBLE);
								mTv.setVisibility(View.VISIBLE);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						StatusUtils
								.handleError(arg0, OrderCommentActivity.this);
					}
				});
		BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
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
								mSp.edit()
										.putString(TOKEN,
												body.optString("token"))
										.commit();
								Log.i(TAG, "接口访问令牌存入本地~~~");
								evaSubmit();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						StatusUtils
								.handleError(arg0, OrderCommentActivity.this);
					}
				});
		BaseApplication.getInstance().addToRequestQueue(mRequest, "");
	}

	private void evaSubmit() {
		JSONObject jsonObject = new JSONObject();
		try {
			int cstId = BaseApplication.getInstance().getCstID();
			jsonObject.put("cstId", cstId);// 客户ID
			jsonObject.put("ordId", ordId);// 订单号
//			if (isAllGoodEvaluation) {
//				jsonObject.put("evaluationLv", 3);// 综合评分 1：差评，2：中评，3：好评
//				jsonObject.put("evaluationDesc", "");// 综合评价
//				jsonObject.put("disEvaluationLv", 3);// 配送评分 1：差评，2：中评，3：好评
//				jsonObject.put("disEvaluationDesc", "");// 配送评价
//				JSONArray prdList = new JSONArray();
//				for (int i = 2; i < mProductCommentList.size(); i++) {
//					mProductCommentList.get(i).setPrdEvaluationLv(3);
//					mProductCommentList.get(i).setPrdEvaluationDesc("");
//					JSONObject jsonObject1 = new JSONObject();
//					jsonObject1.put("ordId", mProductCommentList.get(i)
//							.getOrdId());
//					jsonObject1.put("cityPrdId", mProductCommentList.get(i)
//							.getCityPrdId());
//					jsonObject1.put("prdId", mProductCommentList.get(i)
//							.getPrdId());
//					jsonObject1.put("prdEvaluationLv",
//							mProductCommentList.get(i).getPrdEvaluationLv());
//					jsonObject1.put("prdEvaluationDesc", mProductCommentList
//							.get(i).getPrdEvaluationDesc());
//					prdList.put(jsonObject1);
//				}
//				jsonObject.put("prdList", prdList);// 产品评价
//			} else {
				evaluationLv = mProductCommentList.get(0).getPrdEvaluationLv();
				if (evaluationLv != 0) {
					jsonObject.put("evaluationLv", evaluationLv);// 综合评分 1 2 3
				} else {
					jsonObject.put("evaluationLv", 0);
				}

				evaluationDesc = mProductCommentList.get(0)
						.getPrdEvaluationDesc();
				if (evaluationDesc != null) {
					jsonObject.put("evaluationDesc", evaluationDesc);// 综合评价
				} else {
					jsonObject.put("evaluationDesc", "");
				}

				disEvaluationLv = mProductCommentList.get(1)
						.getPrdEvaluationLv();
				if (disEvaluationLv != 0) {
					jsonObject.put("disEvaluationLv", disEvaluationLv);// 配送评分 1
																		// 2 3
				} else {
					jsonObject.put("disEvaluationLv", 0);
				}

				disEvaluationDesc = mProductCommentList.get(1)
						.getPrdEvaluationDesc();
				if (disEvaluationDesc != null) {
					jsonObject.put("disEvaluationDesc", disEvaluationDesc);// 配送评价
				} else {
					jsonObject.put("disEvaluationDesc", "");
				}
				JSONArray prdList = new JSONArray();
				for (int i = 2; i < mProductCommentList.size(); i++) {
					JSONObject jsonObject1 = new JSONObject();
					jsonObject1.put("ordId", mProductCommentList.get(i)
							.getOrdId());
					jsonObject1.put("cityPrdId", mProductCommentList.get(i)
							.getCityPrdId());
					jsonObject1.put("prdId", mProductCommentList.get(i)
							.getPrdId());
					if (mProductCommentList.get(i).getPrdEvaluationLv() != 0) {
						jsonObject1.put("prdEvaluationLv", mProductCommentList
								.get(i).getPrdEvaluationLv());
					} else {
						jsonObject1.put("prdEvaluationLv", 0);
					}
					if (mProductCommentList.get(i).getPrdEvaluationDesc() != null) {
						jsonObject1.put("prdEvaluationDesc",
								mProductCommentList.get(i)
										.getPrdEvaluationDesc());
					} else {
						jsonObject1.put("prdEvaluationDesc", "");
					}
					prdList.put(jsonObject1);
				}
				jsonObject.put("prdList", prdList);// 产品评价
//			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String url = WDStringRequest
				.getUrl(Consts.SERVER_evaSubmit, jsonObject);
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
									Toast.makeText(OrderCommentActivity.this,
											"订单评价成功", Toast.LENGTH_SHORT)
											.show();
									body = new JSONObject(
											jsonObject.getString("body"));
									if (body != null) {
										integral = body.optInt("integral");
										Log.i(TAG, "integral: " + integral);
									}
									finish();
								} else if (code == 10403) {
									Toast.makeText(OrderCommentActivity.this,
											"请选择综合评分", Toast.LENGTH_SHORT)
											.show();
								} else if (code == 10404) {
									Toast.makeText(OrderCommentActivity.this,
											"请选择配送评分", Toast.LENGTH_SHORT)
											.show();
								} else if (code == 10405) {
									Toast.makeText(OrderCommentActivity.this,
											"请选择产品评分", Toast.LENGTH_SHORT)
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
						StatusUtils
								.handleError(arg0, OrderCommentActivity.this);
					}
				});
		BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
	}

}