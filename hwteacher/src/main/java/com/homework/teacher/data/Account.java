package com.homework.teacher.data;

import java.io.Serializable;
import java.util.List;

import org.json.JSONObject;

public class Account implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7743823660041924963L;
	private int id;// 客户ID
	private String nickName;// 昵称
	private int gender;// 性别
	private String birthday;// 生日
	private String loginName;// 登录名
	private String mobileNo;// 绑定手机
	private String email;// 绑定邮箱
	private int weChat;// 微信OpenID
	private int qq;// QQ
	private String portraitURL;// 头像
	private int loginPwdFlag;// 登录密码设置状态 0：未设置，1：已设置
	private int payPwdFlag;// 提现密码设置状态 0：未设置，1：已设置
	private int integral;// 积分
	private double cash;// 余额（元）
	private List<Voucher> voucherList;// 代金券

	public Account() {

	}

	public static Account parseJson(JSONObject jsonObject) {
		Account account = new Account();
		JSONObject obj = jsonObject;
		if (null != obj) {
			account.setId(obj.optInt("id"));
			account.setNickName(obj.optString("nickName"));
			account.setGender(obj.optInt("gender"));
			account.setBirthday(obj.optString("birthday"));
			account.setLoginName(obj.optString("loginName"));
			account.setMobileNo(obj.optString("mobileNo"));
			account.setEmail(obj.optString("email"));
			account.setWeChat(obj.optInt("weChat"));
			account.setQq(obj.optInt("qq"));
			account.setPortraitURL(obj.optString("portraitURL"));
			account.setLoginPwdFlag(obj.optInt("loginPwdFlag"));
			account.setPayPwdFlag(obj.optInt("payPwdFlag"));
			account.setIntegral(obj.optInt("integral"));
			account.setCash(obj.optDouble("cash"));
			account.setVoucherList(Voucher.parseJson(obj
					.optJSONArray("voucherList")));
		}
		return account;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getWeChat() {
		return weChat;
	}

	public void setWeChat(int weChat) {
		this.weChat = weChat;
	}

	public int getQq() {
		return qq;
	}

	public void setQq(int qq) {
		this.qq = qq;
	}

	public String getPortraitURL() {
		return portraitURL;
	}

	public void setPortraitURL(String portraitURL) {
		this.portraitURL = portraitURL;
	}

	public int getLoginPwdFlag() {
		return loginPwdFlag;
	}

	public void setLoginPwdFlag(int loginPwdFlag) {
		this.loginPwdFlag = loginPwdFlag;
	}

	public int getPayPwdFlag() {
		return payPwdFlag;
	}

	public void setPayPwdFlag(int payPwdFlag) {
		this.payPwdFlag = payPwdFlag;
	}

	public int getIntegral() {
		return integral;
	}

	public void setIntegral(int integral) {
		this.integral = integral;
	}

	public double getCash() {
		return cash;
	}

	public void setCash(double cash) {
		this.cash = cash;
	}

	public List<Voucher> getVoucherList() {
		return voucherList;
	}

	public void setVoucherList(List<Voucher> voucherList) {
		this.voucherList = voucherList;
	}

}
