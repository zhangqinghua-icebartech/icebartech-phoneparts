package com.icebartech.core.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 阿里云oss配置
 *
 * @author wenhsh
 */
@Data
@Component
@ConfigurationProperties(prefix = "aliyun")
public class AliyunProperties {

    /**
     * access_id
     */
    private String accessId;

    /**
     * access_key
     */
    private String accessKey;

    /**
     * endpoint
     */
    private String ossEndpoint;

    /**
     * 域名配置
     */
    private String ossUrl;

    /**
     * bucket名称
     */
    private String bucketName;

}
