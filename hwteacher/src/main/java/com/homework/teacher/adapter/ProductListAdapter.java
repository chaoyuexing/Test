package com.homework.teacher.adapter;

import java.math.BigDecimal;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.homework.teacher.Consts;
import com.homework.teacher.R;
import com.homework.teacher.activity.AddToCookBookActivity;
import com.homework.teacher.activity.LoginActivity;
import com.homework.teacher.app.BaseApplication;
import com.homework.teacher.data.Product;
import com.homework.teacher.http.WDStringRequest;
import com.homework.teacher.utils.StatusUtils;

/**
 * @author zhangkc
 */
public class ProductListAdapter extends BaseAdapter {

	private final static String TAG = "ProductListAdapter";
	private final static String TOKEN = "token";
	private Handler myHandler;
	private Context mContext;
	private ImageLoader mImageLoader;
	private List<Product> mData;
	private SharedPreferences mSp;
	private int cityPrdId, prdId;

	public ProductListAdapter(Context context, Handler handler,
			ImageLoader imageLoader, List<Product> list, SharedPreferences sp) {
		this.mContext = context;
		this.myHandler = handler;
		this.mImageLoader = imageLoader;
		this.mData = list;
		this.mSp = sp;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public boolean isEmpty() {
		return mData.size() == 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null || convertView.getTag() == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.adapter_product_item, parent, false);
			holder = new ViewHolder();
			holder.init(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final Product product = (Product) getItem(position);
		if (product != null) {
			mImageLoader.displayImage(product.getThumbnailURL(),
					holder.productIv);
			holder.productNameTv.setText(product.getPrdName());
			holder.productSellNumTv.setText("月销 " + product.getSellNum());
			holder.productFavRateTv.setText("好评率 "
					+ mul(product.getFavRate(), 100) + "%");
			holder.productPriceTv.setText("￥" + product.getPrice());
			holder.addToCookBookTv
					.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent();
							if (BaseApplication.getInstance().getCstID() != 0) {
								intent.putExtra(AddToCookBookActivity.PROID,
										product.getPrdId());
								intent.putExtra(AddToCookBookActivity.PRONAME,
										product.getPrdName());
								intent.putExtra(AddToCookBookActivity.URL,
										product.getThumbnailURL());
								intent.setClass(mContext,
										AddToCookBookActivity.class);
							} else {
								intent.setClass(mContext, LoginActivity.class);
							}
							mContext.startActivity(intent);
						}
					});
			holder.addToProductCarTv
					.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							cityPrdId = product.getId();
							prdId = product.getPrdId();
							tokenFetch();
						}
					});
		}
		return convertView;
	}

	/**
	 * float和double只能用来做科学计算或者是工程计算，在商业计算中我们要用java.math.BigDecimal
	 * 我们如果需要精确计算，非要用String来够造BigDecimal不可
	 * 
	 * @param num1
	 * @param num2
	 * @return
	 */
	public static double mul(double num1, int num2) {
		BigDecimal bd = new BigDecimal(Double.toString(num1));
		BigDecimal bd2 = new BigDecimal(num2);
		return bd.multiply(bd2).doubleValue();
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
								mSp.edit()
										.putString(TOKEN,
												body.optString("token"))
										.commit();
								Log.i(TAG, "接口访问令牌存入本地~~~");
								iodCart();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						StatusUtils.handleError(arg0, mContext);
					}
				});
		BaseApplication.getInstance().addToRequestQueue(mRequest, "");
	}

	private void iodCart() {
		JSONObject jsonObject = new JSONObject();
		try {
			int cstId = BaseApplication.getInstance().getCstID();
			// if (cstId == 0) {
			// throw new IllegalStateException("need a cstId, but now is null");
			// } else {
			jsonObject.put("cstId", cstId);// 客户ID
			// }
			int ordId = BaseApplication.getInstance().getORDID();
			// if (ordId == 0) {
			// throw new IllegalStateException("need a ordId, but now is null");
			// } else {
			jsonObject.put("ordId", ordId);// 订单号
			// }
			jsonObject.put("iodNum", 1);// 增减份数
			jsonObject.put("cityPrdId", cityPrdId);// 城市产品ID
			jsonObject.put("prdId", prdId);// 产品ID
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String url = WDStringRequest.getUrl(Consts.SERVER_iodCart, jsonObject);
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
								} else {// code==1 响应错误
									Intent intent = new Intent();
									intent.setClass(mContext,
											LoginActivity.class);
									mContext.startActivity(intent);
								}
							}
							if (body != null) {
								// int ordId = body.optInt("ordId");
								int totalNum = body.optInt("totalNum");
								double totalPrice = body
										.optDouble("totalPrice");
								Message message = new Message();
								Bundle bundle = new Bundle();
								bundle.putInt("totalNum", totalNum);
								bundle.putDouble("totalPrice", totalPrice);
								message.setData(bundle);
								myHandler.sendMessage(message);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						StatusUtils.handleError(arg0, mContext);
					}
				});
		BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
	}

	class ViewHolder {
		private ImageView productIv;
		private TextView productNameTv, productSellNumTv, productFavRateTv,
				productPriceTv, addToCookBookTv, addToProductCarTv;

		void init(View convertView) {
			productIv = (ImageView) convertView.findViewById(R.id.productIv);
			productNameTv = (TextView) convertView
					.findViewById(R.id.productNameTv);
			productSellNumTv = (TextView) convertView
					.findViewById(R.id.productSellNumTv);
			productFavRateTv = (TextView) convertView
					.findViewById(R.id.productFavRateTv);
			productPriceTv = (TextView) convertView
					.findViewById(R.id.productPriceTv);
			addToCookBookTv = (TextView) convertView
					.findViewById(R.id.addToCookBookTv);
			addToProductCarTv = (TextView) convertView
					.findViewById(R.id.addToProductCarTv);
		}
	}
}