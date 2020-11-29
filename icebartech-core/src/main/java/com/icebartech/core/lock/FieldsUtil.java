package com.icebartech.core.lock;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FieldsUtil {

    public static List<Field> getAllFields(Class<?> tempClass) {
        List<Field> fieldList = new ArrayList<>();
        while (tempClass != null && !tempClass.getName().toLowerCase().equals("java.lang.object")) {
            fieldList.addAll(Arrays.asList(tempClass.getDeclaredFields()));
            tempClass = tempClass.getSuperclass();
        }
        return fieldList;
    }

    /**
     * 获取属性值。
     */
    @SuppressWarnings("unchecked")
    public static <T> T getValue(Object object, Class<T> fieldClz) {
        try {
            System.out.println(fieldClz.getSimpleName());
            System.out.println(fieldClz.getName());
            Field field = object.getClass().getField(fieldClz.getSimpleName());
            //设置对象的访问权限，保证对private的属性的访问
            return (T) field.get(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取方法的全限定名称
     * com.icebartech.core.RedisCompment.lock(java.long.String, long)
     */
    @SuppressWarnings("rawtypes")
    public static String methodFullName(Method method) {
        // 1. 获取类的全限定名。com.icebartech.core.RedisCompment
        StringBuilder sb = new StringBuilder(method.getDeclaringClass().getName());

        // 2. 获取方法的名称
        sb.append(".").append(method.getName());

        // 3. 获取参数类型
        sb.append("(");
        Class[] paramTypes = method.getParameterTypes();
        for (Class clz : paramTypes) {
            sb.append(clz.getName()).append(", ");
        }
        if (sb.lastIndexOf(",") == (sb.length() - 2)) {
            sb.delete(sb.length() - 2, sb.length());
        }
        sb.append(")");

        return sb.toString();
    }
}
