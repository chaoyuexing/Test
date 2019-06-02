package com.homework.teacher.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.homework.teacher.R;
import com.homework.teacher.fragment.AccountPwLoginFragment;
import com.homework.teacher.fragment.VerifyCodeLoginFragment;

/**
 * 登录页面
 * 
 * @author zhangkc
 * 
 */
public class LoginActivity extends FragmentActivity implements
		View.OnClickListener {
	private static final String TAG = LoginActivity.class.getName();
	private TextView mVerifyCodeTv, mAccountPwTv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initUI();
	}

	private void initUI() {
		((Button) findViewById(R.id.back))
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
		((TextView) findViewById(R.id.tvSet)).setVisibility(View.GONE);
		mVerifyCodeTv = (TextView) findViewById(R.id.verifyCodeTv);
		mAccountPwTv = (TextView) findViewById(R.id.accountPwTv);
		mVerifyCodeTv.setOnClickListener(this);
		mAccountPwTv.setOnClickListener(this);

		changeFragment(new VerifyCodeLoginFragment());
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
		case R.id.verifyCodeTv:
			Log.e(TAG, "手机验证码登录页面-被点击");
			changeFragment(new VerifyCodeLoginFragment());
			mVerifyCodeTv.setBackgroundColor(getResources().getColor(
					R.color.white));
			mVerifyCodeTv.setTextColor(getResources().getColor(R.color.black));
			mAccountPwTv.setBackgroundColor(getResources().getColor(
					R.color.white_gray));
			mAccountPwTv.setTextColor(getResources().getColor(
					R.color.gray_normal));
			break;
		case R.id.accountPwTv:
			Log.e(TAG, "账号密码登录页面-被点击");
			changeFragment(new AccountPwLoginFragment());
			mVerifyCodeTv.setBackgroundColor(getResources().getColor(
					R.color.white_gray));
			mVerifyCodeTv.setTextColor(getResources().getColor(
					R.color.gray_normal));
			mAccountPwTv.setBackgroundColor(getResources().getColor(
					R.color.white));
			mAccountPwTv.setTextColor(getResources().getColor(R.color.black));
			break;
		default:
			break;
		}
	}
}
