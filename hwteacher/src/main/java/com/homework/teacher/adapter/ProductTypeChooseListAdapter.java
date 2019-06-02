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
import com.homework.teacher.data.ProductTypeChoose;

/**
 * @author zhangkc
 */
public class ProductTypeChooseListAdapter extends BaseAdapter {
	private final static String TAG = "ProductTypeChooseListAdapter";
	private List<ProductTypeChoose> mData;
	private Context mContext;
	private int itemId;// 点击item的位置id

	public ProductTypeChooseListAdapter(Context context, int itemId,
			List<ProductTypeChoose> list) {
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
						R.layout.adapter_product_type_choose_item_onclick,
						parent, false);
			} else {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.adapter_product_type_choose_item, parent,
						false);
			}
			holder = new ViewHolder();
			holder.init(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final ProductTypeChoose ptc = (ProductTypeChoose) getItem(position);
		if (ptc != null) {
			holder.productTypeChooseNameTv.setText(ptc.getName());
			holder.productTypeChooseNumTv.setText(ptc.getPrdNum() + "");
		}
		return convertView;
	}

	class ViewHolder {
		private TextView productTypeChooseNameTv, productTypeChooseNumTv;

		void init(View convertView) {
			productTypeChooseNameTv = (TextView) convertView
					.findViewById(R.id.productTypeChooseNameTv);
			productTypeChooseNumTv = (TextView) convertView
					.findViewById(R.id.productTypeChooseNumTv);
		}
	}
}