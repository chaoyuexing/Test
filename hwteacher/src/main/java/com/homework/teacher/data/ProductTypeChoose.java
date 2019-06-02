package com.homework.teacher.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class ProductTypeChoose implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3963124327888637608L;
	private int id;// 商品品类ID
	private String name;// 商品品类名称
	private int prdNum;// 已选商品数量 数量，不含份数，同一商品选两份算一个

	public ProductTypeChoose() {

	}

	public ProductTypeChoose(int id, String name, int prdNum) {
		this.id = id;
		this.name = name;
		this.prdNum = prdNum;
	}

	public static List<ProductTypeChoose> parseJson(JSONArray jsonArray) {
		List<ProductTypeChoose> list = new ArrayList<ProductTypeChoose>();
		if (null != jsonArray && jsonArray.length() > 0) {
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject obj = jsonArray.optJSONObject(i);
				if (null != obj) {
					ProductTypeChoose ptc = new ProductTypeChoose();
					ptc.setId(obj.optInt("id"));
					ptc.setName(obj.optString("name"));
					ptc.setPrdNum(obj.optInt("prdNum"));
					list.add(ptc);
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPrdNum() {
		return prdNum;
	}

	public void setPrdNum(int prdNum) {
		this.prdNum = prdNum;
	}

}
