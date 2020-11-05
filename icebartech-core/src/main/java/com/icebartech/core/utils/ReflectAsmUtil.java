package com.icebartech.core.utils;

import com.icebartech.core.utils.hibernate.FieldBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * ReflectAsm反射工具类
 */
@Slf4j
public class ReflectAsmUtil {

    /**
     * 按照类名反射出它的一个对象
     *
     * @param classname 包名+类名
     * @return Object
     */
    public static Object getObjByClassName(String classname) {
        Object obj = null;
        if (classname != null) {
            try {
                Class a = Class.forName(classname);
                obj = a.newInstance();
            } catch (Exception e) {
                log.error("ReflectAsmUtil.getObjByClassName error.", e);
            }
        }
        return obj;
    }

    /**
     * 按照类名,参数值反射出它的一个对象
     *
     * @param classname 包名+类名
     * @param parameter 构造函数的参数值
     * @return Object
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Object getObjByClassNameAndParameter(String classname, Object[] parameter) {
        Object obj = null;
        if (classname != null) {
            try {
                Class a = Class.forName(classname);
                // 获取公有的构造函数,指定参数
                Constructor con = a.getConstructor(getParameterClass(parameter));
                obj = con.newInstance(parameter);
            } catch (Exception e) {
                log.error("ReflectAsmUtil.getObjByClassNameAndParameter error.", e);
            }
        }
        return obj;

    }

    /**
     * 用类名反射调用它的某个方法(一般针对工具类或者service)(无参数)
     *
     * @param obj        对象
     * @param methodName 方法名
     * @return Object
     */
    public static Object invokeMethod(Object obj, String methodName) {
        return invokeMethodWithObjHasParame(obj, methodName, new Object[0]);
    }

    /**
     * 用类名反射调用它的某个方法(一般针对工具类或者service)(有参数)
     *
     * @param obj        对象
     * @param methodName 方法名
     * @param parameter  参数数组
     * @return Object
     */
    public static Object invokeMethodHasParame(Object obj, String methodName, Object[] parameter) {
        return invokeMethodWithObjHasParame(obj, methodName, parameter);
    }

    /**
     * 用对象反射调用它的某个方法(没有参数的方法)
     *
     * @param obj        对象
     * @param methodName 方法名
     * @return [返回类型说明]
     */
    public static Object invokeMethodWithObj(Object obj, String methodName) {
        return invokeMethodWithObjHasParame(obj, methodName, new Object[0]);
    }

    /**
     * 用对象反射调用它的某个方法(有参数的方法)
     *
     * @param obj        对象
     * @param methodName 方法名
     * @param parameter  参数数组
     * @return Object
     */

    public static Object invokeMethodWithObjHasParame(Object obj, String methodName, Object[] parameter) {
        return invokeMethodWithObjHasSpecialParame(obj, methodName, parameter, getParameterClass(parameter));
    }

    /**
     * 获取参数列表的class对象
     *
     * @param parameter 参数值数组
     * @return Class[]
     */
    private static Class[] getParameterClass(Object[] parameter) {
        Class[] methodParameters = null;
        if (parameter != null &&
                parameter.length > 0) {
            methodParameters = new Class[parameter.length];
            for (int i = 0; i < parameter.length; i++) {
                methodParameters[i] = parameter[i].getClass();
            }
        }
        return methodParameters;
    }

    /**
     * 用对象反射调用它的某个方法(指定参数类型的方法)
     *
     * @param obj              对象
     * @param methodName       方法名
     * @param parameter        参数数组
     * @param methodParameters 参数类型数组
     * @return Object
     */
    public static Object invokeMethodWithObjHasSpecialParame(Object obj, String methodName,
                                                             Object[] parameter, Class[] methodParameters) {
        Object object = null;
        try {
            Method method = obj.getClass().getMethod(methodName.trim(), methodParameters);
            object = method.invoke(obj, parameter);
        } catch (Exception e) {
            log.error("ReflectAsmUtil.invokeMethodWithObjHasSpecialParame error.", e);
        }
        return object;
    }

    /**
     * 反射获取一个类的方法信息 包括参数,方法名,返回类型
     *
     * @param clazz
     * @return List<String>
     */
    public static List<String> getMethodMsg(Class clazz) {
        List<String> retValue = new ArrayList<>();
        // 通过getMethods得到类中包含的方法
        Method m[] = clazz.getDeclaredMethods();
        for (Method aM : m) {
            String meth = aM.toString();
            // 截取出所有的参数,参数以,形式分割
            meth = meth.substring(meth.indexOf("(") + 1, meth.indexOf(")"));
            // ret由3部分构成:参数;方法名;返回类型
            String ret = meth + ";" + aM.getName() + ";" + aM.getReturnType();
            retValue.add(ret);
        }
        return retValue;
    }

    /**
     * 检测对象中是否存在某个属性
     */
    public static Boolean checkBeanHasField(Class clazz, String fieldName) {
        if (StringUtils.isEmpty(fieldName)) {
            return false;
        }
        List<Field> fieldList = new ArrayList<>();
        Class tempClass = clazz;
        //当父类为null的时候说明到达了最上层的父类(Object类).
        while (tempClass != null) {
            fieldList.addAll(Arrays.asList(tempClass.getDeclaredFields()));
            //得到父类,然后赋给自己
            tempClass = tempClass.getSuperclass();
        }
        for (Field field : fieldList) {
            if (field.getName().equals(fieldName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据属性名获取属性值
     */
    public static Object getFieldValueByName(Object obj, String fieldName) {
        String getter = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        return invokeMethodWithObj(obj, getter);
    }

    /**
     * 获取属性类型(type)，属性名(name)，属性值(value)的map组成的list
     */
    public static List<FieldBean> getFields(Object obj) {
        List<Field> fieldList = new ArrayList<>();
        Class tempClass = obj.getClass();
        //当父类为null的时候说明到达了最上层的父类(Object类).
        while (tempClass != null) {
            fieldList.addAll(Arrays.asList(tempClass.getDeclaredFields()));
            //得到父类,然后赋给自己
            tempClass = tempClass.getSuperclass();
        }
        List<FieldBean> list = new ArrayList<>();
        for (Field field : fieldList) {
            FieldBean bean = new FieldBean();
            bean.setField(field);
            bean.setName(field.getName());
            bean.setType(field.getType());
            bean.setValue(getFieldValueByName(obj, field.getName()));
            list.add(bean);
        }
        return list;
    }

    public static List<FieldBean> getFields(Map<String, Object> map) {
        List<FieldBean> list = new ArrayList<>();
        if (null != map) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                FieldBean bean = new FieldBean();
                bean.setName(entry.getKey());
                bean.setType(entry.getValue().getClass());
                bean.setValue(entry.getValue());
                list.add(bean);
            }
        }
        return list;
    }
}
