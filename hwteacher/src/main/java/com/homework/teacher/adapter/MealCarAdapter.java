package com.homework.teacher.adapter;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.homework.teacher.Consts;
import com.homework.teacher.R;
import com.homework.teacher.app.BaseApplication;
import com.homework.teacher.data.MealCar;
import com.homework.teacher.http.WDStringRequest;
import com.homework.teacher.utils.StatusUtils;

/**
 * @author zhangkc
 */
public class MealCarAdapter extends BaseAdapter {

	private final static String TAG = "MealCarAdapter";
	private final static String TOKEN = "token";
	private Handler myHandler;
	private Context mContext;
	private List<MealCar> mData;
	private SharedPreferences mSp;
	private int cityPrdId, prdId;
	private int productNum;// 份数
	private boolean isAddToProductCar;

	public MealCarAdapter(Context context, Handler handler, List<MealCar> list,
			SharedPreferences sp) {
		this.mContext = context;
		this.myHandler = handler;
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
					R.layout.adapter_mealcar_item, parent, false);
			holder = new ViewHolder();
			holder.init(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final MealCar mealCar = (MealCar) getItem(position);
		if (mealCar != null) {
			holder.productNameTv.setText(mealCar.getPrdName());
			holder.productPriceTv.setText("￥" + mealCar.getPrice());
			holder.deleteToProductCarTv
					.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							productNum = Integer.valueOf(holder.productNumTv
									.getText().toString());
							prdId = mealCar.getPrdId();
							if (productNum > 0) {
								productNum--;
								holder.productNumTv.setText(String
										.valueOf(productNum));
								if (productNum == 0) {
									Message message = new Message();
									message.arg1 = prdId;
									myHandler.sendMessage(message);
								}
							}
							cityPrdId = mealCar.getCityPrdId();
							isAddToProductCar = false;
							tokenFetch();
						}
					});
			holder.productNumTv.setText(String.valueOf(mealCar.getNum()));
			holder.addToProductCarTv
					.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							productNum = Integer.valueOf(holder.productNumTv
									.getText().toString());
							productNum++;
							holder.productNumTv.setText(String
									.valueOf(productNum));
							cityPrdId = mealCar.getCityPrdId();
							prdId = mealCar.getPrdId();
							isAddToProductCar = true;
							tokenFetch();
						}
					});
		}
		return convertView;
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
			if (isAddToProductCar) {
				jsonObject.put("iodNum", 1);// 增减份数
			} else {
				jsonObject.put("iodNum", -1);// 增减份数
			}
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
								}
							}
							if (body != null) {
								// int ordId = body.optInt("ordId");
								int totalNum = body.optInt("totalNum");
								double totalPrice = body
										.optDouble("totalPrice");
								int vid = body.optInt("vid");
								double vAmount = body.optDouble("vAmount");
								double waitPay = body.optDouble("waitPay");
								Log.i(TAG, "totalNum: " + totalNum
										+ " , totalPrice: " + totalPrice
										+ " , vid: " + vid + " , vAmount: "
										+ vAmount + " , waitPay: " + waitPay);
								Message message = new Message();
								Bundle bundle = new Bundle();
								bundle.putDouble("totalPrice", totalPrice);
								bundle.putDouble("vAmount", vAmount);
								bundle.putDouble("waitPay", waitPay);
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
		private TextView productNameTv, productPriceTv, deleteToProductCarTv,
				productNumTv, addToProductCarTv;

		void init(View convertView) {
			productNameTv = (TextView) convertView
					.findViewById(R.id.productNameTv);
			productPriceTv = (TextView) convertView
					.findViewById(R.id.productPriceTv);
			deleteToProductCarTv = (TextView) convertView
					.findViewById(R.id.deleteToProductCarTv);
			productNumTv = (TextView) convertView
					.findViewById(R.id.productNumTv);
			addToProductCarTv = (TextView) convertView
					.findViewById(R.id.addToProductCarTv);
		}
	}
}