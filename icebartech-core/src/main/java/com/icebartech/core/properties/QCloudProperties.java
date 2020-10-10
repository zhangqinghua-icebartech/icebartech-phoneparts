package com.icebartech.core.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "qcloud")
public class QCloudProperties {

    private String appid;

    private String bucketName;

    private String region;

    private String secretId;

    private String secretKey;

    private String cosUrl;

}
