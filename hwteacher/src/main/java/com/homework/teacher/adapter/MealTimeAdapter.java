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
import com.homework.teacher.data.MealTime;

/**
 * @author zhangkc
 */
public class MealTimeAdapter extends BaseAdapter {

	private final static String TAG = "MealTimeAdapter";
	private List<MealTime> mData;
	private Context mContext;
	private int itemId;// 点击item的位置id

	public MealTimeAdapter(Context context, int itemId, List<MealTime> list) {
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
						R.layout.adapter_mealtime_item_onclick, parent, false);
			} else {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.adapter_mealtime_item, parent, false);
			}
			holder = new ViewHolder();
			holder.init(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final MealTime mt = (MealTime) getItem(position);
		if (mt != null) {
			holder.mealTimeTv.setText(mt.getValue());

		}
		return convertView;
	}

	class ViewHolder {
		private TextView mealTimeTv;

		void init(View convertView) {
			mealTimeTv = (TextView) convertView.findViewById(R.id.mealTimeTv);
		}
	}
}
