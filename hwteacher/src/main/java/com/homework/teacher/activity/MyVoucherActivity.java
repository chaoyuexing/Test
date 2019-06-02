package com.homework.teacher.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.homework.teacher.R;
import com.homework.teacher.fragment.VoucherAlreadyUsedFragment;
import com.homework.teacher.fragment.VoucherNotUsedFragment;

/**
 * 我的代金券
 * 
 * @author zhangkc
 * @date 2017-7-31
 */
public class MyVoucherActivity extends BaseFragmentActivity implements
		OnClickListener {
	private final static String TAG = "MyVoucherActivity";
	private TextView mNotusedTv, mAlreadyusedTv;
	private View mNotusedView, mAlreadyusedView, mVoucherStateTv;
	private RelativeLayout mTab1, mTab2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myvoucher);

		((Button) findViewById(R.id.back))
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
		((TextView) findViewById(R.id.title)).setText(R.string.my_voucher);
		((TextView) findViewById(R.id.tvSet)).setVisibility(View.GONE);

		initUI();
	}

	private void initUI() {
		// tab文字
		mNotusedTv = (TextView) findViewById(R.id.notusedTv);
		mAlreadyusedTv = (TextView) findViewById(R.id.alreadyusedTv);
		// tab底边线
		mNotusedView = findViewById(R.id.notusedView);
		mAlreadyusedView = findViewById(R.id.alreadyusedView);
		// tab整体
		mTab1 = (RelativeLayout) findViewById(R.id.tab_notused);
		mTab2 = (RelativeLayout) findViewById(R.id.tab_alreadyused);
		mTab1.setOnClickListener(this);
		mTab2.setOnClickListener(this);

		mVoucherStateTv = (TextView) findViewById(R.id.voucherStateTv);
		mVoucherStateTv.setOnClickListener(this);

		changeFragment(new VoucherNotUsedFragment());
	}

	private void changeFragment(Fragment fm) {
		FragmentManager supportFragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = supportFragmentManager
				.beginTransaction();
		transaction.replace(R.id.content, fm);
		transaction.commit();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tab_notused:
			Log.e(TAG, "未使用tab被点击");
			mNotusedTv.setTextColor(getResources()
					.getColor(R.color.theme_color));
			mNotusedView.setVisibility(View.VISIBLE);
			mAlreadyusedTv.setTextColor(getResources().getColor(R.color.black));
			mAlreadyusedView.setVisibility(View.GONE);
			changeFragment(new VoucherNotUsedFragment());
			break;
		case R.id.tab_alreadyused:
			Log.e(TAG, "已使用tab被点击");
			mNotusedTv.setTextColor(getResources().getColor(R.color.black));
			mNotusedView.setVisibility(View.GONE);
			mAlreadyusedTv.setTextColor(getResources().getColor(
					R.color.theme_color));
			mAlreadyusedView.setVisibility(View.VISIBLE);
			changeFragment(new VoucherAlreadyUsedFragment());
			break;
		case R.id.voucherStateTv:
			Intent intent = new Intent();
			intent.setClass(MyVoucherActivity.this, AboutVoucherActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
}
