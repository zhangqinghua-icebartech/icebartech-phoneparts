package com.icebartech.core.utils;

import com.google.common.collect.Lists;
import com.icebartech.core.enums.CommonResultCodeEnum;
import com.icebartech.core.exception.ServiceException;
import org.apache.commons.lang3.ArrayUtils;
import org.dozer.DozerBeanMapper;
import org.dozer.loader.api.BeanMappingBuilder;
import org.dozer.loader.api.TypeMappingOption;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigInteger;
import java.util.*;
/**
 * 简单封装Dozer, 实现深度转换Bean<->Bean的Mapper.实现:
 * <p>
 * 1. 持有Mapper的单例.
 * 2. 返回值类型转换.
 * 3. 批量转换Collection中的所有对象.
 * 4. 区分创建新的B对象与将对象A值复制到已存在的B对象两种函数.
 *
 * @author calvin
 */
public class BeanMapper {

    //private static final Logger log = org.slf4j.LoggerFactory.getLogger(BeanMapper.class);
    /**
     * 持有Dozer单例, 避免重复创建DozerMapper消耗资源.
     */
    private static volatile DozerBeanMapper dozer;

    private static DozerBeanMapper getDozer() {
        if (dozer == null) {
            synchronized (DozerBeanMapper.class) {
                if (dozer == null) {
                    dozer = new DozerBeanMapper();
                    // 开启Dozer对java8的LocalDateTime支持
                    dozer.setMappingFiles(Collections.singletonList("config/dozerConverters.xml"));
                }
            }
        }
        return dozer;
    }

    /**
     * 基于Dozer转换对象的类型.
     */
    public static <T> T map(Object source, Class<T> destinationClass) {
        if (source == null) {
            return null;
        }

        if (source instanceof Object[]) {
            return arraysToObject((Object[]) source, destinationClass);
        }
        if (source instanceof Map) {
            //noinspection unchecked
            return mapToObject((Map<String, Object>) source, destinationClass);
        }

        return getDozer().map(source, destinationClass);
    }

    /**
     * 基于Dozer转换Collection中对象的类型.
     */
    public static <T> List<T> mapList(Collection<?> sourceList, Class<T> destinationClass) {
        List<T> destinationList = Lists.newArrayList();
        if (sourceList == null) {
            return destinationList;
        }
        for (Object source : sourceList) {
            T destinationObject = map(source, destinationClass);
            destinationList.add(destinationObject);
        }
        return destinationList;
    }

    /**
     * 全属性复制
     *
     * @param source            源对象
     * @param destinationObject 输出对象
     */
    public static void copy(final Object source, final Object destinationObject) {
        if (null == source || null == destinationObject) {
            return;
        }
        getDozer().map(source, destinationObject);
    }

    /**
     * 忽略null值的属性复制
     *
     * @param source            源对象
     * @param destinationObject 输出对象
     */
    public static void copyProperties(final Object source, final Object destinationObject) {
        // 自定义配置是全局DozerBeanMapper有效的 并且设定配置必须在map()前 所以这里不使用单例的dozer
        DozerBeanMapper dozer = new DozerBeanMapper();
        dozer.setMappingFiles(Collections.singletonList("config/dozerConverters.xml"));
        dozer.addMapping(new BeanMappingBuilder() {
            @Override
            protected void configure() {
                mapping(source.getClass(), destinationObject.getClass(), mappingBuilder -> {
                    mappingBuilder.mapNull(false);
                    // mappingBuilder.mapEmptyString(false);
                });
            }
        });
        dozer.map(source, destinationObject);
        dozer.destroy();
    }

    /**
     * 自定义配置复制
     *
     * @param source            源对象
     * @param destinationObject 输出对象
     */
    public static void copyProperties(final Object source, final Object destinationObject, TypeMappingOption... typeMappingOption) {
        // 自定义配置是全局DozerBeanMapper有效的 并且设定配置必须在map()前 所以这里不使用单例的dozer
        DozerBeanMapper dozer = new DozerBeanMapper();
        dozer.setMappingFiles(Collections.singletonList("config/dozerConverters.xml"));
        dozer.addMapping(new BeanMappingBuilder() {
            @Override
            protected void configure() {
                mapping(source.getClass(), destinationObject.getClass(), typeMappingOption);
            }
        });
        dozer.map(source, destinationObject);
        dozer.destroy();
    }

    private static <T> T mapToObject(Map<String, Object> map, Class<T> beanClass) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        int setCoutn = 0;
        T obj = null;
        try {
            obj = beanClass.newInstance();
            Field[] fields = getClassFields(obj.getClass());
            for (Field field : fields) {
                if (map.get(field.getName()) == null) {
                    continue;
                }
                Object value = map.get(field.getName());
                // 针对Biginterget处理
                if (value.getClass() != field.getClass()) {
                    if (value.getClass() == BigInteger.class) {
                        value = ((BigInteger) value).longValue();
                    }
                }

                int mod = field.getModifiers();
                if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                    continue;
                }
                field.setAccessible(true);

                field.set(obj, value);
                setCoutn++;
            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            //log.error(e.getMessage(), e);
        }
        return 0 == setCoutn ? null : obj;
    }

    private static Map<String, Object> arraysToMap(Object[] objs) {
        if (objs == null || objs.length == 0) {
            return null;
        }

        if (objs.length % 2 > 0) {
            throw new ServiceException(CommonResultCodeEnum.INVALID_ARGUMENTS, "参数需成对存在：" + Arrays.toString(objs));
        }

        Map<String, Object> map = new LinkedHashMap<>();
        for (int i = 0; i < objs.length; i += 2) {
            // 支持值为null
            if (objs[i] == null) {
                throw new ServiceException(CommonResultCodeEnum.INVALID_ARGUMENTS, "属性名不能为空：" + Arrays.toString(objs));
            }
            map.put(objs[i].toString(), objs[i + 1]);
        }
        return map;
    }

    private static <T> T arraysToObject(Object[] objs, Class<T> beanClass) {
        Map<String, Object> map = arraysToMap(objs);
        if (beanClass == Map.class) {
            //noinspection unchecked
            return (T) map;
        }
        return mapToObject(map, beanClass);
    }

    private static Field[] getClassFields(Class curClass) {
        Field[] objFields = curClass.getDeclaredFields();
        if (curClass.getSuperclass() != null) {
            objFields = ArrayUtils.addAll(objFields, getClassFields(curClass.getSuperclass()));
        }
        return objFields;
    }

    public static void main(String[] args) {
    }
}