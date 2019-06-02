package com.homework.teacher.fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.homework.teacher.Consts;
import com.homework.teacher.R;
import com.homework.teacher.activity.SelectMealPlaceActivity;
import com.homework.teacher.activity.SelectTakeTimeActivity;
import com.homework.teacher.app.BaseApplication;
import com.homework.teacher.data.MealPlan;
import com.homework.teacher.http.WDStringRequest;
import com.homework.teacher.utils.StatusUtils;
import com.homework.teacher.widget.MyCommonDialog;

/**
 * 用餐计划
 * 
 * @author zhangkc
 * 
 */
public class MealPlanFragment extends Fragment {
	private final static String TAG = "MealPlanFragment1";
	private final static String TOKEN = "token";
	private final static String TAKETIMEID = "takeTimeId";
	private final static String TAKETIME = "value";
	private final static String TAKEPOINTID = "takePointId";
	private final static String TAKEPOINTNAME = "takePointName";
	private View view;
	protected BaseApplication mApp;
	private SharedPreferences sp;

	private TextView weekDayTv, holiDayTv, mealCateSetTv, weekLunchTakeTimeTv,
			weekLunchTakePointTv, weekLunchDecreaseNumTv, weekLunchNumTv,
			weekLunchIncreaseNumTv, weekLunchClearTv, weekSupperTakeTimeTv,
			weekSupperTakePointTv, weekSupperDecreaseNumTv, weekSupperNumTv,
			weekSupperIncreaseNumTv, weekSupperClearTv, holiLunchTakeTimeTv,
			holiLunchTakePointTv, holiLunchDecreaseNumTv, holiLunchNumTv,
			holiLunchIncreaseNumTv, holiLunchClearTv, holiSupperTakeTimeTv,
			holiSupperTakePointTv, holiSupperDecreaseNumTv, holiSupperNumTv,
			holiSupperIncreaseNumTv, holiSupperClearTv;
	private LinearLayout weekdayPlanLy, holidayPlanLy;
	private int weekLunchEntryId, weekSupperEntryId, holiLunchEntryId,
			holiSupperEntryId, entryId;// 用餐计划条目ID
	private int weekLunchMealCateId, weekSupperMealCateId, holiLunchMealCateId,
			holiSupperMealCateId;// 餐别ID
	private int weekLunchNum = 1;
	private int weekSupperNum = 1;
	private int holiLunchNum = 1;
	private int holiSupperNum = 1;
	private int dinnerNum = 1;// // 用餐人数
	private int weekLunchTakeTimeId, weekSupperTakeTimeId, holiLunchTakeTimeId,
			holiSupperTakeTimeId, takeTimeId;// 取餐时间ID
	private int weekLunchTakePointId, weekSupperTakePointId,
			holiLunchTakePointId, holiSupperTakePointId, takePointId;// 取餐点ID
	private String takeTime;// 取餐时间
	private String takePointName;// 取餐点名称
	private int flag;
	private MyCommonDialog dialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.mealplan_fragment, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.e(TAG, "onActivityCreated");
		mApp = BaseApplication.getInstance();
		sp = mApp.getSp();

