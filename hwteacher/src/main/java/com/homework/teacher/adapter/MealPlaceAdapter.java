package com.homework.teacher.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.homework.teacher.R;
import com.homework.teacher.data.MealPlace;

/**
 * @author zhangkc
 */
public class MealPlaceAdapter extends BaseAdapter {

	private final static String TAG = "MealPlaceAdapter";
	private Context mContext;
	private List<MealPlace> mData;

	public MealPlaceAdapter(Context context, List<MealPlace> list) {
		this.mContext = context;
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
					R.layout.adapter_mealplace_item, parent, false);
			holder = new ViewHolder();
			holder.init(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final MealPlace mealPlace = (MealPlace) getItem(position);
		if (mealPlace != null) {
			holder.mealPlaceNameTv.setText(mealPlace.getAreaName2() + "-"
					+ mealPlace.getAreaName3() + "-" + mealPlace.getAreaName4()
					+ "-" + mealPlace.getName());
		}
		return convertView;
	}

	class ViewHolder {
		private TextView mealPlaceNameTv;

		void init(View convertView) {
			mealPlaceNameTv = (TextView) convertView
					.findViewById(R.id.mealPlaceNameTv);
		}
	}
}