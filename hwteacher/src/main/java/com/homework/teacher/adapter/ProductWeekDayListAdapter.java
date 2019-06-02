package com.homework.teacher.adapter;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.homework.teacher.Consts;
import com.homework.teacher.R;
import com.homework.teacher.app.BaseApplication;
import com.homework.teacher.data.ProductWeekDay;
import com.homework.teacher.http.WDStringRequest;
import com.homework.teacher.utils.StatusUtils;

/**
 * @author zhangkc
 */
public class ProductWeekDayListAdapter extends BaseAdapter {

	private final static String TAG = "ProductWeekDayListAdapter";
	private final static String TOKEN = "token";
	private Handler myHandler;
	private Context mContext;
	private List<ProductWeekDay> mData;
	private SharedPreferences mSp;
	private int entryId;// 一周食谱条目ID
	private int productNum;// 份数
	private boolean isIncreaseNum;

	public ProductWeekDayListAdapter(Context context, Handler handler,
			List<ProductWeekDay> list, SharedPreferences sp) {
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
					R.layout.adapter_product_weekday_item, parent, false);
			holder = new ViewHolder();
			holder.init(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final ProductWeekDay productWeekDay = (ProductWeekDay) getItem(position);
		if (productWeekDay != null) {
			int weekDay = productWeekDay.getWeekDay();
			if (position == 0) {
				if (weekDay == 1) {
					holder.productNameTv.setText("[周一]  "
							+ productWeekDay.getPrdName());
				} else if (weekDay == 2) {
					holder.productNameTv.setText("[周二]  "
							+ productWeekDay.getPrdName());
				} else if (weekDay == 3) {
					holder.productNameTv.setText("[周三]  "
							+ productWeekDay.getPrdName());
				} else if (weekDay == 4) {
					holder.productNameTv.setText("[周四]  "
							+ productWeekDay.getPrdName());
				} else if (weekDay == 5) {
					holder.productNameTv.setText("[周五]  "
							+ productWeekDay.getPrdName());
				} else if (weekDay == 6) {
					holder.productNameTv.setText("[周六]  "
							+ productWeekDay.getPrdName());
				} else if (weekDay == 7) {
					holder.productNameTv.setText("[周日]  "
							+ productWeekDay.getPrdName());
				}
			} else {
				if (((ProductWeekDay) getItem(position - 1)) != null) {
					if (productWeekDay.getWeekDay() == ((ProductWeekDay) getItem(position - 1))
							.getWeekDay()) {
						holder.productNameTv.setText("            "
								+ productWeekDay.getPrdName());
					} else {
						if (weekDay == 1) {
							holder.productNameTv.setText("[周一]  "
									+ productWeekDay.getPrdName());
						} else if (weekDay == 2) {
							holder.productNameTv.setText("[周二]  "
									+ productWeekDay.getPrdName());
						} else if (weekDay == 3) {
							holder.productNameTv.setText("[周三]  "
									+ productWeekDay.getPrdName());
						} else if (weekDay == 4) {
							holder.productNameTv.setText("[周四]  "
									+ productWeekDay.getPrdName());
						} else if (weekDay == 5) {
							holder.productNameTv.setText("[周五]  "
									+ productWeekDay.getPrdName());
						} else if (weekDay == 6) {
							holder.productNameTv.setText("[周六]  "
									+ productWeekDay.getPrdName());
						} else if (weekDay == 7) {
							holder.productNameTv.setText("[周日]  "
									+ productWeekDay.getPrdName());
						}
					}
				}
			}
			holder.productPriceTv.setText("￥" + productWeekDay.getPrice());
			holder.decreaseNumTv.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					productNum = Integer.valueOf(holder.productNumTv.getText()
							.toString());
					entryId = productWeekDay.getId();
					if (productNum > 0) {
						productNum--;
						holder.productNumTv.setText(String.valueOf(productNum));
						if (productNum == 0) {
							Message message = new Message();
							message.arg1 = entryId;
							myHandler.sendMessage(message);
						}
					}
					isIncreaseNum = false;
					tokenFetch();
				}
			});
			holder.productNumTv
					.setText(String.valueOf(productWeekDay.getNum()));
			holder.increaseNumTv.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					productNum = Integer.valueOf(holder.productNumTv.getText()
							.toString());
					productNum++;
					holder.productNumTv.setText(String.valueOf(productNum));
					isIncreaseNum = true;
					entryId = productWeekDay.getId();
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
								iodPrd();
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

	private void iodPrd() {
		JSONObject jsonObject = new JSONObject();
		try {
			int cstId = BaseApplication.getInstance().getCstID();
			jsonObject.put("cstId", cstId);// 客户ID
			jsonObject.put("entryId", entryId);// 一周食谱条目ID
			if (isIncreaseNum) {
				jsonObject.put("iodNum", 1);// 增加或减少份数 大于0为增加，小于0为减少
			} else {
				jsonObject.put("iodNum", -1);// 增加或减少份数 大于0为增加，小于0为减少
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String url = WDStringRequest.getUrl(Consts.SERVER_iodPrd, jsonObject);
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
									Toast.makeText(mContext, "成功",
											Toast.LENGTH_SHORT).show();
								} else {// code==1 响应错误

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
		private TextView weekDayTv, productNameTv, productPriceTv,
				decreaseNumTv, productNumTv, increaseNumTv;
		private View itemLineView, lineView;

		void init(View convertView) {
			weekDayTv = (TextView) convertView.findViewById(R.id.weekDayTv);
			productNameTv = (TextView) convertView
					.findViewById(R.id.productNameTv);
			productPriceTv = (TextView) convertView
					.findViewById(R.id.productPriceTv);
			decreaseNumTv = (TextView) convertView
					.findViewById(R.id.decreaseNumTv);
			productNumTv = (TextView) convertView
					.findViewById(R.id.productNumTv);
			increaseNumTv = (TextView) convertView
					.findViewById(R.id.increaseNumTv);
			itemLineView = convertView.findViewById(R.id.itemLineView);
			lineView = convertView.findViewById(R.id.lineView);
		}
	}
}