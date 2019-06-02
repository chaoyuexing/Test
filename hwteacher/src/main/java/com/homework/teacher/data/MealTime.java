package com.homework.teacher.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class MealTime implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1336861458764091231L;
	private int mealCateId;// 餐别ID
	private String mealCateName;// 餐别名称
	private int id;// 取餐时间ID
	private String value;// 取餐时间

	public MealTime() {

	}

	public MealTime(int mealCateId, String mealCateName, int id, String value) {
		this.mealCateId = mealCateId;
		this.mealCateName = mealCateName;
		this.id = id;
		this.value = value;
	}

	public static List<MealTime> parseJson(JSONArray jsonArray) {
		List<MealTime> list = new ArrayList<MealTime>();
		if (null != jsonArray && jsonArray.length() > 0) {
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject obj = jsonArray.optJSONObject(i);
				if (null != obj) {
					MealTime mealTime = new MealTime();
					mealTime.setMealCateId(obj.optInt("mealCateId"));
					mealTime.setMealCateName(obj.optString("mealCateName"));
					mealTime.setId(obj.optInt("id"));
					mealTime.setValue(obj.optString("value"));
					list.add(mealTime);
				}
			}
		}
		return list;
	}

	public int getMealCateId() {
		return mealCateId;
	}

	public void setMealCateId(int mealCateId) {
		this.mealCateId = mealCateId;
	}

	public String getMealCateName() {
		return mealCateName;
	}

	public void setMealCateName(String mealCateName) {
		this.mealCateName = mealCateName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
