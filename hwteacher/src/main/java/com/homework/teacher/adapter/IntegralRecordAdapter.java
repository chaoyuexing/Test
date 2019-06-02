package com.homework.teacher.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.homework.teacher.R;
import com.homework.teacher.data.IntegralRecord;

/**
 * @author zhangkc
 * @date 2017-7-25
 */
public class IntegralRecordAdapter extends BaseAdapter {

	private final static String TAG = "IntegralRecordAdapter";
	private Context mContext;
	private List<IntegralRecord> mData;

	public IntegralRecordAdapter(Context context, List<IntegralRecord> list) {
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
					R.layout.adapter_integral_record_item, parent, false);
			holder = new ViewHolder();
			holder.init(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final IntegralRecord integralRecord = (IntegralRecord) getItem(position);
		if (integralRecord != null) {
			holder.commentTv.setText(integralRecord.getComment());
			holder.valueTv.setText(integralRecord.getValue() + "");
		}
		return convertView;
	}

	class ViewHolder {
		private TextView commentTv, valueTv;

		void init(View convertView) {
			commentTv = (TextView) convertView.findViewById(R.id.commentTv);
			valueTv = (TextView) convertView.findViewById(R.id.valueTv);
		}
	}
}
