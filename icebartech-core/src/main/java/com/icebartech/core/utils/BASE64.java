package com.icebartech.core.utils;

import org.apache.commons.codec.binary.Base64;

/**
 * BASE64加密解密
 */
public class BASE64 {
    /**
     * BASE64解密 * @param key * @return * @throws Exception
     */
    public static byte[] decryptBASE64(byte[] key) throws Exception {
        return (new Base64()).decode(key);
    }

    /**
     * BASE64加密 * @param key * @return * @throws Exception
     */
    public static byte[] encryptBASE64(byte[] key) throws Exception {
        return (new Base64()).encode(key);
    }

    public static void main(String[] args) throws Exception {
        byte[] data = BASE64.encryptBASE64("http://aub.iteye.com/".getBytes("UTF-8"));
        System.out.println("加密前：" + new String(data, "UTF-8"));
        byte[] byteArray = BASE64.decryptBASE64(data);
        System.out.println("解密后：" + new String(byteArray, "UTF-8"));
    }
} 