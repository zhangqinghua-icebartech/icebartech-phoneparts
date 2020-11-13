package com.icebartech.core.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 微信APP登陆相关配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "weixin.app")
public class WeixinAppProperties {

    private String appId;

    private String appSecret;

}
