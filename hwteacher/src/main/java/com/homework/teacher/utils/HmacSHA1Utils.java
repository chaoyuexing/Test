package com.homework.teacher.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * HmacSHA1加密算法工具类
 *
 * @author zhangkc
 */
public class HmacSHA1Utils {
    //	private static final String APPSecret = "d3dbb841e493dac5b6ec061fe33242e1";
    public static final String APPSecret = "b5e33f9789b14eb86c8abd963b382cea81cc5142";
    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

    //vsichu的算法方法
    public static byte[] getHmacSHA1(String src)
            throws NoSuchAlgorithmException, UnsupportedEncodingException,
            InvalidKeyException {
        Mac mac = Mac.getInstance("HmacSHA1");
        SecretKeySpec secret = new SecretKeySpec(APPSecret.getBytes("UTF-8"),
                mac.getAlgorithm());
        mac.init(secret);
        return mac.doFinal(src.getBytes());
    }

    //homework的算法方法
    public static byte[] hwGetHmacSHA1(String str, String secret) {
        byte[] rawHmac = null;
        try {
            SecretKeySpec signinKey = new SecretKeySpec(secret.getBytes(), HMAC_SHA1_ALGORITHM);
            Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            mac.init(signinKey);
            rawHmac = mac.doFinal(str.getBytes());
//            String result = EncodeUtil.encodeBase64(rawHmac);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return rawHmac;
    }
}
