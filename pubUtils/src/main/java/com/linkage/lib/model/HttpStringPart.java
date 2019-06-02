package com.linkage.lib.model;

/**
 * @author 方达
 * @param key
 * @param value
 * 描述http中String类的参数 用且仅用于LinkedList的插入与迭代操作。
 * 因考虑到涉及数据加密等操作，此类私有化set接口。
 */
public class HttpStringPart
{
	private static final String EMPTY_STRING = "";
	
	private String key;
	private String value;
	
	
	public HttpStringPart(String key, String value) {
		this.key = key;
		if (value == null) {
			value = EMPTY_STRING;
		}
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

}
