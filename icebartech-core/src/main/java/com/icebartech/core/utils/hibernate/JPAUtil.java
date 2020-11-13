package com.icebartech.core.utils.hibernate;

import com.icebartech.core.enums.CommonResultCodeEnum;
import com.icebartech.core.exception.ServiceException;
import com.icebartech.core.params.PageParam;
import com.icebartech.core.utils.ReflectAsmUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.Assert;

import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Anler
 */
@Slf4j
public class JPAUtil {

    public static <T> Specification<T> getSpec(List<FieldBean> fields) {
        fields.removeIf(s -> s.getValue() == null);
        return (root, cq, cb) -> new Query<>(root, cq, cb, fields).newSpecification();
    }

    public static Pageable getPageable(List<FieldBean> fields) {
        Assert.notNull(fields, "fields cannot be null!");

        List<Sort.Order> orders = new ArrayList<>();
        String pageIndexStr = "1", pageSizeStr = "10";
        for (FieldBean field : fields) {
            if ("pageIndex".equals(field.getName())) {
                pageIndexStr = String.valueOf(field.getValue());
            } else if ("pageSize".equals(field.getName())) {
                pageSizeStr = String.valueOf(field.getValue());
            } else if (field.getQueryType() == QueryType.ASC) {
                orders.add(new Sort.Order(Sort.Direction.ASC, field.getName()));
            } else if (field.getQueryType() == QueryType.DESC) {
                orders.add(new Sort.Order(Sort.Direction.DESC, field.getName()));
            }
        }

        if (!NumberUtils.isParsable(pageIndexStr) || !NumberUtils.isParsable(pageSizeStr)) {
            throw new ServiceException(CommonResultCodeEnum.INTERFACE_INNER_INVOKE_ERROR, "pageIndex or pageSize cannot Parsable to number!");
        }
        int pageIndex = Integer.parseInt(pageIndexStr), pageSize = Integer.parseInt(pageSizeStr);
        pageIndex = pageIndex < 1 ? 1 : pageIndex;
        pageSize = pageSize < 1 ? 10 : pageSize;


        return PageRequest.of(pageIndex - 1, pageSize, Sort.by(orders));
    }

    public static Pageable getPageable(PageParam pageParam) {
        return PageRequest.of(pageParam.getPageIndex() - 1, pageParam.getPageSize());
    }

    public static Pageable getPageable(Integer pageIndex, Integer pageSize) {
        pageSize = pageSize == null ? 10 : pageSize < 1 ? 1 : pageSize;
        pageIndex = pageIndex == null ? 1 : pageSize < 1 ? 1 : pageIndex;
        return PageRequest.of(pageIndex - 1, pageSize);
    }

    public static Pageable getPageable(Integer pageSize) {
        pageSize = pageSize == null ? 10 : pageSize < 1 ? 1 : pageSize;
        return PageRequest.of(0, pageSize);
    }

    public static <T> TypedQuery<T> querySetParameter(TypedQuery<T> query, Object param) {
        List<FieldBean> fieldsInfo = ReflectAsmUtil.getFields(param);
        for (FieldBean field : fieldsInfo) {
            try {
                if (null != field.getValue()) {
                    query.setParameter(field.getName(), field.getValue());
                }
            } catch (IllegalArgumentException e) {
                log.debug(e.getMessage());
            }
        }
        return query;
    }
}