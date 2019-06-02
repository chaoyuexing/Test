package com.homework.teacher.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class MealCate implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5011632696337739365L;
	private int id;// 用餐计划条目ID
	private int mealCateId;// 餐别ID
	private String mealCateName;// 餐别名称
	private int chooseFlag;// 选定标记 0：未选，1：已选

	public MealCate() {

	}

	public MealCate(int id, int mealCateId, String mealCateName, int chooseFlag) {
		this.id = id;
		this.mealCateId = mealCateId;
		this.mealCateName = mealCateName;
		this.chooseFlag = chooseFlag;
	}

	public static List<MealCate> parseJson(JSONArray jsonArray) {
		List<MealCate> list = new ArrayList<MealCate>();
		if (null != jsonArray && jsonArray.length() > 0) {
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject obj = jsonArray.optJSONObject(i);
				if (null != obj) {
					MealCate mealCate = new MealCate();
					mealCate.setId(obj.optInt("id"));
					mealCate.setMealCateId(obj.optInt("mealCateId"));
					mealCate.setMealCateName(obj.optString("mealCateName"));
					mealCate.setChooseFlag(obj.optInt("chooseFlag"));
					list.add(mealCate);
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

	public int getChooseFlag() {
		return chooseFlag;
	}

	public void setChooseFlag(int chooseFlag) {
		this.chooseFlag = chooseFlag;
	}

}
