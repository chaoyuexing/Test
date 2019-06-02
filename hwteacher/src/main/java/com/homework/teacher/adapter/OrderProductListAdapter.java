package com.homework.teacher.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.homework.teacher.R;
import com.homework.teacher.data.MealCar;

/**
 * @author zhangkc
 */
public class OrderProductListAdapter extends BaseAdapter {

	private final static String TAG = "OrderProductListAdapter";
	private Context mContext;
	private List<MealCar> mData;

	public OrderProductListAdapter(Context context, List<MealCar> list) {
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
					R.layout.adapter_orderproduct_item, parent, false);
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
			holder.productNumTv.setText(mealCar.getNum() + "");
		}
		return convertView;
	}

	class ViewHolder {
		private TextView productNameTv, productPriceTv, productNumTv;

		void init(View convertView) {
			productNameTv = (TextView) convertView
					.findViewById(R.id.productNameTv);
			productPriceTv = (TextView) convertView
					.findViewById(R.id.productPriceTv);
			productNumTv = (TextView) convertView
					.findViewById(R.id.productNumTv);
		}
	}
}
