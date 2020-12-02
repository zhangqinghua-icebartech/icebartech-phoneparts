package com.icebartech.core.utils;

import org.apache.commons.collections.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 校验工具类
 *
 * @author wenhsh
 */
public class ValidatorUtils {

    private static Validator validator;

    static {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    /**
     * 手动校验一个对象
     *
     * @param obj
     * @return
     */
    public static List<String> validate(Object obj) {
        List<String> errors = new ArrayList<>();
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(obj);
        if (CollectionUtils.isNotEmpty(constraintViolations)) {
            constraintViolations.forEach(e -> {
                String error = e.getMessage();
                errors.add(error);
            });
        }
        return errors;
    }
}
