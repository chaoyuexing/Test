package com.homework.teacher.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class ProductWeekDay implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1574121317455629979L;
	private int id;// 一周食谱条目ID
	private int prdId;// 商品ID
	private String prdName;// 商品名称
	private double price;// 单价
	private int num;// 份数
	private int weekDay;// 周几 正序排列

	public ProductWeekDay() {

	}

	public ProductWeekDay(int id, int prdId, String prdName, double price,
			int num, int weekDay) {
		this.id = id;
		this.prdId = prdId;
		this.prdName = prdName;
		this.price = price;
		this.num = num;
		this.weekDay = weekDay;
	}

	public static List<ProductWeekDay> parseJson(JSONArray jsonArray) {
		List<ProductWeekDay> list = new ArrayList<ProductWeekDay>();
		if (null != jsonArray && jsonArray.length() > 0) {
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject obj = jsonArray.optJSONObject(i);
				if (null != obj) {
					ProductWeekDay productWeekDay = new ProductWeekDay();
					productWeekDay.setId(obj.optInt("id"));
					productWeekDay.setPrdId(obj.optInt("prdId"));
					productWeekDay.setPrdName(obj.optString("prdName"));
					productWeekDay.setPrice(obj.optDouble("price"));
					productWeekDay.setNum(obj.optInt("num"));
					productWeekDay.setWeekDay(obj.optInt("weekDay"));
					list.add(productWeekDay);
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

	public int getPrdId() {
		return prdId;
	}

	public void setPrdId(int prdId) {
		this.prdId = prdId;
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

	public int getWeekDay() {
		return weekDay;
	}

	public void setWeekDay(int weekDay) {
		this.weekDay = weekDay;
	}

}
