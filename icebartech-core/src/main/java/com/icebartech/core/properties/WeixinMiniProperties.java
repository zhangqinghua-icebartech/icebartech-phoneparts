package com.icebartech.core.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 小程序相关配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "weixin.mini")
public class WeixinMiniProperties {

    private String appId;

    private String appSecret;

}
