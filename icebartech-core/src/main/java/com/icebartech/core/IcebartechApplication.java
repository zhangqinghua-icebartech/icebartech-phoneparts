package com.icebartech.core;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.concurrent.Executor;

/**
 * @author Anler
 */
@SpringBootApplication(scanBasePackages = "com.icebartech")
@EnableTransactionManagement
@EnableAsync
@EnableScheduling
@EntityScan("com.icebartech.**.po")
@EnableJpaRepositories("com.icebartech.**.repository")
@EnableEncryptableProperties
public class IcebartechApplication {

    public static void main(String[] args) {
        SpringApplication.run(IcebartechApplication.class, args);
    }

    /**
     * 异步方法线程池
     */
    @Bean
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //设置核心线程数
        executor.setCorePoolSize(10);
        // //设置最大线程数
        // executor.setMaxPoolSize(200);
        // //线程池所使用的缓冲队列
        // executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("icebartech-");
        executor.initialize();
        return executor;
    }

    /**
     * 跨域过滤器
     */
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", buildConfig());
        return new CorsFilter(source);
    }

    private CorsConfiguration buildConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        // 允许任何的head头部
        corsConfiguration.addAllowedHeader(CorsConfiguration.ALL);
        // 允许任何域名使用
        corsConfiguration.addAllowedOrigin(CorsConfiguration.ALL);
        // 允许任何的请求方法
        corsConfiguration.addAllowedMethod(CorsConfiguration.ALL);
        corsConfiguration.setAllowCredentials(true);
        return corsConfiguration;
    }

    /**
     * Java8时间序列化 并禁用对日期以时间戳方式输出
     *
     * @return
     */
    @Bean
    public ObjectMapper serializingObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

}
