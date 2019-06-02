package com.homework.teacher.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.homework.teacher.R;

/**
 * 关于作业教师版
 * 
 * @author zhangkc
 * @date 2017-11-13
 */

public class AboutVsichuActivity extends Activity {
	private final static String TAG = "AboutVsichuActivity";
	private RelativeLayout mVsichuIntroduceRl, mManagePhotoRl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_vsichu);

		((Button) findViewById(R.id.back))
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
		((TextView) findViewById(R.id.title)).setText(R.string.about_vsichu);
		((TextView) findViewById(R.id.tvSet)).setVisibility(View.GONE);

		mVsichuIntroduceRl = (RelativeLayout) findViewById(R.id.vsichuIntroduceRl);
		mVsichuIntroduceRl.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(AboutVsichuActivity.this,
						VsichuIntroduceActivity.class);
				startActivity(intent);
			}
		});
		mManagePhotoRl = (RelativeLayout) findViewById(R.id.managePhotoRl);
		mManagePhotoRl.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(AboutVsichuActivity.this,
						ManagePhotoActivity.class);
				startActivity(intent);
			}
		});
	}
}