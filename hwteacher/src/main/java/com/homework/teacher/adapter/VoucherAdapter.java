package com.homework.teacher.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.homework.teacher.R;
import com.homework.teacher.data.Voucher;

/**
 * @author zhangkc
 */
public class VoucherAdapter extends BaseAdapter {

	private final static String TAG = "VoucherAdapter";
	private Context mContext;
	private List<Voucher> mData;

	public VoucherAdapter(Context context, List<Voucher> list) {
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
					R.layout.adapter_voucher_item, parent, false);
			holder = new ViewHolder();
			holder.init(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final Voucher voucher = (Voucher) getItem(position);
		if (voucher != null) {
			holder.voucherTv.setText(voucher.getAmount() + "元");
		}
		return convertView;
	}

	class ViewHolder {
		private TextView voucherTv;

		void init(View convertView) {
			voucherTv = (TextView) convertView.findViewById(R.id.voucherTv);
		}
	}
}