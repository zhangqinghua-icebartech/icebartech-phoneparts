package com.icebartech.core.lock;

import com.icebartech.core.components.RedisComponent;
import com.icebartech.core.enums.CommonResultCodeEnum;
import com.icebartech.core.exception.ServiceException;
import javassist.*;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Aspect
@Component
public class LockMethodAspect {

    @Autowired
    private RedisComponent redisComponent;

    @Around("@annotation(com.icebartech.core.lock.RedisLock)")
    public Object around(ProceedingJoinPoint joinPoint) {
        // 1. 获取注解
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RedisLock redisLock = method.getAnnotation(RedisLock.class);

        // 2. 获取Key，取类名+方法名
        String key = getKey(joinPoint, method, redisLock.key());

        // 3. 加锁
        try {
            long waitTime = redisLock.waitTime() * 1000;

            while (waitTime >= 0) {
                boolean islock = redisComponent.lock(key, redisLock.expire());
                if (islock) {
                    log.info("获取锁成功：" + key);
                    return joinPoint.proceed();
                }

                log.warn("获取锁失败，0.1秒后重试：" + key);
                waitTime -= 100;
                Thread.sleep(100);
            }

            throw new ServiceException(CommonResultCodeEnum.INTERFACE_INNER_INVOKE_ERROR, "获取锁超时：" + key);
        }
        // 4.1 方法中出现的业务异常
        catch (ServiceException e) {
            throw e;
        }
        // 4.2 其它异常
        catch (Throwable throwable) {
            throwable.printStackTrace();
            throw new ServiceException(CommonResultCodeEnum.INTERFACE_INNER_INVOKE_ERROR,"获取锁异常：" + key);
        }
        // 4.3 释放锁
        finally {
            redisComponent.unlock(key);
            log.info("释放锁：" + key);
        }
    }

    /**
     * 获取
     * 1. 注解参数为null，直接返回。
     * 2. 注解参数为字符串，返回字符串。
     * 3. 注解参数为带#字符串，返回此字符串对应的方法参数值
     */
    private static String getKey(ProceedingJoinPoint joinPoint, Method method, String key) {
        // 1. 获取参数
        String redisKey = FieldsUtil.methodFullName(method);

        if (StringUtils.isBlank(key)) {
            return redisKey;
        }
        if (!key.startsWith("#")) {
            return redisKey + "." + key;
        }

        key = key.substring(1);
        Map<String, Object> argsMap = getArgs(joinPoint);
        if (!argsMap.containsKey(key)) {
            throw new ServiceException(CommonResultCodeEnum.INTERFACE_INNER_INVOKE_ERROR,"方法参数不存在: " + key);
        }
        return redisKey + ":" + argsMap.get(key);
    }

    /**
     * 通过反射机制 获取被切参数名以及参数值
     */
    private static Map<String, Object> getArgs(ProceedingJoinPoint joinPoint) {
        try {
            String classType = joinPoint.getTarget().getClass().getName();
            Class<?> clazz = Class.forName(classType);

            // 获取类名称
            String clazzName = clazz.getName();
            // 获取方法名称
            String methodName = joinPoint.getSignature().getName();
            // 获取方法参数
            Object[] args = joinPoint.getArgs();
            Map<String, Object> map = new HashMap<>();

            ClassPool pool = ClassPool.getDefault();
            //ClassClassPath classPath = new ClassClassPath(this.getClass());
            ClassClassPath classPath = new ClassClassPath(clazz);
            pool.insertClassPath(classPath);

            CtClass cc = pool.get(clazzName);
            CtMethod cm = cc.getDeclaredMethod(methodName);
            MethodInfo methodInfo = cm.getMethodInfo();
            CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
            LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
            if (attr != null) {
                // String[] paramNames = new String[cm.getParameterTypes().length];
                int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
                for (int i = 0; i < cm.getParameterTypes().length; i++) {
                    map.put(attr.variableName(i + pos), args[i]);//paramNames即参数名
                }
            }
            return map;
        } catch (ClassNotFoundException | NotFoundException e) {
            throw new ServiceException("切面获取方法参数异常：" + e.getMessage(), e);
        }
    }
}
