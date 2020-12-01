package com.icebartech.core.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

/**
 * Java反射工具类，字段处理
 */
public class ReflectUtils {

    /**
     * 获取包括父类在内的所有字段
     */
    public static List<Field> fields(Class<?> clz) {
        List<Field> fields = new ArrayList<>();
        // 当父类为null的时候说明到达了最上层的父类(Object类).
        while (clz != null) {
            fields.addAll(0, Arrays.asList(clz.getDeclaredFields()));
            // 得到父类,然后赋给自己
            clz = clz.getSuperclass();
        }
        // 移除调final，static的字段
        fields.removeIf(d -> Modifier.isFinal(d.getModifiers()) || Modifier.isStatic(d.getModifiers()));
        return fields;
    }

    /**
     * 获取List的泛型
     * List<String> > String
     */
    public static Class<?> listArgType(Field field) {
        ParameterizedType listGenericType = (ParameterizedType) field.getGenericType();
        Type[] types = listGenericType.getActualTypeArguments();
        if (types.length > 0) {
            try {
                return Class.forName(types[0].getTypeName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 判断字段是否List类型
     */
    public static Boolean isList(Class<?> clz) {
        return clz == List.class;
    }

    public static Boolean isList(Field field) {
        return isList(field.getType());
    }

    /**
     * 判断字段是否基本数据类型
     * 1. 基本数据类型
     * 1. 基本数据类型的包装类
     */
    public static Boolean isBaseType(Class<?> clz) {
        final String[] types = {
                "int", "double", "long", "short", "byte", "boolean", "char", "float",
                "java.lang.Integer",
                "java.lang.Double",
                "java.lang.Float",
                "java.lang.Long",
                "java.lang.Short",
                "java.lang.Byte",
                "java.lang.Boolean",
                "java.lang.Character",
                "java.lang.String"};
        return Stream.of(types).anyMatch(t -> t.equals(clz.getName()));
    }

    public static Boolean isBaseType(Field field) {
        return isBaseType(field.getType());
    }

    /**
     * 判断是否日期类型
     */
    public static Boolean isDateType(Field field) {
        return isDateType(field.getType());
    }

    public static Boolean isDateType(Class<?> clz) {
        final String[] types = {
                "java.util.Date",
                "java.time.LocalDate",
                "java.time.LocalTime",
                "java.time.LocalDateTime"};
        return Stream.of(types).anyMatch(t -> t.equals(clz.getName()));
    }

    /**
     * 是否集合类型
     */
    public static Boolean isListType(Field field) {
        if (field.getType() == List.class) {
            return field.getGenericType() instanceof ParameterizedType;
        }
        return false;
    }

    public static Boolean isCollectionType(Class<?> clz) {
        //判断返回类型是否是集合类型
        boolean isCollection = Collection.class.isAssignableFrom(clz);
        //判断返回类型是否是数组类型
        boolean isArray = clz.isArray();
        return isCollection || isArray;
    }

    /**
     * 是否对象类型
     * 1. 排除基本数据类型（包括包装类）
     * 2. 排除时间类型
     * 3. 排除集合类型
     */
    public static Boolean isObjectType(Field field) {
        return !(isBaseType(field)) && !isDateType(field) && !isListType(field);
    }

    public static Boolean isObjectType(Class<?> clz) {
        return !(isBaseType(clz)) && !isDateType(clz) && !isList(clz);
    }


    public static Class<?> findListType(Field field) {
        ParameterizedType pt = (ParameterizedType) field.getGenericType();
        return (Class<?>) pt.getActualTypeArguments()[0];
    }
}
