package com.icebartech.core.utils;

import org.springframework.util.StringUtils;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * 自定义日期管理解析器
 *
 * @author haosheng.wenhs
 */
public class DateConvertEditor extends PropertyEditorSupport {
    private SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (StringUtils.hasText(text)) {
            try {
                if (!text.contains(":") && text.length() == 10) {
                    setValue(dateFormat.parse(text));
                } else if (text.indexOf(":") > 0 && text.length() == 19) {
                    setValue(datetimeFormat.parse(text));
                } else {
                    throw new IllegalArgumentException("can not parse date, date format is error!");
                }
            } catch (ParseException e) {
                IllegalArgumentException ex = new IllegalArgumentException("can not parse date: " + e.getMessage());
                ex.initCause(ex);
                throw ex;
            }
        } else {
            setValue(null);
        }
    }
}
