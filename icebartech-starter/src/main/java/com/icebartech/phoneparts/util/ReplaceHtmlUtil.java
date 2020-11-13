package com.icebartech.phoneparts.util;

import com.icebartech.core.properties.AliyunProperties;
import com.icebartech.core.utils.AliyunOSSUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class ReplaceHtmlUtil {

    public static ReplaceHtmlUtil instance;
    @Autowired
    private AliyunProperties aliyunProperties;

    public static String replaceHtmlTag(String str) {
        if (instance.aliyunProperties == null) return null;
        try {
            String regxpForTag = "<\\s*" + "img" + "\\s+([^>]*)\\s*";
            String regxpForTagAttrib = "src=\\s*\"([^\"]+)\"";
            Pattern patternForTag = Pattern.compile(regxpForTag, Pattern.CASE_INSENSITIVE);
            Pattern patternForAttrib = Pattern.compile(regxpForTagAttrib, Pattern.CASE_INSENSITIVE);
            Matcher matcherForTag = patternForTag.matcher(str);
            StringBuffer sb = new StringBuffer();
            boolean result = matcherForTag.find();
            while (result) {
                StringBuffer sbreplace = new StringBuffer("<img ");
                Matcher matcherForAttrib = patternForAttrib.matcher(matcherForTag.group(1));
                if (matcherForAttrib.find()) {
                    String attributeStr = matcherForAttrib.group(1);
                    if (attributeStr.startsWith(instance.aliyunProperties.getOssUrl())) {
                        URL url = new URL(attributeStr);
                        String fileKey = attributeStr.replaceAll(instance.aliyunProperties.getOssUrl(), "").replaceAll("\\?" + url.getQuery(), "");
                        fileKey = fileKey.startsWith("/") ? fileKey.substring(1) : fileKey;
                        attributeStr = AliyunOSSUtil.generateurl(fileKey);
                    }
                    matcherForAttrib.appendReplacement(sbreplace, "src= \"" + attributeStr + "\"");
                }
                matcherForAttrib.appendTail(sbreplace);
                matcherForTag.appendReplacement(sb, sbreplace.toString());
                result = matcherForTag.find();
            }
            matcherForTag.appendTail(sb);
            return sb.toString();
        } catch (Exception e) {
            log.error("图片转换发生错误" + e.getMessage());
        }
        return null;
    }

    @PostConstruct
    public void init() {
        instance = this;
    }
}
