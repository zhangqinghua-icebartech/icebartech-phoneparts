package com.icebartech.core.utils.hibernate;

public enum QueryType {
    /**
     *
     */
    EQ(""),
    /**
     * 属性名后面拼接NotEq
     */
    NOT_EQ("NotEq"),
    /**
     * 属性名后面拼接Like
     */
    LIKE("Like"),
    /**
     * 属性名后面拼接NotLike
     */
    NOT_LIKE("NotLike"),
    /**
     * 属性名后面拼接GT 大于
     */
    GT("GT"),
    /**
     * 属性名后面拼接GE 大于等于
     */
    GE("GE"),
    /**
     * 属性名后面拼接LT 小于
     */
    LT("LT"),
    /**
     * 属性名后面拼接LE 小于等于
     */
    LE("LE"),
    /**
     * 属性名后面拼接Null 属性值为Boolean
     */
    NULL("Null"),
    /**
     * 属性名后面拼接NotNull 属性值为Boolean
     */
    NOT_NULL("NotNull"),
    /**
     * 属性名后面拼接In 属性值为数组或者以逗号(,)分隔的字符串
     */
    IN("In"),
    /**
     * 属性名后面拼接NotIn 属性值为数组或者以逗号(,)分隔的字符串
     */
    NOT_IN("NotIn"),
    /**
     * 属性名后面拼接ASC 属性值为Boolean
     */
    ASC("ASC"),
    /**
     * 属性名后面拼接DESC 属性值为Boolean
     */
    DESC("DESC");

    public String key;

    QueryType(String key) {
        this.key = key;
    }
}
