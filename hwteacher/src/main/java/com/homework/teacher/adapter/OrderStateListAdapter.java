package com.homework.teacher.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.homework.teacher.R;
import com.homework.teacher.data.OrderState;

/**
 * @author zhangkc
 */
public class OrderStateListAdapter extends BaseAdapter {
	private final static String TAG = "OrderStateListAdapter";
	private List<OrderState> mData;
	private Context mContext;
	private int itemId;// 点击item的位置id

	public OrderStateListAdapter(Context context, int itemId,
			List<OrderState> list) {
		this.mContext = context;
		this.itemId = itemId;
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
		Log.e(TAG, "position: " + position);
		final ViewHolder holder;
		if (convertView == null || convertView.getTag() == null) {
			if (position == itemId) {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.adapter_order_state_item_onclick, parent,
						false);
			} else {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.adapter_order_state_item, parent, false);
			}
			holder = new ViewHolder();
			holder.init(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final OrderState os = (OrderState) getItem(position);
		if (os != null) {
			String stateName = os.getStateName();
			holder.stateNameTv.setText(stateName);

			int num = os.getNum();
			if (num == 0) {
				holder.numTv.setVisibility(View.GONE);
			} else {
				holder.numTv.setText(num + "");
			}
		}
		return convertView;
	}

	class ViewHolder {
		private TextView stateNameTv, numTv;

		void init(View convertView) {
			stateNameTv = (TextView) convertView.findViewById(R.id.stateNameTv);
			numTv = (TextView) convertView.findViewById(R.id.numTv);
		}
	}
}