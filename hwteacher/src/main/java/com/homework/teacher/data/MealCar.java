package com.homework.teacher.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class MealCar implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2339598726104871509L;
	private int prdId;// 产品ID
	private int cityPrdId;// 城市产品ID
	private String prdName;// 产品名称
	private double price;// 产品单价（元） 关联城市产品表取最新产品价格，不是订单产品关联表中的价格
	private int num;// 份数

	public MealCar() {

	}

	public static List<MealCar> parseJson(JSONArray jsonArray) {
		List<MealCar> list = new ArrayList<MealCar>();
		if (null != jsonArray && jsonArray.length() > 0) {
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject obj = jsonArray.optJSONObject(i);
				if (null != obj) {
					MealCar mealCar = new MealCar();
					mealCar.setPrdId(obj.optInt("prdId"));
					mealCar.setCityPrdId(obj.optInt("cityPrdId"));
					mealCar.setPrdName(obj.optString("prdName"));
					mealCar.setPrice(obj.optDouble("price"));
					mealCar.setNum(obj.optInt("num"));
					list.add(mealCar);
				}
			}
		}
		return list;
	}

	public int getPrdId() {
		return prdId;
	}

	public void setPrdId(int prdId) {
		this.prdId = prdId;
	}

	public int getCityPrdId() {
		return cityPrdId;
	}

	public void setCityPrdId(int cityPrdId) {
		this.cityPrdId = cityPrdId;
	}

	public String getPrdName() {
		return prdName;
	}

	public void setPrdName(String prdName) {
		this.prdName = prdName;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

}
