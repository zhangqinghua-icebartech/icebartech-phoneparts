package com.icebartech.core.utils;

public class ToNumberSystem26 {

    /**
     * 将指定的自然数转换为26进制表示。映射关系：[1-26] ->[A-Z]。
     *
     * @param n
     * @return
     */
    public static String toNumberSystem26(int n) {
        StringBuilder s = new StringBuilder();
        while (n > 0) {
            int m = n % 26;
            if (m == 0) {
                m = 26;
            }
            s.insert(0, (char) (m + 64));
            n = (n - m) / 26;
        }
        return s.toString();
    }

}
