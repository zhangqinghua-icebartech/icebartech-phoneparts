package com.icebartech.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 代表需要进行访问控制的注解，如果在方法中加了该注解，那么会经过AccessValveIntercetpor拦截器进行校验，校验通过后才可访问<br>
 * 注意：如果添加了该注解，那么必须要添加RequireLogin的注解，原因很简单：访问控制肯定要能拿当前登录用户的上下文信息
 *
 * @author Administrator
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireAccessControl {

}
