package com.homework.teacher.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.homework.teacher.Consts;
import com.homework.teacher.R;
import com.homework.teacher.activity.OrderConfirmActivity;
import com.homework.teacher.activity.OrderHelperActivity;
import com.homework.teacher.activity.ProductDetailActivity;
import com.homework.teacher.activity.SelectDatePopupWindow;
import com.homework.teacher.adapter.ProductListAdapter;
import com.homework.teacher.adapter.ProductTypeListAdapter;
import com.homework.teacher.app.BaseApplication;
import com.homework.teacher.data.Product;
import com.homework.teacher.data.ProductType;
import com.homework.teacher.data.Remind;
import com.homework.teacher.http.WDStringRequest;
import com.homework.teacher.utils.DateUtils;
import com.homework.teacher.utils.StatusUtils;
import com.homework.teacher.widget.MarqueeTextView;
import com.homework.teacher.widget.ProgressHUD;

/**
 * 首页
 * 
 * @author zhangkc
 * 
 */
public class HomeFragment extends Fragment {
	private final static String TAG = "HomeFragment";
	private ProgressHUD mProgressHUD;
	private final static String TOKEN = "token";
	private final static String ORDID = "ordId";
	private View view;
	protected BaseApplication mApp;
	private SharedPreferences sp;
	protected ImageLoader imageLoader;
	private MarqueeTextView mTipsTv;
	private TextView mLocationTv, mTitleTv, mSetTv, mDateTv, mLunchTv,
			mSupperTv, mMealCarNumTv, mMealCarPriceTv;
	private RelativeLayout mMealCarRl;

	private ListView mProductTypeListView;
	private List<ProductType> productTypeList;
	private ProductTypeListAdapter mProductTypeAdapter;

