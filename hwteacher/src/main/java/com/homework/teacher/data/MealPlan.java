package com.homework.teacher.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class MealPlan implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4765836183688413192L;
	private int id;// 用餐计划条目ID
	private int mealCateId;// 餐别ID
	private String mealCateName;// 餐别名称
	private int takeTimeId;// 取餐时间ID
	private String takeTime;// 取餐时间
	private int takePointId;// 取餐点ID
	private String takePointName;// 取餐点名称
	private int dinnerNum;// 用餐人数

	public MealPlan() {

	}

	public MealPlan(int id, int mealCateId, String mealCateName,
			int takeTimeId, String takeTime, int takePointId,
			String takePointName, int dinnerNum) {
		this.id = id;
		this.mealCateId = mealCateId;
		this.mealCateName = mealCateName;
		this.takeTimeId = takeTimeId;
		this.takeTime = takeTime;
		this.takePointId = takePointId;
		this.takePointName = takePointName;
		this.dinnerNum = dinnerNum;
	}

	public static List<MealPlan> parseJson(JSONArray jsonArray) {
		List<MealPlan> list = new ArrayList<MealPlan>();
		if (null != jsonArray && jsonArray.length() > 0) {
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject obj = jsonArray.optJSONObject(i);
				if (null != obj) {
					MealPlan mealPlan = new MealPlan();
					mealPlan.setId(obj.optInt("id"));
					mealPlan.setMealCateId(obj.optInt("mealCateId"));
					mealPlan.setMealCateName(obj.optString("mealCateName"));
					mealPlan.setTakeTimeId(obj.optInt("takeTimeId"));
					mealPlan.setTakeTime(obj.optString("takeTime"));
					mealPlan.setTakePointId(obj.optInt("takePointId"));
					mealPlan.setTakePointName(obj.optString("takePointName"));
					mealPlan.setDinnerNum(obj.optInt("dinnerNum"));
					list.add(mealPlan);
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

	public int getTakeTimeId() {
		return takeTimeId;
	}

	public void setTakeTimeId(int takeTimeId) {
		this.takeTimeId = takeTimeId;
	}

	public String getTakeTime() {
		return takeTime;
	}

	public void setTakeTime(String takeTime) {
		this.takeTime = takeTime;
	}

	public int getTakePointId() {
		return takePointId;
	}

	public void setTakePointId(int takePointId) {
		this.takePointId = takePointId;
	}

	public String getTakePointName() {
		return takePointName;
	}

	public void setTakePointName(String takePointName) {
		this.takePointName = takePointName;
	}

	public int getDinnerNum() {
		return dinnerNum;
	}

	public void setDinnerNum(int dinnerNum) {
		this.dinnerNum = dinnerNum;
	}

}
