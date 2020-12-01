package com.icebartech.core.utils;

import com.alibaba.druid.sql.visitor.functions.Char;
import com.icebartech.core.enums.CommonResultCodeEnum;
import com.icebartech.core.exception.ServiceException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 对象转换工具
 * 1. 支持基本数据类型及其包装类的相互转换
 * 1. 支持复杂数字类型的相互转换
 * 1. 支持多种日期类型的相互转换
 * 1. 支持map和对象的转换
 * 1. 支持对象的相互转换
 */
public class BeanMapperNew {

    /**
     * 拷贝字段（深拷贝）
     * 以源对象的有值字段为主，如果源对象的字段为null，则不拷贝。
     *
     * @param from 源对象
     * @param to   目标对象
     */
    public static void copyPropertiesDeep(Object from, Object to) {
        copyProperties(from, to, 1);
    }

    /**
     * 拷贝字段（中拷贝）
     * 以源对象的有值字段为主，如果源对象的字段为null，则不拷贝。
     *
     * @param from 源对象
     * @param to   目标对象
     */
    public static void copyPropertiesMiddle(Object from, Object to) {
        copyProperties(from, to, 2);
    }

    /**
     * 拷贝字段（浅拷贝）
     * 源对象作为目标对象的一个补充，是拷贝目标对象为null的字段。
     *
     * @param from 源对象
     * @param to   目标对象
     */
    public static void copyPropertiesShallow(Object from, Object to) {
        copyProperties(from, to, 3);
    }

