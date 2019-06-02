package com.homework.teacher.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class MealPlace1 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3567457241024615403L;
	private String id;
	private String mealPlaceName;

	public MealPlace1() {

	}

	public MealPlace1(String mealPlaceName) {
		this.mealPlaceName = mealPlaceName;
	}

	public static List<MealPlace1> parseJson(JSONObject json) {
		List<MealPlace1> list = new ArrayList<MealPlace1>();
		JSONArray array = json.optJSONArray("data");
		if (null != array && array.length() > 0) {
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.optJSONObject(i);
				if (null != obj) {
					MealPlace1 mp = new MealPlace1();
					mp.setId(String.valueOf(obj.optLong("id")));
					mp.setMealPlaceName(obj.optString("mealPlaceName"));
					list.add(mp);
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

	public String getMealPlaceName() {
		return mealPlaceName;
	}

	public void setMealPlaceName(String mealPlaceName) {
		this.mealPlaceName = mealPlaceName;
	}

}