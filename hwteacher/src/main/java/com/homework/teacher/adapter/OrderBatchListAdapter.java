package com.homework.teacher.adapter;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
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
import com.homework.teacher.data.OrderDetail;
import com.homework.teacher.http.WDStringRequest;
import com.homework.teacher.utils.StatusUtils;

/**
 * 批量下单 or 订单批量操作 adapter
 * 
 * @author zhangkc
 * @date 2017-7-5
 */
public class OrderBatchListAdapter extends BaseAdapter {
	private final static String TAG = "OrderBatchListAdapter";
	private Handler myHandler;
	private Context mContext;
	private List<OrderDetail> mData;
	private int ordId;

	public OrderBatchListAdapter(Context context, Handler handler,
			List<OrderDetail> list) {
		this.mContext = context;
		this.myHandler = handler;
		this.mData = list;
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
					R.layout.adapter_order_batch_item, parent, false);
			holder = new ViewHolder();
			holder.init(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final OrderDetail orderDetail = (OrderDetail) getItem(position);
		if (orderDetail != null) {
			ordId = orderDetail.getOrdId();
			if (orderDetail.getDayType() == 1) {
				holder.dayTv.setBackgroundDrawable(mContext.getResources()
						.getDrawable(R.drawable.holiday));
			} else if (orderDetail.getDayType() == 2) {
				holder.dayTv.setBackgroundDrawable(mContext.getResources()
						.getDrawable(R.drawable.weekday));
			}
			holder.timeTv.setText(orderDetail.getDinnerDate() + " "
					+ orderDetail.getTtValue());
			holder.takePointTv.setText(orderDetail.getTpName());
			holder.totalPriceTv.setText("￥" + orderDetail.getTotalPrice());
			holder.dinnerNumTv.setText(orderDetail.getDinnerNum() + "人用餐");
			holder.orderDeleteTv.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					orderDelete();
				}
			});
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

	class ViewHolder {
		private TextView dayTv, timeTv, takePointTv, totalPriceTv, dinnerNumTv,
				orderDeleteTv;

		void init(View convertView) {
			dayTv = (TextView) convertView.findViewById(R.id.dayTv);
			timeTv = (TextView) convertView.findViewById(R.id.timeTv);
			takePointTv = (TextView) convertView.findViewById(R.id.takePointTv);
			totalPriceTv = (TextView) convertView
					.findViewById(R.id.totalPriceTv);
			dinnerNumTv = (TextView) convertView.findViewById(R.id.dinnerNumTv);
			orderDeleteTv = (TextView) convertView
					.findViewById(R.id.orderDeleteTv);
		}
	}
}