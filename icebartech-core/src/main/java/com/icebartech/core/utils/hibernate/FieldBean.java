package com.icebartech.core.utils.hibernate;

import lombok.Data;

import java.lang.reflect.Field;

@Data
public final class FieldBean {

    private Field field;

    private String name;

    private Class<?> type;

    private Object value;

    private QueryType queryType;


}
