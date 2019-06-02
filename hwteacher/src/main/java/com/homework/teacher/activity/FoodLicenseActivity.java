package com.homework.teacher.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.homework.teacher.Consts;
import com.homework.teacher.R;
import com.homework.teacher.app.BaseApplication;

/**
 * 食品经营许可证
 * 
 * @author zhangkc
 * @date 2017-11-21
 */

public class FoodLicenseActivity extends Activity {
	private final static String TAG = "FoodLicenseActivity";
	private ImageView mFoodLicenseIv;
	private ImageLoader mImageLoader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_food_license);

		((Button) findViewById(R.id.back))
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
		((TextView) findViewById(R.id.title)).setText("食品经营许可证");
		((TextView) findViewById(R.id.tvSet)).setVisibility(View.GONE);

		mFoodLicenseIv = (ImageView) findViewById(R.id.foodLicenseIv);

		mImageLoader = BaseApplication.getInstance().imageLoader;
		mImageLoader.displayImage(Consts.IMAGE_FOODLICENSE_URL, mFoodLicenseIv);
	}

}
