package com.homework.teacher.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class OrderState implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5252271659417906227L;
	private int state;// 订单状态 0待提交， 1：待支付，2：制作中，3：待领取，4：待点评，5：已完成，-1：已退订
	private String stateName;// 订单状态名称
	private int num;// 数量 只取未逻辑删除的 数量为0的后台照常返回，前台显示状态标签，但不显示数量

	public OrderState() {

	}

	public OrderState(int state, String stateName, int num) {
		this.state = state;
		this.stateName = stateName;
		this.num = num;
	}

	public static List<OrderState> parseJson(JSONArray jsonArray) {
		List<OrderState> list = new ArrayList<OrderState>();
		if (null != jsonArray && jsonArray.length() > 0) {
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject obj = jsonArray.optJSONObject(i);
				if (null != obj) {
					OrderState os = new OrderState();
					os.setState(obj.optInt("state"));
					os.setStateName(obj.optString("stateName"));
					os.setNum(obj.optInt("num"));
					list.add(os);
				}
			}
		}
		return list;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

}
