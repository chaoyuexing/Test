package com.homework.teacher.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.homework.teacher.Consts;
import com.homework.teacher.R;
import com.homework.teacher.adapter.ProductCommentListAdapter;
import com.homework.teacher.app.BaseApplication;
import com.homework.teacher.data.ProductDetailComment;
import com.homework.teacher.http.WDStringRequest;
import com.homework.teacher.utils.StatusUtils;

/**
 * 产品详情页
 * 
 * @author zhangkc
 * @date 2017-7-14
 */
public class ProductDetailActivity extends Activity {
	private final static String TAG = ProductDetailActivity.class.getName();
	public final static String CITYPRDID = "cityPrdId";
	protected BaseApplication mApp;
	protected ImageLoader imageLoader;
	private int cityPrdId;// 城市产品ID
	private ImageView mImgIv;
	private TextView mProductNameTv, mProductSellNumTv, mProductFavRateTv,
			mProductPriceTv, mProductIntroduceTv, mProductCommentTv,
			mIntroduceTv, mEvaLvAllTv, mEvaLv3Tv, mEvaLv2Tv, mEvaLv1Tv,
			mContentCommentTv;
	private LinearLayout mCommentLy;
	private PullToRefreshListView mProductCommentListView;
	private List<ProductDetailComment> productCommentList;
	private ProductCommentListAdapter mProductCommentListAdapter;
	private long curPageIndex = 1;
	private final int pageSize = 20;
	private int evaLv;// 评分 1：差评，2：中评，3：好评（不传为查看全部）
	private int descFlag;// 评价内容标识 1：只看有内容的评价，0：查看所有评价（不传等同于0）

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_productdetail);

		if (getIntent() != null) {
			cityPrdId = getIntent().getIntExtra(CITYPRDID, 0);
			Log.i(TAG, "cityPrdId: " + cityPrdId);
		}
		mApp = BaseApplication.getInstance();
		imageLoader = mApp.imageLoader;
		((Button) findViewById(R.id.back))
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
		mImgIv = (ImageView) findViewById(R.id.imgIv);
		mProductNameTv = (TextView) findViewById(R.id.productNameTv);
		mProductSellNumTv = (TextView) findViewById(R.id.productSellNumTv);
		mProductFavRateTv = (TextView) findViewById(R.id.productFavRateTv);
		mProductPriceTv = (TextView) findViewById(R.id.productPriceTv);

		mProductIntroduceTv = (TextView) findViewById(R.id.productIntroduceTv);
		mProductIntroduceTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mProductIntroduceTv.setBackgroundColor(getResources().getColor(
						R.color.theme_color));
				mProductIntroduceTv.setTextColor(getResources().getColor(
						R.color.white));
				mProductCommentTv.setBackgroundColor(getResources().getColor(
						R.color.white));
				mProductCommentTv.setTextColor(getResources().getColor(
						R.color.black));
				mIntroduceTv.setVisibility(View.VISIBLE);
				mCommentLy.setVisibility(View.GONE);
			}
		});
		mProductCommentTv = (TextView) findViewById(R.id.productCommentTv);
		mProductCommentTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mProductIntroduceTv.setBackgroundColor(getResources().getColor(
						R.color.white));
				mProductIntroduceTv.setTextColor(getResources().getColor(
						R.color.black));
				mProductCommentTv.setBackgroundColor(getResources().getColor(
						R.color.theme_color));
				mProductCommentTv.setTextColor(getResources().getColor(
						R.color.white));
				mIntroduceTv.setVisibility(View.GONE);
				mCommentLy.setVisibility(View.VISIBLE);
			}
		});
		mIntroduceTv = (TextView) findViewById(R.id.introduceTv);
		mEvaLvAllTv = (TextView) findViewById(R.id.evaLvAllTv);
		mEvaLvAllTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mEvaLvAllTv.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.meal_cate_click));
				mEvaLvAllTv
						.setTextColor(getResources().getColor(R.color.white));
				mEvaLv3Tv.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.meal_cate_normal));
				mEvaLv3Tv.setTextColor(getResources().getColor(R.color.black));
				mEvaLv2Tv.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.meal_cate_normal));
				mEvaLv2Tv.setTextColor(getResources().getColor(R.color.black));
				mEvaLv1Tv.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.meal_cate_normal));
				mEvaLv1Tv.setTextColor(getResources().getColor(R.color.black));
				evaLv = 0;
				curPageIndex = 1;
				productCommentList.clear();
				getEvaInfo(evaLv, descFlag);
			}
		});
		mEvaLv3Tv = (TextView) findViewById(R.id.evaLv3Tv);
		mEvaLv3Tv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mEvaLvAllTv.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.meal_cate_normal));
				mEvaLvAllTv
						.setTextColor(getResources().getColor(R.color.black));
				mEvaLv3Tv.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.meal_cate_click));
				mEvaLv3Tv.setTextColor(getResources().getColor(R.color.white));
				mEvaLv2Tv.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.meal_cate_normal));
				mEvaLv2Tv.setTextColor(getResources().getColor(R.color.black));
				mEvaLv1Tv.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.meal_cate_normal));
				mEvaLv1Tv.setTextColor(getResources().getColor(R.color.black));
				evaLv = 3;
				curPageIndex = 1;
				productCommentList.clear();
				getEvaInfo(evaLv, descFlag);
			}
		});
		mEvaLv2Tv = (TextView) findViewById(R.id.evaLv2Tv);
		mEvaLv2Tv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mEvaLvAllTv.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.meal_cate_normal));
				mEvaLvAllTv
						.setTextColor(getResources().getColor(R.color.black));
				mEvaLv3Tv.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.meal_cate_normal));
				mEvaLv3Tv.setTextColor(getResources().getColor(R.color.black));
				mEvaLv2Tv.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.meal_cate_click));
				mEvaLv2Tv.setTextColor(getResources().getColor(R.color.white));
				mEvaLv1Tv.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.meal_cate_normal));
				mEvaLv1Tv.setTextColor(getResources().getColor(R.color.black));
				evaLv = 2;
				curPageIndex = 1;
				productCommentList.clear();
				getEvaInfo(evaLv, descFlag);
			}
		});
		mEvaLv1Tv = (TextView) findViewById(R.id.evaLv1Tv);
		mEvaLv1Tv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mEvaLvAllTv.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.meal_cate_normal));
				mEvaLvAllTv
						.setTextColor(getResources().getColor(R.color.black));
				mEvaLv3Tv.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.meal_cate_normal));
				mEvaLv3Tv.setTextColor(getResources().getColor(R.color.black));
				mEvaLv2Tv.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.meal_cate_normal));
				mEvaLv2Tv.setTextColor(getResources().getColor(R.color.black));
				mEvaLv1Tv.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.meal_cate_click));
				mEvaLv1Tv.setTextColor(getResources().getColor(R.color.white));
				evaLv = 1;
				curPageIndex = 1;
				productCommentList.clear();
				getEvaInfo(evaLv, descFlag);
			}
		});
		mContentCommentTv = (TextView) findViewById(R.id.contentCommentTv);
		final Drawable checkDrawable = getResources().getDrawable(
				R.drawable.mealcate_check);
		checkDrawable.setBounds(0, 0, checkDrawable.getMinimumWidth(),
				checkDrawable.getMinimumHeight());
		final Drawable unCheckDrawable = getResources().getDrawable(
				R.drawable.mealcate_uncheck);
		unCheckDrawable.setBounds(0, 0, unCheckDrawable.getMinimumWidth(),
				unCheckDrawable.getMinimumHeight());
		mContentCommentTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (descFlag == 0) {// 0：查看所有评价
					mContentCommentTv.setCompoundDrawables(checkDrawable, null,
							null, null);
					descFlag = 1;
				} else if (descFlag == 1) {// 1：只看有内容的评价
					mContentCommentTv.setCompoundDrawables(unCheckDrawable,
							null, null, null);
					descFlag = 0;
				}
				curPageIndex = 1;
				productCommentList.clear();
				getEvaInfo(evaLv, descFlag);
			}
		});
		mCommentLy = (LinearLayout) findViewById(R.id.commentLy);

		mProductCommentListView = (PullToRefreshListView) findViewById(R.id.productCommentListView);
		if (productCommentList == null) {
			productCommentList = new ArrayList<ProductDetailComment>();
		}
		mProductCommentListAdapter = new ProductCommentListAdapter(
				ProductDetailActivity.this, productCommentList);
		mProductCommentListView.setAdapter(mProductCommentListAdapter);
		mProductCommentListView
				.setOnRefreshListener(new OnRefreshListener2<ListView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						curPageIndex = 1;
						productCommentList.clear();
						getEvaInfo(evaLv, descFlag);
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						curPageIndex++;
						getEvaInfo(evaLv, descFlag);
					}
				});

		getProductDetail();
		statEvaNum();
		curPageIndex = 1;
		productCommentList.clear();
		getEvaInfo(evaLv, descFlag);
	}

	private void getProductDetail() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("cityPrdId", cityPrdId);// 城市产品ID
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String url = WDStringRequest.getUrl(Consts.SERVER_getProductDetail,
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
							JSONObject body = null; // 响应体中的body
							if (jsonObject != null) {
								code = jsonObject.getInt("code");
								if (code == 0) {
									body = new JSONObject(jsonObject
											.getString("body"));
								}
							}
							if (body != null) {
								imageLoader.displayImage(
										body.optString("imgURL"), mImgIv);// 产品图片

								mProductNameTv.setText(body
										.optString("prdName"));// 产品名称

								mProductSellNumTv.setText("月销 "
										+ body.optInt("sellNum"));// 月销量
																	// 滚动月销量（30天），非自然月

								mProductFavRateTv.setText("好评率 "
										+ body.optDouble("favRate") * 100 + "%");// 好评率

								mProductPriceTv.setText("￥"
										+ body.optDouble("price"));// 产品价格

								mIntroduceTv.setText("\t\t"
										+ body.optString("description"));// 产品描述
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						StatusUtils.handleError(arg0,
								ProductDetailActivity.this);
					}
				});
		BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
	}

	private void statEvaNum() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("cityPrdId", cityPrdId);// 城市产品ID
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String url = WDStringRequest.getUrl(Consts.SERVER_statEvaNum,
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
							JSONObject body = null; // 响应体中的body
							if (jsonObject != null) {
								code = jsonObject.getInt("code");
								if (code == 0) {
									body = new JSONObject(
											jsonObject.getString("body"));
								}
							}
							if (body != null) {
								mEvaLv3Tv.setText("好评(" + body.optInt("favNum")
										+ ")");// 好评数量
								mEvaLv2Tv.setText("中评(" + body.optInt("midNum")
										+ ")");// 中评数量
								mEvaLv1Tv.setText("差评(" + body.optInt("negNum")
										+ ")");// 差评数量
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						StatusUtils.handleError(arg0,
								ProductDetailActivity.this);
					}
				});
		BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
	}

	private void getEvaInfo(int evaLv, int descFlag) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("cityPrdId", cityPrdId);// 城市产品ID
			jsonObject.put("evaLv", evaLv);// 评分
			jsonObject.put("descFlag", descFlag);// 评价内容标识
			jsonObject.put("pageNo", curPageIndex);// 页码
			jsonObject.put("pageSize", pageSize);// 每页记录数
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String url = WDStringRequest.getUrl(Consts.SERVER_getEvaInfo,
				jsonObject);
		String relative_url = WDStringRequest.getRelativeUrl();
		String sign_body = WDStringRequest.getSignBody();

		WDStringRequest mRequest = new WDStringRequest(Request.Method.GET, url,
				relative_url, sign_body, false,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						mProductCommentListView.onRefreshComplete();
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
								productCommentList.addAll(ProductDetailComment
										.parseJson(body));

								if (productCommentList != null) {
									if (productCommentList.size() > 0) {
										if (((productCommentList.size()) % pageSize) == 0) {
											mProductCommentListView
													.setMode(Mode.BOTH);
										} else {
											mProductCommentListView
													.setMode(Mode.PULL_FROM_START);
										}
									}
									mProductCommentListAdapter
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
						StatusUtils.handleError(arg0,
								ProductDetailActivity.this);
					}
				});
		BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
	}

}