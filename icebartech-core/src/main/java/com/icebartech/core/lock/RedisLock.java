package com.icebartech.core.lock;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface RedisLock {

    /**
     * 第几个参数
     */
    int index();

    /**
     * 锁的过期秒数,默认是5秒
     */
    int expire() default 10;

    /**
     * 尝试加锁秒数，最多等待时间
     */
    long waitTime() default 100;
}
