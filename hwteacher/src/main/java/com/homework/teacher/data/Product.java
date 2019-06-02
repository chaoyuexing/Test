package com.homework.teacher.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class Product implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1718331899925619820L;
	private int id;// 城市产品ID
	private int prdId;// 产品ID
	private String prdName;// 产品名称
	private double price;// 售价（元）
	private int sellNum;// 月销量
	private double favRate;// 好评率
	private String thumbnailURL;// 缩略图
	private boolean isAddToCookBook;// 是否添加到一周食谱

	public Product() {

	}

	public Product(int id, int prdId, String prdName, double price,
			int sellNum, double favRate, String thumbnailURL) {
		this.id = id;
		this.prdId = prdId;
		this.prdName = prdName;
		this.price = price;
		this.sellNum = sellNum;
		this.favRate = favRate;
		this.thumbnailURL = thumbnailURL;
		// this.isAddToCookBook = isAddToCookBook;
	}

	public static List<Product> parseJson(JSONArray jsonArray) {
		List<Product> list = new ArrayList<Product>();
		if (null != jsonArray && jsonArray.length() > 0) {
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject obj = jsonArray.optJSONObject(i);
				if (null != obj) {
					Product product = new Product();
					product.setId(obj.optInt("id"));
					product.setPrdId(obj.optInt("prdId"));
					product.setPrdName(obj.optString("prdName"));
					product.setPrice(obj.optDouble("price"));
					product.setSellNum(obj.optInt("sellNum"));
					product.setFavRate(obj.optDouble("favRate"));
					product.setThumbnailURL(obj.optString("thumbnailURL"));
					// product.setAddToCookBook(obj.optBoolean("isAddToCookBook"));
					list.add(product);
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

	public int getSellNum() {
		return sellNum;
	}

	public void setSellNum(int sellNum) {
		this.sellNum = sellNum;
	}

	public double getFavRate() {
		return favRate;
	}

	public void setFavRate(double favRate) {
		this.favRate = favRate;
	}

	public String getThumbnailURL() {
		return thumbnailURL;
	}

	public void setThumbnailURL(String thumbnailURL) {
		this.thumbnailURL = thumbnailURL;
	}

	public boolean isAddToCookBook() {
		return isAddToCookBook;
	}

	public void setAddToCookBook(boolean isAddToCookBook) {
		this.isAddToCookBook = isAddToCookBook;
	}

}
