package com.homework.teacher.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.homework.teacher.Consts;
import com.homework.teacher.R;
import com.homework.teacher.adapter.VoucherNotUsedAdapter;
import com.homework.teacher.app.BaseApplication;
import com.homework.teacher.data.VoucherRecord;
import com.homework.teacher.http.WDStringRequest;
import com.homework.teacher.utils.StatusUtils;

/**
 * 代金券未使用
 * 
 * @author zhangkc
 * @date 2017-8-1
 */
public class VoucherNotUsedFragment extends Fragment {
	private final static String TAG = "VoucherNotUsedFragment";
	private View view;

	private PullToRefreshListView mVoucherNotUsedListView;
	private List<VoucherRecord> voucherRecordList;
	private VoucherNotUsedAdapter mVoucherNotUsedAdapter;
	private long curPageIndex = 1;
	private final int pageSize = 20;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.voucher_notused_fragment, container,
				false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.e(TAG, "onActivityCreated");

		mVoucherNotUsedListView = (PullToRefreshListView) view
				.findViewById(R.id.voucherNotUsedListView);
		if (voucherRecordList == null) {
			voucherRecordList = new ArrayList<VoucherRecord>();
		}
		mVoucherNotUsedListView
				.setOnRefreshListener(new OnRefreshListener2<ListView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						curPageIndex = 1;
						voucherRecordList.clear();
						listVoucher();
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						curPageIndex++;
						listVoucher();
					}
				});
		mVoucherNotUsedAdapter = new VoucherNotUsedAdapter(getActivity(),
				voucherRecordList);
		mVoucherNotUsedListView.setAdapter(mVoucherNotUsedAdapter);
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.e(TAG, "onResume");
		curPageIndex = 1;
		voucherRecordList.clear();
		listVoucher();
	}

	private void listVoucher() {
		JSONObject jsonObject = new JSONObject();
		try {
			int cstId = BaseApplication.getInstance().getCstID();
			jsonObject.put("cstId", cstId);// 客户ID
			jsonObject.put("useFlag", 0);// 使用标记 0：未使用，1：已使用
			jsonObject.put("pageNo", curPageIndex);// 页码
			jsonObject.put("pageSize", pageSize);// 每页记录数
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String url = WDStringRequest.getUrl(Consts.SERVER_listVoucher,
				jsonObject);
		String relative_url = WDStringRequest.getRelativeUrl();
		String sign_body = WDStringRequest.getSignBody();

		WDStringRequest mRequest = new WDStringRequest(Request.Method.GET, url,
				relative_url, sign_body, false,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						mVoucherNotUsedListView.onRefreshComplete();
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
								voucherRecordList.addAll(VoucherRecord
										.parseJson(body));

								if (voucherRecordList != null) {
									if (voucherRecordList.size() > 0) {
										if (((voucherRecordList.size()) % pageSize) == 0) {
											mVoucherNotUsedListView
													.setMode(Mode.BOTH);
										} else {
											mVoucherNotUsedListView
													.setMode(Mode.PULL_FROM_START);
										}
									}
									mVoucherNotUsedAdapter
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
						StatusUtils.handleError(arg0, getActivity());
					}
				});
		BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
	}

}
