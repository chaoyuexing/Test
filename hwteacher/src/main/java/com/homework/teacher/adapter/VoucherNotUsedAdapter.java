package com.homework.teacher.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.homework.teacher.R;
import com.homework.teacher.data.VoucherRecord;

/**
 * @author zhangkc
 * @date 2017-8-2
 */
public class VoucherNotUsedAdapter extends BaseAdapter {

	private final static String TAG = "VoucherNotUsedAdapter";
	private Context mContext;
	private List<VoucherRecord> mData;

	public VoucherNotUsedAdapter(Context context, List<VoucherRecord> list) {
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
					R.layout.adapter_voucher_not_used, parent, false);
			holder = new ViewHolder();
			holder.init(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final VoucherRecord voucherRecord = (VoucherRecord) getItem(position);
		if (voucherRecord != null) {
			holder.amountTv.setText(voucherRecord.getAmount() + "元");
			holder.givCommentTv.setText(voucherRecord.getGivComment());
		}
		return convertView;
	}

	class ViewHolder {
		private TextView amountTv, givCommentTv;

		void init(View convertView) {
			amountTv = (TextView) convertView.findViewById(R.id.amountTv);
			givCommentTv = (TextView) convertView
					.findViewById(R.id.givCommentTv);
		}
	}
}
