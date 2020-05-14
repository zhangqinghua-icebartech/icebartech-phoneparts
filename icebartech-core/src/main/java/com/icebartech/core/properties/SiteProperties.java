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

}
