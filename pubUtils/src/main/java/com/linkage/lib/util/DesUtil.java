package com.linkage.lib.util;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import com.linkage.lib.util.base64.BASE64Decoder;
import com.linkage.lib.util.base64.BASE64Encoder;

public class DesUtil {

    private final static String DES = "DES";

    /**
     * Description 根据键值进行加密
     *
     * @param data 需要加密的字符串
     * @param key  加密键byte数组
     * @return 加密后的字符串
     * @throws Exception
     */
    public static String encrypt(String data, String key) throws Exception {
        byte[] bt = encrypt(data.getBytes(), key.getBytes());
        
        return  new BASE64Encoder().encode(bt);
    }

    /**
     * Description 根据键值进行解密
     *
     * @param data 需要解密的字符串
     * @param key  加密键byte数组
     * @return 解密后的字符串
     * @throws Exception
     */
    public static String decrypt(String data, String key) throws Exception {
        if (data == null) return null;
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] buf = decoder.decodeBuffer(data);
        byte[] bt = decrypt(buf, key.getBytes());
        return new String(bt);
    }

    /**
     * Description 根据键值进行加密
     *
     * @param data 需要加密的byte数组
     * @param key  加密键byte数组
     * @return byte[]
     * @throws Exception
     */
    private static byte[] encrypt(byte[] data, byte[] key) throws Exception {
        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);
        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);
        // Cipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance(DES);
        // 用密钥初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
        return cipher.doFinal(data);
    }

    /**
     * Description 根据键值进行解密
     *
     * @param data 需要解密的byte数组
     * @param key  加密键byte数组
     * @return byte[]
     * @throws Exception
     */
    private static byte[] decrypt(byte[] data, byte[] key) throws Exception {
        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);
        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);
        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance(DES);
        // 用密钥初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
        return cipher.doFinal(data);
    }

    public static void main(String [] args){
        String key="njxtqgjypt";
        String temp="刘洋是个好人！！";
        String jiami=null;
        String jiemi=null;
        try{
            jiami=DesUtil.encrypt(temp,key);
            System.out.println("加密="+jiami);
            jiemi=DesUtil.decrypt(jiami,key);
            System.out.println("解密="+jiemi);
        }catch(Exception e){
            e.printStackTrace();
        }


//        System.out.println(StringUtils.isNumeric("00912"));
////        System.out.println(System.currentTimeMillis());
//        Date date1=new Date(1399881898115l);
//        System.out.println(DateUtils.truncatedCompareTo(date1, new Date(), Calendar.MINUTE));
//        Date date2=DateUtils.addMinutes(date1,5);
//        Date date3=DateUtils.addMinutes(date1,300);
//        System.out.println(DateUtils.truncatedCompareTo(date2, new Date(), Calendar.MINUTE));
//        System.out.println(DateUtils.truncatedCompareTo(date3, new Date(), Calendar.MINUTE));

    }

}

