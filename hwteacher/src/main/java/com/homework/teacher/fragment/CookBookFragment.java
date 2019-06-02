package com.homework.teacher.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.homework.teacher.R;
import com.homework.teacher.activity.LoginActivity;
import com.homework.teacher.app.BaseApplication;

/**
 * 一周食谱
 * 
 * @author zhangkc
 * 
 */
public class CookBookFragment extends Fragment implements OnClickListener {
	private final static String TAG = "CookBookFragment";
	private View view;
	private TextView mRecommendTv, mAlreadychooseTv, mWeekrecipeTv,
			mMealplanTv, mBatchorderTv;
	private View mRecommendView, mAlreadychooseView, mWeekrecipeView,
			mMealplanView, mBatchorderView;
	private RelativeLayout mTab0, mTab1, mTab2, mTab3, mTab4;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.cookbook_fragment, container, false);

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initUI();
	}

	private void initUI() {
		// tab文字
		mRecommendTv = (TextView) view.findViewById(R.id.recommendTv);
		mAlreadychooseTv = (TextView) view.findViewById(R.id.alreadychooseTv);
		mWeekrecipeTv = (TextView) view.findViewById(R.id.weekrecipeTv);
		mMealplanTv = (TextView) view.findViewById(R.id.mealplanTv);
		mBatchorderTv = (TextView) view.findViewById(R.id.batchorderTv);
		// tab底边线
		mRecommendView = view.findViewById(R.id.recommendView);
		mAlreadychooseView = view.findViewById(R.id.alreadychooseView);
		mWeekrecipeView = view.findViewById(R.id.weekrecipeView);
		mMealplanView = view.findViewById(R.id.mealplanView);
		mBatchorderView = view.findViewById(R.id.batchorderView);
		// tab整体
		mTab0 = (RelativeLayout) view.findViewById(R.id.tab_recommend);
		mTab1 = (RelativeLayout) view.findViewById(R.id.tab_alreadychoose);
		mTab2 = (RelativeLayout) view.findViewById(R.id.tab_weekrecipe);
		mTab3 = (RelativeLayout) view.findViewById(R.id.tab_mealplan);
		mTab4 = (RelativeLayout) view.findViewById(R.id.tab_batchorder);
		mTab0.setOnClickListener(this);
		mTab1.setOnClickListener(this);
		mTab2.setOnClickListener(this);
		mTab3.setOnClickListener(this);
		mTab4.setOnClickListener(this);

		changeFragment(new RecommendFragment());
	}

	private void changeFragment(Fragment fm) {
		FragmentManager supportFragmentManager = getChildFragmentManager();
		FragmentTransaction transaction = supportFragmentManager
				.beginTransaction();
		transaction.replace(R.id.content, fm);
		transaction.commit();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tab_recommend:
			Log.e(TAG, "推荐食谱tab被点击");
			mRecommendTv.setTextColor(getResources().getColor(
					R.color.theme_color));
			mRecommendView.setVisibility(View.VISIBLE);
			mAlreadychooseTv.setTextColor(getResources()
					.getColor(R.color.black));
			mAlreadychooseView.setVisibility(View.GONE);
			mWeekrecipeTv.setTextColor(getResources().getColor(R.color.black));
			mWeekrecipeView.setVisibility(View.GONE);
			mMealplanTv.setTextColor(getResources().getColor(R.color.black));
			mMealplanView.setVisibility(View.GONE);
			mBatchorderTv.setTextColor(getResources().getColor(R.color.black));
			mBatchorderView.setVisibility(View.GONE);
			changeFragment(new RecommendFragment());
			break;
		case R.id.tab_alreadychoose:
			Log.e(TAG, "已选菜品tab被点击");
			mRecommendTv.setTextColor(getResources().getColor(R.color.black));
			mRecommendView.setVisibility(View.GONE);
			mAlreadychooseTv.setTextColor(getResources().getColor(
					R.color.theme_color));
			mAlreadychooseView.setVisibility(View.VISIBLE);
			mWeekrecipeTv.setTextColor(getResources().getColor(R.color.black));
			mWeekrecipeView.setVisibility(View.GONE);
			mMealplanTv.setTextColor(getResources().getColor(R.color.black));
			mMealplanView.setVisibility(View.GONE);
			mBatchorderTv.setTextColor(getResources().getColor(R.color.black));
			mBatchorderView.setVisibility(View.GONE);
			Log.i(TAG, "客户(用户)ID： " + BaseApplication.getInstance().getCstID());
			if (BaseApplication.getInstance().getCstID() != 0) {
				changeFragment(new AlreadyChooseFragment());
			} else {
				Intent intent = new Intent();
				intent.setClass(getActivity(), LoginActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.tab_weekrecipe:
			Log.e(TAG, "我的食谱tab被点击");
			mRecommendTv.setTextColor(getResources().getColor(R.color.black));
			mRecommendView.setVisibility(View.GONE);
			mAlreadychooseTv.setTextColor(getResources()
					.getColor(R.color.black));
			mAlreadychooseView.setVisibility(View.GONE);
			mWeekrecipeTv.setTextColor(getResources().getColor(
					R.color.theme_color));
			mWeekrecipeView.setVisibility(View.VISIBLE);
			mMealplanTv.setTextColor(getResources().getColor(R.color.black));
			mMealplanView.setVisibility(View.GONE);
			mBatchorderTv.setTextColor(getResources().getColor(R.color.black));
			mBatchorderView.setVisibility(View.GONE);
			Log.i(TAG, "客户(用户)ID： " + BaseApplication.getInstance().getCstID());
			if (BaseApplication.getInstance().getCstID() != 0) {
				changeFragment(new WeekRecipeFragment());
			} else {
				Intent intent = new Intent();
				intent.setClass(getActivity(), LoginActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.tab_mealplan:
			Log.e(TAG, "用餐计划tab被点击");
			mRecommendTv.setTextColor(getResources().getColor(R.color.black));
			mRecommendView.setVisibility(View.GONE);
			mAlreadychooseTv.setTextColor(getResources()
					.getColor(R.color.black));
			mAlreadychooseView.setVisibility(View.GONE);
			mWeekrecipeTv.setTextColor(getResources().getColor(R.color.black));
			mWeekrecipeView.setVisibility(View.GONE);
			mMealplanTv.setTextColor(getResources().getColor(
					R.color.theme_color));
			mMealplanView.setVisibility(View.VISIBLE);
			mBatchorderTv.setTextColor(getResources().getColor(R.color.black));
			mBatchorderView.setVisibility(View.GONE);
			Log.i(TAG, "客户(用户)ID： " + BaseApplication.getInstance().getCstID());
			if (BaseApplication.getInstance().getCstID() != 0) {
				changeFragment(new MealPlanFragment());
			} else {
				Intent intent = new Intent();
				intent.setClass(getActivity(), LoginActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.tab_batchorder:
			Log.e(TAG, "批量下单tab被点击");
			mRecommendTv.setTextColor(getResources().getColor(R.color.black));
			mRecommendView.setVisibility(View.GONE);
			mAlreadychooseTv.setTextColor(getResources()
					.getColor(R.color.black));
			mAlreadychooseView.setVisibility(View.GONE);
			mWeekrecipeTv.setTextColor(getResources().getColor(R.color.black));
			mWeekrecipeView.setVisibility(View.GONE);
			mMealplanTv.setTextColor(getResources().getColor(R.color.black));
			mMealplanView.setVisibility(View.GONE);
			mBatchorderTv.setTextColor(getResources().getColor(
					R.color.theme_color));
			mBatchorderView.setVisibility(View.VISIBLE);
			Log.i(TAG, "客户(用户)ID： " + BaseApplication.getInstance().getCstID());
			if (BaseApplication.getInstance().getCstID() != 0) {
				changeFragment(new BatchOrderFragment());
			} else {
				Intent intent = new Intent();
				intent.setClass(getActivity(), LoginActivity.class);
				startActivity(intent);
			}
			break;
		default:
			break;
		}
	}
}