package com.icebartech.excel;

import com.icebartech.excel.annotation.ExcelField;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

class CellMappingFactory {

    public static List<Property> mapRows(Class<?> clazz) {
        List<Property> rows = new ArrayList<>();
        rows.add(mapHeader(clazz));
        return rows;
    }

    public static List<Property> mapRows(List<?> datas) {
        List<Property> rows = new ArrayList<>();
        rows.add(mapHeader(datas.get(0).getClass()));

        int index = rows.get(0).getChildren().get(0).getHeight();
        for (Object data : datas) {
            Property body = mapBody(data, index);
            if (body == null) continue;
            index = index + body.getChildren().get(0).getHeight();
            rows.add(body);
        }
        return rows;
    }

    public static Property mapHeader(Class<?> clazz) {
        // 解析字段
        Property header = Property.buildRoot(0);
        for (Field field : clazz.getDeclaredFields()) {
            header.addChild(field2property(field));
        }

        return header;
    }

    public static Property mapBody(Object object, Integer row) {

        Property body = Property.buildRoot(row);
        for (Field field : object.getClass().getDeclaredFields()) {
            body.addChildren(value2property(object, field));
        }
        if (body.getChildren() == null) {
            return null;
        }

        return body;
    }

    private static Property field2property(Field field) {
        ExcelField excelField = field.getAnnotation(ExcelField.class);
        if (null == excelField) return null;

        // 基本类型和包装类
        Property property = new Property();
        property.setName(field.getName());
        property.setValue(excelField.value());
        property.setColumnWidth(excelField.columnWidth());

        // todo 日期类型
        if (isDateType(field)) {
            property.setValue(excelField.value());
        }

        // 对象类型
        if (isObjectType(field)) {
            for (Field ff : field.getType().getDeclaredFields()) {
                property.addChild(field2property(ff));
            }
        }

        // 集合类型
        if (isListType(field)) {
            ParameterizedType pt = (ParameterizedType) field.getGenericType();
            Class<?> genericClazz = (Class<?>) pt.getActualTypeArguments()[0];
            for (Field ff : genericClazz.getDeclaredFields()) {
                property.addChild(field2property(ff));
            }
        }
        return property;
    }

    private static List<Property> value2property(Object object, Field field) {
        ExcelField excelField = field.getAnnotation(ExcelField.class);
        if (null == excelField) return null;

        List<Property> list = new ArrayList<>();

        // 基本数据类型和包装类
        if (object == null) {
            Property property = new Property();
            property.setValue(null);
            list.add(property);
            return list;
        }

        if (isBaseType(field)) {
            Property property = new Property();
            property.setValue(getFieldValue(object, field));
            list.add(property);
        }
        // 日期类型
        if (isDateType(field)) {
            Property property = new Property();
            property.setValue(getFieldValue(object, field));
            list.add(property);
        }

        // 对象类型
        if (isObjectType(field)) {
            Object fieldValue = getFieldValue(object, field);
            for (Field childField : field.getType().getDeclaredFields()) {
                List<Property> properties = value2property(fieldValue, childField);
                if (properties != null) list.addAll(properties);
            }
        }

        // 集合类型
        if (isListType(field)) {
            List<?> datas = (List<?>) getFieldValue(object, field);
            ParameterizedType pt = (ParameterizedType) field.getGenericType();
            Class<?> genericClazz = (Class<?>) pt.getActualTypeArguments()[0];
            for (Field ff : genericClazz.getDeclaredFields()) {
                Property property = new Property();
                list.add(property);

                if (datas == null || datas.size() == 0) continue;

                property.setValue(getFieldValue(datas.get(0), ff));

                for (int i = 1; i < datas.size(); i++) {
                    Property child = new Property();
                    child.setValue(getFieldValue(datas.get(i), ff));
                    property.addChild(child);
                    property = child;
                }
            }
        }

        return list;
    }

    private static Object getFieldValue(Object object, Field filed) {
        try {
            filed.setAccessible(true);
            return filed.get(object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取List范型的字段列表
     */
    private static Field[] getListGenericFields(Field field) {
        if (field.getType() != java.util.List.class) {
            return new Field[]{};
        }
        // 如果是List类型，得到其Generic的类型
        if (!(field.getGenericType() instanceof ParameterizedType)) return new Field[]{};

        // 如果是泛型参数的类型
        ParameterizedType pt = (ParameterizedType) field.getGenericType();
        //得到泛型里的class类型对象
        Class<?> genericClazz = (Class<?>) pt.getActualTypeArguments()[0];

        return genericClazz.getDeclaredFields();
    }

    /**
     * 判断字段是否基本数据类型
     * 1. 基本数据类型
     * 1. 基本数据类型的包装类
     */
    private static Boolean isBaseType(Field field) {
        String[] types = {
                "int", "double", "long", "short", "byte", "boolean", "char", "float",
                "java.lang.Integer",
                "java.lang.Double",
                "java.lang.Float",
                "java.lang.Long",
                "java.lang.Short",
                "java.math.BigDecimal",
                "java.lang.Byte",
                "java.lang.Boolean",
                "java.lang.Character",
                "java.lang.String"};

        for (String type : types) {
            if (field.getType().getName().equals(type)) return true;

        }

        return false;
    }

    /**
     * 判断是否日期类型
     */
    private static Boolean isDateType(Field field) {
        String[] types = {
                "java.util.Date",
                "java.time.LocalDateTime"};
        for (String type : types) {
            if (field.getType().getName().equals(type)) return true;

        }
        return false;
    }

    /**
     * 是否集合类型
     */
    public static Boolean isListType(Field field) {
        if (field.getType() == java.util.List.class) {
            return field.getGenericType() instanceof ParameterizedType;
        }
        return false;
    }

    /**
     * 是否对象类型
     */
    private static Boolean isObjectType(Field field) {
        return !(isBaseType(field)) && !isDateType(field) && !isListType(field);
    }
}
