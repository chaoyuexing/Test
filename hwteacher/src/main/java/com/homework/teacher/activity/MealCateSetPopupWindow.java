package com.homework.teacher.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.homework.teacher.Consts;
import com.homework.teacher.R;
import com.homework.teacher.adapter.MealCateAdapter;
import com.homework.teacher.app.BaseApplication;
import com.homework.teacher.data.MealCate;
import com.homework.teacher.http.WDStringRequest;
import com.homework.teacher.utils.StatusUtils;

/**
 * 餐别设置页
 * 
 * @author zhangkc
 * 
 */
public class MealCateSetPopupWindow extends Activity {
	private final static String TAG = MealCateSetPopupWindow.class.getName();
	private final static String TOKEN = "token";
	public final static String DAYTYPE = "dayType";// 1：节假日，2：工作日
	protected BaseApplication mApp;
	private SharedPreferences sp;
	private LinearLayout layout;
	private TextView mDayTv;
	private ListView mMealCateListView;
	private List<MealCate> mMealCateList;
	private MealCateAdapter mMealCateAdapter;
	private long curPageIndex = 1;
	private final int pageSize = 20;
	private int dayType, mcId, aodFlag;
	private String mealCateName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mealcateset);

		if (getIntent() != null) {
			dayType = getIntent().getIntExtra(DAYTYPE, 0);
			Log.i(TAG, "dayType: " + dayType);
		}
		mApp = BaseApplication.getInstance();
		sp = mApp.getSp();
		layout = (LinearLayout) findViewById(R.id.pop_layout);
		// 添加弹出窗口范围监听可以优先获取触点，即不再执行onTouchEvent()函数，点击其他地方时执行onTouchEvent()函数销毁Activity
		layout.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

			}
		});
		mDayTv = (TextView) findViewById(R.id.dayTv);
		if (dayType == 1) {
			mDayTv.setText("节假日-餐别");
		} else if (dayType == 2) {
			mDayTv.setText("工作日-餐别");
		}
		mMealCateListView = (ListView) findViewById(R.id.mealCateListView);
		if (mMealCateList == null) {
			mMealCateList = new ArrayList<MealCate>();
		}
		// MealCate mealCate1 = new MealCate(1, 11, "午餐", 0);
		// mMealCateList.add(mealCate1);
		// MealCate mealCate2 = new MealCate(2, 12, "晚餐", 1);
		// mMealCateList.add(mealCate2);
		// MealCate mealCate3 = new MealCate(3, 13, "夜宵", 1);
		// mMealCateList.add(mealCate3);
		mMealCateAdapter = new MealCateAdapter(MealCateSetPopupWindow.this,
				mMealCateList);
		mMealCateListView.setAdapter(mMealCateAdapter);
		mMealCateListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						mcId = mMealCateList.get(position).getMealCateId();
						mealCateName = mMealCateList.get(position)
								.getMealCateName();
						if (mMealCateList.get(position).getChooseFlag() == 0) {// 选定标记
																				// 0：未选，1：已选
							mMealCateList.get(position).setChooseFlag(1);
							aodFlag = 1;// 添加或删除标记 1：添加，-1：删除
						} else if (mMealCateList.get(position).getChooseFlag() == 1) {
							mMealCateList.get(position).setChooseFlag(0);
							aodFlag = -1;
						}
						mMealCateAdapter = new MealCateAdapter(
								MealCateSetPopupWindow.this, mMealCateList);
						mMealCateAdapter.notifyDataSetChanged();
						mMealCateListView.setAdapter(mMealCateAdapter);
						tokenFetch();
					}
				});
		getDinnerPlan();
	}

	private void getDinnerPlan() {
		JSONObject jsonObject = new JSONObject();
		try {
			int cstId = BaseApplication.getInstance().getCstID();
			jsonObject.put("cstId", cstId);// 客户ID
			jsonObject.put("dayType", dayType);// 日期类型 1：节假日，2：工作日
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String url = WDStringRequest.getUrl(Consts.SERVER_dinnerPlan,
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
								mMealCateList.clear();
								mMealCateList.addAll(MealCate.parseJson(body));
								mMealCateAdapter.notifyDataSetChanged();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						StatusUtils.handleError(arg0,
								MealCateSetPopupWindow.this);
					}
				});
		BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
	}

	private void tokenFetch() {
		String url = WDStringRequest.getUrl(Consts.SERVER_tokenFetch, null);
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
								sp.edit()
										.putString(TOKEN,
												body.optString("token"))
										.commit();
								Log.i(TAG, "接口访问令牌存入本地~~~");
								setDinnerPlan();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						StatusUtils.handleError(arg0,
								MealCateSetPopupWindow.this);
					}
				});
		BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
	}

	private void setDinnerPlan() {
		JSONObject jsonObject = new JSONObject();
		try {
			int cstId = BaseApplication.getInstance().getCstID();
			jsonObject.put("cstId", cstId);// 客户ID
			jsonObject.put("dayType", dayType);// 日期类型 1：节假日，2：工作日
			jsonObject.put("mcId", mcId);// 餐别ID
			jsonObject.put("aodFlag", aodFlag);// 添加或删除标记 1：添加，-1：删除
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String url = WDStringRequest.getUrl(Consts.SERVER_setDinnerPlan,
				jsonObject);
		String relative_url = WDStringRequest.getRelativeUrl();
		String sign_body = WDStringRequest.getSignBody();

		WDStringRequest mRequest = new WDStringRequest(Request.Method.GET, url,
				relative_url, sign_body, true, new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						try {
							JSONObject jsonObject = new JSONObject(response);
							int code = 0;// 响应头中的code
							if (jsonObject != null) {
								code = jsonObject.getInt("code");
								if (code == 0) {
									if (aodFlag == 1) {
										Toast.makeText(
												MealCateSetPopupWindow.this,
												"添加" + mealCateName + "成功",
												Toast.LENGTH_SHORT).show();
									} else {
										Toast.makeText(
												MealCateSetPopupWindow.this,
												"删除" + mealCateName + "成功",
												Toast.LENGTH_SHORT).show();
									}
								} else {
									Toast.makeText(MealCateSetPopupWindow.this,
											"操作失败", Toast.LENGTH_SHORT).show();
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
								MealCateSetPopupWindow.this);
					}
				});
		BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
	}

	// 实现onTouchEvent触屏函数但点击屏幕时销毁本Activity
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return true;
	}

}
