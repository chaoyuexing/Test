package com.homework.teacher.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.homework.teacher.Consts;
import com.homework.teacher.R;
import com.homework.teacher.app.BaseApplication;
import com.homework.teacher.http.WDStringRequest;
import com.homework.teacher.utils.StatusUtils;
import com.homework.teacher.utils.StringUtils;

/**
 * 积分兑换代金券页
 * 
 * @author zhangkc
 * @date 2017-7-25
 */
public class IntegralExcVouActivity extends Activity {
	private final static String TAG = IntegralExcVouActivity.class.getName();
	public final static String INTEGRAL = "integral";
	private final static String TOKEN = "token";
	protected BaseApplication mApp;
	private SharedPreferences sp;
	private TextView mIntegralTv, mVoucherTv, mConfirmExcTv;
	private EditText mExcIntegralEt;
	private int integral;// 积分
	private String value;// 兑换积分值
	private double excValue;// 兑换积分值
	private double vouAmount;// 获得代金券金额

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_integralexcvou);

		if (getIntent() != null) {
			integral = getIntent().getIntExtra(INTEGRAL, 0);
			Log.i(TAG, "integral: " + integral);
		}
		mApp = BaseApplication.getInstance();
		sp = mApp.getSp();
		((Button) findViewById(R.id.back))
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
		((TextView) findViewById(R.id.title)).setText(R.string.integralexcvou);
		((TextView) findViewById(R.id.tvSet)).setVisibility(View.GONE);

		mIntegralTv = (TextView) findViewById(R.id.integralTv);// 当前积分
		mIntegralTv.setText(integral + "");

		mExcIntegralEt = (EditText) findViewById(R.id.excIntegralEt);// 兑换积分
		mExcIntegralEt.setText(integral + "");
		mExcIntegralEt.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				value = s.toString();
				if (!StringUtils.isEmpty(value)) {
					excValue = Double.valueOf(value).doubleValue();
					mVoucherTv.setText(excValue / 100 + "元代金券");
				}
			}
		});

		value = mExcIntegralEt.getText().toString().trim();
		mVoucherTv = (TextView) findViewById(R.id.voucherTv);// 代金券
		if (!StringUtils.isEmpty(value)) {
			excValue = Double.valueOf(value).doubleValue();
			mVoucherTv.setText(excValue / 100 + "元代金券");
		}

		mConfirmExcTv = (TextView) findViewById(R.id.confirmExcTv);// 确认兑换
		mConfirmExcTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (StringUtils.isEmpty(value)) {
					Toast.makeText(IntegralExcVouActivity.this, "兑换值不能为空",
							Toast.LENGTH_SHORT).show();
				} else {
					if ((int) excValue > integral) {
						Toast.makeText(IntegralExcVouActivity.this,
								"兑换积分的值不可以大于当前积分", Toast.LENGTH_SHORT).show();
					} else {
						tokenFetch();
					}
				}
			}
		});
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
								intExcVou();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						StatusUtils.handleError(arg0,
								IntegralExcVouActivity.this);
					}
				});
		BaseApplication.getInstance().addToRequestQueue(mRequest, "");
	}

	private void intExcVou() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("cstId", BaseApplication.getInstance().getCstID());// 客户ID
			jsonObject.put("excValue", excValue);// 兑换积分值
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String url = WDStringRequest
				.getUrl(Consts.SERVER_intExcVou, jsonObject);
		String relative_url = WDStringRequest.getRelativeUrl();
		String sign_body = WDStringRequest.getSignBody();

		WDStringRequest mRequest = new WDStringRequest(Request.Method.GET, url,
				relative_url, sign_body, true, new Response.Listener<String>() {
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
								vouAmount = body.optDouble("vouAmount");// 代金券金额
								Log.i(TAG, "vouAmount: " + vouAmount);
								Toast.makeText(IntegralExcVouActivity.this,
										"恭喜您获得" + vouAmount + "元代金券1张",
										Toast.LENGTH_SHORT).show();
								finish();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						StatusUtils.handleError(arg0,
								IntegralExcVouActivity.this);
					}
				});
		BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
	}

}
