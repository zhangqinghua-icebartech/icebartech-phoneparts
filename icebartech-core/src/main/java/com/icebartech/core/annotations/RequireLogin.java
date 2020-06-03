package com.icebartech.core.annotations;

import com.icebartech.core.constants.UserEnum;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireLogin {
    @AliasFor("value")
    UserEnum[] userEnum() default {};

    @AliasFor("userEnum")
    UserEnum[] value() default {};

    /**
     * 忽略某些用户类型
     *
     * @return
     */
    UserEnum[] ignore() default {};
}