		weekDayTv = (TextView) view.findViewById(R.id.weekDayTv);
		weekDayTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				weekDayTv.setBackgroundResource(R.drawable.meal_cate_click);
				weekDayTv.setTextColor(getResources().getColor(R.color.white));
				holiDayTv.setBackgroundResource(R.drawable.meal_cate_normal);
				holiDayTv.setTextColor(getResources().getColor(R.color.black));
				weekdayPlanLy.setVisibility(View.VISIBLE);
				holidayPlanLy.setVisibility(View.GONE);
				tokenFetch("weekLunchDinnerPlan");
				tokenFetch("weekSupperDinnerPlan");
			}
		});
		holiDayTv = (TextView) view.findViewById(R.id.holiDayTv);
		holiDayTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				weekDayTv.setBackgroundResource(R.drawable.meal_cate_normal);
				weekDayTv.setTextColor(getResources().getColor(R.color.black));
				holiDayTv.setBackgroundResource(R.drawable.meal_cate_click);
				holiDayTv.setTextColor(getResources().getColor(R.color.white));
				weekdayPlanLy.setVisibility(View.GONE);
				holidayPlanLy.setVisibility(View.VISIBLE);
				tokenFetch("holiLunchDinnerPlan");
				tokenFetch("holiSupperDinnerPlan");
			}
		});
		mealCateSetTv = (TextView) view.findViewById(R.id.mealCateSetTv);
		mealCateSetTv.setVisibility(View.GONE);

		// 工作日 午餐
		weekLunchTakeTimeTv = (TextView) view
				.findViewById(R.id.weekLunchTakeTimeTv);
		weekLunchTakeTimeTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				takeTimeId = weekLunchTakeTimeId;
				Intent intent = new Intent();
				intent.putExtra("flag", 1);
				intent.putExtra(SelectTakeTimeActivity.MEALCATEID,
						weekLunchMealCateId);
				intent.setClass(getActivity(), SelectTakeTimeActivity.class);
				getRootFragment().startActivityForResult(intent, 0);
			}
		});
		weekLunchTakePointTv = (TextView) view
				.findViewById(R.id.weekLunchTakePointTv);
		weekLunchTakePointTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				takePointId = weekLunchTakePointId;
				Intent intent = new Intent();
				intent.putExtra("flag", 1);
				intent.setClass(getActivity(), SelectMealPlaceActivity.class);
				getRootFragment().startActivityForResult(intent, 0);
			}
		});
		weekLunchDecreaseNumTv = (TextView) view
				.findViewById(R.id.weekLunchDecreaseNumTv);
		weekLunchDecreaseNumTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				weekLunchNum = Integer.valueOf(weekLunchNumTv.getText()
						.toString());
				if (weekLunchNum > 1) {
					weekLunchNum--;
					weekLunchNumTv.setText(String.valueOf(weekLunchNum));
					dinnerNum = weekLunchNum;
					entryId = weekLunchEntryId;
					tokenFetch("updateDinnerNum");
				}
			}
		});
		weekLunchNumTv = (TextView) view.findViewById(R.id.weekLunchNumTv);
		weekLunchIncreaseNumTv = (TextView) view
				.findViewById(R.id.weekLunchIncreaseNumTv);
		weekLunchIncreaseNumTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				weekLunchNum = Integer.valueOf(weekLunchNumTv.getText()
						.toString());
				weekLunchNum++;
				weekLunchNumTv.setText(String.valueOf(weekLunchNum));
				dinnerNum = weekLunchNum;
				entryId = weekLunchEntryId;
				tokenFetch("updateDinnerNum");
			}
		});
		weekLunchClearTv = (TextView) view.findViewById(R.id.weekLunchClearTv);
		weekLunchClearTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog = new MyCommonDialog(getActivity(), "提示消息",
						"确定要清空工作日-午餐的用餐计划吗？", "取消", "确定");
				dialog.setOkListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (dialog.isShowing()) {
							dialog.dismiss();
						}
						entryId = weekLunchEntryId;
						tokenFetch("clearWeekLunchDinnerPlan");
						if (dialog.isShowing()) {
							dialog.dismiss();
						}
					}
				});
				dialog.setCancelListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (dialog.isShowing()) {
							dialog.dismiss();
						}
					}
				});
				dialog.show();
			}
		});

		// 工作日 晚餐
		weekSupperTakeTimeTv = (TextView) view
				.findViewById(R.id.weekSupperTakeTimeTv);
		weekSupperTakeTimeTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				takeTimeId = weekSupperTakeTimeId;
				Intent intent = new Intent();
				intent.putExtra("flag", 2);
				intent.putExtra(SelectTakeTimeActivity.MEALCATEID,
						weekSupperMealCateId);
				intent.setClass(getActivity(), SelectTakeTimeActivity.class);
				getRootFragment().startActivityForResult(intent, 0);
			}
		});
		weekSupperTakePointTv = (TextView) view
				.findViewById(R.id.weekSupperTakePointTv);
		weekSupperTakePointTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				takePointId = weekSupperTakePointId;
				Intent intent = new Intent();
				intent.putExtra("flag", 2);
				intent.setClass(getActivity(), SelectMealPlaceActivity.class);
				getRootFragment().startActivityForResult(intent, 0);
			}
		});
		weekSupperDecreaseNumTv = (TextView) view
				.findViewById(R.id.weekSupperDecreaseNumTv);
		weekSupperDecreaseNumTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				weekSupperNum = Integer.valueOf(weekSupperNumTv.getText()
						.toString());
				if (weekSupperNum > 1) {
					weekSupperNum--;
					weekSupperNumTv.setText(String.valueOf(weekSupperNum));
					dinnerNum = weekSupperNum;
					entryId = weekSupperEntryId;
					tokenFetch("updateDinnerNum");
				}
			}
		});
		weekSupperNumTv = (TextView) view.findViewById(R.id.weekSupperNumTv);
		weekSupperIncreaseNumTv = (TextView) view
				.findViewById(R.id.weekSupperIncreaseNumTv);
		weekSupperIncreaseNumTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				weekSupperNum = Integer.valueOf(weekSupperNumTv.getText()
						.toString());
				weekSupperNum++;
				weekSupperNumTv.setText(String.valueOf(weekSupperNum));
				dinnerNum = weekSupperNum;
				entryId = weekSupperEntryId;
				tokenFetch("updateDinnerNum");
			}
		});
		weekSupperClearTv = (TextView) view
				.findViewById(R.id.weekSupperClearTv);
		weekSupperClearTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog = new MyCommonDialog(getActivity(), "提示消息",
						"确定要清空工作日-晚餐的用餐计划吗？", "取消", "确定");
				dialog.setOkListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (dialog.isShowing()) {
							dialog.dismiss();
						}
						entryId = weekSupperEntryId;
						tokenFetch("clearWeekSupperDinnerPlan");
						if (dialog.isShowing()) {
							dialog.dismiss();
						}
					}
				});
				dialog.setCancelListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (dialog.isShowing()) {
							dialog.dismiss();
						}
					}
				});
				dialog.show();
			}
		});

		// 节假日 午餐
		holiLunchTakeTimeTv = (TextView) view
				.findViewById(R.id.holiLunchTakeTimeTv);
		holiLunchTakeTimeTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				takeTimeId = holiLunchTakeTimeId;
				Intent intent = new Intent();
				intent.putExtra("flag", 3);
				intent.putExtra(SelectTakeTimeActivity.MEALCATEID,
						holiLunchMealCateId);
				intent.setClass(getActivity(), SelectTakeTimeActivity.class);
				getRootFragment().startActivityForResult(intent, 0);
			}
		});
		holiLunchTakePointTv = (TextView) view
				.findViewById(R.id.holiLunchTakePointTv);
		holiLunchTakePointTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				takePointId = holiLunchTakePointId;
				Intent intent = new Intent();
				intent.putExtra("flag", 3);
				intent.setClass(getActivity(), SelectMealPlaceActivity.class);
				getRootFragment().startActivityForResult(intent, 0);
			}
		});
		holiLunchDecreaseNumTv = (TextView) view
				.findViewById(R.id.holiLunchDecreaseNumTv);
		holiLunchDecreaseNumTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				holiLunchNum = Integer.valueOf(holiLunchNumTv.getText()
						.toString());
				if (holiLunchNum > 1) {
					holiLunchNum--;
					holiLunchNumTv.setText(String.valueOf(holiLunchNum));
					dinnerNum = holiLunchNum;
					entryId = holiLunchEntryId;
					tokenFetch("updateDinnerNum");
				}
			}
		});
		holiLunchNumTv = (TextView) view.findViewById(R.id.holiLunchNumTv);
		holiLunchIncreaseNumTv = (TextView) view
				.findViewById(R.id.holiLunchIncreaseNumTv);
		holiLunchIncreaseNumTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				holiLunchNum = Integer.valueOf(holiLunchNumTv.getText()
						.toString());
				holiLunchNum++;
				holiLunchNumTv.setText(String.valueOf(holiLunchNum));
				dinnerNum = holiLunchNum;
				entryId = holiLunchEntryId;
				tokenFetch("updateDinnerNum");
			}
		});
		holiLunchClearTv = (TextView) view.findViewById(R.id.holiLunchClearTv);
		holiLunchClearTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog = new MyCommonDialog(getActivity(), "提示消息",
						"确定要清空节假日-午餐的用餐计划吗？", "取消", "确定");
				dialog.setOkListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (dialog.isShowing()) {
							dialog.dismiss();
						}
						entryId = holiLunchEntryId;
						tokenFetch("clearHoliLunchDinnerPlan");
						if (dialog.isShowing()) {
							dialog.dismiss();
						}
					}
				});
				dialog.setCancelListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (dialog.isShowing()) {
							dialog.dismiss();
						}
					}
				});
				dialog.show();
			}
		});

		// 节假日 晚餐
		holiSupperTakeTimeTv = (TextView) view
				.findViewById(R.id.holiSupperTakeTimeTv);
		holiSupperTakeTimeTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				takeTimeId = holiSupperTakeTimeId;
				Intent intent = new Intent();
				intent.putExtra("flag", 4);
				intent.putExtra(SelectTakeTimeActivity.MEALCATEID,
						holiSupperMealCateId);
				intent.setClass(getActivity(), SelectTakeTimeActivity.class);
				getRootFragment().startActivityForResult(intent, 0);
			}
		});
		holiSupperTakePointTv = (TextView) view
				.findViewById(R.id.holiSupperTakePointTv);
		holiSupperTakePointTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				takePointId = holiSupperTakePointId;
				Intent intent = new Intent();
				intent.putExtra("flag", 4);
				intent.setClass(getActivity(), SelectMealPlaceActivity.class);
				getRootFragment().startActivityForResult(intent, 0);
			}
		});
		holiSupperDecreaseNumTv = (TextView) view
				.findViewById(R.id.holiSupperDecreaseNumTv);
		holiSupperDecreaseNumTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				holiSupperNum = Integer.valueOf(holiSupperNumTv.getText()
						.toString());
				if (holiSupperNum > 1) {
					holiSupperNum--;
					holiSupperNumTv.setText(String.valueOf(holiSupperNum));
					dinnerNum = holiSupperNum;
					entryId = holiSupperEntryId;
					tokenFetch("updateDinnerNum");
				}
			}
		});
		holiSupperNumTv = (TextView) view.findViewById(R.id.holiSupperNumTv);
		holiSupperIncreaseNumTv = (TextView) view
				.findViewById(R.id.holiSupperIncreaseNumTv);
		holiSupperIncreaseNumTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				holiSupperNum = Integer.valueOf(holiSupperNumTv.getText()
						.toString());
				holiSupperNum++;
				holiSupperNumTv.setText(String.valueOf(holiSupperNum));
				dinnerNum = holiSupperNum;
				entryId = holiSupperEntryId;
				tokenFetch("updateDinnerNum");
			}
		});
		holiSupperClearTv = (TextView) view
				.findViewById(R.id.holiSupperClearTv);
		holiSupperClearTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog = new MyCommonDialog(getActivity(), "提示消息",
						"确定要清空节假日-晚餐的用餐计划吗？", "取消", "确定");
				dialog.setOkListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (dialog.isShowing()) {
							dialog.dismiss();
						}
						entryId = holiSupperEntryId;
						tokenFetch("clearHoliSupperDinnerPlan");
						if (dialog.isShowing()) {
							dialog.dismiss();
						}
					}
				});
				dialog.setCancelListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (dialog.isShowing()) {
							dialog.dismiss();
						}
					}
				});
				dialog.show();
			}
		});

		weekdayPlanLy = (LinearLayout) view.findViewById(R.id.weekdayPlanLy);
		holidayPlanLy = (LinearLayout) view.findViewById(R.id.holidayPlanLy);

		tokenFetch("weekLunchDinnerPlan");
		tokenFetch("weekSupperDinnerPlan");
	}

	/**
	 * 得到根Fragment
	 * 
	 * @return
	 */
	private Fragment getRootFragment() {
		Fragment fragment = getParentFragment();
		while (fragment.getParentFragment() != null) {
			fragment = fragment.getParentFragment();
		}
		return fragment;
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.e(TAG, "onResume");
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {
			Bundle bundle = data.getExtras();
			switch (resultCode) {
			case 1:
				takePointId = bundle.getInt(TAKEPOINTID);
				takePointName = bundle.getString(TAKEPOINTNAME);
				flag = bundle.getInt("flag");
				if (flag == 1) {
					entryId = weekLunchEntryId;
					weekLunchTakePointTv.setText(takePointName);
				} else if (flag == 2) {
					entryId = weekSupperEntryId;
					weekSupperTakePointTv.setText(takePointName);
				} else if (flag == 3) {
					entryId = holiLunchEntryId;
					holiLunchTakePointTv.setText(takePointName);
				} else if (flag == 4) {
					entryId = holiSupperEntryId;
					holiSupperTakePointTv.setText(takePointName);
				}
				tokenFetch("updateTakePoint");
				break;
			case 2:
				takeTimeId = bundle.getInt(TAKETIMEID);
				takeTime = bundle.getString(TAKETIME);
				flag = bundle.getInt("flag");
				if (flag == 1) {
					entryId = weekLunchEntryId;
					weekLunchTakeTimeTv.setText(takeTime);
				} else if (flag == 2) {
					entryId = weekSupperEntryId;
					weekSupperTakeTimeTv.setText(takeTime);
				} else if (flag == 3) {
					entryId = holiLunchEntryId;
					holiLunchTakeTimeTv.setText(takeTime);
				} else if (flag == 4) {
					entryId = holiSupperEntryId;
					holiSupperTakeTimeTv.setText(takeTime);
				}
				tokenFetch("updateTakeTime");
				break;
			default:
				break;
			}
		}
	}

	private void tokenFetch(final String method) {
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
									body = new JSONObject(jsonObject
											.getString("body"));
								}
							}
							if (body != null) {
								sp.edit()
										.putString(TOKEN,
												body.optString("token"))
										.commit();
								Log.i(TAG, "接口访问令牌存入本地~~~");
								if (method.equals("weekLunchDinnerPlan")) {
									weekLunchDinnerPlan();
								} else if (method
										.equals("weekSupperDinnerPlan")) {
									weekSupperDinnerPlan();
								} else if (method.equals("holiLunchDinnerPlan")) {
									holiLunchDinnerPlan();
								} else if (method
										.equals("holiSupperDinnerPlan")) {
									holiSupperDinnerPlan();
								} else if (method.equals("updateDinnerNum")) {
									updateDinnerNum();
								} else if (method.equals("updateTakeTime")) {
									updateTakeTime();
								} else if (method.equals("updateTakePoint")) {
									updateTakePoint();
								} else if (method
										.equals("clearWeekLunchDinnerPlan")) {
									clearWeekLunchDinnerPlan();
								} else if (method
										.equals("clearWeekSupperDinnerPlan")) {
									clearWeekSupperDinnerPlan();
								} else if (method
										.equals("clearHoliLunchDinnerPlan")) {
									clearHoliLunchDinnerPlan();
								} else if (method
										.equals("clearHoliSupperDinnerPlan")) {
									clearHoliSupperDinnerPlan();
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						StatusUtils.handleError(arg0, getActivity());
					}
				});
		BaseApplication.getInstance().addToRequestQueue(mRequest, "");
	}

	private void weekLunchDinnerPlan() {
		JSONObject jsonObject = new JSONObject();
		try {
			int cstId = BaseApplication.getInstance().getCstID();
			jsonObject.put("cstId", cstId);// 客户ID
			jsonObject.put("dayType", 2);// 日期类型 1：节假日，2：工作日
			jsonObject.put("mealCateId", 10);// 餐别ID
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String url = WDStringRequest.getUrl(Consts.SERVER_queryDinnerPlan,
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
							JSONArray body = null; // 响应体中的body
							if (jsonObject != null) {
								code = jsonObject.getInt("code");
								if (code == 0) {
									body = new JSONArray(jsonObject
											.getString("body"));
								}
							}
							if (body != null) {
								MealPlan mealPlan = MealPlan.parseJson(body)
										.get(0);
								weekLunchEntryId = mealPlan.getId();
								weekLunchMealCateId = mealPlan.getMealCateId();
								weekLunchTakeTimeId = mealPlan.getTakeTimeId();
								weekLunchTakePointId = mealPlan
										.getTakePointId();
								if ("".equals(mealPlan.getTakeTime())) {
									weekLunchTakeTimeTv.setText("选择取餐时间");
								} else {
									weekLunchTakeTimeTv.setText(mealPlan
											.getTakeTime());
								}
								if ("".equals(mealPlan.getTakePointName())) {
									weekLunchTakePointTv.setText("选择取餐点");
								} else {
									weekLunchTakePointTv.setText(mealPlan
											.getTakePointName());
								}
								weekLunchNumTv.setText(String.valueOf(mealPlan
										.getDinnerNum()));
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						StatusUtils.handleError(arg0, getActivity());
					}
				});
		BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
	}

	private void weekSupperDinnerPlan() {
		JSONObject jsonObject = new JSONObject();
		try {
			int cstId = BaseApplication.getInstance().getCstID();
			jsonObject.put("cstId", cstId);// 客户ID
			jsonObject.put("dayType", 2);// 日期类型 1：节假日，2：工作日
			jsonObject.put("mealCateId", 12);// 餐别ID
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String url = WDStringRequest.getUrl(Consts.SERVER_queryDinnerPlan,
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
							JSONArray body = null; // 响应体中的body
							if (jsonObject != null) {
								code = jsonObject.getInt("code");
								if (code == 0) {
									body = new JSONArray(jsonObject
											.getString("body"));
								}
							}
							if (body != null) {
								MealPlan mealPlan = MealPlan.parseJson(body)
										.get(0);
								weekSupperEntryId = mealPlan.getId();
								weekSupperMealCateId = mealPlan.getMealCateId();
								weekSupperTakeTimeId = mealPlan.getTakeTimeId();
								weekSupperTakePointId = mealPlan
										.getTakePointId();
								if ("".equals(mealPlan.getTakeTime())) {
									weekSupperTakeTimeTv.setText("选择取餐时间");
								} else {
									weekSupperTakeTimeTv.setText(mealPlan
											.getTakeTime());
								}
								if ("".equals(mealPlan.getTakePointName())) {
									weekSupperTakePointTv.setText("选择取餐点");
								} else {
									weekSupperTakePointTv.setText(mealPlan
											.getTakePointName());
								}
								weekSupperNumTv.setText(String.valueOf(mealPlan
										.getDinnerNum()));
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						StatusUtils.handleError(arg0, getActivity());
					}
				});
		BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
	}

	private void holiLunchDinnerPlan() {
		JSONObject jsonObject = new JSONObject();
		try {
			int cstId = BaseApplication.getInstance().getCstID();
			jsonObject.put("cstId", cstId);// 客户ID
			jsonObject.put("dayType", 1);// 日期类型 1：节假日，2：工作日
			jsonObject.put("mealCateId", 10);// 餐别ID
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String url = WDStringRequest.getUrl(Consts.SERVER_queryDinnerPlan,
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
							JSONArray body = null; // 响应体中的body
							if (jsonObject != null) {
								code = jsonObject.getInt("code");
								if (code == 0) {
									body = new JSONArray(jsonObject
											.getString("body"));
								}
							}
							if (body != null) {
								MealPlan mealPlan = MealPlan.parseJson(body)
										.get(0);
								holiLunchEntryId = mealPlan.getId();
								holiLunchMealCateId = mealPlan.getMealCateId();
								holiLunchTakeTimeId = mealPlan.getTakeTimeId();
								holiLunchTakePointId = mealPlan
										.getTakePointId();
								if ("".equals(mealPlan.getTakeTime())) {
									holiLunchTakeTimeTv.setText("选择取餐时间");
								} else {
									holiLunchTakeTimeTv.setText(mealPlan
											.getTakeTime());
								}
								if ("".equals(mealPlan.getTakePointName())) {
									holiLunchTakePointTv.setText("选择取餐点");
								} else {
									holiLunchTakePointTv.setText(mealPlan
											.getTakePointName());
								}
								holiLunchNumTv.setText(String.valueOf(mealPlan
										.getDinnerNum()));
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						StatusUtils.handleError(arg0, getActivity());
					}
				});
		BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
	}

	private void holiSupperDinnerPlan() {
		JSONObject jsonObject = new JSONObject();
		try {
			int cstId = BaseApplication.getInstance().getCstID();
			jsonObject.put("cstId", cstId);// 客户ID
			jsonObject.put("dayType", 1);// 日期类型 1：节假日，2：工作日
			jsonObject.put("mealCateId", 12);// 餐别ID
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String url = WDStringRequest.getUrl(Consts.SERVER_queryDinnerPlan,
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
							JSONArray body = null; // 响应体中的body
							if (jsonObject != null) {
								code = jsonObject.getInt("code");
								if (code == 0) {
									body = new JSONArray(jsonObject
											.getString("body"));
								}
							}
							if (body != null) {
								MealPlan mealPlan = MealPlan.parseJson(body)
										.get(0);
								holiSupperEntryId = mealPlan.getId();
								holiSupperMealCateId = mealPlan.getMealCateId();
								holiSupperTakeTimeId = mealPlan.getTakeTimeId();
								holiSupperTakePointId = mealPlan
										.getTakePointId();
								if ("".equals(mealPlan.getTakeTime())) {
									holiSupperTakeTimeTv.setText("选择取餐时间");
								} else {
									holiSupperTakeTimeTv.setText(mealPlan
											.getTakeTime());
								}
								if ("".equals(mealPlan.getTakePointName())) {
									holiSupperTakePointTv.setText("选择取餐点");
								} else {
									holiSupperTakePointTv.setText(mealPlan
											.getTakePointName());
								}
								holiSupperNumTv.setText(String.valueOf(mealPlan
										.getDinnerNum()));
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						StatusUtils.handleError(arg0, getActivity());
					}
				});
		BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
	}

	private void updateTakeTime() {
		JSONObject jsonObject = new JSONObject();
		try {
			int cstId = BaseApplication.getInstance().getCstID();
			jsonObject.put("cstId", cstId);// 客户ID
			jsonObject.put("entryId", entryId);// 用餐计划条目ID
			jsonObject.put("takeTimeId", takeTimeId);// 取餐时间ID
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
									Toast.makeText(getActivity(), "修改取餐时间成功",
											Toast.LENGTH_SHORT).show();
								} else {
									Toast.makeText(getActivity(), "修改取餐时间失败",
											Toast.LENGTH_SHORT).show();
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						StatusUtils.handleError(arg0, getActivity());
					}
				});
		BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
	}

	private void updateTakePoint() {
		JSONObject jsonObject = new JSONObject();
		try {
			int cstId = BaseApplication.getInstance().getCstID();
			jsonObject.put("cstId", cstId);// 客户ID
			jsonObject.put("entryId", entryId);// 用餐计划条目ID
			jsonObject.put("takePointId", takePointId);// 取餐点ID
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
									Toast.makeText(getActivity(), "修改取餐点成功",
											Toast.LENGTH_SHORT).show();
								} else {
									Toast.makeText(getActivity(), "修改取餐点失败",
											Toast.LENGTH_SHORT).show();
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						StatusUtils.handleError(arg0, getActivity());
					}
				});
		BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
	}

	private void updateDinnerNum() {
		JSONObject jsonObject = new JSONObject();
		try {
			int cstId = BaseApplication.getInstance().getCstID();
			jsonObject.put("cstId", cstId);// 客户ID
			jsonObject.put("entryId", entryId);// 用餐计划条目ID
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
									Toast.makeText(getActivity(), "修改用餐人数成功",
											Toast.LENGTH_SHORT).show();
								} else {
									Toast.makeText(getActivity(), "修改用餐人数失败",
											Toast.LENGTH_SHORT).show();
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						StatusUtils.handleError(arg0, getActivity());
					}
				});
		BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
	}

	private void clearWeekLunchDinnerPlan() {
		JSONObject jsonObject = new JSONObject();
		try {
			int cstId = BaseApplication.getInstance().getCstID();
			jsonObject.put("cstId", cstId);// 客户ID
			jsonObject.put("entryId", entryId);// 用餐计划条目ID
			jsonObject.put("takeTimeId", 0);// 取餐时间ID
			jsonObject.put("takePointId", 0);// 取餐点ID
			jsonObject.put("dinnerNum", 1);// 用餐人数
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
									Toast.makeText(getActivity(), "清空用餐计划成功",
											Toast.LENGTH_SHORT).show();
									tokenFetch("weekLunchDinnerPlan");
								} else {
									Toast.makeText(getActivity(), "清空用餐计划失败",
											Toast.LENGTH_SHORT).show();
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						StatusUtils.handleError(arg0, getActivity());
					}
				});
		BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
	}

	private void clearWeekSupperDinnerPlan() {
		JSONObject jsonObject = new JSONObject();
		try {
			int cstId = BaseApplication.getInstance().getCstID();
			jsonObject.put("cstId", cstId);// 客户ID
			jsonObject.put("entryId", entryId);// 用餐计划条目ID
			jsonObject.put("takeTimeId", 0);// 取餐时间ID
			jsonObject.put("takePointId", 0);// 取餐点ID
			jsonObject.put("dinnerNum", 1);// 用餐人数
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
									Toast.makeText(getActivity(), "清空用餐计划成功",
											Toast.LENGTH_SHORT).show();
									tokenFetch("weekSupperDinnerPlan");
								} else {
									Toast.makeText(getActivity(), "清空用餐计划失败",
											Toast.LENGTH_SHORT).show();
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						StatusUtils.handleError(arg0, getActivity());
					}
				});
		BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
	}

	private void clearHoliLunchDinnerPlan() {
		JSONObject jsonObject = new JSONObject();
		try {
			int cstId = BaseApplication.getInstance().getCstID();
			jsonObject.put("cstId", cstId);// 客户ID
			jsonObject.put("entryId", entryId);// 用餐计划条目ID
			jsonObject.put("takeTimeId", 0);// 取餐时间ID
			jsonObject.put("takePointId", 0);// 取餐点ID
			jsonObject.put("dinnerNum", 1);// 用餐人数
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
									Toast.makeText(getActivity(), "清空用餐计划成功",
											Toast.LENGTH_SHORT).show();
									tokenFetch("holiLunchDinnerPlan");
								} else {
									Toast.makeText(getActivity(), "清空用餐计划失败",
											Toast.LENGTH_SHORT).show();
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						StatusUtils.handleError(arg0, getActivity());
					}
				});
		BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
	}

	private void clearHoliSupperDinnerPlan() {
		JSONObject jsonObject = new JSONObject();
		try {
			int cstId = BaseApplication.getInstance().getCstID();
			jsonObject.put("cstId", cstId);// 客户ID
			jsonObject.put("entryId", entryId);// 用餐计划条目ID
			jsonObject.put("takeTimeId", 0);// 取餐时间ID
			jsonObject.put("takePointId", 0);// 取餐点ID
			jsonObject.put("dinnerNum", 1);// 用餐人数
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
									Toast.makeText(getActivity(), "清空用餐计划成功",
											Toast.LENGTH_SHORT).show();
									tokenFetch("holiSupperDinnerPlan");
								} else {
									Toast.makeText(getActivity(), "清空用餐计划失败",
											Toast.LENGTH_SHORT).show();
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						StatusUtils.handleError(arg0, getActivity());
					}
				});
		BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
	}

}
