package com.homework.teacher.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.homework.teacher.R;
import com.homework.teacher.data.ProductType;

/**
 * @author zhangkc
 */
public class ProductTypeListAdapter extends BaseAdapter {

	private final static String TAG = "ProductTypeListAdapter";
	private List<ProductType> mData;
	private Context mContext;
	private int itemId;// 点击item的位置id
	private ImageLoader mImageLoader;

	public ProductTypeListAdapter(Context context, int itemId,
			ImageLoader imageLoader, List<ProductType> list) {
		this.mContext = context;
		this.itemId = itemId;
		this.mImageLoader = imageLoader;
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
						R.layout.adapter_product_type_item_onclick, parent,
						false);
			} else {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.adapter_product_type_item, parent, false);
			}
			holder = new ViewHolder();
			holder.init(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final ProductType pt = (ProductType) getItem(position);
		if (pt != null) {
			if (position == itemId) {
				mImageLoader.displayImage(pt.getHlIconURL(),
						holder.productTypeIconIv);
			} else {
				mImageLoader.displayImage(pt.getIconURL(),
						holder.productTypeIconIv);
			}
			holder.productTypeNameTv.setText(pt.getName());

		}
		return convertView;
	}

	class ViewHolder {
		private ImageView productTypeIconIv;
		private TextView productTypeNameTv;

		void init(View convertView) {
			productTypeIconIv = (ImageView) convertView
					.findViewById(R.id.productTypeIconIv);
			productTypeNameTv = (TextView) convertView
					.findViewById(R.id.productTypeNameTv);
		}
	}
}