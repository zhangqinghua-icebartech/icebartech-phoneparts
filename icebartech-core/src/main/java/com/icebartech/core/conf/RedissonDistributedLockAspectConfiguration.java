//package com.icebartech.core.conf;
//
//import com.icebartech.core.annotations.LockAction;
//import com.icebartech.core.enums.CommonResultCodeEnum;
//import com.icebartech.core.exception.ServiceException;
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.redisson.RedissonMultiLock;
//import org.redisson.api.RLock;
//import org.redisson.api.RedissonClient;
//import org.redisson.spring.starter.RedissonAutoConfiguration;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.AutoConfigureAfter;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
//import org.springframework.expression.EvaluationContext;
//import org.springframework.expression.ExpressionParser;
//import org.springframework.expression.common.TemplateParserContext;
//import org.springframework.expression.spel.standard.SpelExpressionParser;
//import org.springframework.expression.spel.support.StandardEvaluationContext;
//
//import java.lang.reflect.Method;
//import java.util.concurrent.locks.Lock;
//
//@Aspect
//@Configuration
//@AutoConfigureAfter(RedissonAutoConfiguration.class)
//@EnableAutoConfiguration
//@Slf4j
//public class RedissonDistributedLockAspectConfiguration {
//
//    @Autowired
//    private RedissonClient redissonClient;
//
//    private ExpressionParser parser = new SpelExpressionParser();
//
//    private LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();
//
//    @Pointcut("@annotation(com.icebartech.core.annotations.LockAction)")
//    private void lockPoint() {
//
//    }
//
//    @Around("lockPoint()")
//    public Object around(ProceedingJoinPoint pjp) throws Throwable {
//        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
//        LockAction lockAction = method.getAnnotation(LockAction.class);
//        Object[] args = pjp.getArgs();
//        String[] keys = parse(lockAction.value(), method, args);
//
//        Lock lock = null;
//        if (1 < keys.length) {
//            // 多个key 使用联锁
//            RLock[] locks = new RLock[keys.length];
//            for (int i = 0; i < keys.length; i++) {
//                locks[i] = getLock(keys[i], lockAction);
//            }
//            lock = new RedissonMultiLock(locks);
//            if (!((RedissonMultiLock) lock).tryLock(lockAction.waitTime(), lockAction.leaseTime(), lockAction.unit())) {
//                log.debug("is lock failed [{}]", keys);
//                throw new ServiceException(CommonResultCodeEnum.INTERFACE_INNER_INVOKE_ERROR, "函数加锁失败");
//            }
//        } else if (1 == keys.length) {
//            // 单个key 正常加锁
//            lock = getLock(keys[0], lockAction);
//            if (!((RLock) lock).tryLock(lockAction.waitTime(), lockAction.leaseTime(), lockAction.unit())) {
//                log.debug("is lock failed [{}]", keys[0]);
//                throw new ServiceException(CommonResultCodeEnum.INTERFACE_INNER_INVOKE_ERROR, "函数加锁失败");
//            }
//        }
//
//        //得到锁,执行方法，释放锁
//        try {
//            log.debug("is lock success [{}]", keys);
//            return pjp.proceed();
//        } catch (ServiceException e) {
//            throw e;
//        } catch (Exception e) {
//            log.error("execute locked method occured an exception", e);
//            throw e;
//        } finally {
//            if (null != lock) {
//                lock.unlock();
//            }
//            log.debug("release lock [{}]", keys);
//        }
//    }
//
//    /**
//     * @param keys   表达式
//     * @param method 方法
//     * @param args   方法参数
//     * @return
//     * @description 解析spring EL表达式
//     */
//    private String[] parse(String[] keys, Method method, Object[] args) {
//        String[] keysParse = new String[keys.length];
//        for (int i = 0; i < keys.length; i++) {
//            String[] params = discoverer.getParameterNames(method);
//            EvaluationContext context = new StandardEvaluationContext();
//            if (null != params && 0 < params.length) {
//                for (int j = 0; j < params.length; j++) {
//                    context.setVariable(params[j], args[j]);
//                }
//            }
//            keysParse[i] = parser.parseExpression(keys[i], new TemplateParserContext()).getValue(context, String.class);
//        }
//        return keysParse;
//    }
//
//    private RLock getLock(String key, LockAction lockAction) {
//        switch (lockAction.lockType()) {
//            case REENTRANT_LOCK:
//                return redissonClient.getLock(key);
//            case FAIR_LOCK:
//                return redissonClient.getFairLock(key);
//            case READ_LOCK:
//                return redissonClient.getReadWriteLock(key).readLock();
//            case WRITE_LOCK:
//                return redissonClient.getReadWriteLock(key).writeLock();
//            default:
//                throw new RuntimeException("do not support lock type:" + lockAction.lockType().name());
//        }
//    }
//
//}
//
