package com.homework.teacher.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.homework.teacher.Consts;
import com.homework.teacher.R;
import com.homework.teacher.adapter.ProductChooseListAdapter;
import com.homework.teacher.adapter.ProductTypeChooseListAdapter;
import com.homework.teacher.app.BaseApplication;
import com.homework.teacher.data.ProductChoose;
import com.homework.teacher.data.ProductTypeChoose;
import com.homework.teacher.http.WDStringRequest;
import com.homework.teacher.utils.StatusUtils;

/**
 * 已选菜品
 * 
 * @author zhangkc
 * 
 */
public class AlreadyChooseFragment extends Fragment {
	private final static String TAG = "AlreadyChooseFragment";
	private View view;
	protected BaseApplication mApp;
	private SharedPreferences sp;
	protected ImageLoader imageLoader;

	private ListView mProductTypeChooseListView;
	private List<ProductTypeChoose> productTypeChooseList;
	private ProductTypeChooseListAdapter mProductTypeChooseAdapter;

	private PullToRefreshListView mProductChooseListView;
	private List<ProductChoose> productChooseList;
	private ProductChooseListAdapter mProductChooseListAdapter;
	private long curPageIndex = 1;
	private final int pageSize = 20;
	private int prdTypeId;
	@SuppressLint("HandlerLeak")
	private Handler myHandler = new Handler() {
		// 接收到消息后处理
		public void handleMessage(Message msg) {
			qrySelectedPrd(prdTypeId);
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.alreadychoose_fragment, container,
				false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.e(TAG, "onActivityCreated");
		mApp = BaseApplication.getInstance();
		sp = mApp.getSp();
		imageLoader = mApp.imageLoader;

		mProductTypeChooseListView = (ListView) view
				.findViewById(R.id.mealKindChooseListView);
		if (productTypeChooseList == null) {
			productTypeChooseList = new ArrayList<ProductTypeChoose>();
		}
		mProductTypeChooseAdapter = new ProductTypeChooseListAdapter(
				getActivity(), 0, productTypeChooseList);
		mProductTypeChooseListView.setAdapter(mProductTypeChooseAdapter);
		mProductTypeChooseListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						mProductTypeChooseAdapter = new ProductTypeChooseListAdapter(
								getActivity(), position, productTypeChooseList);
						mProductTypeChooseAdapter.notifyDataSetChanged();
						mProductTypeChooseListView
								.setAdapter(mProductTypeChooseAdapter);
						mProductTypeChooseListView.setSelection(position);
						prdTypeId = productTypeChooseList.get(position).getId();
						qrySelectedPrd(prdTypeId);
					}
				});

		mProductChooseListView = (PullToRefreshListView) view
				.findViewById(R.id.mealChooseListView);
		if (productChooseList == null) {
			productChooseList = new ArrayList<ProductChoose>();
		}
		mProductChooseListView
				.setOnRefreshListener(new OnRefreshListener2<ListView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						productChooseList.clear();
						qrySelectedPrd(prdTypeId);
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						curPageIndex++;
					}
				});
		mProductChooseListAdapter = new ProductChooseListAdapter(getActivity(),
				myHandler, productChooseList, sp);
		mProductChooseListView.setAdapter(mProductChooseListAdapter);
		countPrdType();
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.e(TAG, "onResume");
		qrySelectedPrd(prdTypeId);
	}

	private void countPrdType() {
		JSONObject jsonObject = new JSONObject();
		try {
			int cstId = BaseApplication.getInstance().getCstID();
			jsonObject.put("cstId", cstId);// 客户ID
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String url = WDStringRequest.getUrl(Consts.SERVER_countPrdType,
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
								productTypeChooseList.clear();
								productTypeChooseList.addAll(ProductTypeChoose
										.parseJson(body));
								mProductTypeChooseAdapter
										.notifyDataSetChanged();
								prdTypeId = productTypeChooseList.get(0)
										.getId();
								qrySelectedPrd(prdTypeId);
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

	private void qrySelectedPrd(int prdTypeId) {
		JSONObject jsonObject = new JSONObject();
		try {
			int cstId = BaseApplication.getInstance().getCstID();
			jsonObject.put("cstId", cstId);// 客户ID
			jsonObject.put("cityId", 4);// 城市ID 目前固定为南京
			jsonObject.put("prdTypeId", prdTypeId);// 商品品类ID
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String url = WDStringRequest.getUrl(Consts.SERVER_qrySelectedPrd,
				jsonObject);
		String relative_url = WDStringRequest.getRelativeUrl();
		String sign_body = WDStringRequest.getSignBody();

		WDStringRequest mRequest = new WDStringRequest(Request.Method.GET, url,
				relative_url, sign_body, false,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						mProductChooseListView.onRefreshComplete();
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
								productChooseList.clear();
								productChooseList.addAll(ProductChoose
										.parseJson(body));
								mProductChooseListAdapter
										.notifyDataSetChanged();
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
