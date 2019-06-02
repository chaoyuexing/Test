package com.homework.teacher.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.homework.teacher.Consts;
import com.homework.teacher.R;
import com.homework.teacher.activity.OrderBatchOperateActivity;
import com.homework.teacher.activity.OrderDetailActivity;
import com.homework.teacher.adapter.OrderListAdapter;
import com.homework.teacher.adapter.OrderStateListAdapter;
import com.homework.teacher.app.BaseApplication;
import com.homework.teacher.data.OrderDetail;
import com.homework.teacher.data.OrderState;
import com.homework.teacher.http.WDStringRequest;
import com.homework.teacher.utils.StatusUtils;
import com.homework.teacher.widget.ProgressHUD;

/**
 * 订单
 * 
 * @author zhangkc
 * 
 */
public class OrderFragment extends Fragment {
	private final static String TAG = "OrderFragment";
	private ProgressHUD mProgressHUD;
	private View view;
	protected BaseApplication mApp;
	private SharedPreferences sp;

	private ListView mOrderStateListView;
	private List<OrderState> orderStateList;
	private OrderStateListAdapter mOrderStateListAdapter;

	private PullToRefreshListView mOrderListView;
	private List<OrderDetail> orderList;
	private OrderListAdapter mOrderListAdapter;
	private TextView mEmptyTv;
	private long curPageIndex = 1;
	private final int pageSize = 20;
	private int state = 1;
	private String stateName;
	@SuppressLint("HandlerLeak")
	private Handler myHandler = new Handler() {
		// 接收到消息后处理
		public void handleMessage(Message msg) {
			state = msg.arg1;
			Log.i(TAG, "handleMessage: state: " + state);
			// queryStateNum();

			curPageIndex = 1;
			orderList.clear();
			listByState(state);
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.order_fragment, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.e(TAG, "onActivityCreated");
		mApp = BaseApplication.getInstance();
		sp = mApp.getSp();

		((TextView) view.findViewById(R.id.title)).setText(R.string.order_list);
		((TextView) view.findViewById(R.id.tvSet))
				.setText(R.string.batch_operation);
		((TextView) view.findViewById(R.id.tvSet)).setVisibility(View.GONE);
		((TextView) view.findViewById(R.id.tvSet))
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent();
						intent.setClass(getActivity(),
								OrderBatchOperateActivity.class);
						intent.putExtra(OrderBatchOperateActivity.STATE, state);
						intent.putExtra(OrderBatchOperateActivity.STATENAME,
								stateName);
						startActivity(intent);
					}
				});

		mOrderStateListView = (ListView) view
				.findViewById(R.id.orderStateListView);
		if (orderStateList == null) {
			orderStateList = new ArrayList<OrderState>();
		}
		mOrderStateListAdapter = new OrderStateListAdapter(getActivity(), 0,
				orderStateList);
		mOrderStateListView.setAdapter(mOrderStateListAdapter);
		mOrderStateListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						mOrderStateListAdapter = new OrderStateListAdapter(
								getActivity(), position, orderStateList);
						mOrderStateListAdapter.notifyDataSetChanged();
						mOrderStateListView.setAdapter(mOrderStateListAdapter);
						mOrderStateListView.setSelection(position);
						state = orderStateList.get(position).getState();
						stateName = orderStateList.get(position).getStateName();
						curPageIndex = 1;
						orderList.clear();
						listByState(state);
					}
				});

		mOrderListView = (PullToRefreshListView) view
				.findViewById(R.id.orderListView);
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
		mOrderListAdapter = new OrderListAdapter(getActivity(), myHandler,
				orderList, sp);
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
						intent.setClass(getActivity(),
								OrderDetailActivity.class);
						startActivity(intent);
					}
				});
		mEmptyTv = (TextView) view.findViewById(R.id.emptyTv);
		queryStateNum();
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.i(TAG, "onResume: state: " + state);
		// queryStateNum();

		curPageIndex = 1;
		orderList.clear();
		listByState(state);
	}

	private void queryStateNum() {
		JSONObject jsonObject = new JSONObject();
		try {
			int cstId = BaseApplication.getInstance().getCstID();
			jsonObject.put("cstId", cstId);// 客户ID
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String url = WDStringRequest.getUrl(Consts.SERVER_stateNum, jsonObject);
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
								orderStateList.clear();
								orderStateList.addAll(OrderState
										.parseJson(body));
								mOrderStateListAdapter.notifyDataSetChanged();
								state = orderStateList.get(0).getState();
								stateName = orderStateList.get(0)
										.getStateName();
								Log.i(TAG, "queryStateNum: state: " + state
										+ " , stateName: " + stateName);
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

	private void queryStateNumNotChangeStateValue() {
		JSONObject jsonObject = new JSONObject();
		try {
			int cstId = BaseApplication.getInstance().getCstID();
			jsonObject.put("cstId", cstId);// 客户ID
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String url = WDStringRequest.getUrl(Consts.SERVER_stateNum, jsonObject);
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
								orderStateList.clear();
								orderStateList.addAll(OrderState
										.parseJson(body));
								mOrderStateListAdapter.notifyDataSetChanged();
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

	private void listByState(final int state) {
		showMyDialog(true);
		JSONObject jsonObject = new JSONObject();
		try {
			int cstId = BaseApplication.getInstance().getCstID();
			jsonObject.put("cstId", cstId);// 客户ID
			jsonObject.put("state", state);// 订单状态
			// jsonObject.put("mealCateId", 10);// 餐别
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
						mProgressHUD.dismiss();
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
									} else if (orderList.size() > 0) {
										if (((orderList.size()) % pageSize) == 0) {
											mOrderListView.setMode(Mode.BOTH);
										} else {
											mOrderListView
													.setMode(Mode.PULL_FROM_START);
										}
										mEmptyTv.setVisibility(View.GONE);
										for (int i = 0; i < orderList.size(); i++) {
											orderList.get(i).setState(state);
										}
									}
									mOrderListAdapter.notifyDataSetChanged();
								}
							}
							queryStateNumNotChangeStateValue();
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
		mProgressHUD = ProgressHUD.show(getActivity(), "正在加载", true,
				isCancelable, null);
	}
}