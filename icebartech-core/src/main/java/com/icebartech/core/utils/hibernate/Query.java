package com.icebartech.core.utils.hibernate;

import com.icebartech.core.utils.ReflectAsmUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.criteria.*;
import javax.persistence.criteria.CriteriaBuilder.In;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Query基类
 * 封装JPA CriteriaBuilder查询条件
 */
@Slf4j
@Data
@SuppressWarnings({"unused", "unchecked", "rawtypes", "null", "hiding"})
public class Query<T> implements Serializable {

    private static final long serialVersionUID = -3258216448586995690L;

    /**
     * 查询条件列表
     */
    private Root<T> root;

    private List<Predicate> predicates;

    private CriteriaQuery<?> criteriaQuery;

    private CriteriaBuilder criteriaBuilder;

    /**
     * 排序方式列表
     */
    private List<Order> orders;

    /**
     * 关联模式
     */
    private Map<String, Query<T>> subQuery;

    private Map<String, Query<T>> linkQuery;

    private String projection;

    /**
     * 或条件
     */
    private List<Query<T>> orQuery;

    private String groupBy;

    public Query(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        this.root = root;
        this.criteriaQuery = criteriaQuery;
        this.criteriaBuilder = criteriaBuilder;
        this.predicates = new ArrayList();
        this.orders = new ArrayList();
    }

    public Query(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder, List<FieldBean> fields) {
        this.root = root;
        this.criteriaQuery = criteriaQuery;
        this.criteriaBuilder = criteriaBuilder;
        this.predicates = new ArrayList();
        this.orders = new ArrayList();
        if (CollectionUtils.isNotEmpty(fields)) {
            for (FieldBean field : fields) {
                this.addCondition(field);
            }
        }
    }

    /**
     * 增加子查询
     */
    private void addSubQuery(String propertyName, Query<T> query) {
        if (this.subQuery == null) {
            this.subQuery = new HashMap();
        }

        if (query.projection == null) {
            throw new RuntimeException("子查询字段未设置");
        }

        this.subQuery.put(propertyName, query);
    }

    private void addSubQuery(Query<T> query) {
        addSubQuery(query.projection, query);
    }

    /**
     * 增关联查询
     */
    public void addLinkQuery(String propertyName, Query<T> query) {
        if (this.linkQuery == null) {
            this.linkQuery = new HashMap();
        }

        this.linkQuery.put(propertyName, query);
    }

    /**
     * 相等
     */
    public void eq(String propertyName, Object value) {
        if (isNullOrEmpty(value) || !ReflectAsmUtil.checkBeanHasField(root.getModel().getBindableJavaType(), propertyName)) {
            return;
        }
        this.predicates.add(criteriaBuilder.equal(root.get(propertyName), value));
    }

    private boolean isNullOrEmpty(Object value) {
        if (value instanceof String) {
            return StringUtils.isEmpty((String) value);
        }
        return value == null;
    }

    public void orLike(List<String> propertyName, String value) {
        if (isNullOrEmpty(value) || (propertyName.size() == 0)) {
            return;
        }
        if (!value.contains("%")) {
            value = "%" + value + "%";
        }
        Predicate predicate = criteriaBuilder.or(criteriaBuilder.like(root.get(propertyName.get(0)), value));
        for (int i = 1; i < propertyName.size(); ++i) {
            predicate = criteriaBuilder.or(predicate, criteriaBuilder.like(root.get(propertyName.get(i)), value));
        }
        this.predicates.add(predicate);
    }

    /**
     * 空
     */
    public void isNull(String propertyName) {
        this.predicates.add(criteriaBuilder.isNull(root.get(propertyName)));
    }

    /**
     * 非空
     */
    public void isNotNull(String propertyName) {
        this.predicates.add(criteriaBuilder.isNotNull(root.get(propertyName)));
    }

    /**
     * 不相等
     */
    public void notEq(String propertyName, Object value) {
        if (isNullOrEmpty(value)) {
            return;
        }
        this.predicates.add(criteriaBuilder.notEqual(root.get(propertyName), value));
    }

