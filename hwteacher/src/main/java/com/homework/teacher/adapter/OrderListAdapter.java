package com.homework.teacher.adapter;

import java.util.Date;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.homework.teacher.Consts;
import com.homework.teacher.R;
import com.homework.teacher.activity.OrderCommentActivity;
import com.homework.teacher.activity.OrderDetailActivity;
import com.homework.teacher.app.BaseApplication;
import com.homework.teacher.data.OrderDetail;
import com.homework.teacher.http.WDStringRequest;
import com.homework.teacher.utils.DateUtils;
import com.homework.teacher.utils.StatusUtils;
import com.homework.teacher.widget.MyCommonDialog;

/**
 * @author zhangkc
 * @date 2017-5-15
 */
public class OrderListAdapter extends BaseAdapter {
	private final static String TAG = "OrderListAdapter";
	private final static String TOKEN = "token";
	private Handler myHandler;
	private Context mContext;
	private List<OrderDetail> mData;
	private SharedPreferences mSp;
	private MyCommonDialog dialog;
	private int ordId;
	private int state;
	// 取餐时间前1.5小时截止退订，把退订按钮隐藏。
	private long timeDifference = 5400000;// 1.5*60*60*1000 =
											// 5400000毫秒，即1.5小时时间差。

	public OrderListAdapter(Context context, Handler handler,
			List<OrderDetail> list, SharedPreferences sp) {
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
					R.layout.adapter_order_item, parent, false);
			holder = new ViewHolder();
			holder.init(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final OrderDetail orderDetail = (OrderDetail) getItem(position);
		if (orderDetail != null) {
			holder.timeTv.setText(orderDetail.getDinnerDate());
			holder.mealCateTv.setText(orderDetail.getMcName());
			holder.takeTimeTv.setText(orderDetail.getTtValue());
			holder.totalPriceTv.setText("￥"
					+ String.valueOf(orderDetail.getTotalPrice()));
			holder.takePointTv.setText(orderDetail.getTpName());
			holder.dinnerNumTv.setText(orderDetail.getDinnerNum() + "人用餐");

			state = orderDetail.getState();
			if (state == 1) {// 待支付
				holder.btnTv1.setVisibility(View.VISIBLE);
				holder.btnTv1.setText("删除");
				holder.btnTv1.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						ordId = orderDetail.getOrdId();
						orderDelete();
					}
				});
				holder.btnTv2.setVisibility(View.VISIBLE);
				holder.btnTv2.setText("支付");
				holder.btnTv2.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent();
						int ordId = orderDetail.getOrdId();
						intent.putExtra(OrderDetailActivity.ORDID, ordId);// 订单号
						intent.putExtra(OrderDetailActivity.STATE, state);// 订单状态
						intent.setClass(mContext, OrderDetailActivity.class);
						mContext.startActivity(intent);
					}
				});
				holder.textView.setVisibility(View.INVISIBLE);
			} else if (state == 2) {// 制作中
				String date = orderDetail.getDinnerDate() + " "
						+ orderDetail.getTtValue() + ":59";
				Log.i(TAG, "取餐时间: " + date);
				Date endDate = DateUtils.stringToDate2(date);
				// 获得两个时间的毫秒时间差异
				long diff = endDate.getTime() - System.currentTimeMillis();
				if (diff >= timeDifference) {
					holder.btnTv1.setVisibility(View.INVISIBLE);
					holder.btnTv2.setVisibility(View.VISIBLE);
					holder.btnTv2.setText("退订");
					holder.btnTv2
							.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									String mealCate = orderDetail
											.getDinnerDate()
											+ " "
											+ orderDetail.getMcName();
									dialog = new MyCommonDialog(mContext,
											"提示消息", "确定要退订" + mealCate + "吗？",
											"取消", "确定");
									dialog.setOkListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											if (dialog.isShowing()) {
												dialog.dismiss();
											}
											ordId = orderDetail.getOrdId();
											tokenFetch();
											if (dialog.isShowing()) {
												dialog.dismiss();
											}
										}
									});
									dialog.setCancelListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											if (dialog.isShowing()) {
												dialog.dismiss();
											}
										}
									});
									dialog.show();
								}
							});
					holder.textView.setVisibility(View.INVISIBLE);
				} else {
					holder.btnTv1.setVisibility(View.INVISIBLE);
					holder.btnTv2.setVisibility(View.INVISIBLE);
					holder.textView.setVisibility(View.VISIBLE);
					holder.textView.setTextColor(mContext.getResources()
							.getColor(R.color.theme_color));
					holder.textView.setText("烹饪中...");
				}
			} else if (state == 3) {// 待领取
				holder.btnTv1.setVisibility(View.INVISIBLE);
				holder.btnTv2.setVisibility(View.INVISIBLE);
				holder.textView.setVisibility(View.VISIBLE);
				holder.textView.setTextColor(mContext.getResources().getColor(
						R.color.black));
				holder.textView.setText("取餐码:" + orderDetail.getFetchCode());
			} else if (state == 4) {// 待点评
				holder.btnTv1.setVisibility(View.INVISIBLE);
				holder.btnTv2.setVisibility(View.VISIBLE);
				holder.btnTv2.setText("点评");
				holder.btnTv2.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent();
						ordId = orderDetail.getOrdId();
						intent.putExtra(OrderDetailActivity.ORDID, ordId);// 订单号
						intent.setClass(mContext, OrderCommentActivity.class);
						mContext.startActivity(intent);
					}
				});
				holder.textView.setVisibility(View.INVISIBLE);
			} else if (state == 5 || state == -1) {// 已完成或已退订
				holder.btnTv1.setVisibility(View.INVISIBLE);
				holder.btnTv2.setVisibility(View.VISIBLE);
				holder.btnTv2.setText("删除");
				holder.btnTv2.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						ordId = orderDetail.getOrdId();
						orderDelete();
					}
				});
				holder.textView.setVisibility(View.INVISIBLE);
			}
		}
		return convertView;
	}

	private void orderDelete() {
		JSONObject jsonObject = new JSONObject();
		try {
			int cstId = BaseApplication.getInstance().getCstID();
			jsonObject.put("cstId", cstId);// 客户ID
			jsonObject.put("ordId", ordId);// 订单号
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String url = WDStringRequest.getUrl(Consts.SERVER_orderDelete,
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
							if (jsonObject != null) {
								code = jsonObject.getInt("code");
								if (code == 0) {
									Toast.makeText(mContext, "删除成功",
											Toast.LENGTH_SHORT).show();
									Message message = new Message();
									message.arg1 = state;
									myHandler.sendMessage(message);
								} else {
									Toast.makeText(mContext, "删除失败",
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
								unSubscribe();
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

	private void unSubscribe() {
		JSONObject jsonObject = new JSONObject();
		try {
			int cstId = BaseApplication.getInstance().getCstID();
			jsonObject.put("cstId", cstId);// 客户ID
			jsonObject.put("ordId", ordId);// 订单号
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String url = WDStringRequest.getUrl(Consts.SERVER_unSubscribe,
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
									Toast.makeText(mContext, "退订成功",
											Toast.LENGTH_SHORT).show();
									Message message = new Message();
									message.arg1 = state;
									myHandler.sendMessage(message);
								} else if (code == 10406) {
									Toast.makeText(mContext,
											"抱歉，已过退订截止时间，无法退订",
											Toast.LENGTH_SHORT).show();
								} else if (code == 10407) {
									Toast.makeText(mContext, "退订失败，可联系客服协助处理",
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
		private TextView timeTv, mealCateTv, takeTimeTv, totalPriceTv,
				takePointTv, dinnerNumTv, btnTv1, btnTv2, textView;

		void init(View convertView) {
			timeTv = (TextView) convertView.findViewById(R.id.timeTv);
			mealCateTv = (TextView) convertView.findViewById(R.id.mealCateTv);
			takeTimeTv = (TextView) convertView.findViewById(R.id.takeTimeTv);
			totalPriceTv = (TextView) convertView
					.findViewById(R.id.totalPriceTv);
			takePointTv = (TextView) convertView.findViewById(R.id.takePointTv);
			dinnerNumTv = (TextView) convertView.findViewById(R.id.dinnerNumTv);
			btnTv1 = (TextView) convertView.findViewById(R.id.btnTv1);
			btnTv2 = (TextView) convertView.findViewById(R.id.btnTv2);
			textView = (TextView) convertView.findViewById(R.id.textView);
		}
	}
}