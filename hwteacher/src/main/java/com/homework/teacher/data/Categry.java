package com.homework.teacher.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class Categry implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3353343241979027912L;
	private int weekDay;// 周几 1 — 7
	private int mealCateId;// 餐别ID

	public Categry() {

	}

	public static List<Categry> parseJson(JSONArray jsonArray) {
		List<Categry> list = new ArrayList<Categry>();
		if (null != jsonArray && jsonArray.length() > 0) {
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject obj = jsonArray.optJSONObject(i);
				if (null != obj) {
					Categry categry = new Categry();
					categry.setWeekDay(obj.optInt("weekDay"));
					categry.setMealCateId(obj.optInt("mealCateId"));
					list.add(categry);
				}
			}
		}
		return list;
	}

	public int getWeekDay() {
		return weekDay;
	}

	public void setWeekDay(int weekDay) {
		this.weekDay = weekDay;
	}

	public int getMealCateId() {
		return mealCateId;
	}

	public void setMealCateId(int mealCateId) {
		this.mealCateId = mealCateId;
	}

}
