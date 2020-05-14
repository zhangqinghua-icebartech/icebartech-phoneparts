package com.icebartech.core.utils;

import com.icebartech.core.utils.wxpay.WXPayUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 微信支付工具类
 *
 * @author Administrator
 */
public class WeiXinPayUtil {

    /**
     * 获得微信支付md5签名后的sign值 32位md5
     *
     * @param apiSecret
     * @param params    微信支付要求的所有非空参数
     * @return
     */
    public static String getSign(String apiSecret, Map<String, String> params) {
        StringBuilder prestr = new StringBuilder();
        if (StringUtils.isEmpty(apiSecret)) {
            throw new RuntimeException("param [apiSecret] can not be null!");
        }
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);

        for (String key : keys) {
            String value = params.get(key);
            prestr.append(key).append("=").append(value).append("&");
        }
        //末尾加上apiSecret
        prestr.append("key=").append(apiSecret);
        //进行md5加密
        String md5Sign = MD5.sign(prestr.toString());
        //转换成大写
        return md5Sign.toUpperCase();
    }

    /**
     * 除去数组中的空值和签名参数
     *
     * @param sArray 签名参数组
     * @return 去掉空值与签名参数后的新签名参数组
     */
    public static Map<String, String> paramFilter(Map<String, String> sArray) {
        Map<String, String> result = new HashMap<>();
        if (sArray == null || sArray.size() <= 0) {
            return result;
        }
        for (Map.Entry<String, String> e : sArray.entrySet()) {
            String value = e.getValue();
            String key = e.getKey();
            if (value == null || "".equals(value) || "sign".equalsIgnoreCase(key)) {
                continue;
            }
            result.put(key, value);
        }
        return result;
    }

    /**
     * 随机获取32位数字与字母的组合
     *
     * @return
     */
    public static String getNonceStr() {
        return RandomStringUtils.randomAlphanumeric(32);
    }

    /**
     * 获取本机ip
     *
     * @return
     */
    public static String getMachineIp() {
        String ip = "127.0.0.1";
        try {
            InetAddress address = InetAddress.getLocalHost();
            String hostIp = address.getHostAddress();
            if (StringUtils.isNotEmpty(hostIp)) {
                ip = hostIp;
            }
        } catch (UnknownHostException e) {

        }
        return ip;
    }

    /**
     * 把微信支付返回的xml转换成map返回
     *
     * @param xml
     * @return
     */
    public static Map<String, String> parseXmlToMap(String xml) {
        try {
            return WXPayUtil.xmlToMap(xml);
        } catch (Exception e) {
            return null;
        }
    }

    public static String timeofWeiXinPay(LocalDateTime date) {
        return date.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }

}
