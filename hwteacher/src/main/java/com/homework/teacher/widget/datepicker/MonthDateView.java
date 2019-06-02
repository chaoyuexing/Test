package com.homework.teacher.widget.datepicker;

import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

// http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/0930/3538.html
public class MonthDateView extends View {
	private static final int NUM_COLUMNS = 7;
	private static final int NUM_ROWS = 6;
	private Paint mPaint;

	private int mPastDayColor = Color.parseColor("#bfbfbf");// 今天之前的日期（灰色）
	private int mHoliDayColor = Color.parseColor("#FF0000");// 节假日（红色）
	private int mDayColor = Color.parseColor("#000000");// 非节假日（黑色）
	// private int mCurrentDayColor = Color.parseColor("#f7b940");// 今天（橙色）
	private int mCurrentDayColor = Color.parseColor("#bfbfbf");// 今天（灰色）
	private int mSelectBGColor = Color.parseColor("#ff00ff00");// 选中（背景绿色）
	private int mSelectDayColor = Color.parseColor("#ffffff");
	private int mCurrYear, mCurrMonth, mCurrDay;
	private int mSelYear, mSelMonth, mSelDay;
	private int mColumnSize, mRowSize;
	private DisplayMetrics mDisplayMetrics;
	private int mDaySize = 18;
	private TextView tv_date;
	private int[][] daysString;
	private int mCircleRadius = 6;
	private DateClick dateClick;
	private int mCircleColor = Color.parseColor("#ff0000");
	private List<Integer> daysHasThingList;
	public int holiday[] = new int[31];

