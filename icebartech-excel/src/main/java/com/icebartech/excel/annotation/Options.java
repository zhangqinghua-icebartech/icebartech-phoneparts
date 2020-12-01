package com.icebartech.excel.annotation;

public interface Options {

    /**
     * 下拉框列表展示的内容
     * <p>
     * Java,C,C++,PHP
     * <p>
     * 或者
     * <p>
     * 01-Java,02-C,03-C++,04-PHP
     */
    String[] get();

    default Object convert(Object value) {
        return value;
    }
}
