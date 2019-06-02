package com.homework.teacher.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.homework.teacher.Consts;
import com.homework.teacher.R;
import com.homework.teacher.adapter.OrderBatchListAdapter;
import com.homework.teacher.app.BaseApplication;
import com.homework.teacher.data.OrderDetail;
import com.homework.teacher.http.WDStringRequest;
import com.homework.teacher.utils.StatusUtils;
import com.homework.teacher.utils.StringUtils;

/**
 * 订单批量操作页
 * 
 * @author zhangkc
 * @date 2017-7-4
 */
public class OrderBatchOperateActivity extends Activity {
	private final static String TAG = OrderBatchOperateActivity.class.getName();
	public final static String STATE = "state";
	public final static String STATENAME = "stateName";

	private PullToRefreshListView mOrderListView;
	private List<OrderDetail> orderList;
	private OrderBatchListAdapter mOrderListAdapter;
	private TextView mTvLeft, mTvSet, mLunchTv, mSupperTv, mEmptyTv;
	private Button mPayBtn;
	private long curPageIndex = 1;
	private final int pageSize = 20;
	private int state;// 订单状态 0待提交， 1：待支付，2：制作中，3：待领取，4：待点评，5：已完成，-1：已退订
	private String stateName;
	private int mealCateId = 10;// 餐别ID：午餐 10 晚餐 12
	@SuppressLint("HandlerLeak")
	private Handler myHandler = new Handler() {
		// 接收到消息后处理
		public void handleMessage(Message msg) {
			listByState(state);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_batch_operate);

		if (getIntent() != null) {
			state = getIntent().getIntExtra(STATE, 0);
			stateName = getIntent().getStringExtra(STATENAME);
			Log.i(TAG, "state: " + state + " , stateName: " + stateName);
		}
		((Button) findViewById(R.id.back))
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
		mTvLeft = ((TextView) findViewById(R.id.tvLeft));
		if (StringUtils.isEmpty(stateName)) {
			mTvLeft.setText("");
		} else {
			mTvLeft.setText(stateName);
		}
		((TextView) findViewById(R.id.title)).setVisibility(View.GONE);
		mTvSet = ((TextView) findViewById(R.id.tvSet));
		if (state == 2 || state == 3 || state == 4) {
			mTvSet.setVisibility(View.GONE);
		} else {
			mTvSet.setText("清空");
		}
		mTvSet.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				clearOrd();
			}
		});
		mLunchTv = (TextView) findViewById(R.id.lunchTv);
		mSupperTv = (TextView) findViewById(R.id.supperTv);
		mLunchTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mLunchTv.setBackgroundResource(R.drawable.meal_cate_click);
				mLunchTv.setTextColor(getResources().getColor(R.color.white));
				mSupperTv.setBackgroundResource(R.drawable.meal_cate_normal);
				mSupperTv.setTextColor(getResources().getColor(R.color.black));
				mealCateId = 10;
				listByState(state);
			}
		});
		mSupperTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mLunchTv.setBackgroundResource(R.drawable.meal_cate_normal);
				mLunchTv.setTextColor(getResources().getColor(R.color.black));
				mSupperTv.setBackgroundResource(R.drawable.meal_cate_click);
				mSupperTv.setTextColor(getResources().getColor(R.color.white));
				mealCateId = 12;
				listByState(state);
			}
		});
		mPayBtn = (Button) findViewById(R.id.payBtn);
		if (state == 1) {
			mPayBtn.setVisibility(View.VISIBLE);
		}
		mPayBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(OrderBatchOperateActivity.this,
						OrderPayActivity.class);
				intent.putExtra(OrderPayActivity.FROM, 3);
				intent.putExtra(OrderPayActivity.STATE, 1);
				startActivity(intent);
			}
		});

		mOrderListView = (PullToRefreshListView) findViewById(R.id.orderListView);
		if (orderList == null) {
			orderList = new ArrayList<OrderDetail>();
		}
		mOrderListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				orderList.clear();
				listByState(state);
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				curPageIndex++;
			}
		});
		mOrderListAdapter = new OrderBatchListAdapter(
				OrderBatchOperateActivity.this, myHandler, orderList);
		mOrderListView.setAdapter(mOrderListAdapter);
		mOrderListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Intent intent = new Intent();
						int ordId = orderList.get(position - 1).getOrdId();
						intent.putExtra(OrderDetailActivity.ORDID, ordId);// 订单号
						int state = orderList.get(position - 1).getState();
						intent.putExtra(OrderDetailActivity.STATE, state);// 订单状态
						intent.setClass(OrderBatchOperateActivity.this,
								OrderDetailActivity.class);
						startActivity(intent);
					}
				});
		mEmptyTv = (TextView) findViewById(R.id.emptyTv);
		listByState(state);
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.e(TAG, "onResume");
		listByState(state);
	}

	private void listByState(final int state) {
		JSONObject jsonObject = new JSONObject();
		try {
			int cstId = BaseApplication.getInstance().getCstID();
			jsonObject.put("cstId", cstId);// 客户ID
			jsonObject.put("state", state);// 订单状态
			jsonObject.put("mealCateId", mealCateId);// 餐别
			jsonObject.put("bgnDate", "");// 订餐开始日期
			jsonObject.put("endDate", "");// 订餐结束日期
			jsonObject.put("pageNo", curPageIndex);// 页码
			jsonObject.put("pageSize", pageSize);// 每页记录数
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String url = WDStringRequest.getUrl(Consts.SERVER_listByState,
				jsonObject);
		String relative_url = WDStringRequest.getRelativeUrl();
		String sign_body = WDStringRequest.getSignBody();

		WDStringRequest mRequest = new WDStringRequest(Request.Method.GET, url,
				relative_url, sign_body, false,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						mOrderListView.onRefreshComplete();
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
								orderList.clear();
								orderList.addAll(OrderDetail.parseJson(body));

								if (orderList != null) {
									if (orderList.size() == 0) {
										mEmptyTv.setVisibility(View.VISIBLE);
										mEmptyTv.setText(R.string.orders_null);
										mPayBtn.setVisibility(View.GONE);
									} else if (orderList.size() > 0) {
										mEmptyTv.setVisibility(View.GONE);
										if (state == 1) {
											mPayBtn.setVisibility(View.VISIBLE);
										}
										for (int i = 0; i < orderList.size(); i++) {
											orderList.get(i).setState(state);
										}
									}
									mOrderListAdapter.notifyDataSetChanged();
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
								OrderBatchOperateActivity.this);
					}
				});
		BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
	}

	private void clearOrd() {
		JSONObject jsonObject = new JSONObject();
		try {
			int cstId = BaseApplication.getInstance().getCstID();
			jsonObject.put("cstId", cstId);// 客户ID
			jsonObject.put("mcId", mealCateId);// 餐别ID
			jsonObject.put("state", state);// 订单状态
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String url = WDStringRequest.getUrl(Consts.SERVER_clearOrd, jsonObject);
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
									Toast.makeText(
											OrderBatchOperateActivity.this,
											"清空成功", Toast.LENGTH_SHORT).show();
									listByState(state);
								} else {
									Toast.makeText(
											OrderBatchOperateActivity.this,
											"清空失败", Toast.LENGTH_SHORT).show();
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
								OrderBatchOperateActivity.this);
					}
				});
		BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
	}

}