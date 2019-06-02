package com.homework.teacher.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.homework.teacher.R;
import com.homework.teacher.app.BaseApplication;
import com.homework.teacher.utils.Utils;

/**
 * @author zhangkc
 * @date 2017-11-15
 */
public class UserGuideActivity extends Activity implements OnClickListener,
		OnPageChangeListener {
	private final static String TAG = UserGuideActivity.class.getName();
	public final static String FROM = "from";
	private final static String FIRSTINSTALL = "firstInstall";
	private final static int firstInstall = 0;
	private int type;

	private ViewPager vp;
	private ViewPagerAdapter vpAdapter;
	private List<View> views;
	// 引导图片资源
	private int[] pics;
	private SharedPreferences sp;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_guide);

		sp = BaseApplication.getInstance().getSp();
		type = getIntent().getIntExtra(FROM, 0);
		if (type == firstInstall) {
			sp.edit().putInt(FIRSTINSTALL, 1).commit();
			pics = new int[] { R.drawable.intr_page_1, R.drawable.intr_page_2,
					R.drawable.intr_page_3 };
		}
		views = new ArrayList<View>();
		int width = Utils.getWindowWidth(this);
		int height = Utils.getWindowHeight(this);
		LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(
				width, height);

		// 初始化引导图片列表
		for (int i = 0; i < pics.length; i++) {
			ImageView iv = new ImageView(this);
			iv.setLayoutParams(mParams);
			iv.setBackgroundResource(pics[i]);
			iv.setTag(i);
			iv.setOnClickListener(this);
			views.add(iv);
		}
		vp = (ViewPager) findViewById(R.id.viewpager);
		// 初始化Adapter
		vpAdapter = new ViewPagerAdapter(views);
		vp.setAdapter(vpAdapter);
		// 绑定回调
		vp.setOnPageChangeListener(this);
	}

	// 当滑动状态改变时调用
	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	// 当当前页面被滑动时调用
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	// 当新的页面被选中时调用
	@Override
	public void onPageSelected(int arg0) {

	}

	@Override
	public void onClick(View v) {
		int position = (Integer) v.getTag();
		if (position == pics.length - 1) {
			Intent i = new Intent(UserGuideActivity.this, MainActivity.class);
			startActivity(i);
			finish();
		}
	}

	class ViewPagerAdapter extends PagerAdapter {
		// 界面列表
		private List<View> views;

		public ViewPagerAdapter(List<View> views) {
			this.views = views;
		}

		// 销毁arg1位置的界面
		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(views.get(arg1));
		}

		@Override
		public void finishUpdate(View arg0) {

		}

		// 获得当前界面数
		@Override
		public int getCount() {
			if (views != null) {
				return views.size();
			}
			return 0;
		}

		// 初始化arg1位置的界面
		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(views.get(arg1), 0);
			return views.get(arg1);
		}

		// 判断是否由对象生成界面
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return (arg0 == arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {

		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {

		}
	}
}