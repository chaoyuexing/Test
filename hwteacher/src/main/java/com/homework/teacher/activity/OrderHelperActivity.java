package com.homework.teacher.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.homework.teacher.R;

/**
 * 订餐帮助
 * 
 * @author zhangkc
 * @date 2017-12-25
 */

public class OrderHelperActivity extends Activity {
	private final static String TAG = "OrderHelperActivity";
	private TextView mOrderHelperTv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_helper);

		((Button) findViewById(R.id.back))
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
		((TextView) findViewById(R.id.title)).setText(R.string.order_helper);
		((TextView) findViewById(R.id.tvSet)).setVisibility(View.GONE);

		mOrderHelperTv = (TextView) findViewById(R.id.orderHelperTv);
		SpannableStringBuilder spannable = new SpannableStringBuilder(
				getResources().getString(R.string.order_helper_des));

		// 设置单个下单 颜色、点击事件
		spannable.setSpan(new ForegroundColorSpan(Color.BLUE), 57, 63,
				Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		spannable.setSpan(new ClickableSpan() {
			@Override
			public void onClick(View widget) {
				Intent intent = new Intent();
				intent.setClass(OrderHelperActivity.this,
						SingleOrderHelperActivity.class);
				startActivity(intent);
			}

			@Override
			public void updateDrawState(TextPaint ds) {
				ds.setUnderlineText(false);
			}
		}, 57, 63, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

		// 设置批量下单 颜色、点击事件
		spannable.setSpan(new ForegroundColorSpan(Color.BLUE), 98, 104,
				Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		spannable.setSpan(new ClickableSpan() {
			@Override
			public void onClick(View widget) {
				Intent intent = new Intent();
				intent.setClass(OrderHelperActivity.this,
						BatchOrderHelperActivity.class);
				startActivity(intent);
			}

			@Override
			public void updateDrawState(TextPaint ds) {
				ds.setUnderlineText(false);
			}
		}, 98, 104, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

		spannable.setSpan(new ForegroundColorSpan(Color.RED), 191, 196,
				Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		spannable.setSpan(new ForegroundColorSpan(Color.RED), 269, 272,
				Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		mOrderHelperTv.setText(spannable);
		mOrderHelperTv.setMovementMethod(LinkMovementMethod.getInstance());
	}

}