    /**
     * 拷贝字段
     * 3. 源对象作为目标对象的一个补充，是拷贝目标对象为null的字段。
     * 2. 以源对象的有值字段为主，如果源对象的字段为null，则不拷贝
     * 1. 拷贝源对象的所有字段，即使为null。
     *
     * @param origin 源对象
     * @param dest   目标对象
     * @param type   1. 深拷贝 2. 中拷贝 3. 浅拷贝
     */
    private static void copyProperties(Object origin, Object dest, Integer type) {
        if (origin == null) return;

        Object originObj = BeanMapper.map(origin, dest.getClass());


        List<Field> fields = ReflectUtils.fields(dest.getClass());
        for (Field field : fields) {
            boolean accessible = field.isAccessible();
            field.setAccessible(true);

            try {
                Object destValue = field.get(dest);
                Object originValue = field.get(originObj);

                if (type == 1) {
                    field.set(dest, originValue);
                } else if (type == 2 && originValue != null) {
                    field.set(dest, originValue);
                } else if (destValue == null) {
                    field.set(dest, originValue);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            field.setAccessible(accessible);
        }

    }

    public static void copyNotNull(Object origin, Object dest) {

    }

    public static <T> List<T> map(Collection<?> sources, Class<T> destClz) {
        List<T> list = new ArrayList<>();
        if (sources == null || sources.size() == 0) return list;
        for (Object o : sources) {
            list.add(map(o, destClz));
        }
        return list;
    }

    /**
     * 类型转换
     * 1. 转换至基本数据类型
     * 2. 转换至特殊数字类型
     * 3. 转换至日期
     * 4. 转换至对象
     * 5. 不支持的类型
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> T map(Object source, Class<T> destClz) {
        if (source == null) return null;
        if (destClz == null) throw new ServiceException("对象转换失败，未指定目标类型");

        // 要克隆对象，不能直接赋值
        if (source.getClass() == destClz) return (T) deepClone(source);

        // 0. 转字符串
        if (destClz == String.class) {
            String formatDate = dateToString(source);
            return formatDate == null ? (T) String.valueOf(source) : (T) formatDate;
        }

        // 1. 数组转对象
        if (source instanceof Object[]) {
            return arraysToObject((Object[]) source, destClz);
        }

        // 2. map转对象
        if (source instanceof Map) {
            //noinspection unchecked
            return mapToObject((Map<String, Object>) source, destClz);
        }

        // 3. 基本数据类型
        if (isBaseType(destClz)) {
            return toBaseType(source, destClz);
        }
        // 4. 特殊数字类型
        if (isNumeric(destClz)) {
            return toNumeric(source, destClz);
        }

        // 5. 日期类型
        if (isDate(destClz)) {
            return toDate(source, destClz);
        }

        // todo 枚举转字符串，字符串转枚举

        // 6.1 Map->Map treeMap->hashMap...
        //noinspection ConstantConditions
        if (source instanceof Map && Map.class.isAssignableFrom(destClz)) {
            try {
                Map map = Modifier.isAbstract(destClz.getModifiers()) ? new HashMap<>() : (Map) destClz.newInstance();
                map.putAll((Map) source);
                return (T) map;
            } catch (Exception e) {
                throw new ServiceException("对象转换（Map->Map）异常：" + e.getMessage(), e);
            }
        }

        // 6.2 Object->Map
        //noinspection ConstantConditions
        if (!(source instanceof Map) && Map.class.isAssignableFrom(destClz)) {
            try {
                Map<String, Object> map = Modifier.isAbstract(destClz.getModifiers()) ? new HashMap<>() : (Map<String, Object>) destClz.newInstance();
                List<Field> fields = ReflectUtils.fields(source.getClass());
                for (Field field : fields) {
                    boolean accessible = field.isAccessible();
                    field.setAccessible(true);
                    map.put(field.getName(), field.get(source));
                    field.setAccessible(accessible);
                }
                return (T) map;
            } catch (ServiceException e) {
                throw e;
            } catch (IllegalAccessException e) {
                throw new ServiceException("对象转换（Object->Map）异常，对象属性无法访问：" + e.getMessage(), e);
            } catch (Exception e) {
                throw new ServiceException("对象转换（Object->Map）异常，未知错误：" + e.getMessage(), e);
            }
        }

        // 7.1 Map->Object tps: 250000
        //noinspection ConstantConditions
        if (source instanceof Map) {
            try {
                T obj = destClz.newInstance();
                Map<Object, Object> map = (Map<Object, Object>) source;
                for (Map.Entry<Object, Object> entry : map.entrySet()) {
                    String propertyName = entry.getKey().toString();    // 属性名
                    Object propertyValue = entry.getValue();            // 属性值
                    Field field = getClassField(destClz, propertyName);    //获取和map的key匹配的属性名称
                    if (field == null) {
                        continue;
                    }
                    Class<?> fieldTypeClass = field.getType();
                    propertyValue = map(propertyValue, fieldTypeClass);

                    // 设置值（需要按照规范写，setXXX），setAccessible = true，直接赋值
                    String setMethodName = "set" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
                    try {
                        destClz.getMethod(setMethodName, field.getType()).invoke(obj, propertyValue);
                    } catch (NoSuchMethodException e) {
                        // 没有set方法，用另外一种方法处理
                        boolean accessible = field.isAccessible();
                        field.setAccessible(true);
                        field.set(obj, propertyValue);
                        field.setAccessible(accessible);
                    } catch (InvocationTargetException e) {
                        // 方法调用异常，直接抛出去
                        throw new ServiceException("对象转换异常，方法「" + setMethodName + "」调用失败：" + e.getMessage(), e);
                    }
                }
                return obj;
            } catch (ServiceException e) {
                throw e;
            } catch (IllegalAccessException e) {
                throw new ServiceException("对象转换异常，字段无法访问：" + e.getMessage(), e);
            } catch (InstantiationException e) {
                throw new ServiceException("对象转换异常，创建目标对象失败：" + e.getMessage(), e);
            } catch (Exception e) {
                throw new ServiceException("对象转换异常，未知错误：" + e.getMessage(), e);
            }
        }

        // 7.2 Object -> Object destFields.forEach tps: 100000 destFields.parallelStream().forEach tps: 50000 奇怪了更少了
        // todo 已知道 List，Enum无法newInstance
        try {
            T t = destClz.newInstance();

            List<Field> destFields = ReflectUtils.fields(destClz);
            List<Field> fromFields = ReflectUtils.fields(source.getClass());

            destFields.forEach(destField -> fromFields.stream().filter(f -> f.getName().equals(destField.getName())).findFirst().ifPresent(fromField -> {
                try {
                    String setMethodName = "set" + fromField.getName().substring(0, 1).toUpperCase() + fromField.getName().substring(1);
                    boolean fromAccessible = fromField.isAccessible();
                    fromField.setAccessible(true);
                    Object fromValue = fromField.get(source);
                    if (fromValue == null) return;

                    fromField.setAccessible(fromAccessible);

                    // 针对List > List 进行处理
                    if (ReflectUtils.isList(fromField) && ReflectUtils.isList(destField)) {
                        fromValue = map((List) fromValue, ReflectUtils.listArgType(destField));
                    }

                    try {
                        // 赋值，即set方法，要类型相同（或者继承）
                        if (destField.getType() != fromValue.getClass() &&
                            !destField.getType().isAssignableFrom(fromValue.getClass())) {
                            destClz.getMethod(setMethodName, destField.getType()).invoke(t, map(fromValue, destField.getType()));
                        } else {
                            destClz.getMethod(setMethodName, destField.getType()).invoke(t, fromValue);
                        }
                    } catch (ServiceException e) {
                        throw e;
                    } catch (NoSuchMethodException e) {
                        // 没有set方法，用另外一种方法处理
                        boolean accessible = destField.isAccessible();
                        destField.setAccessible(true);
                        destField.set(t, fromValue);
                        destField.setAccessible(accessible);
                    } catch (InvocationTargetException e) {
                        if (e.getTargetException() instanceof ServiceException) {
                            throw (ServiceException) e.getTargetException();
                        }
                        // 方法调用异常，直接抛出去
                        throw new ServiceException("对象转换异常，方法「" + setMethodName + "」调用失败：" + e.getMessage(), e);
                    } catch (IllegalAccessException e) {
                        throw new ServiceException("对象转换异常，字段无法访问：" + e.getMessage(), e);
                    } catch (Exception e) {
                        throw new ServiceException("对象转换异常，未知错误：" + e.getMessage(), e);
                    }
                } catch (ServiceException e) {
                    throw e;
                } catch (IllegalAccessException e) {
                    throw new ServiceException("对象转换异常，字段无法访问：" + e.getMessage(), e);
                } catch (Exception e) {
                    throw new ServiceException("对象转换异常，未知错误：" + e.getMessage(), e);
                }
            }));

            return t;
        } catch (ServiceException e) {
            throw e;
        } catch (IllegalAccessException e) {
            throw new ServiceException("对象转换异常，字段无法访问：" + e.getMessage(), e);
        } catch (InstantiationException e) {
            throw new ServiceException("对象转换异常，创建目标对象失败：" + e.getMessage(), e);
        }
    }

    /**
     * 根据给定对象类匹配对象中的特定字段
     */
    private static Field getClassField(Class<?> clazz, String fieldName) {
        if (Object.class.getName().equals(clazz.getName())) {
            return null;
        }
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.getName().equals(fieldName)) {
                return field;
            }
        }
        Class<?> superClass = clazz.getSuperclass();    // 如果该类还有父类，将父类对象中的字段也取出
        if (superClass != null) {                       // 递归获取
            return getClassField(superClass, fieldName);
        }
        return null;
    }

