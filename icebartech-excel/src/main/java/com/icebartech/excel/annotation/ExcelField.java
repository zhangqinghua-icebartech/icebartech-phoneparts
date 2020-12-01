package com.icebartech.excel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author wuwenze
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelField {

    /**
     * @return 单元格名称(如 : id字段显示为 ' 编号 ') 默认为字段名
     */
    String value() default "";

    /**
     * 单元格长度，1个长度表示1个数字/字母，或半个汉字。没有则按照标题文字计算长度
     */
    int columnWidth() default 0;

    Class<? extends Options> options() default Options.class;
}