    /**
     * not in
     *
     * @param propertyName 属性名称
     * @param value        值集合
     */
    public void notIn(String propertyName, Collection value) {
        if ((value == null) || (value.size() == 0)) {
            return;
        }
        Iterator iterator = value.iterator();
        In in = criteriaBuilder.in(root.get(propertyName));
        while (iterator.hasNext()) {
            in.value(iterator.next());
        }
        this.predicates.add(criteriaBuilder.not(in));
    }

    /**
     * 模糊匹配
     *
     * @param propertyName 属性名称
     * @param value        属性值
     */
    public void like(String propertyName, String value) {
        if (isNullOrEmpty(value)) {
            return;
        }
        if (!value.contains("%")) {
            value = "%" + value + "%";
        }
        this.predicates.add(criteriaBuilder.like(root.get(propertyName).as(String.class), value));
    }

    /**
     * 模糊匹配
     *
     * @param propertyName 属性名称
     * @param value        属性值
     */
    public void notLike(String propertyName, String value) {
        if (isNullOrEmpty(value)) {
            return;
        }
        if (!value.contains("%")) {
            value = "%" + value + "%";
        }
        this.predicates.add(criteriaBuilder.notLike(root.get(propertyName).as(String.class), value));
    }

    /**
     * 时间区间查询
     *
     * @param propertyName 属性名称
     * @param lo           属性起始值
     * @param go           属性结束值
     */
    public void between(String propertyName, LocalDateTime lo, LocalDateTime go) {
        if (!isNullOrEmpty(lo) && !isNullOrEmpty(go)) {
            this.predicates.add(criteriaBuilder.between(root.get(propertyName), lo, go));
        }
    }


