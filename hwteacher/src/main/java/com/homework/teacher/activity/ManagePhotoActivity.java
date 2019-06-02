package com.homework.teacher.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.homework.teacher.Consts;
import com.homework.teacher.R;
import com.homework.teacher.app.BaseApplication;

/**
 * 经营证照
 * 
 * @author zhangkc
 * @date 2017-11-20
 */

public class ManagePhotoActivity extends Activity {
	private final static String TAG = "ManagePhotoActivity";
	private ImageView mBusiLicenseIv, mFoodLicenseIv;
	private LinearLayout mBusiLicenseLy, mFoodLicenseLy;
	private ImageLoader mImageLoader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_photo);

		((Button) findViewById(R.id.back))
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
		((TextView) findViewById(R.id.title)).setText("经营证照");
		((TextView) findViewById(R.id.tvSet)).setVisibility(View.GONE);

		mBusiLicenseIv = (ImageView) findViewById(R.id.busiLicenseIv);
		mFoodLicenseIv = (ImageView) findViewById(R.id.foodLicenseIv);

		mImageLoader = BaseApplication.getInstance().imageLoader;
		mImageLoader.displayImage(Consts.IMAGE_BUSILICENSE_URL, mBusiLicenseIv);
		mImageLoader.displayImage(Consts.IMAGE_FOODLICENSE_URL, mFoodLicenseIv);

		mBusiLicenseLy = (LinearLayout) findViewById(R.id.busiLicenseLy);
		mFoodLicenseLy = (LinearLayout) findViewById(R.id.foodLicenseLy);
		mBusiLicenseLy.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(ManagePhotoActivity.this,
						BusiLicenseActivity.class);
				startActivity(intent);
			}
		});
		mFoodLicenseLy.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(ManagePhotoActivity.this,
						FoodLicenseActivity.class);
				startActivity(intent);
			}
		});
	}

}
