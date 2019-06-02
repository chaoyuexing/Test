package com.homework.teacher.utils;

import java.io.ByteArrayOutputStream;
import android.util.Base64;
import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

/**
 * RAS加密工具 Created by jiangye on 2016/11/15.
 */
public class RSAUtils {
	/**
	 * 加密算法RSA
	 */
	public static final String KEY_ALGORITHM = "RSA";

	/**
	 * 获取公钥的key
	 */
	private static final String PUBLIC_KEY = "RSAPublicKey";

	/**
	 * 获取私钥的key
	 */
	private static final String PRIVATE_KEY = "RSAPrivateKey";

	/**
	 * RSA最大加密明文大小
	 */
	private static final int MAX_ENCRYPT_BLOCK = 117;

	/**
	 * RSA最大解密密文大小
	 */
	private static final int MAX_DECRYPT_BLOCK = 128;

	public static final String PUB_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDLvlXbz8Afcz+4SaJ+N06ZV5DpbnkJHbwdZINfhl6T6DlVqL+usx92Q5OuXOEJ7zP3QQZNXWXdsgPoOJC7eqy0qU9xBiNMb8KRhf0g3KXhj5F7jC8TBWL0IZwCvb1ZpUJjOWJcVhvRKyR4ohH5R7J1wZOblzyaI3//nrD4oWcE/wIDAQAB";
	public static final String PRI_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMu+VdvPwB9zP7hJon43TplXkOlueQkdvB1kg1+GXpPoOVWov66zH3ZDk65c4QnvM/dBBk1dZd2yA+g4kLt6rLSpT3EGI0xvwpGF/SDcpeGPkXuMLxMFYvQhnAK9vVmlQmM5YlxWG9ErJHiiEflHsnXBk5uXPJojf/+esPihZwT/AgMBAAECgYA5OWlWBqC4dJ81/ICGILSTvxN1bNDT7aUTQFLhtpTDyBqqY24K/EZe6hYFiBKwvp2C2aGSgyQEQ2Zzh8em0IrG5uESJTxYi8vfcoIFxHH/MvmomRHxrJAXfif7fbo55C4yw9XwH+kW7GqnqdCQTWzqcEO2sr6VXHuNiXaDeXb2oQJBAOlLHqsmbIX6bfWISQbTS8OLUrsOP5kQ0Ctn7EXrP97dC8j8NUQiMBReGJrKh1IHdjhOgAKkofiYwNEjtTe42E8CQQDfku5zpz9m4E6lLCS5hgq0t3Yetkg6L8mdD+QK5izEbwIZwgONSswVrpsL4R/K9Eev51B6qmjcdvtO8ubtMyxRAkBwzrwzsk7Dska9dXsqa720/oewn8Es+K5Qbt4XKRwnXee29g2jtoxdLOrk3o30olsN+xd3L7iKhwKlnoigOyeNAkEA3s2PgoQW5A//cA2I37YcLzJxrXOckRgUhMhUSyRNUskJv9+gKIt0zNq/utv0Eg7U7hDIHCZ879BG68uXpKUNsQJAYjQHvCwIq8x9mhSJxM/+sas2QXci5uhz2SXOHcvHAE7wgdCugJmCv6KlTU+d/MZnGtQILvm368tBBVepKVK/9g==";

	// private static final Map<String, Object> keyMap = new HashMap<>();
	/*
	 * static{ KeyPairGenerator keyPairGen = null; try { keyPairGen =
	 * KeyPairGenerator.getInstance(KEY_ALGORITHM); keyPairGen.initialize(1024);
	 * KeyPair keyPair = keyPairGen.generateKeyPair(); RSAPublicKey publicKey =
	 * (RSAPublicKey) keyPair.getPublic(); RSAPrivateKey privateKey =
	 * (RSAPrivateKey) keyPair.getPrivate(); keyMap.put(PUBLIC_KEY, publicKey);
	 * keyMap.put(PRIVATE_KEY, privateKey); } catch (NoSuchAlgorithmException e)
	 * { e.printStackTrace(); } }
	 */

	/**
	 * <P>
	 * 私钥解密
	 * </p>
	 * 
	 * @param encryptedData
	 *            已加密数据
	 * @param privateKey
	 *            私钥(BASE64编码)
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptByPrivateKey(byte[] encryptedData,
			String privateKey) throws Exception {
		byte[] keyBytes = Base64.decode(privateKey, Base64.NO_WRAP);
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
		// Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		// 服务器端jdk 和 android上的jdk 对RSA的缺省算法实现不同。
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.DECRYPT_MODE, privateK);
		int inputLen = encryptedData.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;
		// 对数据分段解密
		while (inputLen - offSet > 0) {
			if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
				cache = cipher
						.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
			} else {
				cache = cipher
						.doFinal(encryptedData, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * MAX_DECRYPT_BLOCK;
		}
		byte[] decryptedData = out.toByteArray();
		out.close();
		return decryptedData;
	}

	/**
	 * <p>
	 * 公钥加密
	 * </p>
	 * 
	 * @param data
	 *            源数据
	 * @param publicKey
	 *            公钥(BASE64编码)
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptByPublicKey(byte[] data, String publicKey)
			throws Exception {
		byte[] keyBytes = Base64.decode(publicKey, Base64.NO_WRAP);
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key publicK = keyFactory.generatePublic(x509KeySpec);
		// 对数据加密
		// Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		// 服务器端jdk 和 android上的jdk 对RSA的缺省算法实现不同。
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.ENCRYPT_MODE, publicK);
		int inputLen = data.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;
		// 对数据分段加密
		while (inputLen - offSet > 0) {
			if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
				cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
			} else {
				cache = cipher.doFinal(data, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * MAX_ENCRYPT_BLOCK;
		}
		byte[] encryptedData = out.toByteArray();
		out.close();
		return encryptedData;
	}

}
