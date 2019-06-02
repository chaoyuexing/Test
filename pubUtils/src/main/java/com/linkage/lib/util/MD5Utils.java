package com.linkage.lib.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {

	/**
	 * md5加密
	 * 
	 * @param str
	 *            字符串
	 * @return 经过md5处理后的字符串
	 */
	public static String md5(String str) {
		try {
			return getMD5(str.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("UnsupportedEncoding UTF-8");
		}
	}

	/**
	 * md5加密
	 * 
	 * @param source
	 *            字符串
	 * @return 经过md5处理后的字符串
	 */
	private static String getMD5(byte[] source) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			StringBuffer result = new StringBuffer();
			for (byte b : md5.digest(source)) {
				result.append(Integer.toHexString((b & 0xf0) >>> 4));
				result.append(Integer.toHexString(b & 0x0f));
			}
			return result.toString().toUpperCase();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("不支持MD5算法");
		}
	}
	
}
