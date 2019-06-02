package com.homework.teacher.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class Voucher implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4916551801403606133L;
	private int id;// 代金券ID
	private double amount; // 代金券金额（元）

	public Voucher() {

	}

	public static List<Voucher> parseJson(JSONArray jsonArray) {
		List<Voucher> list = new ArrayList<Voucher>();
		if (null != jsonArray && jsonArray.length() > 0) {
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject obj = jsonArray.optJSONObject(i);
				if (null != obj) {
					Voucher voucher = new Voucher();
					voucher.setId(obj.optInt("id"));
					voucher.setAmount(obj.optDouble("amount"));
					list.add(voucher);
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

}
