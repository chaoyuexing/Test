package com.homework.teacher.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.homework.teacher.R;

/**
 * 作业教师版简介
 * 
 * @author zhangkc
 * @date 2017-11-13
 */

public class VsichuIntroduceActivity extends Activity {
	private final static String TAG = "VsichuIntroduceActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vsichu_introduce);

		((Button) findViewById(R.id.back))
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
		((TextView) findViewById(R.id.title)).setText("作业教师版简介");
		((TextView) findViewById(R.id.tvSet)).setVisibility(View.GONE);
	}

}
