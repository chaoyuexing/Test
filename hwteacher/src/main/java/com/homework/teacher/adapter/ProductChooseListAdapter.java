package com.homework.teacher.adapter;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.homework.teacher.Consts;
import com.homework.teacher.R;
import com.homework.teacher.activity.AddToCookBookActivity;
import com.homework.teacher.app.BaseApplication;
import com.homework.teacher.data.ProductChoose;
import com.homework.teacher.http.WDStringRequest;
import com.homework.teacher.utils.StatusUtils;

/**
 * @author zhangkc
 */
public class ProductChooseListAdapter extends BaseAdapter {
	private final static String TAG = "ProductChooseListAdapter";
	private final static String TOKEN = "token";
	private Handler myHandler;
	private Context mContext;
	private List<ProductChoose> mData;
	private SharedPreferences mSp;
	private int prdId;

	public ProductChooseListAdapter(Context context, Handler handler,
			List<ProductChoose> list, SharedPreferences sp) {
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
					R.layout.adapter_product_choose_item, parent, false);
			holder = new ViewHolder();
			holder.init(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final ProductChoose productChoose = (ProductChoose) getItem(position);
		if (productChoose != null) {
			holder.productChooseInfoRl
					.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent();
							intent.putExtra(AddToCookBookActivity.PROID,
									productChoose.getPrdId());
							intent.putExtra(AddToCookBookActivity.PRONAME,
									productChoose.getPrdName());
							intent.putExtra(AddToCookBookActivity.URL,
									productChoose.getThumbnailURL());
							intent.setClass(mContext,
									AddToCookBookActivity.class);
							mContext.startActivity(intent);
						}
					});
			holder.productChooseNameTv.setText(productChoose.getPrdName());
			holder.productChoosePriceTv.setText("￥" + productChoose.getPrice());
			holder.productChooseMealsTv.setText(productChoose.getMeals());
			holder.productChooseDeleteTv
					.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							prdId = productChoose.getPrdId();
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
								deletePrd();
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

	private void deletePrd() {
		JSONObject jsonObject = new JSONObject();
		try {
			int cstId = BaseApplication.getInstance().getCstID();
			jsonObject.put("cstId", cstId);// 客户ID
			jsonObject.put("prdId", prdId);// 商品ID
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String url = WDStringRequest
				.getUrl(Consts.SERVER_deletePrd, jsonObject);
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
									Toast.makeText(mContext, "删除该商品成功",
											Toast.LENGTH_SHORT).show();
									Message message = new Message();
									myHandler.sendMessage(message);
								} else {
									Toast.makeText(mContext, "删除该商品失败",
											Toast.LENGTH_SHORT).show();
								}
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
		private RelativeLayout productChooseInfoRl;
		private TextView productChooseNameTv, productChoosePriceTv,
				productChooseMealsTv, productChooseDeleteTv;

		void init(View convertView) {
			productChooseInfoRl = (RelativeLayout) convertView
					.findViewById(R.id.productChooseInfoRl);
			productChooseNameTv = (TextView) convertView
					.findViewById(R.id.productChooseNameTv);
			productChoosePriceTv = (TextView) convertView
					.findViewById(R.id.productChoosePriceTv);
			productChooseMealsTv = (TextView) convertView
					.findViewById(R.id.productChooseMealsTv);
			productChooseDeleteTv = (TextView) convertView
					.findViewById(R.id.productChooseDeleteTv);
		}
	}
}