    /**
     * 小于等于
     *
     * @param propertyName 属性名称
     * @param value        属性值
     */
    public void le(String propertyName, Object value) {
        if (isNullOrEmpty(value)) {
            return;
        }
        if (value instanceof LocalDate) {
            this.predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(propertyName), (LocalDate) value));
        } else if (value instanceof LocalDateTime) {
            this.predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(propertyName), (LocalDateTime) value));
        } else {
            this.predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(propertyName), String.valueOf(value)));
        }
    }

    /**
     * 小于
     *
     * @param propertyName 属性名称
     * @param value        属性值
     */
    public void lt(String propertyName, Object value) {
        if (isNullOrEmpty(value)) {
            return;
        }
        if (value instanceof LocalDate) {
            this.predicates.add(criteriaBuilder.lessThan(root.get(propertyName), (LocalDate) value));
        } else if (value instanceof LocalDateTime) {
            this.predicates.add(criteriaBuilder.lessThan(root.get(propertyName), (LocalDateTime) value));
        } else {
            this.predicates.add(criteriaBuilder.lessThan(root.get(propertyName), String.valueOf(value)));
        }
    }

    /**
     * 大于等于
     *
     * @param propertyName 属性名称
     * @param value        属性值
     */
    public void ge(String propertyName, Object value) {
        if (isNullOrEmpty(value)) {
            return;
        }
        if (value instanceof LocalDate) {
            this.predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(propertyName), (LocalDate) value));
        } else if (value instanceof LocalDateTime) {
            this.predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(propertyName), (LocalDateTime) value));
        } else {
            this.predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(propertyName), String.valueOf(value)));
        }
    }

    /**
     * 大于
     *
     * @param propertyName 属性名称
     * @param value        属性值
     */
    public void gt(String propertyName, Object value) {
        if (isNullOrEmpty(value)) {
            return;
        }
        if (value instanceof LocalDate) {
            this.predicates.add(criteriaBuilder.greaterThan(root.get(propertyName), (LocalDate) value));
        } else if (value instanceof LocalDateTime) {
            this.predicates.add(criteriaBuilder.greaterThan(root.get(propertyName), (LocalDateTime) value));
        } else {
            this.predicates.add(criteriaBuilder.greaterThan(root.get(propertyName), String.valueOf(value)));
        }
    }

    /**
     * in
     *
     * @param propertyName 属性名称
     * @param value        值集合
     */
    public void in(String propertyName, Collection value) {
        if ((value == null) || (value.size() == 0)) {
            return;
        }
        Iterator iterator = value.iterator();
        In in = criteriaBuilder.in(root.get(propertyName));
        while (iterator.hasNext()) {
            in.value(iterator.next());
        }
        this.predicates.add(in);
    }

    public void or(Predicate... predicates) {
        this.predicates.add(criteriaBuilder.or(predicates));
    }

    public void and(Predicate... predicates) {
        this.predicates.add(criteriaBuilder.and(predicates));
    }

    /**
     * 直接添加JPA内部的查询条件,用于应付一些复杂查询的情况,例如或
     */
    public void addCriterions(Predicate predicate) {
        this.predicates.add(predicate);
    }

    /**
     * 创建查询条件
     *
     * @return JPA离线查询
     */
    public CriteriaQuery newCriteriaQuery() {
        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        if (!isNullOrEmpty(groupBy)) {
            criteriaQuery.groupBy(root.get(groupBy));
        }
        if (CollectionUtils.isNotEmpty(orders)) {
            criteriaQuery.orderBy(orders);
        } else {
            criteriaQuery.orderBy(criteriaBuilder.desc(root.get("id")));
        }
        addLinkCondition(this);
        return criteriaQuery;
    }

    public Predicate newSpecification() {
        return newCriteriaQuery().getRestriction();
    }

    public void addCondition(FieldBean field) {
        Object fieldValue = field.getValue();
        String fieldName = field.getName();
        if (!ReflectAsmUtil.checkBeanHasField(root.getModel().getBindableJavaType(), fieldName)) {
            // entity没有这个字段 忽略
            return;
        }
        switch (field.getQueryType()) {
            case LIKE:
                this.like(fieldName, String.valueOf(fieldValue));
                break;
            case NOT_LIKE:
                this.notLike(fieldName, String.valueOf(fieldValue));
                break;
            case GT:
                this.gt(fieldName, fieldValue);
                break;
            case GE:
                this.ge(fieldName, fieldValue);
                break;
            case LT:
                this.lt(fieldName, fieldValue);
                break;
            case LE:
                this.le(fieldName, fieldValue);
                break;
            case NULL:
                if (fieldValue instanceof Boolean && (Boolean) fieldValue) {
                    this.isNull(fieldName);
                }
                break;
            case NOT_NULL:
                if (fieldValue instanceof Boolean && (Boolean) fieldValue) {
                    this.isNotNull(fieldName);
                }
                break;
            case IN:
                if (fieldValue instanceof Collection) {
                    this.in(fieldName, (Collection) fieldValue);
                } else {
                    this.in(fieldName, Arrays.asList(fieldValue.toString().split(",")));
                }
                break;
            case NOT_IN:
                if (fieldValue instanceof Collection) {
                    this.notIn(fieldName, (Collection) fieldValue);
                } else {
                    this.notIn(fieldName, Arrays.asList(fieldValue.toString().split(",")));
                }
                break;
            case NOT_EQ:
                this.notEq(fieldName, String.valueOf(fieldValue));
                break;
            case ASC:
                this.addOrder(fieldName, QueryType.ASC);
                break;
            case DESC:
                this.addOrder(fieldName, QueryType.DESC);
                break;
            default:
                this.eq(fieldName, fieldValue);
                break;
        }
    }

    private void addLinkCondition(Query<T> query) {
        Map<String, Query<T>> subQuery = query.linkQuery;
        if (subQuery == null) {
            return;
        }

        for (Object o : subQuery.keySet()) {
            String key = (String) o;
            Query<T> sub = subQuery.get(key);
            root.join(key);
            criteriaQuery.where(sub.predicates.toArray(new Predicate[0]));
            addLinkCondition(sub);
        }
    }

    public void addOrder(String propertyName, QueryType order) {
        if (propertyName == null) {
            return;
        }

        if (this.orders == null) {
            this.orders = new ArrayList();
        }

        switch (order) {
            case ASC:
                this.orders.add(criteriaBuilder.asc(root.get(propertyName)));
                break;
            case DESC:
                this.orders.add(criteriaBuilder.desc(root.get(propertyName)));
                break;
            default:
                break;
        }
    }

    public void setOrder(String propertyName, QueryType order) {
        this.orders = null;
        addOrder(propertyName, order);
    }

}
