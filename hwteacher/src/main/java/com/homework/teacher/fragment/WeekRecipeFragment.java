package com.homework.teacher.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.homework.teacher.Consts;
import com.homework.teacher.R;
import com.homework.teacher.adapter.ProductWeekDayListAdapter;
import com.homework.teacher.app.BaseApplication;
import com.homework.teacher.data.ProductWeekDay;
import com.homework.teacher.http.WDStringRequest;
import com.homework.teacher.utils.StatusUtils;

/**
 * 一周食谱
 * 
 * @author zhangkc
 * 
 */
public class WeekRecipeFragment extends Fragment {
	private final static String TAG = "WeekRecipeFragment";
	private View view;
	protected BaseApplication mApp;
	private SharedPreferences sp;
	private TextView mealCate10Tv, mealCate12Tv;
	private ListView mealList;
	private List<ProductWeekDay> mProductWeekDayList;
	private ProductWeekDayListAdapter mProductWeekDayListAdapter;
	@SuppressLint("HandlerLeak")
	private Handler myHandler = new Handler() {
		// 接收到消息后处理
		public void handleMessage(Message msg) {
			if (msg.arg1 != 0) {
				for (int i = 0; i < mProductWeekDayList.size(); i++) {
					if (mProductWeekDayList.get(i).getId() == msg.arg1) {
						mProductWeekDayList.remove(mProductWeekDayList.get(i));
						mProductWeekDayListAdapter.notifyDataSetChanged();
					}
				}
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.weekrecipe_fragment, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mApp = BaseApplication.getInstance();
		sp = mApp.getSp();
		mealCate10Tv = (TextView) view.findViewById(R.id.mealCate10Tv);
		mealCate10Tv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mealCate10Tv.setTextColor(Color.WHITE);
				mealCate10Tv.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.meal_time_click));
				mealCate12Tv.setTextColor(Color.BLACK);
				mealCate12Tv.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.meal_time_normal));
				mProductWeekDayList.clear();
				mProductWeekDayListAdapter.notifyDataSetChanged();
				qryDayPrd(10);
			}
		});
		mealCate12Tv = (TextView) view.findViewById(R.id.mealCate12Tv);
		mealCate12Tv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mealCate10Tv.setTextColor(Color.BLACK);
				mealCate10Tv.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.meal_time_normal));
				mealCate12Tv.setTextColor(Color.WHITE);
				mealCate12Tv.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.meal_time_click));
				mProductWeekDayList.clear();
				mProductWeekDayListAdapter.notifyDataSetChanged();
				qryDayPrd(12);
			}
		});
		mealList = (ListView) view.findViewById(R.id.mealList);
		if (mProductWeekDayList == null) {
			mProductWeekDayList = new ArrayList<ProductWeekDay>();
		}
		mProductWeekDayListAdapter = new ProductWeekDayListAdapter(
				getActivity(), myHandler, mProductWeekDayList, sp);
		mealList.setAdapter(mProductWeekDayListAdapter);
		qryDayPrd(10);
	}

	private void qryDayPrd(int mealCateId) {
		JSONObject jsonObject = new JSONObject();
		try {
			int cstId = BaseApplication.getInstance().getCstID();
			jsonObject.put("cstId", cstId);// 客户ID
			jsonObject.put("cityId", 4);// 城市ID 目前固定为南京
			jsonObject.put("mealCateId", mealCateId);// 餐别ID
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String url = WDStringRequest
				.getUrl(Consts.SERVER_qryDayPrd, jsonObject);
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
								mProductWeekDayList.clear();
								mProductWeekDayList.addAll(ProductWeekDay
										.parseJson(body));
								mProductWeekDayListAdapter
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
