package com.homework.teacher.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.homework.teacher.Consts;
import com.homework.teacher.R;
import com.homework.teacher.adapter.MealTimeAdapter1;
import com.homework.teacher.app.BaseApplication;
import com.homework.teacher.data.MealTime;
import com.homework.teacher.http.WDStringRequest;
import com.homework.teacher.utils.StatusUtils;

/**
 * 取餐时间选择页
 * 
 * @author zhangkc
 * 
 */
public class SelectTakeTimeActivity extends Activity {
	private final static String TAG = SelectTakeTimeActivity.class.getName();
	public final static String MEALCATEID = "mealCateId";
	private final static String TAKETIMEID = "takeTimeId";
	private final static String TAKETIME = "value";
	private ListView mTakeTimeListView;
	private List<MealTime> mealTimeList;
	private MealTimeAdapter1 mMealTimeAdapter;
	private int mealCateId;
	private int flag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selecttaketime);

		if (getIntent() != null) {
			mealCateId = getIntent().getIntExtra(MEALCATEID, 0);
			flag = getIntent().getIntExtra("flag", 0);
			Log.i(TAG, "mealCateId: " + mealCateId + " flag: " + flag);
		}
		((Button) findViewById(R.id.back))
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
		((TextView) findViewById(R.id.title)).setText(R.string.select_taketime);
		((TextView) findViewById(R.id.tvSet)).setVisibility(View.GONE);
		mTakeTimeListView = (ListView) findViewById(R.id.takeTimeList);
		if (mealTimeList == null) {
			mealTimeList = new ArrayList<MealTime>();
		}
		mMealTimeAdapter = new MealTimeAdapter1(SelectTakeTimeActivity.this,
				mealTimeList);
		mTakeTimeListView.setAdapter(mMealTimeAdapter);
		mTakeTimeListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				int takeTimeId = mealTimeList.get(position).getId();
				String value = mealTimeList.get(position).getValue();
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putInt(TAKETIMEID, takeTimeId);
				bundle.putString(TAKETIME, value);
				bundle.putInt("flag", flag);
				intent.putExtras(bundle);
				setResult(2, intent);
				finish();
			}
		});
		getTakeTime();
	}

	private void getTakeTime() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("mealCateId", mealCateId);// 餐别ID
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String url = WDStringRequest.getUrl(Consts.SERVER_getTakeTime,
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
									body = new JSONArray(
											jsonObject.getString("body"));
								}
							}
							if (body != null) {
								mealTimeList.clear();
								mealTimeList.addAll(MealTime.parseJson(body));
								mMealTimeAdapter.notifyDataSetChanged();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						StatusUtils.handleError(arg0,
								SelectTakeTimeActivity.this);
					}
				});
		BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
	}

}