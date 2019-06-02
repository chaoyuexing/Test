package com.homework.teacher.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import com.homework.teacher.adapter.IntegralRecordAdapter;
import com.homework.teacher.app.BaseApplication;
import com.homework.teacher.data.IntegralRecord;
import com.homework.teacher.http.WDStringRequest;
import com.homework.teacher.utils.StatusUtils;

/**
 * 我的积分页
 * 
 * @author zhangkc
 * @date 2017-7-25
 */
public class MyIntegralActivity extends Activity {
	private final static String TAG = MyIntegralActivity.class.getName();
	private TextView mIntegralStateTv, mIntegralTv, mExcTv;
	private PullToRefreshListView mIntegralRecordListView;
	private List<IntegralRecord> integralRecordList;
	private IntegralRecordAdapter mIntegralRecordAdapter;
	private int integral;// 积分
	private long curPageIndex = 1;
	private final int pageSize = 20;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myintegral);

		((Button) findViewById(R.id.back))
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
		((TextView) findViewById(R.id.title)).setText(R.string.my_integral);
		((TextView) findViewById(R.id.tvSet)).setVisibility(View.GONE);

		mIntegralStateTv = (TextView) findViewById(R.id.integralStateTv);
		mIntegralStateTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MyIntegralActivity.this,
						AboutIntegralActivity.class);
				startActivity(intent);
			}
		});
		mIntegralTv = (TextView) findViewById(R.id.integralTv);
		mExcTv = (TextView) findViewById(R.id.excTv);
		mExcTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra(IntegralExcVouActivity.INTEGRAL, integral);
				intent.setClass(MyIntegralActivity.this,
						IntegralExcVouActivity.class);
				startActivity(intent);
			}
		});
		mIntegralRecordListView = (PullToRefreshListView) findViewById(R.id.integralRecordListView);
		if (integralRecordList == null) {
			integralRecordList = new ArrayList<IntegralRecord>();
		}
		mIntegralRecordListView
				.setOnRefreshListener(new OnRefreshListener2<ListView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						curPageIndex = 1;
						integralRecordList.clear();
						listIntegralRecord();
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						curPageIndex++;
						listIntegralRecord();
					}
				});
		mIntegralRecordAdapter = new IntegralRecordAdapter(
				MyIntegralActivity.this, integralRecordList);
		mIntegralRecordListView.setAdapter(mIntegralRecordAdapter);
		if (BaseApplication.getInstance().getCstID() != 0) {
			getCustomerInfo();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (BaseApplication.getInstance().getCstID() != 0) {
			getCustomerInfo();
		}
		curPageIndex = 1;
		integralRecordList.clear();
		listIntegralRecord();
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
									body = new JSONObject(
											jsonObject.getString("body"));
								}
							}
							if (body != null) {
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
						StatusUtils.handleError(arg0, MyIntegralActivity.this);
					}
				});
		BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
	}

	private void listIntegralRecord() {
		JSONObject jsonObject = new JSONObject();
		try {
			int cstId = BaseApplication.getInstance().getCstID();
			jsonObject.put("cstId", cstId);// 客户ID
			jsonObject.put("pageNo", curPageIndex);// 页码
			jsonObject.put("pageSize", pageSize);// 每页记录数
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String url = WDStringRequest.getUrl(Consts.SERVER_listIntegralRecord,
				jsonObject);
		String relative_url = WDStringRequest.getRelativeUrl();
		String sign_body = WDStringRequest.getSignBody();

		WDStringRequest mRequest = new WDStringRequest(Request.Method.GET, url,
				relative_url, sign_body, false,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						mIntegralRecordListView.onRefreshComplete();
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
								integralRecordList.addAll(IntegralRecord
										.parseJson(body));
								if (integralRecordList != null) {
									if (integralRecordList.size() > 0) {
										if (((integralRecordList.size()) % pageSize) == 0) {
											mIntegralRecordListView
													.setMode(Mode.BOTH);
										} else {
											mIntegralRecordListView
													.setMode(Mode.PULL_FROM_START);
										}
									}
									mIntegralRecordAdapter
											.notifyDataSetChanged();
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						StatusUtils.handleError(arg0, MyIntegralActivity.this);
					}
				});
		BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
	}

}
