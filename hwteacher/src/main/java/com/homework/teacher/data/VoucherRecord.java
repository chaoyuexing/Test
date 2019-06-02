package com.homework.teacher.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author zhangkc
 * @date 2017-8-1
 */
public class VoucherRecord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6783683508443520239L;
	private int id;// 代金券ID
	private double amount; // 金额
	private String givComment; // 赠送备注
	private String payComment; // 消费备注

	public VoucherRecord() {

	}

	public static List<VoucherRecord> parseJson(JSONObject jsonObject) {
		JSONArray jsonArray = jsonObject.optJSONArray("dataList");
		List<VoucherRecord> list = new ArrayList<VoucherRecord>();
		if (null != jsonArray && jsonArray.length() > 0) {
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject obj = jsonArray.optJSONObject(i);
				if (null != obj) {
					VoucherRecord voucherRecord = new VoucherRecord();
					voucherRecord.setId(obj.optInt("id"));
					voucherRecord.setAmount(obj.optDouble("amount"));
					voucherRecord.setGivComment(obj.optString("givComment"));
					voucherRecord.setPayComment(obj.optString("payComment"));
					list.add(voucherRecord);
				}
			}
		}
		return list;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getGivComment() {
		return givComment;
	}

	public void setGivComment(String givComment) {
		this.givComment = givComment;
	}

	public String getPayComment() {
		return payComment;
	}

	public void setPayComment(String payComment) {
		this.payComment = payComment;
	}

}
