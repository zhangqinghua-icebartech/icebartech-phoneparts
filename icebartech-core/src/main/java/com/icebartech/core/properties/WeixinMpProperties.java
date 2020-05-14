package com.icebartech.core.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 公众号相关配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "weixin.mp")
public class WeixinMpProperties {

    private String token;
    private String appId;
    private String appSecret;

}
