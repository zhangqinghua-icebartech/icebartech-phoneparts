package com.icebartech.core.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * 系统基本配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "site")
public class SiteProperties {

    private String appName;

    private String explanation;

    private String host;

    private Long version = 1L;

    private Duration sessionExpires = Duration.ofMillis(30);

    /**
     * IP访问限制：连续访问最高阀值，超过该值则认定为恶意操作的IP，进行限制
     */
    private Long limitNumber = 100L;

    /**
     * IP访问限制：访问最小安全时间，在该时间内如果访问次数大于阀值，则记录为恶意IP，否则视为正常访问（单位：ms）
     */
    private Long limitSafeTime = 1000L;

    /**
     * IP访问限制：恶意IP关黑屋时间（ms）
     */
    private Long limitDisableTime = 0L;
}