	private PullToRefreshListView mProductListView;
	private List<Product> productList;
	private ProductListAdapter mProductListAdapter;
	private long curPageIndex = 1;
	private final int pageSize = 20;
	private int prdTypeId;
	private int mealCateId = 10;// 餐别ID：午餐 10 晚餐 12
	private String chooseOrderDate;// 下单日期，默认明天
	private boolean isAllowOrder = true;// 当前日期是否允许预定
	private int ordId;// 订单号
	private int totalNum;// 菜品总份数
	private double totalPrice;// 订单总价
	@SuppressLint("HandlerLeak")
	private Handler myHandler = new Handler() {
		// 接收到消息后处理
		public void handleMessage(Message msg) {
			Bundle bundle = msg.getData();
			if (bundle != null) {
				totalNum = bundle.getInt("totalNum", 0);
				totalPrice = bundle.getDouble("totalPrice", 0.0);
				Log.i(TAG, "totalNum: " + totalNum + " , totalPrice: "
						+ totalPrice);
				if (totalNum == 0) {
					mMealCarRl
							.setBackgroundResource(R.drawable.meal_car_default);
					mMealCarRl.setOnClickListener(null);
					mMealCarNumTv.setVisibility(View.GONE);
					mMealCarPriceTv.setVisibility(View.GONE);
				} else {
					mMealCarRl.setBackgroundResource(R.drawable.meal_car);
					mMealCarRl.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent();
							intent.setClass(getActivity(),
									OrderConfirmActivity.class);
							startActivity(intent);
						}
					});
					mMealCarNumTv.setVisibility(View.VISIBLE);
					mMealCarNumTv.setText(String.valueOf(totalNum));
					mMealCarPriceTv.setVisibility(View.VISIBLE);
					mMealCarPriceTv.setText("￥" + String.valueOf(totalPrice));
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
		view = inflater.inflate(R.layout.home_fragment, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mApp = BaseApplication.getInstance();
		sp = mApp.getSp();
		imageLoader = mApp.imageLoader;

		mLocationTv = (TextView) view.findViewById(R.id.locationTv);
		mLocationTv.setText("南京");
		mLocationTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getActivity(), "其他城市正在开通中", Toast.LENGTH_SHORT)
						.show();
			}
		});
		mTitleTv = (TextView) view.findViewById(R.id.title);
		mTitleTv.setVisibility(View.GONE);
		mSetTv = (TextView) view.findViewById(R.id.tvSet);
		mSetTv.setText(R.string.order_helper);
		mSetTv.setTextColor(Color.WHITE);
		mSetTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), OrderHelperActivity.class);
				startActivity(intent);
			}
		});
		mTipsTv = (MarqueeTextView) view.findViewById(R.id.tipsTv);
		mDateTv = (TextView) view.findViewById(R.id.dateTv);
		chooseOrderDate = DateUtils.getTomorrowDate();
		mDateTv.setText("明天 " + chooseOrderDate);
		mLunchTv = (TextView) view.findViewById(R.id.lunchTv);
		mSupperTv = (TextView) view.findViewById(R.id.supperTv);
		mMealCarNumTv = (TextView) view.findViewById(R.id.mealCarNumTv);
		mMealCarPriceTv = (TextView) view.findViewById(R.id.mealCarPriceTv);
		mDateTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent(getActivity(),
						SelectDatePopupWindow.class), 0);
			}
		});
		mLunchTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mLunchTv.setBackgroundResource(R.drawable.meal_cate_click);
				mLunchTv.setTextColor(getResources().getColor(R.color.white));
				mSupperTv.setBackgroundResource(R.drawable.meal_cate_normal);
				mSupperTv.setTextColor(getResources().getColor(R.color.black));
				mealCateId = 10;
				getCityProduct(prdTypeId, mealCateId);
				if (BaseApplication.getInstance().getCstID() != 0) {
					tokenFetch();
				}
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
				getCityProduct(prdTypeId, mealCateId);
				if (BaseApplication.getInstance().getCstID() != 0) {
					tokenFetch();
				}
			}
		});
		mMealCarRl = (RelativeLayout) view.findViewById(R.id.mealCarRl);

		mProductTypeListView = (ListView) view
				.findViewById(R.id.mealKindListView);
		if (productTypeList == null) {
			productTypeList = new ArrayList<ProductType>();
		}
		mProductTypeAdapter = new ProductTypeListAdapter(getActivity(), 0,
				imageLoader, productTypeList);
		mProductTypeListView.setAdapter(mProductTypeAdapter);
		mProductTypeListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						mProductTypeAdapter = new ProductTypeListAdapter(
								getActivity(), position, imageLoader,
								productTypeList);
						mProductTypeAdapter.notifyDataSetChanged();
						mProductTypeListView.setAdapter(mProductTypeAdapter);
						mProductTypeListView.setSelection(position);
						prdTypeId = productTypeList.get(position).getId();
						getCityProduct(prdTypeId, mealCateId);
					}
				});

		mProductListView = (PullToRefreshListView) view
				.findViewById(R.id.mealListView);
		if (productList == null) {
			productList = new ArrayList<Product>();
		}
		mProductListView
				.setOnRefreshListener(new OnRefreshListener2<ListView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						productList.clear();
						getCityProduct(prdTypeId, mealCateId);
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						curPageIndex++;
					}
				});
		mProductListAdapter = new ProductListAdapter(getActivity(), myHandler,
				imageLoader, productList, sp);
		mProductListView.setAdapter(mProductListAdapter);
		mProductListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Intent intent = new Intent();
						int cityPrdId = productList.get(position - 1).getId();
						intent.putExtra(ProductDetailActivity.CITYPRDID,
								cityPrdId);// 城市产品ID
						intent.setClass(getActivity(),
								ProductDetailActivity.class);
						startActivity(intent);
					}
				});
		queryByKey();
		queryProductTypeByCityId();
		Log.i(TAG, "客户(用户)ID： " + BaseApplication.getInstance().getCstID());
		if (BaseApplication.getInstance().getCstID() != 0) {
			tokenFetch();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (BaseApplication.getInstance().getCstID() != 0) {
			tokenFetch();
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {
			Bundle bundle = data.getExtras();
			switch (resultCode) {
			case Activity.RESULT_OK:
				int year = bundle.getInt("year");
				int month = bundle.getInt("month");
				int day = bundle.getInt("day");
				String month1,
				day1;
				if (month < 10) {
					month1 = "0" + month;
				} else {
					month1 = "" + month;
				}
				if (day < 10) {
					day1 = "0" + day;
				} else {
					day1 = "" + day;
				}
				chooseOrderDate = year + "-" + month1 + "-" + day1;
				Log.e(TAG, "chooseOrderDate: " + chooseOrderDate);
				mDateTv.setText(chooseOrderDate);
				// if (chooseOrderDate.equals(DateUtils.getDate())) {
				// Toast.makeText(getActivity(), "所有产品都库存不足，不允许预定",
				// Toast.LENGTH_SHORT).show();
				// isAllowOrder = false;
				// }
				break;
			default:
				break;
			}
		}
	}

	private void queryByKey() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("key", "REMIND_INFO1");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String url = WDStringRequest.getUrl(Consts.SERVER_queryByKey,
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
								List<Remind> reminds = Remind.parseJson(body);
								String remindInfo = "";
								for (int i = 0; i < reminds.size(); i++) {
									remindInfo = remindInfo
											+ reminds.get(i).getValue() + " | ";
								}
								Log.e(TAG, remindInfo);
								remindInfo = remindInfo.substring(0,
										remindInfo.length() - 3);
								Log.e(TAG, remindInfo);
								mTipsTv.setText(remindInfo);
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

	private void queryProductTypeByCityId() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("cityId", 4);// 4为南京的ID，目前只做南京
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String url = WDStringRequest.getUrl(
				Consts.SERVER_queryProductTypeByCityId, jsonObject);
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
								productTypeList.clear();
								productTypeList.addAll(ProductType
										.parseJson(body));
								mProductTypeAdapter.notifyDataSetChanged();
								prdTypeId = productTypeList.get(0).getId();
								getCityProduct(prdTypeId, mealCateId);
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

	private void getCityProduct(int prdTypeId, int mealCateId) {
		showMyDialog(true);
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("cityId", 4);// 4为南京的ID，开始只做南京
			jsonObject.put("prdTypeId", prdTypeId);
			jsonObject.put("mealCateId", mealCateId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String url = WDStringRequest.getUrl(Consts.SERVER_getCityProduct,
				jsonObject);
		String relative_url = WDStringRequest.getRelativeUrl();
		String sign_body = WDStringRequest.getSignBody();

		WDStringRequest mRequest = new WDStringRequest(Request.Method.GET, url,
				relative_url, sign_body, false,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						mProgressHUD.dismiss();
						mProductListView.onRefreshComplete();
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
								productList.clear();
								productList.addAll(Product.parseJson(body));
								mProductListAdapter.notifyDataSetChanged();
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
								createOrQueryOrder();
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
		BaseApplication.getInstance().addToRequestQueue(mRequest, "");
	}

	private void createOrQueryOrder() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("cstId", BaseApplication.getInstance().getCstID());// 客户ID
			jsonObject.put("cityId", 4);// 目前固定为南京—4
			jsonObject.put("dinnerDate", chooseOrderDate);// 用餐日期
			jsonObject.put("mealCateId", mealCateId);// 餐别ID
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String url = WDStringRequest.getUrl(Consts.SERVER_createOrQueryOrder,
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
							JSONObject body = null; // 响应体中的body
							if (jsonObject != null) {
								code = jsonObject.getInt("code");
								if (code == 0) {
									body = new JSONObject(jsonObject
											.getString("body"));
								}
							}
							if (body != null) {
								ordId = body.optInt("ordId");
								sp.edit().putInt(ORDID, ordId).commit();
								Log.i(TAG, "订单号: " + ordId + " 存入本地~~~");
								totalNum = body.optInt("totalNum");
								totalPrice = body.optDouble("totalPrice");
								Log.i(TAG, "totalNum: " + totalNum
										+ " , totalPrice: " + totalPrice);
								if (totalNum == 0) {
									mMealCarRl
											.setBackgroundResource(R.drawable.meal_car_default);
									mMealCarRl.setOnClickListener(null);
									mMealCarNumTv.setVisibility(View.GONE);
									mMealCarPriceTv.setVisibility(View.GONE);
								} else {
									mMealCarRl
											.setBackgroundResource(R.drawable.meal_car);
									mMealCarRl
											.setOnClickListener(new View.OnClickListener() {
												@Override
												public void onClick(View v) {
													Intent intent = new Intent();
													intent.setClass(
															getActivity(),
															OrderConfirmActivity.class);
													startActivity(intent);
												}
											});
									mMealCarNumTv.setVisibility(View.VISIBLE);
									mMealCarNumTv.setText(String
											.valueOf(totalNum));
									mMealCarPriceTv.setVisibility(View.VISIBLE);
									mMealCarPriceTv.setText("￥"
											+ String.valueOf(totalPrice));
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

	private void showMyDialog(boolean isCancelable) {
		mProgressHUD = ProgressHUD.show(getActivity(), "正在加载", true,
				isCancelable, null);
	}

}