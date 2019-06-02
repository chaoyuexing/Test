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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.homework.teacher.Consts;
import com.homework.teacher.R;
import com.homework.teacher.adapter.OrderBatchListAdapter;
import com.homework.teacher.app.BaseApplication;
import com.homework.teacher.data.OrderDetail;
import com.homework.teacher.http.WDStringRequest;
import com.homework.teacher.utils.StatusUtils;

/**
 * 一周食谱-批量下单
 * 
 * @author zhangkc
 * 
 */
public class BatchOrderActivity extends Activity {
	private final static String TAG = BatchOrderActivity.class.getName();
	public final static String STARTORDERDATE = "startOrderDate";
	public final static String ENDORDERDATE = "endOrderDate";

	private PullToRefreshListView mOrderListView;
	private List<OrderDetail> orderList;
	private OrderBatchListAdapter mOrderListAdapter;
	private TextView mLunchTv, mSupperTv, mEmptyTv;
	private Button mPayBtn;
	private long curPageIndex = 1;
	private final int pageSize = 20;
	private int state;// 订单状态 0待提交， 1：待支付，2：制作中，3：待领取，4：待点评，5：已完成，-1：已退订
	private int mealCateId = 10;// 餐别ID：午餐 10 晚餐 12
	private String bgnDate;// 订餐开始日期
	private String endDate;// 订餐结束日期
	private int addType = 2;// 下单类型 1：单个，2：批量 （订单模块不传，一周食谱-批量下单时要传）
	@SuppressLint("HandlerLeak")
	private Handler myHandler = new Handler() {
		// 接收到消息后处理
		public void handleMessage(Message msg) {
			curPageIndex = 1;
			orderList.clear();
			listByState(state);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_batchorder);

		if (getIntent() != null) {
			bgnDate = getIntent().getStringExtra(STARTORDERDATE);
			endDate = getIntent().getStringExtra(ENDORDERDATE);
			Log.i(TAG, "bgnDate: " + bgnDate + " , endDate: " + endDate);
		}
		((Button) findViewById(R.id.back))
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
		((TextView) findViewById(R.id.title)).setText(R.string.batch_order);
		((TextView) findViewById(R.id.tvSet)).setVisibility(View.GONE);
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
				curPageIndex = 1;
				orderList.clear();
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
				curPageIndex = 1;
				orderList.clear();
				listByState(state);
			}
		});
		mPayBtn = (Button) findViewById(R.id.payBtn);
		mPayBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra(OrderPayActivity.FROM, 2);
				intent.putExtra(OrderPayActivity.STATE, 0);
				intent.putExtra(OrderPayActivity.STARTORDERDATE, bgnDate);
				intent.putExtra(OrderPayActivity.ENDORDERDATE, endDate);
				intent.setClass(BatchOrderActivity.this, OrderPayActivity.class);
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
				curPageIndex = 1;
				orderList.clear();
				listByState(state);
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				curPageIndex++;
				listByState(state);
			}
		});
		mOrderListAdapter = new OrderBatchListAdapter(BatchOrderActivity.this,
				myHandler, orderList);
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
						intent.setClass(BatchOrderActivity.this,
								OrderDetailActivity.class);
						startActivity(intent);
					}
				});
		mEmptyTv = (TextView) findViewById(R.id.emptyTv);
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.e(TAG, "onResume");
		curPageIndex = 1;
		orderList.clear();
		listByState(state);
	}

	private void listByState(final int state) {
		JSONObject jsonObject = new JSONObject();
		try {
			int cstId = BaseApplication.getInstance().getCstID();
			jsonObject.put("cstId", cstId);// 客户ID
			jsonObject.put("state", state);// 订单状态
			jsonObject.put("mealCateId", mealCateId);// 餐别
			jsonObject.put("bgnDate", bgnDate);// 订餐开始日期
			jsonObject.put("endDate", endDate);// 订餐结束日期
			jsonObject.put("addType", addType);// 下单类型
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
								orderList.addAll(OrderDetail.parseJson(body));

								if (orderList != null) {
									if (orderList.size() == 0) {
										mEmptyTv.setVisibility(View.VISIBLE);
										mEmptyTv.setText(R.string.orders_null);
										mPayBtn.setVisibility(View.GONE);
									} else if (orderList.size() > 0) {
										if (((orderList.size()) % pageSize) == 0) {
											mOrderListView.setMode(Mode.BOTH);
										} else {
											mOrderListView
													.setMode(Mode.PULL_FROM_START);
										}
										mEmptyTv.setVisibility(View.GONE);
										mPayBtn.setVisibility(View.VISIBLE);
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
						StatusUtils.handleError(arg0, BatchOrderActivity.this);
					}
				});
		BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
	}

}