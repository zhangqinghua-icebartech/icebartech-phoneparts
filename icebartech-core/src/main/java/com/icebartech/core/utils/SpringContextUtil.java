package com.icebartech.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * spring bean管理工具类
 *
 * @author haosheng.wenhs
 */
@Component
@Slf4j
public class SpringContextUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;
    private static boolean flag = false;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        setValue(applicationContext);
    }

    public static void setValue(ApplicationContext applicationContext) {
        SpringContextUtil.applicationContext = applicationContext;
        SpringContextUtil.flag = true;
    }

    public static ApplicationContext getApplicationContext() {
        if (applicationContext == null) {
            if (flag == true) {
                return null;
            }
            flag = true;
            applicationContext = new ClassPathXmlApplicationContext(new String[]{"classpath*:/spring*.xml"});
            flag = false;
        }
        return applicationContext;
    }

    /**
     * 获取一个以name为名字注册的spring bean对象
     *
     * @param name bean名字
     * @return
     * @throws BeansException
     */
    public static Object getBean(String name) throws BeansException {
        return applicationContext.getBean(name);
    }

    /**
     * 获取类型为requireType的对象
     *
     * @param requireType 对象类型
     * @return
     * @throws BeansException
     */
    public static <T> T getBean(Class<T> requireType) throws BeansException {
        return applicationContext.getBean(requireType);
    }

    /**
     * 获取类型为requireType的对象
     *
     * @param name        对象名字
     * @param requireType 对象类型
     * @return
     * @throws BeansException
     */
    public static <T> T getBean(String name, Class<T> requireType) throws BeansException {
        return applicationContext.getBean(name, requireType);
    }

    /**
     * 获取spring容器中是否包含一个名字为name的bean
     *
     * @param name
     * @return
     */
    public static boolean containsBean(String name) {
        return applicationContext.containsBean(name);
    }

    /**
     * 获取一个bean的类型
     *
     * @param name bean名字
     * @return
     * @throws NoSuchBeanDefinitionException
     */
    public static Class<?> getType(String name) throws NoSuchBeanDefinitionException {

        return applicationContext.getType(name);
    }

    /**
     * 获取一个bean定义的所有别名
     *
     * @param name bean名字
     * @return
     * @throws NoSuchBeanDefinitionException
     */
    public static String[] getAliases(String name) throws NoSuchBeanDefinitionException {

        return applicationContext.getAliases(name);
    }

    /**
     * jdbc template
     */
    public static JdbcTemplate getJdbcTemplate() {
        return (JdbcTemplate) getApplicationContext().getBean("userJdbcTemplate");
    }


    /**
     * datasource xml, read all jdbc template
     */
    public static DataSource getDataSource() {
        return ((DataSource) getApplicationContext().getBean("user_dbDataSource"));
    }


    /**
     * connection
     */
    public static Connection getConnection() {
        try {
            return getDataSource().getConnection();
        } catch (Exception e) {
            log.error("", e);
            return null;
        }
    }

    /**
     * 获取当前环境
     */
    public static String getActiveProfile() {
        return applicationContext.getEnvironment().getActiveProfiles()[0];
    }
}
