package com.icebartech.phoneparts.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * @author Created by liuao on 2019/8/28.
 * @desc
 */
public class ProduceCodeUtil {


    /**
     * 生产兑换码
     * @return 兑换码
     */
    public static String findRedeemCode(){
        return findDateNum() + findChar(6);
    }


    /**
     * 生成序列号
     * @param title 标题
     * @return 序列号
     */
    public static String findSerialNum(String title){
        return title + findDateNum("yyyyMM") + findChar(6);
    }

    /**
     * 获取时间数字串 20201213
     * @return 数字串
     */
    public static String findDateNum(){

        Date data = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
        return sf.format(data);
    }


    /**
     * 获取时间数字串 1213
     * @return 数字串
     */
    public static String findDateNum(String format){
        Date data = new Date();
        SimpleDateFormat sf = new SimpleDateFormat(format);
        return sf.format(data);
    }

    /**
     * 随机数字和字母串，不要字母O，因为跟0很相似
     * @param length 长度
     * @return 字符串
     */
    public static String findChar(int length){
        StringBuilder str = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            boolean b = random.nextBoolean();
            if (b) { // 字符串
                // int choice = random.nextBoolean() ? 65 : 97; 取得65大写字母还是97小写字母
                str.append((char) (65 + random.nextInt(26)));// 取得大写字母
            } else { // 数字
                str.append(random.nextInt(10));
            }
        }
        return str.toString().replace("O", "0");
    }

    public static void main(String[] args) {
        System.out.println(findRedeemCode());
    }

}
