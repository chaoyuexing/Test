package com.homework.teacher.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.homework.teacher.R;
import com.homework.teacher.utils.Utils;

/**
 * 批量下单
 * 
 * @author zhangkc
 * @date 2018-1-4
 */
public class BatchOrderHelperActivity extends Activity implements
		OnPageChangeListener {
	private final static String TAG = BatchOrderHelperActivity.class.getName();

	private ViewPager vp;
	private ViewPagerAdapter vpAdapter;
	private List<View> views;
	// 引导图片资源
	private int[] pics;
	private ImageView mCloseIv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_helper_guide);

		pics = new int[] { R.drawable.batch_intr_page_1,
				R.drawable.batch_intr_page_2, R.drawable.batch_intr_page_3,
				R.drawable.batch_intr_page_4, R.drawable.batch_intr_page_5,
				R.drawable.batch_intr_page_6 };
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
			views.add(iv);
		}
		vp = (ViewPager) findViewById(R.id.viewpager);
		// 初始化Adapter
		vpAdapter = new ViewPagerAdapter(views);
		vp.setAdapter(vpAdapter);
		// 绑定回调
		vp.setOnPageChangeListener(this);

		mCloseIv = (ImageView) findViewById(R.id.closeIv);
		mCloseIv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
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
