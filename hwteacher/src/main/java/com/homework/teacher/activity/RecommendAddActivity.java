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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.homework.teacher.Consts;
import com.homework.teacher.R;
import com.homework.teacher.app.BaseApplication;
import com.homework.teacher.http.WDStringRequest;
import com.homework.teacher.utils.StatusUtils;

/**
 * 推荐食谱-添加食谱
 * 
 * @author zhangkc
 * @date 2017-7-19
 */
public class RecommendAddActivity extends Activity {
	private final static String TAG = RecommendAddActivity.class.getName();
	public final static String RECNAME = "recName";
	public final static String RECID = "recId";
	private final static String TOKEN = "token";
	protected BaseApplication mApp;
	private SharedPreferences sp;
	private TextView mWeekDay1Tv, mWeekDay2Tv, mWeekDay3Tv, mWeekDay4Tv,
			mWeekDay5Tv, mWeekDay6Tv, mWeekDay7Tv, mLunchTv, mSupperTv,
			mConfirmAddTv;
	private String recName;// 推荐食谱名称
	private int recId;// 推荐食谱ID
	private List<Integer> weekDayArr;// 周几
	private List<Integer> mcIdArr;// 餐别ID

	private boolean isWeekDay1Choose = true;
	private boolean isWeekDay2Choose = true;
	private boolean isWeekDay3Choose = true;
	private boolean isWeekDay4Choose = true;
	private boolean isWeekDay5Choose = true;
	private boolean isWeekDay6Choose = true;
	private boolean isWeekDay7Choose = true;
	private boolean isLunchChoose = true;
	private boolean isSupperChoose = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recommend_add);

		if (getIntent() != null) {
			recName = getIntent().getStringExtra(RECNAME);
			recId = getIntent().getIntExtra(RECID, 0);
			Log.i(TAG, "recName: " + recName + " , recId: " + recId);
		}
		mApp = BaseApplication.getInstance();
		sp = mApp.getSp();
		((Button) findViewById(R.id.back))
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
		((TextView) findViewById(R.id.title)).setText(recName);
		((TextView) findViewById(R.id.tvSet)).setVisibility(View.GONE);

		mWeekDay1Tv = (TextView) findViewById(R.id.weekDay1Tv);
		mWeekDay1Tv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isWeekDay1Choose) {
					mWeekDay1Tv
							.setBackgroundResource(R.drawable.meal_cate_normal);
					mWeekDay1Tv.setTextColor(getResources().getColor(
							R.color.black));
					isWeekDay1Choose = false;
					Integer weekday = 1;
					weekDayArr.remove(weekday);
				} else {
					mWeekDay1Tv
							.setBackgroundResource(R.drawable.meal_cate_click);
					mWeekDay1Tv.setTextColor(getResources().getColor(
							R.color.white));
					isWeekDay1Choose = true;
					weekDayArr.add(1);
				}
			}
		});
		mWeekDay2Tv = (TextView) findViewById(R.id.weekDay2Tv);
		mWeekDay2Tv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isWeekDay2Choose) {
					mWeekDay2Tv
							.setBackgroundResource(R.drawable.meal_cate_normal);
					mWeekDay2Tv.setTextColor(getResources().getColor(
							R.color.black));
					isWeekDay2Choose = false;
					Integer weekday = 2;
					weekDayArr.remove(weekday);
				} else {
					mWeekDay2Tv
							.setBackgroundResource(R.drawable.meal_cate_click);
					mWeekDay2Tv.setTextColor(getResources().getColor(
							R.color.white));
					isWeekDay2Choose = true;
					weekDayArr.add(2);
				}
			}
		});
		mWeekDay3Tv = (TextView) findViewById(R.id.weekDay3Tv);
		mWeekDay3Tv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isWeekDay3Choose) {
					mWeekDay3Tv
							.setBackgroundResource(R.drawable.meal_cate_normal);
					mWeekDay3Tv.setTextColor(getResources().getColor(
							R.color.black));
					isWeekDay3Choose = false;
					Integer weekday = 3;
					weekDayArr.remove(weekday);
				} else {
					mWeekDay3Tv
							.setBackgroundResource(R.drawable.meal_cate_click);
					mWeekDay3Tv.setTextColor(getResources().getColor(
							R.color.white));
					isWeekDay3Choose = true;
					weekDayArr.add(3);
				}
			}
		});
		mWeekDay4Tv = (TextView) findViewById(R.id.weekDay4Tv);
		mWeekDay4Tv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isWeekDay4Choose) {
					mWeekDay4Tv
							.setBackgroundResource(R.drawable.meal_cate_normal);
					mWeekDay4Tv.setTextColor(getResources().getColor(
							R.color.black));
					isWeekDay4Choose = false;
					Integer weekday = 4;
					weekDayArr.remove(weekday);
				} else {
					mWeekDay4Tv
							.setBackgroundResource(R.drawable.meal_cate_click);
					mWeekDay4Tv.setTextColor(getResources().getColor(
							R.color.white));
					isWeekDay4Choose = true;
					weekDayArr.add(4);
				}
			}
		});
		mWeekDay5Tv = (TextView) findViewById(R.id.weekDay5Tv);
		mWeekDay5Tv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isWeekDay5Choose) {
					mWeekDay5Tv
							.setBackgroundResource(R.drawable.meal_cate_normal);
					mWeekDay5Tv.setTextColor(getResources().getColor(
							R.color.black));
					isWeekDay5Choose = false;
					Integer weekday = 5;
					weekDayArr.remove(weekday);
				} else {
					mWeekDay5Tv
							.setBackgroundResource(R.drawable.meal_cate_click);
					mWeekDay5Tv.setTextColor(getResources().getColor(
							R.color.white));
					isWeekDay5Choose = true;
					weekDayArr.add(5);
				}
			}
		});
		mWeekDay6Tv = (TextView) findViewById(R.id.weekDay6Tv);
		mWeekDay6Tv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isWeekDay6Choose) {
					mWeekDay6Tv
							.setBackgroundResource(R.drawable.meal_cate_normal);
					mWeekDay6Tv.setTextColor(getResources().getColor(
							R.color.black));
					isWeekDay6Choose = false;
					Integer weekday = 6;
					weekDayArr.remove(weekday);
				} else {
					mWeekDay6Tv
							.setBackgroundResource(R.drawable.meal_cate_click);
					mWeekDay6Tv.setTextColor(getResources().getColor(
							R.color.white));
					isWeekDay6Choose = true;
					weekDayArr.add(6);
				}
			}
		});
		mWeekDay7Tv = (TextView) findViewById(R.id.weekDay7Tv);
		mWeekDay7Tv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isWeekDay7Choose) {
					mWeekDay7Tv
							.setBackgroundResource(R.drawable.meal_cate_normal);
					mWeekDay7Tv.setTextColor(getResources().getColor(
							R.color.black));
					isWeekDay7Choose = false;
					Integer weekday = 7;
					weekDayArr.remove(weekday);
				} else {
					mWeekDay7Tv
							.setBackgroundResource(R.drawable.meal_cate_click);
					mWeekDay7Tv.setTextColor(getResources().getColor(
							R.color.white));
					isWeekDay7Choose = true;
					weekDayArr.add(7);
				}
			}
		});

		mLunchTv = (TextView) findViewById(R.id.lunchTv);
		mLunchTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isLunchChoose) {
					mLunchTv.setBackgroundResource(R.drawable.meal_cate_normal);
					mLunchTv.setTextColor(getResources()
							.getColor(R.color.black));
					isLunchChoose = false;
					Integer mcId = 10;
					mcIdArr.remove(mcId);
				} else {
					mLunchTv.setBackgroundResource(R.drawable.meal_cate_click);
					mLunchTv.setTextColor(getResources()
							.getColor(R.color.white));
					isLunchChoose = true;
					mcIdArr.add(10);
				}
			}
		});
		mSupperTv = (TextView) findViewById(R.id.supperTv);
		mSupperTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isSupperChoose) {
					mSupperTv
							.setBackgroundResource(R.drawable.meal_cate_normal);
					mSupperTv.setTextColor(getResources().getColor(
							R.color.black));
					isSupperChoose = false;
					Integer mcId = 12;
					mcIdArr.remove(mcId);
				} else {
					mSupperTv.setBackgroundResource(R.drawable.meal_cate_click);
					mSupperTv.setTextColor(getResources().getColor(
							R.color.white));
					isSupperChoose = true;
					mcIdArr.add(12);
				}
			}
		});

		if (weekDayArr == null) {
			weekDayArr = new ArrayList<Integer>();
			for (int i = 1; i < 8; i++) {
				weekDayArr.add(i);
			}
		}
		if (mcIdArr == null) {
			mcIdArr = new ArrayList<Integer>();
			mcIdArr.add(10);
			mcIdArr.add(12);
		}
		mConfirmAddTv = (TextView) findViewById(R.id.confirmAddTv);
		mConfirmAddTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (weekDayArr.size() == 0 || mcIdArr.size() == 0) {
					Toast.makeText(RecommendAddActivity.this, "请至少选择一天和一个餐别",
							Toast.LENGTH_SHORT).show();
				} else {
					tokenFetch();
				}
			}
		});
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
								recAddToMyRecipe();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						StatusUtils
								.handleError(arg0, RecommendAddActivity.this);
					}
				});
		BaseApplication.getInstance().addToRequestQueue(mRequest, "");
	}

	private void recAddToMyRecipe() {
		JSONObject jsonObject = new JSONObject();
		try {
			int cstId = BaseApplication.getInstance().getCstID();
			jsonObject.put("cstId", cstId);// 客户ID
			jsonObject.put("recId", recId);// 推荐食谱ID

			JSONArray weekDayArray = new JSONArray();
			for (int i = 0; i < weekDayArr.size(); i++) {
				weekDayArray.put(weekDayArr.get(i));
			}
			jsonObject.put("weekDayArr", weekDayArray);// 周几

			JSONArray mcIdArray = new JSONArray();
			for (int i = 0; i < mcIdArr.size(); i++) {
				mcIdArray.put(mcIdArr.get(i));
			}
			jsonObject.put("mcIdArr", mcIdArray);// 餐别ID
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String url = WDStringRequest.getUrl(Consts.SERVER_recAddToMyRecipe,
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
									Toast.makeText(RecommendAddActivity.this,
											"添加成功", Toast.LENGTH_SHORT).show();
									finish();
								} else {
									Toast.makeText(RecommendAddActivity.this,
											"添加失败", Toast.LENGTH_SHORT).show();
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						StatusUtils
								.handleError(arg0, RecommendAddActivity.this);
					}
				});
		BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
	}

}