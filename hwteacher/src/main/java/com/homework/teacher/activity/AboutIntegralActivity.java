package com.homework.teacher.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.homework.teacher.R;

/**
 * 积分说明
 * 
 * @author zhangkc
 * @date 2017-11-12
 */

public class AboutIntegralActivity extends Activity {
	private final static String TAG = "AboutIntegralActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_integral);

		((Button) findViewById(R.id.back))
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
		((TextView) findViewById(R.id.title)).setText(R.string.about_integral);
		((TextView) findViewById(R.id.tvSet)).setVisibility(View.GONE);
	}

}
