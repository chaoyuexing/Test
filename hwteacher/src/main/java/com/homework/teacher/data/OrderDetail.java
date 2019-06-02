package com.homework.teacher.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class OrderDetail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4695528710188868338L;
	private int ordId;// 订单号
	private String dinnerDate;// 用餐日期
	private int dayType;// 日期类型 1：节假日，2：工作日
	private int mcId;// 餐别Id
	private String mcName;// 餐别
	private int ttId;// 取餐时间Id
	private String ttValue;// 取餐时间
	private int tpId;// 取餐点Id
	private String tpName;// 取餐点
	private int dinnerNum;// 用餐人数
	private double totalPrice;// 订单总价
	private String fetchCode;// 取餐码

	private int state;// 订单状态 0待提交， 1：待支付，2：制作中，3：待领取，4：待点评，5：已完成，-1：已退订

	public OrderDetail() {

	}

	public OrderDetail(int ordId, String dinnerDate, int dayType, int mcId,
			String mcName, int ttId, String ttValue, int tpId, String tpName,
			int dinnerNum, double totalPrice, String fetchCode, int state) {
		this.ordId = ordId;
		this.dinnerDate = dinnerDate;
		this.dayType = dayType;
		this.mcId = mcId;
		this.mcName = mcName;
		this.ttId = ttId;
		this.ttValue = ttValue;
		this.tpId = tpId;
		this.tpName = tpName;
		this.dinnerNum = dinnerNum;
		this.totalPrice = totalPrice;
		this.fetchCode = fetchCode;
		this.state = state;
	}

	public static List<OrderDetail> parseJson(JSONObject jsonObject) {
		JSONArray jsonArray = jsonObject.optJSONArray("dataList");
		List<OrderDetail> list = new ArrayList<OrderDetail>();
		if (null != jsonArray && jsonArray.length() > 0) {
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject obj = jsonArray.optJSONObject(i);
				if (null != obj) {
					OrderDetail orderDetail = new OrderDetail();
					orderDetail.setOrdId(obj.optInt("ordId"));
					orderDetail.setDinnerDate(obj.optString("dinnerDate"));
					orderDetail.setDayType(obj.optInt("dayType"));
					orderDetail.setMcId(obj.optInt("mcId"));
					orderDetail.setMcName(obj.optString("mcName"));
					orderDetail.setTtId(obj.optInt("ttId"));
					orderDetail.setTtValue(obj.optString("ttValue"));
					orderDetail.setTpId(obj.optInt("tpId"));
					orderDetail.setTpName(obj.optString("tpName"));
					orderDetail.setDinnerNum(obj.optInt("dinnerNum"));
					orderDetail.setTotalPrice(obj.optDouble("totalPrice"));
					orderDetail.setFetchCode(obj.optString("fetchCode"));
					list.add(orderDetail);
				}
			}
		}
		return list;
	}

	public int getOrdId() {
		return ordId;
	}

	public void setOrdId(int ordId) {
		this.ordId = ordId;
	}

	public String getDinnerDate() {
		return dinnerDate;
	}

	public void setDinnerDate(String dinnerDate) {
		this.dinnerDate = dinnerDate;
	}

	public int getDayType() {
		return dayType;
	}

	public void setDayType(int dayType) {
		this.dayType = dayType;
	}

	public int getMcId() {
		return mcId;
	}

	public void setMcId(int mcId) {
		this.mcId = mcId;
	}

	public String getMcName() {
		return mcName;
	}

	public void setMcName(String mcName) {
		this.mcName = mcName;
	}

	public int getTtId() {
		return ttId;
	}

	public void setTtId(int ttId) {
		this.ttId = ttId;
	}

	public String getTtValue() {
		return ttValue;
	}

	public void setTtValue(String ttValue) {
		this.ttValue = ttValue;
	}

	public int getTpId() {
		return tpId;
	}

	public void setTpId(int tpId) {
		this.tpId = tpId;
	}

	public String getTpName() {
		return tpName;
	}

	public void setTpName(String tpName) {
		this.tpName = tpName;
	}

	public int getDinnerNum() {
		return dinnerNum;
	}

	public void setDinnerNum(int dinnerNum) {
		this.dinnerNum = dinnerNum;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getFetchCode() {
		return fetchCode;
	}

	public void setFetchCode(String fetchCode) {
		this.fetchCode = fetchCode;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

}