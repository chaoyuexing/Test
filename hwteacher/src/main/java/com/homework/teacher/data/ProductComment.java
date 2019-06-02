package com.homework.teacher.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author zhangkc
 * @date 2017-7-11
 */
public class ProductComment implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3407446699005148210L;
	private String prdName;
	private int ordId;
	private int cityPrdId;
	private int prdId;
	private int prdEvaluationLv;// 产品评分 1：差评，2：中评，3：好评
	private String prdEvaluationDesc;// 产品评价

	public ProductComment() {

	}

	public ProductComment(String prdName, int ordId, int cityPrdId, int prdId,
			int prdEvaluationLv, String prdEvaluationDesc) {
		this.prdName = prdName;
		this.ordId = ordId;
		this.cityPrdId = cityPrdId;
		this.prdId = prdId;
		this.prdEvaluationLv = prdEvaluationLv;
		this.prdEvaluationDesc = prdEvaluationDesc;
	}

	public static List<ProductComment> parseJson(JSONObject jsonObject) {
		List<ProductComment> list = new ArrayList<ProductComment>();
		ProductComment productComment1 = new ProductComment();
		productComment1.setPrdName("综合评价");
		list.add(productComment1);
		ProductComment productComment2 = new ProductComment();
		productComment2.setPrdName("配送评价");
		list.add(productComment2);

		int ordId = jsonObject.optInt("ordId");
		JSONArray jsonArray = jsonObject.optJSONArray("prdList");
		if (null != jsonArray && jsonArray.length() > 0) {
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject obj = jsonArray.optJSONObject(i);
				if (null != obj) {
					ProductComment productComment = new ProductComment();
					productComment.setPrdName(obj.optString("prdName"));
					productComment.setOrdId(ordId);
					productComment.setCityPrdId(obj.optInt("cityPrdId"));
					productComment.setPrdId(obj.optInt("prdId"));
					list.add(productComment);
				}
			}
		}
		return list;
	}

	public String getPrdName() {
		return prdName;
	}

	public void setPrdName(String prdName) {
		this.prdName = prdName;
	}

	public int getOrdId() {
		return ordId;
	}

	public void setOrdId(int ordId) {
		this.ordId = ordId;
	}

	public int getCityPrdId() {
		return cityPrdId;
	}

	public void setCityPrdId(int cityPrdId) {
		this.cityPrdId = cityPrdId;
	}

	public int getPrdId() {
		return prdId;
	}

	public void setPrdId(int prdId) {
		this.prdId = prdId;
	}

	public int getPrdEvaluationLv() {
		return prdEvaluationLv;
	}

	public void setPrdEvaluationLv(int prdEvaluationLv) {
		this.prdEvaluationLv = prdEvaluationLv;
	}

	public String getPrdEvaluationDesc() {
		return prdEvaluationDesc;
	}

	public void setPrdEvaluationDesc(String prdEvaluationDesc) {
		this.prdEvaluationDesc = prdEvaluationDesc;
	}

}