package com.homework.teacher.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
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
import com.homework.teacher.adapter.MealPlaceAdapter;
import com.homework.teacher.app.BaseApplication;
import com.homework.teacher.data.MealPlace;
import com.homework.teacher.http.WDStringRequest;
import com.homework.teacher.utils.StatusUtils;

/**
 * 取餐点选择页
 * 
 * @author zhangkc
 * 
 */
public class SelectMealPlaceActivity extends Activity {
	private final static String TAG = SelectMealPlaceActivity.class.getName();
	private final static String TAKEPOINTID = "takePointId";
	private final static String TAKEPOINTNAME = "takePointName";

	private ListView mMealPlaceListView;
	private List<MealPlace> mealPlaceList;
	private MealPlaceAdapter mMealPlaceAdapter;
	private int flag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selectmealplace);

		if (getIntent() != null) {
			flag = getIntent().getIntExtra("flag", 0);
			Log.i(TAG, "flag: " + flag);
		}
		((Button) findViewById(R.id.back))
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
		((TextView) findViewById(R.id.title))
				.setText(R.string.select_mealplace);
		((TextView) findViewById(R.id.tvSet)).setVisibility(View.GONE);
		mMealPlaceListView = (ListView) findViewById(R.id.mealPlaceList);
		if (mealPlaceList == null) {
			mealPlaceList = new ArrayList<MealPlace>();
		}
		mMealPlaceAdapter = new MealPlaceAdapter(SelectMealPlaceActivity.this,
				mealPlaceList);
		mMealPlaceListView.setAdapter(mMealPlaceAdapter);
		mMealPlaceListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				int takePointId = mealPlaceList.get(position).getId();
				String takePointName = mealPlaceList.get(position)
						.getAreaName3()
						+ "-"
						+ mealPlaceList.get(position).getAreaName4()
						+ "-"
						+ mealPlaceList.get(position).getName();
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putInt(TAKEPOINTID, takePointId);
				bundle.putString(TAKEPOINTNAME, takePointName);
				bundle.putInt("flag", flag);
				intent.putExtras(bundle);
				setResult(1, intent);
				finish();
			}
		});
		getTakePoint();
	}

	private void getTakePoint() {
		String url = WDStringRequest.getUrl(Consts.SERVER_getTakePoint, null);
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
								List<MealPlace> list = MealPlace
										.parseJson(body);
								mealPlaceList.clear();
								mealPlaceList.addAll(list);
								mMealPlaceAdapter.notifyDataSetChanged();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						StatusUtils.handleError(arg0,
								SelectMealPlaceActivity.this);
					}
				});
		BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
	}

}