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
import com.homework.teacher.data.RecommendName;

/**
 * @author zhangkc
 * @date 2017-7-19
 */
public class RecommendNameListAdapter extends BaseAdapter {
	private final static String TAG = "RecommendNameListAdapter";
	private List<RecommendName> mData;
	private Context mContext;
	private int itemId;// 点击item的位置id

	public RecommendNameListAdapter(Context context, int itemId,
			List<RecommendName> list) {
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
		final RecommendName recommendName = (RecommendName) getItem(position);
		if (recommendName != null) {
			holder.productTypeChooseNameTv.setText(recommendName.getRecName());
			holder.productTypeChooseNumTv.setText("");
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
