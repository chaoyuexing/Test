package com.homework.teacher.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class ProductChoose implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1121287133876556543L;
	private int prdId;// 商品ID
	private String prdName;// 商品名称
	private double price;// 单价
	private String thumbnailURL;// 缩略图
	private String meals;// 餐别 此商品在一周食谱中的所选餐别，“　”分隔

	public ProductChoose() {

	}

	public ProductChoose(int prdId, String prdName, double price,
			String thumbnailURL, String meals) {
		this.prdId = prdId;
		this.prdName = prdName;
		this.price = price;
		this.thumbnailURL = thumbnailURL;
		this.meals = meals;
	}

	public static List<ProductChoose> parseJson(JSONArray jsonArray) {
		List<ProductChoose> list = new ArrayList<ProductChoose>();
		if (null != jsonArray && jsonArray.length() > 0) {
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject obj = jsonArray.optJSONObject(i);
				if (null != obj) {
					ProductChoose productChoose = new ProductChoose();
					productChoose.setPrdId(obj.optInt("prdId"));
					productChoose.setPrdName(obj.optString("prdName"));
					productChoose.setPrice(obj.optDouble("price"));
					productChoose
							.setThumbnailURL(obj.optString("thumbnailURL"));
					productChoose.setMeals(obj.optString("meals"));
					list.add(productChoose);
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

	public String getThumbnailURL() {
		return thumbnailURL;
	}

	public void setThumbnailURL(String thumbnailURL) {
		this.thumbnailURL = thumbnailURL;
	}

	public String getMeals() {
		return meals;
	}

	public void setMeals(String meals) {
		this.meals = meals;
	}

}
