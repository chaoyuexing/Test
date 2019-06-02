package com.homework.teacher.activity;

import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.homework.teacher.Consts;
import com.homework.teacher.R;
import com.homework.teacher.app.BaseApplication;
import com.homework.teacher.data.Remind;
import com.homework.teacher.http.WDStringRequest;
import com.homework.teacher.utils.DateUtils;
import com.homework.teacher.utils.StatusUtils;
import com.homework.teacher.widget.datepicker.MonthDateView;
import com.homework.teacher.widget.datepicker.MonthDateView.DateClick;

/**
 * 订餐日期选择页
 * 
 * @author zhangkc
 * 
 */
public class SelectDatePopupWindow extends Activity implements OnClickListener {
	private final static String TAG = SelectDatePopupWindow.class.getName();
	private LinearLayout layout;
	private TextView mTipsTv;
	private MonthDateView monthDateView;
	private TextView tv_date;
	private ImageView iv_left, iv_right;
	int holiday[] = new int[31];
	@SuppressLint("HandlerLeak")
	private Handler myHandler = new Handler() {
		// 接收到消息后处理
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				monthDateView.setHoliday(holiday); // 调用方法
				monthDateView.invalidate();// 刷新方法
				break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selectdate);

		layout = (LinearLayout) findViewById(R.id.pop_layout);
		// 添加弹出窗口范围监听可以优先获取触点，即不再执行onTouchEvent()函数，点击其他地方时执行onTouchEvent()函数销毁Activity
		layout.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

			}
		});
		mTipsTv = (TextView) findViewById(R.id.tipsTv);
		monthDateView = (MonthDateView) findViewById(R.id.monthDateView);
		tv_date = (TextView) findViewById(R.id.date_text);
		monthDateView.setTextView(tv_date);
		monthDateView.setDateClick(new DateClick() {
			@Override
			public void onClickOnDate() {
				String date = DateUtils.getDate();
				String[] array = new String[3];
				array = date.split("-");
				int mCurrYear = Integer.parseInt(array[0]);
				int mCurrMonth = Integer.parseInt(array[1]);
				int mCurrDay = Integer.parseInt(array[2]);
				if (mCurrYear >= monthDateView.getmSelYear()
						&& mCurrMonth >= (monthDateView.getmSelMonth() + 1)
						&& mCurrDay >= monthDateView.getmSelDay()) {
				} else {
					// 只对今天以后的日期做点击处理
					Intent intent = new Intent();
					Bundle bundle = new Bundle();
					bundle.putInt("year", monthDateView.getmSelYear());
					bundle.putInt("month", monthDateView.getmSelMonth() + 1);
					bundle.putInt("day", monthDateView.getmSelDay());
					intent.putExtras(bundle);
					setResult(RESULT_OK, intent);
					finish();
				}
			}
		});
		iv_left = (ImageView) findViewById(R.id.iv_left);
		iv_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				monthDateView.onLeftClick();
				// 获取节假日
				String yearMonth;
				int year = monthDateView.getmSelYear();
				int month = monthDateView.getmSelMonth() + 1;
				if (month < 10) {
					yearMonth = year + "-0" + month;
				} else {
					yearMonth = year + "-" + month;
				}
				getFestival(yearMonth);

				Calendar cal = Calendar.getInstance();
				int currentYear = cal.get(Calendar.YEAR);
				int currentMonth = cal.get(Calendar.MONTH) + 1;
				if (year <= currentYear && month <= currentMonth) {
					iv_left.setVisibility(View.GONE);
				}
			}
		});
		iv_right = (ImageView) findViewById(R.id.iv_right);
		iv_right.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				monthDateView.onRightClick();
				// 获取节假日
				String yearMonth;
				int year = monthDateView.getmSelYear();
				int month = monthDateView.getmSelMonth() + 1;
				if (month < 10) {
					yearMonth = year + "-0" + month;
				} else {
					yearMonth = year + "-" + month;
				}
				getFestival(yearMonth);

				Calendar cal = Calendar.getInstance();
				int currentYear = cal.get(Calendar.YEAR);
				int currentMonth = cal.get(Calendar.MONTH) + 1;
				if (year >= currentYear && month > currentMonth) {
					iv_left.setVisibility(View.VISIBLE);
				}
			}
		});
		queryByKey();
		getFestival(DateUtils.getMonthYear());
	}

	private void queryByKey() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("key", "REMIND_INFO2");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String url = WDStringRequest.getUrl(Consts.SERVER_queryByKey,
				jsonObject);
		String relative_url = WDStringRequest.getRelativeUrl();
		String sign_body = WDStringRequest.getSignBody();

		WDStringRequest mRequest = new WDStringRequest(Request.Method.GET, url,
				relative_url, sign_body, false,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						try {
							JSONObject jsonObject = new JSONObject(response);
							int code = 0;// 响应头中的code
							JSONArray body = null; // 响应体中的body
							if (jsonObject != null) {
								code = jsonObject.getInt("code");
								if (code == 0) {
									body = new JSONArray(
											jsonObject.getString("body"));
								}
							}
							if (body != null) {
								List<Remind> reminds = Remind.parseJson(body);
								String remindInfo = "";
								for (int i = 0; i < reminds.size(); i++) {
									remindInfo = remindInfo
											+ reminds.get(i).getValue();
								}
								mTipsTv.setText(remindInfo);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						StatusUtils.handleError(arg0,
								SelectDatePopupWindow.this);
					}
				});
		BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
	}

	private void getFestival(String yearMonth) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("yearMonth", yearMonth);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String url = WDStringRequest.getUrl(Consts.SERVER_getFestival,
				jsonObject);
		String relative_url = WDStringRequest.getRelativeUrl();
		String sign_body = WDStringRequest.getSignBody();

		WDStringRequest mRequest = new WDStringRequest(Request.Method.GET, url,
				relative_url, sign_body, false,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						try {
							JSONObject jsonObject = new JSONObject(response);
							int code = 0;// 响应头中的code
							JSONArray body = null; // 响应体中的body
							if (jsonObject != null) {
								code = jsonObject.getInt("code");
								if (code == 0) {
									body = new JSONArray(
											jsonObject.getString("body"));
								}
							}
							if (body != null) {
								JSONArray dateArray = body;
								for (int i = 0; i < 31; i++) {
									holiday[i] = 0;
								}
								for (int i = 0; i < dateArray.length(); i++) {
									int date = dateArray.getInt(i);
									Log.i("holiday", "date: " + date);
									holiday[i] = date;
								}
								Message message = new Message();
								message.what = 1;
								myHandler.sendMessage(message);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						StatusUtils.handleError(arg0,
								SelectDatePopupWindow.this);
					}
				});
		BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
	}

	// 实现onTouchEvent触屏函数但点击屏幕时销毁本Activity
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return true;
	}

	public void onClick(View v) {
		switch (v.getId()) {
		default:
			break;
		}
	}
}
