package com.icebartech.core.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 短信基本配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "spring.sms")
public class SmsProperties {

    private List<String> url;

    private List<String> appId;

    private List<String> appKey;

    private List<String> projectCodes;

    private String mode;

}