	public MonthDateView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mDisplayMetrics = getResources().getDisplayMetrics();
		Calendar calendar = Calendar.getInstance();
		mPaint = new Paint();
		mCurrYear = calendar.get(Calendar.YEAR);
		mCurrMonth = calendar.get(Calendar.MONTH);
		mCurrDay = calendar.get(Calendar.DATE);
		setSelectYearMonth(mCurrYear, mCurrMonth, mCurrDay);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		initSize();
		daysString = new int[6][7];
		mPaint.setTextSize(mDaySize * mDisplayMetrics.scaledDensity);
		String dayString;
		int mMonthDays = DateUtils.getMonthDays(mSelYear, mSelMonth);
		int weekNumber = DateUtils.getFirstDayWeek(mSelYear, mSelMonth) - 1;
		Log.e("DateView", "DateView:" + mSelYear + "年" + mSelMonth + "月1号周"
				+ weekNumber);
		for (int day = 0; day < mMonthDays; day++) {
			dayString = (day + 1) + "";
			int column = 0;
			if (weekNumber == 0) {
				column = (day + weekNumber - 1 + 7) % 7;
			} else {
				column = (day + weekNumber - 1) % 7;
			}
			int row = 0;
			if (weekNumber == 0) {
				if ((day + weekNumber - 1) < 0) {
					row = (day + weekNumber - 1 + 7) / 7;
				} else {
					row = (day + weekNumber - 1) / 7 + 1;
				}
			} else {
				row = (day + weekNumber - 1) / 7;
			}

			daysString[row][column] = day + 1;
			int startX = (int) (mColumnSize * column + (mColumnSize - mPaint
					.measureText(dayString)) / 2);
			int startY = (int) (mRowSize * row + mRowSize / 2 - (mPaint
					.ascent() + mPaint.descent()) / 2);
			// 1.mCurrYear系统年份 ；2.mSelYear日历年份；3.mCurrMonth系统月份；4.mSelMonth日历月份。
			// 1.今天之前的日期（灰色）；2.节假日（红色）；3.非节假日（黑色）；4.今天（橙色）5.选中（背景绿色）。
			if (mCurrYear >= mSelYear && mCurrMonth >= mSelMonth
//					&& mCurrDay > Integer.parseInt(dayString)) {
					&& mCurrDay >= Integer.parseInt(dayString)) {
				mPaint.setColor(mPastDayColor);
			} else {
//				if (Integer.parseInt(dayString) == mSelDay) {
				if (Integer.parseInt(dayString) == mSelDay + 1) {
					// 绘制背景色圆形
					mPaint.setColor(mSelectBGColor);
					canvas.drawCircle((float) (mColumnSize * (column + 0.5)),
							(float) (mRowSize * (row + 0.5)),
							(float) (mRowSize * 0.5), mPaint);
				}
				mPaint.setColor(mDayColor);
				for (int i = 0; i < holiday.length; i++) {
					if (holiday[i] == Integer.parseInt(dayString)) {
						mPaint.setColor(mHoliDayColor);
					}
				}
				if (mCurrYear == mSelYear && mCurrMonth == mSelMonth
						&& mCurrDay == Integer.parseInt(dayString)) {
					mPaint.setColor(mCurrentDayColor);
				}
			}
			canvas.drawText(dayString, startX, startY, mPaint);
			if (tv_date != null) {
				String yearMonth = mSelYear + "-" + (mSelMonth + 1);
				tv_date.setText(yearMonth);
			}
		}
	}

	public void setHoliday(int holiday[]) {
		this.holiday = holiday;
	}

	@Override
	public boolean performClick() {
		return super.performClick();
	}

	private int downX = 0, downY = 0;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int eventCode = event.getAction();
		switch (eventCode) {
		case MotionEvent.ACTION_DOWN:
			downX = (int) event.getX();
			downY = (int) event.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		case MotionEvent.ACTION_UP:
			int upX = (int) event.getX();
			int upY = (int) event.getY();
			if (Math.abs(upX - downX) < 10 && Math.abs(upY - downY) < 10) {// 点击事件
				performClick();
				doClickAction((upX + downX) / 2, (upY + downY) / 2);
			}
			break;
		}
		return true;
	}

	/**
	 * 初始化列宽行高
	 */
	private void initSize() {
		mColumnSize = getWidth() / NUM_COLUMNS;
		mRowSize = getHeight() / NUM_ROWS;
	}

	/**
	 * 设置年月
	 * 
	 * @param year
	 * @param month
	 */
	private void setSelectYearMonth(int year, int month, int day) {
		mSelYear = year;
		mSelMonth = month;
		mSelDay = day;
	}

	/**
	 * 执行点击事件
	 * 
	 * @param x
	 * @param y
	 */
	private void doClickAction(int x, int y) {
		int row = y / mRowSize;
		int column = x / mColumnSize;
		setSelectYearMonth(mSelYear, mSelMonth, daysString[row][column]);
		invalidate();
		// 执行activity发送过来的点击处理事件
		if (dateClick != null) {
			dateClick.onClickOnDate();
		}
	}

	/**
	 * 左点击，日历向后翻页
	 */
	public void onLeftClick() {
		int year = mSelYear;
		int month = mSelMonth;
		int day = mSelDay;
		if (month == 0) {// 若果是1月份，则变成12月份
			year = mSelYear - 1;
			month = 11;
		} else if (DateUtils.getMonthDays(year, month) == day) {
			// 如果当前日期为该月最后一点，当向前推的时候，就需要改变选中的日期
			month = month - 1;
			day = DateUtils.getMonthDays(year, month);
		} else {
			month = month - 1;
		}
		setSelectYearMonth(year, month, day);
		invalidate();
	}

	/**
	 * 右点击，日历向前翻页
	 */
	public void onRightClick() {
		int year = mSelYear;
		int month = mSelMonth;
		int day = mSelDay;
		if (month == 11) {// 若果是12月份，则变成1月份
			year = mSelYear + 1;
			month = 0;
		} else if (DateUtils.getMonthDays(year, month) == day) {
			// 如果当前日期为该月最后一点，当向前推的时候，就需要改变选中的日期
			month = month + 1;
			day = DateUtils.getMonthDays(year, month);
		} else {
			month = month + 1;
		}
		setSelectYearMonth(year, month, day);
		invalidate();
	}

	/**
	 * 获取选择的年份
	 * 
	 * @return
	 */
	public int getmSelYear() {
		return mSelYear;
	}

	/**
	 * 获取选择的月份
	 * 
	 * @return
	 */
	public int getmSelMonth() {
		return mSelMonth;
	}

	/**
	 * 获取选择的日期
	 * 
	 * @param mSelDay
	 */
	public int getmSelDay() {
		return this.mSelDay;
	}

	/**
	 * 普通日期的字体颜色，默认黑色
	 * 
	 * @param mDayColor
	 */
	public void setmDayColor(int mDayColor) {
		this.mDayColor = mDayColor;
	}

	/**
	 * 选择日期的颜色，默认为白色
	 * 
	 * @param mSelectDayColor
	 */
	public void setmSelectDayColor(int mSelectDayColor) {
		this.mSelectDayColor = mSelectDayColor;
	}

	/**
	 * 选中日期的背景颜色，默认绿色
	 * 
	 * @param mSelectBGColor
	 */
	public void setmSelectBGColor(int mSelectBGColor) {
		this.mSelectBGColor = mSelectBGColor;
	}

	/**
	 * 当前日期不是选中的颜色，默认橙色
	 * 
	 * @param mCurrentColor
	 */
	public void setmCurrentColor(int mCurrentDayColor) {
		this.mCurrentDayColor = mCurrentDayColor;
	}

	/**
	 * 日期的大小，默认18sp
	 * 
	 * @param mDaySize
	 */
	public void setmDaySize(int mDaySize) {
		this.mDaySize = mDaySize;
	}

	/**
	 * 设置显示当前日期的控件
	 * 
	 * @param tv_date
	 *            显示日期
	 */
	public void setTextView(TextView tv_date) {
		this.tv_date = tv_date;
		invalidate();
	}

	/**
	 * 设置事务天数
	 * 
	 * @param daysHasThingList
	 */
	public void setDaysHasThingList(List<Integer> daysHasThingList) {
		this.daysHasThingList = daysHasThingList;
	}

	/***
	 * 设置圆圈的半径，默认为6
	 * 
	 * @param mCircleRadius
	 */
	public void setmCircleRadius(int mCircleRadius) {
		this.mCircleRadius = mCircleRadius;
	}

	/**
	 * 设置圆圈的半径
	 * 
	 * @param mCircleColor
	 */
	public void setmCircleColor(int mCircleColor) {
		this.mCircleColor = mCircleColor;
	}

	/**
	 * 设置日期的点击回调事件
	 * 
	 * @author shiwei.deng
	 * 
	 */
	public interface DateClick {
		public void onClickOnDate();
	}

	/**
	 * 设置日期点击事件
	 * 
	 * @param dateClick
	 */
	public void setDateClick(DateClick dateClick) {
		this.dateClick = dateClick;
	}

	/**
	 * 跳转至今天
	 */
	public void setTodayToView() {
		setSelectYearMonth(mCurrYear, mCurrMonth, mCurrDay);
		invalidate();
	}
}
