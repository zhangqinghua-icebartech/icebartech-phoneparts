package com.icebartech.core.vo;

import com.icebartech.core.po.BasePo;
import com.icebartech.core.utils.BeanMapper;
import com.icebartech.core.utils.ReflectAsmUtil;
import com.icebartech.core.utils.hibernate.FieldBean;
import com.icebartech.core.utils.hibernate.Property;
import com.icebartech.core.utils.hibernate.QueryType;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class QueryParam {

    public static <T extends BasePo> FieldBean eq(Property<T, ?> property, Object val) {
        return warp(property, val, QueryType.EQ);
    }

    public static <T extends BasePo> FieldBean notEq(Property<T, ?> property, Object val) {
        return warp(property, val, QueryType.NOT_EQ);
    }

    public static <T extends BasePo> FieldBean like(Property<T, ?> property, String val) {
        return warp(property, val, QueryType.LIKE);
    }

    public static <T extends BasePo> FieldBean notLike(Property<T, ?> property, String val) {
        return warp(property, val, QueryType.NOT_LIKE);
    }


    public static <T extends BasePo> FieldBean gt(Property<T, ?> property, Object val) {
        return warp(property, val, QueryType.GT);
    }

    public static <T extends BasePo> FieldBean ge(Property<T, ?> property, Object val) {
        return warp(property, val, QueryType.GE);
    }

    public static <T extends BasePo> FieldBean lt(Property<T, ?> property, Object val) {
        return warp(property, val, QueryType.LT);
    }

    public static <T extends BasePo> FieldBean le(Property<T, ?> property, Object val) {
        return warp(property, val, QueryType.LE);
    }

    public static <T extends BasePo> FieldBean isNull(Property<T, ?> property, boolean val) {
        return warp(property, val, QueryType.NULL);
    }

    public static <T extends BasePo> FieldBean notNull(Property<T, ?> property, boolean val) {
        return warp(property, val, QueryType.NOT_NULL);
    }

    public static <T extends BasePo> FieldBean in(Property<T, ?> property, Collection val) {
        return warp(property, val, QueryType.IN);
    }

    public static <T extends BasePo> FieldBean notIn(Property<T, ?> property, Collection val) {
        return warp(property, val, QueryType.NOT_IN);
    }

    public static <T extends BasePo> FieldBean asc(Property<T, ?> property) {
        return warp(property, null, QueryType.ASC);
    }

    public static <T extends BasePo> FieldBean desc(Property<T, ?> property) {
        return warp(property, null, QueryType.DESC);
    }


    /**
     * 专门用来新增/修改用的，跟eq一样
     */
    public static <T extends BasePo> FieldBean attr(Property<T, ?> property, Object val) {
        return warp(property, val, QueryType.EQ);
    }

    public static FieldBean id(Long id) {
        FieldBean fieldBean = new FieldBean();
        fieldBean.setName("id");
        fieldBean.setType(id.getClass());
        fieldBean.setValue(id);
        return fieldBean;
    }

    public static FieldBean pageSize(Integer val) {
        val = null == val ? 1 : val;
        FieldBean fieldBean = new FieldBean();
        fieldBean.setName("pageSize");
        fieldBean.setType(val.getClass());
        fieldBean.setValue(val);
        return fieldBean;
    }

    public static FieldBean pageIndex(Integer val) {
        val = null == val ? 10 : val;
        FieldBean fieldBean = new FieldBean();
        fieldBean.setName("pageIndex");
        fieldBean.setType(val.getClass());
        fieldBean.setValue(val);
        return fieldBean;
    }

    public static List<FieldBean> object2FieldBeans(Object param) {
        List<FieldBean> fieldBeans = ReflectAsmUtil.getFields(param);
        warpFieldBeans(fieldBeans);
        return fieldBeans;

    }

    public static List<FieldBean> arrays2FieldBeans(Object... fieldValuePairs) {
        @SuppressWarnings("unchecked")
        Map<String, Object> params = BeanMapper.map(fieldValuePairs, Map.class);
        List<FieldBean> fieldBeans = ReflectAsmUtil.getFields(params);
        warpFieldBeans(fieldBeans);
        return fieldBeans;
    }

    private static void warpFieldBeans(List<FieldBean> fieldBeans) {
        if (fieldBeans == null) return;

        for (FieldBean fieldBean : fieldBeans) {
            String fieldName = fieldBean.getName();
            QueryType fieldType = QueryType.EQ;

            for (QueryType type : QueryType.values()) {
                if (!type.equals(QueryType.EQ)) {
                    int lastIndex = fieldName.lastIndexOf(type.key);
                    if (fieldName.endsWith(type.key)) {
                        fieldName = fieldName.substring(0, lastIndex);
                        fieldType = type;
                        break;
                    }
                }
            }

            fieldBean.setQueryType(fieldType);
            fieldBean.setName(fieldName);
        }
    }


    public static String fieldBeansToString(List<FieldBean> fieldBeans) {
        if (fieldBeans == null) return null;
        StringBuilder sb = new StringBuilder();
        for (FieldBean fieldBean : fieldBeans) {
            // 分页查询pageSize，pageIndex没有queryType
            if (fieldBean.getQueryType() == null) continue;
            sb.append(String.format("%s(%s, %s),", fieldBean.getQueryType().key, fieldBean.getName(), String.valueOf(fieldBean.getValue())));
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    public static String object2Totring(Object param) {
        return fieldBeansToString(object2FieldBeans(param));
    }


    public static String fieldValuePairsToString(Object... fieldValuePairs) {
        return fieldBeansToString(arrays2FieldBeans(fieldValuePairs));
    }

    private static <T extends BasePo> FieldBean warp(Property<T, ?> property, Object val, QueryType queryType) {
        FieldBean fieldBean = new FieldBean();
        fieldBean.setName(property.getName());
        fieldBean.setType(null == val ? null : val.getClass());
        fieldBean.setValue(val);
        fieldBean.setQueryType(queryType);
        return fieldBean;
    }
}
