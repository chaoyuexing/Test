package com.homework.teacher.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.homework.teacher.Consts;
import com.homework.teacher.R;
import com.homework.teacher.activity.LoginActivity;
import com.homework.teacher.activity.RecommendAddActivity;
import com.homework.teacher.adapter.RecommendListAdapter;
import com.homework.teacher.adapter.RecommendNameListAdapter;
import com.homework.teacher.app.BaseApplication;
import com.homework.teacher.data.ProductRecommend;
import com.homework.teacher.data.RecommendName;
import com.homework.teacher.http.WDStringRequest;
import com.homework.teacher.utils.StatusUtils;

/**
 * 推荐食谱
 * 
 * @author zhangkc
 * @date 2017-7-19
 */
public class RecommendFragment extends Fragment {
	private final static String TAG = "RecommendFragment";
	private View view;

	private ListView mRecommendNameListView;
	private List<RecommendName> recommendNameList;
	private RecommendNameListAdapter mRecommendNameAdapter;

	private PullToRefreshListView mRecommendListView;
	private List<ProductRecommend> productRecommendList;
	private RecommendListAdapter mRecommendListAdapter;
	private TextView mLunchTv, mSupperTv, mAddTv;
	private long curPageIndex = 1;
	private final int pageSize = 20;
	private String recName;// 推荐食谱名称
	private int recId;// 推荐食谱ID
	private int mealCateId = 10;// 餐别ID：午餐 10 晚餐 12
	@SuppressLint("HandlerLeak")
	private Handler myHandler = new Handler() {
		// 接收到消息后处理
		public void handleMessage(Message msg) {
			getRecDetail(recId, mealCateId);
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.recommend_fragment, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.e(TAG, "onActivityCreated");

		mRecommendNameListView = (ListView) view
				.findViewById(R.id.recommendNameListView);
		if (recommendNameList == null) {
			recommendNameList = new ArrayList<RecommendName>();
		}
		mRecommendNameAdapter = new RecommendNameListAdapter(getActivity(), 0,
				recommendNameList);
		mRecommendNameListView.setAdapter(mRecommendNameAdapter);
		mRecommendNameListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						mRecommendNameAdapter = new RecommendNameListAdapter(
								getActivity(), position, recommendNameList);
						mRecommendNameAdapter.notifyDataSetChanged();
						mRecommendNameListView
								.setAdapter(mRecommendNameAdapter);
						mRecommendNameListView.setSelection(position);
						recName = recommendNameList.get(position).getRecName();
						recId = recommendNameList.get(position).getRecId();
						getRecDetail(recId, mealCateId);
					}
				});

		mRecommendListView = (PullToRefreshListView) view
				.findViewById(R.id.recommendListView);
		if (productRecommendList == null) {
			productRecommendList = new ArrayList<ProductRecommend>();
		}
		mRecommendListView
				.setOnRefreshListener(new OnRefreshListener2<ListView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						productRecommendList.clear();
						getRecDetail(recId, mealCateId);
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						curPageIndex++;
					}
				});
		mRecommendListAdapter = new RecommendListAdapter(getActivity(),
				productRecommendList);
		mRecommendListView.setAdapter(mRecommendListAdapter);

		mLunchTv = (TextView) view.findViewById(R.id.lunchTv);
		mSupperTv = (TextView) view.findViewById(R.id.supperTv);
		mLunchTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mLunchTv.setBackgroundResource(R.drawable.meal_cate_click);
				mLunchTv.setTextColor(getResources().getColor(R.color.white));
				mSupperTv.setBackgroundResource(R.drawable.meal_cate_normal);
				mSupperTv.setTextColor(getResources().getColor(R.color.black));
				mealCateId = 10;
				getRecDetail(recId, mealCateId);
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
				getRecDetail(recId, mealCateId);
			}
		});
		mAddTv = (TextView) view.findViewById(R.id.addTv);
		mAddTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i(TAG, "客户(用户)ID： "
						+ BaseApplication.getInstance().getCstID());
				if (BaseApplication.getInstance().getCstID() != 0) {
					Intent intent = new Intent();
					intent.putExtra(RecommendAddActivity.RECNAME, recName);
					intent.putExtra(RecommendAddActivity.RECID, recId);
					intent.setClass(getActivity(), RecommendAddActivity.class);
					startActivity(intent);
				} else {
					Intent intent = new Intent();
					intent.setClass(getActivity(), LoginActivity.class);
					startActivity(intent);
				}
			}
		});

		listRecName();
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.e(TAG, "onResume");
		getRecDetail(recId, mealCateId);
	}

	private void listRecName() {
		JSONObject jsonObject = new JSONObject();
		String url = WDStringRequest.getUrl(Consts.SERVER_listRecName,
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
								recommendNameList.clear();
								recommendNameList.addAll(RecommendName
										.parseJson(body));
								mRecommendNameAdapter.notifyDataSetChanged();
								recName = recommendNameList.get(0).getRecName();
								recId = recommendNameList.get(0).getRecId();
								getRecDetail(recId, mealCateId);
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

	private void getRecDetail(int recId, int mcId) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("recId", recId);// 推荐食谱ID
			jsonObject.put("mcId", mcId);// 餐别ID
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String url = WDStringRequest.getUrl(Consts.SERVER_getRecDetail,
				jsonObject);
		String relative_url = WDStringRequest.getRelativeUrl();
		String sign_body = WDStringRequest.getSignBody();

		WDStringRequest mRequest = new WDStringRequest(Request.Method.GET, url,
				relative_url, sign_body, false,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						mRecommendListView.onRefreshComplete();
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
								productRecommendList.clear();
								productRecommendList.addAll(ProductRecommend
										.parseJson(body));
								mRecommendListAdapter.notifyDataSetChanged();
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
