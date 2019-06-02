package com.homework.teacher.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.homework.teacher.R;
import com.homework.teacher.data.ProductRecommend;

/**
 * @author zhangkc
 * @date 2017-7-21
 */
public class RecommendListAdapter extends BaseAdapter {
	private final static String TAG = "RecommendListAdapter";
	private List<ProductRecommend> mData;
	private Context mContext;

	public RecommendListAdapter(Context context, List<ProductRecommend> list) {
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
					R.layout.adapter_product_recommend_item, parent, false);
			holder = new ViewHolder();
			holder.init(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final ProductRecommend productRecommend = (ProductRecommend) getItem(position);
		if (productRecommend != null) {
			if (productRecommend.getIsTitle() == 1) {
				int weekDay = productRecommend.getWeekDay();
				if (weekDay == 1) {
					holder.prdNameTv.setText("[周一]  "
							+ productRecommend.getPrdName());
				} else if (weekDay == 2) {
					holder.prdNameTv.setText("[周二]  "
							+ productRecommend.getPrdName());
				} else if (weekDay == 3) {
					holder.prdNameTv.setText("[周三]  "
							+ productRecommend.getPrdName());
				} else if (weekDay == 4) {
					holder.prdNameTv.setText("[周四]  "
							+ productRecommend.getPrdName());
				} else if (weekDay == 5) {
					holder.prdNameTv.setText("[周五]  "
							+ productRecommend.getPrdName());
				} else if (weekDay == 6) {
					holder.prdNameTv.setText("[周六]  "
							+ productRecommend.getPrdName());
				} else if (weekDay == 7) {
					holder.prdNameTv.setText("[周日]  "
							+ productRecommend.getPrdName());
				}
			} else {
				holder.prdNameTv.setText("            "
						+ productRecommend.getPrdName());
			}
			holder.priceTv.setText("￥" + productRecommend.getPrice());
			holder.numTv.setText("x" + productRecommend.getNum());
		}
		return convertView;
	}

	class ViewHolder {
		private View lineTopView, lineBottomView;
		private TextView weekDayTv, prdNameTv, priceTv, numTv;

		void init(View convertView) {
			lineTopView = convertView.findViewById(R.id.lineTopView);
			lineBottomView = convertView.findViewById(R.id.lineBottomView);

			weekDayTv = (TextView) convertView.findViewById(R.id.weekDayTv);
			prdNameTv = (TextView) convertView.findViewById(R.id.prdNameTv);
			priceTv = (TextView) convertView.findViewById(R.id.priceTv);
			numTv = (TextView) convertView.findViewById(R.id.numTv);
		}
	}
}
