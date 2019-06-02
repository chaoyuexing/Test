package com.homework.teacher.activity;

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
import com.nostra13.universalimageloader.core.ImageLoader;
import com.homework.teacher.Consts;
import com.homework.teacher.R;
import com.homework.teacher.app.BaseApplication;
import com.homework.teacher.data.Categry;
import com.homework.teacher.http.WDStringRequest;
import com.homework.teacher.utils.StatusUtils;
import com.homework.teacher.widget.CircularImage;

/**
 * 添加到一周食谱页
 * 
 * @author zhangkc
 * 
 */
public class AddToCookBookActivity extends Activity {
	private final static String TAG = AddToCookBookActivity.class.getName();
	private final static String TOKEN = "token";
	public final static String PROID = "prdId";// 产品ID
	public final static String PRONAME = "prdName";// 产品名称
	public final static String URL = "thumbnailURL";// 缩略图
	protected BaseApplication mApp;
	private SharedPreferences sp;
	protected ImageLoader imageLoader;
	private CircularImage productIv;
	private TextView productNameTv, mealCate110Tv, mealCate112Tv,
			mealCate210Tv, mealCate212Tv, mealCate310Tv, mealCate312Tv,
			mealCate410Tv, mealCate412Tv, mealCate510Tv, mealCate512Tv,
			mealCate610Tv, mealCate612Tv, mealCate710Tv, mealCate712Tv;
	private int prdId, weekDay, mealCateId, aodFlag;
	private String prdName, thumbnailURL;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addtocookbook);

		if (getIntent() != null) {
			prdId = getIntent().getIntExtra(PROID, 0);
			prdName = getIntent().getStringExtra(PRONAME);
			thumbnailURL = getIntent().getStringExtra(URL);
			Log.i(TAG, "prdId: " + prdId + " prdName: " + prdName
					+ " thumbnailURL: " + thumbnailURL);
		}
		mApp = BaseApplication.getInstance();
		sp = mApp.getSp();
		imageLoader = mApp.imageLoader;
		((Button) findViewById(R.id.back))
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
		((TextView) findViewById(R.id.title)).setText(R.string.add_to_cookbook);
		((TextView) findViewById(R.id.tvSet)).setVisibility(View.GONE);
		productIv = (CircularImage) findViewById(R.id.productIv);
		imageLoader.displayImage(thumbnailURL, productIv);
		productNameTv = (TextView) findViewById(R.id.productNameTv);
		productNameTv.setText(prdName);
		mealCate110Tv = (TextView) findViewById(R.id.mealCate110Tv);
		mealCate110Tv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				weekDay = 1;
				mealCateId = 10;
				if (mealCate110Tv.getCurrentTextColor() == getResources()
						.getColor(R.color.white)) {// 已添加
					mealCate110Tv.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.meal_time_normal));
					mealCate110Tv.setTextColor(getResources().getColor(
							R.color.black));
					aodFlag = -1;
				} else {
					mealCate110Tv.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.meal_time_click));
					mealCate110Tv.setTextColor(getResources().getColor(
							R.color.white));
					aodFlag = 1;
				}
				tokenFetch("iodRecipe");
			}
		});
		mealCate112Tv = (TextView) findViewById(R.id.mealCate112Tv);
		mealCate112Tv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				weekDay = 1;
				mealCateId = 12;
				if (mealCate112Tv.getCurrentTextColor() == getResources()
						.getColor(R.color.white)) {// 已添加
					mealCate112Tv.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.meal_time_normal));
					mealCate112Tv.setTextColor(getResources().getColor(
							R.color.black));
					aodFlag = -1;
				} else {
					mealCate112Tv.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.meal_time_click));
					mealCate112Tv.setTextColor(getResources().getColor(
							R.color.white));
					aodFlag = 1;
				}
				tokenFetch("iodRecipe");
			}
		});
		mealCate210Tv = (TextView) findViewById(R.id.mealCate210Tv);
		mealCate210Tv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				weekDay = 2;
				mealCateId = 10;
				if (mealCate210Tv.getCurrentTextColor() == getResources()
						.getColor(R.color.white)) {// 已添加
					mealCate210Tv.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.meal_time_normal));
					mealCate210Tv.setTextColor(getResources().getColor(
							R.color.black));
					aodFlag = -1;
				} else {
					mealCate210Tv.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.meal_time_click));
					mealCate210Tv.setTextColor(getResources().getColor(
							R.color.white));
					aodFlag = 1;
				}
				tokenFetch("iodRecipe");
			}
		});
		mealCate212Tv = (TextView) findViewById(R.id.mealCate212Tv);
		mealCate212Tv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				weekDay = 2;
				mealCateId = 12;
				if (mealCate212Tv.getCurrentTextColor() == getResources()
						.getColor(R.color.white)) {// 已添加
					mealCate212Tv.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.meal_time_normal));
					mealCate212Tv.setTextColor(getResources().getColor(
							R.color.black));
					aodFlag = -1;
				} else {
					mealCate212Tv.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.meal_time_click));
					mealCate212Tv.setTextColor(getResources().getColor(
							R.color.white));
					aodFlag = 1;
				}
				tokenFetch("iodRecipe");
			}
		});
		mealCate310Tv = (TextView) findViewById(R.id.mealCate310Tv);
		mealCate310Tv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				weekDay = 3;
				mealCateId = 10;
				if (mealCate310Tv.getCurrentTextColor() == getResources()
						.getColor(R.color.white)) {// 已添加
					mealCate310Tv.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.meal_time_normal));
					mealCate310Tv.setTextColor(getResources().getColor(
							R.color.black));
					aodFlag = -1;
				} else {
					mealCate310Tv.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.meal_time_click));
					mealCate310Tv.setTextColor(getResources().getColor(
							R.color.white));
					aodFlag = 1;
				}
				tokenFetch("iodRecipe");
			}
		});
		mealCate312Tv = (TextView) findViewById(R.id.mealCate312Tv);
		mealCate312Tv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				weekDay = 3;
				mealCateId = 12;
				if (mealCate312Tv.getCurrentTextColor() == getResources()
						.getColor(R.color.white)) {// 已添加
					mealCate312Tv.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.meal_time_normal));
					mealCate312Tv.setTextColor(getResources().getColor(
							R.color.black));
					aodFlag = -1;
				} else {
					mealCate312Tv.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.meal_time_click));
					mealCate312Tv.setTextColor(getResources().getColor(
							R.color.white));
					aodFlag = 1;
				}
				tokenFetch("iodRecipe");
			}
		});
		mealCate410Tv = (TextView) findViewById(R.id.mealCate410Tv);
		mealCate410Tv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				weekDay = 4;
				mealCateId = 10;
				if (mealCate410Tv.getCurrentTextColor() == getResources()
						.getColor(R.color.white)) {// 已添加
					mealCate410Tv.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.meal_time_normal));
					mealCate410Tv.setTextColor(getResources().getColor(
							R.color.black));
					aodFlag = -1;
				} else {
					mealCate410Tv.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.meal_time_click));
					mealCate410Tv.setTextColor(getResources().getColor(
							R.color.white));
					aodFlag = 1;
				}
				tokenFetch("iodRecipe");
			}
		});
		mealCate412Tv = (TextView) findViewById(R.id.mealCate412Tv);
		mealCate412Tv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				weekDay = 4;
				mealCateId = 12;
				if (mealCate412Tv.getCurrentTextColor() == getResources()
						.getColor(R.color.white)) {// 已添加
					mealCate412Tv.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.meal_time_normal));
					mealCate412Tv.setTextColor(getResources().getColor(
							R.color.black));
					aodFlag = -1;
				} else {
					mealCate412Tv.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.meal_time_click));
					mealCate412Tv.setTextColor(getResources().getColor(
							R.color.white));
					aodFlag = 1;
				}
				tokenFetch("iodRecipe");
			}
		});
		mealCate510Tv = (TextView) findViewById(R.id.mealCate510Tv);
		mealCate510Tv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				weekDay = 5;
				mealCateId = 10;
				if (mealCate510Tv.getCurrentTextColor() == getResources()
						.getColor(R.color.white)) {// 已添加
					mealCate510Tv.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.meal_time_normal));
					mealCate510Tv.setTextColor(getResources().getColor(
							R.color.black));
					aodFlag = -1;
				} else {
					mealCate510Tv.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.meal_time_click));
					mealCate510Tv.setTextColor(getResources().getColor(
							R.color.white));
					aodFlag = 1;
				}
				tokenFetch("iodRecipe");
			}
		});
		mealCate512Tv = (TextView) findViewById(R.id.mealCate512Tv);
		mealCate512Tv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				weekDay = 5;
				mealCateId = 12;
				if (mealCate512Tv.getCurrentTextColor() == getResources()
						.getColor(R.color.white)) {// 已添加
					mealCate512Tv.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.meal_time_normal));
					mealCate512Tv.setTextColor(getResources().getColor(
							R.color.black));
					aodFlag = -1;
				} else {
					mealCate512Tv.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.meal_time_click));
					mealCate512Tv.setTextColor(getResources().getColor(
							R.color.white));
					aodFlag = 1;
				}
				tokenFetch("iodRecipe");
			}
		});
		mealCate610Tv = (TextView) findViewById(R.id.mealCate610Tv);
		mealCate610Tv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				weekDay = 6;
				mealCateId = 10;
				if (mealCate610Tv.getCurrentTextColor() == getResources()
						.getColor(R.color.white)) {// 已添加
					mealCate610Tv.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.meal_time_normal));
					mealCate610Tv.setTextColor(getResources().getColor(
							R.color.black));
					aodFlag = -1;
				} else {
					mealCate610Tv.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.meal_time_click));
					mealCate610Tv.setTextColor(getResources().getColor(
							R.color.white));
					aodFlag = 1;
				}
				tokenFetch("iodRecipe");
			}
		});
		mealCate612Tv = (TextView) findViewById(R.id.mealCate612Tv);
		mealCate612Tv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				weekDay = 6;
				mealCateId = 12;
				if (mealCate612Tv.getCurrentTextColor() == getResources()
						.getColor(R.color.white)) {// 已添加
					mealCate612Tv.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.meal_time_normal));
					mealCate612Tv.setTextColor(getResources().getColor(
							R.color.black));
					aodFlag = -1;
				} else {
					mealCate612Tv.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.meal_time_click));
					mealCate612Tv.setTextColor(getResources().getColor(
							R.color.white));
					aodFlag = 1;
				}
				tokenFetch("iodRecipe");
			}
		});
		mealCate710Tv = (TextView) findViewById(R.id.mealCate710Tv);
		mealCate710Tv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				weekDay = 7;
				mealCateId = 10;
				if (mealCate710Tv.getCurrentTextColor() == getResources()
						.getColor(R.color.white)) {// 已添加
					mealCate710Tv.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.meal_time_normal));
					mealCate710Tv.setTextColor(getResources().getColor(
							R.color.black));
					aodFlag = -1;
				} else {
					mealCate710Tv.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.meal_time_click));
					mealCate710Tv.setTextColor(getResources().getColor(
							R.color.white));
					aodFlag = 1;
				}
				tokenFetch("iodRecipe");
			}
		});
		mealCate712Tv = (TextView) findViewById(R.id.mealCate712Tv);
		mealCate712Tv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				weekDay = 7;
				mealCateId = 12;
				if (mealCate712Tv.getCurrentTextColor() == getResources()
						.getColor(R.color.white)) {// 已添加
					mealCate712Tv.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.meal_time_normal));
					mealCate712Tv.setTextColor(getResources().getColor(
							R.color.black));
					aodFlag = -1;
				} else {
					mealCate712Tv.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.meal_time_click));
					mealCate712Tv.setTextColor(getResources().getColor(
							R.color.white));
					aodFlag = 1;
				}
				tokenFetch("iodRecipe");
			}
		});
		queryCategry();
	}

	/**
	 * 一周食谱-查询某商品已选餐别接口
	 */
	private void queryCategry() {
		JSONObject jsonObject = new JSONObject();
		try {
			int cstId = BaseApplication.getInstance().getCstID();
			jsonObject.put("cstId", cstId);// 客户ID
			jsonObject.put("prdId", prdId);// 商品ID
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String url = WDStringRequest.getUrl(Consts.SERVER_qryPrdMealCate,
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
								List<Categry> categries = Categry
										.parseJson(body);
								for (int i = 0; i < categries.size(); i++) {
									int weekDay = categries.get(i).getWeekDay();
									int mealCateId = categries.get(i)
											.getMealCateId();
									String s = weekDay + "" + mealCateId;
									if ("110".equals(s)) {
										mealCate110Tv
												.setBackgroundDrawable(getResources()
														.getDrawable(
																R.drawable.meal_time_click));
										mealCate110Tv
												.setTextColor(getResources()
														.getColor(R.color.white));
									} else if ("112".equals(s)) {
										mealCate112Tv
												.setBackgroundDrawable(getResources()
														.getDrawable(
																R.drawable.meal_time_click));
										mealCate112Tv
												.setTextColor(getResources()
														.getColor(R.color.white));
									} else if ("210".equals(s)) {
										mealCate210Tv
												.setBackgroundDrawable(getResources()
														.getDrawable(
																R.drawable.meal_time_click));
										mealCate210Tv
												.setTextColor(getResources()
														.getColor(R.color.white));
									} else if ("212".equals(s)) {
										mealCate212Tv
												.setBackgroundDrawable(getResources()
														.getDrawable(
																R.drawable.meal_time_click));
										mealCate212Tv
												.setTextColor(getResources()
														.getColor(R.color.white));
									} else if ("310".equals(s)) {
										mealCate310Tv
												.setBackgroundDrawable(getResources()
														.getDrawable(
																R.drawable.meal_time_click));
										mealCate310Tv
												.setTextColor(getResources()
														.getColor(R.color.white));
									} else if ("312".equals(s)) {
										mealCate312Tv
												.setBackgroundDrawable(getResources()
														.getDrawable(
																R.drawable.meal_time_click));
										mealCate312Tv
												.setTextColor(getResources()
														.getColor(R.color.white));
									} else if ("410".equals(s)) {
										mealCate410Tv
												.setBackgroundDrawable(getResources()
														.getDrawable(
																R.drawable.meal_time_click));
										mealCate410Tv
												.setTextColor(getResources()
														.getColor(R.color.white));
									} else if ("412".equals(s)) {
										mealCate412Tv
												.setBackgroundDrawable(getResources()
														.getDrawable(
																R.drawable.meal_time_click));
										mealCate412Tv
												.setTextColor(getResources()
														.getColor(R.color.white));
									} else if ("510".equals(s)) {
										mealCate510Tv
												.setBackgroundDrawable(getResources()
														.getDrawable(
																R.drawable.meal_time_click));
										mealCate510Tv
												.setTextColor(getResources()
														.getColor(R.color.white));
									} else if ("512".equals(s)) {
										mealCate512Tv
												.setBackgroundDrawable(getResources()
														.getDrawable(
																R.drawable.meal_time_click));
										mealCate512Tv
												.setTextColor(getResources()
														.getColor(R.color.white));
									} else if ("610".equals(s)) {
										mealCate610Tv
												.setBackgroundDrawable(getResources()
														.getDrawable(
																R.drawable.meal_time_click));
										mealCate610Tv
												.setTextColor(getResources()
														.getColor(R.color.white));
									} else if ("612".equals(s)) {
										mealCate612Tv
												.setBackgroundDrawable(getResources()
														.getDrawable(
																R.drawable.meal_time_click));
										mealCate612Tv
												.setTextColor(getResources()
														.getColor(R.color.white));
									} else if ("710".equals(s)) {
										mealCate710Tv
												.setBackgroundDrawable(getResources()
														.getDrawable(
																R.drawable.meal_time_click));
										mealCate710Tv
												.setTextColor(getResources()
														.getColor(R.color.white));
									} else if ("712".equals(s)) {
										mealCate712Tv
												.setBackgroundDrawable(getResources()
														.getDrawable(
																R.drawable.meal_time_click));
										mealCate712Tv
												.setTextColor(getResources()
														.getColor(R.color.white));
									}
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
								AddToCookBookActivity.this);
					}
				});
		BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
	}

	private void tokenFetch(final String method) {
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
								if (method.equals("iodRecipe")) {
									iodRecipe();
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
								AddToCookBookActivity.this);
					}
				});
		BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
	}

	private void iodRecipe() {
		JSONObject jsonObject = new JSONObject();
		try {
			int cstId = BaseApplication.getInstance().getCstID();
			jsonObject.put("cstId", cstId);// 客户ID
			jsonObject.put("prdId", prdId);// 商品ID
			jsonObject.put("weekDay", weekDay);// 周几
			jsonObject.put("mealCateId", mealCateId);// 餐别ID
			jsonObject.put("aodFlag", aodFlag);// 添加或删除 1：添加，-1：删除
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String url = WDStringRequest.getUrl(Consts.SERVER_aodPrdMealCate,
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
												AddToCookBookActivity.this,
												"添加该餐别成功", Toast.LENGTH_SHORT)
												.show();
									} else {
										Toast.makeText(
												AddToCookBookActivity.this,
												"删除该餐别成功", Toast.LENGTH_SHORT)
												.show();
									}
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
								AddToCookBookActivity.this);
					}
				});
		BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
	}

}