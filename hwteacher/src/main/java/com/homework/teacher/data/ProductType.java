package com.homework.teacher.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class ProductType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2700571506762403533L;
	private int id;// 产品类型ID
	private String name;// 产品类型名称
	private String iconURL;// 图标
	private String hlIconURL;// 高亮图标

	public ProductType() {

	}

	public ProductType(int id, String name, String iconURL, String hlIconURL) {
		this.id = id;
		this.name = name;
		this.iconURL = iconURL;
		this.hlIconURL = hlIconURL;
	}

	public static List<ProductType> parseJson(JSONArray jsonArray) {
		List<ProductType> list = new ArrayList<ProductType>();
		if (null != jsonArray && jsonArray.length() > 0) {
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject obj = jsonArray.optJSONObject(i);
				if (null != obj) {
					ProductType pt = new ProductType();
					pt.setId(obj.optInt("id"));
					pt.setName(obj.optString("name"));
					pt.setIconURL(obj.optString("iconURL"));
					pt.setHlIconURL(obj.optString("hlIconURL"));
					list.add(pt);
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

	public String getIconURL() {
		return iconURL;
	}

	public void setIconURL(String iconURL) {
		this.iconURL = iconURL;
	}

	public String getHlIconURL() {
		return hlIconURL;
	}

	public void setHlIconURL(String hlIconURL) {
		this.hlIconURL = hlIconURL;
	}

}
