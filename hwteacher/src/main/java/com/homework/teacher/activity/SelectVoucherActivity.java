package com.homework.teacher.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.homework.teacher.Consts;
import com.homework.teacher.R;
import com.homework.teacher.adapter.VoucherAdapter;
import com.homework.teacher.app.BaseApplication;
import com.homework.teacher.data.Voucher;
import com.homework.teacher.http.WDStringRequest;
import com.homework.teacher.utils.StatusUtils;

/**
 * 代金券选择页
 * 
 * @author zhangkc
 * 
 */
public class SelectVoucherActivity extends Activity {
	private final static String TAG = SelectVoucherActivity.class.getName();
	private final static String VOUCHERID = "voucherId";
	private final static String VOUCHERAMOUNT = "voucherAmount";
	private ListView mVoucherListView;
	private List<Voucher> mVoucherList;
	private VoucherAdapter mVoucherAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selectvoucher);

		((Button) findViewById(R.id.back))
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
		((TextView) findViewById(R.id.title)).setText(R.string.select_voucher);
		((TextView) findViewById(R.id.tvSet)).setVisibility(View.GONE);
		mVoucherListView = (ListView) findViewById(R.id.voucherListView);
		if (mVoucherList == null) {
			mVoucherList = new ArrayList<Voucher>();
		}
		mVoucherAdapter = new VoucherAdapter(SelectVoucherActivity.this,
				mVoucherList);
		mVoucherListView.setAdapter(mVoucherAdapter);
		mVoucherListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				int voucherId = mVoucherList.get(position).getId();
				double voucherAmount = mVoucherList.get(position).getAmount();
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putInt(VOUCHERID, voucherId);
				bundle.putDouble(VOUCHERAMOUNT, voucherAmount);
				intent.putExtras(bundle);
				setResult(2, intent);
				finish();
			}
		});
		if (BaseApplication.getInstance().getCstID() != 0) {
			getCustomerInfo();
		}
	}

	private void getCustomerInfo() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("cstId", BaseApplication.getInstance().getCstID());// 客户ID
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String url = WDStringRequest.getUrl(Consts.SERVER_getCustomerInfo,
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
							JSONObject body = null; // 响应体中的body
							if (jsonObject != null) {
								code = jsonObject.getInt("code");
								if (code == 0) {
									body = new JSONObject(
											jsonObject.getString("body"));
								}
							}
							if (body != null) {
								List<Voucher> list = Voucher.parseJson(body
										.optJSONArray("voucherList"));// 代金券
								mVoucherList.clear();
								mVoucherList.addAll(list);
								mVoucherAdapter.notifyDataSetChanged();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						StatusUtils.handleError(arg0,
								SelectVoucherActivity.this);
					}
				});
		BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
	}

}