package com.icebartech.core.utils;

import com.icebartech.core.components.AliyunOSSComponent;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * 阿里云OSS工具
 */
@Component
public class AliyunOSSUtil {

    private static AliyunOSSUtil instance;

    @Autowired
    private AliyunOSSComponent aliyunOSSComponent;

    public static String generateurl(String fileKey) {
        if (instance.aliyunOSSComponent == null) return null;

        if (StringUtils.isBlank(fileKey)) return null;
        String fileUrl = instance.aliyunOSSComponent.generateDownloadUrl(fileKey.split(",")[0]);
        if (org.apache.commons.lang3.StringUtils.isNotBlank(fileUrl)) {
            return fileUrl;
        }
        return null;
    }

    public static List<String> generateurls(String filekeys) {
        if (instance.aliyunOSSComponent == null) return null;

        if (org.apache.commons.lang3.StringUtils.isBlank(filekeys)) return new ArrayList<>();
        List<String> imageList = new ArrayList<>();
        for (String fileKey : filekeys.split(",")) {
            String fileUrl = instance.aliyunOSSComponent.generateDownloadUrl(fileKey);
            if (org.apache.commons.lang3.StringUtils.isNotBlank(fileUrl)) imageList.add(fileUrl);
        }
        return imageList;
    }

    @PostConstruct
    private void init() {
        instance = this;
    }
}
