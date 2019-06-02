package com.homework.teacher.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author zhangkc
 * @date 2017-7-19
 */
public class RecommendName implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5678863389170457014L;
	private int recId;// 推荐食谱ID
	private String recName;// 推荐食谱名称

	public RecommendName() {

	}

	public RecommendName(int recId, String recName) {
		this.recId = recId;
		this.recName = recName;
	}

	public static List<RecommendName> parseJson(JSONArray jsonArray) {
		List<RecommendName> list = new ArrayList<RecommendName>();
		if (null != jsonArray && jsonArray.length() > 0) {
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject obj = jsonArray.optJSONObject(i);
				if (null != obj) {
					RecommendName recommendName = new RecommendName();
					recommendName.setRecId(obj.optInt("recId"));
					recommendName.setRecName(obj.optString("recName"));
					list.add(recommendName);
				}
			}
		}
		return list;
	}

	public int getRecId() {
		return recId;
	}

	public void setRecId(int recId) {
		this.recId = recId;
	}

	public String getRecName() {
		return recName;
	}

	public void setRecName(String recName) {
		this.recName = recName;
	}

}
