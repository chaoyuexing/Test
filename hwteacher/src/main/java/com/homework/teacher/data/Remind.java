package com.homework.teacher.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class Remind implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8690639398672447070L;
	private String id;
	private String value;

	public Remind() {

	}

	public static List<Remind> parseJson(JSONArray jsonArray) {
		List<Remind> list = new ArrayList<Remind>();
		if (null != jsonArray && jsonArray.length() > 0) {
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject obj = jsonArray.optJSONObject(i);
				if (null != obj) {
					Remind remind = new Remind();
					remind.setId(String.valueOf(obj.optLong("id")));
					remind.setValue(obj.optString("value"));
					list.add(remind);
				}
			}
		}
		return list;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
