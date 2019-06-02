package com.homework.teacher.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author zhangkc
 * @date 2017-7-21
 */
public class ProductRecommend implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6479724270819200418L;
	private int weekDay;// 周几
	private String prdName;// 商品名称
	private double price;// 商品单价
	private int num;// 商品数量
	private int isTitle;// 是否是标题 1是0不是

	public ProductRecommend() {

	}

	public ProductRecommend(int weekDay, String prdName, double price, int num,
			int isTitle) {
		this.weekDay = weekDay;
		this.prdName = prdName;
		this.price = price;
		this.num = num;
		this.isTitle = isTitle;
	}

	public static List<ProductRecommend> parseJson(JSONArray jsonArray) {
		List<ProductRecommend> list = new ArrayList<ProductRecommend>();
		if (null != jsonArray && jsonArray.length() > 0) {
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject obj = jsonArray.optJSONObject(i);
				if (null != obj) {
					ProductRecommend productRecommend = new ProductRecommend();
					productRecommend.setWeekDay(obj.optInt("weekDay"));
					productRecommend.setPrdName(obj.optString("prdName"));
					productRecommend.setPrice(obj.optDouble("price"));
					productRecommend.setNum(obj.optInt("num"));
					productRecommend.setIsTitle(obj.optInt("isTitle"));
					list.add(productRecommend);
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

	public int getIsTitle() {
		return isTitle;
	}

	public void setIsTitle(int isTitle) {
		this.isTitle = isTitle;
	}

}
