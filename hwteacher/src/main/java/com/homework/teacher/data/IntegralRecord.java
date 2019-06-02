package com.homework.teacher.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author zhangkc
 * @date 2017-7-25
 */

public class IntegralRecord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3979740466389285584L;
	private int value;// 积分变动值
	private String comment;// 交易备注

	public IntegralRecord() {

	}

	public IntegralRecord(int value, String comment) {
		this.value = value;
		this.comment = comment;
	}

	public static List<IntegralRecord> parseJson(JSONObject jsonObject) {
		JSONArray jsonArray = jsonObject.optJSONArray("dataList");
		List<IntegralRecord> list = new ArrayList<IntegralRecord>();
		if (null != jsonArray && jsonArray.length() > 0) {
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject obj = jsonArray.optJSONObject(i);
				if (null != obj) {
					IntegralRecord integralRecord = new IntegralRecord();
					integralRecord.setValue(obj.optInt("value"));
					integralRecord.setComment(obj.optString("comment"));
					list.add(integralRecord);
				}
			}
		}
		return list;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