    /**
     * 转换基本数据类型
     * byte Byte
     * char Char
     * int Integer
     * long Long
     * short Short
     * float Float
     * double Double
     * boolean Boolean
     * tps: ?
     */
    @SuppressWarnings("unchecked")
    private static <T> T toBaseType(Object value, Class<T> destClz) {
        if (value == null) return null;
        if (destClz == char.class || destClz == Char.class) {
            return (T) new Byte(value.toString());
        }
        if (destClz == byte.class || destClz == Byte.class) {
            return (T) new Character(value.toString().charAt(0));
        }
        if (destClz == int.class || destClz == Integer.class) {
            return (T) new Integer(new Double(value.toString()).intValue());
        }
        if (destClz == long.class || destClz == Long.class) {
            return (T) new Long(new Double(value.toString()).longValue());
        }
        if (destClz == short.class || destClz == Short.class) {
            return (T) new Short(new Double(value.toString()).shortValue());
        }
        if (destClz == float.class || destClz == Float.class) {
            return (T) new Float(value.toString());
        }
        if (destClz == double.class || destClz == Double.class) {
            return (T) new Double(value.toString());
        }
        if (destClz == boolean.class || destClz == Boolean.class) {
            return (T) Boolean.valueOf(value.toString());
        }
        return (T) String.valueOf(value);
    }

    /**
     * 转换特殊数字
     * 1. BigInteger
     * 2. BigDecimal
     * tps: 6000000
     */
    @SuppressWarnings("unchecked")
    private static <T> T toNumeric(Object value, Class<T> destClz) {
        if (value == null) return null;
        if (destClz == BigInteger.class) {
            return (T) BigInteger.valueOf(toBaseType(value, Long.class));
        }
        return (T) BigDecimal.valueOf(toBaseType(value, Double.class));
    }

