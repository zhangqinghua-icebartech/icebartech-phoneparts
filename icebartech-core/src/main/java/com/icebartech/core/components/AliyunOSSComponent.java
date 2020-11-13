package com.icebartech.core.components;

import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PolicyConditions;
import com.aliyun.oss.model.PutObjectResult;
import com.icebartech.core.enums.CommonResultCodeEnum;
import com.icebartech.core.exception.ServiceException;
import com.icebartech.core.properties.AliyunProperties;
import com.icebartech.core.properties.SiteProperties;
import com.icebartech.core.utils.IdGenerator;
import com.icebartech.core.vo.AttachmentInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 阿里云oss操作组件类
 *
 * @author wenhsh
 */
@Component
@Slf4j
public class AliyunOSSComponent {

    @Autowired
    private AliyunProperties aliyunProperties;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private SiteProperties siteProperties;

    private OSSClient getClient() {
        return new OSSClient(aliyunProperties.getOssEndpoint(), aliyunProperties.getAccessId(), aliyunProperties.getAccessKey());
    }

    private void closeClient(OSSClient client) {
        if (null != client) {
            client.shutdown();
        }
    }

    public String uploadFile(InputStream inputStream, String suffix, String fileName) {
        String fileKey = siteProperties.getAppName() + "/" + idGenerator.getFileKey(suffix);

        OSSClient client = getClient();
        try {
            fileName = StringUtils.isEmpty(fileName) ? fileKey : fileName;
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentDisposition("attachment; filename=\"" + fileName + "\"");
            PutObjectResult result = client.putObject(aliyunProperties.getBucketName(), fileKey, inputStream, metadata);
            String eTag = result.getETag();
            return StringUtils.isEmpty(eTag) ? null : fileKey;
        } finally {
            closeClient(client);
        }
    }

    public String uploadFile(byte[] fileBytes, String suffix, String fileName) {
        return uploadFile(new ByteArrayInputStream(fileBytes), suffix, fileName);
    }

    public String uploadFile(String fileUrl, String suffix, String fileName) {
        try {
            return uploadFile(new URL(fileUrl).openStream(), suffix, fileName);
        } catch (IOException e) {
            throw new ServiceException(CommonResultCodeEnum.INTERFACE_OUTER_INVOKE_ERROR, "文件上传OSS失败,fileUrl: " + fileUrl);
        }
    }

    private String getOSSUrl() {
        String url = aliyunProperties.getOssUrl();
        return url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
    }

    /**
     * 生成带签名的文件上传链接 只能用PUT方式上传 不支持小程序
     *
     * @param suffix
     * @return
     */
    public Map<String, String> generateUploadUrl(String suffix, String contentType) {
        Map<String, String> rtnMap = new HashMap<>();

        String fileKey = siteProperties.getAppName() + "/" + idGenerator.getFileKey(suffix);
        OSSClient client = getClient();
        try {
            Date expirationTime = DateUtils.addMinutes(new Date(), 30);
            GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(aliyunProperties.getBucketName(), fileKey);
            //设置过期时间
            generatePresignedUrlRequest.setExpiration(expirationTime);
            //设置Content-Type
            generatePresignedUrlRequest.setContentType(contentType);
            generatePresignedUrlRequest.setMethod(HttpMethod.PUT);
            URL url = client.generatePresignedUrl(generatePresignedUrlRequest);
            rtnMap.put("fileKey", fileKey);
            rtnMap.put("uploadUrl", getOSSUrl() + url.getFile());
            rtnMap.put("downloadUrl", generateDownloadUrl(fileKey));
            return rtnMap;
        } finally {
            closeClient(client);
        }
    }

    /**
     * 生成用于POST文件上传的签名表单内容
     *
     * @param suffix
     * @return
     */
    public Map<String, Object> generateUploadFormData(String suffix) {

        String fileKey = siteProperties.getAppName() + "/" + idGenerator.getFileKey(suffix);
        OSSClient client = getClient();
        try {
            Date expirationTime = DateUtils.addMinutes(new Date(), 30);
            PolicyConditions conditions = new PolicyConditions();
            String successActionStatus = "200";
            conditions.addConditionItem(PolicyConditions.COND_KEY, fileKey);
            conditions.addConditionItem(PolicyConditions.COND_SUCCESS_ACTION_STATUS, successActionStatus);
            String postPolicy = client.generatePostPolicy(expirationTime, conditions);
            String encodedPolicy = BinaryUtil.toBase64String(postPolicy.getBytes("utf-8"));
            String postSignature = client.calculatePostSignature(postPolicy);

            Map<String, String> formData = new HashMap<>();
            formData.put("key", fileKey);
            formData.put("policy", encodedPolicy);
            formData.put("OSSAccessKeyId", aliyunProperties.getAccessId());
            formData.put("success_action_status", successActionStatus);
            formData.put("signature", postSignature);

            Map<String, Object> rtnMap = new HashMap<>();
            rtnMap.put("formData", formData);
            rtnMap.put("fileKey", fileKey);
            rtnMap.put("uploadUrl", getOSSUrl());
            rtnMap.put("downloadUrl", generateDownloadUrl(fileKey));
            return rtnMap;
        } catch (UnsupportedEncodingException e) {
            return null;
        } finally {
            closeClient(client);
        }
    }

    /**
     * 生成带签名的文件下载链接
     *
     * @param fileKey
     * @return
     */
    public String generateDownloadUrl(String fileKey) {
        if (StringUtils.isBlank(fileKey)) {
            return "";
        }
        OSSClient client = getClient();
        try {
            Date expirationTime = DateUtils.addMinutes(new Date(), 30);
            URL url = client.generatePresignedUrl(aliyunProperties.getBucketName(), fileKey, expirationTime, HttpMethod.GET);
            return getOSSUrl() + url.getFile();
        } finally {
            closeClient(client);
        }
    }

    /**
     * 生成带签名的文件下载链接
     *
     * @param fileKey
     * @param process 图片处理访问规则
     * @return
     */
    public String generateDownloadUrl(String fileKey, String process) {
        if (StringUtils.isBlank(fileKey)) {
            return "";
        }
        OSSClient client = getClient();
        try {
            Date expirationTime = DateUtils.addMinutes(new Date(), 30);
            GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(aliyunProperties.getBucketName(), fileKey, HttpMethod.GET);
            request.setExpiration(expirationTime);
            request.setProcess(process);
            URL url = client.generatePresignedUrl(request);
            return getOSSUrl() + url.getFile();
        } finally {
            closeClient(client);
        }
    }


    /**
     * 传入以，分割的fileKey 获取listUrl
     *
     * @param imageStr
     * @return
     */
    public List<AttachmentInfo> string2UrlList(String imageStr) {
        List<AttachmentInfo> imageList = new ArrayList<>();
        if (StringUtils.isNotEmpty(imageStr)) {
            String[] ids = imageStr.split(",");
            for (String fileKey : ids) {
                String url = this.generateDownloadUrl(fileKey);
                imageList.add(new AttachmentInfo(fileKey, url));
            }
        }
        return imageList;
    }

    /**
     * 传入fileLit  获取已 ， 分割的fileKey字符串
     *
     * @param imageList
     * @return
     */
    public String urlList2FileKey(List<AttachmentInfo> imageList) {
        String imageFileKeys = "";
        if (CollectionUtils.isNotEmpty(imageList)) {
            List<String> list = imageList.stream().map(AttachmentInfo::getFileKey).collect(Collectors.toList());
            imageFileKeys = StringUtils.join(list, ",");
        }
        return imageFileKeys;
    }


}
