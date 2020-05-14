package com.icebartech.core.utils;

import lombok.extern.slf4j.Slf4j;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.springframework.util.Assert;

/**
 * 汉字转换成拼音工具类
 *
 * @author Administrator
 */
@Slf4j
public class Cn2PinyinUtil {

    /**
     * 转换每个汉字的首字母为拼音
     *
     * @param cn 汉字
     * @return
     */
    public static String converter2FirstPinyin(String cn) {
        Assert.notNull(cn, "cn can not be null!");
        StringBuffer pinyinName = new StringBuffer();
        try {
            char[] nameChar = cn.toCharArray();
            HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
            defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
            defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
            for (int i = 0; i < nameChar.length; i++) {
                if (nameChar[i] > 128) {
                    try {
                        pinyinName.append(PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat)[0].charAt(0));
                    } catch (BadHanyuPinyinOutputFormatCombination e) {
                        return null;
                    }
                } else {
                    pinyinName.append(nameChar[i]);
                }
            }
        } catch (Exception e) {
            log.error("converter2FirstPinyin error, msgInfo:" + e.getMessage(), e);
        }
        return pinyinName.toString();
    }

    /**
     * 把汉字转换为全拼音
     *
     * @param cn 汉字
     * @return
     */
    public static String converter2Pinyin(String cn) {
        Assert.notNull(cn, "cn can not be null!");
        StringBuffer pinyinName = new StringBuffer();
        try {
            char[] nameChar = cn.toCharArray();
            HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
            defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
            defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
            for (int i = 0; i < nameChar.length; i++) {
                if (nameChar[i] > 128) {
                    try {
                        pinyinName.append(PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat)[0]);
                    } catch (BadHanyuPinyinOutputFormatCombination e) {
                        return null;
                    }
                } else {
                    pinyinName.append(nameChar[i]);
                }
            }
        } catch (Exception e) {
            log.error("converter2Pinyin error, msgInfo:" + e.getMessage(), e);
        }
        return pinyinName.toString();
    }

    public static void main(String[] args) {
        System.out.println(Cn2PinyinUtil.converter2Pinyin("温浩胜"));
    }
}
