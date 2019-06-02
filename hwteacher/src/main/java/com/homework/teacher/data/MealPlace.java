package com.homework.teacher.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class MealPlace implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2694065408222028311L;
	private int id;// 取餐点ID
	private int areaId2;// 二级服务区ID（城市）
	private String areaName2;// 二级服务区名称（城市）
	private int areaId3;// 三级服务区ID（区）
	private String areaName3;// 三级服务区名称（区）
	private int areaId4;// 四级服务区ID（商圈）
	private String areaName4;// 四级服务区名称（商圈）
	private String name;// 取餐点名称
	private int kitchenId;// 厨房ID

	public MealPlace() {

	}

	public MealPlace(int id, int areaId2, String areaName2, int areaId3,
			String areaName3, int areaId4, String areaName4, String name,
			int kitchenId) {
		this.id = id;
		this.areaId2 = areaId2;
		this.areaName2 = areaName2;
		this.areaId3 = areaId3;
		this.areaName3 = areaName3;
		this.areaId4 = areaId4;
		this.areaName4 = areaName4;
		this.name = name;
		this.kitchenId = kitchenId;
	}

	public static List<MealPlace> parseJson(JSONArray jsonArray) {
		List<MealPlace> list = new ArrayList<MealPlace>();
		if (null != jsonArray && jsonArray.length() > 0) {
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject obj = jsonArray.optJSONObject(i);
				if (null != obj) {
					MealPlace mealPlace = new MealPlace();
					mealPlace.setId(obj.optInt("id"));
					mealPlace.setAreaId2(obj.optInt("areaId2"));
					mealPlace.setAreaName2(obj.optString("areaName2"));
					mealPlace.setAreaId3(obj.optInt("areaId3"));
					mealPlace.setAreaName3(obj.optString("areaName3"));
					mealPlace.setAreaId4(obj.optInt("areaId4"));
					mealPlace.setAreaName4(obj.optString("areaName4"));
					mealPlace.setName(obj.optString("name"));
					mealPlace.setKitchenId(obj.optInt("kitchenId"));
					list.add(mealPlace);
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

	public int getAreaId2() {
		return areaId2;
	}

	public void setAreaId2(int areaId2) {
		this.areaId2 = areaId2;
	}

	public String getAreaName2() {
		return areaName2;
	}

	public void setAreaName2(String areaName2) {
		this.areaName2 = areaName2;
	}

	public int getAreaId3() {
		return areaId3;
	}

	public void setAreaId3(int areaId3) {
		this.areaId3 = areaId3;
	}

	public String getAreaName3() {
		return areaName3;
	}

	public void setAreaName3(String areaName3) {
		this.areaName3 = areaName3;
	}

	public int getAreaId4() {
		return areaId4;
	}

	public void setAreaId4(int areaId4) {
		this.areaId4 = areaId4;
	}

	public String getAreaName4() {
		return areaName4;
	}

	public void setAreaName4(String areaName4) {
		this.areaName4 = areaName4;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getKitchenId() {
		return kitchenId;
	}

	public void setKitchenId(int kitchenId) {
		this.kitchenId = kitchenId;
	}

}
