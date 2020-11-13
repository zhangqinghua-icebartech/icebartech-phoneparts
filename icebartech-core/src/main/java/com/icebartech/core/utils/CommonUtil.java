package com.icebartech.core.utils;

import com.icebartech.core.constants.IcebartechConstants;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class CommonUtil {

    public final static String MD5(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] btInput = s.getBytes("UTF-8");
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String pwdMd5(String userPwd, String userPwd2) {
        return MD5((MD5(userPwd)
                + userPwd2 + "icebar@^&*%$#!%^"));
    }

    public static void main(String[] args) {
        String slat = RandomStringUtils.randomAlphanumeric(8);
        System.out.println(slat);
        System.out.println(pwdMd5("bg123456", slat));
    }

    public static boolean validatePwdMD5(String matchPwd, String password,
                                         String matchPwd2) {
        return matchPwd.equals(pwdMd5(password, matchPwd2));
    }


    /**
     * 把数组按照“参数=参数值”的模式用“&”字符拼接成字符串
     *
     * @param paramMap
     * @return
     */
    public static String createLinkString(Map<String, Object> paramMap) {
        List<String> keys = new ArrayList<String>(paramMap.keySet());

        StringBuffer rtnStr = new StringBuffer();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            Object value = paramMap.get(key);
            if (i == keys.size() - 1) {
                rtnStr.append(key + "=" + value);
            } else {
                rtnStr.append(key + "=" + value + "&");
            }
        }
        return rtnStr.toString();
    }

    /**
     * byte数组转换为char数组
     *
     * @param bytes
     * @return
     */
    public static char[] getChars(byte[] bytes) {
        Charset cs = Charset.forName("UTF-8");
        ByteBuffer bb = ByteBuffer.allocate(bytes.length);
        bb.put(bytes);
        bb.flip();
        CharBuffer cb = cs.decode(bb);
        return cb.array();
    }

    /**
     * char数组转换为byte数组
     *
     * @param chars
     * @return
     */
    public static byte[] getBytes(char[] chars) {
        Charset cs = Charset.forName("UTF-8");
        CharBuffer cb = CharBuffer.allocate(chars.length);
        cb.put(chars);
        cb.flip();
        ByteBuffer bb = cs.encode(cb);
        return bb.array();
    }

    /**
     * 获取文件名的后缀
     *
     * @param fileName
     * @return
     */
    public static String getFileSuffix(String fileName) {
        String rtn = null;
        if (StringUtils.isNotEmpty(fileName)) {
            int index = fileName.lastIndexOf(".");
            if (index > -1) {
                rtn = fileName.substring(index + 1);
            }
        }
        return rtn;
    }

    /**
     * 获取真实的分表表名后缀，格式如：_xxxx，会用leftPad来左补齐
     *
     * @param shardingId
     * @param by
     * @return
     */
    public static String getShardingTableSuffix(long shardingId, int by) {
        if (shardingId <= 0 || by <= 0) {
            throw new RuntimeException("shardingId or by is invalid!");
        } else if (by > 1024) {
            throw new RuntimeException("by should be less than 1024!");
        }
        //计算by是几位数
        int index = 1;
        int a = 0;
        int temp = by;
        while ((a = temp / 10) > 0) {
            index++;
            temp = a;
        }
        long realShardingId = shardingId % by;
        return "_" + StringUtils.leftPad(String.valueOf(realShardingId), index, '0');
    }

    /**
     * doble保留两位小数格式化后返回
     *
     * @param val
     * @return
     */
    public static Double formatDouble2(Double val) {
        DecimalFormat format = new DecimalFormat("#.00");
        String str = format.format(val);
        return Double.valueOf(str);
    }

    /**
     * doble保留三位小数格式化后返回
     *
     * @param val
     * @return
     */
    public static Double formatDouble3(Double val) {
        DecimalFormat format = new DecimalFormat("#.000");
        String str = format.format(val);
        return Double.valueOf(str);
    }

    /**
     * doble保留四位小数格式化后返回
     *
     * @param val
     * @return
     */
    public static Double formatDouble4(Double val) {
        DecimalFormat format = new DecimalFormat("#.0000");
        String str = format.format(val);
        return Double.valueOf(str);
    }

    /**
     * double转百分比表示法
     *
     * @param val
     * @return
     */
    public static String getPercent(Double val) {
        if (val == null) {
            val = 0d;
        }
        DecimalFormat df = new DecimalFormat("###.00%");
        return df.format(val);
    }

    /**
     * 简单转义html标签
     *
     * @param str
     * @return
     */
    public static String escapeHtmlSimple(String str) {
        str = str.replaceAll("&", "&amp;");
        str = str.replaceAll("<", "&lt;");
        str = str.replaceAll(">", "&gt;");
        str = str.replaceAll("\"", "&quot;");
        return str;
    }

    /**
     * 判断是否为低版本ie浏览器
     *
     * @param userAgent
     * @return
     */
    public static boolean isMSIE(String userAgent) {
        if (StringUtils.isNotBlank(userAgent) && StringUtils.isNotBlank(userAgent) && userAgent.indexOf("MSIE") > -1 && userAgent.indexOf("Opera") < 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 通过userAgent来判断是微信的请求头
     *
     * @param userAgent
     * @return
     */
    public static boolean isMicroMessenger(String userAgent) {
        if (StringUtils.isNotBlank(userAgent) && StringUtils.isNotBlank(userAgent) && userAgent.contains(IcebartechConstants.WEIXIN_USER_AGENT)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断请求是否来自于手机端
     *
     * @param userAgent
     * @return
     */
    public static boolean isMobile(String userAgent) {
        boolean isMoblie = false;
        String[] mobileAgents = {"iphone", "android", "phone", "mobile",
                "wap", "netfront", "java", "opera mobi", "opera mini", "ucweb",
                "windows ce", "symbian", "series", "webos", "sony",
                "blackberry", "dopod", "nokia", "samsung", "palmsource", "xda",
                "pieplus", "meizu", "midp", "cldc", "motorola", "foma",
                "docomo", "up.browser", "up.link", "blazer", "helio", "hosin",
                "huawei", "novarra", "coolpad", "webos", "techfaith",
                "palmsource", "alcatel", "amoi", "ktouch", "nexian",
                "ericsson", "philips", "sagem", "wellcom", "bunjalloo", "maui",
                "smartphone", "iemobile", "spice", "bird", "zte-", "longcos",
                "pantech", "gionee", "portalmmm", "jig browser", "hiptop",
                "benq", "haier", "^lct", "320x320", "240x320", "176x220",
                "w3c ", "acs-", "alav", "alca", "amoi", "audi", "avan", "benq",
                "bird", "blac", "blaz", "brew", "cell", "cldc", "cmd-", "dang",
                "doco", "eric", "hipt", "inno", "ipaq", "java", "jigs", "kddi",
                "keji", "leno", "lg-c", "lg-d", "lg-g", "lge-", "maui", "maxo",
                "midp", "mits", "mmef", "mobi", "mot-", "moto", "mwbp", "nec-",
                "newt", "noki", "oper", "palm", "pana", "pant", "phil", "play",
                "port", "prox", "qwap", "sage", "sams", "sany", "sch-", "sec-",
                "send", "seri", "sgh-", "shar", "sie-", "siem", "smal", "smar",
                "sony", "sph-", "symb", "t-mo", "teli", "tim-", /*"tosh",*/ "tsm-",
                "upg1", "upsi", "vk-v", "voda", "wap-", "wapa", "wapi", "wapp",
                "wapr", "webc", "winw", "winw", "xda", "xda-", "Googlebot-Mobile", IcebartechConstants.WEIXIN_USER_AGENT};
        for (String mobileAgent : mobileAgents) {
            if (userAgent.toLowerCase().indexOf(mobileAgent.toLowerCase()) >= 0) {
                isMoblie = true;
                break;
            }
        }
        return isMoblie;
    }

    /**
     * 随机红包简单算法
     *
     * @param remainMoney 剩余的钱
     * @param remainSize  剩余的红包数量
     * @return
     */
    public static double getRandomMoney(double remainMoney, int remainSize) {
        // remainSize 剩余的红包数量
        // remainMoney 剩余的钱
        if (remainSize == 1) {
            remainSize--;
            return (double) Math.round(remainMoney * 100) / 100;
        }
        Random r = new Random();
        double min = 0.01;
        double max = remainMoney / remainSize * 2;
        double money = r.nextDouble() * max;
        money = money <= min ? 0.01 : money;
        money = Math.floor(money * 100) / 100d;
        remainSize--;
        // remainMoney -= money;
        return money;
    }

    /**
     * 将手机密码处理成135****1067的形式
     *
     * @param str
     * @return
     */
    public static String encodeMobile(String str) {
        if (StringUtils.isBlank(str) || str.length() != 11) {
            return str;
        }
        return str.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

    private static void toStr(String sessionKey, String encryptedData, String iv) throws Exception {
        byte[] sessionKeyBy = BASE64.decryptBASE64(sessionKey.getBytes("UTF-8"));
        byte[] encryptedDataBy = BASE64.decryptBASE64(encryptedData.getBytes("UTF-8"));
        byte[] ivBy = BASE64.decryptBASE64(iv.getBytes("UTF-8"));
        byte[] dec = Pkcs7Encoder.decryptOfDiyIV(encryptedDataBy, sessionKeyBy, ivBy);
        System.out.println(new String(dec, "UTF-8"));


    }
}