    /**
     * 转换日期
     * from: Date, LocalDate, LocalTime, LocalDateTime, MySQL(Date, Time, Timestamp), String(yyyy-MM-dd HH:mm:ss, yyyy-MM-dd, HH:mm:ss)
     * to  : Date, LocalDate, LocalTime, LocalDateTime
     * tps : 50000/s
     */
    @SuppressWarnings("unchecked")
    private static <T> T toDate(Object value, Class<T> destClz) {
        // Java类型快速转换
        if (value.getClass() == Date.class) {
            Instant instant = ((Date) value).toInstant();
            ZoneId zoneId = ZoneId.systemDefault();

            if (destClz == LocalDate.class)
                return (T) instant.atZone(zoneId).toLocalDate();
            if (destClz == LocalTime.class)
                return (T) instant.atZone(zoneId).toLocalTime();
            if (destClz == LocalDateTime.class)
                return (T) instant.atZone(zoneId).toLocalDateTime();
        }
        if (value.getClass() == LocalDate.class) {
            LocalDate localDate = (LocalDate) value;

            if (destClz == Date.class)
                return (T) Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            if (destClz == LocalTime.class)
                return (T) LocalTime.MIN;
            if (destClz == LocalDateTime.class)
                return (T) localDate.atStartOfDay();
        }
        if (value.getClass() == LocalTime.class) {
            LocalTime localTime = (LocalTime) value;
            if (destClz == Date.class)
                return (T) Date.from(localTime.atDate(LocalDate.of(1970, 1, 1)).atZone(ZoneId.systemDefault()).toInstant());
            if (destClz == LocalDate.class)
                return (T) LocalDate.of(1970, 1, 1);
            if (destClz == LocalDateTime.class)
                return (T) localTime.atDate(LocalDate.of(1970, 1, 1));
        }
        if (value.getClass() == LocalDateTime.class) {
            LocalDateTime localDateTime = (LocalDateTime) value;
            if (destClz == Date.class)
                return (T) Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
            if (destClz == LocalDate.class)
                return (T) localDateTime.toLocalDate();
            if (destClz == LocalTime.class)
                return (T) localDateTime.toLocalTime();
        }

        // 数据库类型快速转换
        if (value.getClass() == java.sql.Date.class) {
            java.sql.Date date = (java.sql.Date) value;
            if (destClz == Date.class)
                return (T) Date.from(date.toInstant());
            if (destClz == LocalDate.class)
                return (T) date.toLocalDate();
            if (destClz == LocalTime.class)
                return (T) LocalTime.MIN;
            if (destClz == LocalDateTime.class)
                return (T) date.toLocalDate().atStartOfDay();
        }
        if (value.getClass() == java.sql.Time.class) {
            java.sql.Time time = (java.sql.Time) value;
            if (destClz == Date.class)
                return (T) Date.from(time.toInstant());
            if (destClz == LocalDate.class)
                return (T) LocalDate.of(1970, 1, 1);
            if (destClz == LocalTime.class)
                return (T) time.toLocalTime();
            if (destClz == LocalDateTime.class)
                return (T) time.toLocalTime().atDate(LocalDate.of(1970, 1, 1));
        }
        if (value.getClass() == java.sql.Timestamp.class) {
            java.sql.Timestamp timestamp = (java.sql.Timestamp) value;
            if (destClz == Date.class)
                return (T) Date.from(timestamp.toInstant());
            if (destClz == LocalDate.class)
                return (T) timestamp.toLocalDateTime().toLocalDate();
            if (destClz == LocalTime.class)
                return (T) timestamp.toLocalDateTime().toLocalTime();
            if (destClz == LocalDateTime.class)
                return (T) timestamp.toLocalDateTime();
        }
        // 字符串类型
        // yyyy-MM-dd HH:mm:ss
        String formatTime = value.toString();
        final SimpleDateFormat[] timeFormats = {new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"),
                                                new SimpleDateFormat("yyyy-MM-dd"),
                                                new SimpleDateFormat("HH:mm:ss"),
                                                new SimpleDateFormat("HH:mm")};
        for (SimpleDateFormat format : timeFormats) {
            try {
                Date date = format.parse(formatTime);
                if (destClz == Date.class) return (T) date;
                return toDate(date, destClz);
            } catch (Exception e) {
                // 不作处理
            }
        }
        // 异常情况
        throw new ServiceException(String.format("对象转换失败，不支持的日期类型「from: %s(%s) to %s」", value.getClass().getSimpleName(), value.toString(), destClz.getSimpleName()));
    }

