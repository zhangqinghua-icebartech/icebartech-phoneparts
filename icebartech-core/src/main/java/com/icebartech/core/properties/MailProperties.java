package com.icebartech.core.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 邮箱配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "spring.mail")
public class MailProperties {

    /**
     * 收件人
     */
    private String from;

    /**
     * 路径
     */
    private String url;

    /**
     * 设备id
     */
    private String appid;

    /**
     * 秘钥
     */
    private String appKey;

    /**
     * 内容模板
     */
    private List<String> templates;

}
