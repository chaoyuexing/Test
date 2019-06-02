package com.homework.teacher.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.homework.teacher.R;
import com.homework.teacher.data.MealPlan;

/**
 * @author zhangkc
 */
public class MealPlanAdapter extends BaseAdapter {

	private final static String TAG = "MealPlanAdapter";
	private Context mContext;
	private List<MealPlan> mData;

	public MealPlanAdapter(Context context, List<MealPlan> list) {
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
					R.layout.adapter_mealplan_item, parent, false);
			holder = new ViewHolder();
			holder.init(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final MealPlan mealPlan = (MealPlan) getItem(position);
		if (mealPlan != null) {
			holder.mealCateNameTv.setText(mealPlan.getMealCateName());
			holder.takeTimeTv.setText(mealPlan.getTakeTime());
			holder.dinnerNumTv.setText(String.valueOf(mealPlan.getDinnerNum()));
			holder.takePointNameTv.setText(mealPlan.getTakePointName());
		}
		return convertView;
	}

	class ViewHolder {
		private TextView mealCateNameTv, takeTimeTv, dinnerNumTv,
				takePointNameTv;

		void init(View convertView) {
			mealCateNameTv = (TextView) convertView
					.findViewById(R.id.mealCateNameTv);
			takeTimeTv = (TextView) convertView.findViewById(R.id.takeTimeTv);
			dinnerNumTv = (TextView) convertView.findViewById(R.id.dinnerNumTv);
			takePointNameTv = (TextView) convertView
					.findViewById(R.id.takePointNameTv);
		}
	}
}