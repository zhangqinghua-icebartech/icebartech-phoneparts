package com.icebartech.core.utils.hibernate;

import com.icebartech.core.utils.StringKit;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.hibernate.HibernateException;
import org.hibernate.transform.ResultTransformer;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

/**
 * @author Anler
 * @Date 2019/2/16 18:56
 * @Description Hibernate自定义查询列名转为驼峰格式匹配赋值返回自定义的pojo类型
 */
public class SelfResultTransformer implements ResultTransformer {

    private static final long serialVersionUID = 4226687819666848461L;

    private final Class<?> resultClass;
    private Field[] fields;
    private BeanUtilsBean beanUtilsBean;

    public SelfResultTransformer(final Class<?> resultClass) {
        this.resultClass = resultClass;
        this.fields = this.resultClass.getDeclaredFields();
        beanUtilsBean = BeanUtilsBean.getInstance();
    }

    /**
     * aliases为每条记录的数据库字段名
     * tupe为与aliases对应的字段的值
     */
    @Override
    public Object transformTuple(final Object[] tuple, final String[] aliases) {
        Object result;
        try {
            result = this.resultClass.newInstance();
            for (int i = 0; i < aliases.length; i++) {
                for (Field field : this.fields) {
                    String fieldName = field.getName();
                    //将下划线格式转为驼峰，如数据库字段为 USER_NAME，自定义pojo的属性名为userName就可以使用
                    if (fieldName.equalsIgnoreCase(StringKit.deCodeUnderlined(aliases[i].toLowerCase()))) {
                        Object val;
                        if (field.getType().isEnum()) {
                            val = toEnum(tuple[i].toString(), field.getType());
                        } else if (field.getType().isAssignableFrom(LocalDateTime.class)) {
                            val = LocalDateTime.ofInstant(((Timestamp) tuple[i]).toInstant(), ZoneId.systemDefault());
                        } else {
                            val = tuple[i];
                        }
                        beanUtilsBean.setProperty(result, fieldName, val);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            throw new HibernateException("Could not instantiate resultclass: " + this.resultClass.getName(), e);
        }
        return result;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public List transformList(final List collection) {
        return collection;
    }

    public Object toEnum(String s, Class clazz) {
        Object enumeration = null;
        Method[] ms = clazz.getMethods();
        for (Method m : ms) {
            if (m.getName().equalsIgnoreCase("valueOf")) {
                try {
                    enumeration = m.invoke(clazz, s);
                } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                return enumeration;
            }
        }
        return null;
    }


}