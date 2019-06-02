package com.homework.teacher.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.homework.teacher.R;
import com.homework.teacher.data.ProductDetailComment;

/**
 * 产品详情页-点评列表adapter
 * 
 * @author zhangkc
 * @date 2017-7-17
 */
public class ProductCommentListAdapter extends BaseAdapter {
	private final static String TAG = "ProductCommentListAdapter";
	private Context mContext;
	private List<ProductDetailComment> mData;

	public ProductCommentListAdapter(Context context,
			List<ProductDetailComment> list) {
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
					R.layout.adapter_productcomment_item, parent, false);
			holder = new ViewHolder();
			holder.init(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final Drawable clickDrawable = mContext.getResources().getDrawable(
				R.drawable.comment_praise_click);
		clickDrawable.setBounds(0, 0, clickDrawable.getMinimumWidth(),
				clickDrawable.getMinimumHeight());
		final Drawable unClickDrawable = mContext.getResources().getDrawable(
				R.drawable.comment_praise);
		unClickDrawable.setBounds(0, 0, unClickDrawable.getMinimumWidth(),
				unClickDrawable.getMinimumHeight());
		final ProductDetailComment productDetailComment = (ProductDetailComment) getItem(position);
		if (productDetailComment != null) {
			holder.mNickNameTv.setText(productDetailComment.getNickName());
			holder.mEvaDescTv.setText(productDetailComment.getEvaDesc());
			int evaLv = productDetailComment.getEvaLv();
			if (evaLv == 1) {// 1：差评
				holder.mTv1.setCompoundDrawables(clickDrawable, null, null,
						null);
				holder.mTv2.setCompoundDrawables(unClickDrawable, null, null,
						null);
				holder.mTv3.setCompoundDrawables(unClickDrawable, null, null,
						null);
			} else if (evaLv == 2) {// 2：中评
				holder.mTv1.setCompoundDrawables(clickDrawable, null, null,
						null);
				holder.mTv2.setCompoundDrawables(clickDrawable, null, null,
						null);
				holder.mTv3.setCompoundDrawables(unClickDrawable, null, null,
						null);
			} else if (evaLv == 3) {// 3：好评
				holder.mTv1.setCompoundDrawables(clickDrawable, null, null,
						null);
				holder.mTv2.setCompoundDrawables(clickDrawable, null, null,
						null);
				holder.mTv3.setCompoundDrawables(clickDrawable, null, null,
						null);
			}
		}

		return convertView;
	}

	class ViewHolder {
		private TextView mTv1, mTv2, mTv3, mNickNameTv, mEvaDescTv;

		void init(View convertView) {
			mTv1 = (TextView) convertView.findViewById(R.id.tv1);
			mTv2 = (TextView) convertView.findViewById(R.id.tv2);
			mTv3 = (TextView) convertView.findViewById(R.id.tv3);
			mNickNameTv = (TextView) convertView.findViewById(R.id.nickNameTv);
			mEvaDescTv = (TextView) convertView.findViewById(R.id.evaDescTv);
		}
	}
}
