package com.icebartech.core.utils;

/**
 * 字符串与unicode编码的相互转换
 *
 * @author Administrator
 */
public class String2UnicodeUtil {

    private static final int RANGE_ONE_START = 0xe001;
    private static final int RANGE_ONE_END = 0xe05a;
    private static final int RANGE_TWO_START = 0xe101;
    private static final int RANGE_TWO_END = 0xe15a;
    private static final int RANGE_THREE_START = 0xe201;
    private static final int RANGE_THREE_END = 0xe253;
    private static final int RANGE_FOUR_START = 0xe301;
    private static final int RANGE_FOUR_END = 0xe34d;
    private static final int RANGE_FIVE_START = 0xe401;
    private static final int RANGE_FIVE_END = 0xe44c;
    private static final int RANGE_SIX_START = 0xe501;
    private static final int RANGE_SIX_END = 0xe537;

    /**
     * 把字符串转换为unicode编码
     *
     * @param str 要转换的字符串
     * @return
     */
    public static String string2Unicode(String str) {
        str = (str == null ? "" : str);
        String tmp;
        StringBuilder sb = new StringBuilder();
        char c;
        int i, j;
        sb.setLength(0);
        for (i = 0; i < str.length(); i++) {
            c = str.charAt(i);
            sb.append("\\u");
            //取出高8位
            j = (c >>> 8);
            tmp = Integer.toHexString(j);
            if (tmp.length() == 1) {
                sb.append("0");
            }
            sb.append(tmp);
            //取出低8位
            j = (c & 0xFF);
            tmp = Integer.toHexString(j);
            if (tmp.length() == 1) {
                sb.append("0");
            }
            sb.append(tmp);
        }
        return (new String(sb));
    }

    /**
     * 把单一个字符转换为unicode编码
     *
     * @param c 要转换的字符
     * @return
     */
    public static String char2Unicode(char c) {
        StringBuilder sb = new StringBuilder();
        sb.append("\\u");
        //取出高8位
        int j = (c >>> 8);
        String tmp = Integer.toHexString(j);
        if (tmp.length() == 1) {
            sb.append("0");
        }
        sb.append(tmp);
        //取出低8位
        j = (c & 0xFF);
        tmp = Integer.toHexString(j);
        if (tmp.length() == 1) {
            sb.append("0");
        }
        sb.append(tmp);
        return sb.toString();
    }

    /**
     * 把unicode编码成字符串
     *
     * @param str 要转换的unicode编码
     * @return
     */
    public static String unicode2String(String str) {
        str = (str == null ? "" : str);
        //如果不是unicode码则原样返回
        if (!str.contains("\\u")) {
            return str;
        }

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < str.length() - 6; ) {
            String strTemp = str.substring(i, i + 6);
            String value = strTemp.substring(2);
            int c = 0;
            for (int j = 0; j < value.length(); j++) {
                char tempChar = value.charAt(j);
                int t = 0;
                switch (tempChar) {
                    case 'a':
                        t = 10;
                        break;
                    case 'b':
                        t = 11;
                        break;
                    case 'c':
                        t = 12;
                        break;
                    case 'd':
                        t = 13;
                        break;
                    case 'e':
                        t = 14;
                        break;
                    case 'f':
                        t = 15;
                        break;
                    default:
                        t = tempChar - 48;
                        break;
                }
                c += t * ((int) Math.pow(16, (value.length() - j - 1)));
            }
            sb.append((char) c);
            i = i + 6;
        }
        return sb.toString();
    }

    /**
     * 把字符串的emoji表情转换成unicode编码
     *
     * @param emoji 要转换的字符串
     * @return
     */
    public static String emoji2Unicode(String emoji) {
        if (emoji == null || emoji.length() == 0) {
            return emoji;
        }
        StringBuilder sb = new StringBuilder();
        char[] contentArray = emoji.toString().toCharArray();
        for (char a : contentArray) {
            if (isEmojiUnicode(a)) {
                sb.append(char2Unicode(a));
            } else {
                sb.append(a);
            }
        }
        return sb.toString();
    }

    /**
     * 判断是否是emoji字符
     *
     * @param c
     * @return
     */
    public static boolean isEmojiUnicode(char c) {
        if (c < RANGE_ONE_START || c > RANGE_SIX_END) {
            return false;
        }
        if (c >= RANGE_ONE_START && c <= RANGE_ONE_END) {
            return true;
        }
        if (c >= RANGE_TWO_START && c <= RANGE_TWO_END) {
            return true;
        }
        if (c >= RANGE_THREE_START && c <= RANGE_THREE_END) {
            return true;
        }
        if (c >= RANGE_FOUR_START && c <= RANGE_FOUR_END) {
            return true;
        }
        if (c >= RANGE_FIVE_START && c <= RANGE_FIVE_END) {
            return true;
        }
        if (c >= RANGE_SIX_START && c <= RANGE_SIX_END) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        System.out.println(string2Unicode("我爱你"));
        System.out.println("\u4f60");
        System.out.println(char2Unicode('爱'));
    }
}
