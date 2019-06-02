package com.homework.teacher.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author zhangkc
 * @date 2017-7-17
 */
public class ProductDetailComment implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1014706996500283631L;
	private int evaLv;// 评分 1：差评，2：中评，3：好评
	private String evaDesc;// 评价
	private String nickName;// 客户昵称

	public ProductDetailComment() {

	}

	public ProductDetailComment(int evaLv, String evaDesc, String nickName) {
		this.evaLv = evaLv;
		this.evaDesc = evaDesc;
		this.nickName = nickName;
	}

	public static List<ProductDetailComment> parseJson(JSONObject jsonObject) {
		JSONArray jsonArray = jsonObject.optJSONArray("dataList");
		List<ProductDetailComment> list = new ArrayList<ProductDetailComment>();
		if (null != jsonArray && jsonArray.length() > 0) {
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject obj = jsonArray.optJSONObject(i);
				if (null != obj) {
					ProductDetailComment productDetailComment = new ProductDetailComment();
					productDetailComment.setEvaLv(obj.optInt("evaLv"));
					productDetailComment.setEvaDesc(obj.optString("evaDesc"));
					productDetailComment.setNickName(obj.optString("nickName"));
					list.add(productDetailComment);
				}
			}
		}
		return list;
	}

	public int getEvaLv() {
		return evaLv;
	}

	public void setEvaLv(int evaLv) {
		this.evaLv = evaLv;
	}

	public String getEvaDesc() {
		return evaDesc;
	}

	public void setEvaDesc(String evaDesc) {
		this.evaDesc = evaDesc;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

}