    /**
     * 日期对象转成格式化的字符串
     */
    private static String dateToString(Object value) {
        final SimpleDateFormat[] timeFormats = {new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"),
                                                new SimpleDateFormat("yyyy-MM-dd"),
                                                new SimpleDateFormat("HH:mm:ss"),
                                                new SimpleDateFormat("HH:mm")};

        if (value.getClass() == Date.class) {
            SimpleDateFormat aa = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return aa.format(new Date());
        }
        if (value.getClass() == LocalDate.class) {
            return ((LocalDate) value).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        if (value.getClass() == LocalTime.class) {
            return ((LocalTime) value).format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        }
        if (value.getClass() == LocalDateTime.class) {
            return ((LocalDateTime) value).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        if (value.getClass() == java.sql.Date.class) {
            return BeanMapper.map(value, LocalDate.class).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        if (value.getClass() == java.sql.Time.class) {
            return BeanMapper.map(value, LocalDateTime.class).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        if (value.getClass() == java.sql.Timestamp.class) {
            return BeanMapper.map(value, LocalDateTime.class).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        return null;
    }

    /**
     * 判断一个对象是否是基本类型或基本类型的封装类型
     * tps: 25000000
     */
    private static boolean isBaseType(Class<?> clz) {
        if (clz == null) return false;
        if (clz == String.class) return true;
        // 判断是否基本数据类型
        if (clz.isPrimitive()) return true;
        // 判断是否包装类
        try {
            return ((Class<?>) clz.getField("TYPE").get(null)).isPrimitive();
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean isNumeric(Class<?> clz) {
        if (clz == null) return false;
        return (clz == BigInteger.class || clz == BigDecimal.class);
    }

    private static boolean isDate(Class<?> clz) {
        if (clz == null) return false;
        return clz == Date.class ||
               clz == LocalDate.class ||
               clz == LocalTime.class ||
               clz == LocalDateTime.class;
    }

    /**
     * 数组转对象
     * [name,zhangsan,sex,man] -> {name: zhangsan, sex:man}
     */
    private static <T> T arraysToObject(Object[] objs, Class<T> beanClass) {
        Map<String, Object> map = arraysToMap(objs);
        if (beanClass == Map.class) {
            //noinspection unchecked
            return (T) map;
        }
        return mapToObject(map, beanClass);
    }

    /**
     * 数组转Map
     * [name,zhangsan,sex,man] -> {name: zhangsan, sex:man}
     */
    private static Map<String, Object> arraysToMap(Object[] objs) {
        if (objs == null || objs.length == 0) {
            return new HashMap<>();
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

    /**
     * map转对象
     */
    private static <T> T mapToObject(Map<String, Object> map, Class<T> beanClass) {
        if (map == null) {
            return null;
        }
        int setCoutn = 0;
        T obj = null;
        try {
            obj = beanClass.newInstance();
            if (map.isEmpty()) return obj;

            List<Field> fields = ReflectUtils.fields(obj.getClass());
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
                // todo 针对枚举

                int mod = field.getModifiers();
                if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                    continue;
                }
                field.setAccessible(true);

                // todo 这里如果value的类型和field的类型不同，还是会失败，尝试map一遍
                if (field.getType() != value.getClass()) {
                    value = map(value, field.getType());
                }
                field.set(obj, value);
                setCoutn++;
            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            //log.error(e.getMessage(), e);
        }
        return obj;
    }

    public static <T> T deepClone(T src) {
        try {
            Object obj = null;
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(src);
            objectOutputStream.close();
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            obj = objectInputStream.readObject();
            objectInputStream.close();
            //noinspection unchecked
            return (T) obj;
        } catch (Exception e) {
            throw new ServiceException("深度克隆对象异常：" + e.getMessage());
        }
    }

    public static void main(String[] args) throws NoSuchMethodException {
        // BigDecimal a = BeanMapper.mapper(BigDecimal.valueOf(10), BigDecimal.class);
        // System.out.println(a);
//        Object[] os = {null, int.class, Integer.class, Date.class};
//        long time = System.currentTimeMillis();
//        for (int i = 0; i < 25000000; i++) {
//            isBaseType((Class<?>) os[i % 3]);
//        }
//        System.out.println(System.currentTimeMillis() - time);
//
//        System.out.println(Integer.class.isPrimitive());

//        System.out.println(BeanMapper.mapper(new Date(), LocalDate.class).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
//        System.out.println(BeanMapper.mapper(new Date(), LocalTime.class).format(DateTimeFormatter.ofPattern("HH:mm:ss")));
//        System.out.println(BeanMapper.mapper(new Date(), LocalDateTime.class).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//        System.out.println("==========LocalDate -> Date LocalTime LocalDateTime");
//        System.out.println(timeFormat.format(BeanMapper.mapper(LocalDate.now(), Date.class)));
//        System.out.println(BeanMapper.mapper(LocalDate.now(), LocalTime.class).format(DateTimeFormatter.ofPattern("HH:mm:ss")));
//        System.out.println(BeanMapper.mapper(LocalDate.now(), LocalDateTime.class).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//        System.out.println("==========LocalTime -> Date LocalDate LocalDateTime");
//        System.out.println(timeFormat.format(BeanMapper.mapper(LocalTime.now(), Date.class)));
//        System.out.println(BeanMapper.mapper(LocalTime.now(), LocalDate.class).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
//        System.out.println(BeanMapper.mapper(LocalTime.now(), LocalDateTime.class).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//        System.out.println("==========LocalDateTime -> Date LocalDate LocalTime LocalDateTime");
//        System.out.println(timeFormat.format(BeanMapper.mapper(LocalDateTime.now(), Date.class)));
//        System.out.println(BeanMapper.mapper(LocalTime.now(), LocalDate.class).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
//        System.out.println(BeanMapper.mapper(LocalTime.now(), LocalDateTime.class).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//        System.out.println("==========字符串 -> Date LocalDate LocalTime LocalDateTime");
//        System.out.println(timeFormat.format(BeanMapper.mapper("2019-10-12", Date.class)));
//        System.out.println(BeanMapper.mapper("2019-10-12", LocalDate.class).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
//        System.out.println(BeanMapper.mapper("2019-10-12", LocalTime.class).format(DateTimeFormatter.ofPattern("HH:mm:ss")));
//        System.out.println(BeanMapper.mapper("2019-10-12", LocalDateTime.class).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//
//        System.out.println(timeFormat.format(BeanMapper.mapper("10:20:33", Date.class)));
//        System.out.println(BeanMapper.mapper("10:20:33", LocalDate.class).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
//        System.out.println(BeanMapper.mapper("10:20:33", LocalTime.class).format(DateTimeFormatter.ofPattern("HH:mm:ss")));
//        System.out.println(BeanMapper.mapper("10:20:33", LocalDateTime.class).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//
//        System.out.println(timeFormat.format(BeanMapper.mapper("2019-10-12 10:20:33", Date.class)));
//        System.out.println(BeanMapper.mapper("2019-10-12 10:20:33", LocalDate.class).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
//        System.out.println(BeanMapper.mapper("2019-10-12 10:20:33", LocalTime.class).format(DateTimeFormatter.ofPattern("HH:mm:ss")));
//        System.out.println(BeanMapper.mapper("2019-10-12 10:20:33", LocalDateTime.class).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

//        Map<String, Object> map = new HashMap<>();
//        map.put("id", "4546.0");
//        User user = new User();
//        user.setName("zhang san");
//        map.put("user", user);
//
//        long time = System.currentTimeMillis();
//        for (int i = 0; i < 250000; i++) {
//            BeanMapper.map(map, User.class);
//        }
//        System.out.println(System.currentTimeMillis() - time);

//        Map<String, Object> map = new HashMap<>();
//        map.put("name", "zhang");
//        map.put("age", "4");
//        map.put("user", new User());
//
//        User user = new User();
//        user.setId(1L);
//        user.setName("zhang san");
//        user.setPassword("1234567");
//        user.setModifier("sys");
//        user.setCreator("sys");
//        user.setGmtCreated(LocalDateTime.now());
//        user.setGmtModified(LocalDateTime.now());

//        User child = new User();
//        child.setId(2L);
//        child.setName("li si");
//        user.setChild(child);
//
//        long time =System.currentTimeMillis();
//        for(int i = 0; i < 10000000; i++) {
//            BeanMapper.map(user, BizUser.class);
//        }
//        System.out.println(System.currentTimeMillis() - time);

//        UserAdminInsertParam param = new UserAdminInsertParam();
//        param.setName("ZhangQinghua");
//        param.setPassword("123456");
//        param.setChild(child);
//
//        System.out.println(BeanMapper.map(param, User.class));

//        Object[] objs = new Object[]{"userName", "zhangsan"};
//        System.out.println(BeanMapper.map(objs, AdminUserAdminInsertParam.class));

        System.out.println(BeanMapper.map(new Date(), String.class));

        System.out.println(BeanMapper.map(LocalDate.now(), String.class));
    }


}
