package com.homework.teacher.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.homework.teacher.R;
import com.homework.teacher.data.MealCate;

/**
 * @author zhangkc
 */
public class MealCateAdapter extends BaseAdapter {

	private final static String TAG = "MealCateAdapter";
	private Context mContext;
	private List<MealCate> mData;

	public MealCateAdapter(Context context, List<MealCate> list) {
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
					R.layout.adapter_mealcate_item, parent, false);
			holder = new ViewHolder();
			holder.init(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final MealCate mealCate = (MealCate) getItem(position);
		if (mealCate != null) {
			holder.mealCateNameTv.setText(mealCate.getMealCateName());
			if (mealCate.getChooseFlag() == 0) {// 选定标记 0：未选，1：已选
				holder.mealCateCheckTv.setBackgroundDrawable(mContext
						.getResources()
						.getDrawable(R.drawable.mealcate_uncheck));
			} else {
				holder.mealCateCheckTv.setBackgroundDrawable(mContext
						.getResources().getDrawable(R.drawable.mealcate_check));
			}
		}
		return convertView;
	}

	class ViewHolder {
		private TextView mealCateNameTv, mealCateCheckTv;

		void init(View convertView) {
			mealCateNameTv = (TextView) convertView
					.findViewById(R.id.mealCateNameTv);
			mealCateCheckTv = (TextView) convertView
					.findViewById(R.id.mealCateCheckTv);
		}
	}
}