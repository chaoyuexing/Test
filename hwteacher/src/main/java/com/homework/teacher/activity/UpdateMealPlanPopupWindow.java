package com.homework.teacher.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.homework.teacher.Consts;
import com.homework.teacher.R;
import com.homework.teacher.app.BaseApplication;
import com.homework.teacher.data.MealPlan;
import com.homework.teacher.http.WDStringRequest;
import com.homework.teacher.utils.StatusUtils;

/**
 * 修改用餐计划页
 * 
 * @author zhangkc
 * 
 */
public class UpdateMealPlanPopupWindow extends Activity {
	private final static String TAG = UpdateMealPlanPopupWindow.class.getName();
	private final static String TOKEN = "token";
	private final static String TAKETIMEID = "takeTimeId";
	private final static String TAKETIME = "value";
	private final static String TAKEPOINTID = "takePointId";
	private final static String TAKEPOINTNAME = "takePointName";
	protected BaseApplication mApp;
	private SharedPreferences sp;
	private LinearLayout layout;
	private TextView mMealCateNameTv, mTakeTimeTv, mTakePointNameTv;
	private EditText mDinnerNumEt;
	private MealPlan mealPlan;
	private int entryId;// 用餐计划条目ID
	private int mealCateId;// 餐别ID
	private int takeTimeId;// 取餐时间ID
	private int takePointId;// 取餐点ID
	private int dinnerNum;// 用餐人数
	private String takeTime;// 取餐时间
	private String takePointName;// 取餐点名称

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_updatemealplan);

		if (getIntent().getExtras() != null) {
			mealPlan = (MealPlan) getIntent().getExtras().getSerializable(
					"mealplan");
			entryId = mealPlan.getId();
			mealCateId = mealPlan.getMealCateId();
			takeTimeId = mealPlan.getTakeTimeId();
			takePointId = mealPlan.getTakePointId();
			dinnerNum = mealPlan.getDinnerNum();
		}
		mApp = BaseApplication.getInstance();
		sp = mApp.getSp();
		layout = (LinearLayout) findViewById(R.id.pop_layout);
		// 添加弹出窗口范围监听可以优先获取触点，即不再执行onTouchEvent()函数，点击其他地方时执行onTouchEvent()函数销毁Activity
		layout.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

			}
		});
		mMealCateNameTv = (TextView) findViewById(R.id.mealCateNameTv);
		if (!"".equals(mealPlan.getMealCateName())) {
			mMealCateNameTv.setText(mealPlan.getMealCateName());
		}
		mTakeTimeTv = (TextView) findViewById(R.id.takeTimeTv);
		if (!"".equals(mealPlan.getTakeTime())) {
			mTakeTimeTv.setText(mealPlan.getTakeTime());
		}
		mTakeTimeTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra(SelectTakeTimeActivity.MEALCATEID, mealCateId);
				intent.setClass(UpdateMealPlanPopupWindow.this,
						SelectTakeTimeActivity.class);
				startActivityForResult(intent, 0);
			}
		});
		mTakePointNameTv = (TextView) findViewById(R.id.takePointNameTv);
		if (!"".equals(mealPlan.getTakePointName())) {
			mTakePointNameTv.setText(mealPlan.getTakePointName());
		}
		mTakePointNameTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent(
						UpdateMealPlanPopupWindow.this,
						SelectMealPlaceActivity.class), 0);
			}
		});
		mDinnerNumEt = (EditText) findViewById(R.id.dinnerNumEt);
		if (dinnerNum != 0) {
			mDinnerNumEt.setText(String.valueOf(dinnerNum));
		}
		mDinnerNumEt.setOnEditorActionListener(new OnEditorActionListener() {// 编辑完点击软键盘上的回车键触发
					@Override
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						dinnerNum = Integer.parseInt(mDinnerNumEt.getText()
								.toString());
						tokenFetch();
						return false;
					}
				});
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {
			Bundle bundle = data.getExtras();
			switch (resultCode) {
			case 1:
				takePointId = bundle.getInt(TAKEPOINTID);
				takePointName = bundle.getString(TAKEPOINTNAME);
				mTakePointNameTv.setText(takePointName);
				tokenFetch();
				break;
			case 2:
				takeTimeId = bundle.getInt(TAKETIMEID);
				takeTime = bundle.getString(TAKETIME);
				mTakeTimeTv.setText(takeTime);
				tokenFetch();
				break;
			default:
				break;
			}
		}
	}

	private void tokenFetch() {
		String url = WDStringRequest.getUrl(Consts.SERVER_tokenFetch, null);
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
							JSONObject body = null; // 响应体中的body
							if (jsonObject != null) {
								code = jsonObject.getInt("code");
								if (code == 0) {
									body = new JSONObject(
											jsonObject.getString("body"));
								}
							}
							if (body != null) {
								sp.edit()
										.putString(TOKEN,
												body.optString("token"))
										.commit();
								Log.i(TAG, "接口访问令牌存入本地~~~");
								updateDinnerPlan();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						StatusUtils.handleError(arg0,
								UpdateMealPlanPopupWindow.this);
					}
				});
		BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
	}

	private void updateDinnerPlan() {
		JSONObject jsonObject = new JSONObject();
		try {
			int cstId = BaseApplication.getInstance().getCstID();
			jsonObject.put("cstId", cstId);// 客户ID
			jsonObject.put("entryId", entryId);// 用餐计划条目ID
			jsonObject.put("takeTimeId", takeTimeId);// 取餐时间ID
			jsonObject.put("takePointId", takePointId);// 取餐点ID
			jsonObject.put("dinnerNum", dinnerNum);// 用餐人数
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String url = WDStringRequest.getUrl(Consts.SERVER_updateDinnerPlan,
				jsonObject);
		String relative_url = WDStringRequest.getRelativeUrl();
		String sign_body = WDStringRequest.getSignBody();

		WDStringRequest mRequest = new WDStringRequest(Request.Method.GET, url,
				relative_url, sign_body, true, new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						try {
							JSONObject jsonObject = new JSONObject(response);
							int code = 0;// 响应头中的code
							if (jsonObject != null) {
								code = jsonObject.getInt("code");
								if (code == 0) {
									Toast.makeText(
											UpdateMealPlanPopupWindow.this,
											"修改用餐计划成功", Toast.LENGTH_SHORT)
											.show();
								} else {
									Toast.makeText(
											UpdateMealPlanPopupWindow.this,
											"修改用餐计划失败", Toast.LENGTH_SHORT)
											.show();
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						StatusUtils.handleError(arg0,
								UpdateMealPlanPopupWindow.this);
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

}
