package com.homework.teacher.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.vdurmont.emoji.EmojiParser;
import com.homework.teacher.R;
import com.homework.teacher.data.ProductComment;

/**
 * @author zhangkc
 * @date 2017-6-26
 */
public class ProductCommentAdapter extends BaseAdapter {
	private final static String TAG = "ProductCommentAdapter";
	private Handler myHandler;
	private Context mContext;
	private List<ProductComment> mData;

	public ProductCommentAdapter(Context context, Handler handler,
			List<ProductComment> list) {
		this.mContext = context;
		this.myHandler = handler;
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
					R.layout.adapter_ordercomment_item, parent, false);
			holder = new ViewHolder();
			holder.init(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final ProductComment productComment = (ProductComment) getItem(position);
		if (productComment != null) {
			holder.mCommentNameTv.setText(productComment.getPrdName());
		}

		final Drawable praiseDrawable = mContext.getResources().getDrawable(
				R.drawable.comment_praise);
		praiseDrawable.setBounds(0, 0, praiseDrawable.getMinimumWidth(),
				praiseDrawable.getMinimumHeight());

		final Drawable praiseClickDrawable = mContext.getResources()
				.getDrawable(R.drawable.comment_praise_click);
		praiseClickDrawable.setBounds(0, 0,
				praiseClickDrawable.getMinimumWidth(),
				praiseClickDrawable.getMinimumHeight());
		if (productComment.getPrdEvaluationLv() == 3) {
			holder.mTv1.setCompoundDrawables(null, null, praiseClickDrawable,
					null);
			holder.mTv2.setCompoundDrawables(null, null, praiseClickDrawable,
					null);
			holder.mTv3.setCompoundDrawables(null, null, praiseClickDrawable,
					null);
		}
		holder.mTv1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				holder.mTv1.setCompoundDrawables(null, null,
						praiseClickDrawable, null);
				holder.mTv2.setCompoundDrawables(null, null, praiseDrawable,
						null);
				holder.mTv3.setCompoundDrawables(null, null, praiseDrawable,
						null);
				Message message = new Message();
				Bundle bundle = new Bundle();
				bundle.putInt("position", position);
				bundle.putInt("lv", 1);
				message.setData(bundle);
				myHandler.sendMessage(message);
			}
		});
		holder.mTv2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				holder.mTv1.setCompoundDrawables(null, null,
						praiseClickDrawable, null);
				holder.mTv2.setCompoundDrawables(null, null,
						praiseClickDrawable, null);
				holder.mTv3.setCompoundDrawables(null, null, praiseDrawable,
						null);
				Message message = new Message();
				Bundle bundle = new Bundle();
				bundle.putInt("position", position);
				bundle.putInt("lv", 2);
				message.setData(bundle);
				myHandler.sendMessage(message);
			}
		});
		holder.mTv3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				holder.mTv1.setCompoundDrawables(null, null,
						praiseClickDrawable, null);
				holder.mTv2.setCompoundDrawables(null, null,
						praiseClickDrawable, null);
				holder.mTv3.setCompoundDrawables(null, null,
						praiseClickDrawable, null);
				Message message = new Message();
				Bundle bundle = new Bundle();
				bundle.putInt("position", position);
				bundle.putInt("lv", 3);
				message.setData(bundle);
				myHandler.sendMessage(message);
			}
		});
		holder.mCommentEt.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				Message message = new Message();
				Bundle bundle = new Bundle();
				bundle.putInt("position", position);
				String string = s.toString();
				Log.e(TAG + "->s.toString()", string);
				String desc = EmojiParser.removeAllEmojis(string);
				Log.e(TAG + "->removeAllEmojis", desc);
				bundle.putString("desc", desc);
				message.setData(bundle);
				myHandler.sendMessage(message);
			}
		});

		return convertView;
	}

	class ViewHolder {
		private TextView mCommentNameTv, mTv1, mTv2, mTv3;
		private EditText mCommentEt;

		void init(View convertView) {
			mCommentNameTv = (TextView) convertView
					.findViewById(R.id.commentNameTv);
			mTv1 = (TextView) convertView.findViewById(R.id.tv1);
			mTv2 = (TextView) convertView.findViewById(R.id.tv2);
			mTv3 = (TextView) convertView.findViewById(R.id.tv3);
			mCommentEt = (EditText) convertView.findViewById(R.id.commentEt);
		}
	}
}
